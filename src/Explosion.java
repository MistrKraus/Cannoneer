import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;


/**
 * Created by kraus on 20.03.2017.
 *
 * Objekt reprezentujici vybuch
 */
public class Explosion implements IDrawable, IMappable {

    private Missile missile;

    private Point coordinates;
    private double radius;

    /**
     * kontruktor exploze
     *
     * @param missile strela, ktera dopadla
     */
    public Explosion(Missile missile) {
        this.coordinates = missile.getCoordinates().copy();
        this.radius = missile.getStrikeRadius();

        this.missile = missile;
    }

    /**
     * pokud je v radiusu exploze {@code Target} objekt je mu udeleno zraneni
     *
     * @param world ridici trida
     */
    public void explode(World world) {
        world.getTargets().stream().filter(target -> target.isInRadius(coordinates, radius, target.getCoordinates()))
            .forEach(target -> {
                    target.dealtDamage(100);
                    missile.setCollidingSpot("Cíl");
            });



        //world.getPlayers().stream().filter(player -> player.isInRadius(coordinates, radius, player.getCoordinates()))
        //    .forEach(player -> player.dealtDamage(100));
    }

    /**
     * vykresli explozi
     *
     * @param g graficky kontext, ktery nakresli instanci
     * @param scaleMperPixelX pocet metru, ktere reprezetuje jeden metr na x-ove ose
     * @param scaleMperPixelY pocet metru, ktere reprezetuje jeden metr na y-ove ose
     */
    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        g.setFill(Color.ORANGE);
        g.fillOval((coordinates.getX() - radius / 2) * scaleMperPixelX, (coordinates.getY() - radius / 2) * scaleMperPixelY,
                radius * scaleMperPixelX, radius * scaleMperPixelY);
    }

    @Override
    public void update(World world) {

    }

    /**
     * @return prumer exploze
     */
    @Override
    public double getHeight() {
        return radius;
    }

    /**
     * @return prumer exploze
     */
    @Override
    public double getWidthX() {
        return radius;
    }

    /**
     * @return prumer exploze
     */
    @Override
    public double getWidthY() {
        return radius;
    }

    /**
     * @return souradnice exploze v metrech
     */
    @Override
    public Point getCoordinates() {
        return coordinates;
    }

    /**
     * @return x-ova souradnice v metrech
     */
    @Override
    public double getX() {
        return coordinates.getX();
    }

    /**
     * @return y-ova souradnice v metech
     */
    @Override
    public double getY() {
        return coordinates.getY();
    }

    /**
     * @return nadmorska vyska v metrech
     */
    @Override
    public double getZ() {
        return coordinates.getZ();
    }

    /**
     * @return prumer exploze
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Nastavi nove souradnice
     *
     * @param point souradnice v metrech
     */
    @Override
    public void setCoordinates(Point point) {
        coordinates = point.copy();
    }

    /**
     * Nastavi nove souradnice
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnie v metrech
     * @param z z-ova souradnice v metrech
     */
    @Override
    public void setCoordinates(double x, double y, double z) {
        setCoordinates(new Point(x, y, z));
    }
}
