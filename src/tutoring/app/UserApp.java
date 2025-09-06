package tutoring.app;

import tutoring.domain.*;
import java.util.Scanner;
import java.util.ArrayList;
public class UserApp {
	private Scanner input;
	private IUser userList;
	public UserApp() {
		input = new Scanner(System.in);
		userList = new UserList();
	}
	public void showProfile(User user) {
		String name = user.getName();
		String email = user.getEmail();
		System.out.println("-----------Profile--------------");
		System.out.println("Name: "+name);
		System.out.println("Email: "+email);
	}
	public void changeName(User user) {
		System.out.print("New Name: ");
		String name = input.nextLine();
		user.setName(name);
		userList.updateUser(user);
		System.out.println("Name updates successfully!");
	}
	public void changeEmail(User user) {
		System.out.print("New Email: ");
		String email = input.nextLine();
		user.setEmail(email);
		userList.updateUser(user);
		System.out.println("Email updates successfully!");
	}
	public void changePassword(User user) {
		System.out.print("Current Password: ");
		String currentPassword = input.nextLine();
		if(!userList.checkPassword(currentPassword)) {
			throw new IllegalArgumentException("Wrong password!");
		}
		System.out.print("New Password: ");
		String newPassword = input.nextLine();
		user.setPassword(newPassword);
		userList.updateUser(user);
		System.out.println(" updates successfully!");
	}
	public User logout() {
		System.out.println("Successfully logged out!");
		return null;
	}
}
