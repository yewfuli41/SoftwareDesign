package tutoring.persistence;

import java.util.List;
import tutoring.domain.*;
public interface ITutoringSessionRepo {
    List<TutoringSession> loadAllSessions();
    void saveAllSessions(List<TutoringSession> sessions);
    TutoringSession findById(int id);
    void addSession(TutoringSession session);
    void updateSession(TutoringSession session);
    void deleteSession(int id);
}
