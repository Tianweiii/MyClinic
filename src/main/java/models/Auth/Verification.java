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

    //to check if the fields got update or not when updating user
    public static boolean verifyFields() {
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

    public static void verifyAppointment(String appointmentID) {}

    public static void verifyPayment(String paymentID) {}

    public static void verifyMedicalRecord(String medicalRecordID) {}
}
