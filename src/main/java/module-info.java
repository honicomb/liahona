module com.whitcomb.liahona {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.whitcomb.liahona;
    exports com.whitcomb.liahona.models.goals;
    exports com.whitcomb.liahona.models.values;
    exports com.whitcomb.liahona.models.configs;
    exports com.whitcomb.liahona.models.tracking;
    exports com.whitcomb.liahona.controllers.values to javafx.fxml;
    exports com.whitcomb.liahona.controllers.goals to javafx.fxml;
    exports com.whitcomb.liahona.controllers.tracking to javafx.fxml;

    opens com.whitcomb.liahona to javafx.fxml;
    opens com.whitcomb.liahona.controllers.values to javafx.fxml;
    opens com.whitcomb.liahona.controllers.goals to javafx.fxml;
    opens com.whitcomb.liahona.controllers.tracking to javafx.fxml;
}