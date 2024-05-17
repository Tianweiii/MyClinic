package models.Users;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import models.Auth.Verification;
import models.Datas.*;
import models.Filing.FileIO;

public class Admin extends User {
    String salary;

    public Admin(String ID, String username, String password, String dateOfBirth, String gender, String role, String salary) {
        super(ID, username, password, dateOfBirth, gender, role);
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + salary;
    }

    public String getSalary() {
        return salary;
    }

    public void registerAccount(String id, String username, String password, String DOB, String gender, String role, String special) {
        if (role.equals("admin")) {
            String data = MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}, {6}\n", id, username, password, DOB, gender, role, special);
            FileIO writer = new FileIO("a", "admin");
            writer.appendFile(data);
        } else if (role.equals("doctor")) {
            String data = MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}, {6}\n", id, username, password, DOB, gender, role, special);
            FileIO writer = new FileIO("a", "doctor");
            writer.appendFile(data);
        } else {
            String data = MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}, {6}\n", id, username, password, DOB, gender, role, special);
            FileIO writer = new FileIO("a", "patient");
            writer.appendFile(data);
        }
        System.out.println("Successfully Registered Account!");
    }

    public void writeToRoleFile(ArrayList<String> data, String role) {
        FileIO writer = new FileIO("w", role);
        writer.writeFile(data);
    }

    public void updateUser(String id, String username, String password, String DOB, String gender, String role, String special) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        switch (role) {
            case "admin", "doctor", "patient" -> {
                FileIO reader = new FileIO("r", role);
                for (String row : reader.readFile()) {
                    String[] arr = FileIO.splitString(row);
                    if (arr[0].equals(id)) {
                        row = MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}, {6}", id, username, password, DOB, gender, role, special);
                    }
                    data.add(row);
                }
            }
        }
        writeToRoleFile(data, role);
    }

    public void deleteUser(String id, String role) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        switch (role) {
            case "admin", "doctor", "patient" -> {
                FileIO reader = new FileIO("r", role);
                for (String row : reader.readFile()) {
                    String[] arr = FileIO.splitString(row);
                    if (!arr[0].equals(id)) {
                        data.add(row);
                    }
                }
            }
        }
        writeToRoleFile(data, role);
    }

    public void walkInAppointment(String patientID, String doctorID, String time, String duration, String description) throws IOException {
        //get current date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);
        //get schedule id of current date
        String appointmentScheduleId = Schedule.getAppointmentScheduleID(currentDate);

        // get new appointment id
        String id = Appointment.getNewAppointmentId();
        //create appointment object and update appointment file
        Appointment newAppointment = new Appointment(id, patientID, doctorID, currentDate, time, duration, "walk-in", description, appointmentScheduleId);
        newAppointment.addToAppointmentFile();
        System.out.println("Appointment file updated");

        //update data history file count
        DataHistory.updateDataHistoryCount("appointment");

        //update schedule file
        Schedule.updateScheduleFile(id, appointmentScheduleId, time, duration);
        System.out.println("Schedule file updated");
    }

    public void manageAppointment(String appointmentID, String date, String status) throws IOException{
        ArrayList<String> data = new ArrayList<>();
        Appointment target = Appointment.findAppointment(appointmentID, date);
        target.setStatus(status);
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(appointmentID) && arr[3].equals(date)) {
                row = target.toString();
            }
            data.add(row);
        }
        Appointment.writeToAppointmentFile(data);
    }

    public ArrayList<MedicalRecord> trackMedicalRecord() throws IOException {
        ArrayList<MedicalRecord> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "medicalRecord");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            MedicalRecord record = new MedicalRecord(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
            data.add(record);
        }
        return data;
    }

    public ArrayList<Appointment> trackDailyAppointment() throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);

        return Appointment.findAppointment(currentDate);
    }

    public void addPayment(String PatientID, String AppointmentID, String amount, String status) {
        String id = Payment.getNewPaymentId();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);

        Payment data = new Payment(id, PatientID, AppointmentID, amount, status, currentDate);
        data.addToPaymentFile();
    }

    public void managePayment(String paymentId, String patientId, String status) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        Payment target = Payment.findPayment(paymentId, patientId);
        target.setStatus(status);
        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(paymentId) && arr[1].equals(patientId)) {
                row = target.toString();
            }
            data.add(row);
        }
        Payment.writeToPaymentFile(data, paymentId);
    }
}
