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

    private final static int MAX_SPEED = 25;

    public Wind() {
        this(random.nextInt(360), random.nextInt(MAX_SPEED));
    }

    public Wind(double azimuth, double speed) {
        coordinates = new Point(0,0,0);
        azimuthP = new Point(0,0,0);

        this.azimuth = azimuth;
        this.speed = speed;
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        Affine affine = g.getTransform();

        double alpha = g.getGlobalAlpha();
        g.setGlobalAlpha(0.5);
        g.translate(50, 100);
        g.rotate(azimuth);

        double[] stumpPointsX = new double[] {
                -speed / 2, speed / 2 - speed / 5, speed / 10, speed / 2, speed / 10, speed / 2 - speed / 5
        };

        double[] stumpPointsY = new double[] {
                0, -speed / 13, -speed / 3, 0, speed / 3, speed / 13
        };

        for (int i = 0; i < stumpPointsX.length; i++)
            stumpPointsX[i] *= 5;//scaleMperPixelX;

        for (int i = 0; i < stumpPointsY.length; i++)
            stumpPointsY[i] *= 5;//scaleMperPixelY;


        g.setFill(Color.LIGHTBLUE);
        g.fillPolygon(stumpPointsX, stumpPointsY, stumpPointsX.length);

        g.setTransform(affine);
        g.setGlobalAlpha(alpha);
    }

    @Override
    public void update(World world) {
        if (speedDuration-- < 1) {
            speed += (random.nextInt(MAX_SPEED / 2) - MAX_SPEED / 4);
            //speed = MAX_SPEED;

            if (speed < 0)
                speed = 0;

            if (speed > MAX_SPEED)
                speed = MAX_SPEED;

            speedDuration = random.nextInt(100) + 20;
            //speedDuration = 100000;

            if (speed != 0)
                speedDuration /= Math.abs(speed) / 2;
            else
                speedDuration /= 4;
        }

        if (azimuthDuration-- < 1) {
            azimuth += random.nextInt(40) - 20;

            if (azimuth < 0)
                azimuth += 360;

            azimuth %= 360;

//            System.out.println((Math.PI * azimuth) / 180);

            azimuthDuration = random.nextInt(150) + 20;
            //azimuthDuration = 30;
        }

        azimuthP = new Point(Math.cos((Math.PI * azimuth) / 180) * speed, Math.sin((Math.PI * azimuth) / 180) * speed, 0);

        coordinates = azimuthP.copy();
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getSpeed() {
        return speed;
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
