package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Datas.Appointment;
import models.Filing.FileIO;
import models.Users.Admin;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    Admin admin = Cookie.identityAdmin;

    @FXML
    private AnchorPane homeMenu;
    @FXML
    private Text monthlyRevenueTotal;
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
    @FXML
    private BarChart<String, Number> totalUserChart;
    @FXML
    private Text dailyAppointments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        addUserChartData();
        try {
            getCurrentMonthRevenue();
            getDailyAppointmentCount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void clickTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !adminTable.getSelectionModel().isEmpty()) {
            Admin selectedItem = adminTable.getSelectionModel().getSelectedItem();

            String data = selectedItem.toString();

            System.out.println(data);
        }
    }

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

    public void addMonthlyRevenueChart() {

    }

    public void setMonthlyRevenue() {

    }

    public void clickAdminButton() {

    }

    public void getDailyAppointmentCount() throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String currentDate = formatter.format(date);

        int number = Appointment.findAppointment(currentDate).toArray().length;

        dailyAppointments.setText(String.valueOf(number) + " appointments");
    }

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
