/*
 Author: LSun
 Class Description: domain class for Tutor subclass of User
*/
package tutoring.domain;
import java.util.ArrayList;
public class Tutor extends User{
	private int tutorID;
	private ArrayList<TutoringSession> sessionsCreated;

	public Tutor(String name, String email, String password,int tutorID) {
		super(name, email, password);
		this.tutorID = tutorID;
		this.sessionsCreated = new ArrayList<TutoringSession>();
	}
	
	public int getUserID() { return tutorID;}
	public void setUserID(int tutorID) { this.tutorID = tutorID;}
	
	public ArrayList<TutoringSession> getSessionsCreated() { return sessionsCreated;}
	public void setSessionsCreated(ArrayList<TutoringSession> sessionsCreated) { 
		this.sessionsCreated = sessionsCreated;
	}
}
