package tutoring.domain;

import java.util.List;

public class UserList implements IUser {
    private final IUserRepo userRepo;
    private List<User> users;

    public UserList(IUserRepo userRepo) {
        this.userRepo = userRepo;
        this.users = userRepo.findAll();
    }

    @Override
    public User login(String username, String password) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(username) &&
                u.getPassword().equals(password)) {
                return u; // successful login
            }
        }
        return null; // failed login
    }

    @Override
    public boolean isTutor(User user) {
    	// Use instanceof because User consists of Student and Tutor object
        return user instanceof Tutor;
    }

    @Override
    public boolean isStudent(User user) {
        return user instanceof Student;
    }

    public void addUser(User user) {
        users.add(user);
        userRepo.saveAll(users);
    }

    public void removeUser(User user) {
        users.remove(user);
        userRepo.saveAll(users);
    }
}