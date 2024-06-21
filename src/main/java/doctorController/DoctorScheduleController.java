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
import models.Datas.Schedule;
import models.Users.Doctor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorScheduleController implements Initializable {
    Doctor doctor = Cookie.identityDoctor;

    @FXML
    private TableColumn<Schedule, String> DIDC;

    @FXML
    private Label DID;

    @FXML
    private TableColumn<Schedule, String> DateC;

    @FXML
    private Label SID;

    @FXML
    private TableColumn<Schedule, String> SIDC;

    @FXML
    private TableColumn<Schedule, String> SlotsC;

    @FXML
    private Button addnewschedule;

    @FXML
    private DatePicker date;

    @FXML
    private CheckBox elevenAM;

    @FXML
    private TableColumn<Schedule, String> elevenAMcolumn;

    @FXML
    private CheckBox fivePM;

    @FXML
    private TableColumn<Schedule, String> fivePMcolumn;

    @FXML
    private CheckBox fourPM;

    @FXML
    private TableColumn<Schedule, String> fourPMcolumn;

    @FXML
    private CheckBox nineAM;

    @FXML
    private TableColumn<Schedule, String> nineAMcolumn;

    @FXML
    private CheckBox onePM;

    @FXML
    private TableColumn<Schedule, String> onePMcolumn;

    @FXML
    private TableView<Schedule> ScTable;

    @FXML
    private TextField searchSchedule;

    @FXML
    private CheckBox tenAM;

    @FXML
    private TableColumn<Schedule, String> tenAMcolumn;

    @FXML
    private CheckBox threePM;

    @FXML
    private TableColumn<Schedule, String> threePMcolumn;

    @FXML
    private TableView<Schedule> CurrentScTable;

    @FXML
    private CheckBox twelvePM;

    @FXML
    private TableColumn<Schedule, String> twelvePMcolumn;

    @FXML
    private CheckBox twoPM;

    @FXML
    private TableColumn<Schedule, String> twoPMcolumn;
    private ObservableList<Schedule> schedules = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        this.doctor = Cookie.identityDoctor;

        DID.setText(doctor.getID());

        SIDC.setCellValueFactory(new PropertyValueFactory<>("scheduleID"));
        DIDC.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        DateC.setCellValueFactory(new PropertyValueFactory<>("date"));
        SlotsC.setCellValueFactory(new PropertyValueFactory<>("timeslots"));
        nineAMcolumn.setCellValueFactory(new PropertyValueFactory<>("nineAM"));
        tenAMcolumn.setCellValueFactory(new PropertyValueFactory<>("tenAM"));
        elevenAMcolumn.setCellValueFactory(new PropertyValueFactory<>("elevenAM"));
        twelvePMcolumn.setCellValueFactory(new PropertyValueFactory<>("twelvePM"));
        onePMcolumn.setCellValueFactory(new PropertyValueFactory<>("onePM"));
        twoPMcolumn.setCellValueFactory(new PropertyValueFactory<>("twoPM"));
        threePMcolumn.setCellValueFactory(new PropertyValueFactory<>("threePM"));
        fourPMcolumn.setCellValueFactory(new PropertyValueFactory<>("fourPM"));
        fivePMcolumn.setCellValueFactory(new PropertyValueFactory<>("fivePM"));
        schedules = loadScheduleData();
        FilteredList<Schedule> filteredData = new FilteredList<>(schedules, b -> true);

        //Set the filtered data to the table
        ScTable.setItems(filteredData);
        autoGenerateSID();
        loadCurrentDaySchedule();

        //Add listener for search field
        searchSchedule.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(schedule -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                if (schedule.getScheduleID().toLowerCase().contains(searchKeyword) ||
                        schedule.getDoctorID().toLowerCase().contains(searchKeyword) ||
                        (schedule.getDate() != null && schedule.getDate().contains(searchKeyword)) ||
                        schedule.getTimeslots().toLowerCase().contains(searchKeyword)) {
                    return true;
                }

                return false;
            });    ScTable.setItems(filteredData);

        });
    }

    private ObservableList<Schedule> loadScheduleData(){
        ObservableList<Schedule> schedule = FXCollections.observableArrayList();
        String filePath = "src/main/java/models/TextFiles/schedule";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                Schedule s = new Schedule(details[0], details[1], details[2], details[3]);
                schedule.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    private void autoGenerateSID() {
        try {
            String newId = DataHistory.getNewId("schedule");
            SID.setText(newId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void addNewSchedule() throws IOException {
        if (this.doctor == null) {
            System.err.println("Cannot add medical record. Doctor is not logged in or not properly initialized.");
            return;
        }

        // Get schedule data
        String scheduleID = "SC" + SID.getText();
        String doctorID = DID.getText();
        LocalDate selectedDate = null;
        if (date.getValue() != null) {
            selectedDate = date.getValue();
        } else {
            System.out.println("Please select a date");
            return;
        }
        String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String timeSlots = "9-10-11-12-13-14-15-16-17";
        String[] slots = timeSlots.split("-");

        // Replace specific slots based on checkboxes
        if (nineAM.isSelected()) slots[0] = "C";
        if (tenAM.isSelected()) slots[1] = "C";
        if (elevenAM.isSelected()) slots[2] = "C";
        if (twelvePM.isSelected()) slots[3] = "C";
        if (onePM.isSelected()) slots[4] = "C";
        if (twoPM.isSelected()) slots[5] = "C";
        if (threePM.isSelected()) slots[6] = "C";
        if (fourPM.isSelected()) slots[7] = "C";
        if (fivePM.isSelected()) slots[8] = "C";

        // Join the slots array back into a string
        timeSlots = String.join("-", slots);
        String finalTime;

        // Check if all slots have been replaced
        if (timeSlots.equals("C-C-C-C-C-C-C-C-C")) {
            finalTime = "[C-C-C-C-C-C-C-C-C]\n";
        } else {
            finalTime = MessageFormat.format("[{0}]\n", timeSlots);
        }

        doctor.addSchedule(scheduleID, doctorID, formattedDate, finalTime);
        // Reload data into TableView
        autoGenerateSID();
        ScTable.setItems(loadScheduleData());

        // Clear the form fields
        date.setValue(null);
        nineAM.setSelected(false);
        tenAM.setSelected(false);
        elevenAM.setSelected(false);
        twelvePM.setSelected(false);
        onePM.setSelected(false);
        twoPM.setSelected(false);
        threePM.setSelected(false);
        fourPM.setSelected(false);
        fivePM.setSelected(false);
    }

    private ObservableList<Schedule> loadCurrentSchedule(){
        ObservableList<Schedule> schedule = FXCollections.observableArrayList();
        String filePath = "src/main/java/models/TextFiles/schedule";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(", ");
                Schedule s = new Schedule(details[0], details[1], details[2], details[3]);
                schedule.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedule;
    }
    public int loadCurrentDaySchedule() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String todayStr = today.format(formatter);

        ObservableList<Schedule> allSchedules = loadScheduleData();
        ObservableList<Schedule> currentDaySchedules = FXCollections.observableArrayList();

        int totalHoursWorked = 0;

        for (Schedule schedule : allSchedules) {
            if (schedule.getDate().equals(todayStr)) {
                currentDaySchedules.add(schedule);

                // Calculate the hours worked for the current schedule
                String timeslots = schedule.getTimeslots();
                String[] slots = timeslots.split("-");

                for (String slot : slots) {
                    if (!slot.equals("C")) {
                        totalHoursWorked += 1; // Each valid slot counts as one hour
                    }
                }
            }
        }
        return totalHoursWorked;
    }
}

