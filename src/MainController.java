import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kraus on 13.03.2017.
 */
public class MainController implements Initializable {

    @FXML
    private Spinner<Double> azimuthSp;
    @FXML
    private Spinner<Double> elevationSp;
    @FXML
    private Spinner<Double> speedSp;

    private World world;

    private final Data data;

    @FXML
    private Canvas canvas;

    public MainController(Data data) {
        this.data = data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.setWidth(data.getMapWidth());
        canvas.setHeight(data.getMapHeight());

        azimuthSp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-90, 180, 0, 0.5));
        elevationSp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-90, 90, 0, 0.5));
        speedSp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 3, 0.01));

        GraphicsContext context = canvas.getGraphicsContext2D();

        context.setFill(Color.LIGHTGREY);
        context.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        context.translate(canvas.getLayoutX(), canvas.getLayoutY());

        world = new World(context, data);
        world.addPlayer(new Player(data.getShooterXm(), data.getShooterYm(), data.getTerrainZm()[data.getShooterX()][data.getShooterY()]));
        world.addTarget(new Target(data.getTargetXm(), data.getTargetYm(), data.getTerrainZm()[data.getTargetX()][data.getTargetY()]));
        world.update();
    }

    public void handleBtn(ActionEvent actionEvent) {

        System.out.println("Vystrel!\n Azimut: " + azimuthSp.getValue() +
                "\n Elevation: " + elevationSp.getValue() +
                "\n Speed: " + speedSp.getValue());

        world.addMissile(world.getPlayer().fire(azimuthSp.getValue(), elevationSp.getValue(), speedSp.getValue()));
        world.start();
    }


}
