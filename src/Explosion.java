import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.transform.Affine;

import java.util.Random;


/**
 * Created by kraus on 20.03.2017.
 *
 * Objekt reprezentujici vybuch
 */
public class Explosion implements IDrawable, IMappable {

    private int deathCountdown;

    private double posX;
    private double posY;

    private Missile missile;

    private Point coordinates;
    private final int dmg;
    private final double radius;

    private final static int DEF_LIFETIME = 35;

    /**
     * kontruktor exploze
     *
     * @param missile strela, ktera dopadla
     */
    public Explosion(Missile missile) {
        this.missile = missile;

        this.coordinates = missile.getCoordinates().copy();
        this.radius = missile.getStrikeRadius();

        this.dmg = missile.getDmg();

        this.deathCountdown = 0;
    }

    /**
     * pokud je v radiusu exploze {@code Target} objekt je mu udeleno zraneni
     *
     * @param world ridici trida
     */
    public void explode(World world) {
        int currDmg = new Random().nextInt(dmg - 4) + 5;

        world.getTargets().stream().filter(target -> target.isInRadius(coordinates, radius, target.getCoordinates()))
            .forEach(target -> {
                    target.dealtDamage(currDmg);
                    missile.setCollidingSpot(target.toString());
            });

        world.getPlayers().stream().filter(player -> player.isInRadius(coordinates, radius, player.getCoordinates()))
            .forEach(player -> {
                player.dealtDamage(currDmg);
                missile.setCollidingSpot(player.toString());
            });
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
        Affine t = g.getTransform();

        //g.setGlobalAlpha((DEF_LIFETIME - deathCountdown)/((1.5) *DEF_LIFETIME));

        //System.out.println((double)(DEF_LIFETIME - deathCountdown)/DEF_LIFETIME);

        double width = deathCountdown * ((radius * scaleMperPixelX) / DEF_LIFETIME);
        double height = deathCountdown * ((radius * scaleMperPixelY) / DEF_LIFETIME);

        g.translate(coordinates.getX() * scaleMperPixelX - width / 2, coordinates.getY() * scaleMperPixelY - height / 2);

        g.setFill(Color.rgb(255, ((130 / DEF_LIFETIME) * deathCountdown), 0));


        g.fillOval(0, 0, width + 3, height + 3);

        g.setTransform(t);
        //g.setGlobalAlpha(1);
    }

    @Override
    public void update(World world) {
        if (deathCountdown++ == DEF_LIFETIME) {
            world.removeExplosion(this);
            return;
        }


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
