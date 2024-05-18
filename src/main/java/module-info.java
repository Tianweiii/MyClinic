module org.example.learnjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.cmsclinic to javafx.fxml;
    exports org.example.cmsclinic;
    exports controllers;
    opens controllers to javafx.fxml;
    opens models.Users to javafx.base;
    opens models.Datas to javafx.base;
}