import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 11.03.2017.
 *
 * Cil
 */
public class Target implements IHittable, IDrawable {

    /**
     * defaultni pocet hp
     */
    protected static final int DEFAULT_HP = 100;

    /**
     * souracnice cile v metrech
     */
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
        coordinates = point;
        this.hp = hp;
    }

    /**
     * Vraci informaci, zda utocnik zasahl cil
     *
     * @param azimuth smer strely
     * @param elevation zdvih hlavne
     * @param speed rychlost strely
     * @param missile pouzita strela
     * @return zda byl cil zasazen
     */
    public boolean gotHit(double azimuth, double elevation, double speed, Missile missile) {
        int dmg = dealtDamage(missile, coordinates);

        if (dmg > 0) {
            dealDmg(dmg);
            return true;
        }

        return false;
    }

    /**
     * snizi pocet hp cile
     *
     * @param dmg pocet hp ~ zivotu, ktere se maji ubrat
     */
    public void dealDmg (int dmg) {
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

    }

    @Override
    public double getHeight() {
        return 1;
    }

    @Override
    public double getWidth() {
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
                ", ]" + coordinates.getZ();
    }
}
