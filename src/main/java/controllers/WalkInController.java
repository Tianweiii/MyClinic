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
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Auth.Verification;
import models.Datas.Appointment;
import models.Datas.DataHistory;
import models.Datas.Schedule;
import models.Filing.FileIO;
import models.Users.Admin;
import models.Users.Patient;
import models.Users.User;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class WalkInController implements Initializable {

    Admin admin = Cookie.identityAdmin;
    private FilteredList<Appointment> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentPatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        appointmentDoctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        appointmentDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        appointmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentScheduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));

        addWalkInAppointmentToTable();
        setWalkInAppointmentIdField();
        try {
            setPatientIdComboBox();
            setDoctorIdComboBox();
            filteredList = new FilteredList<>(getAllWalkInAppointment(), predicate -> true);
            walkInAppointmentTable.setItems(filteredList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setDateTodayField();

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchAppointments(newValue));

        patientIdComboBox.setOnAction(e -> {
            if (patientIdComboBox.getValue() != null) {
                try {
                    setPatientData(patientIdComboBox.getValue().split(" ")[0]);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        doctorIdComboBox.setOnAction(e -> {
            if (doctorIdComboBox.getValue() != null) {
                try {
                    setDoctorData(doctorIdComboBox.getValue().split(" ")[0]);
                    setTimeComboBox();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        timeComboBox.setOnAction(e -> {
            if (timeComboBox.getValue() != null) {
                setTimeDurationComboBox();
            }
        });
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
        sortedData.comparatorProperty().bind(walkInAppointmentTable.comparatorProperty());
        walkInAppointmentTable.setItems(sortedData);
    }

    @FXML
    private TableView<Appointment> walkInAppointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDoctorIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDurationColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentPatientIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentScheduleIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentStatusColumn;
    @FXML
    private TableColumn<Appointment, ?> appointmentTimeColumn;
    ObservableList<Appointment> getAllWalkInAppointment() throws IOException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] data = FileIO.splitString(row);
            if (data[6].equals("walk-in")) {
                Appointment apt = new Appointment(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
                appointments.add(apt);
            }
        }
        return appointments;
    }
    public void addWalkInAppointmentToTable() {
        try {
            walkInAppointmentTable.setItems(getAllWalkInAppointment());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) {
        addButton.setDisable(true);
        cancelButton.setDisable(false);
        if (e.getClickCount() == 1 && !walkInAppointmentTable.getSelectionModel().isEmpty()) {
            Appointment selected = walkInAppointmentTable.getSelectionModel().getSelectedItem();
            try {
                setPatientData(selected.getPatientID());
                setDoctorData(selected.getDoctorID());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            appointmentIdField.setText(selected.getAppointmentID());
            patientIdComboBox.setValue(selected.getPatientID());
            patientIdComboBox.setDisable(true);
            doctorIdComboBox.setValue(selected.getDoctorID());
            doctorIdComboBox.setDisable(true);
            dateTodayField.setText(selected.getDate());
            timeComboBox.setValue(selected.getTime());
            timeComboBox.setDisable(true);
            durationComboBox.setValue(selected.getDuration());
            durationComboBox.setDisable(true);
            appointmentDescriptionTextField.setText(selected.getDescription());
        }
    }

    @FXML
    private Text patientMedicalCase;
    @FXML
    private Text patientName;
    @FXML
    private Text patientAge;
    @FXML
    private Text patientGender;
    @FXML
    private Text patientId;
    public void setPatientData(String patId) throws IOException {
        patientId.setText(patId);
        FileIO reader = new FileIO("r", "patient");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(patId)) {
                patientName.setText(arr[1]);
                patientAge.setText(User.getAge(arr[3]));
                patientGender.setText(arr[4]);
                patientMedicalCase.setText(arr[6]);
                return;
            }
        }
    }
    public void resetPatientData() {
        patientId.setText("Choose Appointment");
        patientName.setText("Choose Appointment");
        patientAge.setText("Choose Appointment");
        patientGender.setText("Choose Appointment");
        patientMedicalCase.setText("Choose Appointment");
    }

    @FXML
    private Text doctorAge;
    @FXML
    private Text doctorGender;
    @FXML
    private Text doctorId;
    @FXML
    private Text doctorName;
    @FXML
    private Text doctorSpecialization;
    public void setDoctorData(String docId) throws IOException {
        doctorId.setText(docId);
        FileIO reader = new FileIO("r", "doctor");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(docId)) {
                doctorName.setText(arr[1]);
                doctorAge.setText(User.getAge(arr[3]));
                doctorGender.setText(arr[4]);
                doctorSpecialization.setText(arr[6]);
                return;
            }
        }
    }
    public void resetDoctorData() {
        doctorId.setText("Choose Appointment");
        doctorName.setText("Choose Appointment");
        doctorAge.setText("Choose Appointment");
        doctorGender.setText("Choose Appointment");
        doctorSpecialization.setText("Choose Appointment");
    }

    @FXML
    private TextField appointmentDescriptionTextField;
    @FXML
    private Text appointmentIdField;
    @FXML
    private Text dateTodayField;
    @FXML
    private ComboBox<String> doctorIdComboBox;
    @FXML
    private ComboBox<String> durationComboBox;
    @FXML
    private ComboBox<String> patientIdComboBox;
    @FXML
    private ComboBox<String> timeComboBox;
    public void setWalkInAppointmentIdField() {
        String id;
        try {
            id = DataHistory.getNewId("appointment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        appointmentIdField.setText("APT" + String.valueOf(id));
    }
    public void setPatientIdComboBox() throws IOException {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (String patient : User.getAccounts("patient")) {
            String[] row = FileIO.splitString(patient);
            list.add(MessageFormat.format("{0} {1}", row[0], row[1]));
        }
        patientIdComboBox.setItems(list);
    }
    public void setDoctorIdComboBox() throws IOException {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (String doctor : User.getAccounts("doctor")) {
            String[] row = FileIO.splitString(doctor);
            list.add(MessageFormat.format("{0} {1}", row[0], row[1]));
        }
        doctorIdComboBox.setItems(list);
    }
    public void setDateTodayField() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);

        dateTodayField.setText(currentDate);
    }
    public void setTimeDurationComboBox() {
        durationComboBox.getSelectionModel().clearSelection();
        durationComboBox.setDisable(false);
        String duration;
        String doctorId = doctorIdComboBox.getValue().split(" ")[0];
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);
        String time = timeComboBox.getValue();
        try {
            duration = Schedule.getAvailableDuration(doctorId, currentDate, time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        switch (duration) {
            case "2" -> durationComboBox.setItems(FXCollections.observableArrayList("1h", "2h"));
            case "3" -> durationComboBox.setItems(FXCollections.observableArrayList("1h", "2h", "3h"));
            default -> durationComboBox.setItems(FXCollections.observableArrayList("1h"));
        }
    }
    public void setTimeComboBox() throws IOException {
        timeComboBox.getSelectionModel().clearSelection();
        durationComboBox.getSelectionModel().clearSelection();
        durationComboBox.setDisable(true);
        timeComboBox.setDisable(false);
        ObservableList<String> list = FXCollections.observableArrayList();
        String doctorId = doctorIdComboBox.getValue().split(" ")[0];
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);

        list.addAll(Schedule.getScheduleTimeslot(doctorId, currentDate));
        timeComboBox.setItems(list);
    }
    public void resetAppointmentData() {
        timeComboBox.getSelectionModel().clearSelection();
        durationComboBox.getSelectionModel().clearSelection();
        timeComboBox.setDisable(true);
        durationComboBox.setDisable(true);
        patientIdComboBox.setDisable(false);
        doctorIdComboBox.setDisable(false);
        setWalkInAppointmentIdField();
        setDateTodayField();
        patientIdComboBox.setValue(null);
        doctorIdComboBox.setValue(null);
        timeComboBox.setValue(null);
        durationComboBox.setValue(null);
        appointmentDescriptionTextField.setText("");
    }

    @FXML
    private PieChart walkInChart;

    @FXML
    private Button addButton;
    public void addWalkInAppointment() {
        String patientId = "";
        String doctorId = "";
        String time = "";
        String duration = "";
        if (patientIdComboBox.getValue() != null) {
            patientId = patientIdComboBox.getValue().split(" ")[0];
        }
        if (doctorIdComboBox.getValue() != null) {
            doctorId = doctorIdComboBox.getValue().split(" ")[0];
        }
        String date = dateTodayField.getText();
        if (timeComboBox.getValue() != null) {
            time = timeComboBox.getValue();
        }
        if (durationComboBox.getValue() != null) {
            duration = durationComboBox.getValue();
        }
        String description = appointmentDescriptionTextField.getText();

        if (!Verification.verifyEmptyFields(patientId, doctorId, time, duration, description)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Walk In Appointment error");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        try {
            admin.walkInAppointment(patientId, doctorId, date, time + "00", duration, description);
            filteredList = new FilteredList<>(getAllWalkInAppointment(), predicate -> true);
            walkInAppointmentTable.setItems(filteredList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Walk In Appointment");
        alert.setContentText("Walk In Appointment successfully created");
        alert.showAndWait();
        addWalkInAppointmentToTable();
        resetFields();
    }
    @FXML
    private Button cancelButton;
    public void cancelWalkInAppointment() throws IOException {
        String id = appointmentIdField.getText();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm");
        confirmation.setContentText(String.format("Are you sure you want to cancel %s's appointment?", id));
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            admin.cancelWalkInAppointment(id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful cancellation");
            alert.setContentText("Appointment has been successfully cancelled!");
            alert.showAndWait();
            addWalkInAppointmentToTable();
            resetFields();
        }
    }
    @FXML
    private Button resetButton;
    public void resetFields() {
        walkInAppointmentTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        cancelButton.setDisable(true);
        resetAppointmentData();
        resetPatientData();
        resetDoctorData();
    }
}


