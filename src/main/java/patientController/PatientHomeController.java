package patientController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Auth.Cookie;
import models.Users.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientHomeController implements Initializable {

    Patient patient = Cookie.identityPatient;

    @FXML
    private AnchorPane contentArea;
    @FXML
    private VBox navbar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pressHomeButton();
    }

    public void pressHomeButton() { loadPage("patientMain.fxml"); }

    public void pressTimeslots() { loadPage("timeSlot.fxml"); }

    public void pressAppointment() { loadPage("patientAppointment.fxml"); }

    public void pressMedicalRecord() { loadPage("medicalRecord.fxml"); }

    public void pressHistoricalAppointment() { loadPage("historicalAppointment.fxml"); }

    private void loadPage(String fxmlFile) {
//        Parent page = cache.get(fxmlFile);
        Parent page;
//        if (page == null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientFXML/" + fxmlFile));
            page = loader.load();
//                cache.put(fxmlFile, page);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
//        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(page);
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
                    Cookie.clearCookie();
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
