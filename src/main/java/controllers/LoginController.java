package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import models.Users.Admin;
import models.Users.Doctor;
import models.Users.Patient;
import models.Users.User;
import models.Auth.Cookie;

public class LoginController implements Initializable {
    Admin admin = null;
    Doctor doctor = null;
    Patient patient = null;

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button logInButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.setItems(FXCollections.observableArrayList("Patient", "Admin", "Doctor"));
        comboBox.setValue("Patient");
    }

    public void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = comboBox.getValue().toLowerCase();
        User user = User.loginAccount(username, password, role);

        if (user != null) {
            if (user instanceof Admin a) {
                Cookie.setCookie(a);
                admin = Cookie.identityAdmin;
                System.out.println(Cookie.identityAdmin);
            } else if (user instanceof Doctor d) {
                Cookie.setCookie(d);
                doctor = Cookie.identityDoctor;
                System.out.println(Cookie.identityDoctor);
            } else if (user instanceof Patient p) {
                Cookie.setCookie(p);
                patient = Cookie.identityPatient;
                System.out.println(Cookie.identityPatient);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful login");
            alert.setContentText("You are now logged in!");
            alert.showAndWait();

            String path = switch (role) {
                case "admin" -> "/org/example/cmsclinic/adminMain.fxml";
                case "doctor" -> "/doctorFXML/drMenu.fxml";
                case "patient" -> "/patientFXML/patientHome.fxml";
                default -> null;
            };

            Parent parent = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Error");
            alert.setContentText("Incorrect username or password");
            alert.showAndWait();
        }
    }
}