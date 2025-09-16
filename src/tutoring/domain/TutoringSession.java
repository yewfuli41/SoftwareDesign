
/*
 Author: LSun
 Class Description: tutoring will be stored in ArrayList
 Rules: 
 	1. Update values in ArrayList bookingList, then write to the data files
 	2. If date, startTime, duration were to change, 
 	ArrayList bookingList should be updated with the new date, startTime, duration.
 	3. Files to read: TutoringSessionData.txt, BookingData.txt, UserData.txt
 	TutoringSessionData.txt: tutoringSessionId (PK), tutorId (FK), date (dd-mm-yyyy), startTime (HH:mm), endTime (HH:mm), subjectId, duration (int minutes), capacity, availableCapacity 
 	4. fetch ArrayList bookingList data with the tutoringSessionId in the BookingList.txt
*/
package tutoring.domain;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class TutoringSession {
	private int tutoringSessionID; // 
	private ArrayList<Booking> bookingList;
	private int availableCapacity;
	private int capacity;
	private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
	private Subject subject; // When creating session, use subjectId as referral
	private int duration; // in minutes
	private Tutor tutor; // When creating session, use tutorId as a referral
	
	public TutoringSession(int tutoringSessionID, int availableCapacity, int capacity,
	        LocalDate date, LocalTime startTime, int duration,
	        Subject subject, Tutor tutor) {

	    if (duration <= 0) {
	        throw new IllegalArgumentException("Duration must be positive");
	    }

	    this.date = date;
	    this.startTime = startTime;
	    this.duration = duration;

	    LocalTime calculatedEnd = this.startTime.plusMinutes(this.duration);
	    if (!calculatedEnd.isAfter(this.startTime)) {
	        throw new IllegalArgumentException("Session cannot extend into the next day");
	    }
	    this.endTime = calculatedEnd;

	    this.tutoringSessionID = tutoringSessionID;
	    this.bookingList = new ArrayList<>();
	    this.capacity = capacity;
	    this.availableCapacity = capacity - bookingList.size();

	    this.subject = subject;
	    this.tutor = tutor;
	}

	
	public int getTutoringSessionID() { return tutoringSessionID;}
	// Does not permit the changing of TutoringSessionId
	// public void setTutoringSessionID(int tutoringSessionID) { this.tutoringSessionID = tutoringSessionID;}
	
	public ArrayList<Booking> getBookingList() { return bookingList;}
	// If bookingList is updated
	public void setBookingList(ArrayList<Booking> bookingList) { this.bookingList = bookingList;}
	
	public int getAvailableCapacity() { return availableCapacity;}
	// used at Booking, add/delete by 1, cannot make changes if available capacity is zero
	// if add booking, getAvailableCapacity()-1
	// if cancel booking, getAvailableCapacity()+1
	public void setAvailableCapacity(int availableCapacity) {
		if (availableCapacity<0) {	throw new IllegalArgumentException("Cannot set available capacity less than zero");}
		if (availableCapacity>capacity) { throw new IllegalArgumentException("Available capacity cannot excel total capacity");}
		this.availableCapacity = availableCapacity;
	}
	
	public int getCapacity() { return capacity;}
	
	// If capacity is changed, availableCapacity should also change
	public void setCapacity(int newCapacity) {
	    int takenSlots = this.capacity - this.availableCapacity;

	    // prevent shrinking below taken slots
	    if (newCapacity < takenSlots) { throw new IllegalArgumentException("Cannot set capacity lower than the number of already booked slots (" + takenSlots + ")");}
	    
	    // adjust available capacity proportionally
	    this.availableCapacity += (newCapacity - this.capacity);
	    this.capacity = newCapacity;
	}

	
	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date;}
	
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { 
    	LocalTime calculatedEnd = startTime.plusMinutes(this.duration);
        if (!calculatedEnd.isAfter(startTime)) {
            throw new IllegalArgumentException("Session cannot extend into the next day");
        }
        
    	this.startTime=startTime;
    	this.endTime=calculatedEnd;
    }
    public LocalTime getEndTime() { return endTime; }
    
	public int getDuration() { return duration;}
	// If duration is changed, recalculate startTime, replace original endTime with new calculated endTime
	public void setDuration(int duration) { 
		LocalTime calculatedEnd = startTime.plusMinutes(duration);
	    // If the calculated end time is before or equal to the start time,
	    // it means it wrapped into the next day.
	    if (!calculatedEnd.isAfter(startTime)) {
	        throw new IllegalArgumentException("Session cannot extend into the next day");
	    }
	    
		this.duration = duration;		
		this.endTime = calculatedEnd;
	}
	
	/// Returns the Subject object of the TutoringSession
	public Subject getSubject() { return subject;}
	// If subject is changed, must refer ArrayList<Subject> subjectList for available subject
	// If writing to TutoringSessionData.txt, use subjectId
	public void setSubject(Subject subject) { this.subject= subject;}
	
	// No user have the permission to change the tutor of created session
	public Tutor getTutor() { return tutor;}
	
	
	
}

