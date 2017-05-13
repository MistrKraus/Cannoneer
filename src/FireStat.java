

/**
 * Created by kraus on 27.04.2017.
 */
public class FireStat {
    private final String playerName;
    private final String collideSpot;
    private final String windDirection;

    private final double windSpeed;
    private final double missileAzimuth;
    private final double missileElevation;
    private final double startX;
    private final double startY;
    private final double startZ;
    private final double missileSpeed;
    private final double collidingX;
    private final double collidingY;
    private final double collidingZ;

    public FireStat (String playerName, Point startCoord, Missile missile) {

        this.playerName = playerName;
        this.collideSpot = missile.getCollidingSpot();

        this.missileAzimuth = Math.round(missile.getAzimuth() * 100) / 100;
        this.missileElevation = Math.round(missile.getElevation() * 100) / 100;
        this.missileSpeed = Math.round(missile.getACCELERATION() * 100) / 100;

        this.startX = Math.round(startCoord.getX() * 100) / 100;
        this.startY = Math.round(startCoord.getY() * 100) / 100;
        this.startZ = Math.round(startCoord.getZ() * 100) / 100;

        this.collidingX = Math.round(missile.getCoordinates().getX() * 100) / 100;
        this.collidingY = Math.round(missile.getCoordinates().getY() * 100) / 100;
        this.collidingZ = Math.round(missile.getCoordinates().getZ() * 100) / 100;

        double wDir = 0;
        double wSpeed = 0;

        for (Wind wind :
                missile.getWinds()) {
            wDir += wind.getAzimuth();
            wSpeed += wind.getSpeed();
        }
        wDir /= missile.getWinds().size();
        windSpeed = Math.round((wSpeed / missile.getWinds().size()) * 100) / 100;


        if (wDir >= 330 || wDir < 30) {
            windDirection = "Vychod";
            return;
        }

        if (wDir >= 30 && wDir < 60) {
            windDirection = "Jiho-vychod";
            return;
        }

        if (wDir >= 60 && wDir < 120) {
            windDirection = "Jih";
            return;
        }

        if (wDir >= 120 && wDir < 150) {
            windDirection = "Jiho-zapad";
            return;
        }

        if (wDir >= 150 && wDir < 210) {
            windDirection = "Zapad";
            return;
        }

        if (wDir >= 210 && wDir < 240) {
            windDirection = "Severo-zapad";
            return;
        }

        if (wDir >= 240 && wDir < 300) {
            windDirection = "Sever";
            return;
        }

        if (wDir >= 300 && wDir < 330) {
            windDirection = "Severo-vychod";
            return;
        }

        windDirection = "---";
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getMissileAzimuth() {
        return missileAzimuth;
    }

    public double getMissileElevation() {
        return missileElevation;
    }

    public double getMissileSpeed() {
        return missileSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getCollideSpot() {
        return collideSpot;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getStartZ() {
        return startZ;
    }

    public double getCollidingX() {
        return collidingX;
    }

    public double getCollidingY() {
        return collidingY;
    }

    public double getCollidingZ() {
        return collidingZ;
    }
}
