import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by kraus on 12.03.2017.
 *
 * Prepravka s daty nactenymi ze vstupniho souboru
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

    /**
     * Konstruktor nacitajici vstupni soubor
     *
     * @param fileName nazev vstupniho souboru
     */
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

            if (mapHeight <= 0 || mapWidth <= 0) {
                System.out.println("Nactena data ze souboru jsou chybna!");
                return;
            }

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

    /**
     * Zjisti zda jsou nactena data spravna
     *
     * @return
     */
    public boolean dataConsistent() {
        return !(mapWidth <= 0 || mapHeight <= 0 || deltaXm <= 0 || deltaYm <= 0 ||
                shooterX < 0 || shooterY < 0 || targetX < 0 || targetY < 0);
    }

    /**
     * @return sirka mapy ve sloupcich
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * @return vyska mapy v radcich
     */
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * @return sirka jednoho sloupce
     */
    public double getDeltaXm() {
        return deltaXm;
    }

    /**
     * @return vyska jedne radky
     */
    public double getDeltaYm() {
        return deltaYm;
    }

    /**
     * @return sirka mapy v metrech
     */
    public double getMapWidthM() {
        return mapWidthM;
    }

    /**
     * @return vyska mapy v metrech
     */
    public double getMapHeightM() {
        return mapHeightM;
    }

    /**
     * @return vychozi x-ova pozice strelce ve sloupcich
     */
    public int getShooterX() {
        return shooterX;
    }

    /**
     * @return vychozi y-ova pozice strelce v radcich
     */
    public int getShooterY() {
        return shooterY;
    }

    /**
     * @return vychozi x-ova pozice strelce v metrech
     */
    public double getShooterXm() {
        return shooterX * deltaXm;
    }

    /**
     * @return vychozi y-ova pozice strelce v metrech
     */
    public double getShooterYm() {
        return shooterY * deltaYm;
    }

    /**
     * @return vychozi x-ova pozice cile ve sloupcich
     */
    public int getTargetX() {
        return targetX;
    }

    /**
     * @return vychozi y-ova pozie cile v radcich
     */
    public int getTargetY() {
        return targetY;
    }

    /**
     * @return vychozi x-ova pozice cile v metrech
     */
    public double getTargetXm() {
        return targetX * deltaXm;
    }

    /**
     * @return vychozi y-ova pozice cile v metrech
     */
    public double getTargetYm() {
        return targetY * deltaYm;
    }

    /**
     * @return dvourozmerne pole s vyskou terenu
     */
    public double[][] getTerrainZm() {
        return terrainZm;
    }

    /**
     * Zmeni vysku terenu na zadanych souradnicich
     *
     * @param x sloupec na mape
     * @param y radek na mape
     * @param value nova vyska terenu
     */
    public void setTerrainZm(int x, int y, double value) {
        terrainZm[x][y] = value;
    }
}
