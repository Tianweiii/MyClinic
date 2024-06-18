package models.Users;

import javafx.collections.ObservableList;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Datas.DataHistory;
import models.Datas.MedicalRecord;
import models.Datas.Schedule;
import models.Filing.FileIO;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Patient extends User {
    final private String medicalCase;

    public Patient(String ID, String username, String password, String dateOfBirth, String gender, String role, String medicalCase) {
        super(ID, username, password, dateOfBirth, gender, role);
        this.medicalCase = medicalCase;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + medicalCase;
    }

    public String getMedicalCase() {
        return medicalCase;
    }

    public static String[] getPatientId() throws IOException {
        ArrayList<String> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "patient");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            data.add(arr[0]);
        }
        return data.toArray(new String[data.size()]);
    }

    public static ArrayList<Schedule> viewTimeslot() throws IOException {
        // get all schedule starting from today to the most recent schedule
        ArrayList<Schedule> data = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);
        Date formattedCurrentDate = null;
        try {
            formattedCurrentDate = formatter.parse(currentDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        FileIO reader = new FileIO("r", "schedule");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            String fileDate = arr[2];
            try {
                Date parser = formatter.parse(fileDate);
                if (!parser.before(formattedCurrentDate)) {
                    Schedule temp = new Schedule(arr[0], arr[1], arr[2], arr[3]);
                    data.add(temp);
                }
            } catch (ParseException e) {
                System.out.println(e);
            }
        }
        return data;
    }

    public void makeAppointment(String patientID, String doctorID, String date, String time, String duration, String status, String description, String scheduleID) throws IOException {
        String appointmentScheduleId = Schedule.getAppointmentScheduleID(doctorID, date);
        String appointmentID = Appointment.getNewAppointmentId();

        Appointment appointment = new Appointment(appointmentID, patientID, doctorID, date, time, duration, status, description, scheduleID);
        appointment.addToAppointmentFile();

        DataHistory.updateDataHistoryCount("appointment");

        Schedule.updateScheduleFile(appointmentID, appointmentScheduleId, time, duration);

        System.out.println("Appointment created: " + appointment);
    }

    public void cancelAppointment(String appointmentId) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        Appointment target = Appointment.findAppointment(appointmentId);
        target.setStatus("cancelled");

        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(appointmentId)) {
                row = target.toString();
            }
            data.add(row);
        }
        Appointment.writeToAppointmentFile(data);
    }

    public ArrayList<MedicalRecord> trackPersonalMedicalRecord(String patientId) throws IOException {
        ArrayList<MedicalRecord> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "medicalRecord");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(patientId)) {
                MedicalRecord placeholder = new MedicalRecord(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
                data.add(placeholder);
            }
        }
        return data;
    }

    public ArrayList<Appointment> trackHistoricalAppointment(String patientId) throws IOException {
        ArrayList<Appointment> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(patientId)) {
                Appointment placeholder = new Appointment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8]);
                data.add(placeholder);
            }
        }
        return data;
    }
}
