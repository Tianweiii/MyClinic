package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Datas.MedicalRecord;
import models.Filing.FileIO;
import models.Users.Admin;
import models.Users.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TrackMedRecController implements Initializable {

    Admin admin = Cookie.identityAdmin;
    private FilteredList<MedicalRecord> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicalRecordIdColumn.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        medicationColumn.setCellValueFactory(new PropertyValueFactory<>("medication"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        addToMedicalRecordTable();
        try {
            filteredList = new FilteredList<>(getAllMedicalRecord(), predicate -> true);
            medicalRecordTable.setItems(filteredList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchMedicalRecords(newValue));
    }

    @FXML
    private TextField searchBar;
    public void searchMedicalRecords(String input) {
        String keyword = input.toLowerCase();
        filteredList.setPredicate(data -> {
            if (input.isEmpty() || input.isBlank() || input == null) {
                return true;
            }

            if (data.getRecordID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getPatientID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getAppointmentID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDiagnosis().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDescription().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else {
                return false;
            }
        });
        SortedList<MedicalRecord> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(medicalRecordTable.comparatorProperty());
        medicalRecordTable.setItems(sortedData);
    }

    @FXML
    private TableView<MedicalRecord> medicalRecordTable;
    @FXML
    private TableColumn<MedicalRecord, String> appointmentIdColumn;
    @FXML
    private TableColumn<MedicalRecord, String> descriptionColumn;
    @FXML
    private TableColumn<MedicalRecord, String> diagnosisColumn;
    @FXML
    private TableColumn<MedicalRecord, String> medicalRecordIdColumn;
    @FXML
    private TableColumn<MedicalRecord, String> medicationColumn;
    @FXML
    private TableColumn<MedicalRecord, String> patientIdColumn;
    ObservableList<MedicalRecord> getAllMedicalRecord() throws IOException {
        ObservableList<MedicalRecord> medicalRecords = FXCollections.observableArrayList();
        medicalRecords.addAll(admin.trackMedicalRecord());
        return medicalRecords;
    }
    public void addToMedicalRecordTable() {
        try {
            medicalRecordTable.setItems(getAllMedicalRecord());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) throws IOException {
        if (e.getClickCount() == 1 && !medicalRecordTable.getSelectionModel().isEmpty()) {
            MedicalRecord selection = medicalRecordTable.getSelectionModel().getSelectedItem();
            resetMedication();
            setPatientData(selection.getPatientID());
            setMedicalRecordData(selection.getRecordID());
            setMedicationData(selection.getMedication());
        }
    }

    @FXML
    private Text recordIdTextField;
    @FXML
    private Text appointmentIdTextField;
    @FXML
    private Text diagnosisTextField;
    @FXML
    private TextArea descriptionTextArea;
    public void setMedicalRecordData(String medicalRecordId) throws IOException {
        FileIO reader = new FileIO("r", "medicalRecord");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(medicalRecordId)) {
                recordIdTextField.setText(arr[0]);
                appointmentIdTextField.setText(arr[2]);
                diagnosisTextField.setText(arr[3]);
                descriptionTextArea.setText(arr[5]);
            }
        }
    }
    public void resetMedicalRecord() {
        recordIdTextField.setText("Select Record");
        appointmentIdTextField.setText("Select Record");
        diagnosisTextField.setText("Select Record");
        descriptionTextArea.setText("");
    }

    @FXML
    private VBox medicationVBox;
    public void setMedicationData(String medications) {
//        String[] data = FileIO.splitArray(medications);
        String[] data = medications.split("-");
        for (String medication : data) {
            Text text = new Text(medication);
            text.setFont(Font.font("Arial Hebrew", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            medicationVBox.getChildren().add(text);
        }
    }
    public void resetMedication() {
        medicationVBox.getChildren().clear();
    }

    @FXML
    private Text medicalCaseTextField;
    @FXML
    private Text patientIdTextField;
    @FXML
    private Text patientNameTextField;
    public void setPatientData(String patientID) throws IOException {
        for (String row : User.getAccounts("patient")) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(patientID)) {
                patientIdTextField.setText(arr[0]);
                patientNameTextField.setText(arr[1]);
                medicalCaseTextField.setText(arr[6]);
            }
        }
    }
    public void resetPatientData() {
        patientIdTextField.setText("Patient ID");
        patientNameTextField.setText("Patient Name");
        medicalCaseTextField.setText("Medical Case");
    }

    @FXML
    private Button resetButton;
    public void resetFields() {
        resetPatientData();
        resetMedicalRecord();
        resetMedication();
        medicalRecordTable.getSelectionModel().clearSelection();
    }
}
