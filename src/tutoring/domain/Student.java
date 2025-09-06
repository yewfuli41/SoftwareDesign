
package tutoring.domain;
import java.util.ArrayList;
public class Student extends User{
	private int studentID;
	private ArrayList<Booking> sessionsAttended;
	public Student(String name, String email, String password, int studentID) {
		super(name,email,password);
		this.studentID = studentID;
		this.sessionsAttended = new ArrayList<Booking>();
	}
	public int getUserID() {
		return studentID;
	}
	public void setUserID(int studentID) {
		this.studentID = studentID;
	}
	public ArrayList<Booking> getSessionsAttended() {
		return sessionsAttended;
	}
	public void setSessionsAttended(ArrayList<Booking> sessionsAttended) {
		this.sessionsAttended = sessionsAttended;
	}
}

