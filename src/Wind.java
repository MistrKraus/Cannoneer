import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by kraus on 24.03.2017.
 */
public class Wind implements IDrawable {
    private Point coordinates;
    private Point azimuthP;

    private double azimuth;
    private double speed;

    private int speedDuration = 0;
    private int azimuthDuration = 0;

    private static Random random = new Random();

    private final int MAX_SPEED = 18;

    public Wind() {
        coordinates = new Point(0,0,0);
        azimuthP = new Point(0,0,0);

        azimuth = random.nextInt(360);
        speed = random.nextInt(18);
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        Affine affine = g.getTransform();


        g.translate(50, 50);
        g.rotate(azimuth);

        double[] stumpPointsX = new double[] {
                -speed / 2, speed / 2 - speed / 5, speed / 10, speed / 2, speed / 10, speed / 2 - speed / 5
        };

        double[] stumpPointsY = new double[] {
                0, -speed / 13, -speed / 3, 0, speed / 3, speed / 13
        };

        for (int i = 0; i < stumpPointsX.length; i++)
            stumpPointsX[i] *= 10;//scaleMperPixelX;

        for (int i = 0; i < stumpPointsY.length; i++)
            stumpPointsY[i] *= 10;//scaleMperPixelY;


        g.setFill(Color.BLACK);
        g.fillPolygon(stumpPointsX, stumpPointsY, stumpPointsX.length);

        g.setTransform(affine);
    }

    @Override
    public void update(World world) {
        if (speedDuration-- < 1) {
            speed += random.nextInt(MAX_SPEED / 2) - MAX_SPEED / 4;

            if (speed < 0)
                speed = 0;

            if (speed > MAX_SPEED)
                speed = MAX_SPEED;

            speedDuration = random.nextInt(100) + 20;
            //speedDuration = 100000;

            if (speed != 0)
                speedDuration /= Math.abs(speed);

        } else if (azimuthDuration-- < 1) {
            azimuth += random.nextInt(40) - 20;

            azimuthP = new Point(Math.cos(azimuth), Math.sin(azimuth), 0);

            azimuthDuration = random.nextInt(150) + 20;
            //azimuthDuration = 30;
        }
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidthX() {
        return 0;
    }

    @Override
    public double getWidthY() {
        return 0;
    }
}