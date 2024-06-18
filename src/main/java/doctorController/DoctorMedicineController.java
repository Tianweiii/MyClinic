package doctorController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Auth.Cookie;
import models.Datas.DataHistory;
import models.Datas.Medicine;
import models.Users.Doctor;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DoctorMedicineController implements Initializable {
    Doctor doctor = Cookie.identityDoctor;

    @FXML
    private TableColumn<Medicine, String> MedCatcol;

    @FXML
    private TableColumn<Medicine, String> MedIDcol;

    @FXML
    private TableColumn<Medicine, String> MedNamecol;

    @FXML
    private TableView<Medicine> MedTable;

    @FXML
    private TableColumn<Medicine, String> MedUsecol;

    @FXML
    private ComboBox<String> medCatinput;

    @FXML
    private Label medIDinput;

    @FXML
    private TextField medNameinput;

    @FXML
    private TextField medSearch;

    @FXML
    private TextArea medUseinput;

    private ObservableList<Medicine> med;
    private Integer index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MedIDcol.setCellValueFactory(new PropertyValueFactory<>("medicineID"));
        MedNamecol.setCellValueFactory(new PropertyValueFactory<>("name"));
        MedCatcol.setCellValueFactory(new PropertyValueFactory<>("category"));
        MedUsecol.setCellValueFactory(new PropertyValueFactory<>("usage"));
        autoMedID();
        med = loadMedicine();
        FilteredList<Medicine> filteredData = new FilteredList<>(med, b -> true);
        MedTable.setItems(filteredData);

        medSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                return record.getMedicineID().toLowerCase().contains(searchKeyword) ||
                        record.getName().toLowerCase().contains(searchKeyword) ||
                        record.getCategory().toLowerCase().contains(searchKeyword) ||
                        record.getUsage().toLowerCase().contains(searchKeyword);
            });
        });

        List<String> predefinedCategories = List.of("Analgesics","Antiarrhythmics","Antibacterial","Antibiotic","Antidiabetic","Antidiarrheal","Antihistamine","Antihypertensive","Anti-Inflammatory","Antipyretic","Diuretic","Hormones","Muscle Relaxants","Sleeping Drugs","Vitamins");
        Set<String> fileCategories = med.stream().map(Medicine::getCategory).collect(Collectors.toSet());
        List<String> allCategories = new ArrayList<>(predefinedCategories);
        allCategories.addAll(fileCategories);
        allCategories = allCategories.stream().distinct().sorted().collect(Collectors.toList());
        medCatinput.setItems(FXCollections.observableArrayList(allCategories));
    }

    private ObservableList<Medicine> loadMedicine() {
        ObservableList<Medicine> med = FXCollections.observableArrayList();
        String filePath = "src/main/java/models/TextFiles/medicine";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                if (details.length == 4) { // Ensure that the line has exactly 4 parts
                    Medicine medical = new Medicine(details[0], details[1], details[2], details[3]);
                    med.add(medical);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return med;
    }

    private void saveMedicineToFile() {
        String filePath = "src/main/java/models/TextFiles/medicine";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Medicine m : med) {
                bw.write(m.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getRecord() {
        index = MedTable.getSelectionModel().getFocusedIndex();
        if (index <= -1) {
            return;
        }
        Medicine selectedMedicine = MedTable.getItems().get(index);
        medIDinput.setText(selectedMedicine.getMedicineID());
        medNameinput.setText(selectedMedicine.getName());
        medCatinput.setValue(selectedMedicine.getCategory());
        medUseinput.setText(selectedMedicine.getUsage());
    }

    @FXML
    public void saveChanges() {
        String name = medNameinput.getText();
        String category = medCatinput.getValue();
        String usage = medUseinput.getText();

        String medID = medIDinput.getText();
        medIDinput.setText(medID); // Update medIDinput label with new ID

        if (index == null || index <= -1) {
            // Adding new medicine
            Medicine newMedicine = new Medicine(medID, name, category, usage);
            med.add(newMedicine);
            try {
                DataHistory.updateDataHistoryCount("medicine");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Editing existing medicine
            Medicine selectedMedicine = MedTable.getItems().get(index);
            selectedMedicine.setName(name);
            selectedMedicine.setCategory(category);
            selectedMedicine.setUsage(usage);
        }

        MedTable.refresh(); // Refresh TableView
        saveMedicineToFile();
        clearInput();
    }



    @FXML
    private void clearInput() {
        String filePath = "src/main/java/models/TextFiles/medicine";
        int recordCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) {
                recordCount++;
            }
            medIDinput.setText("MED" + (recordCount + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        medNameinput.clear();
        medCatinput.setValue(null);
        medUseinput.clear();
        index = -1;
    }

    private void autoMedID() {
//        String filePath = "src/main/java/models/TextFiles/medicine";
//        int recordCount = 0;
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            while (br.readLine() != null) {
//                recordCount++;
//            }
//            medIDinput.setText("MED" + (recordCount + 1));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            String newId = DataHistory.getNewId("medicine");
            medIDinput.setText("MED" + newId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
