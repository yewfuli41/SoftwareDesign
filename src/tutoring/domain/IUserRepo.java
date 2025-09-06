package tutoring.domain;

import java.util.List;

public interface IUserRepo {
    User findById(int id);
    Tutor findTutorById(int id);
    Student findStudentById(int id);
    List<User> findAll();
    List<Tutor> findAllTutors();
    List<Student> findAllStudents();
    void saveAll(List<User> users);
}