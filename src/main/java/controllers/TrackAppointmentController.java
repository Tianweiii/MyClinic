package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Filing.FileIO;
import models.Users.Admin;
import models.Users.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class TrackAppointmentController implements Initializable {

    Admin admin = Cookie.identityAdmin;
    private String cache;
    private FilteredList<Appointment> filteredList;

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

        addToDailyAppointmentTable();
        try {
            setPieChartData();
            filteredList = new FilteredList<>(getAllDailyAppointment(), predicate -> true);
            dailyAppointmentTable.setItems(filteredList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchAppointments(newValue));
    }

    @FXML
    private TableView<Appointment> dailyAppointmentTable;
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
    private TableColumn<Appointment, String> statusColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    ObservableList<Appointment> getAllDailyAppointment() throws IOException {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        String currentDate = formatter.format(date);

        ObservableList<Appointment> dailyAppointments = FXCollections.observableArrayList();
//        ArrayList<Appointment> appointments = Appointment.findAppointment(currentDate);
//        ArrayList<Appointment> appointments = admin.trackDailyAppointment();
        dailyAppointments.addAll(admin.trackDailyAppointment());

        return dailyAppointments;
    }
    public void addToDailyAppointmentTable() {
        try {
            dailyAppointmentTable.setItems(getAllDailyAppointment());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) throws IOException {
        if (e.getClickCount() == 1 && !dailyAppointmentTable.getSelectionModel().isEmpty()) {
            Appointment selection = dailyAppointmentTable.getSelectionModel().getSelectedItem();
            setPatientData(selection.getPatientID());
            setDoctorData(selection.getDoctorID());
            setAppointmentData(selection.getAppointmentID());
        }
    }

    @FXML
    private Text patientId;
    @FXML
    private Text patientUsername;
    @FXML
    private Text medicalCase;
    public void setPatientData(String patientID) throws IOException {
        for (String row : User.getAccounts("patient")) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(patientID)) {
                patientId.setText(arr[0]);
                patientUsername.setText(arr[1]);
                medicalCase.setText(arr[6]);
            }
        }
    }
    public void resetPatientData() {
        patientId.setText("Patient ID");
        patientUsername.setText("Username");
        medicalCase.setText("Medical Case");
    }

    @FXML
    private Text doctorId;
    @FXML
    private Text doctorUsername;
    @FXML
    private Text specialization;
    public void setDoctorData(String doctorID) throws IOException {
        for (String row : User.getAccounts("doctor")) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(doctorID)) {
                doctorId.setText(arr[0]);
                doctorUsername.setText(arr[1]);
                specialization.setText(arr[6]);
            }
        }
    }
    public void resetDoctorData() {
        doctorId.setText("Doctor ID");
        doctorUsername.setText("Username");
        specialization.setText("Specialization");
    }

    @FXML
    private TextField searchBar;
    public void searchAppointments(String input) {
        String keyword = input.toLowerCase();
        filteredList.setPredicate(data -> {
            if (input.isEmpty() || input.isBlank() || input == null) {
                return true;
            }

            if (data.getAppointmentID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getPatientID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDoctorID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDate().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getTime().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDuration().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getStatus().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDescription().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getScheduleID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else {
                return false;
            }
        });
        SortedList<Appointment> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(dailyAppointmentTable.comparatorProperty());
        dailyAppointmentTable.setItems(sortedData);
    }

    @FXML
    private VBox chartVBox;
    public void setPieChartData() throws IOException {
        double toDo = Appointment.getToDoAppointment();
        double completed = Appointment.getCompletedDailyAppointment();
        PieChart appointmentPieChart = new PieChart();
        appointmentPieChart.setTitle("Appointment PieChart");

        if (toDo == 0 && completed == 0) {
            Text message = new Text("No appointments today");
            chartVBox.getChildren().add(message);
        } else {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("To complete", toDo),
                    new PieChart.Data("Complete", completed)
            );
            appointmentPieChart.setData(pieChartData);
            chartVBox.getChildren().add(appointmentPieChart);
        }
    }

    @FXML
    private Text appointmentDate;
    @FXML
    private Text appointmentDescription;
    @FXML
    private Text appointmentDuration;
    @FXML
    private Text appointmentId;
    @FXML
    private ComboBox<String> appointmentStatusComboBox;
    @FXML
    private Text appointmentTime;
    public void setAppointmentData(String aptId) throws IOException {
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(aptId)) {
                appointmentId.setText(arr[0]);
                appointmentDate.setText(arr[3]);
                appointmentTime.setText(arr[4]);
                appointmentDuration.setText(arr[5]);
                appointmentStatusComboBox.setValue(arr[6]);
                cache = arr[6];
                appointmentDescription.setText(arr[7]);
                initializeComboBox(cache);
            }
        }
    }
    public void initializeComboBox(String current) {
        if (!current.equals("walk-in")) {
            appointmentStatusComboBox.setDisable(false);
            appointmentStatusComboBox.setItems(FXCollections.observableArrayList("completed", "missed", "cancelled"));
            editButton.setDisable(false);
        } else {
            appointmentStatusComboBox.setDisable(true);
            editButton.setDisable(true);
        }
    }
    public void resetAppointmentData() {
        appointmentId.setText("Choose APT");
        appointmentDate.setText("Choose APT");
        appointmentTime.setText("Choose APT");
        appointmentDuration.setText("Choose APT");
        appointmentDescription.setText("Choose APT");
        appointmentStatusComboBox.setValue(null);
        appointmentStatusComboBox.setDisable(true);
    }

    @FXML
    private Button editButton;
    public void editAppointmentStatus() throws IOException {
        String currentApt = appointmentId.getText();
        String newStatus = appointmentStatusComboBox.getValue();
        if (!newStatus.equals(cache)) {
            admin.manageAppointment(currentApt, appointmentDate.getText(), newStatus);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Edit successful");
            alert.setContentText("Appointment status has been successfully updated.");
            alert.showAndWait();
            addToDailyAppointmentTable();
            chartVBox.getChildren().clear();
            setPieChartData();
            resetFields();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit error");
            alert.setContentText("Status is the same as previous");
            alert.showAndWait();
        }
    }
    public void resetFields() {
        resetPatientData();
        resetDoctorData();
        resetAppointmentData();
        dailyAppointmentTable.getSelectionModel().clearSelection();
        editButton.setDisable(true);
    }
}
