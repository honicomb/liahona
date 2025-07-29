module com.whitcomb.liahona {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.whitcomb.liahona to javafx.fxml;
    exports com.whitcomb.liahona;
}