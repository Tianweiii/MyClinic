package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Datas.Payment;
import models.Filing.FileIO;
import models.Users.Admin;
import models.Users.Doctor;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    Admin admin = Cookie.identityAdmin;
    private FilteredList<Admin> filteredAdmin;
    private FilteredList<Doctor> filteredDoctor;
    private FilteredList<Patient> filteredPatient;

    @FXML
    private AnchorPane homeMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAdminToTable();
        addDoctorToTable();
        addPatientToTable();

        addUserChartData();
        try {
            getCurrentMonthRevenue();
            addMonthlyRevenueChart();
            getDailyAppointmentCount();
            filteredAdmin = new FilteredList<>(getAllAdmin(), predicate -> true);
            adminTable.setItems(filteredAdmin);

            filteredDoctor = new FilteredList<>(getAllDoctor(), predicate -> true);
            doctorTable.setItems(filteredDoctor);

            filteredPatient = new FilteredList<>(getAllPatient(), predicate -> true);
            patientTable.setItems(filteredPatient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        roleSearchBar.textProperty().addListener((observable, oldValue, newValue) -> searchRoles(newValue));
    }

    @FXML
    private Button showAdminButton;
    @FXML
    private Button showDoctorButton;
    @FXML
    private Button showPatientButton;
    public void showAdminTable() {
        adminTable.setVisible(true);
        doctorTable.setVisible(false);
        patientTable.setVisible(false);
    }

    public void showDoctorTable() {
        adminTable.setVisible(false);
        doctorTable.setVisible(true);
        patientTable.setVisible(false);
    }

    public void showPatientTable() {
        adminTable.setVisible(false);
        doctorTable.setVisible(false);
        patientTable.setVisible(true);
    }

    @FXML
    private TextField roleSearchBar;
    public void searchRoles(String input) {
        String keyword = input.toLowerCase();
        if (adminTable.isVisible()) {
            filteredAdmin.setPredicate(data -> {
                if (input.isEmpty() || input.isBlank() || input == null) {
                    return true;
                }

                if (data.getID().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getUsername().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getPassword().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getDateOfBirth().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getGender().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getSalary().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<Admin> sortedData = new SortedList<>(filteredAdmin);
            sortedData.comparatorProperty().bind(adminTable.comparatorProperty());
            adminTable.setItems(sortedData);

        } else if (doctorTable.isVisible()) {
            filteredDoctor.setPredicate(data -> {
                if (input.isEmpty() || input.isBlank() || input == null) {
                    return true;
                }

                if (data.getID().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getUsername().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getPassword().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getDateOfBirth().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getGender().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getSpecialization().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<Doctor> sortedData = new SortedList<>(filteredDoctor);
            sortedData.comparatorProperty().bind(doctorTable.comparatorProperty());
            doctorTable.setItems(sortedData);

        } else if (patientTable.isVisible()) {
            filteredPatient.setPredicate(data -> {
                if (input.isEmpty() || input.isBlank() || input == null) {
                    return true;
                }

                if (data.getID().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getUsername().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getPassword().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getDateOfBirth().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getGender().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (data.getMedicalCase().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<Patient> sortedData = new SortedList<>(filteredPatient);
            sortedData.comparatorProperty().bind(patientTable.comparatorProperty());
            patientTable.setItems(sortedData);
        }
    }

    @FXML
    private TableView<Admin> adminTable;
    @FXML
    private TableColumn<Admin, String> adminId;
    @FXML
    private TableColumn<Admin, String> adminDOB;
    @FXML
    private TableColumn<Admin, String> adminGender;
    @FXML
    private TableColumn<Admin, String> adminPassword;
    @FXML
    private TableColumn<Admin, String> adminRole;
    @FXML
    private TableColumn<Admin, String> adminSalary;
    @FXML
    private TableColumn<Admin, String> adminUsername;
    ObservableList<Admin> getAllAdmin() throws IOException {
        ObservableList<Admin> admins = FXCollections.observableArrayList();
        FileIO reader = new FileIO("r", "admin");
        for (String row : reader.readFile()) {
            String[] data = row.split(",\\s");
            Admin ad = new Admin(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
            admins.add(ad);
        }
        return admins;
    }

    public void addAdminToTable() {
        adminId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        adminUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        adminPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        adminDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        adminGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        adminRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        adminSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        try {
            adminTable.setItems(getAllAdmin());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> doctorDOB;
    @FXML
    private TableColumn<Doctor, String> doctorGender;
    @FXML
    private TableColumn<Doctor, String> doctorId;
    @FXML
    private TableColumn<Doctor, String> doctorPassword;
    @FXML
    private TableColumn<Doctor, String> doctorRole;
    @FXML
    private TableColumn<Doctor, String> doctorSpecialization;
    @FXML
    private TableColumn<Doctor, String> doctorUsername;
    ObservableList<Doctor> getAllDoctor() throws IOException {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        FileIO reader = new FileIO("r", "doctor");
        for (String row : reader.readFile()) {
            String[] data = row.split(",\\s");
            Doctor dt = new Doctor(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
            doctors.add(dt);
        }
        return doctors;
    }

    public void addDoctorToTable() {
        doctorId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        doctorUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        doctorPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        doctorDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        doctorGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        doctorRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        doctorSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        try {
            doctorTable.setItems(getAllDoctor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> patientDOB;
    @FXML
    private TableColumn<Patient, String> patientGender;
    @FXML
    private TableColumn<Patient, String> patientId;
    @FXML
    private TableColumn<Patient, String> patientMedicalCase;
    @FXML
    private TableColumn<Patient, String> patientPassword;
    @FXML
    private TableColumn<Patient, String> patientRole;
    @FXML
    private TableColumn<Patient, String> patientUsername;
    ObservableList<Patient> getAllPatient() throws IOException {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        FileIO reader = new FileIO("r", "patient");
        for (String row : reader.readFile()) {
            String[] data = row.split(",\\s");
            Patient pt = new Patient(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
            patients.add(pt);
        }
        return patients;
    }

    public void addPatientToTable() {
        patientId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        patientUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        patientPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        patientDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        patientGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        patientRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        patientMedicalCase.setCellValueFactory(new PropertyValueFactory<>("medicalCase"));
        try {
            patientTable.setItems(getAllPatient());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clickAdminTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !adminTable.getSelectionModel().isEmpty()) {
            Admin selectedItem = adminTable.getSelectionModel().getSelectedItem();
            String data = selectedItem.toString();
            System.out.println(data);
        }
    }
    public void clickDoctorTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !doctorTable.getSelectionModel().isEmpty()) {
            Doctor selectedItem = doctorTable.getSelectionModel().getSelectedItem();
            String data = selectedItem.toString();
            System.out.println(data);
        }
    }
    public void clickPatientTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !patientTable.getSelectionModel().isEmpty()) {
            Patient selectedItem = patientTable.getSelectionModel().getSelectedItem();
            String data = selectedItem.toString();
            System.out.println(data);
        }
    }

    @FXML
    private BarChart<String, Number> totalUserChart;
    public void addUserChartData() {
        Number adminCount;
        Number doctorCount;
        Number patientCount;
        FileIO adminReader = new FileIO("r", "admin");
        FileIO doctorReader = new FileIO("r", "doctor");
        FileIO patientReader = new FileIO("r", "patient");
        try {
            adminCount = adminReader.countRowNum();
            doctorCount = doctorReader.countRowNum();
            patientCount = patientReader.countRowNum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CategoryAxis userCountXAxis = new CategoryAxis();
        NumberAxis userCountYAxis = new NumberAxis();
        userCountXAxis.setLabel("User");
        userCountYAxis.setLabel("Count");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Users");
        series.getData().add(new XYChart.Data<>("Admin", adminCount));
        series.getData().add(new XYChart.Data<>("Doctor", doctorCount));
        series.getData().add(new XYChart.Data<>("Patient", patientCount));

        totalUserChart.getData().add(series);
    }

    @FXML
    private LineChart<String, Number> monthlyRevenueChart;
    public void addMonthlyRevenueChart() throws IOException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        Map<String, Number> data = Payment.getMonthlyRevenue();
        series.getData().add(new XYChart.Data<>("Jan", data.get("Jan")));
        series.getData().add(new XYChart.Data<>("Feb", data.get("Feb")));
        series.getData().add(new XYChart.Data<>("Mar", data.get("Mar")));
        series.getData().add(new XYChart.Data<>("Apr", data.get("Apr")));
        series.getData().add(new XYChart.Data<>("May", data.get("May")));

        monthlyRevenueChart.getData().add(series);
    }

    @FXML
    private Text dailyAppointments;
    public void getDailyAppointmentCount() throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);

        int number = Appointment.findAppointment(currentDate).toArray().length;

        dailyAppointments.setText(String.valueOf(number) + " appointments");
    }

    @FXML
    private Text monthlyRevenueTotal;
    public void getCurrentMonthRevenue() throws IOException {
        int total = 0;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String currentDate = formatter.format(date);

        FileIO reader = new FileIO("r", "payment");
        for (String row : reader.readFile()) {
            String[] data = row.split(",\\s");
            if (data[5].split("/")[1].equals(currentDate) && data[4].equals("paid")) {
                total += Integer.parseInt(data[3]);
            }
        }
        monthlyRevenueTotal.setText("RM" + String.valueOf(total));
    }
}
