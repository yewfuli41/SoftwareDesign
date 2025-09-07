package tutoring.domain;

public interface IUser {
	//methods define yourself.
	User login(String username, String password);
    boolean isTutor(User user);
    boolean isStudent(User user);
}

