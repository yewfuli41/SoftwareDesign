package tutoring.app;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import tutoring.domain.*;
import tutoring.persistence.*;
public class TutoringSessionApp {
	private Scanner scanner;
	private IUser userList;
	private ITutoringSession tutoringSessionList;
	private List<TutoringSession> sessions;
	public TutoringSessionApp() {
		scanner = new Scanner(System.in);
		userList = new UserList();
		tutoringSessionList = new TutoringSessionList();
		sessions = tutoringSessionList.getAllSessions();
	}
	// ---------------- Tutor Menu ----------------
    public void tutorMenu(Tutor tutor) {
        int choice;
        do {
            System.out.println("\n--- Manage Sessions ---");
            System.out.println("1. Edit Session");
            System.out.println("2. Cancel Session");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editTutoringSession(tutor);
                    break;
                case 2:
                    cancelTutoringSession(tutor);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }
	public void createTutoringSession(Tutor tutor) {
		try {
			// Gather inputs
			System.out.print("Enter subject name: ");
	        String subjectName = scanner.nextLine();
	        Subject subject = new Subject(0, subjectName, "");

	        System.out.print("Enter session date (dd-MM-yyyy): ");
	        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

	        System.out.print("Enter start time (HH:mm): ");
	        LocalTime startTime = LocalTime.parse(scanner.nextLine().trim(), DateTimeFormatter.ofPattern("HH:mm"));

	        System.out.print("Enter duration (minutes): ");
	        int duration = scanner.nextInt();
	        scanner.nextLine();

	        System.out.print("Enter capacity: ");
	        int capacity = scanner.nextInt();
	        scanner.nextLine();

	        // Call domain service
	        TutoringSession session = tutoringSessionList.createTutoringSession(
	        		tutor, subject, date, startTime, duration, capacity
	        );

	        System.out.println("Session created: " + formatSession(session));

	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	}

	public void editTutoringSession(Tutor tutor) {
		displaySessions(tutor,sessions);

	    try {
	    	System.out.print("Enter session ID to edit: ");
	        int sessionId = Integer.parseInt(scanner.nextLine());

	        System.out.print("Enter new date (dd-MM-yyyy): ");
	        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

	        System.out.print("Enter new start time (HH:mm): ");
	        LocalTime startTime = LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

	        System.out.print("Enter new duration (minutes): ");
	        int duration = Integer.parseInt(scanner.nextLine());

	        System.out.print("Enter new capacity: ");
	        int capacity = Integer.parseInt(scanner.nextLine());

	        tutoringSessionList.editSession(tutor, sessionId, date, startTime, duration, capacity);

	        System.out.println("Session updated successfully!");
	    } catch (IllegalArgumentException e) {
	    	System.out.println("Error: " + e.getMessage());
	    } catch (Exception e) {
	    	System.out.println("Invalid input: " + e.getMessage());
	    }
	}

	public void cancelTutoringSession(Tutor tutor) {
		displaySessions(tutor,sessions);

	    System.out.print("Enter session ID to cancel: ");
	    int sessionId = Integer.parseInt(scanner.nextLine());
	        
	    // Check if session exists first hand
	    TutoringSession session = tutoringSessionList.getSessionById(sessionId);
	    if (session == null) {
	    	System.out.println("No session found with ID: " + sessionId);
	        return;
	    }

	    try {
	    	tutoringSessionList.cancelSession(tutor, sessionId);
	        System.out.println("Session canceled successfully!");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	public void searchTutoringSessions(User user) {
        System.out.println("\nSearch Tutoring Sessions");
        System.out.println("Filter by: ");
        System.out.println("1. Subject");
        System.out.println("2. Date");
        System.out.println("3. Tutor");
        System.out.println("4. Timeslot");
        System.out.println("5. Show All");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        List<TutoringSession> filtered = new ArrayList<>(sessions);

        switch (choice) {
            case 1: // Subject
                System.out.print("Enter subject name: ");
                String subjectName = scanner.nextLine().trim().toLowerCase();
                filtered = sessions.stream()
                    .filter(s -> s.getSubject().getSubjectName().toLowerCase().contains(subjectName))
                    .toList();
                break;

            case 2: // Date
                System.out.print("Enter date (yyyy-MM-dd): ");
                String dateStr = scanner.nextLine().trim();
                filtered = sessions.stream()
                    .filter(s -> s.getDate().toString().equals(dateStr))
                    .toList();
                break;

            case 3: // Tutor
                System.out.print("Enter tutor name: ");
                String tutorName = scanner.nextLine().trim().toLowerCase();
                filtered = sessions.stream()
                    .filter(s -> s.getTutor().getName().toLowerCase().contains(tutorName))
                    .toList();
                break;

            case 4: // Timeslot (start time only)
                System.out.print("Enter time (HH:mm): ");
                String timeStr = scanner.nextLine().trim();
                filtered = sessions.stream()
                    .filter(s -> s.getStartTime().toString().equals(timeStr))
                    .toList();
                break;

            case 5: // Show all
            default:
                break;
        }

        // Display results
        displaySessions(user,filtered);
    }
	
	public void displaySessions(User user,List<TutoringSession> sessions) {
	    System.out.println("\n--- Available Sessions ---");

	    if (userList.isStudent(user)) {
	    	  if (sessions.isEmpty()) {
	              System.out.println("No sessions found matching your criteria.");
	          } else {
	              System.out.println("\nSearch Results:");
	              for (TutoringSession s : sessions) {
	                  System.out.println(
	                      s.getTutoringSessionID() + " - " +
	                      s.getSubject().getSubjectName() +
	                      " by " + s.getTutor().getName() +
	                      " | Capacity: " + s.getCapacity() +
	                      " | Date: " + s.getDate() +
	                      " | Time: " + s.getStartTime() + " - " + s.getEndTime()
	                  );
	              }
	          }

	    } else if (userList.isTutor(user)) {
	        // Tutor: show only their own sessions
	    	tutoringSessionList.getSessionsByTutor(user.getUserID())
	               .forEach(s -> System.out.println(formatSession(s)));

	    } 
	}

	/**
	 * Format session details for display
	*/
	public String formatSession(TutoringSession s) {
	    return String.format(
	    		"ID: %d | Tutor: %s | Subject: %s | Date: %s | Start: %s | Duration: %d min | Capacity: %d/%d",
	    		s.getTutoringSessionID(),
	            s.getTutor().getName(),
	            s.getSubject().getSubjectName(),
	            s.getDate(),
	            s.getStartTime(),
	            s.getDuration(),
	            s.getAvailableCapacity(),
	            s.getCapacity()
	    );
	}	
}
