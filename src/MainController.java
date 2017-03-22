import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kraus on 13.03.2017.
 *
 * obstarava komunikaci s grafickym rozhranim
 */
public class MainController implements Initializable {

    @FXML
    public Pane container;
    public Label distanceLbl;
    public Label shooterZLbl;
    public Label targetZLbl;
    @FXML
    private Spinner<Double> azimuthSp;
    @FXML
    private Spinner<Double> elevationSp;
    @FXML
    private Spinner<Double> speedSp;
    @FXML
    private Canvas canvas;


    private World world;
    private double defaultStageWidth;
    private double defaultStageHeight;
    private double ratio;
    private double stageAspectRatio;

    private final Data data;
    private final Stage stage;

    private static final double DEFAULT_CAVANS_HEIGHT = 257;

    /**
     * kontruktor
     *
     * @param data nactena data ze souboru
     * @param stage
     */
    public MainController(Data data, Stage stage) {
        this.data = data;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ratio = data.getMapWidthM() / data.getMapHeightM();

        canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio);
        canvas.setHeight(DEFAULT_CAVANS_HEIGHT);

        canvas.minWidth(DEFAULT_CAVANS_HEIGHT * ratio);
        canvas.minHeight(DEFAULT_CAVANS_HEIGHT);

        stage.setWidth(DEFAULT_CAVANS_HEIGHT * ratio + 233);
        stage.setHeight(DEFAULT_CAVANS_HEIGHT + 20);

        stage.setMinWidth(stage.getWidth() + 20);
        stage.setMinHeight(stage.getHeight() + 40);

        defaultStageWidth = stage.getWidth();
        defaultStageHeight = stage.getHeight();

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = defaultStageWidth - newValue.doubleValue();
            double height = width * (1 / ratio);

            canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio - width);
            canvas.setHeight(DEFAULT_CAVANS_HEIGHT - height);

            //stage.setHeight(defaultStageHeight - height);

            world.update();
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            //double height = DEFAULT_CAVANS_HEIGHT + 20 - newValue.doubleValue();
            double height = defaultStageHeight - newValue.doubleValue();
            double width = height * ratio;

            canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio - width);
            canvas.setHeight(DEFAULT_CAVANS_HEIGHT - height);

            //stage.setWidth(defaultStageWidth - width);

            world.update();
        });

        azimuthSp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-180, 180, 0, 0.5));
        elevationSp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-90, 90, 20, 0.5));
        speedSp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 1, 0.01));

        GraphicsContext context = canvas.getGraphicsContext2D();

        context.setFill(Color.LIGHTGREY);
        context.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        context.translate(canvas.getLayoutX(), canvas.getLayoutY());

        world = new World(context, data);
        world.addPlayer(new Player(data.getShooterXm(), data.getShooterYm(), data.getTerrainZm()[data.getShooterX()][data.getShooterY()]));
        world.addTarget(new Target(data.getTargetXm(), data.getTargetYm(), data.getTerrainZm()[data.getTargetX()][data.getTargetY()]));

        System.out.println(world.getTarget().toString());

        distanceLbl.setText(String.format("%.2f m",world.getPlayer().getCoordinates().getPointsDistance(world.getTarget().getCoordinates())));
        shooterZLbl.setText(String.format("%.2f m",world.getPlayer().getZ()));
        targetZLbl.setText(String.format("%.2f m",world.getTarget().getZ()));

        world.start();
    }

    /**
     * Reakce na stisk tlačítka.
     * Pokud je hra spustena vypise vzdalenost mezi strelcem a cilem zaroven s jejich nadmorskymi vyskami a zajisti vystrel hrace
     *
     * @param actionEvent data o stiku tlacitka
     */
    public void handleBtn(ActionEvent actionEvent) {
        if (!world.isRunning())
            return;

        distanceLbl.setText(String.format("%.2f m", world.getPlayer().getCoordinates().getPointsDistance(world.getTarget().getCoordinates())));
        shooterZLbl.setText(String.format("%.2f m", world.getPlayer().getZ()));
        targetZLbl.setText(String.format("%.2f m", world.getTarget().getZ()));

//        System.out.println("Vystrel!\n Azimut: " + azimuthSp.getValue() +
//                "\n Elevation: " + elevationSp.getValue() +
//                "\n Speed: " + speedSp.getValue());


        world.addMissile(world.getPlayer().fire(azimuthSp.getValue(), elevationSp.getValue(), speedSp.getValue()));
        //world.start();
    }
}
