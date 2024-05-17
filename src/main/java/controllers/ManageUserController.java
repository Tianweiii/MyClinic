package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Auth.Verification;
import models.Datas.DataHistory;
import models.Filing.FileIO;
import models.Users.Admin;
import models.Users.Doctor;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {

    Admin admin = Cookie.identityAdmin;
    private FilteredList<Admin> filteredAdmin;
    private FilteredList<Doctor> filteredDoctor;
    private FilteredList<Patient> filteredPatient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        adminUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        adminPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        adminDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        adminGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        adminRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        adminSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        doctorId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        doctorUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        doctorPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        doctorDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        doctorGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        doctorRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        doctorSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        patientId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        patientUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        patientPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        patientDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        patientGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        patientRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        patientMedicalCase.setCellValueFactory(new PropertyValueFactory<>("medicalCase"));

        addAdminToTable();
        addDoctorToTable();
        addPatientToTable();
        setAdminIdField();
        setDoctorIdField();
        setPatientIdField();
        setAdminGenderComboBox();
        setDoctorGenderComboBox();
        setPatientGenderComboBox();

        try {
            filteredAdmin = new FilteredList<>(getAllAdmin(), predicate -> true);
            adminTable.setItems(filteredAdmin);

            filteredDoctor = new FilteredList<>(getAllDoctor(), predicate -> true);
            doctorTable.setItems(filteredDoctor);

            filteredPatient = new FilteredList<>(getAllPatient(), predicate -> true);
            patientTable.setItems(filteredPatient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userSearchBar.textProperty().addListener((observable, oldValue, newValue) -> searchUsers(newValue));
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
    private TextField userSearchBar;
    public void searchUsers(String input) {
        String keyword = input.toLowerCase();
        if (adminView.isVisible()) {
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

        } else if (doctorView.isVisible()) {
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

        } else if (patientView.isVisible()) {
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
            id = DataHistory.getNewId("admin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        adminIdField.setText("AD" + String.valueOf(id));
    }
    public void setAdminGenderComboBox() {
        adminGenderComboBox.setItems(FXCollections.observableArrayList("male", "female"));
    }

    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> doctorId;
    @FXML
    private TableColumn<Doctor, String> doctorDOB;
    @FXML
    private TableColumn<Doctor, String> doctorGender;
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
        try {
            doctorTable.setItems(getAllDoctor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Text doctorIdField;
    @FXML
    private TextField doctorUsernameTextField;
    @FXML
    private TextField doctorPasswordTextField;
    @FXML
    private DatePicker doctorDobField;
    @FXML
    private ComboBox<String> doctorGenderComboBox;
    @FXML
    private Text doctorRoleField;
    @FXML
    private TextField doctorSpecializationTextField;
    public void clickDoctorTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !doctorTable.getSelectionModel().isEmpty()) {
            Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
            String data = selected.toString();
            System.out.println(data);
            doctorIdField.setText(selected.getID());
            doctorUsernameTextField.setText(selected.getUsername());
            doctorPasswordTextField.setText(selected.getPassword());
            doctorDobField.setValue(selected.dobToLocaldate());
            doctorGenderComboBox.setValue(selected.getGender());
            doctorSpecializationTextField.setText(selected.getSpecialization());
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }
    public void setDoctorIdField() {
        String id;
        try {
            id = DataHistory.getNewId("doctor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        doctorIdField.setText("DT" + String.valueOf(id));
    }
    public void setDoctorGenderComboBox() {
        doctorGenderComboBox.setItems(FXCollections.observableArrayList("male", "female"));
    }

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> patientId;
    @FXML
    private TableColumn<Patient, String> patientDOB;
    @FXML
    private TableColumn<Patient, String> patientGender;
    @FXML
    private TableColumn<Patient, String> patientPassword;
    @FXML
    private TableColumn<Patient, String> patientRole;
    @FXML
    private TableColumn<Patient, String> patientMedicalCase;
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
        try {
            patientTable.setItems(getAllPatient());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Text patientIdField;
    @FXML
    private TextField patientUsernameTextField;
    @FXML
    private TextField patientPasswordTextField;
    @FXML
    private DatePicker patientDobField;
    @FXML
    private ComboBox<String> patientGenderComboBox;
    @FXML
    private Text patientRoleField;
    @FXML
    private TextField patientMedicalCaseTextField;
    public void clickPatientTableRow(MouseEvent e) {
        if (e.getClickCount() == 1 && !patientTable.getSelectionModel().isEmpty()) {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            String data = selected.toString();
            System.out.println(data);
            patientIdField.setText(selected.getID());
            patientUsernameTextField.setText(selected.getUsername());
            patientPasswordTextField.setText(selected.getPassword());
            patientDobField.setValue(selected.dobToLocaldate());
            patientGenderComboBox.setValue(selected.getGender());
            patientMedicalCaseTextField.setText(selected.getMedicalCase());
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }
    public void setPatientIdField() {
        String id;
        try {
            id = DataHistory.getNewId("patient");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        patientIdField.setText("PT" + String.valueOf(id));
    }
    public void setPatientGenderComboBox() {
        patientGenderComboBox.setItems(FXCollections.observableArrayList("male", "female"));
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
            DataHistory.updateUserHistoryId(role);
            addAdminToTable();
            resetFields();
        } else if (doctorView.isVisible()) {
            String dob = "";
            String id = doctorIdField.getText();
            String username = doctorUsernameTextField.getText();
            String password = doctorPasswordTextField.getText();
            if (doctorDobField.getValue() != null) {
                dob = admin.LocalDateToDob(doctorDobField.getValue());
            }
            String gender = doctorGenderComboBox.getValue();
            String role = doctorRoleField.getText().toLowerCase();
            String specialization = doctorSpecializationTextField.getText();

            if (!Verification.verifyEmptyFields(username, password, dob, gender, specialization)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register user error");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
                return;
            }

            if (!Verification.verifyUsername(username, "doctor")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register user error");
                alert.setContentText("Username already exists. Please use another username");
                alert.showAndWait();
                return;
            }

            admin.registerAccount(id, username, password, dob, gender, role, specialization);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful registration");
            alert.setContentText("User has been successfully registered!");
            alert.showAndWait();
            DataHistory.updateUserHistoryId(role);
            addDoctorToTable();
            resetFields();
        } else if (patientView.isVisible()) {
            String dob = "";
            String id = patientIdField.getText();
            String username = patientUsernameTextField.getText();
            String password = patientPasswordTextField.getText();
            if (patientDobField.getValue() != null) {
                dob = admin.LocalDateToDob(patientDobField.getValue());
            }
            String gender = patientGenderComboBox.getValue();
            String role = patientRoleField.getText().toLowerCase();
            String medicalCase = patientMedicalCaseTextField.getText();

            if (!Verification.verifyEmptyFields(username, password, dob, gender, medicalCase)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register user error");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
                return;
            }

            if (!Verification.verifyUsername(username, "patient")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register user error");
                alert.setContentText("Username already exists. Please use another username");
                alert.showAndWait();
                return;
            }

            admin.registerAccount(id, username, password, dob, gender, role, medicalCase);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful registration");
            alert.setContentText("User has been successfully registered!");
            alert.showAndWait();
            DataHistory.updateUserHistoryId(role);
            addPatientToTable();
            resetFields();
        }
    }
    public void updateUser() throws IOException {
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

            if (!Verification.verifyUsername(username, "admin")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update user error");
                alert.setContentText("Username already exists. Please use another username");
                alert.showAndWait();
                return;
            }
            admin.updateUser(id, username, password, dob, gender, role, salary);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful update");
            alert.setContentText("User data has been successfully updated!");
            alert.showAndWait();
            addAdminToTable();
            resetFields();
        } else if (doctorView.isVisible()) {
            String id = doctorIdField.getText();
            String username = doctorUsernameTextField.getText();
            String password = doctorPasswordTextField.getText();
            String dob = admin.LocalDateToDob(doctorDobField.getValue());
            String gender = doctorGenderComboBox.getValue();
            String role = doctorRoleField.getText().toLowerCase();
            String specialization = doctorSpecializationTextField.getText();
            admin.updateUser(id, username, password, dob, gender, role, specialization);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful update");
            alert.setContentText("User data has been successfully updated!");
            alert.showAndWait();
            addDoctorToTable();
            resetFields();
        } else if (patientView.isVisible()) {
            String id = patientIdField.getText();
            String username = patientUsernameTextField.getText();
            String password = patientPasswordTextField.getText();
            String dob = admin.LocalDateToDob(patientDobField.getValue());
            String gender = patientGenderComboBox.getValue();
            String role = patientRoleField.getText().toLowerCase();
            String medicalCase = patientMedicalCaseTextField.getText();
            admin.updateUser(id, username, password, dob, gender, role, medicalCase);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful update");
            alert.setContentText("User data has been successfully updated!");
            alert.showAndWait();
            addPatientToTable();
            resetFields();
        }
    }
    public void deleteUser() throws IOException {
        if (adminView.isVisible()) {
            String id = adminIdField.getText();
            String role = adminRoleField.getText().toLowerCase();

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm");
            confirmation.setContentText(String.format("Are you sure you want to delete %s's data?", id));
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                admin.deleteUser(id, role);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful deletion");
                alert.setContentText("User data has been successfully deleted!");
                alert.showAndWait();
                addAdminToTable();
                resetFields();
            }
        } else if (doctorView.isVisible()) {
            String id = doctorIdField.getText();
            String role = doctorRoleField.getText().toLowerCase();

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm");
            confirmation.setContentText(String.format("Are you sure you want to delete %s's data?", id));
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                admin.deleteUser(id, role);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful deletion");
                alert.setContentText("User data has been successfully deleted!");
                alert.showAndWait();
                addDoctorToTable();
                resetFields();
            }
        } else if (patientView.isVisible()) {
            String id = patientIdField.getText();
            String role = patientRoleField.getText().toLowerCase();

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm");
            confirmation.setContentText(String.format("Are you sure you want to delete %s's data?", id));
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                admin.deleteUser(id, role);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful deletion");
                alert.setContentText("User data has been successfully deleted!");
                alert.showAndWait();
                addPatientToTable();
                resetFields();
            }
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
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        } else if (doctorView.isVisible()) {
            doctorTable.getSelectionModel().clearSelection();
            setDoctorIdField();
            doctorUsernameTextField.setText("");
            doctorPasswordTextField.setText("");
            doctorDobField.setValue(null);
            doctorGenderComboBox.setValue(null);
            doctorSpecializationTextField.setText("");
            addButton.setDisable(false);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        } else if (patientView.isVisible()) {
            patientTable.getSelectionModel().clearSelection();
            setPatientIdField();
            patientUsernameTextField.setText("");
            patientPasswordTextField.setText("");
            patientDobField.setValue(null);
            patientGenderComboBox.setValue(null);
            patientMedicalCaseTextField.setText("");
            addButton.setDisable(false);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }
}

