import javafx.scene.canvas.GraphicsContext;

/**
 * Created by kraus on 18.03.2017.
 */
public interface IDrawable {

    /**
     * Pomoci dodaneho grafickeho kontextu vykresli obraz sve instance
     *
     * @param g graficky kontext, ktery nakresli instanci
     */
    void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY);

    void update(World world);

    /**
     * Vrati vysku
     *
     * @return vyska
     */
    double getHeight();

    /**
     * Vrati sirku na x-ove ose
     *
     * @return sirka na x-ove ose
     */
    double getWidthX();

    /**
     * Vrati sirku na y-ove ose
     *
     * @return sirka na y-ove ose
     */
    double getWidthY();

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

    void setCoordinates(Point point);

    void setCoordinates(double x, double y, double z);
}
