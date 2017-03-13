import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by kraus on 12.03.2017.
 */
public class Data {

    private int mapWidth;
    private int mapHeight;
    private int deltaXm;
    private int deltaYm;
    /** sloupec*/
    private int shooterX;
    /** radek*/
    private int shooterY;
    private int targetX;
    private int targetY;

    private int[][] terrainZ;

    public Data(String fileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            this.mapWidth = in.readInt();
            this.mapHeight = in.readInt();
            this.deltaXm = in.readInt() / 1000;
            this.deltaYm = in.readInt() / 1000;
            this.shooterX = in.readInt();
            this.shooterY = in.readInt();
            this.targetX = in.readInt();
            this.targetY = in.readInt();

            terrainZ = new int[mapWidth][mapHeight];

            for (int i = 0; i < mapWidth; i++) {
                for (int j = 0; j < mapHeight; j++) {
                    terrainZ[i][j] = in.readInt() / 1000;
                    //System.out.println(terrainZ[i][j]);
                }
            }
        } catch (IOException e) {
            System.out.println("Při četení vstupního souboru '" + fileName + "' došlo k chybě!");
            e.printStackTrace();
        }
    }

    public boolean dataConsistent() {
        return !(mapWidth <= 0 || mapHeight <= 0 || deltaXm <= 0 || deltaYm <= 0 ||
                shooterX < 0 || shooterY < 0 || targetX < 0 || targetY < 0);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getDeltaXm() {
        return deltaXm;
    }

    public int getDeltaYm() {
        return deltaYm;
    }

    public int getShooterX() {
        return shooterX;
    }

    public int getShooterY() {
        return shooterY;
    }

    public int getShooterXm() {
        return shooterX * deltaXm;
    }

    public int getShooterYm() {
        return shooterY * deltaYm;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public int getTargetXm() {
        return targetX * deltaXm;
    }

    public int getTargetYm() {
        return targetY * deltaYm;
    }

    public int[][] getTerrainZ() {
        return terrainZ;
    }
}
