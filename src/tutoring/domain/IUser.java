package tutoring.domain;

public interface IUser {
	//methods define yourself.
	User login();
    boolean isTutor(User user);
    boolean isStudent(User user);
}

