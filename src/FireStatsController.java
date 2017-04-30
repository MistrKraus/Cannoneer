import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private ObservableList<FireStat> fireStats;

    public FireStatsController(ObservableList<FireStat> fireStats) {
        this.fireStats = fireStats;
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

        table.setItems(fireStats);
    }
}
