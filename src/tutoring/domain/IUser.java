package tutoring.domain;

public interface IUser {
	public void updateUser(User user);
	public boolean checkPassword(String password);
}
