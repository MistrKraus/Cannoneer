import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 11.03.2017.
 *
 * Hrac
 */
public class Player extends Target implements IShooting {

    public Player (double x, double y, double z) {
        super(x, y, z);
    }

    public Player (Point point) {
        super(point);
    }

    public Player (double x, double y, double z, int hp) {
        super(x, y, z, hp);
    }

    public Player(Point point, int hp) {
        super(point, hp);
    }

    @Override
    public Missile fire(double azimuth, double elevation, double speed) {
        return new Missile(coordinates, azimuth, elevation, speed);
    }

    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        g.setStroke(Color.BLUE);
        g.setLineWidth(1);
        g.strokeLine(getX() * scaleX - 5, getY() * scaleY, getX() * scaleX + 5, getY() * scaleY);
        g.strokeLine(getX() * scaleX, getY() * scaleY + 5, getX() * scaleX, getY() * scaleY - 5);
    }

    @Override
    public void update(World world) {

    }

    @Override
    public String toString() {
        return "Player: " +
                hp + " HP " +
                "[" + coordinates.getX() +
                ", " + coordinates.getY() +
                ", ]" + coordinates.getZ();
    }
}
