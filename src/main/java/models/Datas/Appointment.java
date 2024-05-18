package models.Datas;

import models.Filing.FileIO;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appointment {
    final private String appointmentID;
    final private String patientID;
    final private String doctorID;
    private String date;
    private String time;
    private String duration;
    private String status;
    private String description;
    private String scheduleID;

    public Appointment(String appointmentID, String patientID, String doctorID, String date, String time, String duration, String status, String description, String scheduleID) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.status = status;
        this.description = description;
        this.scheduleID = scheduleID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }
    public String getPatientID() {
        return patientID;
    }
    public String getDoctorID() {
        return doctorID;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getDuration() {
        return duration;
    }
    public String getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
    public String getScheduleID() {
        return scheduleID;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}", appointmentID, patientID, doctorID, date, time, duration, status, description, scheduleID);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ArrayList<Appointment> findAppointment(String date) throws IOException {
        ArrayList<Appointment> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[3].equals(date)) {
                Appointment someAppointment = new Appointment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8]);
                data.add(someAppointment);
            }
        }
        return data;
    }

    public static Appointment findAppointment(String appointmentID, String date) throws IOException {
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(appointmentID) && arr[3].equals(date)) {
                System.out.println("Found data");
                return new Appointment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8]);
            }
        }
        return null;
    }

    public List<Map<String, String>> getPatientAppointment(String patientID) throws IOException {
        List<Map<String, String>> appointmentList = new ArrayList<>();
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(patientID)) {
                Map<String, String> map = new HashMap<>();
                map.put(arr[0], arr[3]);
                appointmentList.add(map);
            }
        }
        return appointmentList;
    }

    public static String getNewAppointmentId() {
        String id;
        try {
            id = DataHistory.getNewId("appointment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return MessageFormat.format("APT{0}", id);
    }

    public void addToAppointmentFile() {
        FileIO appender = new FileIO("a", "appointment");
        appender.appendFile(toString() + "\n");
        System.out.println("Created walk-in appointment");
    }

    public static void writeToAppointmentFile(ArrayList<String> data) {
        FileIO writer = new FileIO("w", "appointment");
        writer.writeFile(data);
    }


}
