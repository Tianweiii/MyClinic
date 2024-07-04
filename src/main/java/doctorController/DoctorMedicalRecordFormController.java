package doctorController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Datas.DataHistory;
import models.Datas.Medicine;
import models.Users.Doctor;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class DoctorMedicalRecordFormController implements Initializable {

    Doctor doctor = Cookie.identityDoctor;

    @FXML
    private ComboBox<String> AID;

    @FXML
    private Button AddButton;

    @FXML
    private TextArea Desc;

    @FXML
    private TextArea Diagnosis;

    @FXML
    private Button ListButton;

    @FXML
    private ComboBox<String> Med1;

    @FXML
    private ComboBox<String> Med2;

    @FXML
    private ComboBox<String> Med3;

    @FXML
    private ComboBox<String> Med4;

    @FXML
    private ComboBox<String> Med5;

    @FXML
    private ComboBox<String> Med6;

    @FXML
    private TextArea PC;

    @FXML
    private Label PID;

    @FXML
    private Label RID;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Medicine> medicines = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        this.doctor = Cookie.identityDoctor;
        loadAppointments();
        populateAppointmentComboBox();
        setAIDComboBoxListener();
        autoGenerateRID();
        loadMedicines();
        populateMedicineComboBoxes();

    }

    private void loadAppointments() {
        String filePath = "src/main/java/models/TextFiles/appointment";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                Appointment appointment = new Appointment(details[0], details[1], details[2], details[3], details[4], details[5], details[6], details[7], details[8]);
                appointments.add(appointment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateAppointmentComboBox() {
        ObservableList<String> appointmentIDs = FXCollections.observableArrayList();
        for (Appointment appointment : appointments) {
            appointmentIDs.add(appointment.getAppointmentID());
        }
        AID.setItems(appointmentIDs);
    }

    private void setAIDComboBoxListener() {
        AID.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedAID = AID.getSelectionModel().getSelectedItem();
                for (Appointment appointment : appointments) {
                    if (appointment.getAppointmentID().equals(selectedAID)) {
                        PID.setText(appointment.getPatientID());
                        break;
                    }
                }
            }
        });
    }

    private void autoGenerateRID() {
        try {
            String newId = DataHistory.getNewId("medicalRecord");
            RID.setText("MR" + newId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMedicines() {
        String filePath = "src/main/java/models/TextFiles/medicine";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");

                Medicine medicine = new Medicine(details[0], details[1], details[2], details[3]);
                medicines.add(medicine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateMedicineComboBoxes() {
        ObservableList<String> medicineNames = FXCollections.observableArrayList();
        for (Medicine medicine : medicines) {
            medicineNames.add(medicine.getName());
        }
        Med1.setItems(medicineNames);
        Med2.setItems(medicineNames);
        Med3.setItems(medicineNames);
        Med4.setItems(medicineNames);
        Med5.setItems(medicineNames);
        Med6.setItems(medicineNames);
    }

    @FXML
    private void addNewMedicalRecord() throws IOException {
        if (this.doctor == null) {
            System.err.println("Cannot add medical record. Doctor is not logged in or not properly initialized.");
            return;
        }

        String recordID = RID.getText();
        String patientID = PID.getText();
        String appointmentID = AID.getValue();
        String diagnosis = Diagnosis.getText();

        if (appointmentID == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("MUST SELECTED AN APPOINTMENT");
            alert.setContentText("Please select an appointment.");
            alert.showAndWait();
            return;
        }

        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Diagnosis is required");
            alert.setContentText("Please enter a diagnosis.");
            alert.showAndWait();
            return;
        }

        List<String> medications = new ArrayList<>();
        medications.add(Med1.getValue());
        medications.add(Med2.getValue());
        medications.add(Med3.getValue());
        medications.add(Med4.getValue());
        medications.add(Med5.getValue());
        medications.add(Med6.getValue());
        String medicationsStr = String.join("-", medications);

        String description = Desc.getText();
        if (description == null || description.trim().isEmpty()) {
            description = "null";
        } else {
            description += "\n";
        }

        // Call the method from the Doctor class
        doctor.addMedicalRecord(recordID, patientID, appointmentID, diagnosis, medicationsStr, description);

        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ADD Record");
        alert.setHeaderText("Added record successfully");
        alert.setContentText("RecordID: " + recordID);
        alert.showAndWait();

        autoGenerateRID();
        AID.setValue(null);
        Diagnosis.clear();
        Med1.setValue(null);
        Med2.setValue(null);
        Med3.setValue(null);
        Med4.setValue(null);
        Med5.setValue(null);
        Med6.setValue(null);
        Desc.clear();
    }
}