
package tutoring.domain;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import tutoring.persistence.IBookingRepo;
import tutoring.persistence.RepoMain;

public class BookingList implements IBooking {
	RepoMain factory;
    private List<Booking> bookings;
    private final IBookingRepo bookingRepo;
    public BookingList() {
    	this.factory = new RepoMain();
        this.bookingRepo = factory.getBookingRepo();
        bookings = bookingRepo.loadAllBookings();
    }

    /// Book a tutoring session
    @Override
    public Booking bookingSession(TutoringSession session)
    {
    	/*
    	 * Student student = session.getStudent(); // assume session knows the student
        int bookingID = bookings.size() + 1; // auto-generate simple ID
        String date = java.time.LocalDate.now().toString(); // today
        
        Booking booking = new Booking(
                bookingID,
                student.getName(),
                student.getContactNumber(),
                student.getIC_passportNumber(),
                session,
                date,
                "Pending",
                student
        );
        bookings.add(booking);
        return booking;
        */
    	 return null;
    }

    // Confirm a booking
    @Override
    public void confirmBooking(Booking booking) 
    {
        if (bookings.contains(booking)) 
        {
            booking.setStatus("Confirmed");
        }
        
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

