/*
 Author: LSun
 Class description: no plans for managing Subject.txt, will hard code
 */

package tutoring.domain;

public class Subject {
	private int subjectID;
	private String subjectName;
	private String description;
	public Subject(int subjectID, String subjectName, String description) {
		this.subjectID = subjectID;
		this.subjectName = subjectName;
		this.description = description;
	}
	public int getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
