package patientController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Filing.FileIO;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HistoricalAppointmentController implements Initializable {

    Patient patient = Cookie.identityPatient;
    String patientId = patient.getID();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        scheduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));

        addAppointmentToTable();
    }

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> doctorIdColumn;
    @FXML
    private TableColumn<Appointment, String> durationColumn;
    @FXML
    private TableColumn<Appointment, String> patientIdColumn;
    @FXML
    private TableColumn<Appointment, String> scheduleIdColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;

    public void addAppointmentToTable() {
        try {
            appointmentTable.setItems(patient.trackHistoricalAppointment(patientId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !appointmentTable.getSelectionModel().isEmpty()) {
            Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
            try {
                setDoctorData(selected.getDoctorID());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            aptIdField.setText(selected.getAppointmentID());
            dateField.setText(selected.getDate());
            descriptionField.setText(selected.getDescription());
            durationField.setText(selected.getDuration());
            statusField.setText(selected.getStatus());
            timeField.setText(selected.getTime());
        }
    }

    @FXML
    private TextField aptIdField;
    @FXML
    private TextField dateField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField timeField;


    @FXML
    private Text docId;
    @FXML
    private Text docName;
    @FXML
    private Text specialization;
    public void setDoctorData(String doctorId) throws IOException {
        FileIO reader = new FileIO("r", "doctor");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(doctorId)) {
                docId.setText(arr[0]);
                docName.setText(arr[1]);
                specialization.setText(arr[6]);
            }
        }
    }

}
