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

    public static void verifyAppointment(String appointmentID) {}

    public static void verifyPayment(String paymentID) {}

    public static void verifyMedicalRecord(String medicalRecordID) {}
}
