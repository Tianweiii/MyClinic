package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment {
    private String paymentID;
    private String patientID;
    private String appointmentID;
    private String amount;
    private String status;
    private String dateModified;

    public Payment(String paymentID, String patientID, String appointmentID, String amount, String status, String dateModified) {
        this.paymentID = paymentID;
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.amount = amount;
        this.status = status;
        this.dateModified = dateModified;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}", paymentID, patientID, appointmentID, amount, status, dateModified);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getNewPaymentId() {
        String id;
        FileIO reader = new FileIO("r", "payment");
        try {
            id = String.valueOf(reader.countRowNum() + 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return MessageFormat.format("PY{0}", id);
    }

    public static Payment findPayment(String paymentId, String patientId) throws IOException {
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(paymentId) && arr[1].equals(patientId)) {
                System.out.println("Found data.");
                return new Payment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
            }
        }
        return null;
    }

    public void addToPaymentFile() {
        FileIO appender = new FileIO("a", "payment");
        appender.appendFile(toString());
        System.out.println("Added payment.");
    }

    public static void writeToPaymentFile(ArrayList<String> data, String paymentId) {
        FileIO writer = new FileIO("w", "payment");
        writer.writeFile(data);
        System.out.printf("Successfully edited %s payment details.", paymentId);
    }

    public List<Map<String, String>> getPatientPayment(String PatientID, String appointmentID) throws IOException {
        List<Map<String, String>> paymentList = new ArrayList<>();
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(PatientID) && arr[2].equals(appointmentID)) {
                Map<String, String> map = new HashMap<>();
                map.put(arr[0], arr[5]);
                paymentList.add(map);
            }
        }
        return paymentList;
    }

    public List<Map<String, String>> getMonthlyRevenue() {
        List<Map<String, String>> monthlyRevenue = new ArrayList<>();
        FileIO reader = new FileIO("r", "payment");

        return monthlyRevenue;
    }
}
