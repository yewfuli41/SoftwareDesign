package tutoring.domain;

public class Booking {
	private int bookingID;
	private String userName;
	private String contactNumber;
	private String IC_passportNumber;
	private TutoringSession tutoringSession;
	private String bookingDate;
	private String status;
	private Student student;
	public Booking(int bookingID, String userName, String contactNumber, String iC_passportNumber,
			TutoringSession tutoringSession, String bookingDate, String status, Student student) {
		this.bookingID = bookingID;
		this.userName = userName;
		this.contactNumber = contactNumber;
		IC_passportNumber = iC_passportNumber;
		this.tutoringSession = tutoringSession;
		this.bookingDate = bookingDate;
		this.status = status;
		this.student = student;
	}
	public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getIC_passportNumber() {
		return IC_passportNumber;
	}
	public void setIC_passportNumber(String iC_passportNumber) {
		IC_passportNumber = iC_passportNumber;
	}
	public TutoringSession getTutoringSession() {
		return tutoringSession;
	}
	public void setTutoringSession(TutoringSession tutoringSession) {
		this.tutoringSession = tutoringSession;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
}
