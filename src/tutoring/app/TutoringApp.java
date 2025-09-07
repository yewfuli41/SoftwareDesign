/*
 Author: Everyone
 Class description: Application layer, main ui, command line based
*/
package tutoring.app;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import tutoring.domain.*;
import tutoring.persistence.*;

public class TutoringApp {

	private ITutoringSession tutoringSessionList;
	private IBooking bookingList;
	private IUser userList;
	RepoMain factory;
	ITutoringSessionRepo sessionRepo;
	IBookingRepo bookingRepo;
	IUserRepo userRepo;
	ISubjectRepo subjectRepo;
    
	UserList ul;
//	
//	// load all sessions with full tutor+subject objects resolved
//	List<TutoringSession> sessions = sessionRepo.loadAllSessions();
//	// load booking list
//	List<Booking> bookings = bookingRepo.loadAllBookings();
//	// load all stored user to users ArrayList
//	List<Tutor> users = userRepo.findAllTutors();
//	List<Student> students = userRepo.findAllStudents();
	
    public TutoringApp() {
    	this.factory = new RepoMain();
    	this.sessionRepo = factory.getSessionRepo();
    	this.bookingRepo = factory.getBookingRepo();
    	this.userRepo = factory.getUserRepo();
    	this.subjectRepo = factory.getSubjectRepo();
        this.tutoringSessionList = new TutoringSessionList(sessionRepo, subjectRepo);
        this.ul = new UserList(userRepo);
        this.bookingList = new BookingList(bookingRepo);
    }

    public static void main(String[] args) {
        TutoringApp app = new TutoringApp();
        app.run();
    }

    // Main menu loop
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        String username="";
        String password="";
        User currentUser= null;

        do {
	        System.out.print("Enter Username (enter 0 to quit): ");
	        username = scanner.nextLine();
	        System.out.print("Enter Password (enter 0 to quit): ");
	        password = scanner.nextLine();
	        // After log in, return currentUser if successful 
	        currentUser = ul.login(username,password);
        } while(currentUser==null||username!="0"||password!="0");
        scanner.nextLine();
        
