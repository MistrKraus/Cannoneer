import com.sun.deploy.util.FXLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by kraus on 10.03.2017.
 */
public class Cannoneer extends Application {

    static Data data;
    static double aspectRatio;

    public static void main(String[] args) throws IOException {
        loadData(args);

        //aspectRatio = Math.data.getMapWidth(), data.getMapHeight();

        if (!data.dataConsistent())
            System.out.println("Načtená data jsou chybná.");
        else
            launch(args);
    }

    private static void loadData(String[] args) {
        if (args.length == 0)
            data = new Data("terrain257x257.ter");

        if (args.length == 1)
            data = new Data(args[0]);
    }

//    public static void gameloop(Data data) {
//        Player player = new Player(
//                new Point(
//                    data.getShooterXm(), data.getShooterYm(),
//                    data.getTerrainZ()[data.getShooterX()][data.getShooterY()]));
//
//        Target target = new Target(new Point(
//                    data.getTargetXm(), data.getTargetYm(),
//                    data.getTerrainZ()[data.getTargetX()][data.getShooterY()]));
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Petr Kraus / A16B0065P");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

        // mam vlastni dependenci injection (zdroj: budouci bakalar)
        loader.setControllerFactory(param -> {
            try {
                return param.getConstructor(Data.class).newInstance(data);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        });
        Parent parent = loader.load();

        double width = data.getMapWidth() + 20;
        double height = data.getMapHeight() + 40;

        Scene scene = new Scene(parent, width, height);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(scene.getWidth() + 20);
        primaryStage.setMinHeight(scene.getHeight() + 40);
        primaryStage.show();
    }

    public void paint(GraphicsContext g) {
        g.setFill(Color.GRAY);




    }
}
