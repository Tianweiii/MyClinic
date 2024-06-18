package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class MedicalRecord {
    final private String recordID;
    final private String patientID;
    private String appointmentID;
    private String diagnosis;
    private String medication;
    private String description;

    public MedicalRecord(String recordID, String patientID, String appointmentID, String diagnosis, String medication, String description) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.diagnosis = diagnosis;
        this.medication = medication;
        this.description = description;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}", recordID, patientID, appointmentID, diagnosis, medication, description);
    }

    public String getRecordID() {
        return recordID;
    }
    public String getPatientID() {
        return patientID;
    }
    public String getAppointmentID() {
        return appointmentID;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public String getMedication() {
        return medication;
    }
    public String getDescription() {
        return description;
    }


}
