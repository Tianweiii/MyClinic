package doctorController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Auth.Cookie;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DoctorMenuController implements Initializable {

    @FXML
    private Button Logoutbutton;
    @FXML
    private VBox MenuBar;
    @FXML
    private Button appointmentbutton;
    @FXML
    private Button homebutton;
    @FXML
    private Button medicalrecordbutton;
    @FXML
    private Button schedulebutton;
    @FXML
    private AnchorPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pressHomeButton();
    }
    @FXML
    public void pressHomeButton() {
        loadPage("drHome.fxml");
    }

    public void pressScheduleButton() {
        loadPage("drSchedule.fxml");
    }

    public void pressAppButton() {
        loadPage("drAppointment.fxml");
    }

    public void pressMedRecButton() {
        loadPage("drMedicalRecordFORM.fxml");
    }

    public void pressMedicineButton() {
        loadPage("drMedicine.fxml");
    }

    public void pressRecordList() {
        loadPage("drMedicalRecordLIST.fxml");
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
                Stage stage = (Stage) MenuBar.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        });
    }

//        @FXML
//        private void switchToList(String event) {
//            try {
//                Parent listRoot = FXMLLoader.load(getClass().getResource("/doctorFXML/drMedicalRecordLIST.fxml"));
//                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                Scene scene = new Scene(listRoot);
//                stage.setScene(scene);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void loadPage(String fxmlFile) {
        Parent page;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/doctorFXML/" + fxmlFile));
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(page);
    }

}
