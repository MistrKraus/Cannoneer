import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 18.03.2017.
 *
 * strela
 */
public class Missile implements IDrawable, IMappable {

    private Point coordinates;
    private Point speed;
    private double strikeRadius;

    private final double acceleration;
    /** vychozi okoli (v metrech), ktere muze byt srelou zasazeno */
    private static final double DEFAULT_STRIKE_RADIUS = 60;
    private static final Point MAGIC_POINT = new Point(0,0,-1);
    private static final double GRAVITY = 10;
    private static final double DELTA_T = 0.01;
    private static final double MAGIC_B = 0.05;

    /**
     * pretizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z z-ova souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param speed rychlost strely v metrech za sekundu
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double speed) {
        this(new Point(x,y,z), azimuth, elevation, speed, DEFAULT_STRIKE_RADIUS);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param speed rychlost strely v metrech za sekundu
     */
    public Missile(Point coordinates, double azimuth, double elevation, double speed) {
        this(coordinates, azimuth, elevation, speed, DEFAULT_STRIKE_RADIUS);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z z-ova souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param speed rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double speed, double strikeRadius) {
        this(new Point(x,y,z), azimuth, elevation, speed, strikeRadius);
    }

    /**
     * konstruktor
     * provede potrebnou matematiku pro pohyb rakety
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param speed rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(Point coordinates, double azimuth, double elevation, double speed, double strikeRadius) {
        //this.coordinates = coordinates.copy().subZ(0.3);
        this.strikeRadius = strikeRadius;
        this.acceleration = speed;

        if (azimuth < 0)
            azimuth += 360;

        azimuth = Math.PI * azimuth / 180;

        double speedX = Math.cos(azimuth);
        double speedY = Math.sin(azimuth) * -1;

        if (elevation < 0)
            elevation += 360;

        elevation = Math.PI * elevation / 180;

        double speedZ = Math.sin(elevation);
        // TODO strelba
        speedZ = 0;

        this.speed = new Point(speedX, speedY, speedZ);
        this.coordinates = coordinates.copy().subZ(0.3).add((this.speed.mul(2)));
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
     * @param data nactena data
     * @return zda-li strela neco zasahla
     */
    public boolean isColliding(double[][] surface, double scaleX, double scaleY, Data data) {
        int iX; // = (int)(coordinates.getX() * scaleX);
        int iY; // = (int)(coordinates.getY() * scaleY);

        //return coordinates.getZ() <= surface[iX][iY];

        for (int i =  -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                iX = (int)((coordinates.getX() + i * 2) * scaleX);
                iY = (int)((coordinates.getY() + j * 2) * scaleY);

                if (iX < 0 || iX >= surface.length || iY < 0 || iY >= surface[0].length)
                    continue;

                if (coordinates.getZ() <= surface[iX][iY])
                    return true;
            }
        }

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
        g.setFill(Color.ORANGE);
        g.fillOval((coordinates.getX() - 3 / 2) * scaleX, (coordinates.getY() - 3 / 2) * scaleY, 3, 3);
    }

    /**
     * Porune strelu prislusnym smerem
     * Zjisti zda neupustila mapu ci nekoliduje s povrchem
     *
     * @param world ridici trida
     */
    @Override
    public void update(World world) {
        //coordinates = coordinates.copy().add(speed.copy().mul(acceleration));

        // Spravne vypocty
        Point xtPlusDeltaT = coordinates.copy().add(((speed.copy()).mul(acceleration)).mul(0.01));
        Point temp1 = new Point(0,0,-1).mul(0.01*10);
        Point temp2 = new Point(world.getWind().getCoordinates().sub(speed.copy()).mul(acceleration));

        coordinates = xtPlusDeltaT.add(temp1).add(temp2.mul(0.05*0.01));

//        Point oldCoord = coordinates.copy();
//        Point newCoord = coordinates.copy();
//
//        Point xtPlusDeltaT = coordinates.copy().add(((speed.copy()).mul(acceleration)).mul(0.01));
//        Point temp1 = new Point(0,0,-1).mul(0.01*10);
//        Point temp2 = new Point(world.getWind().getCoordinates().sub(speed.copy()).mul(acceleration));
//
//        newCoord = xtPlusDeltaT.add(temp1).add(temp2.mul(0.05*0.01));
//
//        double distance = newCoord.getPointsDistance(oldCoord);
//
//        double newCoordSize = newCoord.getPointsDistance(new Point(0,0,0));
//        double oldCoordSize = oldCoord.getPointsDistance(new Point(0,0,0));
//
//        Point smerovyVektor = newCoord.copy().sub(oldCoord);
//
//        Point normVektor = smerovyVektor.copy().mulX(-1.0).mulY(-1.0);
//
//        Point jednotkovyVektor = smerovyVektor.copy().div(normVektor);
//
//        coordinates.add(jednotkovyVektor.mul(2));

        System.out.println(coordinates);

        if (isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())) {
            System.out.println("Strela opustila mapu!");
            world.removeMissile(this);
            return;
        }

        if (isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY(), world.getData())) {
            world.removeMissile(this);

            Explosion explosion = new Explosion(coordinates, strikeRadius);
            world.addExplosion(explosion);
            explosion.explode(world);

            System.out.println("Bum!");
        }

        System.out.println(coordinates);
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
