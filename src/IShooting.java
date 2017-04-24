/**
 * Created by kraus on 12.03.2017.
 *
 * Rozhrani pro objekty, ktere mohou strilet
 */
public interface IShooting {

    /**
     * vytvori strelu
     *
     * @param azimuth smer strely
     * @param elevation zdvih hlavne
     * @param speed rychlot strely
     * @return strela
     */
    Missile fire(double azimuth, double elevation, double speed);

    /**
     * vytvori strelu
     *
     * @param azimuth smer strely
     * @param elevation zdvih hlavne
     * @param speed rychlot strely
     * @param visual ma se strela pouze vyzualizovat
     * @param world reference na svet
     * @return strela
     */
    Missile fire(double azimuth, double elevation, double speed, boolean visual, World world);

}
