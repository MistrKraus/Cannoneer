/**
 * Created by kraus on 24.03.2017.
 */
public interface IMappable {

    /**
     * Vrati instanci tridy {@code Point} s aktualni pozici instance
     *
     * @return instance tridy {@code Point} s aktualni pozici instance
     */
    Point getCoordinates();

    /**
     * vraci x-ovou souradnici
     *
     * @return x-ova souradnice
     */
    default double getX() {
        return getCoordinates().getX();
    }

    /**
     * vraci y-ovou souradnici
     *
     * @return y-ova souradnice
     */
    default double getY() {
        return getCoordinates().getY();
    }

    /**
     * vraci nadmorskou vysku
     *
     * @return nadmorska vyska
     */
    default double getZ() {
        return getCoordinates().getZ();
    }

    /**
     * Nastavi nove souradnice
     *
     * @param point souradnice v metrech
     */
    void setCoordinates(Point point);

    /**
     * Nastavi nove souradnice
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnie v metrech
     * @param z z-ova souradnice v metrech
     */
    void setCoordinates(double x, double y, double z);
}
