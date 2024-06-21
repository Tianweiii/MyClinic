package patientController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import models.Users.Patient;
import models.Users.User;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientAppointmentController implements Initializable {

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

        addAppointmentToTable();
        setAppointmentIdTextField();
        patientIdTextField.setText(patientId);
        try {
            setDoctorIdComboBox();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doctorIdComboBox.setOnAction(e -> {
            if (doctorIdComboBox.getValue() != null) {
                try {
                    setDoctorData(doctorIdComboBox.getValue().split(" ")[0]);
                    setDateComboBox();
                } catch (IOException err) {
                    throw new RuntimeException(err);
                }
            }
        });

        dateComboBox.setOnAction(e -> {
            if (dateComboBox.getValue() != null) {
                try {
                    setTimeComboBox();
                } catch (IOException err) {
                    throw new RuntimeException(err);
                }
            }
        });

        timeComboBox.setOnAction(e -> {
            if (timeComboBox.getValue() != null) {
                try {
                    setDurationComboBox();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> patientIdColumn;
    @FXML
    private TableColumn<Appointment, String> doctorIdColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> durationColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    public void setAppointmentIdTextField() {
        String id;
        try {
            id = DataHistory.getNewId("appointment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        appointmentIdTextField.setText("APT" + String.valueOf(id));
    }
    ObservableList<Appointment> getPatientAppointment() throws IOException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] data = FileIO.splitString(row);
            if (data[1].equals(patientId)) {
                Appointment apt = new Appointment(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
                appointments.add(apt);
            }
        }
        return appointments;
    }
    public void addAppointmentToTable() {
        try {
            appointmentTable.setItems(getPatientAppointment());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) {
        addButton.setDisable(true);

        if (e.getClickCount() == 1 && !appointmentTable.getSelectionModel().isEmpty()) {
            Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
            try {
                setDoctorData(selected.getDoctorID());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            doctorIdComboBox.setValue(selected.getDoctorID());
            doctorIdComboBox.setDisable(true);
            appointmentIdTextField.setText(selected.getAppointmentID());
            descriptionTextInput.setText(selected.getDescription());
            descriptionTextInput.setDisable(true);

            dateComboBox.setValue(selected.getDate());
            dateComboBox.setDisable(true);
            timeComboBox.setValue(selected.getTime());
            timeComboBox.setDisable(true);
            durationComboBox.setValue(selected.getDuration());
            durationComboBox.setDisable(true);

            // Parse the date and compare it with today's date
            LocalDate selectedDate = LocalDate.parse(selected.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate today = LocalDate.now();

            // Enable or disable the cancel button based on the date comparison
            if (selectedDate.isBefore(today)) {
                cancelButton.setDisable(true);
            } else {
                cancelButton.setDisable(false);
            }
            appointmentTable.setDisable(true);
        }
    }

    public void resetAppointmentData() {
        durationComboBox.getSelectionModel().clearSelection();
        durationComboBox.setValue(null);
        timeComboBox.getSelectionModel().clearSelection();
        timeComboBox.setValue(null);
        dateComboBox.getSelectionModel().clearSelection();
        dateComboBox.setValue(null);
        doctorIdComboBox.getSelectionModel().clearSelection();
        doctorIdComboBox.setValue(null);
        durationComboBox.setDisable(true);
        timeComboBox.setDisable(true);
        dateComboBox.setDisable(true);
        doctorIdComboBox.setDisable(false);
        descriptionTextInput.clear();
        descriptionTextInput.setDisable(false);
        setAppointmentIdTextField();
    }


    @FXML
    private Text appointmentIdTextField;
    @FXML
    private Text patientIdTextField;
    @FXML
    private ComboBox<String> doctorIdComboBox;
    @FXML
    private TextField descriptionTextInput;
    public void setDoctorIdComboBox() throws IOException {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (String doctor : User.getAccounts("doctor")) {
            String[] row = FileIO.splitString(doctor);
            list.add(MessageFormat.format("{0} {1}", row[0], row[1]));
        }
        doctorIdComboBox.setItems(list);
    }

    @FXML
    private ComboBox<String> timeComboBox;
    @FXML
    private ComboBox<String> dateComboBox;
    @FXML
    private ComboBox<String> durationComboBox;
    public void setDateComboBox() throws IOException {
        timeComboBox.getSelectionModel().clearSelection();
        durationComboBox.getSelectionModel().clearSelection();
        timeComboBox.setDisable(true);
        durationComboBox.setDisable(true);
        dateComboBox.setDisable(false);
        ObservableList<String> list = FXCollections.observableArrayList();
        String doctorId = doctorIdComboBox.getValue().split(" ")[0];
        FileIO reader = new FileIO("r", "schedule");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(doctorId)) {
                LocalDate date = LocalDate.parse(arr[2], formatter);
                if (!date.isBefore(today)) { // Include today and future dates
                    list.add(arr[2]);
                }
            }
        }

        dateComboBox.setItems(list);
    }
    public void setTimeComboBox() throws IOException {
        timeComboBox.getSelectionModel().clearSelection();
        durationComboBox.getSelectionModel().clearSelection();
        durationComboBox.setDisable(true);
        timeComboBox.setDisable(false);
        ObservableList<String> list = FXCollections.observableArrayList();
        String doctorId = doctorIdComboBox.getValue().split(" ")[0];
        String date = dateComboBox.getValue();

        list.addAll(Schedule.getScheduleTimeslot(doctorId, date));
        timeComboBox.setItems(list);
    }
    public void setDurationComboBox() throws IOException {
        durationComboBox.getSelectionModel().clearSelection();
        durationComboBox.setDisable(false);
        String duration;
        String doctorId = doctorIdComboBox.getValue().split(" ")[0];
        String date = dateComboBox.getValue();
        String time = timeComboBox.getValue();

        try {
            duration = Schedule.getAvailableDuration(doctorId, date, time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        switch (duration) {
            case "2" -> durationComboBox.setItems(FXCollections.observableArrayList("1h", "2h"));
            case "3" -> durationComboBox.setItems(FXCollections.observableArrayList("1h", "2h", "3h"));
            default -> durationComboBox.setItems(FXCollections.observableArrayList("1h"));
        }
    }

    @FXML
    private Text doctorIdTextField;
    @FXML
    private Text doctorNameTextField;
    @FXML
    private Text doctorSpecializationTextField;
    public void setDoctorData(String docId) throws IOException {
        doctorIdTextField.setText(docId);
        FileIO reader = new FileIO("r", "doctor");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(docId)) {
                doctorNameTextField.setText(arr[1]);
                doctorSpecializationTextField.setText(arr[6]);
                return;
            }
        }
    }
    public void resetDoctorData() {
        doctorIdTextField.setText("Doctor ID");
        doctorNameTextField.setText("Doctor Name");
        doctorSpecializationTextField.setText("Specialization");
    }

    @FXML
    private Button addButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button cancelButton;
    public void addAppointment() {
        String doctorId = "";
        String date = "";
        String time = "";
        String duration = "";
        if (doctorIdComboBox.getValue() != null) {
            doctorId = doctorIdComboBox.getValue().split(" ")[0];
        }
        if (dateComboBox.getValue() != null) {
            date = dateComboBox.getValue();
        }
        if (timeComboBox.getValue() != null) {
            time = timeComboBox.getValue();
        }
        if (durationComboBox.getValue() != null) {
            duration = durationComboBox.getValue();
        }
        String description = descriptionTextInput.getText();
        String status = "pending";

        if (!Verification.verifyEmptyFields(doctorId, date, time, duration, description)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Appointment error");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }

        try {
            patient.makeAppointment(patientId, doctorId, date, time, duration, status, description);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Appointment");
        alert.setContentText("Appointment successfully created");
        alert.showAndWait();
        addAppointmentToTable();
        resetFields();
    }
    public void cancelAppointment() throws IOException {
        String id = appointmentIdTextField.getText();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm");
        confirmation.setContentText(String.format("Are you sure you want to cancel %s's appointment?", id));
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            patient.cancelAppointment(id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful cancellation");
            alert.setContentText("Appointment has been successfully cancelled!");
            alert.showAndWait();
            addAppointmentToTable();
            resetFields();
        }
    }
    public void resetFields() {
        appointmentTable.getSelectionModel().clearSelection();
        appointmentTable.setDisable(false);
        addButton.setDisable(false);
        cancelButton.setDisable(true);
        resetAppointmentData();
        resetDoctorData();
    }
}
