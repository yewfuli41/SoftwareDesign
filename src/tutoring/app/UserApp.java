package tutoring.app;

import tutoring.domain.*;
import tutoring.persistence.IUserRepo;

import java.util.Scanner;
import java.util.ArrayList;

public class UserApp {
	private Scanner input;
	private IUser userList;

	public UserApp() {
		input = new Scanner(System.in);
		userList = new UserList();
	}

	public void register() {
		try {
			System.out.print("Enter name: ");
			String name = input.nextLine();
			System.out.print("Enter email: ");
			String email = input.nextLine();
			userList.checkDuplicateEmail(email);
			System.out.print("Enter password: ");
			String password = input.nextLine();
			System.out.print("Register as Tutor or Student? ");
			String type = input.nextLine();
			
			userList.addUser(name,email,password,type);
			System.out.println("Registration successful!");
		}catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}
	public User validateLogin(String email,String password) {
		User user = userList.findByEmail(email);
        if (user != null && user.getPassword().equals(password))
        	return user;
        return null;
	}
	public void updateProfileMenu(User user) {
		try {
			int choice;
			do {
				showProfile(user);
				System.out.println("------------Update Profile-------------");
				System.out.println("1. Change Name");
				System.out.println("2. Change email");
				System.out.println("3. Change Password");
				System.out.println("4. Exit");
				System.out.print("Choice:");
				choice = Integer.parseInt(input.nextLine());
				switch (choice) {
				case 1:
					changeName(user);
					break;
				case 2:
					changeEmail(user);
					break;
				case 3:
					changePassword(user);
					break;
				case 4:
					break;
				default:
					System.out.println("Invalid choice. Try Again!");
				}
			} while (choice != 4);
		} catch(NumberFormatException e) {
			System.out.println("Please enter a valid number!");
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void showProfile(User user) {
		String name = user.getName();
		String email = user.getEmail();
		System.out.println("-----------Profile--------------");
		System.out.println("Name: " + name);
		System.out.println("Email: " + email);
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
		if (!userList.checkPassword(currentPassword,user)) {
			throw new IllegalArgumentException("Wrong password!");
		}
		System.out.print("New Password: ");
		String newPassword = input.nextLine();
		user.setPassword(newPassword);
		userList.updateUser(user);
		System.out.println("Password updates successfully!");
	}

	public User logout() {
		System.out.println("Successfully logged out!");
		return null;
	}
}
