
package tutoring.domain;

public interface IUser {
	public void updateUser(User user);
	public boolean checkPassword(String password);
	User login(String username, String password);
    boolean isTutor(User user);
    boolean isStudent(User user);
}



