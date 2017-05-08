import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

/**
 * Created by kraus on 11.03.2017.
 *
 * Hrac
 */
public class Player extends Target implements IShooting {

    private double azimuth = 0;

    private static final int IMG_CANNON_WIDTH = 4;
    private static final String IMG_PATH = "images/player1.png";

    /**
     * perizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z nadmorska vyska v metech
     */
    public Player (double x, double y, double z) {
        this(new Point(x, y, z));
    }

    /**
     * pretizeni konstruktoru
     *
     * @param point souradnice v metrech
     */
    public Player (Point point) {
        this(point, DEFAULT_HP);
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
        this(new Point(x, y, z), hp);
    }

    /**
     * konstruktor
     *
     * @param point souradnice v metrech
     * @param hp pocet zivotu
     */
    public Player(Point point, int hp) {
        super(point, hp, IMG_PATH);
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
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
        System.out.println("Vystrel");
        return new Missile(coordinates.copy(), azimuth, elevation, speed);
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
//        g.setStroke(Color.BLUE);
//        g.setLineWidth(1);
//        g.strokeLine(getX() * scaleX - 5, getY() * scaleY, getX() * scaleX + 5, getY() * scaleY);
//        g.strokeLine(getX() * scaleX, getY() * scaleY + 5, getX() * scaleX, getY() * scaleY - 5);

        Affine t = g.getTransform();
        g.translate((int)(getX() * scaleX), (int)(getY() * scaleY));
        g.rotate(-azimuth);
        g.drawImage(IMG, - (IMG.getWidth() - IMG_CANNON_WIDTH) / 2, -IMG.getHeight() / 2);
        g.setTransform(t);

//        System.out.println("Player:\n metryX: " + getX() + "\n metryY: " + getY() +
//                "\n X: " +(int)(getX() * scaleX - IMG.getWidth() / 2) +
//                "\n Y: " + (int)(getY() * scaleY - IMG.getHeight() / 2));


//        g.setFill(Color.GREEN);
//
//        double maxWidth = getDrawableSize(getWidthX() * scaleX);
//        double maxHeight = getDrawableSize(getWidthY() * scaleY);
//
//        Rectangle base = new Rectangle((int)(getX() - maxWidth / 2), (int)(getY() - maxHeight / 2), (int)maxWidth, (int)maxHeight);
//        base.setFill(Color.color(0, 62, 0));
//        Rectangle cannon = new Rectangle((int)(getX() - maxWidth), (int)getY(), (int)maxWidth, (int)(maxHeight / 5));
//        base.setFill(Color.color(0, 112, 0));
//
//        Shape player = Shape.union(base, cannon);
        //Path player = Path.union(base, cannon);

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
