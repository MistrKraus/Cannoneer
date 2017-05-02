import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kraus on 26.03.2017.
 */
public class EditorController implements Initializable {

    public BorderPane editorFXML;
    // zajistuje nainjektovani grafickeho kontroleru sem
    public GraficEditorController editorFXMLController;
    public Tab tabGraficky;
    public TitledPane mapaTitledPane;
    public TextField mapWidth;
    public TextField mapHeight;

    public TextField deltaX;
    public TextField deltaY;
    public TextField defauktHeight;
    public TextField playerX;
    public TextField playerY;
    public TextField targetX;
    public TextField targetY;
    public Button confirmBtn;

    public Stage stage;

    private MapEditor mapEditor;

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
        try {
            FileChooser fChooser = new FileChooser();

            fChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            File file = fChooser.showOpenDialog(stage);

            Image mapImg = new Image(file.toURI().toString());
            ImageView iv = new ImageView(mapImg);

            if (iv.getImage() == null)
                return;

            editorFXMLController.setMapImg(iv);
            mapEditor.setHeights(iv);
            editorFXMLController.drawMap();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveFile(ActionEvent actionEvent) {

    }

    public void close(ActionEvent actionEvent) {
        ((Stage)(confirmBtn.getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapEditor = new MapEditor(1, 1, 1, 1, 0, 0, 1, 1, 1 ,1);

        mapWidth.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                int x = Integer.parseInt(string);

                if (x < 1)
                    return mapEditor.getMapWidth();

                return x;
            }

            @Override
            public String toString(Integer object) {
                if (object == null)
                    return Integer.toString(mapEditor.getMapWidth());

                return object.toString();
            }
        }));
        mapHeight.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                int x = Integer.parseInt(string);

                if (x < 1)
                    return mapEditor.getMapHeight();

                return x;
            }

            @Override
            public String toString(Integer object) {
                if (object == null)
                    return Integer.toString(mapEditor.getMapHeight());

                return object.toString();
            }
        }));
        deltaX.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1000)
                    return mapEditor.getDeltaXm() * 1000;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return Double.toString(mapEditor.getDeltaXm() * 1000);

                return object.toString();
            }
        }));
        deltaY.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public Double fromString(String string) {
                double x = Double.parseDouble(string);

                if (x < 1000)
                    return mapEditor.getDeltaYm() * 1000;

                return x;
            }

            @Override
            public String toString(Double object) {
                if (object == null)
                    return Double.toString(mapEditor.getDeltaYm() * 1000);

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
                    return "1.0";

                return object.toString();
            }
        }));
        playerX.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                int x = Integer.parseInt(string);

                if (x < 1)
                    return mapEditor.getPlayerX();

                return x;
            }

            @Override
            public String toString(Integer object) {
                if (object == null)
                    return Integer.toString(mapEditor.getPlayerX());

                return object.toString();
            }
        }));
        playerY.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                int x = Integer.parseInt(string);

                if (x < 1)
                    return mapEditor.getPlayerY();

                return x;
            }

            @Override
            public String toString(Integer object) {
                if (object == null)
                    return Integer.toString(mapEditor.getPlayerY());

                return object.toString();
            }
        }));
        targetX.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                int x = Integer.parseInt(string);

                if (x < 1)
                    return mapEditor.getTargetX();

                return x;
            }

            @Override
            public String toString(Integer object) {
                if (object == null)
                    return Integer.toString(mapEditor.getTargetX());

                return object.toString();
            }
        }));
        targetY.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                int x = Integer.parseInt(string);

                if (x < 1)
                    return mapEditor.getTargetY();

                return x;
            }

            @Override
            public String toString(Integer object) {
                if (object == null)
                    return Integer.toString(mapEditor.getTargetY());

                return object.toString();
            }
        }));

        //stage = (Stage) editorFXML.getScene().getWindow();

        editorFXML.widthProperty().addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
        });

        editorFXML.heightProperty().addListener((observable, oldValue, newValue) -> {
            resizeCanvas();
        });

        resizeCanvas();
    }

    //TODO dokoncit zmenu velikosti canvasu v editoru
    private void resizeCanvas() {
        Bounds lblMMCoord = editorFXMLController.lblMM.localToScene(editorFXMLController.lblMM.getBoundsInLocal());
        Bounds lblMinVyskaCoord = editorFXMLController.lblMinVyska.localToScene(editorFXMLController.lblMinVyska.getBoundsInLocal());
        Bounds mapTitledPaneCoord = mapaTitledPane.localToScene(mapaTitledPane.getBoundsInLocal());

        //System.out.println(lblMinVyskaCoord.getMinX() + " " + lblMMCoord.getMaxX());

        double maxWidth = Math.abs(lblMMCoord.getMaxX() - lblMinVyskaCoord.getMinX() - 20);
        //double maxHeight = maxWidth * (1 / ratio);
        double maxHeight = Math.abs((mapTitledPaneCoord.getMinY() + 20) - (lblMinVyskaCoord.getMinY() - 30));


        if (maxHeight >= maxHeight * mapEditor.getScale() && maxHeight * mapEditor.getScale() <= maxWidth) {
            System.out.println(maxWidth + " " + maxHeight);
            editorFXMLController.setCanvasSize(maxHeight, maxHeight * mapEditor.getScale());
            return;
        }

        if (maxWidth >= maxWidth / mapEditor.getScale() && maxWidth / mapEditor.getScale() <= maxHeight) {
            //System.out.println(maxWidth + " " + maxHeight);
            editorFXMLController.setCanvasSize(maxWidth, maxWidth / mapEditor.getScale());
            return;
        }

        System.out.println("Fail");

//        if (maxWidth > maxHeight * mapEditor.getScale())
//            editorFXMLController.setCanvasSize(maxWidth, maxHeight * mapEditor.getScale());
//        else
//            editorFXMLController.setCanvasSize(maxWidth / mapEditor.getScale(), maxHeight);
    }

    public void setOnAction(ActionEvent actionEvent) {
        mapEditor.setMapWidth(Integer.parseInt(mapWidth.getText()));
        mapEditor.setMapHeight(Integer.parseInt(mapHeight.getText()));
        mapEditor.setDeltaXm(Double.parseDouble(deltaX.getText()) * 1000);
        mapEditor.setDeltaYm(Double.parseDouble(deltaY.getText()) * 1000);

        // vychozi vyska???

        mapEditor.setPlayerX(Integer.parseInt(playerX.getText()));
        mapEditor.setPlayerY(Integer.parseInt(playerY.getText()));
        mapEditor.setTargetX(Integer.parseInt(targetX.getText()));
        mapEditor.setTargetY(Integer.parseInt(targetY.getText()));

        resizeCanvas();
    }
}
