package tutoring.domain;
import java.util.ArrayList;
public class UserList implements IUser{
	private ArrayList<User> users;

	public UserList() {
		this.users = new ArrayList<User>();
	}
	
	public void addUser(User user) 
	{
		users.add(user); 
	}
	public void updateUser(User user) {
		for(int i=0;i<users.size();i++) 
			if(user == users.get(i))
				users.set(i, user);
	}
    
	public void removeUser(User user)
	{
		users.remove(user); 
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
