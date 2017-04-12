import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by kraus on 12.03.2017.
 *
 * Prepravka s daty nactenymi ze vstupniho souboru
 */
public class Data {
    private int shooterX;
    private int shooterY;
    private int targetX;
    private int targetY;

    private Map map;

    private boolean dataConsistent = false;

    /**
     * Konstruktor nacitajici vstupni soubor
     *
     * @param fileName nazev vstupniho souboru
     */
    public Data(String fileName) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            int mapWidth = in.readInt();
            int mapHeight = in.readInt();
            double deltaXm = in.readInt() / 1000;
            double deltaYm = in.readInt() / 1000;
            //this.shooterX = 0;
            this.shooterX = in.readInt();
            //this.shooterY = 0;
            this.shooterY = in.readInt();
            this.targetX = in.readInt();
            this.targetY = in.readInt();

            double[][] terrainZm = new double[mapWidth][mapHeight];

            int zCounter = 0;

            if (mapHeight <= 0 || mapWidth <= 0) {
                System.out.println("Nactena data ze souboru jsou chybna!");
                return;
            }

            try {
                long now = System.nanoTime();
                for (int i = 0; i < mapWidth; i++) {
                    for (int j = 0; j < mapHeight; j++) {
                        zCounter++;
                        terrainZm[i][j] = in.readInt() / 1000;
                        terrainZm[i][j] = 1;

                        //System.out.print(terrainZm[i][j] + "   ");
                    }
                    //System.out.println();
                }

                System.out.println("Nacitani terenu = " + (System.nanoTime() - now) + " ns");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("V souboru chybí " + (mapWidth * mapWidth - zCounter) + " hodnot s nadmořskou výškou");
                System.out.println("Tyto hodnoty byli nastaveny na 0 mm");
            }

            map = new Map(terrainZm, mapWidth, mapHeight, deltaXm, deltaYm);

            dataConsistent = dataConsistent(mapWidth, mapHeight, deltaXm, deltaYm);
        } catch (IOException e) {
            System.out.println("Při čtení vstupního souboru '" + fileName + "' došlo k chybě!");
            e.printStackTrace();

            System.out.println("Ujistěte se, že ve složce src/resources/ je vámi požadovaný soubor s mapou" +
                    " a spusťte aplikaci znovu se správným jeho názvem souboru (včetně koncovky .terrainImg), případně bez paramaerů" +
                    " - mapa bude vybrána automaticky");
        }
    }

    /**
     * Zjisti zda jsou nactena data spravna
     *
     * @return
     */
    public boolean dataConsistent(double mapWidth, double mapHeight, double deltaXm, double deltaYm) {
        return !(mapWidth <= 0 || mapHeight <= 0 || deltaXm <= 0 || deltaYm <= 0 ||
                shooterX < 0 || shooterY < 0 || targetX < 0 || targetY < 0);
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
        return shooterX * map.getDeltaXm();
    }

    /**
     * @return vychozi y-ova pozice strelce v metrech
     */
    public double getShooterYm() {
        return shooterY * map.getDeltaYm();
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
        return targetX * map.getDeltaXm();
    }

    /**
     * @return vychozi y-ova pozice cile v metrech
     */
    public double getTargetYm() {
        return targetY * map.getDeltaYm();
    }

    /**
     * @return instance mapy {@code Map}
     */
    public Map getMap() {
        return map;
    }

    public boolean getDataConsistent() {
        return dataConsistent;
    }
}
