package tutoring.domain;

import java.util.ArrayList;

public class BookingController {
	private IBooking bookingList;
	private ITutoringSession tutoringSessionList;
	private ArrayList<Booking> studentBookings;

	public BookingController() {
		bookingList = new BookingList();
		studentBookings = new ArrayList<>();
		tutoringSessionList = new TutoringSessionList();
	}

	public Booking createBooking(int sessionID, Student student) {
		TutoringSession selectedSession = tutoringSessionList.getSessionById(sessionID);
		if (selectedSession == null) {
			throw new IllegalArgumentException("Invalid session ID selected!");
		}

		// Prevent double booking
		boolean alreadyBooked = bookingList.getAllBookings().stream()
				.anyMatch(b -> b.getTutoringSession().getTutoringSessionID() == selectedSession.getTutoringSessionID()
						&& b.getStudent().getUserID() == student.getUserID());
		if (alreadyBooked) {
			throw new IllegalArgumentException("Student already booked this session");
		}

		// Check capacity
		if (selectedSession.getAvailableCapacity() <= 0) {
			throw new IllegalArgumentException("Session is full");
		}
		Booking newBooking = bookingList.bookingSession(selectedSession,student);
		return newBooking;
	}

	public void checkHasStudentBookings() {
		if (studentBookings.isEmpty()) {
			throw new IllegalArgumentException("No bookings found");
		}
	}

	public void confirmBooking(Booking selectedBooking) {
		bookingList.confirmBooking(selectedBooking);
	}

	public void editBooking(Booking selectedBooking, TutoringSession newSession, TutoringSession currentSession) {
	    // Fetch the latest sessions from repo to avoid stale references
	    TutoringSession latestCurrent = tutoringSessionList.getSessionById(currentSession.getTutoringSessionID());
	    TutoringSession latestNew = tutoringSessionList.getSessionById(newSession.getTutoringSessionID());

	    if(latestNew.getTutoringSessionID() != latestCurrent.getTutoringSessionID()) {
	        latestNew.setAvailableCapacity(latestNew.getAvailableCapacity() - 1);
	        latestCurrent.setAvailableCapacity(latestCurrent.getAvailableCapacity() + 1);

	        tutoringSessionList.editSessionRepo(latestCurrent);
	        tutoringSessionList.editSessionRepo(latestNew);

	        // Update the booking AFTER updating capacities
	        selectedBooking.setTutoringSession(latestNew);
	        bookingList.editBookings(selectedBooking);
	    }
	}

	public void cancelBooking(Booking selectedBooking) {
		bookingList.deleteBookings(selectedBooking);
		selectedBooking.getTutoringSession()
				.setAvailableCapacity(selectedBooking.getTutoringSession().getAvailableCapacity() + 1);
		tutoringSessionList.editSessionRepo(selectedBooking.getTutoringSession());
	}

	public void filterConfirmedBookings() {
		studentBookings = new ArrayList<>(studentBookings.stream()
				.filter(booking -> booking.getStatus().toLowerCase().equals("confirmed")).toList());
	}

	public void filterBookingsBySubject(String subject) {
		studentBookings = new ArrayList<>(studentBookings.stream().filter(
				booking -> booking.getTutoringSession().getSubject().getSubjectName().toLowerCase().equals(subject))
				.toList());
	}

	public void filterBookingsByDate(String month) {
		studentBookings = new ArrayList<>(studentBookings.stream()
				.filter(booking -> booking.getBookingDate().getMonth().toString().equalsIgnoreCase(month)).toList());
	}

	public void filterBookingsByTutor(String tutor) {
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
		for (int i = 0; i < studentBookings.size(); i++) {
			int durationSession = studentBookings.get(i).getTutoringSession().getDuration();
			totalHoursSessionsAttended += durationSession;
		}
		return totalHoursSessionsAttended / 60;
	}
}
