import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 18.03.2017.
 */
public class Missile implements IDrawable {

    private Point coordinates;
    private double strikeRadius;
    private double azimuth;
    private double elevation;
    private double speed;

    /**
     * vychozi okoli (v metrech), ktere muze byt srelou zasazeno
     */
    private final static double DEFAULT_STRIKE_RADIUS = 60;

    public Missile(double x, double y, double z, double azimuth, double elevation, double speed) {
        this(new Point(x,y,z), azimuth, elevation, speed, DEFAULT_STRIKE_RADIUS);
    }

    public Missile(Point coordinates, double azimuth, double elevation, double speed) {
        this(coordinates, azimuth, elevation, speed, DEFAULT_STRIKE_RADIUS);
    }

    public Missile(double x, double y, double z, double azimuth, double elevation, double speed, double strikeRadius) {
        this(new Point(x,y,z), azimuth, elevation, speed, strikeRadius);
    }

    public Missile(Point coordinates, double azimuth, double elevation, double speed, double strikeRadius) {
        this.coordinates = coordinates.copy();
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.speed = speed;
        this.strikeRadius = strikeRadius;
    }

    public double getStrikeRadius() {
        return strikeRadius;
    }

    public boolean isColliding(double[][] surface, double scalePixelperMX, double scalePixelperMY) {
        return (coordinates.getZ() < surface[(int)(coordinates.getX() * scalePixelperMX)][(int)(coordinates.getY() * scalePixelperMY)]);
    }
/*
    public Point collidingPointM(double[][] surface, double scalePixelperMX, double scalePixelperMY) {
        for (int i = 0; i < 5; i++) {
            //coordinates()

            if (isColliding(surface, scalePixelperMX, scalePixelperMY)) {

            } else {

            }
        }
    }
*/
    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        g.setFill(Color.ORANGE);
        g.fillOval(coordinates.getX(), coordinates.getY(), 3, 3);

//        g.setFill(Color.ORANGE);
//        g.strokeOval(coordinates.getX() * scaleX, coordinates.getY() * scaleY, strikeRadius / 2, strikeRadius / 2);
    }

    @Override
    public void update(World world) {
        coordinates.add(new Point(speed, speed, 0));

        if (isColliding(world.getSurface(), world.getScalePixelperMX(), world.getScalePixelperMY())) {
            world.removeMissile(this);
        }
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
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
