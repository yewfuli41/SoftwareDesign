
/*
 Author:
 Class description: 
 Rules: 
 	1. BookingData.txt: bookingId,sessionId,studentId,bookingDate,status
*/
package tutoring.domain;

import java.time.LocalDate;

public class Booking {
    private int bookingID;
    private TutoringSession tutoringSession; // reference to the session
    private LocalDate bookingDate;
    private String status; // CONFIRMED, CANCELLED, PENDING
    private Student student; // reference to the student

    public Booking(int bookingID, TutoringSession tutoringSession,
                   LocalDate bookingDate, String status, Student student) {
        this.bookingID = bookingID;
        // 
        this.tutoringSession = tutoringSession;
        this.bookingDate = bookingDate;
        this.status = status;
        this.student = student;
    }

    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    // refer to TutoringSession.java for retrieving details
    public TutoringSession getTutoringSession() { return tutoringSession; }
    public void setTutoringSession(TutoringSession tutoringSession) { this.tutoringSession = tutoringSession; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    // do not change user(student/tutor) data through booking, leave for User.java and UserList.java
    public String getStudentName() { return student.getName(); }

}

