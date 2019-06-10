package Otopark;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VehicleEnterExitController {

    @FXML private JFXTextField plateTextField;
    @FXML private JFXTimePicker timePicker;
    @FXML private JFXDatePicker datePicker;
    @FXML private JFXCheckBox officialCheckBox;
    private AutoPark autoPark = AutoPark.getInstance();
    @FXML
    public void onVehicleEntersButton(ActionEvent event){

        String time[] = timePicker.getValue().toString().split(":");
        String date[] = datePicker.getValue().toString().split("-");
        //System.out.println(time[0] + time[1]);

        autoPark.vehicleEnters(plateTextField.getText(),new Time(Integer.parseInt(time[0]),Integer.parseInt(time[1])),
                officialCheckBox.isSelected(),new Date(Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0])));
        //AutoPark.setSingle_instance(autoPark);


        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.getOnCloseRequest().handle(new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST));
        window.close();
    }

    @FXML
    public void onVehicleExitsButton(ActionEvent event){
        String time[] = timePicker.getValue().toString().split(":");
        String date[] = datePicker.getValue().toString().split("-");

        autoPark.vehicleExits(plateTextField.getText(),new Date(Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0])),
                new Time(Integer.parseInt(time[0]),Integer.parseInt(time[1])));

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.getOnCloseRequest().handle(new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST));
        window.close();

    }
}
