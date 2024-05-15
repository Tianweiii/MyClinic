package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Filing.FileIO;
import models.Users.Admin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAdminToTable();
        setAdminIdField();
        setAdminGenderComboBox();
    }

    @FXML
    private AnchorPane adminView;
    @FXML
    private AnchorPane doctorView;
    @FXML
    private AnchorPane patientView;
    public void showAdminView() {
        adminView.setVisible(true);
        doctorView.setVisible(false);
        patientView.setVisible(false);
    }
    public void showDoctorView() {
        adminView.setVisible(false);
        doctorView.setVisible(true);
        patientView.setVisible(false);
    }
    public void showPatientView() {
        adminView.setVisible(false);
        doctorView.setVisible(false);
        patientView.setVisible(true);
    }

    @FXML
    private TextField roleSearchBar;

    @FXML
    private Button showAdminButton;
    @FXML
    private Button showDoctorButton;
    @FXML
    private Button showPatientButton;


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
    private Text adminIdField;
    @FXML
    private TextField adminUsernameTextField;
    @FXML
    private TextField adminPasswordTextField;
    @FXML
    private DatePicker adminDobField;
    @FXML
    private ComboBox<String> adminGenderComboBox;
    @FXML
    private TextField adminSalaryTextField;
    public void clickAdminTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !adminTable.getSelectionModel().isEmpty()) {
            Admin selected = adminTable.getSelectionModel().getSelectedItem();
            String data = selected.toString();
            System.out.println(data);
            adminIdField.setText(selected.getID());
            adminUsernameTextField.setText(selected.getUsername());
            adminPasswordTextField.setText(selected.getPassword());
            adminDobField.setValue(selected.dobToLocaldate());
            adminGenderComboBox.setValue(selected.getGender());
            adminSalaryTextField.setText(selected.getSalary());
        }
    }
    public void setAdminIdField() {
        int id;
        FileIO reader = new FileIO("r", "admin");
        try {
            id = (reader.countRowNum() + 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adminIdField.setText("AD" + String.valueOf(id));
    }
    public void setAdminGenderComboBox() {
        adminGenderComboBox.setItems(FXCollections.observableArrayList("male", "female"));
    }

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    public void addUser() {

    }
}

