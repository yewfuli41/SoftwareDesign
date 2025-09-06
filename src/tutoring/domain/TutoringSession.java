package tutoring.domain;
import java.util.ArrayList;
public class TutoringSession {
	private int tutoringSessionID;
	private ArrayList<Booking> bookingList;
	private int availableCapacity;
	private int capacity;
	private int duration;
	private String timeslot;
	private String date;
	private Subject subject;
	private Tutor tutor;
	public TutoringSession(int tutoringSessionID, int availableCapacity, int capacity,
			int duration, String timeslot, String date, Subject subject, Tutor tutor) {
		this.tutoringSessionID = tutoringSessionID;
		this.bookingList = new ArrayList<Booking>();
		this.availableCapacity = availableCapacity;
		this.capacity = capacity;
		this.duration = duration;
		this.timeslot = timeslot;
		this.date = date;
		this.subject = subject;
		this.tutor = tutor;
	}
	public int getTutoringSessionID() {
		return tutoringSessionID;
	}
	public void setTutoringSessionID(int tutoringSessionID) {
		this.tutoringSessionID = tutoringSessionID;
	}
	public ArrayList<Booking> getBookingList() {
		return bookingList;
	}
	public void setBookingList(ArrayList<Booking> bookingList) {
		this.bookingList = bookingList;
	}
	public int getAvailableCapacity() {
		return availableCapacity;
	}
	public void setAvailableCapacity(int availableCapacity) {
		this.availableCapacity = availableCapacity;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Tutor getTutor() {
		return tutor;
	}
	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}
	
}
