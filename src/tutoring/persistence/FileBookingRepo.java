package tutoring.persistence;

import tutoring.domain.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBookingRepo implements IBookingRepo {
    private String bookingFile = "BookingData.txt";

    private final IUserRepo userRepo;
    private final ITutoringSessionRepo sessionRepo;

    public FileBookingRepo(IUserRepo userRepo, ITutoringSessionRepo sessionRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
    }

    @Override
    public List<Booking> loadAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(bookingFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: bookingId,sessionId,studentId,bookingDate,status
                String[] parts = line.split(",");
                
                if (line.trim().isEmpty()) continue;
                if (parts.length < 5) {
                    System.err.println("Skipping malformed booking line: " + line);
                    continue;
                }

                int bookingId = Integer.parseInt(parts[0]);
                int sessionId = Integer.parseInt(parts[1]);
                int studentId = Integer.parseInt(parts[2]);
                LocalDate bookingDate = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String status = parts[4];

                // load session info using sessionRepo refer to sessionId
                TutoringSession session = sessionRepo.findById(sessionId);
                Student student = userRepo.findStudentById(studentId);

                if (session == null || student == null) {
                    System.err.println("Skipping booking " + bookingId + ": missing session/student");
                    continue;
                }

                bookings.add(new Booking(bookingId, session, bookingDate, status, student));
            }
        } catch (FileNotFoundException e) {
        	return new ArrayList<>(); //empty repo because no file
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public Booking findById(int bookingId) {
        return loadAllBookings().stream()
                .filter(b -> b.getBookingID() == bookingId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addBooking(Booking booking) {
        List<Booking> bookings = loadAllBookings();
        if (bookings.stream().anyMatch(b -> b.getBookingID() == booking.getBookingID())) {
            throw new IllegalArgumentException("Booking with ID " + booking.getBookingID() + " already exists");
        }
        bookings.add(booking);
        saveAllBookings(bookings);
    }

    @Override
    public void updateBooking(Booking updated) {
        List<Booking> bookings = loadAllBookings();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingID() == updated.getBookingID()) {
                bookings.set(i, updated);
                break;
            }
        }
        saveAllBookings(bookings);
    }

    @Override
    public void deleteBooking(int bookingId) {
        List<Booking> bookings = loadAllBookings();
        bookings.removeIf(b -> b.getBookingID() == bookingId);
        saveAllBookings(bookings);
    }

    @Override
    public List<Booking> findBySessionId(int sessionId) {
        List<Booking> all = loadAllBookings();
        List<Booking> result = new ArrayList<>();
        for (Booking b : all) {
            if (b.getTutoringSession().getTutoringSessionID() == sessionId) {
                result.add(b);
            }
        }
        return result;
    }
    
    @Override
    public List<Booking> findByStudentId(int studentId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : loadAllBookings()) {
            if (b.getStudent().getUserID() == studentId) {
                result.add(b);
            }
        }
        return result;
    }

    private void saveAllBookings(List<Booking> bookings) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(bookingFile))) {
            for (Booking b : bookings) {
                bw.write(b.getBookingID() + "," +
                         b.getTutoringSession().getTutoringSessionID() + "," +
                         b.getStudent().getUserID() + "," +
                         b.getBookingDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "," +
                         b.getStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
