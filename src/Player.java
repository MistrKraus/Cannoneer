import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 11.03.2017.
 *
 * Hrac
 */
public class Player extends Target implements IShooting {

    /**
     * perizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z nadmorska vyska v metech
     */
    public Player (double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param point souradnice v metrech
     */
    public Player (Point point) {
        super(point);
    }

    /**
     * perizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z nadmorska vyska v metech
     * @param hp pocet zivotu
     */
    public Player (double x, double y, double z, int hp) {
        super(x, y, z, hp);
    }

    /**
     * konstruktor
     *
     * @param point souradnice v metrech
     * @param hp pocet zivotu
     */
    public Player(Point point, int hp) {
        super(point, hp);
    }

    /**
     * vystreli
     *
     * @param azimuth smer strely
     * @param elevation zdvih hlavne
     * @param speed rychlot strely
     * @return strela
     */
    @Override
    public Missile fire(double azimuth, double elevation, double speed) {
        return new Missile(coordinates, azimuth, elevation, speed);
    }

    /**
     * vykresli instanci
     *
     * @param g graficky kontext, ktery nakresli instanci
     * @param scaleX hodnota, ktera po vynasobeni x-ove souradnice v metrech urci tuto souradnici v pixelech
     * @param scaleY hodnota, ktera po vynasobeni y-ove souradnice v metrech urci tuto souradnici v pixelech
     */
    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        g.setStroke(Color.BLUE);
        g.setLineWidth(1);
        g.strokeLine(getX() * scaleX - 5, getY() * scaleY, getX() * scaleX + 5, getY() * scaleY);
        g.strokeLine(getX() * scaleX, getY() * scaleY + 5, getX() * scaleX, getY() * scaleY - 5);
    }

    /**
     * @return retezec popisujici instanci daneho hrace
     */
    @Override
    public String toString() {
        return "Player: " +
                hp + " HP " +
                "[" + coordinates.getX() +
                ", " + coordinates.getY() +
                ", ]" + coordinates.getZ();
    }
}
