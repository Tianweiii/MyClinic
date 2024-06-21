package patientController;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Datas.MedicalRecord;
import models.Filing.FileIO;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientMedicalRecordController implements Initializable {

    Patient patient = Cookie.identityPatient;
    String patientId = patient.getID();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        recordIdColumn.setCellValueFactory(new PropertyValueFactory<>("recordID"));

        addToTable();
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
    private TableColumn<MedicalRecord, String> patientIdColumn;
    @FXML
    private TableColumn<MedicalRecord, String> recordIdColumn;
    public void addToTable() {
        try {
            medicalRecordTable.setItems(patient.trackPersonalMedicalRecord(patientId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !medicalRecordTable.getSelectionModel().isEmpty()) {
            MedicalRecord selected = medicalRecordTable.getSelectionModel().getSelectedItem();
            try {
                setAppointmentData(selected.getAppointmentID());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            recordIdField.setText(selected.getRecordID());
            diagnosisField.setText(selected.getDiagnosis());
            reportDescriptionField.setText(selected.getDescription());
            setMedication(selected.getMedication());
        }

    }

    @FXML
    private TextField aptIdField;
    @FXML
    private TextField dateField;
    @FXML
    private TextField appointmentDescriptionField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField timeField;
    public void setAppointmentData(String appointmentId) throws IOException {
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(appointmentId)) {
                aptIdField.setText(arr[0]);
                dateField.setText(arr[3]);
                timeField.setText(arr[4]);
                durationField.setText(arr[5]);
                statusField.setText(arr[6]);
                appointmentDescriptionField.setText(arr[7]);
            }
        }
    }

    @FXML
    private TextField diagnosisField;
    @FXML
    private VBox medicationVBox;
    @FXML
    private TextField recordIdField;
    @FXML
    private TextArea reportDescriptionField;
    public void setMedication(String data) {
        String[] medications = data.split("-");
        for (String medication : medications) {
            Text text = new Text(medication);
            medicationVBox.getChildren().add(text);
        }
    }
}
