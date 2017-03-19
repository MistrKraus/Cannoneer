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
     * @return pozice "dopadu" strely
     */
    Missile fire(double azimuth, double elevation, double speed);

}
