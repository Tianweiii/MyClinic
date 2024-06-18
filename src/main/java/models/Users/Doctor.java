package models.Users;

import models.Datas.Appointment;
import models.Datas.DataHistory;
import models.Datas.MedicalRecord;
import models.Datas.Schedule;
import models.Filing.FileIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    final private String specialization;

    public Doctor(String ID, String username, String password, String dateOfBirth, String gender, String role, String specialization) {
        super(ID, username, password, dateOfBirth, gender, role);
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void cancelAppointment(String appointmentID) throws IOException {
        ArrayList<String> newData = new ArrayList<>();
        Appointment target = Appointment.findAppointment(appointmentID);
        target.setStatus("cancelled");
        String data = target.toString();
        System.out.println(data);
        FileIO reader = new FileIO("r", "appointment");
        List<String> rows = reader.readFile();
        for (String row : rows) {
            String[] arr = row.split(", ");
            if (arr[0].equals(target.getAppointmentID())) {
                row = data;
            }
            newData.add(row);
        }
        FileIO writer = new FileIO("w", "appointment");
        writer.writeFile(newData);
    }

    public void viewIndividualAppointment(String doctorID) throws IOException{
        ArrayList<String> data = new ArrayList<>();
        FileIO reader = new FileIO("r","appointment");
        List<String> rows = reader.readFile();
        for (String row : rows) {
            String[] arr = row.split(", ");
            if (arr[2].equals(doctorID)) {
                System.out.println(row);
                data.add(row);
            }
        }
    }

    public void addSchedule(String scheduleId, String doctorId, String date, String timeslot) throws IOException {
        Schedule sc = new Schedule(scheduleId, doctorId, date, timeslot);
        String data = sc.toString();
        System.out.println(sc);
        FileIO appender = new FileIO("a","schedule");
        appender.appendFile(data);

        DataHistory.updateDataHistoryCount("schedule");
    }

    public void viewMedicalRecord(String patientID) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "medicalRecord");
        List<String> rows = reader.readFile();
        for (String row : rows) {
            String[] arr = row.split(", ");
            if (arr[1].equals(patientID)) {
                System.out.println(row);
                data.add(row);
            }

        }
    }

    public void addMedicalRecord(String recordID, String patientID, String appointmentID, String diagnosis, String medication, String description) throws IOException {
        MedicalRecord mr = new MedicalRecord(recordID, patientID, appointmentID, diagnosis, medication, description);
        String data = mr.toString();
        FileIO appender = new FileIO("a", "medicalRecord");
        appender.appendFile(data);
        // Update data history
        DataHistory.updateDataHistoryCount("medicalRecord");
    }
    public void addMedicalRecord(MedicalRecord record) {
    }
}
