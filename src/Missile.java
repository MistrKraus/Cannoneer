import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 18.03.2017.
 */
public class Missile implements IDrawable {

    private Point coordinates;
    private double strikeRadius;

    /**
     * vychozi okoli (v metrech), ktere muze byt srelou zasazeno
     */
    private final static double DEFAULT_STRIKE_RADIUS = 60;

    public Missile(double x, double y, double z) {
        this(new Point(x,y,z), DEFAULT_STRIKE_RADIUS);
    }

    public Missile(Point coordinates) {
        this(coordinates, DEFAULT_STRIKE_RADIUS);
    }

    public Missile(double x, double y, double z, double strikeRadius) {
        this(new Point(x,y,z), strikeRadius);
    }

    public Missile(Point coordinates, double strikeRadius) {
        this.coordinates = coordinates;
        this.strikeRadius = strikeRadius;
    }

    public double getStrikeRadius() {
        return strikeRadius;
    }

    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        g.setFill(Color.ORANGE);
        g.strokeOval(coordinates.getX() * scaleX, coordinates.getY() * scaleY, strikeRadius / 2, strikeRadius / 2);
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
