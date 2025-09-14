
package tutoring.domain;

import java.util.List;
import tutoring.persistence.*;

public class UserList implements IUser {
	RepoMain factory;
    private final IUserRepo userRepo;
    private List<User> users;

    public UserList() {
    	this.factory = new RepoMain();
        this.userRepo = factory.getUserRepo();
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

    public void addUser(String name,String email,String password,String type) {
    	int newId = users.size() + 1; // simple auto-id
		User newUser;
		if ("Tutor".equalsIgnoreCase(type)) {
			newUser = new Tutor(name, email, password, newId);
		} else if("Student".equalsIgnoreCase(type)){
			newUser = new Student(name, email, password, newId);
		}else {
			throw new IllegalArgumentException("Invalid user type.");
		}
        users.add(newUser);
        userRepo.saveAll(users);
    }
    public void updateUser(User user) {
		for(int i=0;i<users.size();i++) {
			if(users.get(i).getUserID()==user.getUserID()) {
				users.set(i, user);
				break;
			}
		}
		userRepo.saveAll(users);
	}
    public void removeUser(User user) {
        users.remove(user);
        userRepo.saveAll(users);
    }
    public boolean checkPassword(String password, User user) {
    	if(user.getPassword().equals(password))
    		return true;
    	return false;

    }
    @Override
    public User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
    public void checkDuplicateEmail(String email) {
    	if (findByEmail(email) != null) {
			throw new IllegalArgumentException("Email already exists!");
		}
    }
}

