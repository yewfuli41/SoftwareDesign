package tutoring.domain;

import tutoring.persistence.*;

public class SubjectList implements ISubject {
	private ISubjectRepo subjectRepo;
	
	public SubjectList() {
		this.subjectRepo = new FileSubjectRepo();
	}
	
	@Override
    public Subject createSubject(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty.");
        }
        // Look up SubjectData.txt using FileSubjectRepo subjectRepo
        return subjectRepo.findAll().stream()
                .filter(s -> s.getSubjectName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subject '" + name + "' not found in SubjectData.txt"));
    }
}
