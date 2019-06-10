package Otopark;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.reflect.generics.tree.Tree;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML private JFXTreeTableView parkRecordsTable;
    @FXML private JFXTreeTableColumn<ParkRecord,String> plateCol;
    @FXML private JFXTreeTableColumn<ParkRecord,String> enterTimeCol;
    @FXML private JFXTreeTableColumn<ParkRecord,String> exitTimeCol;
    @FXML private JFXTreeTableColumn<ParkRecord,String> enterDateCol;
    @FXML private JFXTreeTableColumn<ParkRecord,String> exitDateCol;
    @FXML private JFXTreeTableColumn<ParkRecord,String> vehicleTypeCol;
    @FXML private JFXTextField hourlyFeeTextField;
    @FXML private JFXTextField capacityTextField;
    @FXML private Label updateMessage;
    @FXML private Label currentIncomeMessage;
    @FXML private Label todayMessage;

    private AutoPark autoPark = AutoPark.getInstance();
    ObservableList<ParkRecord> parkRecords = FXCollections.observableArrayList(autoPark.getParkRecords());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentIncomeMessage.setText("Current Income:\n0");
        todayMessage.setText("Today:\n" + Date.getToday());
        hourlyFeeTextField.setPromptText("Current Hourly Fee: " + AutoPark.getInstance().getHourlyFee());
        capacityTextField.setPromptText("Current Capacity: " + AutoPark.getInstance().getCapacity());
            prepareCols();
    }

    public void prepareCols(){
        plateCol = new JFXTreeTableColumn<>("Plate");
        plateCol.setPrefWidth(120);
        plateCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ParkRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ParkRecord, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getVehicle().getPlate());
            }
        });

        enterTimeCol = new JFXTreeTableColumn<>("Enter Time");
        enterTimeCol.setPrefWidth(120);
        enterTimeCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ParkRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ParkRecord, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getEnterTime().toString());
            }
        });

        exitTimeCol = new JFXTreeTableColumn<>("Exit Time");
        exitTimeCol.setPrefWidth(120);
        exitTimeCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ParkRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ParkRecord, String> param) {
                if(param.getValue().getValue().getExitTime()==null)
                    return new SimpleStringProperty("Didn't Leave Yet.");
                return new SimpleStringProperty(param.getValue().getValue().getExitTime().toString());
            }
        });


        enterDateCol = new JFXTreeTableColumn<>("Enter Date");
        enterDateCol.setPrefWidth(120);
        enterDateCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ParkRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ParkRecord, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getEnterDate().toString());
            }
        });

        exitDateCol = new JFXTreeTableColumn<>("Exit Date");
        exitDateCol.setPrefWidth(120);
        exitDateCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ParkRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ParkRecord, String> param) {
                if(param.getValue().getValue().getExitDate()==null)
                    return new SimpleStringProperty("Didn't Leave Yet.");
                return new SimpleStringProperty(param.getValue().getValue().getExitDate().toString());
            }
        });

        vehicleTypeCol = new JFXTreeTableColumn<>("Vehicle Type");
        vehicleTypeCol.setPrefWidth(160);
        vehicleTypeCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ParkRecord, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ParkRecord, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getVehicle().toString());
            }
        });


        parkRecordsTable.getColumns().addAll(plateCol,enterTimeCol,exitTimeCol,enterDateCol,exitDateCol,vehicleTypeCol);


        final TreeItem<ParkRecord> root = new RecursiveTreeItem<ParkRecord>(parkRecords, RecursiveTreeObject::getChildren);
        parkRecordsTable.setRoot(root);
        parkRecordsTable.setShowRoot(false);


    }


    public void updateParkRecords(){
        System.out.println("asd");
        autoPark = AutoPark.getInstance();
        parkRecords = FXCollections.observableArrayList(autoPark.getParkRecords());
        final TreeItem<ParkRecord> root = new RecursiveTreeItem<ParkRecord>(parkRecords, RecursiveTreeObject::getChildren);
        parkRecordsTable.setRoot(root);
        parkRecordsTable.setShowRoot(false);

    }

    @FXML
    public void onVehicleEnters(ActionEvent event)throws IOException {
        Parent vehicleEnterScene = FXMLLoader.load(getClass().getResource("VehicleEnterScene.fxml"));
        Stage window = new Stage();
        window.setScene(new Scene(vehicleEnterScene));
        window.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
        window.setOnCloseRequest(e->updateParkRecords());
    }

    @FXML
    public void onVehicleExits(ActionEvent event) throws IOException{
        Parent vehicleExitScene = FXMLLoader.load(getClass().getResource("VehicleExitScene.fxml"));
        Stage window = new Stage();
        window.setScene(new Scene(vehicleExitScene));
        window.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
        window.setOnCloseRequest(e->updateParkRecords());

    }

    @FXML
    public void updateHourlyFee(){
        AutoPark.getInstance().setHourlyFee(Double.parseDouble(hourlyFeeTextField.getText()));
        hourlyFeeTextField.clear();
        updateMessage.setText("Hourly Fee has been updated.");
        hourlyFeeTextField.setPromptText("Current Hourly Fee: " + AutoPark.getInstance().getHourlyFee());

    }

    @FXML
    public void updateCapacity(){
        AutoPark.getInstance().setCapacity(Integer.parseInt(capacityTextField.getText()));
        capacityTextField.clear();
        updateMessage.setText("Capacity has been updated.");
        capacityTextField.setPromptText("Current Capacity: " + AutoPark.getInstance().getCapacity());
    }

    @FXML
    public void openAddSubscription(ActionEvent event) throws IOException{
        Parent addSubscriptionScene = FXMLLoader.load(getClass().getResource("AddSubscriptionScene.fxml"));
        Stage window = new Stage();
        window.setScene(new Scene(addSubscriptionScene));
        window.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();

    }




}
