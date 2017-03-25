import javafx.scene.canvas.GraphicsContext;

/**
 * Created by kraus on 18.03.2017.
 *
 * Interface pro kreslitelne objekty
 */
public interface IDrawable {

    /**
     * Pomoci dodaneho grafickeho kontextu vykresli obraz sve instance
     *
     * @param g graficky kontext, ktery nakresli instanci
     */
    void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY);

    /**
     * aktualizuje objekt
     *
     * @param world ridici trida
     */
    void update(World world);

    /**
     * Vrati vysku v metrech
     *
     * @return vyska v metrech
     */
    double getHeight();

    /**
     * Vrati sirku na x-ove ose v metrech
     *
     * @return sirka na x-ove ose v metrech
     */
    double getWidthX();

    /**
     * Vrati sirku na y-ove ose v metrech
     *
     * @return sirka na y-ove ose v metrech
     */
    double getWidthY();
}
