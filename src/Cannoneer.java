import com.sun.deploy.util.FXLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
        data = new Data("terrain257x257.ter");
        //aspectRatio = Math.data.getMapWidth(), data.getMapHeight();

        if (!data.dataConsistent())
            System.out.println("Data nejsou konzistentní!");
        else
            launch(args);

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

        double width = data.getMapWidth();
        double height = data.getMapHeight();

        Scene scene = new Scene(parent, width, height);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(scene.getWidth() + 20);
        primaryStage.setMinHeight(scene.getHeight() + 40);
        primaryStage.show();
    }
}
