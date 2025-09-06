package tutoring.domain;

import java.util.List;

public interface ISubjectRepo {
    Subject findById(int subjectId);
    List<Subject> findAll();
    void saveAll(List<Subject> subjects);
}