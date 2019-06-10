package Otopark;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSubscriptionController implements Initializable {

    @FXML private JFXTreeTableView subscribedVehiclesTable;
    @FXML private JFXTextField plateTextField;
    @FXML private JFXDatePicker datePickerBegin;
    @FXML private JFXDatePicker datePickerEnd;
    private JFXTreeTableColumn<SubscribedVehicle,String> plateCol;
    private  JFXTreeTableColumn<SubscribedVehicle,String> dateBeginCol;
    private  JFXTreeTableColumn<SubscribedVehicle,String> dateEndCol;


    ObservableList<SubscribedVehicle> subscribedVehicles = FXCollections.observableArrayList(AutoPark.getInstance().getSubscribedVehicles());
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prepareCols();
    }


    public void prepareCols(){

        plateCol = new JFXTreeTableColumn<>("Plate");
        plateCol.setPrefWidth(200);

        plateCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SubscribedVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SubscribedVehicle, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getPlate());
            }
        });

        dateBeginCol = new JFXTreeTableColumn<>("Subscription Begin Date");
        dateBeginCol.setPrefWidth(200);

        dateBeginCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SubscribedVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SubscribedVehicle, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getSubscription().getBegin().toString());
            }
        });

        dateEndCol = new JFXTreeTableColumn<>("Subscription End Date");
        dateEndCol.setPrefWidth(200);

        dateEndCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<SubscribedVehicle, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<SubscribedVehicle, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().getSubscription().getEnd().toString());
            }
        });


        subscribedVehiclesTable.getColumns().addAll(plateCol,dateBeginCol,dateEndCol);
        final TreeItem<SubscribedVehicle> root = new RecursiveTreeItem<>(subscribedVehicles, RecursiveTreeObject::getChildren);
        subscribedVehiclesTable.setRoot(root);
        subscribedVehiclesTable.setShowRoot(false);


    }
    @FXML
    public void addSubscription(){
        String[] beginDate = datePickerBegin.getValue().toString().split("-");
        String[] endDate = datePickerEnd.getValue().toString().split("-");
        Subscription subscription = new Subscription(plateTextField.getText(),
                new Date(Integer.parseInt(beginDate[2]),Integer.parseInt(beginDate[1]), Integer.parseInt(beginDate[0])),
                new Date(Integer.parseInt(endDate[2]),Integer.parseInt(endDate[1]),Integer.parseInt(endDate[0])));
        AutoPark.getInstance().addVehicle(subscription.getVehicle());

        //TreeItem<SubscribedVehicle> subscribedVehicleTreeItem = new TreeItem<>(subscription.getVehicle());
        subscribedVehicles = FXCollections.observableArrayList(AutoPark.getInstance().getSubscribedVehicles());
        subscribedVehiclesTable.setRoot(new RecursiveTreeItem<SubscribedVehicle>(subscribedVehicles, RecursiveTreeObject::getChildren));
        subscribedVehiclesTable.setShowRoot(false);
    }


}
