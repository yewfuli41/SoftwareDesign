package tutoring.domain;
public interface IUser {
	public void updateUser(User user);
	public boolean checkPassword(String password,User user);
	User login(String username, String password);
    boolean isTutor(User user);
    boolean isStudent(User user);
    void addUser(String name,String email,String password,String type);
    User findByEmail(String email);
    void checkDuplicateEmail(String email);
}



