import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import javax.imageio.ImageWriter;
import java.util.ArrayList;

/**
 * Created by kraus on 18.03.2017.
 *
 * strela
 */
public class Missile implements IDrawable, IMappable {

    protected int updateNo;
    protected double startZ;

    protected String collidingSpot = "---";

    protected Point unitVector;
    protected Point coordinates;
    protected Point direction;
    protected Point newCoord;
    protected Point collidingPoint;

    protected double strikeRadius;
    protected double azimuth;
    protected double elevation;
    protected double distance;
    private int dmg;

    protected boolean colliding = false;

    private ArrayList<Wind> winds;

    //protected double[] visualRotate = new double[] { 0, 0, 0 };

//    protected ArrayList<Point> allCoordinates;
//    protected ArrayList<Point> allVisualCoordinates;

    //protected static int pocet = 0;

    //protected final static int index++;
    //protected final int PORADI = ++pocet;
    //protected final int MAX_VISUAL_INDEX;

    protected final double ACCELERATION;

    protected final Point TEMP1;
    protected Image IMG;
    protected ImageView imgV;

    //protected static final int VISUALIZED_MISSILES_COUNT = 8;
    /** vychozi okoli (v metrech), ktere muze byt srelou zasazeno */
    protected static final double HITBOX_TOLERANCE = 0;
    protected static final double DEFAULT_STRIKE_RADIUS = 60;
    protected static final double GRAVITY = 10;
    protected static final double DELTA_T = 0.01;
    protected static final double MAGIC_B = 0.05;
    private static final int DMG = 100;

    protected static final Point MAGIC_POINT = new Point(0,0,-1);

    //protected static final boolean DEFAULT_VISUALIZE = false;

    protected static final String IMG_PATH = "images/missile.png";

    /**
     * pretizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z z-ova souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param acceleration rychlost strely v metrech za sekundu
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double acceleration) {
        this(new Point(x,y,z), azimuth, elevation, acceleration, DEFAULT_STRIKE_RADIUS, DMG);
    }

//    /**
//     * pretizeni konstruktoru
//     *
//     * @param x x-ova souradnice v metrech
//     * @param y y-ova souradnice v metrech
//     * @param z z-ova souradnice v metrech
//     * @param azimuth aizmut ve stupnich
//     * @param elevation zdvih dela ve stupnich
//     * @param acceleration rychlost strely v metrech za sekundu
//     * @param visual vyzualizovat strelu
//     */
//    public Missile(double x, double y, double z, double azimuth, double elevation, double acceleration, boolean visual, World world) {
//        this(new Point(x,y,z), azimuth, elevation, acceleration, DEFAULT_STRIKE_RADIUS, visual, world);
//    }

    /**
     * pretizeni konstruktoru
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param acceleration rychlost strely v metrech za sekundu
     */
    public Missile(Point coordinates, double azimuth, double elevation, double acceleration) {
        this(coordinates, azimuth, elevation, acceleration, DEFAULT_STRIKE_RADIUS, DMG);
    }

//    /**
//     * pretizeni konstruktoru
//     *
//     * @param coordinates souradnice v metrech
//     * @param azimuth aizmut ve stupnich
//     * @param elevation zdvih dela ve stupnich
//     * @param acceleration rychlost strely v metrech za sekundu
//     * @param visual vyzualizovat strelu
//     */
//    public Missile(Point coordinates, double azimuth, double elevation, double acceleration, boolean visual, World world) {
//        this(coordinates, azimuth, elevation, acceleration, DEFAULT_STRIKE_RADIUS, visual, world);
//    }

    public Missile(double x, double y, double z, double azimuth, double elevation, double acceleration, double strikeRadius, int damage) {
        this(new Point(x,y,z), azimuth, elevation, acceleration, strikeRadius, damage);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrech
     * @param z z-ova souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param acceleration rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double acceleration, int strikeRadius) {
        this(new Point(x,y,z), azimuth, elevation, acceleration, strikeRadius, DMG);
    }

//    /**
//     * pretizeni konstruktoru
//     *
//     * @param x x-ova souradnice v metrech
//     * @param y y-ova souradnice v metrech
//     * @param z z-ova souradnice v metrech
//     * @param azimuth aizmut ve stupnich
//     * @param elevation zdvih dela ve stupnich
//     * @param acceleration rychlost strely v metrech za sekundu
//     * @param strikeRadius prumer vybuchu strely
//     * @param visual vyzualizovat strelu
//     * @param world reference na svet
//     */
//    public Missile(double x, double y, double z, double azimuth, double elevation, double acceleration, double strikeRadius, boolean visual, World world) {
//        this(new Point(x,y,z), azimuth, elevation, acceleration, strikeRadius, visual, world);
//    }

