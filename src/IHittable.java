/**
 * Created by kraus on 11.03.2017.
 *
 * Rozhrani pro objekty, ktere mohou byt zasazeny
 */
public interface IHittable {

    /**
     * okoli (v metrech), ktere muze byt srelou zasazeno
     */
    int STRIKE_RADIUS = 60;

    /**
     * Urci pocet zraneni, ktere cil utrpel
     *
     * @param strikeCoordinates pozice utocnika
     * @param targetCoordinates pozice cile
     * @return pocet zraneni
     */
    default int dealtDamage(Point strikeCoordinates, Point targetCoordinates)  {
        double distance = strikeCoordinates.getPointsDistance(targetCoordinates);

        if (distance <= (STRIKE_RADIUS / 2)) {
            return (STRIKE_RADIUS / 2) - (int)(distance - 1);
        }

        return 0;
    }

}
