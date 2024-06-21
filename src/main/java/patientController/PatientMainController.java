package patientController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Filing.FileIO;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientMainController implements Initializable {

    Patient patient = Cookie.identityPatient;
    String patientId = patient.getID();
    String username = patient.getUsername();
    String password = patient.getPassword();
    String dob = patient.getDateOfBirth();
    String gender = patient.getGender();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
    }

    @FXML
    private Text usernameField;
    public void setData() {
        usernameField.setText(username);
        setPatientData();
        try {
            appointmentCountField.setText("You have " + getAppointmentCount() + " pending appointments.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Text appointmentCountField;
    public int getAppointmentCount() throws IOException {
        int count = 0;
        FileIO reader = new FileIO("r", "appointment");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[1].equals(patientId) && arr[6].equals("pending")) {
                count++;
            }
        }
        return count;
    }

    @FXML
    private Text patientDOBField;
    @FXML
    private Text patientGenderField;
    @FXML
    private Text patientIdField;
    @FXML
    private Text patientNameField;
    @FXML
    private Text patientPasswordField;
    public void setPatientData() {
        patientIdField.setText(patientId);
        patientNameField.setText(username);
        patientPasswordField.setText(password);
        patientDOBField.setText(dob);
        patientGenderField.setText(gender);
    }

    @FXML
    private DatePicker dobInput;
    @FXML
    private TextField genderInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField passwordInput;
    public void clearInput() {
        nameInput.clear();
        passwordInput.clear();
        genderInput.clear();
        dobInput.setValue(null);
    }
    public void submitEditForm() {
        String editName = nameInput.getText();
        String editPassword = passwordInput.getText();
        String editGender = genderInput.getText();
        String editDob = "";
        if (dobInput.getValue() != null) {
            editDob = patient.LocalDateToDob(dobInput.getValue());
        }

        if (editName.isBlank() && editPassword.isBlank() && editGender.isBlank() && editDob.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update profile error");
            alert.setContentText("At least 1 field must be filled.");
            alert.showAndWait();
            return;
        }

        if (editName.equals(username) || editPassword.equals(password) || editGender.equals(gender) || editDob.equals(dob)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update profile error");
            alert.setContentText("New and old data cannot be the same.");
            alert.showAndWait();
            return;
        }

        // edit text file and update cookie
        FileIO reader = new FileIO("r", "patient");
        ArrayList<String> data = new ArrayList<>();
        try {
            for (String row : reader.readFile()) {
                String[] arr = FileIO.splitString(row);
                if (arr[0].equals(patientId)) {
                    if (!editName.isBlank()) {
                        arr[1] = editName;
                    }
                    if (!editPassword.isBlank()) {
                        arr[2] = editPassword;
                    }
                    if (!editDob.isBlank()) {
                        arr[3] = editDob;
                    }
                    if (!editGender.isBlank()) {
                        arr[4] = editGender;
                    }
                    String updated = MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}, {6}", arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
                    data.add(updated);
                    Patient temp = new Patient(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
                    Cookie.clearCookie();
                    Cookie.setCookie(temp);
                    patient = Cookie.identityPatient;
                    patientId = patient.getID();
                    username = patient.getUsername();
                    password = patient.getPassword();
                    dob = patient.getDateOfBirth();
                    gender = patient.getGender();

                } else {
                    data.add(row);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileIO writer = new FileIO("w", "patient");
        writer.writeFile(data);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful update");
        alert.setContentText("You have successfully updated your profile!");
        alert.showAndWait();
        setData();
        clearInput();
    }
}
