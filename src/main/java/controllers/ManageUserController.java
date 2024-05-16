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
import models.Auth.Cookie;
import models.Auth.Verification;
import models.Datas.UserHistory;
import models.Filing.FileIO;
import models.Users.Admin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {

    Admin admin = Cookie.identityAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        adminUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        adminPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        adminDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        adminGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        adminRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        adminSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

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
    private Text adminRoleField;
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
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }
    public void setAdminIdField() {
        String id;
        try {
            id = UserHistory.getNewId("admin");
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
    @FXML
    private Button resetButton;
    public void addUser() throws IOException {
        if (adminView.isVisible()) {
            String dob = "";
            String id = adminIdField.getText();
            String username = adminUsernameTextField.getText();
            String password = adminPasswordTextField.getText();
            if (adminDobField.getValue() != null) {
                dob = admin.LocalDateToDob(adminDobField.getValue());
            }
            String gender = adminGenderComboBox.getValue();
            String role = adminRoleField.getText().toLowerCase();
            String salary = adminSalaryTextField.getText();

            if (!Verification.verifyEmptyFields(username, password, dob, gender, salary)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register user error");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
                return;
            }

            if (!Verification.verifyUsername(username, "admin")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register user error");
                alert.setContentText("Username already exists. Please use another username");
                alert.showAndWait();
                return;
            }
            admin.registerAccount(id, username, password, dob, gender, role, salary);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful registration");
            alert.setContentText("User has been successfully registered!");
            alert.showAndWait();
            UserHistory.updateUserHistoryId(role);
            addAdminToTable();
            resetFields();
        } else if (doctorView.isVisible()) {

        } else if (patientView.isVisible()) {

        }
    }
    public void updateUser() throws IOException {
        if (adminView.isVisible()) {
            String id = adminIdField.getText();
            String username = adminUsernameTextField.getText();
            String password = adminPasswordTextField.getText();
            String dob = admin.LocalDateToDob(adminDobField.getValue());
            String gender = adminGenderComboBox.getValue();
            String role = adminRoleField.getText().toLowerCase();
            String salary = adminSalaryTextField.getText();
            admin.updateUser(id, username, password, dob, gender, role, salary);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful update");
            alert.setContentText("User data has been successfully updated!");
            alert.showAndWait();
            addAdminToTable();
            resetFields();
        } else if (doctorView.isVisible()) {

        } else if (patientView.isVisible()) {

        }
    }
    public void deleteUser() throws IOException {
        if (adminView.isVisible()) {
            String id = adminIdField.getText();
            String role = adminRoleField.getText().toLowerCase();
            admin.deleteUser(id, role);

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm");
            confirmation.setContentText(String.format("Are you sure you want to delete %s's data?", id));
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful deletion");
                alert.setContentText("User data has been successfully deleted!");
                alert.showAndWait();
                addAdminToTable();
                resetFields();
            }

        } else if (doctorView.isVisible()) {

        } else if (patientView.isVisible()) {

        }
    }
    public void resetFields() {
        if (adminView.isVisible()) {
            adminTable.getSelectionModel().clearSelection();
            setAdminIdField();
            adminUsernameTextField.setText("");
            adminPasswordTextField.setText("");
            adminDobField.setValue(null);
            adminGenderComboBox.setValue(null);
            adminSalaryTextField.setText("");
            addButton.setDisable(false);
        } else if (doctorView.isVisible()) {

        } else if (patientView.isVisible()) {

        }
    }
}

