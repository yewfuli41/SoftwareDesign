package tutoring.domain;

import java.util.List;

public interface ITutoringSessionRepo {
    List<TutoringSession> loadAllSessions();
    void saveAllSessions(List<TutoringSession> sessions);
    TutoringSession findById(int id);
    void addSession(TutoringSession session);
    void updateSession(TutoringSession session);
    void deleteSession(int id);
}
