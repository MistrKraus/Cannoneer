import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Created by kraus on 12.05.2017.
 */
//TODO dodělat vlastní komponentu
public class WindGraphics extends Canvas {

    private double height;
    private double width;
    private double circleDistance;

    private static final int circleCount = 4;

    private static GraphicsContext g;

    //private static final String IMG_PATH = "images/wind.png";

    public WindGraphics() {
        super();

        g = getGraphicsContext2D();
        g.setStroke(Color.BLACK);
    }

    public void update(double degree, double acceleration) {
        height = getHeight() - 4;
        width = getWidth() - 4;

        circleDistance = ((height + width) / 2) / 4;

        g.setFill(Color.WHITE);
        g.fillOval(0, 0, getWidth(), getHeight());


        Affine affine = g.getTransform();
        g.translate(2, 2);

        g.setFill(Color.GRAY);
        g.setEffect(new GaussianBlur());
        g.fillOval(7, 7, width -14, height - 14);
        g.setEffect(null);

        g.strokeLine(0, height / 2, width, height / 2);
        g.strokeLine(width / 2, 0, width / 2, height);

        g.translate(width / 2, height / 2);

        for (int i = 1; i <= circleCount; i++) {
            double size = i * circleDistance;
            g.strokeOval(-size / 2, -size / 2, size, size);
        }

        g.setFill(Color.LIGHTBLUE);
        g.rotate(degree);

        double[] stumpPointsX = new double[] {
                -acceleration / 2, acceleration / 2 - acceleration / 5, acceleration / 10,
                acceleration / 2, acceleration / 10, acceleration / 2 - acceleration / 5
        };

        double[] stumpPointsY = new double[] {
                0, -acceleration / 13, -acceleration / 3, 0, acceleration / 3, acceleration / 13
        };

        for (int i = 0; i < stumpPointsX.length; i++)
            stumpPointsX[i] *= 3.2;
        for (int i = 0; i < stumpPointsY.length; i++)
            stumpPointsY[i] *= 3.2;

        g.fillPolygon(stumpPointsX, stumpPointsY, stumpPointsX.length);

        g.setTransform(affine);
    }
}
