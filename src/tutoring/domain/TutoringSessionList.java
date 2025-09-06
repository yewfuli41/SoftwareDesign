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
    public TutoringSession createTutoringSession(Tutor tutor, Subject subject,
            LocalDate date, LocalTime startTime,
            int duration, int capacity) {
		// Check overlap
		List<TutoringSession> tutorSessions = sessionRepo.loadAllSessions().stream()
		.filter(s -> s.getTutor().getUserID() == tutor.getUserID())
		.filter(s -> s.getDate().equals(date))
		.toList();
		
		LocalTime endTime = startTime.plusMinutes(duration);
		
		for (TutoringSession s : tutorSessions) {
			LocalTime sEnd = s.getStartTime().plusMinutes(s.getDuration());
			boolean overlap = !(endTime.isBefore(s.getStartTime()) || startTime.isAfter(sEnd));
			if (overlap) {
			throw new IllegalArgumentException(
				"Overlap detected with session ID " + s.getTutoringSessionID());
			}
		}
		
		// Create new session
		TutoringSession session = new TutoringSession(
			generateNextSessionId(),
			capacity,
			capacity,
			date.toString(),
			startTime.toString(),
			duration,
			subject,
			tutor
		);
		
		sessionRepo.addSession(session);
		return session;
	}

    @Override
    public void editSession(Tutor tutor, int sessionId, LocalDate date, LocalTime startTime,int duration, int capacity) {
        TutoringSession existing = sessionRepo.findById(sessionId);
        if (existing == null) throw new IllegalArgumentException("Session not found");
        if (existing.getTutor().getUserID() != tutor.getUserID())
            throw new IllegalArgumentException("You can only edit your own sessions");

        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        if (duration <= 0) throw new IllegalArgumentException("Duration must be positive");

        // --- Overlap check ---
        LocalTime endTime = startTime.plusMinutes(duration);
        List<TutoringSession> tutorSessions = sessionRepo.loadAllSessions().stream()
                .filter(s -> s.getTutor().getUserID() == tutor.getUserID())
                .filter(s -> s.getDate().equals(date))
                .filter(s -> s.getTutoringSessionID() != sessionId) // exclude current session
                .toList();

        for (TutoringSession s : tutorSessions) {
            LocalTime sEnd = s.getStartTime().plusMinutes(s.getDuration());
            boolean overlap = !(endTime.isBefore(s.getStartTime()) || startTime.isAfter(sEnd));
            if (overlap) {
                throw new IllegalArgumentException(
                        "Overlap detected with session ID " + s.getTutoringSessionID());
            }
        }

        // --- Build updated session ---
        TutoringSession updated = new TutoringSession(
                existing.getTutoringSessionID(),
                existing.getAvailableCapacity(), // preserve current bookings
                capacity,
                date.toString(),
                startTime.toString(),
                duration,
                existing.getSubject(),
                tutor
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
