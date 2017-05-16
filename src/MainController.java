import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import javax.swing.*;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
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
    //public Label paddingLbl;
    public MenuBar menu;
    public VBox hBox1;
    public VBox vBox2;
    public Button btnVisual;
    public Button btnFire;
    //public Button btnAddTarget;
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

    private static final String FIRE_BTN_TXT = "FIRE";

    /**
     * kontruktor
     *
     * @param data nactena data ze souboru
     * @param stage
     */
    public MainController(Data data, Stage stage) {
        //Locale d = new Locale("cs", "CZ");
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

        //stage.setOnCloseRequest(event -> stage.close());

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = defaultStageWidth - newValue.doubleValue();
            //double maxHeight = maxWidth * (1 / ratio);
            double height = width * ratio;

//            canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio - maxWidth);
//            canvas.setHeight(DEFAULT_CAVANS_HEIGHT - maxHeight);
            canvas.setWidth(DEFAULT_CAVANS_WIDTH - width);
            canvas.setHeight(DEFAULT_CAVANS_WIDTH * ratio - width);

            world.update();
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            //double maxHeight = DEFAULT_CAVANS_HEIGHT + 20 - newValue.doubleValue();
            double height = defaultStageHeight - newValue.doubleValue();
            //double maxWidth = maxHeight * ratio;
            double width = height * (1 / ratio);

//            canvas.setWidth(DEFAULT_CAVANS_HEIGHT * ratio - maxWidth);
//            canvas.setHeight(DEFAULT_CAVANS_HEIGHT - maxHeight);
            canvas.setWidth(DEFAULT_CAVANS_WIDTH - width);
            canvas.setHeight(DEFAULT_CAVANS_WIDTH * ratio - width);

            world.update();
        });

        stage.setOnShown(event -> {
            azimuthTF.requestFocus();
            world.initialGraphis();
        });

