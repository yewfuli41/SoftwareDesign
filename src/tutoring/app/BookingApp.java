package tutoring.app;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import tutoring.domain.*;
import tutoring.persistence.*;

public class BookingApp {
	private Scanner scanner;
	private BookingController controller;
	private TutoringSessionApp tsapp;

	public BookingApp() {
		scanner = new Scanner(System.in);
		controller = new BookingController();
		tsapp = new TutoringSessionApp();
		
	}

	// ---------------- Student Booking ----------------
	public void studentBookingMenu(Student student) {
		try {
			tsapp.searchTutoringSessions(student);
			// Search sessions using subject name (1, Mathematics. 2, Physics. 3, Computer Science. 4, Chemistry. 5, English.
			/*
			1,Mathematics,Learn algebra, calculus, and geometry
			2,Physics,Covers mechanics, electricity, and magnetism
			3,Computer Science,Introduction to programming and algorithms
			4,Chemistry,Covers organic and inorganic chemistry
			5,English,Improve grammar, writing, and speaking skills
			 */
		
			System.out.print("Select session ID to book: ");
			int selectedID = scanner.nextInt();
			scanner.nextLine();
	
			// final step of create booking, this step will throw errors 
			Booking booking = controller.createBooking(selectedID,student);
			System.out.println("Confirm booking?(Y/N)");
			String decision = scanner.nextLine();
			if (decision.trim().toUpperCase().equals("Y")) {
				confirmBooking(booking);
			} else {
				throw new IllegalArgumentException("Booking on pending, go to Manage Booking page to confirm the registration.");
			}
		} catch (Exception e) {
	    	System.out.println(e.getMessage());
	    } 
	}

	// ---------------- Student Manage Bookings ----------------
	public void studentManageBookings(Student student) {

		controller.setStudentBookings(student);
		controller.checkHasStudentBookings();
		var bookings = controller.getStudentBookings();		
		printAllBookings(bookings);
		Booking selectedBooking = selectBooking(bookings);
		System.out.println("---------------Manage Booking Options-----------------");
		System.out.println("1. Cancel Booking");
		System.out.println("2. Change to another session");
		System.out.println("3. Confirm pending booking");
		System.out.println("4. Exit");
		System.out.print("Choose action: ");
		
		try {
			int action = scanner.nextInt();
			scanner.nextLine();
			switch(action) {
				case 1:
					controller.cancelBooking(selectedBooking);
					System.out.println("Booking cancelled.");
					break;
				case 2:
					changeSession(selectedBooking);
					break;
				case 3:
					confirmBooking(selectedBooking);
					break;
				case 4:
					return;
				default:
					System.out.println("Invalid action.");
			}
		}catch(NumberFormatException ex) {
			System.out.println("Please enter a valid number!");
		}
		
	}
	public void confirmBooking(Booking booking) {
		controller.confirmBooking(booking);
		System.out.println(
				"Booking confirmed for " + booking.getStudent().getName() + " in " + booking.getTutoringSession().getSubject().getSubjectName()
				+" under Mr./Mrs. "+ booking.getTutoringSession().getTutor().getName());
	}
	public void changeSession(Booking selectedBooking) {
		TutoringSession currentSession = selectedBooking.getTutoringSession();
		String subject = currentSession.getSubject().getSubjectName();
		ArrayList<TutoringSession> freeSlots = printAvailableSessionsBySubject(subject);
		TutoringSession newSession = selectSession(freeSlots);
		controller.editBooking(selectedBooking, newSession, currentSession);
		System.out.println("Session changed: " + newSession.getDate() + " | Time: "
				+ newSession.getStartTime()+"-"+newSession.getEndTime());
	}
	public TutoringSession selectSession(ArrayList<TutoringSession> freeSlots) {
		System.out.print("Select new session number to change: ");
		int newIndex = scanner.nextInt() - 1;
		scanner.nextLine();
		if (newIndex < 0 || newIndex >= freeSlots.size()) {
			throw new IllegalArgumentException("Invalid selection.");
		}
		TutoringSession newSession = freeSlots.get(newIndex);
		return newSession;
	}
	public ArrayList<TutoringSession> printAvailableSessionsBySubject(String subject) {
		System.out.println("Available sessions for " + subject + ":");
		tsapp.setSessions();
		List<TutoringSession> availableSessions = tsapp.filterTutoringSessionsBySubject(subject);
		ArrayList<TutoringSession> freeSlots = new ArrayList<>();
		for (TutoringSession session : availableSessions) {
			if (session.getAvailableCapacity() > 0) {
				freeSlots.add(session);
			}
		}
		if (freeSlots.isEmpty()) {
			throw new IllegalArgumentException("No available free slots.");
		}
		for (int i = 0; i < freeSlots.size(); i++) {
			TutoringSession session = freeSlots.get(i);
			System.out.println((i + 1) + ". Date: " + session.getDate() + " | Time: " + session.getStartTime()+"-"+session.getEndTime()
					+ " | Available Seats: " + session.getAvailableCapacity());
		}
		return freeSlots;
	}
	public void printAllBookings(ArrayList<Booking> bookings) {
		System.out.println("Your bookings:");
		for (int i = 0; i < bookings.size(); i++) {
			var booking = bookings.get(i);
			System.out.println((i + 1) + ". " + booking.getTutoringSession().getSubject().getSubjectName() + " | Date: "
					+ booking.getTutoringSession().getDate() + " | Time: " + booking.getTutoringSession().getStartTime()+"-"+booking.getTutoringSession().getEndTime());
		}

	}
	public Booking selectBooking(ArrayList<Booking> bookings) {
		System.out.print("Select booking number to manage: ");
		int selectedIndex = scanner.nextInt() - 1;
		scanner.nextLine();

		if (selectedIndex < 0 || selectedIndex >= bookings.size()) {
			throw new IllegalArgumentException("Invalid selection.");
		}
		Booking selectedBooking = bookings.get(selectedIndex);
		return selectedBooking;
	}
	
