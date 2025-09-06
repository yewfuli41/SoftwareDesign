package tutoring.app;
import java.util.Scanner;
import java.util.ArrayList;
import tutoring.domain.*;
public class TutoringApp {
	private static ITutoringSession tutoringSessionList;
	private static IBooking bookingList;
	private static IUser userList;
	private static UserApp userApp;
	private static User userLogIn;
	static Scanner input; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		input = new Scanner(System.in);
		userLogIn = new Student("hi","hi@gmail.com","hihihi",1);
		userList = new UserList();
		userApp = new UserApp();
		profileMenu(userLogIn);
	}
	public static void profileMenu(User user) {
		
		int choice;
		do {
			System.out.println("------------Options-------------");
			System.out.println("1. Update profile details(name, email or password)");
			System.out.println("2. Back to home page");
			System.out.println("3. Logout");
			System.out.print("Choice:");
			choice = input.nextInt();
			String skip = input.nextLine();
			switch(choice) {
				case 1:
					updateProfileMenu(user);
					break;
				case 3:
					userLogIn = userApp.logout();
					break;
			}
		}while(choice==1);
		
	}
	public static void updateProfileMenu(User user) {
		try {
			int choice;
			do {
				userApp.showProfile(user);
				System.out.println("------------Update Profile-------------");
				System.out.println("1. Change Name");
				System.out.println("2. Change email");
				System.out.println("3. Change Password");
				System.out.println("4. Exit");
				System.out.print("Choice:");
				choice = input.nextInt();
				String skip = input.nextLine();
				switch(choice) {
					case 1:
						userApp.changeName(user);
						break;
					case 2:
						userApp.changeEmail(user);
						break;
					case 3:
						userApp.changePassword(user);
						break;
					case 4:
						break;
				}
			}while(choice!=4);
		}catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
