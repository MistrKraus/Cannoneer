import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kraus on 26.03.2017.
 */
public class EditorController implements Initializable {

    public BorderPane editorFXML;
    // zajistuje nainjektovani grafickeho kontroleru sem
    public GraficEditorController editorFXMLController;

    public TextField mapWidth;
    public TextField mapHeight;
    public TextField deltaX;
    public TextField deltaY;
    public TextField defauktHeight;
    public TextField playerX;
    public TextField playerY;
    public TextField targetX;
    public TextField targetY;

    public Stage stage;

    public void openNew(ActionEvent actionEvent) {
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

    public void openFile(ActionEvent actionEvent) {
        FileChooser fChooser = new FileChooser();

        fChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fChooser.showOpenDialog(stage);

        Image mapImg = new Image(file.toURI().toString());
        ImageView iv = new ImageView(mapImg);

        editorFXMLController.setMapImg(iv);
        editorFXMLController.drawMap();
    }

    public void saveFile(ActionEvent actionEvent) {

    }

    public void close(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapWidth.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        mapHeight.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        deltaX.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        deltaY.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        defauktHeight.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        playerX.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        playerY.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        targetX.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
        targetY.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1)
                    return 1.0;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return "";

                return object.toString();
            }
        }));
    }
}
