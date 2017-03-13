/**
 * Created by kraus on 11.03.2017.
 *
 * Cil
 */
public class Target implements IHittable {

    private static final int DEFAULT_HP = 100;
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
     * @param shooterCoordinates souradnice utocnika
     * @return zda byl cil zasazen
     */
    public boolean gotHit(double azimuth, double elevation, double speed, Point shooterCoordinates) {
        int dmg = dealtDamage(shooterCoordinates, coordinates);

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

    /**
     * vraci souradnice cile
     *
     * @return souradnice cile
     */
    public Point getCoordinates() {
        return coordinates;
    }

    /**
     * vraci x-ovou souradnici
     *
     * @return x-ova souradnice
     */
    public double getX() {
        return coordinates.getX();
    }

    /**
     * vraci y-ovou souradnici
     *
     * @return y-ova souradnice
     */
    public double getY() {
        return coordinates.getY();
    }

    /**
     * vraci nadmorskou vysku
     *
     * @return nadmorska vyska
     */
    public double getZ() {
        return coordinates.getZ();
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
