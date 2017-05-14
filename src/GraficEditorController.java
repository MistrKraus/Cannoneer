import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    public Label lblMinVyska;
    public Label lblMM;

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
                    return "0.0";

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
                    return "0.0";

                return object.toString();
            }
        }));

        g = canvas.getGraphicsContext2D();

        drawMap();
    }

    public void drawMap() {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (mapImg != null) {
            g.drawImage(mapImg.getImage(), 0, 0);
            g.setGlobalAlpha(0.5);
            g.setFill(Color.WHITE);
            g.fillRect(0,0,g.getCanvas().getWidth(), g.getCanvas().getHeight());
            g.setGlobalAlpha(1);
            g.setFill(Color.BLACK);
            g.setFont(Font.font("INPACT", FontWeight.BOLD, 20));
            g.fillText("Dalsi funkce\nnejsou naimplementovane", g.getCanvas().getWidth() / 2, g.getCanvas().getHeight() / 2);
            return;
        }

        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(new Font("Inpact", 20));
        g.fillText("Nactete soubor obrazku mapy\nve formatu .jpg nebo .png!", (int)(canvas.getWidth() / 2),
                (int)(canvas.getHeight() / 2));
    }

    public void setMapImg(ImageView mapImg) {
        this.mapImg = mapImg;
    }

    public void setCanvasSize(double width, double height) {
        canvas.setWidth(width);
        canvas.setHeight(height);

        //System.out.println(width + " " + height);

        drawMap();
    }
}
