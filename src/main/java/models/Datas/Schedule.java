package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
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

    public static String getAppointmentScheduleID(String date) throws IOException {
        FileIO reader = new FileIO("r", "schedule");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[2].equals(date)) {
                return arr[0];
            }
        }
        return null;
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