    /**
     * konstruktor
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param acceleration rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(Point coordinates, double azimuth, double elevation, double acceleration, double strikeRadius) {
        this(coordinates, azimuth, elevation, acceleration, strikeRadius, DMG);
    }

    /**
     * konstruktor
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param acceleration rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(Point coordinates, double azimuth, double elevation, double acceleration, double strikeRadius, int damage) {
        //System.out.printf(PORADI + ". %-10.2f%-10.2f%-10.2f\n", azimuth, elevation, acceleration);
        this.strikeRadius = strikeRadius;
        this.ACCELERATION = acceleration;
        this.dmg = damage;

        winds = new ArrayList<>();

        this.azimuth = azimuth;
        this.elevation = elevation;

        azimuth = Math.PI * azimuth / 180;
        elevation = Math.PI * elevation / 180;

        double speedX = 0;
        double speedY = 0;
        double speedZ = Math.sin(elevation);

        if (!(elevation == 90 || elevation == - 90)) {
            speedX = Math.cos(azimuth);
            speedY = Math.sin(azimuth) * -1;

            //speedZ = Math.tan(elevation);
        }


        this.direction = new Point(speedX, speedY, speedZ);
        this.coordinates = coordinates.copy().add(this.direction.mul(2));
        this.startZ = coordinates.getZ();
        this.direction.mul(ACCELERATION);
        this.newCoord = this.coordinates.copy();
        this.TEMP1 = MAGIC_POINT.copy().mul(DELTA_T * GRAVITY);

//        if (VISUALIZE) {
//            setAllCoordinates(world);
//            MAX_VISUAL_INDEX = (getAllVisualCoordinates().size() - 1) / VISUALIZED_MISSILES_COUNT;
//        } else
//            MAX_VISUAL_INDEX = 0;

        try {
            imgV = new ImageView(new Image(IMG_PATH));
            IMG = imgV.getImage();
        } catch (Exception e) {
            WritableImage imgW = new WritableImage(7, 5);

            PixelWriter pixelWriter = imgW.getPixelWriter();

            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    pixelWriter.setColor(i,j * 4, Color.RED);
            
            for (int i = 1; i < 5; i++)
                for (int j = 0; j < 2; j++)
                    pixelWriter.setColor(i, j * 2 + 1, Color.WHITE);

            pixelWriter.setColor(5,1, Color.RED);
            pixelWriter.setColor(6, 2, Color.RED);
            pixelWriter.setColor(5,3, Color.RED);

            for (int i = 0; i < 3; i++)
                pixelWriter.setColor(i,2, Color.RED);

            for (int i = 0; i < 3; i++)
                pixelWriter.setColor(i + 3, 2, Color.WHITE);

            imgV = new ImageView(imgW);
            IMG = imgV.getImage();
        }

//        imgV.setPreserveRatio(false);
//        imgV.resize(14, 10);
//
//        IMG = imgV.getImage();
//        System.out.println(imgV.getImage().getWidth());
//        System.out.println(IMG.getWidth());
    }

    public Point getCollidingPoint() {
        return collidingPoint;
    }

    /**
     * Zjistuje zda strela neco zasasahla
     *
     * @param surface povrch mapy (teren) v metrech
     * @param scaleX hodnota pro prepocet x-ove souradnice v metrech na sloupec
     * @param scaleY hodnota pro prepocet y-ove souradnice v metrech na radek
     * @return zda-li strela neco zasahla
     */
    public boolean isColliding(double[][] surface, double scaleX, double scaleY) {
        int iX = (int)(coordinates.getX() * scaleX);
        int iY = (int)(coordinates.getY() * scaleY);

        //System.out.println(surface[iX][iY]);

        return coordinates.getZ() <= surface[iX][iY];

        //return coordinates.getZ() <= surface[iX][iY];

//        if (coordinates.getX() - (HITBOX_TOLERANCE - 1) < 0 || coordinates.getY() - (HITBOX_TOLERANCE - 1) < 0 ||
//                coordinates.getX() + (HITBOX_TOLERANCE - 1) > mapWidth || coordinates.getY() + (HITBOX_TOLERANCE - 1) > mapHeight) {
//
//            if
//
//            return false;
//        }


//        for (int i =  -1; i < HITBOX_TOLERANCE; i++) {
//            for (int j = -1; j < HITBOX_TOLERANCE; j++) {
//                iX = (int)((coordinates.getX() + i) * scaleX);
//                iY = (int)((coordinates.getY() + j) * scaleY);
//
//                if (iX < 0 || iX >= surface.length || iY < 0 || iY >= surface[0].length)
//                    continue;
//
//                if (coordinates.getZ() <= surface[iX][iY]) {
//                    //System.out.println(surface[iX][iY] + " < " + coordinates.getZ());
//                    //System.out.println("ME " + iX + " - " + iY);
//                    return true;
//                }
//            }
//        }

        //System.out.println("Povrch: " + surface[iX][iY]);

        //return false;
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


//        double scale = coordinates.getZ() / Math.max(300, startZ);
//        imgV.setFitHeight(30);
//        imgV.setFitWidth(30);
//        IMG = imgV.getImage();
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
        if (colliding)
            return;

        // zapsani hodnot vetru do "Historie strelby"
        if (updateNo % 10 == 0)
            winds.add(new Wind(world.getWind().getAzimuth(), world.getWind().getSpeed()));

        updateNo++;

//        if (VISUALIZE) {
//            updateVisualize();
//            return;
//        }

        //Point directionVector = direction.copy().mul(ACCELERATION);

        //System.out.println(direction);
        //System.out.println(directionVector.copy().div(ACCELERATION));

        coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));
        //System.out.println(coordinates.getZ());

        Point temp2 = new Point(world.getWind().getCoordinates().copy().sub(direction.copy()));

        direction = direction.copy().add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));

        //direction = directionVector.div(ACCELERATION);

        //System.out.println(directionVector.copy().div(ACCELERATION));
        //System.out.println("-----------");

