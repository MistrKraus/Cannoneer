/**
 * Created by kraus on 12.03.2017.
 *
 * bod se tremi souradnicemi
 */
public class Point {
    private double x;
    private double y;
    private double z;

    /**
     * Konstruktor
     *
     * @param x
     * @param y
     * @param z
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * prezizeni konstruktoru
     *
     * @param other
     */
    public Point(Point other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    /**
     * @return kopie konstruktoru bodu
     */
    public Point copy() {
        return new Point(this);
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

    /**
     * vrati x
     *
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * @return z
     */
    public double getZ() {
        return z;
    }

    /**
     * pricte k jednotlivym slozkam slozky jineho {@code Point}
     *
     * @param other {@code Point}
     * @return bod posunuty o hodnoty jineho bodu
     */
    public Point add(Point other) {
        x += other.x;
        y += other.y;
        z += other.z;

        return this;
    }

    public Point add(double x) {
        this.x += x;
        this.y += x;
        this.z += x;

        return this;
    }

    public Point addX(double x) {
        this.x += x;

        return this;
    }

    public Point addY(double y) {
        this.y += y;

        return this;
    }

    public Point addZ(double z) {
        this.z += z;

        return this;
    }

    public Point sub(Point other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;

        return this;
    }

    public Point sub(double x) {
        this.x -= x;
        this.y -= x;
        this.z -= x;

        return this;
    }

    public Point subX(double x) {
        this.x -= x;

        return this;
    }

    public Point subY(double y) {
        this.y -= y;

        return this;
    }

    public Point subZ(double z) {
        this.z -= z;

        return this;
    }

    public Point mul(Point other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;

        return this;
    }

    public Point mul(double x) {
        this.x *= x;
        this.y *= x;
        this.z *= x;

        return this;
    }

    public Point mulX(double x) {
        this.x *= x;

        return this;
    }

    public Point mulY(double y) {
        this.y *= y;

        return this;
    }

    public Point mulZ(double z) {
        this.z *= z;

        return this;
    }

    public Point div(Point other) {
        x /= other.x;
        y /= other.y;
        z /= other.z;

        return this;
    }

    public Point div(double x) {
        this.x /= x;
        this.y /= x;
        this.z /= x;

        return this;
    }

    public Point divX(double x) {
        this.x /= x;

        return this;
    }

    public Point divY(double y) {
        this.y /= y;

        return this;
    }

    public Point divZ(double z) {
        this.z /= z;

        return this;
    }

    public Point setX(double x) {
        this.x = x;

        return this;
    }

    public Point setY(double y) {
        this.y = y;

        return this;
    }

    public Point setZ(double z) {
        this.z = z;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;
        return Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
