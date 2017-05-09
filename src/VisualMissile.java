import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

import java.util.ArrayList;

/**
 * Created by kraus on 25.04.2017.
 */
public class VisualMissile extends Missile {

    private double maxImgWidth;
    private double maxImgHeight;
    private double posX;
    private double posY;
    private double rotate;
    private double maxHeight = -1;

    private Point visualCollidingPoint;
    private Point visualCoordinates;
    private Point currentVisualCoord;

    private final double startX;

    private ArrayList<MissileCopyingMessenger> missileCopyingMessengerList;

    /**
     * konstruktor
     * provede potrebnou matematiku pro pohyb rakety
     *
     * @param coordinates souradnice v metrech
     * @param azimuth aizmut ve stupnich
     * @param elevation zdvih dela ve stupnich
     * @param acceleration rychlost strely v metrech za sekundu
     * @param maxImgWidth maximalni sirka vykreslene vizualizace
     * @param maxImgHeight maximalni vyska vykreslene vizualizase
     * @param posX pocatecni x-ova souradnice (v pixelech)
     * @param posY pocatecni y-ova souradnice (v pixelech)
     * @param firstVisualMissile jedna se o prvni instanci visualizovane strely
     * @param world reference na svet
     */
    public VisualMissile(Point coordinates, double azimuth, double elevation, double acceleration, double maxImgWidth,
                         double maxImgHeight, double posX, double posY, boolean firstVisualMissile, World world) {
        super(coordinates, azimuth, elevation, acceleration);

        this.maxImgWidth = maxImgWidth;
        this.maxImgHeight = maxImgHeight;
        this.posX = posX;
        this.posY = posY;

        double x = Math.sqrt(coordinates.getX() * coordinates.getX() + coordinates.getY() * coordinates.getY());

        //double rotate = Math.tan((visualCoordinates.getY() - coordinates.getZ()) / (visualCoordinates.getX() - x));

        currentVisualCoord = new Point(x, coordinates.getZ(), elevation);
        visualCoordinates = currentVisualCoord.copy();

        startX = visualCoordinates.getX();

        if (colliding)
            return;

        setCollidingPoint(world, firstVisualMissile);

        if (firstVisualMissile) {
            for (MissileCopyingMessenger startCoord :
                    missileCopyingMessengerList) {

                if (startCoord.coordinates.equals(coordinates))
                    continue;

                VisualMissile visualMissile = new VisualMissile(startCoord.coordinates, azimuth, elevation,
                        acceleration, maxImgWidth, maxImgHeight, posX, posY, false, world);
                visualMissile.setDirection(startCoord.direction);
                world.addVisualMissile(visualMissile);
            }
        }

        //world.removeVisualMissile(this);
    }

    /**
     * vizualizuje strelu
     *
     * @param g graficky kontext, ktery nakresli instanci
     * @param scaleX hodnota, ktera po vynasobeni x-ove souradnice v metrech urci tuto souradnici v pixelech
     * @param scaleY hodnota, ktera po vynasobeni y-ove souradnice v metrech urci tuto souradnici v pixelech
     */
    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        double newRotate = currentVisualCoord.copy().getZ();

//        if (visualIndex > MAX_VISUAL_INDEX - 9)
//            visualIndex = MAX_VISUAL_INDEX;
//
//        if (visualIndex < 9)
//            visualRotate = -((allVisualCoordinates.get(0).getZ() * 180) / Math.PI)%360;

        Affine t = g.getTransform();

        System.out.println(startX);
        System.out.println("-----------");

        g.translate(posX + (visualCoordinates.getX() - startX) * scaleX - IMG.getWidth() / 2,
                 g.getCanvas().getHeight() - (posY + visualCoordinates.getY() * scaleY) - IMG.getHeight() / 2);

//            if (i > VISUALIZED_MISSILES_COUNT - visualRotate.length) {
//                double newRotate;
//                if (visualIndex < VISUALIZED_MISSILES_COUNT)
//                    newRotate = -((allVisualCoordinates.get(visualIndex + (VISUALIZED_MISSILES_COUNT
//                            - visualRotate.length * MAX_VISUAL_INDEX)).getZ() * 180) / Math.PI) % 360;
//                else
//                    newRotate = -((allVisualCoordinates.get(visualIndex + (i * MAX_VISUAL_INDEX)).getZ() * 180) / Math.PI) % 360;
//
//                if (Math.abs(visualRotate[i - (VISUALIZED_MISSILES_COUNT - visualRotate.length)] - newRotate) < 5)
//                    visualRotate[i - (VISUALIZED_MISSILES_COUNT - visualRotate.length)] = newRotate;
//
//                g.rotate(visualRotate[i - (VISUALIZED_MISSILES_COUNT - visualRotate.length)]);
//            } else

        //if (Math.abs(visualRotate - newRotate) < 10)

        if (newRotate >= -90 && newRotate <= 90)
            rotate = newRotate;

        //System.out.println(visualRotate);

        g.rotate(rotate);

