module com.subscriptionbox {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.subscriptionbox.ui to javafx.fxml;
    opens com.subscriptionbox.ui.controllers to javafx.fxml;
    opens com.subscriptionbox.ui.fxml to javafx.fxml;
    
    exports com.subscriptionbox.ui;
    exports com.subscriptionbox.ui.controllers;
    exports com.subscriptionbox.models;
    exports com.subscriptionbox.services;
    exports com.subscriptionbox.dao;
} 