        // Check if the current user is Student or Tutor, display two different screen
        if (ul.isTutor(currentUser)) {
	        do {
	            System.out.println("\n===== Tutoring App Menu =====");
	            System.out.println("1. Create New Tutoring Sessions");
	            System.out.println("2. Manage Tutoring Sessions");
	            System.out.println("3. Change Profile");
	            System.out.println("0. Exit");
	            System.out.print("Enter choice: ");
	            choice = scanner.nextInt();
	            scanner.nextLine(); // consume newline
	
	            switch (choice) {
	                case 1:
	                    createTutoringSession(scanner,(Tutor) currentUser);
	                    break;
	                case 2:
	                	tutorMenu(scanner,(Tutor) currentUser);
	                    break;
	                case 3:
	                    //profileManage(scanner, currentUser);
	                    break;
	                case 0:
	                    System.out.println("Exiting Tutoring App...");
	                    break;
	                default:
	                    System.out.println("Invalid choice. Try again!");
	            }
	        } while (choice != 0);
	
	        scanner.close();
	    } else if(ul.isStudent(currentUser)){
	    	do {
	            System.out.println("\n===== Tutoring App Menu =====");
	            System.out.println("1. Book a Session");
	            System.out.println("2. Manage Bookings");
	            System.out.println("3. Change Profile");
	            System.out.println("0. Exit");
	            System.out.print("Enter choice: ");
	            choice = scanner.nextInt();
	            scanner.nextLine(); // consume newline
	
	            switch (choice) {
	                case 1:
	                    studentBookSession(scanner,currentUser);
	                    break;
	                case 2:
	                    studentBookingMenu(scanner);
	                    break;
	                case 3:
	                    studentManageBookings(scanner);
	                    break;
	                case 0:
	                    System.out.println("Exiting Tutoring App...");
	                    break;
	                default:
	                    System.out.println("Invalid choice. Try again!");
	            }
	        } while (choice != 0);
	    	scanner.close();
	    }
    }

    // ---------------- Tutor Menu ----------------
    private void tutorMenu(Scanner scanner, Tutor tutor) {
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
                	editTutoringSession(scanner, tutor);
                    break;
                case 2:
                	cancelTutoringSession(scanner, tutor);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }

    private void createTutoringSession(Scanner scanner, Tutor tutor) {
        try {
            // Gather inputs	
            System.out.print("Enter subject name: ");
            String subjectName = scanner.nextLine();
            Subject subject = new Subject(0, subjectName, "");

            System.out.print("Enter session date (dd-MM-yyyy): ");
            LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            System.out.print("Enter start time (HH:mm): ");
            LocalTime startTime = LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

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

    private void editTutoringSession(Scanner scanner, Tutor tutor) {
        displaySessions(tutor);

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

    private void cancelTutoringSession(Scanner scanner, Tutor tutor) {
        displaySessions(tutor);

        System.out.print("Enter session ID to cancel: ");
        int sessionId = Integer.parseInt(scanner.nextLine());
        
        // Check if session exists first hand
        TutoringSession session = tutoringSessionList.getSessionById(sessionId);
        if (session == null) {
            System.out.println("No session found with ID: " + sessionId);
            return;
        }

        try {
            tutoringSessionList.cancelSession((Tutor) tutor, sessionId);
            System.out.println("Session canceled successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void displaySessions(User user) {
        System.out.println("\n--- Available Sessions ---");

        if (ul.isStudent(user)) {
            // Student: show all sessions
            tutoringSessionList.getAllSessions()
                    .forEach(s -> System.out.println(formatSession(s)));

        } else if (ul.isTutor(user)) {
            // Tutor: show only their own sessions
            tutoringSessionList.getSessionsByTutor(user.getUserID())
                    .forEach(s -> System.out.println(formatSession(s)));

        } 
    }

    /**
     * Format session details for display
     */
    private String formatSession(TutoringSession s) {
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

    // ---------------- Student Booking ----------------
    private void studentBookingMenu(Scanner scanner) {
        System.out.print("Enter your name: ");
        String studentName = scanner.nextLine();
        Student student = new Student(studentName, "", "", 0);

        System.out.print("Enter subject to search: ");
        String keyword = scanner.nextLine();

        // Search sessions
        var availableSessions = tutoringSessionList.getSessions(keyword);
        if (availableSessions.isEmpty()) {
            System.out.println("No sessions available for: " + keyword);
            return;
        }

        System.out.println("Available sessions:");
        for (int i = 0; i < availableSessions.size(); i++) {
            var s = availableSessions.get(i);
            System.out.println((i + 1) + ". " + s.getSubject().getSubjectName()
                    + " | Date: " + s.getDate()
                    + " | Timeslot: " + s.getTimeslot()
                    + " | Duration: " + s.getDuration());
        }

        System.out.print("Select session number to book: ");
        int selectedIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (selectedIndex < 0 || selectedIndex >= availableSessions.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        TutoringSession selectedSession = availableSessions.get(selectedIndex);

        if (selectedSession.getAvailableCapacity() <= 0) {
            System.out.println("Session is full. Cannot book.");
            return;
        }

        Booking booking = bookingList.bookingSession(selectedSession);
        booking.setStudent(student);
        bookingList.confirmBooking(booking);
        student.getSessionsAttended().add(booking);

        // Reduce available seats
        selectedSession.setAvailableCapacity(selectedSession.getAvailableCapacity() - 1);

        System.out.println("Booking confirmed for " + student.getName() +
                " in " + selectedSession.getSubject().getSubjectName());
    }

    // ---------------- Student Manage Bookings ----------------
    private void studentManageBookings(Scanner scanner) {
        System.out.print("Enter your name: ");
        String studentName = scanner.nextLine();
        Student student = new Student(studentName, "", "", 0);

        var bookings = bookingList.getBookings(student);
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("Your bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            var b = bookings.get(i);
            System.out.println((i + 1) + ". " + b.getTutoringSession().getSubject().getSubjectName()
                    + " | Date: " + b.getTutoringSession().getDate()
                    + " | Timeslot: " + b.getTutoringSession().getTimeslot());
        }

        System.out.print("Select booking number to manage: ");
        int selectedIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (selectedIndex < 0 || selectedIndex >= bookings.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Booking selectedBooking = bookings.get(selectedIndex);

        System.out.println("1. Cancel Booking");
        System.out.println("2. Reschedule Booking");
        System.out.print("Choose action: ");
        int action = scanner.nextInt();
        scanner.nextLine();

        if (action == 1) {
            // Cancel Booking
            bookingList.deleteBookings(selectedBooking);
            student.getSessionsAttended().remove(selectedBooking);
            selectedBooking.getTutoringSession().setAvailableCapacity(
                    selectedBooking.getTutoringSession().getAvailableCapacity() + 1);
            System.out.println("Booking cancelled.");

        } else if (action == 2) {
            // Reschedule Booking
            TutoringSession currentSession = selectedBooking.getTutoringSession();
            System.out.println("Available sessions for " + currentSession.getSubject().getSubjectName() + ":");

            var availableSessions = tutoringSessionList.getSessions(currentSession.getSubject().getSubjectName());
            List<TutoringSession> freeSlots = new ArrayList<>();
            for (TutoringSession s : availableSessions) {
                if (!s.equals(currentSession) && s.getAvailableCapacity() > 0) {
                    freeSlots.add(s);
                }
            }

            if (freeSlots.isEmpty()) {
                System.out.println("No available time slots to reschedule.");
                return;
            }

            for (int i = 0; i < freeSlots.size(); i++) {
                TutoringSession s = freeSlots.get(i);
                System.out.println((i + 1) + ". Date: " + s.getDate() + " | Timeslot: " + s.getTimeslot()
                        + " | Available Seats: " + s.getAvailableCapacity());
            }

            System.out.print("Select new session number to reschedule: ");
            int newIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (newIndex < 0 || newIndex >= freeSlots.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            TutoringSession newSession = freeSlots.get(newIndex);

            // Update booking
            selectedBooking.setTutoringSession(newSession);
            newSession.setAvailableCapacity(newSession.getAvailableCapacity() - 1);
            currentSession.setAvailableCapacity(currentSession.getAvailableCapacity() + 1);

            System.out.println("Booking rescheduled to Date: " + newSession.getDate()
                    + " | Timeslot: " + newSession.getTimeslot());

        } else {
            System.out.println("Invalid action.");
        }
    }
}
