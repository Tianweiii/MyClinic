package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Schedule {
    private String scheduleID;
    private String doctorID;
    private String date;
    private String timeslots;

    private String nineAM;
    private String tenAM;
    private String elevenAM;
    private String twelvePM;
    private String onePM;
    private String twoPM;
    private String threePM;
    private String fourPM;
    private String fivePM;

    public Schedule(String scheduleID, String doctorID, String date, String timeslots) {
        this.scheduleID = scheduleID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeslots = timeslots;
        parseTimeslots(timeslots);
    }

    private void parseTimeslots(String timeslots) {
        timeslots = timeslots.substring(1, timeslots.length() - 1);
        String[] slots = timeslots.split("-");

        if (slots.length >= 9) {
            this.nineAM = slots[0].trim();     // Slot number or APT code
            this.tenAM = slots[1].trim();      // APT code
            this.elevenAM = slots[2].trim();   // APT code or slot number
            this.twelvePM = slots[3].trim();   // Slot number
            this.onePM = slots[4].trim();      // Slot number
            this.twoPM = slots[5].trim();      // Slot number
            this.threePM = slots[6].trim();    // Slot number
            this.fourPM = slots[7].trim();     // Slot number
            this.fivePM = slots[8].trim();     // Slot number
        }
    }

    public String setScheduleID() {
        return scheduleID;
    }
    public String getScheduleID() {
        return scheduleID;
    }
    public String setDoctorID() {
        return scheduleID;
    }
    public String getDoctorID() {
        return doctorID;
    }
    public String setDate() { return date; }
    public String getDate() {
        return date;
    }
    public String setTimeslots() {return timeslots;}
    public String getTimeslots() {
        return timeslots;
    }
    public String getNineAM() {
        return nineAM;
    }
    public String getTenAM() {
        return tenAM;
    }
    public String getElevenAM() {
        return elevenAM;
    }
    public String getTwelvePM() {
        return twelvePM;
    }
    public String getOnePM() {
        return onePM;
    }
    public String getTwoPM() {
        return twoPM;
    }
    public String getThreePM() {
        return threePM;
    }
    public String getFourPM() {
        return fourPM;
    }
    public String getFivePM() {
        return fivePM;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}", scheduleID, doctorID, date, timeslots);
    }

    public static void writeToScheduleFile(ArrayList<String> data) {
        FileIO writer = new FileIO("w", "schedule");
        writer.writeFile(data);
    }

    public static String getAppointmentScheduleID(String doctorId, String date) throws IOException {
        FileIO reader = new FileIO("r", "schedule");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(doctorId) && arr[2].equals(date)) {
                return arr[0];
            }
        }
        return null;
    }

    public static ArrayList<String> getScheduleTimeslot(String doctorId, String date) throws IOException {
        ArrayList<String> availableTimes = new ArrayList<>();
        FileIO reader = new FileIO("r", "schedule");
        for (String row : reader.readFile()) {
            String[] data = FileIO.splitString(row);
            if (data[1].equals(doctorId) && data[2].equals(date)) {
                String[] times = FileIO.splitArray(data[3]);
                for (String time : times) {
                    if (!time.contains("APT") || !time.contains("C")) {
                        availableTimes.add(time);
                    }
                }
            }
        }
        return availableTimes;
    }

    public static String getAvailableDuration(String doctorId, String date, String time) throws IOException {
        ArrayList<String> arr = new ArrayList<>();
        String availableDuration = "0";
        FileIO reader = new FileIO("r", "schedule");
        for (String row : reader.readFile()) {
            String[] data = FileIO.splitString(row);
            if (data[1].equals(doctorId) && data[2].equals(date)) {
                String[] times = FileIO.splitArray(data[3]);
                for (String i : times) {
                    if (!i.contains("APT")) {
                        arr.add(i);
                    }
                }
                availableDuration = checkNumbersAfter(arr, time);
            }
        }
        return availableDuration;
    }

    public static String checkNumbersAfter(ArrayList<String> arr, String number) {
        String data = "1";
        String second = String.valueOf(Integer.parseInt(number) + 1);
        String third = String.valueOf(Integer.parseInt(number) + 2);

        for (String i : arr) {
            if (i.equals(second)) {
                data = "2";
            } else if (i.equals(third)) {
                data = "3";
            }
        }
        return data;
    }

    public static void updateScheduleFile(String appointmentID, String scheduleID, String time, String duration) throws IOException {
        duration = duration.replace("h", "");
        ArrayList<String> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "schedule");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(scheduleID)) {
                arr[3] = FileIO.updateArrayData(appointmentID, arr[3], time, duration);
                System.out.println(row);
                row = MessageFormat.format("{0}, {1}, {2}, {3}", arr[0], arr[1], arr[2], arr[3]);
            }
            data.add(row);
        }
        writeToScheduleFile(data);
    }

}
