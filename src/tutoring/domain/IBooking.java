
package tutoring.domain;

import java.util.ArrayList;
import java.util.List;

public interface IBooking {
    
    // Book a tutoring session
    Booking bookingSession(int selectedSessionId, Student student);
    
    // Confirm a booking
    void confirmBooking(Booking booking);
    
    // Delete a booking
    void deleteBookings(Booking booking);
    
    // Edit a booking
    void editBookings(Booking booking);
    
    // Retrieve bookings for a specific student
    ArrayList<Booking> getBookings(Student student);
    List<Booking> getAllBookings();
}

