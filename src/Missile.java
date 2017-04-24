import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.*;

/**
 * Created by kraus on 18.03.2017.
 *
 * strela
 */
public class Missile implements IDrawable, IMappable {

    private int visualIndex;

    private Point unitVector;
    private Point coordinates;
    private Point direction;
    private Point newCoord;

    private double strikeRadius;
    private double azimuth;
    /** vzdalenost mezi novymi a soucasnymi souradnicemi*/
    private double distance;

    private ArrayList<Point> allCoordinates;
    private ArrayList<Point> allVisualCoordinates;

    private static int pocet = 0;

    //private final static int index++;
    private final int PORADI = ++pocet;
    private final int MAX_VISUAL_INDEX;

    private final double ACCELERATION;

    private final boolean VISUALIZE;

    private final Point TEMP1;
    private final Image IMG;

    private static final int VISUALIZED_MISSILES_COUNT = 8;
    /** vychozi okoli (v metrech), ktere muze byt srelou zasazeno */
    private static final double HITBOX_TOLERANCE = 3;
    private static final double DEFAULT_STRIKE_RADIUS = 60;
    private static final double GRAVITY = 10;
    private static final double DELTA_T = 0.01;
    private static final double MAGIC_B = 0.05;

    private static final Point MAGIC_POINT = new Point(0,0,-1);

