package tutoring.persistence;

import java.util.List;

import tutoring.domain.Student;
import tutoring.domain.Tutor;
import tutoring.domain.User;

public interface IUserRepo {
    User findById(int id);
    Tutor findTutorById(int id);
    Student findStudentById(int id);
    List<User> findAll();
    List<Tutor> findAllTutors();
    List<Student> findAllStudents();
    void saveAll(List<User> users);
}