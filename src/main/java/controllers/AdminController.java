package controllers;

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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pressHomeButton();
    }

    @FXML
    private VBox navbar;
    @FXML
    private AnchorPane contentArea;

    private Map<String, Parent> cache = new HashMap<>();

    public void pressHomeButton() {
        loadPage("home.fxml");
    }

    public void pressManageUserButton() {
        loadPage("manageUser.fxml");
    }

    public void pressWalkInButton() {
        loadPage("walkIn.fxml");
    }

    public void pressTrackAptButton() {
        loadPage("trackAppointment.fxml");
    }

    public void pressTrackMedRecButton() {
        loadPage("trackMedRec.fxml");
    }

    public void pressPaymentButton() {
        loadPage("payment.fxml");
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

    private void loadPage(String fxmlFile) {
//        Parent page = cache.get(fxmlFile);
        Parent page;
//        if (page == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cmsclinic/" + fxmlFile));
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
}
