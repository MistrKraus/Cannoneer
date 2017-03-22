import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by kraus on 18.03.2017.
 */
public class Missile implements IDrawable {

    private Point coordinates;
    private Point speed;
    private double strikeRadius;

    private final double acceleration;
    /**
     * vychozi okoli (v metrech), ktere muze byt srelou zasazeno
     */
    private static final double DEFAULT_STRIKE_RADIUS = 60;
    private static final Point MAGIC_POINT = new Point(0,0,-1);
    private static final double GRAVITY = 10;
    private static final double DELTA_T = 0.01;
    private static final double MAGIC_B = 0.05;

    public Missile(double x, double y, double z, double azimuth, double elevation, double speed) {
        this(new Point(x,y,z), azimuth, elevation, speed, DEFAULT_STRIKE_RADIUS);
    }

    public Missile(Point coordinates, double azimuth, double elevation, double speed) {
        this(coordinates, azimuth, elevation, speed, DEFAULT_STRIKE_RADIUS);
    }

    public Missile(double x, double y, double z, double azimuth, double elevation, double speed, double strikeRadius) {
        this(new Point(x,y,z), azimuth, elevation, speed, strikeRadius);
    }

    public Missile(Point coordinates, double azimuth, double elevation, double speed, double strikeRadius) {
        this.coordinates = coordinates.copy().subZ(0.3);
        this.strikeRadius = strikeRadius;
        this.acceleration = speed;

        if (azimuth < 0)
            azimuth += 360;

        azimuth = Math.PI * azimuth / 180;

        double speedX = Math.cos(azimuth);
        double speedY = Math.sin(azimuth) * -1;

        if (elevation < 0)
            elevation += 360;

        elevation = Math.PI * elevation / 180;

        double speedZ = Math.sin(elevation);
        // TODO strelba
        speedZ = 0;

        this.speed = new Point(speedX, speedY, speedZ);
    }

    public double getStrikeRadius() {
        return strikeRadius;
    }

    public boolean isColliding(double[][] surface, double scaleX, double scaleY, Data data) {
        int iX; // = (int)(coordinates.getX() * scaleX);
        int iY; // = (int)(coordinates.getY() * scaleY);

        //return coordinates.getZ() <= surface[iX][iY];

        for (int i =  -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                iX = (int)((coordinates.getX() + i * 2) * scaleX);
                iY = (int)((coordinates.getY() + j * 2) * scaleY);

                if (iX < 0 || iX >= surface.length || iY < 0 || iY >= surface[0].length)
                    continue;

                if (coordinates.getZ() <= surface[iX][iY])
                    return true;
            }
        }

        return false;
    }

    public boolean isOutsideMap(Data data) {
        return (coordinates.getX() < 0 || coordinates.getY() < 0 ||
            coordinates.getX() > data.getMapWidthM() || coordinates.getY() > data.getMapHeightM());
    }

    @Override
    public void draw(GraphicsContext g, double scaleX, double scaleY) {
        g.setFill(Color.ORANGE);
        g.fillOval((coordinates.getX() - strikeRadius / 2) * scaleX, (coordinates.getY() - strikeRadius / 2) * scaleY, 3, 3);
    }

    @Override
    public void update(World world) {
        // vypocet pozice bez vetru
        //coordinates = coordinates.copy().add((speed.add((MAGIC_POINT.copy().mul(GRAVITY * DELTA_T))).add((new Point(0,0,0).sub(acceleration)).mul(MAGIC_B * DELTA_T))));
//        Point v1 = MAGIC_POINT.copy().mul(GRAVITY * DELTA_T);
//        Point v2 = new Point(0,0,0).sub(speed);
//        Point v3 = v2.copy().mul(MAGIC_B*DELTA_T);

        coordinates = coordinates.copy().add(speed.copy().mul(acceleration));

        //System.out.println(coordinates);

        if (isOutsideMap(world.getData())) {
            world.removeMissile(this);
            return;
        }

        if (isColliding(world.getSurface(), world.getScaleX(), world.getScaleY(), world.getData())) {
            world.removeMissile(this);

            Explosion explosion = new Explosion(coordinates, strikeRadius);
            world.addExplosion(explosion);
            explosion.explode(world);

            System.out.println("Bum!");
        }
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidthX() {
        return 0.5;
    }

    @Override
    public double getWidthY() {
        return 0.5;
    }

    @Override
    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void setCoordinates(double x, double y, double z) {
        this.coordinates = new Point(x, y, z);
    }
}
