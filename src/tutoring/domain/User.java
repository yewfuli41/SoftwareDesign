package tutoring.domain;

public abstract class User {
	private String name;
	private String email;
	private String password;
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
	public abstract int getUserID();
	public abstract void setUserID(int userID);
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		int atIndex = email.indexOf('@');
		int dotIndex = email.lastIndexOf('.');
		if (atIndex <= -1)
			throw new IllegalArgumentException("No @ in the email");
		else if (atIndex == 0)
        	throw new IllegalArgumentException("No characters before @ in the email");
        else if (dotIndex == email.length() - 1) 
        	throw new IllegalArgumentException("No . in the email");
        else if (dotIndex < atIndex + 2)
        	throw new IllegalArgumentException("No characters in between @ and .");
        else
        	this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(password.length()<6)
			throw new IllegalArgumentException("Password must have at least 6 characters");
		boolean hasDigit = false;
		boolean hasSpecialChar = false;
		for(int i=0;i<password.length();i++) {
			if(Character.isDigit(password.charAt(i)))
				hasDigit = true;
			else if(!Character.isLetter(password.charAt(i)))
				hasSpecialChar = true;
		}
		if(!hasDigit)
			throw new IllegalArgumentException("Password must have at least 1 digit.");
		else if(!hasSpecialChar)
			throw new IllegalArgumentException("Password must have at least 1 special character.");
		else
			this.password = password;
	}
	
}
