/*
 Author: Everyone
 Class description: Application layer, main UI, command line based
*/
package tutoring.app;

import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import tutoring.domain.*;
import tutoring.persistence.*;

public class TutoringApp {
	private static IUser userList;
	private static UserApp userApp;
	private static BookingApp bookingApp;
	private static TutoringSessionApp tutoringSessionApp;
	private static Scanner input;
	private static User currentUser;

	public TutoringApp() {
		this.userList = new UserList();
		this.input = new Scanner(System.in);
		this.userApp = new UserApp();
		this.tutoringSessionApp = new TutoringSessionApp();
		this.bookingApp = new BookingApp();
		this.currentUser = null;
	}

	public static void main(String[] args) {
		TutoringApp app = new TutoringApp();
		app.run();

	}

	public void run() {
		while (true) {
			System.out.println("=== Welcome to Tutoring App ===");
			System.out.println("\n1. Register");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.print("Choose option: ");
			try {
				int choice = Integer.parseInt(input.nextLine());

				switch (choice) {
				case 1:
					userApp.register();
					break;
				case 2:
					login();
					break;
				case 3:
					System.out.println("Goodbye!");
					return;
				default:
					System.out.println("Invalid choice!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number!");
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}

	public void login() {
		System.out.print("Enter email: ");
		String email = input.nextLine();
		System.out.print("Enter password: ");
		String password = input.nextLine();
		currentUser = userApp.validateLogin(email, password);
		if (currentUser != null) {
			System.out.println("Login successful! Welcome " + currentUser.getName());
			if (currentUser instanceof Student) {
				studentMenu();
			} else if (currentUser instanceof Tutor) {
				tutorMenu();
			}
		} else {
			throw new IllegalArgumentException("Invalid email or password.");
		}
		
	}

      

	public void tutorMenu() {

		while (true) {
			System.out.println("\n===== Tutoring App Menu =====");
			System.out.println("1. Create New Tutoring Sessions");
			System.out.println("2. Manage Tutoring Sessions");
			System.out.println("3. Profile");
			System.out.println("4. Logout");
			System.out.print("Enter choice: ");
			try {
				int choice = Integer.parseInt(input.nextLine());
				switch (choice) {
				case 1:
					tutoringSessionApp.createTutoringSession((Tutor)currentUser);
					break;
				case 2:
					tutoringSessionApp.tutorMenu((Tutor)currentUser);
					break;
				case 3:
					profileMenu(currentUser);
					break;
				case 4:
					currentUser = userApp.logout();
					return;
				default:
					System.out.println("Invalid choice. Try again!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number!");
			}
		}
	}

	public void studentMenu() {
		while (true) {
			System.out.println("\n===== Tutoring App Menu =====");
			System.out.println("1. Search Sessions");
			System.out.println("2. Book a Session");
			System.out.println("3. Manage Bookings");
			System.out.println("4. Bookings Statistics");
			System.out.println("5. Profile");
			System.out.println("6. Logout");
			System.out.print("Enter choice: ");
			try {
				int choice = Integer.parseInt(input.nextLine());

				switch (choice) {
				case 1:
					tutoringSessionApp.searchTutoringSessions(currentUser);
					break;
				case 2:
					bookingApp.studentBookingMenu((Student) currentUser);
					break;
				case 3:
					bookingApp.studentManageBookings((Student) currentUser);
					break;
				case 4:
					bookingApp.bookingsStatisticsMenu((Student)currentUser);
					break;
				case 5:
					profileMenu(currentUser);
					break;
				case 6:
					currentUser = userApp.logout();
					return;
				default:
					System.out.println("Invalid choice. Try again!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number!");
			} catch(IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}


	public static void profileMenu(User user) {

		while (true) {
			System.out.println("------------Options-------------");
			System.out.println("1. Update profile details(name, email or password)");
			System.out.println("2. Back to tutoring app menu");
			System.out.print("Choice:");
			try {
				int choice = Integer.parseInt(input.nextLine());
				switch (choice) {
					case 1:
						userApp.updateProfileMenu(user);
						break;
					case 2:
						return;
					default:
						System.out.println("Invalid choice. Try Again!");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number!");
			}
		}
	}
}
