package tutoring.app;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import tutoring.domain.*;
import tutoring.persistence.*;

public class BookingApp {
	private Scanner scanner;
	private BookingController controller;
	private ITutoringSession tutoringSessionList;
	private IBooking bookingList;

	public BookingApp() {
		scanner = new Scanner(System.in);
		tutoringSessionList = new TutoringSessionList();
		bookingList = new BookingList();
		controller = new BookingController();
	}

	// ---------------- Student Booking ----------------
	public void studentBookingMenu() {
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
			System.out.println((i + 1) + ". " + s.getSubject().getSubjectName() + " | Date: " + s.getDate()
					+ " | Timeslot: " + s.getTimeslot() + " | Duration: " + s.getDuration());
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

		System.out.println(
				"Booking confirmed for " + student.getName() + " in " + selectedSession.getSubject().getSubjectName());
	}

	// ---------------- Student Manage Bookings ----------------
	public void studentManageBookings(Student student) {
		controller.setStudentBookings(student);
		var bookings = controller.getStudentBookings();
		if (bookings.isEmpty()) {
			throw new IllegalArgumentException("No bookings found");
		}

		System.out.println("Your bookings:");
		for (int i = 0; i < bookings.size(); i++) {
			var b = bookings.get(i);
			System.out.println((i + 1) + ". " + b.getTutoringSession().getSubject().getSubjectName() + " | Date: "
					+ b.getTutoringSession().getDate() + " | Timeslot: " + b.getTutoringSession().getTimeslot());
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
			selectedBooking.getTutoringSession()
					.setAvailableCapacity(selectedBooking.getTutoringSession().getAvailableCapacity() + 1);
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

			System.out.println("Booking rescheduled to Date: " + newSession.getDate() + " | Timeslot: "
					+ newSession.getTimeslot());

		} else {
			System.out.println("Invalid action.");
		}
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
