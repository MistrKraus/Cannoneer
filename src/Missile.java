import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Created by kraus on 18.03.2017.
 *
 * strela
 */
public class Missile implements IDrawable, IMappable {

    private Point unitVector;
    private Point coordinates;
    private Point direction;
    private Point newCoord;

    private double strikeRadius;
    private double azimuth;
    /** vzdalenost mezi novymi a soucasnymi souradnicemi*/
    private double distance;

    private final double acceleration;
    private final Point temp1;
    private final Image IMG;
    /** vychozi okoli (v metrech), ktere muze byt srelou zasazeno */
    private static final double HITBOX_TOLERANCE = 3;
    private static final double DEFAULT_STRIKE_RADIUS = 60;
    private static final double GRAVITY = 10;
    private static final double DELTA_T = 0.01;
    private static final double MAGIC_B = 0.05;
    private static final Point MAGIC_POINT = new Point(0,0,-1);
    private static final String IMG_PATH = "images/missile.png";

    /**
     * pretizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z z-ova souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param direction rychlost strely v metrech za sekundu
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double direction) {
        this(new Point(x,y,z), azimuth, elevation, direction, DEFAULT_STRIKE_RADIUS);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param direction rychlost strely v metrech za sekundu
     */
    public Missile(Point coordinates, double azimuth, double elevation, double direction) {
        this(coordinates, azimuth, elevation, direction, DEFAULT_STRIKE_RADIUS);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z z-ova souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param direction rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double direction, double strikeRadius) {
        this(new Point(x,y,z), azimuth, elevation, direction, strikeRadius);
    }

    /**
     * konstruktor
     * provede potrebnou matematiku pro pohyb rakety
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param direction rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(Point coordinates, double azimuth, double elevation, double direction, double strikeRadius) {
        this.strikeRadius = strikeRadius;
        this.acceleration = direction;

        if (azimuth < 0) {
            azimuth += 360;
        }

        this.azimuth = azimuth;

        azimuth = Math.PI * azimuth / 180;

        double speedX = Math.cos(azimuth);
        double speedY = Math.sin(azimuth) * - 1;

        if (elevation < 0)
            elevation += 360;

        elevation = Math.PI * elevation / 180;

        double speedZ = Math.sin(elevation);
        System.out.println(speedZ);

        this.direction = new Point(speedX, speedY, speedZ);
        this.coordinates = coordinates.copy().add(this.direction.mul(2));
        this.newCoord = this.coordinates.copy();
        this.temp1 = MAGIC_POINT.copy().mul(DELTA_T * GRAVITY);

        IMG = new Image(IMG_PATH);
    }

    //TODO nastavit collidingPoint
    public Point getCollidingPoint() {
        return new Point(0,0,0);
    }

    /**
     * Zjistuje zda strela neco zasasahla
     *
     * @param surface povrch mapy (teren) v metrech
     * @param scaleX hodnota pro prepocet x-ove souradnice v metrech na sloupec
     * @param scaleY hodnota pro prepocet y-ove souradnice v metrech na radek
     * @param mapWidth sirka mapy v metech
     * @param mapHeight vyska mapy v metrech
     * @return zda-li strela neco zasahla
     */
    public boolean isColliding(double[][] surface, double scaleX, double scaleY, double mapWidth, double mapHeight) {
        int iX = 0; // = (int)(coordinates.getX() * scaleX);
        int iY = 0; // = (int)(coordinates.getY() * scaleY);

        //return coordinates.getZ() <= surface[iX][iY];

        if (coordinates.getX() - (HITBOX_TOLERANCE - 1) < 0 || coordinates.getY() - (HITBOX_TOLERANCE - 1) < 0 ||
                coordinates.getX() + (HITBOX_TOLERANCE - 1) > mapWidth || coordinates.getY() + (HITBOX_TOLERANCE - 1) > mapHeight) {
            System.out.println("WTF");
            return false;
        }

        for (int i =  -1; i < HITBOX_TOLERANCE; i++) {
            for (int j = -1; j < HITBOX_TOLERANCE; j++) {
                iX = (int)((coordinates.getX() + i) * scaleX);
                iY = (int)((coordinates.getY() + j) * scaleY);

                if (iX < 0 || iX >= surface.length || iY < 0 || iY >= surface[0].length)
                    continue;

                if (coordinates.getZ() <= surface[iX][iY]) {
                    //System.out.println("Povrch: " + surface[iX][iY]);
                    return true;
                }
            }
        }

        //System.out.println("Povrch: " + surface[iX][iY]);

        return false;
    }

