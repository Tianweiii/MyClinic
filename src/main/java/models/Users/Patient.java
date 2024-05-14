package models.Users;

import models.Filing.FileIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Map<String, String>> getAllPatients() throws IOException {
        List<Map<String, String>> patientList = new ArrayList<>();
        FileIO reader = new FileIO("r", "patient");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            Map<String, String> map = new HashMap<>();
            map.put(arr[0], arr[1]);
            patientList.add(map);
        }
        return patientList;
    }
}
