package tutoring.persistence;

import tutoring.domain.*;
import java.io.*;
import java.util.*;

public class FileUserRepo implements IUserRepo {
    private String userFile = "UserData.txt";

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: id,type,name,email,password
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String name = parts[2];
                String email = parts[3];
                String password = parts[4];

                if ("Tutor".equalsIgnoreCase(type)) {
                    users.add(new Tutor(name, email, password, id));
                } else if ("Student".equalsIgnoreCase(type)) {
                    users.add(new Student(name, email, password, id));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(int id) {
        return findAll().stream()
                .filter(u -> u.getUserID() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Tutor findTutorById(int id) {
        return findAllTutors().stream()
                .filter(t -> t.getUserID() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Student findStudentById(int id) {
        return findAllStudents().stream()
                .filter(s -> s.getUserID() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Tutor> findAllTutors() {
        List<Tutor> tutors = new ArrayList<>();
        for (User u : findAll()) {
            if (u instanceof Tutor) tutors.add((Tutor) u);
        }
        return tutors;
    }

    @Override
    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User u : findAll()) {
            if (u instanceof Student) students.add((Student) u);
        }
        return students;
    }

    @Override
    public void saveAll(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            for (User u : users) {
                String type = (u instanceof Tutor) ? "Tutor" : "Student";
                bw.write(u.getUserID() + "," + type + "," + 
                         u.getName() + "," + 
                         u.getEmail() + "," + 
                         u.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
