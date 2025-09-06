/*
 Author: Lek Sun
 Class Description: CRUD methods contract for TutoringApp and TutoringSessionList to use
 Rules: Date Format dd-mm-yyyy
 		Time Format: HH:mm
*/

package tutoring.domain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITutoringSession {
	public int generateNextSessionId();
	public void editSession(Tutor tutor, int sessionId, LocalDate date, LocalTime startTime,int duration, int capacity);
	public void cancelSession(Tutor tutor, int sessionId);
	public TutoringSession getSessionById(int sessionId); // Returns session of that user account
	public List<TutoringSession> getAllSessions();
	public List<TutoringSession> getSessionsByTutor(int tutorId);
	public TutoringSession createTutoringSession(Tutor tutor, Subject subject, LocalDate date, LocalTime startTime, int duration, int capacity);
}
