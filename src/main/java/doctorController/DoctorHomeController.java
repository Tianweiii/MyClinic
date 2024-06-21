package doctorController;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Datas.Schedule;
import models.Filing.FileIO;
import models.Users.Doctor;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static doctorController.DoctorAppointmentController.loadAppointment;

public class DoctorHomeController implements Initializable {
    Doctor doctor = Cookie.identityDoctor;
    String docId = doctor.getID();
    DoctorScheduleController scheduleController = new DoctorScheduleController(); // Instantiate DoctorScheduleController
    @FXML
    private TableColumn<Appointment, String> AppID;

    @FXML
    private TableColumn<Appointment, String> DID;

    @FXML
    private TableColumn<Appointment, String> PID;

    @FXML
    private TableColumn<Appointment, String> SID;

    @FXML
    private TextField SearchAppointment;

    @FXML
    private TableView<Appointment> appList;

    @FXML
    private PieChart appStatus;

    @FXML
    private Label appToday;

    @FXML
    private TableColumn<Appointment, String> dateColumn;

    @FXML
    private TableColumn<Appointment, String> desccolumn;

    @FXML
    private TableColumn<Appointment, String> durationcolumn;

    @FXML
    private TableColumn<Appointment, String> statuscolumn;

    @FXML
    private TableColumn<Appointment, String> timecolumn;

    @FXML
    private Label walkinApp;

    @FXML
    private Label workHr;

    @FXML
    private LineChart<Schedule, String> workinghr;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        AppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        PID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        DID.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timecolumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        durationcolumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statuscolumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        desccolumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        SID.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));

        showApp();
        calAppToday();
        calwalkinToday();
        updateWorkHours();
        piechart();

    }

    public void showApp() {
        ObservableList<Appointment> appointments = loadAppointment();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments, b -> true);

        SearchAppointment.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                // If filter text is empty, display all appointments
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
        appList.setItems(filteredData);
    }

    public void calAppToday() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int count = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[3].equals(currentDate) && arr[2].equals(docId)) {
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        appToday.setText(String.valueOf(count));
    }

    public void calwalkinToday() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int count = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[3].equals(currentDate) && arr[6].equals("walk-in") && arr[2].equals(docId)) {
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        walkinApp.setText(String.valueOf(count));
    }

    public void updateWorkHours() {
        int totalHoursWorked = scheduleController.loadCurrentDaySchedule();
        workHr.setText(String.valueOf(totalHoursWorked));
    }
    public void piechart(){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Complete",countCompleteStatus()),
                        new PieChart.Data("Missed",countMissedStatus()),
                        new PieChart.Data("Pending",countPendingStatus()),
                        new PieChart.Data("Cancelled",countCancelledStatus()),
                        new PieChart.Data("Walk-in",countWalkinStatus()));

        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), "", data.pieValueProperty()
                        )
                ));
        appStatus.setData(pieChartData);

    }
    public int countCompleteStatus() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int completeStatus = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[6].equals("complete") && arr[2].equals(docId)) {
                    completeStatus++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return completeStatus;
    }

    public int countMissedStatus() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int missedStatus = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[6].equals("missed") && arr[2].equals(docId)) {
                    missedStatus++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return missedStatus;
    }
    public int countPendingStatus() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int pendingStatus = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[6].equals("pending") && arr[2].equals(docId)) {
                    pendingStatus++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pendingStatus;
    }
    public int countCancelledStatus() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int cancelledStatus = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[6].equals("cancelled") && arr[2].equals(docId)) {
                    cancelledStatus++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cancelledStatus;
    }
    public int countWalkinStatus() {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        int walkinStatus = 0;
        try {
            FileIO reader = new FileIO("r", "appointment");
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[6].equals("walk-in") && arr[2].equals(docId)) {
                    walkinStatus++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return walkinStatus;
    }
}