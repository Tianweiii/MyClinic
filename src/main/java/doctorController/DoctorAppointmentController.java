package doctorController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Datas.MedicalRecord;
import models.Filing.FileIO;
import models.Users.Doctor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DoctorAppointmentController implements Initializable {
    @FXML
    private TableColumn<Appointment, String> AAID;

    @FXML
    private TableColumn<Appointment, String> ADID;

    @FXML
    private TableColumn<Appointment, String> ADate;

    @FXML
    private TableColumn<Appointment, String> ADesc;

    @FXML
    private TableColumn<Appointment, String> ADuration;

    @FXML
    private TableColumn<Appointment, String> APID;

    @FXML
    private TableColumn<Appointment, String> ASID;

    @FXML
    private TableColumn<Appointment, String> AStatus;

    @FXML
    private TableColumn<Appointment, String> ATime;

    @FXML
    private TableView<Appointment> AppTable;

    @FXML
    private Button CancelButton;

    @FXML
    private TextField SearchApp;

    @FXML
    private Label showAID;

    @FXML
    private Label showDID;

    @FXML
    private Label showDate;

    @FXML
    private Label showDesc;

    @FXML
    private Label showDuration;

    @FXML
    private Label showPID;

    @FXML
    private Label showSID;

    @FXML
    private Label showStatus;

    @FXML
    private Label showTime;

    private ObservableList<Appointment> appointments;
    Integer index;
    Doctor doctor = Cookie.identityDoctor;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AAID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        APID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        ADID.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        ADate.setCellValueFactory(new PropertyValueFactory<>("date"));
        ATime.setCellValueFactory(new PropertyValueFactory<>("time"));
        ADuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        AStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ADesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        ASID.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));

        //Load and filter data
        appointments = loadAppointment();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments, b -> true);

        //Set the filtered data to the table
        AppTable.setItems(filteredData);

        //Add listener for search field
        SearchApp.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (record.getAppointmentID().toLowerCase().contains(searchKeyword) ||
                        record.getPatientID().toLowerCase().contains(searchKeyword) ||
                        record.getDoctorID().toLowerCase().contains(searchKeyword) ||
                        record.getDate().toLowerCase().contains(searchKeyword) ||
                        record.getTime().toLowerCase().contains(searchKeyword) ||
                        record.getDuration().toLowerCase().contains(searchKeyword) ||
                        record.getStatus().toLowerCase().contains(searchKeyword) ||
                        record.getDescription().toLowerCase().contains(searchKeyword) ||
                        record.getScheduleID().toLowerCase().contains(searchKeyword)) {
                    return true;
                }
                return false;
            });
        });
    }

    private ObservableList<Appointment> loadAppointment() {
        //load data from text file
        ObservableList<Appointment> records = FXCollections.observableArrayList();
        FileIO reader = new FileIO("r", "appointment");
        try {
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                Appointment temp = new Appointment(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8]);
                records.add(temp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    @FXML
    public void getApp() {
        index = AppTable.getSelectionModel().getFocusedIndex();

        if (index <= -1) {
            return;
        }
        showAID.setText(AAID.getCellData(index).toString());
        showPID.setText(APID.getCellData(index).toString());
        showDID.setText(ADID.getCellData(index).toString());
        showDate.setText(ADate.getCellData(index).toString());
        showDuration.setText(ADuration.getCellData(index).toString());
        showTime.setText(ATime.getCellData(index).toString());
        showStatus.setText(AStatus.getCellData(index).toString());
        showDesc.setText(ADesc.getCellData(index).toString());
        showSID.setText(ASID.getCellData(index).toString());

        SearchApp.setText("");
    }

    @FXML
    public void cancelAppointment() {
        index = AppTable.getSelectionModel().getFocusedIndex();
        if (index <= -1) {
            return;
        }

        String appointmentID = AAID.getCellData(index).toString();
//        Doctor doctor = new Doctor("DT4", "Alicia", "123", "25/8/2004", "female", "doctor", "brain"); // Assume you have a way to initialize or retrieve the current doctor object

        try {
            doctor.cancelAppointment(appointmentID);
            // Update the status in the observable list
            appointments.get(index).setStatus("cancelled");

            // Refresh the table view
            AppTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
