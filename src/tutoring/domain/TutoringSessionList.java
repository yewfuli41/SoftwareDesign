package tutoring.domain;
import java.util.ArrayList;

public class TutoringSessionList implements ITutoringSession{
	private ArrayList<TutoringSession> tutoringSessions;

	public TutoringSessionList() {
		this.tutoringSessions = new ArrayList<TutoringSession>();
	}
	
	// Create a tutoring session
    public TutoringSession createTutoringSession(Subject subject, String date, int duration) 
    {
        TutoringSession session = new TutoringSession(subject, date, duration);
        tutoringSessions.add(session);
        return session;
    }

    // Edit a tutoring session (example: change date or duration)
    public void editSession(TutoringSession session) 
    {
    	for (int i = 0; i < tutoringSessions.size(); i++) 
    	{
            if (tutoringSessions.get(i).equals(session)) 
            {
                tutoringSessions.set(i, session); // replace with updated version
                break;
            }
        }
    }

    // Cancel a tutoring session
    @Override
    public void cancelSession(TutoringSession session) 
    {
        tutoringSessions.remove(session);
    }

    // Get sessions by keyword(searching subject name)
    @Override
    public ArrayList<TutoringSession> getSessions(String keyword) 
    {
        ArrayList<TutoringSession> results = new ArrayList<>();
        for (TutoringSession session : tutoringSessions) {
            if (session.getSubject().getSubjectName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(session);
            }
        }
        return results;
    }
    
    public ArrayList<TutoringSession> getAllSessions() {
        return tutoringSessions;
    }
}
