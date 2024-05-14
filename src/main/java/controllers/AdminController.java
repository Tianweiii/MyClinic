package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    private AnchorPane homePage;
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
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressWalkInButton() {
        homePage.setVisible(false);
        walkInPage.setVisible(true);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressTrackAptButton() {
        homePage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(true);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(false);
    }

    public void pressTrackMedRepButton() {
        homePage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(true);
        paymentPage.setVisible(false);
    }

    public void pressPaymentButton() {
        homePage.setVisible(false);
        walkInPage.setVisible(false);
        trackAppointmentPage.setVisible(false);
        trackMedReportPage.setVisible(false);
        paymentPage.setVisible(true);
    }
}