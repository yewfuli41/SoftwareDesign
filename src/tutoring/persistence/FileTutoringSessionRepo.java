/*
 Author: LSun
 Class description: for storing/retrieving TutoringSession Data from TutoringSessionData.txt, 
 					includes method for CRUD, loading data, saving/update data etc...
*/
package tutoring.persistence;

import tutoring.domain.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileTutoringSessionRepo implements ITutoringSessionRepo {
    //private String sessionFile = "TutoringSessionData.txt";
	private String sessionFile;

    private final IUserRepo userRepo;
    private final ISubjectRepo subjectRepo;

    public FileTutoringSessionRepo(IUserRepo userRepo, ISubjectRepo subjectRepo, String sessionFile) {
        this.userRepo = userRepo;
        this.subjectRepo = subjectRepo;
        this.sessionFile = sessionFile;
    }

    @Override
    public List<TutoringSession> loadAllSessions() {
        List<TutoringSession> sessions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sessionFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: id,tutorId,date,startTime,subjectId,duration,capacity,available
                String[] parts = line.split(",");
                
                if (line.trim().isEmpty()) continue; // skip blank lines
                if (parts.length < 8) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }
                
                int id = Integer.parseInt(parts[0]);
                int tutorId = Integer.parseInt(parts[1]);
                LocalDate date = LocalDate.parse(parts[2], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                LocalTime start = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
                int subjectId = Integer.parseInt(parts[4]);
                int duration = Integer.parseInt(parts[5]);
                int capacity = Integer.parseInt(parts[6]);
                int available = Integer.parseInt(parts[7]);

                Tutor tutor = userRepo.findTutorById(tutorId);
                Subject subject = subjectRepo.findById(subjectId);

                
                if (tutor == null || subject == null) {
                    System.err.println("Skipping session " + id + ": missing tutor/subject");
                    continue;
                }

                TutoringSession session = new TutoringSession(
                        id, available, capacity,
                        date.toString(), start.toString(),
                        duration, subject, tutor
                );

                sessions.add(session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    @Override
    public void saveAllSessions(List<TutoringSession> sessions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(sessionFile))) {
            for (TutoringSession s : sessions) {
                bw.write(s.getTutoringSessionID() + "," +
                         s.getTutor().getUserID() + "," +
                         s.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "," +
                         s.getStartTime() + "," +
                         s.getSubject().getSubjectID() + "," +
                         s.getDuration() + "," +
                         s.getCapacity() + "," +
                         s.getAvailableCapacity());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CRUD
    @Override
    public TutoringSession findById(int id) {
        return loadAllSessions().stream()
                .filter(s -> s.getTutoringSessionID() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addSession(TutoringSession session) {
        List<TutoringSession> sessions = loadAllSessions();
        sessions.add(session);
        saveAllSessions(sessions);
    }

    @Override
    public void updateSession(TutoringSession updated) {
        List<TutoringSession> sessions = loadAllSessions();
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getTutoringSessionID() == updated.getTutoringSessionID()) {
                sessions.set(i, updated);
                break;
            }
        }
        saveAllSessions(sessions);
    }

    @Override
    public void deleteSession(int id) {
        List<TutoringSession> sessions = loadAllSessions();
        sessions.removeIf(s -> s.getTutoringSessionID() == id);
        saveAllSessions(sessions);
    }
}
