import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by kraus on 10.03.2017.
 *
 * Hlavni trida spoustejici aplikaci
 */
public class Cannoneer extends Application {

    static Data data;

    /**
     * Zajisti nacteni dat, pokud byla zadana spatne, vypise chybovou hlasku
     * Zajisti vytvoreni okna
     *
     * @param args nazev vstupniho souboru
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        loadData(args);

        //aspectRatio = Math.data.getMapWidth(), data.getMapHeight();

        if (data == null)
            return;

        if (!data.getDataConsistent())
            System.out.println("Načtená data jsou chybná.");
        else
            launch(args);
    }

    /**
     * Pokud byl zadan nazev souboru se vstupnimi daty, nacte jej.
     * Jinak jsou data nactena z vychoziho vstupniho souboru
     *
     * @param args nazev stupniho souboru
     */
    private static void loadData(String[] args) {
        if (args.length == 0)
            data = new Data("src/resources/terrain257x257.ter");
            //data = new Data("src/resources/terrain512x512.ter");
            //data = new Data("src/resources/rovny1metr.ter");
            //data = new Data("src/resources/sikmy45stupnu.ter");

        if (args.length == 1)
            data = new Data("src/resources/" + args[0]);

        //System.out.println(Math.sin(0));
    }

    /**
     * Vyvori okno
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Petr Kraus / A16B0065P");

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
        Parent parent = loader.load();

        Scene scene = new Scene(parent, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
        //System.out.println("Stage vykreslena");
    }

}
