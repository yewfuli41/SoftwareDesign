package tutoring.domain;

import tutoring.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TutoringSessionList implements ITutoringSession {
    private final ITutoringSessionRepo sessionRepo;
    private final ISubjectRepo subjectRepo;

    public TutoringSessionList(ITutoringSessionRepo sessionRepo, ISubjectRepo subjectRepo) {
        this.sessionRepo = sessionRepo;
        this.subjectRepo = subjectRepo;
    }

    public int generateNextSessionId() {
        List<TutoringSession> sessions = sessionRepo.loadAllSessions();
        return sessions.stream()
                       .mapToInt(TutoringSession::getTutoringSessionID)
                       .max()
                       .orElse(0) + 1;
    }
    
    @Override
    public void createTutoringSession(Tutor tutor, int subjectId, String dateStr, String timeStr, int duration, int capacity) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime start = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));

        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        if (duration <= 0) throw new IllegalArgumentException("Duration must be positive");
        if (date.isBefore(LocalDate.now())) throw new IllegalArgumentException("Session date cannot be in the past");

        Subject subject = subjectRepo.findById(subjectId);
        if (subject == null) throw new IllegalArgumentException("Invalid subject ID");

        int sessionId = generateNextSessionId();
        TutoringSession session = new TutoringSession(
                sessionId, capacity, capacity,
                date.toString(), start.toString(),
                duration, subject, tutor
        );

        sessionRepo.addSession(session);
    }

    @Override
    public void editSession(Tutor tutor, int sessionId, String dateStr, String timeStr, int duration, int capacity) {
        TutoringSession existing = sessionRepo.findById(sessionId);
        if (existing == null) throw new IllegalArgumentException("Session not found");
        if (existing.getTutor().getUserID() != tutor.getUserID())
            throw new IllegalArgumentException("You can only edit your own sessions");

        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime start = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));

        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        if (duration <= 0) throw new IllegalArgumentException("Duration must be positive");

        TutoringSession updated = new TutoringSession(
                existing.getTutoringSessionID(), existing.getAvailableCapacity(), capacity,
                date.toString(), start.toString(),
                duration, existing.getSubject(), tutor
        );

        sessionRepo.updateSession(updated);
    }

    @Override
    public void cancelSession(Tutor tutor, int sessionId) {
        TutoringSession existing = sessionRepo.findById(sessionId);
        if (existing == null) throw new IllegalArgumentException("Session not found");
        if (existing.getTutor().getUserID() != tutor.getUserID())
            throw new IllegalArgumentException("You can only cancel your own sessions");

        sessionRepo.deleteSession(sessionId);
    }

    // Get session by ID
    @Override
    public TutoringSession getSessionById(int sessionId) {
        return sessionRepo.findById(sessionId);
    }

    // Get all sessions
    @Override
    public List<TutoringSession> getAllSessions() {
        return sessionRepo.loadAllSessions();
    }

    // Get sessions for a specific tutor
    @Override
    public List<TutoringSession> getSessionsByTutor(int tutorId) {
        return sessionRepo.loadAllSessions().stream()
                .filter(s -> s.getTutor().getUserID() == tutorId)
                .collect(Collectors.toList());
    }

}