	public void bookingsStatisticsMenu(Student student) {
		while (true) {
			controller.setStudentBookings(student);
			controller.filterConfirmedBookings();
			double totalHoursSessionsAttend = 0;
			int totalNumberSessionsAttend = 0;
			System.out.println("------------Statistics--------------");
			System.out.println("Filter by:");
			System.out.println("1.Subject");
			System.out.println("2.Month");
			System.out.println("3.Tutor");
			System.out.println("4.Exit");
			System.out.print("choice:");
			try {
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 1:
					System.out.print("Enter subject name:");
					String subject = scanner.nextLine().trim().toLowerCase();
					controller.filterBookingsBySubject(subject);
					break;
				case 2:
					System.out.print("Enter month:");
					String month = scanner.nextLine().trim();
					controller.filterBookingsByDate(month);
					break;
				case 3:
					System.out.print("Enter tutor name:");
					String tutor = scanner.nextLine().trim().toLowerCase();
					controller.filterBookingsByTutor(tutor);
					break;
				case 4:
					return;
				default:
					System.out.println("Invalid choice. Try again!");

				}
			} catch (NumberFormatException ex) {
				System.out.println("Please enter a valid number!");
			}
			ArrayList<Booking> bookings = controller.getStudentBookings();
			totalHoursSessionsAttend = controller.calculateTotalHoursSessionsAttended();
			totalNumberSessionsAttend = bookings.size();
			try {
				printStatistics(totalHoursSessionsAttend, totalNumberSessionsAttend, bookings);
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	

	public void printStatistics(double totalHoursSessionsAttend, int totalNumberSessionsAttend,
			ArrayList<Booking> bookings) {
		System.out.println("--------------------Summary Report----------------------------");
		System.out.println("Total Hours of Confirmed Tutoring Sessions: " + totalHoursSessionsAttend + " hours");
		System.out.println("Total Number of Confirmed Tutoring Sessions: " + totalNumberSessionsAttend);
		System.out.println("----------------Confirmed Tutoring Sessions-------------------");
		if (bookings.size() > 0) {
			for (int i = 0; i < bookings.size(); i++) {
				System.out.println("Booking "+(i+1));
				System.out.println("Tutoring Session: "
						+ bookings.get(i).getTutoringSession().getSubject().getSubjectName());
				System.out.println("Tutor: " + bookings.get(i).getTutoringSession().getTutor().getName());
			}
		} else {
			throw new IllegalArgumentException("No tutoring session is booked before.");
		}

	}
}