        g.drawImage(IMG, 0, 0);
        //g.setFill(Color.ORANGE);
        //g.fillOval(0, 0, 5, 5);
        g.setTransform(t);
    }

    @Override
    public void update(World world) {
        if (colliding)
            return;

        visualCoordinates = currentVisualCoord.copy();

        coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));

        double x = Math.sqrt(coordinates.getX() * coordinates.getX() + coordinates.getY() * coordinates.getY());

        Point temp2 = new Point(0, 0, 0).sub(direction.copy());

        direction = direction.copy().add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));

        //System.out.println(rotate);
        currentVisualCoord = new Point(x, coordinates.getZ(), 0);

        //double rotate = Math.tan((visualCoordinates.getY() - coordinates.getZ()) / (visualCoordinates.getX() - x));
        double rotate = (Math.tan((visualCoordinates.getY() - currentVisualCoord.getY()) /
                (visualCoordinates.getX() - currentVisualCoord.getX())) * 180) / Math.PI;

        currentVisualCoord.setZ(-rotate);

        //System.out.print(String.format("(" + PORADI + ") %.2f m", + coordinates.getX()));

        if (isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())) {
            world.removeVisualMissile(this);
            //System.out.println("Mimo mapu");
            world.addVisualMissile(new VisualMissile(world.getPlayer().getCoordinates().copy(), azimuth,
                    elevation, ACCELERATION, maxImgWidth, maxImgHeight, posX, posY, false, world));

            colliding = true;
            return;
        }

        if (isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {
            world.removeVisualMissile(this);
            //System.out.println("Kolize s terenem:");
            world.addVisualMissile(new VisualMissile(world.getPlayer().getCoordinates().copy(), azimuth,
                    elevation, ACCELERATION, maxImgWidth, maxImgHeight, posX, posY, false, world));
            colliding = true;
        }
    }

    public Point getVisualCollidingPoint(World world) {
        if (visualCollidingPoint == null)
            setCollidingPoint(world, false);

        return visualCollidingPoint;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setStartingCoordLists(World world) {
        setCollidingPoint(world, true);
    }

    public void setCollidingPoint(World world, boolean setStartingCoords) {
        missileCopyingMessengerList = new ArrayList<>();

        if (setStartingCoords)
            missileCopyingMessengerList.add(new MissileCopyingMessenger(coordinates, direction));

        Point currentCoord = coordinates.copy();
        Point currentDirection = direction.copy();

        maxHeight = 0;
        int loopNuber = 0;
        while (!isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())
                && !isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {

            coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));
            Point temp2 = new Point(0, 0, 0).sub(direction.copy());

            direction = direction.copy().add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));

            //TODO ODKOMENTOVAT!!!
//            if (setStartingCoords) {
////                if (missileCopyingMessengerList.get(missileCopyingMessengerList.size() - 1)
////                        .coordinates.getPointsDistance(coordinates) > 200)
//                if (loopNuber++ > 150) {
//                    loopNuber = 0;
//                    missileCopyingMessengerList.add(new MissileCopyingMessenger(coordinates, direction));
//                }
//            }

            maxHeight = Math.max(maxHeight, coordinates.getZ());
        }
        collidingPoint = coordinates.copy();

        coordinates = currentCoord.copy();
        direction = currentDirection.copy();

        double x = Math.sqrt(coordinates.getX() * coordinates.getX() + coordinates.getY() * coordinates.getX());

        visualCollidingPoint = new Point(x, collidingPoint.getZ(), 0);

//        Point startCoord = coordinates.copy();
//
//        while (!isOutsideMap(world.getMap().getMapWidthM(), world.getMap().getMapHeightM())
//                && !isColliding(world.getMap().getSurface(), world.getScaleX(), world.getScaleY())) {
//
//            coordinates = coordinates.copy().add((direction.copy()).mul(DELTA_T));
//
//            Point temp2 = new Point(0, 0, 0).sub(direction.copy());
//
//            direction = direction.copy().add(TEMP1).add(temp2.mul(MAGIC_B * DELTA_T));
//
////            int aCoordSize = allVisualCoordinates.size();
////
////            if (aCoordSize > 1) {
////                allVisualCoordinates.get(aCoordSize - 1).setZ(
////                        Math.tan((allVisualCoordinates.get(aCoordSize - 2).getY() - allVisualCoordinates.get(aCoordSize - 1).getY()) /
////                                (allVisualCoordinates.get(aCoordSize - 2).getX() - allVisualCoordinates.get(aCoordSize - 1).getX())));
////
////                if (allCoordinates.get())
////            }
//
//            //System.out.println("x: " + allVisualCoordinates.get(aCoordSize - 1).getX() + " y: " + allVisualCoordinates.get(aCoordSize - 1).getY());
//        }
//
//        double x = Math.sqrt(coordinates.getX() * coordinates.getX() + coordinates.getY() * coordinates.getY());
//
//        collidingPoint = new Point(x, coordinates.getZ(), 0);
//
//        coordinates = startCoord.copy();
    }
}
