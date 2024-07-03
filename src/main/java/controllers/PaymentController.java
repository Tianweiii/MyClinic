package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.Auth.Cookie;
import models.Auth.Verification;
import models.Datas.Appointment;
import models.Datas.DataHistory;
import models.Datas.Payment;
import models.Filing.FileIO;
import models.Users.Admin;
import models.Users.User;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    Admin admin = Cookie.identityAdmin;
    private FilteredList<Payment> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        paymentPatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        paymentAppointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("method"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("datePaid"));
        addToPaymentTable();
        setNewPaymentId();
        setPaymentDate();

        try {
            setPatientIdComboBox();
            filteredList = new FilteredList<>(getPayment(), predicate -> true);
            paymentTable.setItems(filteredList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchPayments(newValue));

        patientIdComboBox.setOnAction(e -> {
            if (patientIdComboBox.getValue() != null) {
                try {
                    setAppointmentIdComboBox();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        appointmentIdComboBox.setOnAction(e -> {
            if (appointmentIdComboBox.getValue() != null) {
                try {
                    setAppointmentData(appointmentIdComboBox.getValue());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @FXML
    private TextField searchBar;
    public void searchPayments(String input) {
        String keyword = input.toLowerCase();
        filteredList.setPredicate(data -> {
            if (input.isEmpty() || input.isBlank() || input == null) {
                return true;
            }

            if (data.getPaymentID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getPatientID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getAppointmentID().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getAmount().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getMethod().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else if (data.getDatePaid().toLowerCase().indexOf(keyword) > -1) {
                return true;
            } else {
                return false;
            }
        });
        SortedList<Payment> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(paymentTable.comparatorProperty());
        paymentTable.setItems(sortedData);
    }

    @FXML
    private TableView<Payment> paymentTable;
    @FXML
    private TableColumn<Payment, String> paymentAmountColumn;
    @FXML
    private TableColumn<Payment, String> paymentAppointmentIdColumn;
    @FXML
    private TableColumn<Payment, String> paymentMethodColumn;
    @FXML
    private TableColumn<Payment, String> paymentPatientIdColumn;
    @FXML
    private TableColumn<Payment, String> paymentDateColumn;
    @FXML
    private TableColumn<Payment, String> paymentIdColumn;
    ObservableList<Payment> getPayment() throws IOException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        payments.addAll(Payment.getAllPayments());
        return payments;
    }
    public void addToPaymentTable() {
        try {
            paymentTable.setItems(getPayment());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clickTableRow(MouseEvent e) throws IOException {
        if (e.getClickCount() == 1 && !paymentTable.getSelectionModel().isEmpty()) {
            addButton.setDisable(true);
            editButton.setDisable(false);
            Payment selection = paymentTable.getSelectionModel().getSelectedItem();
            setPaymentData(selection.getPaymentID(), selection.getPatientID(), selection.getMethod());
            setAppointmentData(selection.getAppointmentID());
        }
    }

    @FXML
    private Text paymentId;
    @FXML
    private ComboBox<String> patientIdComboBox;
    @FXML
    private ComboBox<String> appointmentIdComboBox;
    @FXML
    private TextField paymentAmount;
    @FXML
    private Text paymentDate;
    public void setNewPaymentId() {
        String id;
        try {
            id = DataHistory.getNewId("payment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        paymentId.setText("PY" + id);
    }
    public void setPaymentDate() {
        Date data = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(data);
        paymentDate.setText(currentDate);
    }
    public void setPatientIdComboBox() throws IOException {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (String patient : User.getAccounts("patient")) {
            String[] row = FileIO.splitString(patient);
            list.add(MessageFormat.format("{0} {1}", row[0], row[1]));
        }
        patientIdComboBox.setItems(list);
    }
    public void setAppointmentIdComboBox() throws IOException {
        appointmentIdComboBox.getSelectionModel().clearSelection();
        appointmentIdComboBox.setDisable(false);
        String patientId = patientIdComboBox.getValue();
        ObservableList<String> list = Appointment.getPatientAppointments(patientId.split(" ")[0]);
        appointmentIdComboBox.setItems(list);
    }
    public void setPaymentData(String paymentID, String patientID, String method) throws IOException {
        Payment target = Payment.findPayment(paymentID, patientID);
        assert target != null;
        paymentId.setText(target.getPaymentID());
        patientIdComboBox.setValue(target.getPatientID());
        patientIdComboBox.setDisable(true);
        appointmentIdComboBox.setValue(target.getAppointmentID());
        appointmentIdComboBox.setDisable(true);
        paymentAmount.setText(target.getAmount());
        paymentDate.setText(target.getDatePaid());
        if (method.equals("card")) {
            cardRadio.setSelected(true);
        } else {
            cashRadio.setSelected(true);
        }
        cardRadio.setDisable(true);
        cashRadio.setDisable(true);
    }
    public void resetPaymentData() {
        setNewPaymentId();
        patientIdComboBox.setValue(null);
        patientIdComboBox.setDisable(false);
        appointmentIdComboBox.setValue(null);
        appointmentIdComboBox.setDisable(true);
        paymentAmount.setText("");
        setPaymentDate();
    }

    @FXML
    private Text appointmentDetailId;
    @FXML
    private Text patientDetailId;
    @FXML
    private Text doctorDetailId;
    @FXML
    private Text dateDetailId;
    @FXML
    private Text descriptionDetailId;
    public void setAppointmentData(String appointmentID) throws IOException {
        Appointment target = Appointment.findAppointment(appointmentID);
        assert target != null;
        appointmentDetailId.setText(target.getAppointmentID());
        patientDetailId.setText(target.getPatientID());
        doctorDetailId.setText(target.getDoctorID());
        dateDetailId.setText(target.getDate());
        descriptionDetailId.setText(target.getDescription());
    }
    public void resetAppointmentData() {
        appointmentDetailId.setText("Appointment ID");
        patientDetailId.setText("Patient ID");
        doctorDetailId.setText("Doctor ID");
        dateDetailId.setText("Date");
        descriptionDetailId.setText("Description");
    }


    @FXML
    private ToggleGroup paymentMethod;
    @FXML
    private RadioButton cardRadio;
    @FXML
    private RadioButton cashRadio;
    public String getPaymentDetail() {
        if (cardRadio.isSelected()) {
            return cardRadio.getText().toLowerCase();
        } else if (cashRadio.isSelected()) {
            return cashRadio.getText().toLowerCase();
        }
        return "";
    }
    public void resetPaymentDetail() {
        paymentMethod.selectToggle(null);
        cardRadio.setDisable(false);
        cashRadio.setDisable(false);
    }

    @FXML
    private Button addButton;
    private boolean isInt(String i) {
        try {
            Integer.parseInt(i);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void addPayment() {
        String patientId = "";
        String appointmentId = "";
        String amount = "";
        String method = getPaymentDetail();
        int flag = 0;

        if (patientIdComboBox.getValue() != null) {
            patientId = patientIdComboBox.getValue().split(" ")[0];
        }
        if (appointmentIdComboBox.getValue() != null) {
            appointmentId = appointmentIdComboBox.getValue();
        }
        if (isInt(paymentAmount.getText())) {
            amount = paymentAmount.getText();
        } else {
            flag = 1;
        }

        if (!Verification.verifyEmptyFields(patientId, appointmentId, amount, method) && flag == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add payment error");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        } else if (!Verification.verifyEmptyFields(patientId, appointmentId, amount, method) && flag == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add payment error");
            alert.setContentText("Please fill in a valid number for amount");
            alert.showAndWait();
            return;
        }

        try {
            admin.addPayment(patientId, appointmentId, amount, method);
            filteredList = new FilteredList<>(getPayment(), predicate -> true);
            paymentTable.setItems(filteredList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment");
        alert.setContentText("Payment Successfully Made");
        alert.showAndWait();
        addToPaymentTable();
        resetFields();
    }

    @FXML
    private Button editButton;
    public void editPayment() throws IOException {
        String id = paymentId.getText();
        String patientId = patientIdComboBox.getValue().split(" ")[0];
        String oldAmount = Payment.getOldPaymentAmount(id);
        String amount = "";
        int flag = 0;

        if (isInt(paymentAmount.getText())) {
            amount = paymentAmount.getText();
        } else {
            flag = 1;
        }

        if (flag == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit Payment error");
            alert.setContentText("Please fill in a valid number in amount");
            alert.showAndWait();
            return;
        } else if (amount.equals(oldAmount)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit Payment error");
            alert.setContentText("New amount is the same as the old amount");
            alert.showAndWait();
            return;
        }

        admin.managePayment(id, patientId, amount);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment");
        alert.setContentText("Payment Successfully Made");
        alert.showAndWait();
        addToPaymentTable();
        resetFields();

    }

    @FXML
    private Button resetButton;
    public void resetFields() {
        paymentTable.getSelectionModel().clearSelection();
        addButton.setDisable(false);
        editButton.setDisable(true);
        resetPaymentData();
        resetAppointmentData();
        resetPaymentDetail();
    }
}


