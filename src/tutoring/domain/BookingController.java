package tutoring.domain;

import java.util.ArrayList;

public class BookingController {
	private IBooking bookingList;
	private ArrayList<Booking> studentBookings;
	public BookingController() {
		bookingList = new BookingList();
		studentBookings = new ArrayList<>();
	}
	
	public void filterConfirmedBookings(){
		studentBookings = new ArrayList<>(studentBookings.stream()
                .filter(booking -> booking.getStatus().toLowerCase().equals("confirmed"))
                .toList());
	}
	public void filterBookingsBySubject(String subject){
    	studentBookings = new ArrayList<>(studentBookings.stream()
                .filter(booking -> booking.getTutoringSession().getSubject().getSubjectName().toLowerCase().equals(subject))
                .toList());
    }
    public void filterBookingsByDate(String month){
    	studentBookings = new ArrayList<>(studentBookings.stream()
                .filter(booking -> booking.getBookingDate().getMonth().toString().equalsIgnoreCase(month))
                .toList());
    }
    public void filterBookingsByTutor(String tutor){
    	studentBookings = new ArrayList<>(studentBookings.stream()
                .filter(booking -> booking.getTutoringSession().getTutor().getName().toLowerCase().equals(tutor))
                .toList());
    }
    public ArrayList<Booking> getStudentBookings() {
		return studentBookings;
	}
    public void setStudentBookings(Student student) {
		this.studentBookings = bookingList.getBookings(student);
	}
	public double calculateTotalHoursSessionsAttended() {
    	double totalHoursSessionsAttended = 0;
    	for(int i=0;i<studentBookings.size();i++) {
    		int durationSession = studentBookings.get(i).getTutoringSession().getDuration();
    		totalHoursSessionsAttended += durationSession;
    	}
    	return totalHoursSessionsAttended/60;
    }
}
