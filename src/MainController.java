import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by kraus on 13.03.2017.
 */
public class MainController implements Initializable {

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

        GraphicsContext context = canvas.getGraphicsContext2D();

        context.setFill(Color.GRAY);
        context.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    }

    public void handleBtn(ActionEvent actionEvent) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.next());
//        FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
//        try {
//            Parent parent = loader.load();
//            loader.getController();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
