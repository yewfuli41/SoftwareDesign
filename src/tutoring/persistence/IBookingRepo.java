package tutoring.persistence;

import java.util.List;
import tutoring.domain.*;
public interface IBookingRepo {
    List<Booking> loadAllBookings();
    Booking findById(int bookingId);
    void addBooking(Booking booking);
    void updateBooking(Booking booking);
    void deleteBooking(int bookingId);

    // return a the booking list related to that session using sessionId
    List<Booking> findBySessionId(int sessionId);
	List<Booking> findByStudentId(int studentId);
}
