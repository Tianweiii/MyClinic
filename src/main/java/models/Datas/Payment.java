package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
import java.lang.reflect.Array;
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
    private String method;
    private String datePaid;

    public Payment(String paymentID, String patientID, String appointmentID, String amount, String method, String datePaid) {
        this.paymentID = paymentID;
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.amount = amount;
        this.method = method;
        this.datePaid = datePaid;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}", paymentID, patientID, appointmentID, amount, method, datePaid);
    }

    public String getPaymentID() {
        return paymentID;
    }
    public String getPatientID() {
        return patientID;
    }
    public String getAppointmentID() {
        return appointmentID;
    }
    public String getAmount() {
        return amount;
    }
    public String getMethod() {
        return method;
    }
    public String getDatePaid() {
        return datePaid;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static String getNewPaymentId() {
        String id;
        try {
            id = DataHistory.getNewId("payment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return MessageFormat.format("PY{0}", id);
    }

    public static ArrayList<Payment> getAllPayments() throws IOException {
        ArrayList<Payment> appointments = new ArrayList<>();
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            Payment data = new Payment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
            appointments.add(data);
        }
        return appointments;
    }

    public static Payment findPayment(String paymentId, String patientId) throws IOException {
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(paymentId) && arr[1].equals(patientId)) {
                return new Payment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
            }
        }
        return null;
    }

    public static String getOldPaymentAmount(String paymentId) throws IOException {
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(paymentId)) {
                return arr[3];
            }
        }
        return null;
    }

    public void addToPaymentFile() {
        FileIO appender = new FileIO("a", "payment");
        appender.appendFile(toString() + "\n");
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

    public static Map<String, Number> getMonthlyRevenue() throws IOException {
        Map<String, Number> data = new HashMap<>();
        int jan = 0;
        int feb = 0;
        int mar = 0;
        int apr = 0;
        int may = 0;
        int jun = 0;
        int jul = 0;
        int aug = 0;
        int sep = 0;
        int oct = 0;
        int nov = 0;
        int dec = 0;
//        List<Map<String, Integer>> monthlyRevenue = new ArrayList<>();
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] rowData = row.split(",\\s");
            int amount = Integer.parseInt(rowData[3]);
            switch (rowData[5].split("/")[1]) {
                case "01" -> jan += amount;
                case "02" -> feb += amount;
                case "03" -> mar += amount;
                case "04" -> apr += amount;
                case "05" -> may += amount;
                case "06" -> jun += amount;
                case "07" -> jul += amount;
                case "08" -> aug += amount;
                case "09" -> sep += amount;
                case "10" -> oct += amount;
                case "11" -> nov += amount;
                case "12" -> dec += amount;
            }
        }
        data.put("Jan", jan);
        data.put("Feb", feb);
        data.put("Mar", mar);
        data.put("Apr", apr);
        data.put("May", may);
        data.put("Jun", jun);
        data.put("Jul", jul);
        data.put("Aug", aug);
        data.put("Sept", sep);
        data.put("Oct", oct);
        data.put("Nov", nov);
        data.put("Dec", dec);
        return data;
    }
}
