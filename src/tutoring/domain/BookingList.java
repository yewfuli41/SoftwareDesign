
package tutoring.domain;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.ArrayList;

import tutoring.persistence.IBookingRepo;
import tutoring.persistence.ITutoringSessionRepo;
import tutoring.persistence.RepoMain;

public class BookingList implements IBooking {
	RepoMain factory;
    private List<Booking> bookings;
    private final IBookingRepo bookingRepo;
    private final ITutoringSessionRepo tutoringSessionRepo;
    private final ITutoringSession tutoringSessionList;
    public BookingList() {
    	this.factory = new RepoMain();
        this.bookingRepo = factory.getBookingRepo();
        this.tutoringSessionRepo = factory.getSessionRepo();
        this.tutoringSessionList = new TutoringSessionList();
        bookings = bookingRepo.loadAllBookings();
    }

    public int generateNextBookingId() {
    	bookings = bookingRepo.loadAllBookings();
        return bookings.stream()
                       .mapToInt(Booking::getBookingID)
                       .max()
                       .orElse(0) + 1;
    }
    
    // Book a tutoring session
    @Override
    public Booking bookingSession(int selectedSessionId, Student student) {
        // reload latest bookings to avoid stale data
        bookings = bookingRepo.loadAllBookings();
        TutoringSession selectedSession = tutoringSessionList.getSessionById(selectedSessionId);
        if (selectedSession == null) {
            throw new IllegalArgumentException("Invalid session ID selected!");
        }

        // Prevent double booking
        boolean alreadyBooked = bookings.stream()
            .anyMatch(b -> b.getTutoringSession().getTutoringSessionID() == selectedSession.getTutoringSessionID()
                    && b.getStudent().getUserID() == student.getUserID());
        if (alreadyBooked) {
            throw new IllegalArgumentException("Student already booked this session");
        }

        // Check capacity
        if (selectedSession.getAvailableCapacity() <= 0) {
            throw new IllegalArgumentException("Session is full");
        }

        // Create new booking
        LocalDate bookingDate = LocalDate.now();
        Booking booking = new Booking(
            generateNextBookingId(),
            selectedSession,
            bookingDate,
            "Pending",
            student
        );

        // persist
        bookingRepo.addBooking(booking);

        // update domain state
        student.getSessionsAttended().add(booking);
        bookings.add(booking);

        return booking;
    }

    // Confirm a booking
    @Override
    public void confirmBooking(Booking booking) {
        if (!bookings.contains(booking)) {
            throw new IllegalArgumentException("Booking not found");
        }

        // enforce capacity check
        TutoringSession session = booking.getTutoringSession();
        if (session.getAvailableCapacity() <= 0) {
            throw new IllegalStateException("No available capacity left");
        }

        booking.setStatus("Confirmed");
        session.setAvailableCapacity(session.getAvailableCapacity() - 1);

        // data persistence, update BookingData.txt and TutoringSessionData.txt for the available capacity change
        bookingRepo.updateBooking(booking);
        tutoringSessionRepo.updateSession(session);
    }

    // Edit a booking
    @Override
    public void editBookings(Booking booking) 
    {
        for (int i = 0; i < bookings.size(); i++) 
        {
            if (bookings.get(i).getBookingID() == booking.getBookingID()) {
                bookings.set(i, booking); // replace with updated booking
                return;
            }
        }
    }

    // Delete a booking
    @Override
    public void deleteBookings(Booking booking) 
    {
        bookings.remove(booking);
    }

    // Retrieve bookings for a specific student
    @Override
    public ArrayList<Booking> getBookings(Student student) 
    {
        ArrayList<Booking> studentBookings = new ArrayList<>();
        for (Booking booking : bookings) 
        {
            if (booking.getStudent().getUserID()==student.getUserID())
            {
                studentBookings.add(booking);
            }
        }
        return studentBookings;
    }
    
    public List<Booking> getAllBookings() {
        return bookings;
    }
    
    
}