//        Point xtPlusDeltaT = coordinates.copy().add(((direction.copy()).mul(ACCELERATION)).mul(DELTA_T));
//        Point temp2 = new Point(world.getWind().getCoordinates().copy().sub(direction.copy()).mul(ACCELERATION));
//
//        newCoord = xtPlusDeltaT.add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));
//
////        distance = newCoord.getPointsDistance(coordinates);
////        unitVector = newCoord.copy().sub(coordinates);
////        unitVector.div(distance);
////        unitVector.mul(2.5);
//
////        coordinates.add(unitVector);
//
//        coordinates = newCoord.copy();

//        System.out.println(PORADI + ".Strela: ");
//        System.out.println(coordinates);

        //System.out.println(newCoord);

        if (isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())) {
            System.out.println("Strela opustila mapu!");
            world.removeMissile(this);
            colliding = true;
            return;
        }

        if (isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {
            this.collidingSpot = "Teren";

            //isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY(), world.getMap().getMapWidthM(), world.getMap().getMapHeightM());

            Explosion explosion = new Explosion(this);
            world.addExplosion(explosion);
            explosion.explode(world);

            System.out.println("Bum!");
            world.removeMissile(this);
            colliding = true;
        }
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getElevation() {
        return elevation;
    }

    public double getACCELERATION() {
        return ACCELERATION;
    }

    public String getCollidingSpot() {
        return collidingSpot;
    }

    public ArrayList<Wind> getWinds() {
        return winds;
    }

    public int getDmg() {
        return dmg;
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

    public void setCollidingSpot(String collidingSpot) {
        this.collidingSpot = collidingSpot;
    }

    //    public ArrayList<Point> getAllCoordinates() {
//        return allCoordinates;
//    }
//
//    public ArrayList<Point> getAllVisualCoordinates() {
//        return allVisualCoordinates;
//    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    @Override
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void setCoordinates(double x, double y, double z) {
        this.coordinates = new Point(x, y, z);
    }

    public void setCollidingPoint(World world) {
        Point currentCoord = coordinates.copy();

        while (!isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())
                && !isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {

            coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));
            Point temp2 = new Point(0, 0, 0).sub(direction.copy());

            direction = direction.copy().add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));
        }

        collidingPoint = coordinates.copy();

        coordinates = currentCoord.copy();
    }
}