    /**
     * Zjisti zda strela opustila mapu
     *
     * @param mapWidth sirka mapy v metrech
     * @param mapHeight vyska mapy v metrech
     * @return zda strela opustila mapu
     */
    public boolean isOutsideMap(double mapWidth, double mapHeight) {
        return (coordinates.getX() < 0 || coordinates.getY() < 0 ||
            coordinates.getX() > mapWidth || coordinates.getY() > mapHeight);
    }

    /**
     * vykresli strelu
     *
     * @param g graficky kontext, ktery nakresli instanci
     * @param scaleX hodnota, ktera po vynasobeni x-ove souradnice v metrech urci tuto souradnici v pixelech
     * @param scaleY hodnota, ktera po vynasobeni y-ove souradnice v metrech urci tuto souradnici v pixelech
     */
    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
//        g.setFill(Color.ORANGE);
//        g.fillOval((coordinates.getX() - 3 / 2) * scaleX, (coordinates.getY() - 3 / 2) * scaleY, 3, 3);

        Affine t = g.getTransform();
        g.translate((coordinates.getX() - IMG.getWidth() / 2) * scaleX, (coordinates.getY() - IMG.getHeight() / 2) * scaleY);
        g.rotate(-azimuth);
        g.drawImage(IMG, 0, 0);
        g.setTransform(t);
    }

    /**
     * Porune strelu prislusnym smerem
     * Zjisti zda neupustila mapu ci nekoliduje s povrchem
     *
     * @param world ridici trida
     */
    @Override
    public void update(World world) {
        // Spravne vypocty -v
        Point xtPlusDeltaT = coordinates.copy().add(((direction.copy()).mul(acceleration)).mul(DELTA_T));
        Point temp2 = new Point(world.getWind().getCoordinates().sub(direction.copy()).mul(acceleration));

        //System.out.println(temp1);
        newCoord = xtPlusDeltaT.add(temp1).add(temp2.mul(MAGIC_B * DELTA_T));

        distance = newCoord.getPointsDistance(coordinates);
        unitVector = newCoord.copy().sub(coordinates);
        unitVector.div(distance);
        unitVector.mul(2.5);

        coordinates.add(unitVector);
//        System.out.println(unitVector);

        System.out.println("Strela: " + coordinates.getZ());

        if (isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())) {
            System.out.println("Strela opustila mapu!");
            world.removeMissile(this);
            return;
        }

        if (isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY(), world.getMap().getMapWidthM(), world.getMap().getMapHeightM())) {
            world.removeMissile(this);
            //isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY(), world.getMap().getMapWidthM(), world.getMap().getMapHeightM());

            Explosion explosion = new Explosion(coordinates, strikeRadius);
            world.addExplosion(explosion);
            explosion.explode(world);

            System.out.println("Bum!");
        }
        //System.out.println("------");
    }

    /**
     * @return prumer potencialne zasazene oblasti v metrech
     */
    public double getStrikeRadius() {
        return strikeRadius;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidthX() {
        return 0.5;
    }

    @Override
    public double getWidthY() {
        return 0.5;
    }

    @Override
    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void setCoordinates(double x, double y, double z) {
        this.coordinates = new Point(x, y, z);
    }
}
