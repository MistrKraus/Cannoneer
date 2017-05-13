import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

/**
 * Created by kraus on 02.05.2017.
 */
public class MyGraph implements IDrawable {

    int maxDistance;
    int maxAcceleration;
    int axisVerticalCount;
    int axisHorizontalCount;
    int axisXstep;
    int axisYstep;

    double maxWidth;
    double maxHeight;
    double deltaX;
    double deltaY;
    double axisDeltaX;
    double axisDeltaY;
    double posX;
    double posY;

    double[] distances;

    private boolean chyba;

    private final static int BORDER_SPACING = 10;
    private final static int HEADLINE_SPACING = 10;
    private final static int TEXT_SPACING_X = 15;
    private final static int TEXT_SPACING_Y = 10;

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

        setDistances(elevation, (int) Math.ceil(acceleration));

        chyba = distances.length == 0;

        if (chyba)
            return;

        maxAcceleration = distances.length;
        maxDistance = (int)distances[distances.length - 1];

        // konstanty pro osu x
        int temp = 0;
        while (maxAcceleration > (temp * 100))
            temp++;

        axisVerticalCount = maxAcceleration / (temp * 10);
        axisXstep = temp * 10;

//        for (int i = 0; i <= 5; i++) {
//            if (maxAcceleration <= (i * 100)) {
//                axisVerticalCount = maxAcceleration / (i * 10);
//                axisXstep = i * 10;
//                break;
//            }
//        }

        //System.out.println(maxDistance);

        // konstanty pro osu y
        temp = 0;

        while (maxDistance > (temp * 200))
            temp++;

        axisHorizontalCount = maxDistance / (temp * 20);
        axisYstep = temp * 20;

//        for (int i = 0; i < 10; i++) {
//            if (maxDistance <= (i * 200)) {
//                axisHorizontalCount = maxDistance / (i * 20);
//                axisYstep = i * 20;
//                break;
//            }
//        }

        this.deltaX = (maxWidth - 2 * (BORDER_SPACING + TEXT_SPACING_X)) / distances.length;
        this.deltaY = (maxHeight - 2 * (BORDER_SPACING + TEXT_SPACING_Y)) / distances[distances.length - 1];
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        g.translate(posX, posY);

        Affine t = g.getTransform();

        g = setAxis(g);

        g.setTransform(t);

        g.setStroke(Color.ORANGE);

//        if (chyba)
//            return;

        g.translate(BORDER_SPACING + 2 * TEXT_SPACING_X, maxHeight - BORDER_SPACING - TEXT_SPACING_Y);

        for (int i = 0; i < distances.length - 1; i++)
            g.strokeLine((i * deltaX), - (distances[i] * deltaY), ((i + 1) * deltaX), - (distances[i + 1] * deltaY));

        g.setTransform(t);
    }

    private GraphicsContext setAxis(GraphicsContext g) {
        g.setStroke(Color.GRAY);

        g.setFill(Color.BLACK);
        g.setTextAlign(TextAlignment.CENTER);
        g.setFont(Font.font("Courier New", FontWeight.BOLD, 10));

        Affine t = g.getTransform();

        // popisky os
        g.translate(BORDER_SPACING, maxHeight / 2);
        g.rotate(-90);

        // osa y
        g.fillText("Dostrel [m]", 0, 0);

        g.setTransform(t);

        g.translate(maxWidth / 2, maxHeight);
        // osa x
        g.fillText("Rychlost strely [m/s]", 0, 0);

        g.setTransform(t);

        // vodorovne osy
        g.translate(BORDER_SPACING + TEXT_SPACING_X, 0);

        double temp = maxHeight - BORDER_SPACING;
        for (int i = 1; i < axisVerticalCount; i++) {
            g.strokeLine( TEXT_SPACING_X + i * axisDeltaX, BORDER_SPACING,
                    TEXT_SPACING_X + i * axisDeltaX, temp - TEXT_SPACING_Y);
            g.fillText(String.valueOf(i * axisXstep), TEXT_SPACING_X + i * axisDeltaX, temp);
        }

        //g.setTransform(t);

        // horizontalni osy
        for (int i = 1; i < axisHorizontalCount; i++) {
            g.strokeLine(TEXT_SPACING_X, temp - TEXT_SPACING_Y -(i * axisDeltaY),
                    maxWidth - (2 * BORDER_SPACING + TEXT_SPACING_X), temp - TEXT_SPACING_Y -(i * axisDeltaY));
            g.fillText(Integer.toString(i * axisYstep), 0,temp - TEXT_SPACING_Y -(i * axisDeltaY));
        }

        //osa Y max hodnota
        g.fillText(Integer.toString((int)distances[distances.length - 1]), 0, 2 * BORDER_SPACING);
        //osa X max hodnota
        g.fillText(Integer.toString(distances.length), maxWidth - (2 * BORDER_SPACING + TEXT_SPACING_X),
                maxHeight - HEADLINE_SPACING);


        g.setStroke(Color.BLACK);
        // Osa y
        g.strokeLine(TEXT_SPACING_X, BORDER_SPACING,
                TEXT_SPACING_X, temp - TEXT_SPACING_Y);
        // Osa x
        g.strokeLine(TEXT_SPACING_X, temp - TEXT_SPACING_Y,
                maxWidth - (2 * BORDER_SPACING + TEXT_SPACING_X), temp - TEXT_SPACING_Y);

        return g;
    }

    @Override
    public void update(World world) {
        this.maxWidth = world.getGraphics().getCanvas().getWidth();
        this.maxHeight = world.getGraphics().getCanvas().getHeight() / 2;

        if (chyba)
            return;

        this.deltaX = (maxWidth - 2 * (BORDER_SPACING  + TEXT_SPACING_X)) / maxAcceleration;
        this.deltaY = (maxHeight - 2 * (BORDER_SPACING + TEXT_SPACING_Y)) / maxDistance;

        this.axisDeltaX = (maxWidth - 2 * (BORDER_SPACING + TEXT_SPACING_X)) / axisVerticalCount;
        this.axisDeltaY = (maxHeight - 2 * (BORDER_SPACING + TEXT_SPACING_Y)) / axisHorizontalCount;
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