//        azimuthTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!newValue)
//                    world.getPlayer().setAzimuth(Double.parseDouble(azimuthTF.getText()));
//            }
//        });

        azimuthTF.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                //System.out.println("FROM");
                double x = Double.parseDouble(string);

                if (x < -90)
                    return -90.0;

                if (x > 270)
                    return 270.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                //System.out.println("TO");
                if (object == null)
                    return "0.0";

                world.getPlayer().setAzimuth(object);

                return object.toString();
            }
        }));

        elevationTF.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                //System.out.println("FROM");
                double x = Double.parseDouble(string);

                if (x > 90)
                    return 90.0;

                if (x < -90)
                    return -90.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                //System.out.println("TO");
                if (object == null)
                    return "0.0";

                return object.toString();
            }
        }));

        speedTF.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                //System.out.println("FROM");
                double x = Double.parseDouble(string);

                if (x < 0)
                    return 0.0;

                if (x > 500)
                    return 500.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                //System.out.println("TO");
                if (object == null)
                    return "0.0";

                return object.toString();
            }
        }));

        GraphicsContext context = canvas.getGraphicsContext2D();

        WindGraphics wg = new WindGraphics();
        double size = 83;
        wg.setWidth(size);
        wg.setHeight(size);

        Button addTargetBtn = new Button("Pridat Cil");
        addTargetBtn.setMinHeight(25);
        addTargetBtn.setMaxWidth(83);
        addTargetBtn.setTextAlignment(TextAlignment.CENTER);
        addTargetBtn.setOnAction(event -> addNewTarget(event));

        Label lbl = new Label();
        lbl.setMinHeight(10);

        hBox1.getChildren().addAll(wg, lbl, addTargetBtn);

        context.setFill(Color.LIGHTGREY);
        context.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        context.translate(canvas.getLayoutX(), canvas.getLayoutY());

        world = new World(context, data, wg);
        world.addPlayer(new Player(data.getShooterXm(), data.getShooterYm(), data.getMap().getTerrain()[data.getShooterX()][data.getShooterY()]));
        world.addTarget(new Target(data.getTargetXm(), data.getTargetYm(), data.getMap().getTerrain()[data.getTargetX()][data.getTargetY()]));

        //System.out.println(world.getTarget().toString());

        distanceLbl.setText(String.format("%.2f m",world.getPlayer().getCoordinates().getPointsDistance(world.getTarget().getCoordinates())));
        shooterZLbl.setText(String.format("%.2f m n. m.",world.getPlayer().getZ()));
        targetZLbl.setText(String.format("%.2f m n. m.",world.getTarget().getZ()));

        world.start();
    }

    /**
     * Nacte data potrebna pro strelbu
     * Pokud je hra spustena vypise vzdalenost mezi strelcem a cilem zaroven s jejich nadmorskymi vyskami a zajisti vystrel hrace
     *
     * @param visual
     */
    private void manageFireParam(boolean visual) throws FileNotFoundException, UnsupportedEncodingException {
        if (!world.isRunning())
            return;

        distanceLbl.setText(String.format("%.2f m", world.getPlayer().getCoordinates().getPointsDistance(world.getTarget().getCoordinates())));
        shooterZLbl.setText(String.format("%.2f m", world.getPlayer().getZ()));
        targetZLbl.setText(String.format("%.2f m", world.getTarget().getZ()));

        double azimuth = Double.parseDouble(azimuthTF.getText());
        double evelation = Double.parseDouble(elevationTF.getText());
        double speed = Double.parseDouble(speedTF.getText());// * 1000;

        if (visual) {
            world.stopVisualize();
            world.update();

            world.visualize(azimuth, evelation, speed);

            return;
        }
        world.addMissile(world.getPlayer().fire(azimuth, evelation, speed));
    }

    /**
     * Reakce na stisk tlačítka.
     *
     * @param actionEvent data o stiku tlacitka
     */
    public void handleBtnFire(ActionEvent actionEvent) throws FileNotFoundException, UnsupportedEncodingException {
        if (!world.isVisuializing()) {
            manageFireParam(false);
            return;
        }

        world.stopVisualize();
        btnFire.setText(FIRE_BTN_TXT);
    }

    /**
     * Reakce na tlacitko "Vizualizuj"
     *
     * @param actionEvent data o stiku tlacitka
     */
    public void handleBtnVizualzuj(ActionEvent actionEvent) throws FileNotFoundException, UnsupportedEncodingException {
        manageFireParam(true);

        btnFire.setText("Navrat");
    }

    /**
     * Otevře okno s editorem map
     *
     * @param actionEvent
     */
    public void openEditor(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editor.fxml"));
            fxmlLoader.setControllerFactory(param -> {
                try {
                    return param.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            Parent root1 = fxmlLoader.load();
            //Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Editor");
            stage.setScene(new Scene(root1));

            stage.show();

            stage.setMinWidth(stage.getWidth() + 10);
            stage.setMinHeight(stage.getHeight());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Implementace editoru neni dokoncena!");
            alert.setContentText("Funguje pouze nacteni obrazku mapy a znema jeji velikosti.");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFireStats(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fireStats.fxml"));
            fxmlLoader.setControllerFactory(param -> {
                try {
                    return param.getConstructor(ObservableList.class).newInstance(world.getFireStats());
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

    public void openNew(ActionEvent actionEvent) {
        Stage primaryStage = new Stage();
        stage.setTitle("Petr Kraus / A16B0065P");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

        // dependence injection
        loader.setControllerFactory(param -> {
            try {
                return param.getConstructor(Data.class, Stage.class).newInstance(data, primaryStage);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        });
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(parent, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void close(ActionEvent actionEvent) {
        stage.close();
    }

    public void addNewTarget(ActionEvent actionEvent) {
        if(!world.isRunning()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Novy cil nezle pridat!");
            alert.setContentText("Abyste mohli pridat novy cil, musi hra bezet.");
            alert.show();
            return;
        }

        Random r = new Random();
        double x = r.nextInt((int) data.getMap().getMapWidthM());
        double y = r.nextInt((int) data.getMap().getMapHeightM());
        double z = data.getMap().getTerrain()[(int)(x * world.getScaleX())][(int)(y * world.getScaleY())];

        world.addTarget(new Target(x, y, z));
    }

    public void openHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Jak hrat?");
        alert.setContentText("Cilem hry je znicit vsechny cile (modre piktogramy) na mape, " +
                "toho docilite strelbou z tanku (zeleny piktogram tanku).\n\n" +
                "Strelbu spravujete nastavenim nasledujich parametru:\n" +
                " - Azumut (natoceni tanku):\n" +
                "     - Hodnoty: -90\u00B0 az 270\u00B0.\n" +
                "     - -90\u00B0 Jih; 0\u00B0 Vychod; 90\u00B0 Sever; 180\u00B0 Zapad; 270\u00B0 Jih.\n" +
                " - Elevace (zdvih hlavne):\n" +
                "     - Hodnoty: -90\u00B0 az 90\u00B0\n" +
                "     - -90\u00B0 Svisle dolu; 0\u00B0 Rovne; 90\u00B0 Svisle nahoru.\n" +
                " - Rychlost strely:\n" +
                "     - Hodnoty: 0.0 m/s az 500 m/s.\n" +
                " - Tlacitko 'FIRE':\n" +
                "     - Vystrely se zadanymi parametry.\n" +
                " - Tlacitko 'Vizualizuj':\n" +
                "     - Vykresli 2 grafy - zavislost dostrelu na pocatecni rychlosti strely pri zadane elevaci;" +
                "profil terenu pod trajektorii strely se zadanymi parametry a samotna strela.\n" +
                "     - Tlacitko 'FIRE' zmeni text (novy text: 'Navrat') a funkci (nova funkce: zobrazeni prostredi pro strelbu).\n\n" +
                "Nabidka v menu:\n" +
                " - Soubor: Otevreni nove hry, ukonceni aplikace\n" +
                " - Zobrazeni: Otevreni okna s tabulkou historie strelby.\n" +
                " - Moznosti: Otevreni editoru map, vygenerovani noveho cile.\n" +
                "\nPro vice informaci si muzete precist dokumentaci.");
        alert.show();
    }
}
