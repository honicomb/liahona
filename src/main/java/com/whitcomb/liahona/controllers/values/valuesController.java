package com.whitcomb.liahona.controllers.values;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import com.whitcomb.liahona.models.values.DBHandlerValues;

public class valuesController implements Initializable {

    @FXML private Button beingTrueBtn;
    @FXML private Button comfortBtn;
    @FXML private Button familyBtn;
    @FXML private Button familyHistoryBtn;
    @FXML private Button feelSpiritBtn;
    @FXML private Button financialStewardshipBtn;
    @FXML private Button healthBtn;
    @FXML private Button inControlBtn;
    @FXML private Button intimacyBtn;
    @FXML private Button obedienceBtn;
    @FXML private Button orderBtn;
    @FXML private ImageView pntIconImgView;
    @FXML private Button posterityBtn;
    @FXML private Button relationshipBtn;
    @FXML private Button truthBtn;
    @FXML private Label valueBoxTitleLbl;
    @FXML private TextArea valueHowTextLbl;
    @FXML private TextArea valueWhyTextLbl;
    @FXML private ResourceBundle resources;
    @FXML private URL location;

    PreparedStatement pre;
    ResultSet rs;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obedienceBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V1");
            populateValueWhy("V1");
            populateHowBulletPoints("V1");
        });

        feelSpiritBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V2");
            populateValueWhy("V2");
            populateHowBulletPoints("V2");
        });

        truthBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V3");
            populateValueWhy("V3");
            populateHowBulletPoints("V3");
        });

        beingTrueBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V4");
            populateValueWhy("V4");
            populateHowBulletPoints("V4");
        });

        relationshipBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V5");
            populateValueWhy("V5");
            populateHowBulletPoints("V5");
        });

        inControlBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V6");
            populateValueWhy("V6");
            populateHowBulletPoints("V6");
        });

        familyBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V7");
            populateValueWhy("V7");
            populateHowBulletPoints("V7");
        });

        healthBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V8");
            populateValueWhy("V8");
            populateHowBulletPoints("V8");
        });

        intimacyBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V9");
            populateValueWhy("V9");
            populateHowBulletPoints("V9");
        });

        familyHistoryBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V10");
            populateValueWhy("V10");
            populateHowBulletPoints("V10");
        });

        orderBtn.setOnAction(e -> {
           clearFields();
           populateTitleOfValue("V11");
           populateValueWhy("V11");
           populateHowBulletPoints("V11");
        });

        comfortBtn.setOnAction(e -> {
           clearFields();
           populateTitleOfValue("V12");
           populateValueWhy("V12");
           populateHowBulletPoints("V12");
        });

        posterityBtn.setOnAction(e -> {
           clearFields();
           populateTitleOfValue("V13");
           populateValueWhy("V13");
           populateHowBulletPoints("V13");
        });

        financialStewardshipBtn.setOnAction(e -> {
            clearFields();
            populateTitleOfValue("V14");
            populateValueWhy("V14");
            populateHowBulletPoints("V14");
        });
    }

    public void clearFields() {
        valueBoxTitleLbl.setText("");
        valueWhyTextLbl.setText("");
        valueHowTextLbl.setText("");
    }

    public void populateTitleOfValue(String valueID) {
        try {
            DBHandlerValues db = new DBHandlerValues();
            Connection conn = db.getConnection();

            pre = conn.prepareStatement("SELECT value_name FROM value WHERE value_id = '" + valueID + "'");
            rs = pre.executeQuery();

            while (rs.next()) {
                valueBoxTitleLbl.setText(rs.getString("value_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateValueWhy(String valueID) {
        try {
            DBHandlerValues db = new DBHandlerValues();
            Connection conn = db.getConnection();

            pre = conn.prepareStatement("SELECT why FROM value WHERE value_id = '" + valueID + "'");
            rs = pre.executeQuery();

            while (rs.next()) {
                valueWhyTextLbl.setText(rs.getString("why"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateHowBulletPoints(String valueID) {
        try {
            DBHandlerValues db = new DBHandlerValues();
            Connection conn = db.getConnection();

            pre = conn.prepareStatement("SELECT how_desc FROM how WHERE value_id = '" + valueID + "'");
            rs = pre.executeQuery();

            while (rs.next()) {
                 valueHowTextLbl.setText(valueHowTextLbl.getText() + "\u25CB " + rs.getString("how_desc") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
