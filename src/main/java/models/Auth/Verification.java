package models.Auth;

import models.Filing.FileIO;

import java.io.IOException;

public class Verification {

    public static void verifyUser(String userID, String role) {

    }

    public static boolean verifyUsername(String name, String role) throws IOException {
        FileIO reader = new FileIO("r", role);
        for (String row : reader.readFile()) {
            String[] data = FileIO.splitString(row);
            if (data[1].equals(name)) {
                return false;
            }
        }
        return true;
    }

    // if either one of the fields are empty, return false
    public static boolean verifyEmptyFields(String username, String password, String dob, String gender, String salary) {
        if (username.isBlank() || password.isBlank() || dob.isBlank() || gender.isBlank() || salary.isBlank()) {
            return false;
        }
        return true;
    }
    public static boolean verifyEmptyFields(String patientId, String appointmentId, String amount, String method) {
        if (patientId.isBlank() || appointmentId.isBlank() || amount.isBlank() || method.isBlank()) {
            return false;
        }
        return true;
    }

    public static boolean verifyFields(String id, String oldUsername, String oldPassword, String oldDOB, String oldGender, String role, String special) {
        switch (role) {
            case "admin":
                FileIO readAdmin = new FileIO("r", "admin");
                try {
                    for (String row : readAdmin.readFile()) {
                        String[] arr = FileIO.splitString(row);
                        if (arr[0].equals(id)) {
                           if (arr[1].equals(oldUsername) && arr[2].equals(oldPassword) && arr[3].equals(oldDOB) && arr[4].equals(oldGender) && arr[6].equals(special)) {
                               return false;
                           }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            case "doctor":
                FileIO readDoc = new FileIO("r", "doctor");
                try {
                    for (String row : readDoc.readFile()) {
                        String[] arr = FileIO.splitString(row);
                        if (arr[0].equals(id)) {
                            if (arr[1].equals(oldUsername) && arr[2].equals(oldPassword) && arr[3].equals(oldDOB) && arr[4].equals(oldGender) && arr[6].equals(special)) {
                                return false;
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            case "patient":
                FileIO readPat = new FileIO("r", "patient");
                try {
                    for (String row : readPat.readFile()) {
                        String[] arr = FileIO.splitString(row);
                        if (arr[0].equals(id)) {
                            if (arr[1].equals(oldUsername) && arr[2].equals(oldPassword) && arr[3].equals(oldDOB) && arr[4].equals(oldGender) && arr[6].equals(special)) {
                                return false;
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            default:
                return false;
        }
    }
}