    private static final boolean DEFAULT_VISUALIZE = false;

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
        this(new Point(x,y,z), azimuth, elevation, direction, DEFAULT_STRIKE_RADIUS, DEFAULT_VISUALIZE, null);
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
     * @param visual vyzualizovat strelu
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double direction, boolean visual, World world) {
        this(new Point(x,y,z), azimuth, elevation, direction, DEFAULT_STRIKE_RADIUS, visual, world);
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
        this(coordinates, azimuth, elevation, direction, DEFAULT_STRIKE_RADIUS, DEFAULT_VISUALIZE, null);
    }

    /**
     * pretizeni konstruktoru
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param direction rychlost strely v metrech za sekundu
     * @param visual vyzualizovat strelu
     */
    public Missile(Point coordinates, double azimuth, double elevation, double direction, boolean visual, World world) {
        this(coordinates, azimuth, elevation, direction, DEFAULT_STRIKE_RADIUS, visual, world);
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
        this(new Point(x,y,z), azimuth, elevation, direction, strikeRadius, DEFAULT_VISUALIZE, null);
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
     * @param visual vyzualizovat strelu
     * @param world reference na svet
     */
    public Missile(double x, double y, double z, double azimuth, double elevation, double direction, double strikeRadius, boolean visual, World world) {
        this(new Point(x,y,z), azimuth, elevation, direction, strikeRadius, visual, world);
    }

    /**
     * konstruktor
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param direction rychlost strely v metrech za sekundu
     * @param strikeRadius prumer vybuchu strely
     */
    public Missile(Point coordinates, double azimuth, double elevation, double direction, double strikeRadius) {
        this(coordinates, azimuth, elevation, direction, strikeRadius, DEFAULT_VISUALIZE, null);
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
     * @param visual vyzualizovat strelu
     * @param world reference na svet
     */
    public Missile(Point coordinates, double azimuth, double elevation, double direction, double strikeRadius, boolean visual, World world) {
        this.strikeRadius = strikeRadius;
        this.ACCELERATION = direction;
        this.VISUALIZE = visual;

        if (azimuth < 0) {
            azimuth += 360;
        }

        this.azimuth = azimuth;

        azimuth = Math.PI * azimuth / 180;

        double speedX = 0;
        double speedY = 0;

        if (!(elevation == 90 || elevation == - 90)) {
            speedX = Math.cos(azimuth);
            speedY = Math.sin(azimuth) * -1;
        }

        if (elevation < 0) {
            elevation += 360;
        }

        elevation = Math.PI * elevation / 180;

        double speedZ = Math.sin(elevation);
        //System.out.println(speedZ);

        this.direction = new Point(speedX, speedY, speedZ);
        this.coordinates = coordinates.copy().add(this.direction.mul(2));
        this.direction.mul(ACCELERATION);
        this.newCoord = this.coordinates.copy();
        this.TEMP1 = MAGIC_POINT.copy().mul(DELTA_T * GRAVITY);

        if (VISUALIZE) {
            setAllCoordinates(world);
            MAX_VISUAL_INDEX = (getAllVisualCoordinates().size() - 1) / VISUALIZED_MISSILES_COUNT;
        } else
            MAX_VISUAL_INDEX = 0;

        IMG = new Image(IMG_PATH);
    }

    public Point getCollidingPoint() {
        if (allCoordinates == null)
            return new Point(0,0,0);

        return allCoordinates.get(allCoordinates.size() - 1);
    }

    public Point getVisualCollidingPoint() {
        if (allVisualCoordinates == null)
            return new Point(0,0,0);

        return allVisualCoordinates.get(allVisualCoordinates.size() - 1);
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
        int iX;// = (int)(coordinates.getX() * scaleX);
        int iY;// = (int)(coordinates.getY() * scaleY);

        //return coordinates.getZ() <= surface[iX][iY];

//        if (coordinates.getX() - (HITBOX_TOLERANCE - 1) < 0 || coordinates.getY() - (HITBOX_TOLERANCE - 1) < 0 ||
//                coordinates.getX() + (HITBOX_TOLERANCE - 1) > mapWidth || coordinates.getY() + (HITBOX_TOLERANCE - 1) > mapHeight) {
//
//            if
//
//            return false;
//        }

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

    private void drawVisualize(GraphicsContext g, double scaleX, double scaleY) {
        for (int i = 0; i < VISUALIZED_MISSILES_COUNT; i++) {
            //System.out.println((allVisualCoordinates.get(visualIndex + (i * MAX_VISUAL_INDEX))));
            Affine t = g.getTransform();

            g.translate(allVisualCoordinates.get(visualIndex + (i * MAX_VISUAL_INDEX)).getX() * scaleX - IMG.getWidth() / 2,
                    g.getCanvas().getHeight() - allVisualCoordinates.get(visualIndex + (i * MAX_VISUAL_INDEX)).getY() * scaleY - IMG.getHeight() / 2);
            g.rotate(-((allVisualCoordinates.get(visualIndex + (i * MAX_VISUAL_INDEX)).getZ() * 180) / Math.PI)%360);

            g.drawImage(IMG, 0, 0);
            //g.setFill(Color.ORANGE);
            //g.fillOval(-5, -5, 5, 5);
            g.setTransform(t);


        }

//        g.setFill(Color.ORANGE);
//        int x,y;
//
//        for (int i = 0; i < allVisualCoordinates.size(); i += 20) {
//            x = (int)(allVisualCoordinates.get(i).getX() * scaleX);
//            y = (int)(g.getCanvas().getHeight() - allVisualCoordinates.get(i).getY() * scaleY);
//            g.fillOval(x - 1, y - 1, x + 1, y + 1);
//        }
    }

    private void updateVisualize() {
        //if (visualIndex >= allVisualCoordinates.size())
        if (visualIndex >= MAX_VISUAL_INDEX)
            visualIndex = 0;

        visualIndex++;
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

        if (!VISUALIZE) {
            Affine t = g.getTransform();
            g.translate((coordinates.getX() - IMG.getWidth() / 2) * scaleX, (coordinates.getY() - IMG.getHeight() / 2) * scaleY);
            g.rotate(-azimuth);
            g.drawImage(IMG, 0, 0);
            g.setTransform(t);

            return;
        }

        drawVisualize(g, scaleX, scaleY);
    }

    /**
     * Porune strelu prislusnym smerem
     * Zjisti zda neupustila mapu ci nekoliduje s povrchem
     *
     * @param world ridici trida
     */
    @Override
    public void update(World world) {
        if (VISUALIZE) {
            updateVisualize();
            return;
        }

        //Point directionVector = direction.copy().mul(ACCELERATION);

        //System.out.println(direction);
        //System.out.println(directionVector.copy().div(ACCELERATION));

        coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));
        System.out.println(coordinates.getZ());

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
            return;
        }

        if (isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {
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

    public boolean getIsVisual() {
        return VISUALIZE;
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

    public ArrayList<Point> getAllCoordinates() {
        return allCoordinates;
    }

    public ArrayList<Point> getAllVisualCoordinates() {
        return allVisualCoordinates;
    }

    @Override
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void setCoordinates(double x, double y, double z) {
        this.coordinates = new Point(x, y, z);
    }

    public void setAllCoordinates(World world) {
        this.allCoordinates = new ArrayList<>();
        this.allVisualCoordinates = new ArrayList<>();

        while (!isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())
                && !isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {

            coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));

            this.allCoordinates.add(coordinates.copy());
            double x = Math.sqrt(coordinates.getX() * coordinates.getX() + coordinates.getY() * coordinates.getY());
            this.allVisualCoordinates.add(new Point(x, coordinates.getZ(), direction.getZ()));

            Point temp2 = new Point(0, 0, 0).sub(direction.copy());

            direction = direction.copy().add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));

            int aCoordSize = allVisualCoordinates.size();

            if (aCoordSize > 1) {
                allVisualCoordinates.get(aCoordSize - 1).setZ(
                        Math.tan((allVisualCoordinates.get(aCoordSize - 2).getY() - allVisualCoordinates.get(aCoordSize - 1).getY()) /
                                (allVisualCoordinates.get(aCoordSize - 2).getX() - allVisualCoordinates.get(aCoordSize - 1).getX())));
            }

            //System.out.println("x: " + allVisualCoordinates.get(aCoordSize - 1).getX() + " y: " + allVisualCoordinates.get(aCoordSize - 1).getY());
        }
        coordinates = allVisualCoordinates.get(0);

        visualIndex = 0;
    }
}
