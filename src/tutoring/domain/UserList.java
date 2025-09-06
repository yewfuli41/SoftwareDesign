
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
    public void updateUser(User user) {
		for(int i=0;i<users.size();i++) 
			if(user == users.get(i))
				users.set(i, user);
	}
    public void removeUser(User user) {
        users.remove(user);
        userRepo.saveAll(users);
    }
    public User findUserByName(String name) 
    {
        for(User u : users) 
        {
            if(u.getName().equals(name)) return u;
        }
        return null;
    }
    public boolean checkPassword(String password) {
    	for(int i=0;i<users.size();i++) 
    		if(users.get(i).getPassword()==password)
    			return true;
    	return false;

    }
}

