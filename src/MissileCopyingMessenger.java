/**
 * prepravka strely
 * uchovava informace potrebne pro vytvoreni presne kopie strely
 *
 * Created by kraus on 26.04.2017.
 */
public class MissileCopyingMessenger {
    public final Point coordinates;
    public final Point direction;

    public MissileCopyingMessenger(Point coordinates, Point direction) {
        this.coordinates = coordinates;
        this.direction = direction;
    }
}
