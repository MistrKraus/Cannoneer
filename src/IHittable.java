/**
 * Created by kraus on 11.03.2017.
 *
 * Rozhrani pro objekty, ktere mohou byt zasazeny
 */
public interface IHittable {

    /**
     * Urci pocet zraneni, ktere cil utrpel
     *
     * @param strikeCoordinates strela
     * @param targetCoordinates pozice cile
     * @return pocet zraneni
     */
    default int dealtDamage(Missile strikeCoordinates, Point targetCoordinates)  {
        double distance = strikeCoordinates.getCoordinates().getPointsDistance(targetCoordinates);

        if (distance <= (strikeCoordinates.getStrikeRadius() / 2)) {
            return (int)((strikeCoordinates.getStrikeRadius() / 2) - (distance - 1));
        }

        return 0;
    }

}
