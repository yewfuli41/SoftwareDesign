package tutoring.persistence;

import tutoring.domain.*;
import java.util.List;

public class RepoMain {
    private final IUserRepo userRepo;
    private final ISubjectRepo subjectRepo;
    private final ITutoringSessionRepo sessionRepo;
    private final IBookingRepo bookingRepo;

    public RepoMain() {
        this.userRepo = new FileUserRepo();
        this.subjectRepo = new FileSubjectRepo();

        // 1. init sessionRepo (needs userRepo + subjectRepo)
        this.sessionRepo = new FileTutoringSessionRepo(userRepo, subjectRepo);

        // 2. init bookingRepo (needs userRepo + sessionRepo)
        this.bookingRepo = new FileBookingRepo(userRepo, sessionRepo);

        // 3. link bookings back to sessions
        linkSessionsAndBookings();
    }

    private void linkSessionsAndBookings() {
        List<TutoringSession> sessions = sessionRepo.loadAllSessions();
        for (TutoringSession session : sessions) {
            List<Booking> bookings = bookingRepo.findBySessionId(session.getTutoringSessionID());
            session.setBookingList(new java.util.ArrayList<>(bookings));
        }
    }

    public IUserRepo getUserRepo() { return userRepo; }
    public ISubjectRepo getSubjectRepo() { return subjectRepo; }
    public ITutoringSessionRepo getSessionRepo() { return sessionRepo; }
    public IBookingRepo getBookingRepo() { return bookingRepo; }
}
