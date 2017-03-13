import java.util.DoubleSummaryStatistics;

/**
 * Created by kraus on 12.03.2017.
 *
 * Souradnice na mape (v metrech)
 */
public class Point {
    /** x-ova souradnice v metrech*/
    private double x;
    /** y-ova souradnice v metrch*/
    private double y;
    /** nadmorska vyska v metech*/
    private double z;

    /**
     * Konstruktor souradnice (bodu) na mape
     *
     * @param x x-ova souradnice v metrech
     * @param y y-ova souradnice v metrch
     * @param z nadmorska vyska v metech
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Vraci vzdalenost bodu
     *
     * @param point bod, vuci kteremu porovnava vzdalenost
     * @return vzdalenost bodu v metrech
     */
    public double getPointsDistance(Point point) {
        double x = point.getX() - this.getX();
        double y = point.getY() - this.getY();
        double z = point.getZ() - this.getZ();

        return (Math.sqrt(x*x + y*y + z*z));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
