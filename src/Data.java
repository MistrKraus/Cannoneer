import com.sun.deploy.util.StringUtils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by kraus on 12.03.2017.
 */
public class Data {

    private int mapWidth;
    private int mapHeight;
    private double deltaXm;
    private double deltaYm;
    private double mapWidthM;
    private double mapHeightM;
    private int shooterX;
    private int shooterY;
    private int targetX;
    private int targetY;

    private double[][] terrainZm;

    public Data(String fileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            this.mapWidth = in.readInt();
            this.mapHeight = in.readInt();
            this.deltaXm = in.readInt() / 1000;
            this.deltaYm = in.readInt() / 1000;
            this.mapWidthM = mapWidth * deltaXm;
            this.mapHeightM = mapHeight * deltaYm;
            this.shooterX = in.readInt();
            this.shooterY = in.readInt();
            this.targetX = in.readInt();
            this.targetY = in.readInt();

            terrainZm = new double[mapWidth][mapHeight];

            int zCounter = 0;

            try {
                for (int i = 0; i < mapWidth; i++) {
                    for (int j = 0; j < mapHeight; j++) {
                        zCounter++;
                        terrainZm[i][j] = in.readInt() / 1000;
                        //System.out.print(terrainZm[i][j] + "   ");
                    }
                    //System.out.println();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("V souboru chybí " + (mapWidth * mapWidth - zCounter) + " hodnot s nadmořskou výškou");
                System.out.println("Tyto hodnoty byli nastaveny na 0 mm");
            }
        } catch (IOException e) {
            System.out.println("Při čtení vstupního souboru '" + fileName + "' došlo k chybě!");
            e.printStackTrace();

            System.out.println("Ujistěte se, že ve složce src/resources/ je vámi požadovaný soubor s mapou" +
                    " a spusťte aplikaci znovu se správným jeho názvem souboru (včetně koncovky .ter), případně bez paramaerů" +
                    " - mapa bude vybrána automaticky");
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

    public double getDeltaXm() {
        return deltaXm;
    }

    public double getDeltaYm() {
        return deltaYm;
    }

    public double getMapWidthM() {
        return mapWidthM;
    }

    public double getMapHeightM() {
        return mapHeightM;
    }

    public int getShooterX() {
        return shooterX;
    }

    public int getShooterY() {
        return shooterY;
    }

    public double getShooterXm() {
        return shooterX * deltaXm;
    }

    public double getShooterYm() {
        return shooterY * deltaYm;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public double getTargetXm() {
        return targetX * deltaXm;
    }

    public double getTargetYm() {
        return targetY * deltaYm;
    }

    public double[][] getTerrainZm() {
        return terrainZm;
    }

    public void setTerrainZm(int x, int y, double value) {
        terrainZm[x][y] = value;
    }
}
