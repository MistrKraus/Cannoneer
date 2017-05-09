import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * Created by kraus on 02.05.2017.
 */
public class MyGraph implements IDrawable {

    double maxWidth;
    double maxHeight;
    double deltaX;
    double deltaY;
    double posX;
    double posY;

    double[] distances;

    private boolean chyba;

    private final static int BORDER_SPACING = 10;

    /**
     * konstruktor
     *
     * @param elevation uhel natoceni hlavne
     * @param acceleration rychlost strely
     * @param maxWidth maximalni sirka vykreslovaneho grafu
     * @param maxHeight maximalni vyska vykreslovaneho grafu
     * @param posX pocatecni x-ove souradnice (v pixelech)
     * @param posY pocatecni y-ove souradnice (v pixelech)
     */
    public MyGraph(double elevation, double acceleration, double maxWidth, double maxHeight, double posX, double posY) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.posY = posY;
        this.posX = posX;

        if (acceleration < 2) {
            chyba = true;
            acceleration = 2;
        }

        if (elevation < 0) {
            chyba = true;
            elevation = 0;
        }

        setDistances(elevation, (int) Math.ceil(acceleration));

//        if (chyba)
//            return;

        this.deltaX = (maxWidth - 2 * BORDER_SPACING) / distances.length;
        this.deltaY = (maxHeight - 2 * BORDER_SPACING) / distances[distances.length - 1];
    }


    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        g.setStroke(Color.BLACK);

        g.strokeLine(posX + BORDER_SPACING, posY + BORDER_SPACING, posX + BORDER_SPACING, maxHeight - BORDER_SPACING);
        g.strokeLine(posX + BORDER_SPACING, maxHeight - BORDER_SPACING, maxWidth - BORDER_SPACING, maxHeight - BORDER_SPACING);

        g.setStroke(Color.ORANGE);

//        if (chyba)
//            return;

        for (int i = 0; i < distances.length - 1; i++)
            g.strokeLine((i * deltaX) + BORDER_SPACING, maxHeight - BORDER_SPACING - (distances[i] * deltaY),
                    ((i + 1) * deltaX) + BORDER_SPACING, maxHeight - BORDER_SPACING - (distances[i + 1] * deltaY));
    }

    @Override
    public void update(World world) {
        this.maxWidth = world.getGraphics().getCanvas().getWidth();
        this.maxHeight = world.getGraphics().getCanvas().getHeight() / 2;

//        if (chyba)
//            return;

        this.deltaX = (maxWidth - 2 * BORDER_SPACING) / distances.length;
        this.deltaY = (maxHeight - 2 * BORDER_SPACING) / distances[distances.length - 1];
    }

    private void setDistances(double elevation, int acceleration) {
        distances = new double[acceleration];

        Point coords;
        Point direction;

        //System.out.println("------------------");

        for (int i = 0; i < acceleration; i++) {
            coords = new Point(0,0,0);
            direction = (new Point(Math.cos(Math.PI * elevation / 180), Math.sin(Math.PI * elevation / 180), 0)).mul(i);
            while (coords.getY() >= 0) {
                coords = coords.copy().add((direction.copy()).mul(0.01));

                Point temp = new Point(direction.copy().mul(-1));

                direction = direction.copy().addY(/*-1 * 0.01 * 10*/-0.1).add(temp.mul(0.05 * 0.01));

                //System.out.println();
            }

            distances[i] = coords.getX();
            //System.out.println(coords.getX());
        }
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidthX() {
        return maxWidth;
    }

    @Override
    public double getWidthY() {
        return maxHeight;
    }

    public boolean getChyba() {
        return chyba;
    }
}
