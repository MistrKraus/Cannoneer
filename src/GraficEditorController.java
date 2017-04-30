import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kraus on 26.03.2017.
 */
public class GraficEditorController implements Initializable {

    public TextField editMinHeight;
    public TextField editMaxHeight;
    public Canvas canvas;
    public Stage stage;

    private GraphicsContext g;

    private ImageView mapImg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editMinHeight.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 0)
                    return 0.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        editMaxHeight.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 0)
                    return 0.0;

                if (x == Double.parseDouble(editMinHeight.getText()))
                    x = Double.parseDouble(editMinHeight.getText());

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));

        g = canvas.getGraphicsContext2D();
    }

    public void drawMap() {
        if (mapImg != null) {
            g.drawImage(mapImg.getImage(), 0, 0);
            return;
        }

        g.setFill(Color.BLACK);
        g.fill();
    }

    public void setMapImg(ImageView mapImg) {
        this.mapImg = mapImg;
    }
}
