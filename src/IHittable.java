/**
 * Created by kraus on 11.03.2017.
 *
 * Rozhrani pro objekty, ktere mohou byt zasazeny
 */
public interface IHittable extends IMappable, IDrawable {

    /**
     * Vraci informaci, zda byl target v radiusu
     *
     * @param coordinates souradnice, okolo kterych testuje pritomnost targetu
     * @param radius okoli
     * @param targetCoordinates souradnice potencialniho cile
     * @return zda byl cil zasazen
     */
    default boolean isInRadius(Point coordinates, double radius, Point targetCoordinates) {
        return (coordinates.getPointsDistance(targetCoordinates) < radius);
    }

    int getHp();

    double getMapX();

    double getMapY();

    /**
     * Urci pocet zraneni, ktere cil utrpel
     */
    void dealtDamage(int dmg);

}
