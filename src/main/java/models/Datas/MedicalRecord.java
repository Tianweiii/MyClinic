package models.Datas;

import models.Filing.FileIO;

import java.text.MessageFormat;

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


}
