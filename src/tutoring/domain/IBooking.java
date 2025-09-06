
package tutoring.domain;

import java.util.ArrayList;

public interface IBooking {
    
    // Book a tutoring session
    Booking bookingSession(TutoringSession session);
    
    // Confirm a booking
    void confirmBooking(Booking booking);
    
    // Delete a booking
    void deleteBookings(Booking booking);
    
    // Edit a booking
    void editBookings(Booking booking);
    
    // Retrieve bookings for a specific student
    ArrayList<Booking> getBookings(Student student);
}

