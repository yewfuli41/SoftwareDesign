package tutoring.domain;
import java.util.ArrayList;
import java.util.Date;
public interface ITutoringSession {
    // Create a tutoring session
    TutoringSession createTutoringSession(Subject subject, Date date, int duration);

    // Edit a tutoring session
    void editSession(TutoringSession session);

    // Cancel a tutoring session
    void cancelSession(TutoringSession session);

    // Get sessions by keyword
    ArrayList<TutoringSession> getSessions(String keyword);
}
