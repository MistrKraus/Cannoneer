import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 11.03.2017.
 *
 * Cil
 */
public class Target implements IHittable {

    /** zivoty ~ hp cile*/
    protected int hp;

    protected double mapX;
    protected double mapY;

    protected final Image IMG;
    private static final String IMG_PATH = "images/target1.png";
    /** defaultni pocet hp*/
    protected static final int DEFAULT_HP = 100;
    /** vychozi vyska v metrech*/
    protected static final double DEFAULT_HEIGHT = 1;
    /** souracnice cile v metrech */
    protected Point coordinates;
    protected static int pocet = 0;
    protected final int PORADI = ++pocet;
    protected Banner banner;

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
        this(point, hp, IMG_PATH);
    }

    public Target(Point point, int hp, String imgPath) {
        coordinates = new Point(point.getX(), point.getY(), point.getZ() + DEFAULT_HEIGHT);
        this.hp = hp;

        //System.out.println("tarX: " + point.getX() + " tarY" + point.getY());

        IMG = loadImage(imgPath);
        banner = new Banner(this);
    }

    public Image loadImage(String path) {
        return new Image(path);
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

    public double getDrawableSize(double size) {
        if (size >= 6)
            return size;
        return 6;
    }

    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
//        g.setStroke(Color.RED);
//        g.setLineWidth(1);
//        g.strokeLine(getX() * scaleX - 5, getY() * scaleY, getX() * scaleX + 5, getY() * scaleY);
//        g.strokeLine(getX() * scaleX, getY() * scaleY + 5, getX() * scaleX, getY() * scaleY - 5);

        mapX = (getX() * scaleX - IMG.getWidth() / 2);
        mapY = (getY() * scaleY - IMG.getHeight() / 2);

        g.drawImage(IMG, mapX, mapY);

        banner.draw(g, scaleX, scaleY);

//        System.out.println("Taget\n metryX: " + getX() + "\n metryY: " + getY() +
//                "\n X: " + (int)(getX() * scaleX - IMG.getWidth() / 2) +
//            "\n Y: " + (int)(getY() * scaleY - IMG.getHeight() / 2));

//        g.setFill(Color.RED);
//
//        double maxWidth = getDrawableSize(getWidthX() * scaleX);
//        double maxHeight = getDrawableSize(getWidthY() * scaleY);
//
//        g.fillRect(getX() * scaleX - maxWidth / 2, getY() * scaleY - maxHeight / 2, maxWidth, maxHeight);
    }

    @Override
    public void update(World world) {
        if (hp <= 0) {
            System.out.println("Cil zasazen");
            world.removeTarget(this);
        }

        banner.update(world);
    }

    @Override
    public double getHeight() {
        return 1;
    }

    @Override
    public double getWidthX() {
        return IMG.getWidth();
    }

    @Override
    public double getWidthY() {
        return IMG.getHeight();
    }

    public double getMapX() {
        return mapX;
    }

    public double getMapY() {
        return mapY;
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
        return "Target" + PORADI;
    }
}
