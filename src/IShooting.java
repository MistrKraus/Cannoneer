/**
 * Created by kraus on 12.03.2017.
 *
 * Rozhrani pro objekty, ktere mohou strilet ~ utocit
 */
public interface IShooting {

    /**
     * Vypocita pozici "dopadu" strely
     *
     * @param azimuth smer strely
     * @param elevation zdvih hlavne
     * @param speed rychlot strely
     * @param missile strela
     * @return pozice "dopadu" strely
     */
    default Point fire(double azimuth, double elevation, double speed, Missile missile) {
        Point strikeCoordinates = new Point(0, 0, 0);



        return strikeCoordinates;
    }
}
