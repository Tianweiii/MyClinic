package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private VBox navbar;

    @FXML
    private Button homeButton;
    @FXML
    private Button paymentButton;
    @FXML
    private Button trackAppointmentButton;
    @FXML
    private Button trackMedicalReportButton;
    @FXML
    private Button walkInButton;
    @FXML
    private Button logOutButton;

    @FXML
    private AnchorPane homePage;
    @FXML
    private AnchorPane manageUserPage;
    @FXML
    private AnchorPane walkInPage;
    @FXML
    private AnchorPane trackAppointmentPage;
    @FXML
    private AnchorPane trackMedReportPage;
    @FXML
    private AnchorPane paymentPage;

    public void pressHomeButton() {
        homePage.setVisible(true);
        manageUserPage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressManageUserButton() {
        homePage.setVisible(false);
        manageUserPage.setVisible(true);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressWalkInButton() {
        homePage.setVisible(false);
        manageUserPage.setVisible(false);
        walkInPage.setVisible(true);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressTrackAptButton() {
        homePage.setVisible(false);
        manageUserPage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(true);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressTrackMedRepButton() {
        homePage.setVisible(false);
        manageUserPage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(true);
        paymentPage.setVisible(false);
    }

    public void pressPaymentButton() {
        homePage.setVisible(false);
        manageUserPage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(true);
    }

    public void pressLogOutButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setContentText("Are you sure you want to log out?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Parent parent = null;
                try {
                    parent = FXMLLoader.load(getClass().getResource("/org/example/cmsclinic/login.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = new Scene(parent);
                Stage stage = (Stage) navbar.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        });
    }
}