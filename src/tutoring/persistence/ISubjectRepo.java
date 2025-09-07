package tutoring.persistence;

import java.util.List;

import tutoring.domain.Subject;
public interface ISubjectRepo {
    Subject findById(int subjectId);
    List<Subject> findAll();
    void saveAll(List<Subject> subjects);
}