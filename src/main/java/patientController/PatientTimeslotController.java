package patientController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Datas.Schedule;
import models.Filing.FileIO;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientTimeslotController implements Initializable {

    Patient patient = Cookie.identityPatient;
    String patientId = patient.getID();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scheduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        setScheduleTable();

        C9.setCellValueFactory(new PropertyValueFactory<>("nineAM"));
        C10.setCellValueFactory(new PropertyValueFactory<>("tenAM"));
        C11.setCellValueFactory(new PropertyValueFactory<>("elevenAM"));
        C12.setCellValueFactory(new PropertyValueFactory<>("twelvePM"));
        C13.setCellValueFactory(new PropertyValueFactory<>("onePM"));
        C14.setCellValueFactory(new PropertyValueFactory<>("twoPM"));
        C15.setCellValueFactory(new PropertyValueFactory<>("threePM"));
        C16.setCellValueFactory(new PropertyValueFactory<>("fourPM"));
        C17.setCellValueFactory(new PropertyValueFactory<>("fivePM"));
    }

    @FXML
    private TableView<Schedule> scheduleTable;
    @FXML
    private TableColumn<Schedule, String> doctorIdColumn;
    @FXML
    private TableColumn<Schedule, String> dateColumn;
    @FXML
    private TableColumn<Schedule, String> scheduleIdColumn;
    public void setScheduleTable() {
        try {
            scheduleTable.setItems(patient.viewTimeslot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !scheduleTable.getSelectionModel().isEmpty()) {
            Schedule selected = scheduleTable.getSelectionModel().getSelectedItem();

            try {
                setDoctorData(selected.getDoctorID());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            setTimeslotTable(selected);
            aptCountField.setText("This schedule has " + getAppointmentCount(selected.getTimeslots()) + " existing appointments");
        }
    }

    @FXML
    private TableView<Schedule> timeslotTable;
    @FXML
    private TableColumn<Schedule, String> C9;
    @FXML
    private TableColumn<Schedule, String> C10;
    @FXML
    private TableColumn<Schedule, String> C11;
    @FXML
    private TableColumn<Schedule, String> C12;
    @FXML
    private TableColumn<Schedule, String> C13;
    @FXML
    private TableColumn<Schedule, String> C14;
    @FXML
    private TableColumn<Schedule, String> C15;
    @FXML
    private TableColumn<Schedule, String> C16;
    @FXML
    private TableColumn<Schedule, String> C17;
    public void setTimeslotTable(Schedule data) {
        timeslotTable.getItems().clear();
        ObservableList<Schedule> list = FXCollections.observableArrayList();
        list.add(data);
        timeslotTable.setItems(list);
    }

    @FXML
    private Text aptCountField;
    public int getAppointmentCount(String timeslots) {
        int count = 0;
        ArrayList<String> cache = new ArrayList<>();
        String[] arr = FileIO.splitArray(timeslots);
        for (String time : arr) {
            if (time.contains("APT") && !cache.contains(time)) {
                cache.add(time);
                count++;
            }
        }
        return count;
    }

    @FXML
    private Text docGenderField;
    @FXML
    private Text docIdField;
    @FXML
    private Text docNameField;
    @FXML
    private Text docSpecializationField;
    public void setDoctorData(String doctorId) throws IOException {
        FileIO reader = new FileIO("r", "doctor");
        for (String row : reader.readFile()) {
            String[] data = FileIO.splitString(row);
            if (data[0].equals(doctorId)) {
                docIdField.setText(data[0]);
                docNameField.setText(data[1]);
                docGenderField.setText(data[4]);
                docSpecializationField.setText(data[6]);
            }
        }
    }
}
