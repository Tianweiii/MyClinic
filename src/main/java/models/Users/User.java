package models.Users;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import models.Filing.FileIO;

public class User {
    final private String ID;
    private String username;
    private String password;
    final private String dateOfBirth;
    final private String gender;
    final private String role;

    public String getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate dobToLocaldate() {
        String[] arr = getDateOfBirth().split("/");
        String date = MessageFormat.format("{0}-{1}-{2}", arr[2], arr[1], arr[0]);
        return LocalDate.parse(date);
    }

    public String LocalDateToDob(LocalDate date) {
        String data = date.toString();
        String[] arr = data.split("-");
        return MessageFormat.format("{0}/{1}/{2}", arr[0], arr[1], arr[2]);
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public User(String ID, String username, String password, String dateOfBirth, String gender, String role) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.role = role;
    }

    public static User loginAccount(String username, String password, String role) {
        FileIO reader = new FileIO("r", role);
        try {
            for (String line : reader.readFile()) {
                String[] row = FileIO.splitString(line);
                if (row[1].equals(username) && row[2].equals(password)) {
                    System.out.println("Account found.");
                    return switch (role) {
                        case "admin" -> new Admin(row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
                        case "doctor" -> new Doctor(row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
                        case "patient" -> new Patient(row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
                        default -> new User(row[0], row[1], row[2], row[3], row[4], row[5]);
                    };
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Account not found");
        return null;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}", ID, username, password, dateOfBirth, gender, role);
    }

    public static List<String> getAccounts(Admin admin) throws IOException {
        FileIO reader = new FileIO("r", "admin");
        return reader.readFile();
    }

    public static List<String> getAccounts(Doctor doctor) throws IOException {
        FileIO reader = new FileIO("r", "doctor");
        return reader.readFile();
    }

    public static List<String> getAccounts(Patient patient) throws IOException {
        FileIO reader = new FileIO("r", "patient");
        return reader.readFile();
    }
}
