import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 11.03.2017.
 *
 * Cil
 */
public class Target implements IHittable {

    /**
     * defaultni pocet hp
     */
    protected static final int DEFAULT_HP = 100;
    /** vychozi vyska v metrech*/
    protected static final double DEFAULT_HEIGHT = 1;
    /** souracnice cile v metrech */
    protected Point coordinates;
    /**
     * zivoty ~ hp cile
     */
    protected int hp;

    /**
     * Konstruktor prirazujuci cili vychozi pocet zivotu ~ hp
     *
     * @param point pozice cile na mape
     */
    public Target(Point point) {
        this(point, DEFAULT_HP);
    }

    /**
     * Konstruktor cile prirazujici vychozi poceti zivotu ~ hp
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z nadmorska vyska v metrech
     */
    public Target (double x, double y, double z) {
        this(new Point(x, y, z), DEFAULT_HP);
    }

    /**
     * Konstruktor cile
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z nadmorska vyska v metrech
     * @param hp zivoty
     */
    public Target(double x, double y, double z, int hp) {
        this(new Point(x, y, z), hp);
    }

    /**
     * Konstruktor cile
     *
     * @param point pozice cile na mape
     * @param hp pocet zivotu ~ hp cile
     */
    public Target(Point point, int hp) {
        coordinates = new Point(point.getX(), point.getY(), point.getZ() + DEFAULT_HEIGHT);
        this.hp = hp;
    }

    /**
     * snizi pocet hp cile
     *
     * @param dmg pocet hp ~ zivotu, ktere se maji ubrat
     */
    @Override
    public void dealtDamage (int dmg) {
        this.hp -= dmg;
    }

    /**
     * vrati pocet hp
     *
     * @return pocet hp
     */
    public int getHp() {
        return hp;
    }

    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        g.setStroke(Color.RED);
        g.setLineWidth(1);
        g.strokeLine(getX() * scaleX - 5, getY() * scaleY, getX() * scaleX + 5, getY() * scaleY);
        g.strokeLine(getX() * scaleX, getY() * scaleY + 5, getX() * scaleX, getY() * scaleY - 5);
    }

    @Override
    public void update(World world) {
        if (hp <= 0) {
            System.out.println("Cil zasazen");
            world.removeTarget(this);
        }
    }

    @Override
    public double getHeight() {
        return 1;
    }

    @Override
    public double getWidthX() {
        return 1;
    }

    @Override
    public double getWidthY() {
        return 1;
    }

    /**
     * vraci souradnice cile
     *
     * @return souradnice cile
     */
    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(Point point) {

    }

    @Override
    public void setCoordinates(double x, double y, double z) {

    }

    /**
     * Vraci retezec s klicovymi informacemi
     *
     * @return retezec s klicovymi informacemi
     */
    @Override
    public String toString() {
        return "Target: " +
                hp + " HP " +
                "[" + coordinates.getX() +
                ", " + coordinates.getY() +
                ", " + coordinates.getZ() +
                "]";
    }
}
