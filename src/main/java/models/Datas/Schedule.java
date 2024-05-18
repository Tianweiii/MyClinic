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

    public Schedule(String scheduleID, String doctorID, String date, String timeslots) {
        this.scheduleID = scheduleID;
        this.doctorID = doctorID;
        this.date = date;
        this.timeslots = timeslots;
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
                    if (!time.contains("APT")) {
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
//        int index = 0;
//        for (int i = 0; i < arr.size(); i++) {
//            if (arr.get(i).equals(number)) {
//                index = i;
//                break;
//            } else {
//                index = -1;
//            }
//        }
//        if (Integer.parseInt(arr.get(index + 1)) == Integer.parseInt(number) + 1 && Integer.parseInt(arr.get(index + 2)) == Integer.parseInt(number) + 2) {
//            return "3";
//        } else if (Integer.parseInt(arr.get(index + 1)) == Integer.parseInt(number) + 1) {
//            return "2";
//        } else {
//            return "1";
//        }
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
