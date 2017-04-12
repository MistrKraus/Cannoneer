import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import javax.xml.bind.Marshaller;
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
    public MenuBar menu;
    public VBox vBox2;
    @FXML
    private TextField azimuthTF;
    @FXML
    private TextField elevationTF;
    @FXML
    private TextField speedTF;
    @FXML
    private Canvas canvas;


    private World world;
    private double defaultStageWidth;
    private double defaultStageHeight;
    private double ratio;
    //private double stageAspectRatio;

    private final Data data;
    private final Stage stage;

    //private static final double DEFAULT_CAVANS_HEIGHT = 257;
    private static final double DEFAULT_CAVANS_WIDTH = 270;

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
        //ratio = data.getMap().getMapWidthM() / data.getMap().getMapHeightM();
        ratio = data.getMap().getMapHeightM() / data.getMap().getMapWidthM();

//        canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio);
//        canvas.setHeight(DEFAULT_CAVANS_HEIGHT);
        canvas.setWidth(DEFAULT_CAVANS_WIDTH);
        canvas.setHeight(DEFAULT_CAVANS_WIDTH * ratio);

//        canvas.minWidth(DEFAULT_CAVANS_HEIGHT * ratio);
//        canvas.minHeight(DEFAULT_CAVANS_HEIGHT);
        canvas.minWidth(canvas.getWidth());
        canvas.minHeight(canvas.getHeight());

//        stage.setWidth(DEFAULT_CAVANS_HEIGHT * ratio + 110);
//        stage.setHeight(DEFAULT_CAVANS_HEIGHT + menu.getHeight() + 115);
            stage.setWidth(DEFAULT_CAVANS_WIDTH + 110);
        stage.setHeight(DEFAULT_CAVANS_WIDTH * ratio + menu.getHeight() + 115);

        stage.setMinWidth(stage.getWidth() + 20);
        stage.setMinHeight(stage.getHeight() + 40);

        defaultStageWidth = stage.getWidth();
        defaultStageHeight = stage.getHeight();

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = defaultStageWidth - newValue.doubleValue();
            //double height = width * (1 / ratio);
            double height = width * ratio;

//            canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio - width);
//            canvas.setHeight(DEFAULT_CAVANS_HEIGHT - height);
            canvas.setWidth(DEFAULT_CAVANS_WIDTH - width);
            canvas.setHeight(DEFAULT_CAVANS_WIDTH * ratio - width);

            world.update();
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            //double height = DEFAULT_CAVANS_HEIGHT + 20 - newValue.doubleValue();
            double height = defaultStageHeight - newValue.doubleValue();
            //double width = height * ratio;
            double width = height * (1 / ratio);

//            canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio - width);
//            canvas.setHeight(DEFAULT_CAVANS_HEIGHT - height);
            canvas.setWidth(DEFAULT_CAVANS_WIDTH - width);
            canvas.setHeight(DEFAULT_CAVANS_WIDTH * ratio - width);

            world.update();
        });

        stage.setOnShown(event -> {
            //System.out.println("showing");
            world.initialGraphis();
        });

        azimuthTF.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                System.out.println("FROM");
                double x = Double.parseDouble(string);

                if (x < -90)
                    return -90.0;

                if (x > 180)
                    return 180.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                System.out.println("TO");
                if (object == null)
                    return "0";

                return object.toString();
            }
        }));

        elevationTF.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                System.out.println("FROM");
                double x = Double.parseDouble(string);

                if (x > 90)
                    return 90.0;

                if (x < -90)
                    return -90.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                System.out.println("TO");
                if (object == null)
                    return "0";

                return object.toString();
            }
        }));

        speedTF.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                System.out.println("FROM");
                double x = Double.parseDouble(string);

                if (x < 0)
                    return 0.0;

                if (x > 1000)
                    return 1000.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                System.out.println("TO");
                if (object == null)
                    return "0";

                return object.toString();
            }
        }));

        GraphicsContext context = canvas.getGraphicsContext2D();

        context.setFill(Color.LIGHTGREY);
        context.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        context.translate(canvas.getLayoutX(), canvas.getLayoutY());

        world = new World(context, data);
        world.addPlayer(new Player(data.getShooterXm(), data.getShooterYm(), data.getMap().getTerrain()[data.getShooterX()][data.getShooterY()]));
        world.addTarget(new Target(data.getTargetXm(), data.getTargetYm(), data.getMap().getTerrain()[data.getTargetX()][data.getTargetY()]));

        //System.out.println(world.getTarget().toString());

        distanceLbl.setText(String.format("%.2f m",world.getPlayer().getCoordinates().getPointsDistance(world.getTarget().getCoordinates())));
        shooterZLbl.setText(String.format("%.2f m n. m.",world.getPlayer().getZ()));
        targetZLbl.setText(String.format("%.2f m n. m.",world.getTarget().getZ()));

        world.start();
    }

    private String ConvertToDouble(String s) {

        return s;
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

        double azimuth = Double.parseDouble(azimuthTF.getText());
        double evelation = Double.parseDouble(elevationTF.getText());
        double speed = Double.parseDouble(speedTF.getText());// * 1000;



        world.addMissile(world.getPlayer().fire(azimuth, evelation, speed));
        //world.start();
    }

    /**
     * Otevře okno s editorem map
     *
     * @param actionEvent
     */
    public void openEditor(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editor.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Editor");
            stage.setScene(new Scene(root1));

            stage.show();

            stage.setMinWidth(stage.getWidth() + 10);
            stage.setMinHeight(stage.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFireStats(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fireStats.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Historie strelby");
            stage.setScene(new Scene(root1));

            stage.show();

            stage.setMinWidth(stage.getWidth());
            stage.setMaxWidth(stage.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
