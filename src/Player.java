/**
 * Created by kraus on 11.03.2017.
 *
 * Hrac
 */
public class Player extends Target implements IShooting {

    public Player (Point point) {
        this(point, 100);
    }

    public Player(Point point, int hp) {
        super(point, hp);
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
