/**
 * Created by kraus on 27.04.2017.
 */
public class FireStat {
    private final String playerName;
    private final String missileAzimuth;
    private final String missileElevation;
    private final String missileSpeed;
    private final String windDirection;
    private final String windSpeed;
    private final String collideSpot;

    private final Point startCoord;
    private final Point collidingCoord;

    public FireStat (String playerName, String missileAzimuth, String missileElevation,
                     String missileSpeed, String windDirection, String windSpeed, String collideSpot,
                        Point startCoord, Point collidingCoord) {

        this.playerName = playerName;
        this.missileAzimuth = missileAzimuth;
        this.missileElevation = missileElevation;
        this.missileSpeed = missileSpeed;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.collideSpot = collideSpot;

        this.startCoord = startCoord;
        this.collidingCoord = collidingCoord;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMissileAzimuth() {
        return missileAzimuth;
    }

    public String getMissileElevation() {
        return missileElevation;
    }

    public String getMissileSpeed() {
        return missileSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCollideSpot() {
        return collideSpot;
    }

    public Point getStartCoord() {
        return startCoord;
    }

    public Point getCollidingCoord() {
        return collidingCoord;
    }
}
