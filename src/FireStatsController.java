import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by kraus on 27.03.2017.
 */
public class FireStatsController implements Initializable {
    @FXML
    public TableView table;
    public TableColumn pName;
    public TableColumn pAzimuth;
    public TableColumn pElevation;
    public TableColumn pX;
    public TableColumn pY;
    public TableColumn pZ;
    public TableColumn mSpeed;
    public TableColumn wDir;
    public TableColumn wSpeed;
    public TableColumn endX;
    public TableColumn endY;
    public TableColumn endZ;
    public TableColumn endSpot;

    public Stage stage;

//    private ObservableList<FireStat> fireStats;

    private ObservableList<FireStat>[] fireStats = new ObservableList[2];

    public FireStatsController(ObservableList<FireStat> fireStats) {
        this.fireStats[0] = fireStats;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        pAzimuth.setCellValueFactory(new PropertyValueFactory<>("missileAzimuth"));
        pElevation.setCellValueFactory(new PropertyValueFactory<>("missileElevation"));
        pX.setCellValueFactory(new PropertyValueFactory<>("startX"));
        pY.setCellValueFactory(new PropertyValueFactory<>("startY"));
        pZ.setCellValueFactory(new PropertyValueFactory<>("startZ"));
        mSpeed.setCellValueFactory(new PropertyValueFactory<>("missileSpeed"));
        wDir.setCellValueFactory(new PropertyValueFactory<>("windDirection"));
        wSpeed.setCellValueFactory(new PropertyValueFactory<>("windSpeed"));
        endX.setCellValueFactory(new PropertyValueFactory<>("collidingX"));
        endY.setCellValueFactory(new PropertyValueFactory<>("collidingY"));
        endZ.setCellValueFactory(new PropertyValueFactory<>("collidingZ"));
        endSpot.setCellValueFactory(new PropertyValueFactory<>("collideSpot"));

        table.setItems(fireStats[0]);
    }

    public void openNew(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fireStats.fxml"));
            fxmlLoader.setControllerFactory(param -> {
                try {
                    return param.getConstructor(ObservableList.class).newInstance(fireStats[0]);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            Parent root1 = fxmlLoader.load();
            //Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Historie strelby");
            stage.setScene(new Scene(root1));

            stage.show();

            //stage.setMinWidth(stage.getWidth());
            stage.setMaxWidth(stage.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFile(ActionEvent actionEvent) {

    }

    public void saveFile(ActionEvent actionEvent) {

    }

    public void close(ActionEvent actionEvent) {
        ((Stage)table.getScene().getWindow()).close();
    }
}
