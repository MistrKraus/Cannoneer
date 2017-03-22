import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;


/**
 * Created by kraus on 20.03.2017.
 */
public class Explosion implements IDrawable {

    private Point coordinates;
    private double radius;

    public Explosion(Point coordinates, double radius) {
        this.coordinates = coordinates.copy();
        this.radius = radius;
    }

    public void explode(World world) {
        world.getPlayers().stream().filter(player -> player.isInRadius(coordinates, radius, player.getCoordinates()))
            .forEach(player -> player.dealtDamage(100));

        world.getTargets().stream().filter(target -> target.isInRadius(coordinates, radius, target.getCoordinates()))
            .forEach(target -> target.dealtDamage(100));
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        g.setFill(Color.ORANGE);
        g.fillOval((coordinates.getX() - radius / 2) * scaleMperPixelX, (coordinates.getY() - radius / 2) * scaleMperPixelY,
                radius * scaleMperPixelX, radius * scaleMperPixelY);
    }

    @Override
    public void update(World world) {

    }

    @Override
    public double getHeight() {
        return radius;
    }

    @Override
    public double getWidthX() {
        return radius;
    }

    @Override
    public double getWidthY() {
        return radius;
    }

    @Override
    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public double getX() {
        return coordinates.getX();
    }

    @Override
    public double getY() {
        return coordinates.getY();
    }

    @Override
    public double getZ() {
        return coordinates.getZ();
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void setCoordinates(Point point) {

    }

    @Override
    public void setCoordinates(double x, double y, double z) {

    }
}
