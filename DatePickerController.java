package Otopark;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class DatePickerController {

    @FXML private JFXDatePicker datePicker;
    @FXML private JFXTextField hourlyFee;
    @FXML private JFXTextField capacity;


    @FXML
    public void onDateEntered(ActionEvent event) throws IOException {
        String[] todayDate = datePicker.getValue().toString().split("-");
        Date.setToday(new Date(   Integer.parseInt(todayDate[2]),Integer.parseInt(todayDate[1]),Integer.parseInt(todayDate[0])  ));
        System.out.println(Date.getToday());

        AutoPark.getInstance().setHourlyFee(Double.parseDouble(hourlyFee.getText()));
        AutoPark.getInstance().setCapacity(Integer.parseInt(capacity.getText()));

        Parent mainScene = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(mainScene));





    }



}
