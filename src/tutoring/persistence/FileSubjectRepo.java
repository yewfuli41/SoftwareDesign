package tutoring.persistence;

import tutoring.domain.*;
import java.io.*;
import java.util.*;

public class FileSubjectRepo implements ISubjectRepo {
    private String subjectFile = "SubjectData.txt";

    @Override
    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(subjectFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: id,name,description
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String description = parts.length > 2 ? parts[2] : "";
                subjects.add(new Subject(id, name, description));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    @Override
    public Subject findById(int subjectId) {
        return findAll().stream()
                .filter(s -> s.getSubjectID() == subjectId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveAll(List<Subject> subjects) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(subjectFile))) {
            for (Subject s : subjects) {
                bw.write(s.getSubjectID() + "," + 
                         s.getSubjectName() + "," + 
                         s.getDescription());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
