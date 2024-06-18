package doctorController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import models.Auth.Cookie;
import models.Datas.MedicalRecord;
import javafx.fxml.Initializable;
import models.Users.Doctor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DoctorMedicalRecordListController implements Initializable {
    Doctor doctor = Cookie.identityDoctor;

    @FXML
    private TableView<MedicalRecord> MRTable;

    @FXML
    private TableColumn<MedicalRecord, String> MRApp;

    @FXML
    private TableColumn<MedicalRecord, String> MRDesc;

    @FXML
    private TableColumn<MedicalRecord, String> MRDiag;

    @FXML
    private TableColumn<MedicalRecord, String> MRMed;

    @FXML
    private TableColumn<MedicalRecord, String> MRPID;

    @FXML
    private TableColumn<MedicalRecord, String> MRRID;

    @FXML
    private TextField SearchMR;

    @FXML
    private Button addNewMR;

    @FXML
    private TextField showAID;

    @FXML
    private TextArea showDes;

    @FXML
    private TextArea showDia;

    @FXML
    private TextArea showMed;

    @FXML
    private TextField showPID;

    @FXML
    private TextField showRID;


    private ObservableList<MedicalRecord> records;
    Integer index;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MRRID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        MRPID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        MRApp.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        MRDiag.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        MRMed.setCellValueFactory(new PropertyValueFactory<>("medication"));
        MRDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        //Load and filter data
        records = loadMedicalRecords();
        FilteredList<MedicalRecord> filteredData = new FilteredList<>(records, b -> true);

        //Set the filtered data to the table
        MRTable.setItems(filteredData);

        //Add listener for search field
        SearchMR.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                if (record.getRecordID().toLowerCase().contains(searchKeyword) ||
                        record.getPatientID().toLowerCase().contains(searchKeyword) ||
                        record.getAppointmentID().toLowerCase().contains(searchKeyword) ||
                        record.getDiagnosis().toLowerCase().contains(searchKeyword) ||
                        record.getMedication().toLowerCase().contains(searchKeyword) ||
                        record.getDescription().toLowerCase().contains(searchKeyword)) {
                    return true;
                }
                return false;
            });
        });
    }

    private ObservableList<MedicalRecord> loadMedicalRecords() { //load data from textfile
        ObservableList<MedicalRecord> records = FXCollections.observableArrayList();
        String filePath = "src/main/java/models/TextFiles/medicalRecord";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                MedicalRecord record = new MedicalRecord(details[0], details[1], details[2], details[3], details[4], details[5]);
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

//    @FXML
//    private void switchToForm(ActionEvent event) { // Change from one FXML to another
//        try {
//            Parent formRoot = FXMLLoader.load(getClass().getResource("/doctorFXML/drMedicalRecordFORM.fxml"));
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(formRoot);
//            stage.setScene(scene);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    @FXML
    public void getRecord() {
        index = MRTable.getSelectionModel().getFocusedIndex();

        if(index<=-1){
            return;
        }
        showRID.setText(MRRID.getCellData(index).toString());
        showPID.setText(MRPID.getCellData(index).toString());
        showAID.setText(MRApp.getCellData(index).toString());
        showDia.setText(MRDiag.getCellData(index).toString());
        showMed.setText(MRMed.getCellData(index).toString());
        showDes.setText(MRDesc.getCellData(index).toString());

        SearchMR.setText("");
    }
}
