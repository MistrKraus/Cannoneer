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
    void draw(GraphicsContext g);

    /**
     * Vrati vysku
     *
     * @return vyska
     */
    int getHeight();

    /**
     * Vrati sirku
     *
     * @return sirka
     */
    int getWidth();

    /**
     * Vrati instanci tridy {@code Point} s aktualni pozici instance
     *
     * @return instance tridy {@code Point} s aktualni pozici instance
     */
    Point getPoint();

    void setPoint(Point point);

    void setPoint(double x, double y, double z);
}
