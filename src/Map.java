import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kraus on 24.03.2017.
 */
public class Map implements IDrawable {
    private double[][] terrain;
    private double[][] surface;

    private int mapWidth;
    private int mapHeight;

    private double mapWidthM;
    private double mapHeightM;
    private double deltaXm;
    private double deltaYm;
    private double maxHeightM;
    private double minHeightM;
    private double midHeightM;

    private ImageView terrainImgView = new ImageView();
    private Image terrainImg;
    WritableImage wImage;

    private final int IMG_HEIGHT;

    private final Point[] RGB_SCALE = new Point[4];

    public Map(double[][] terrainZm, int mapWidth, int mapHeight, double deltaXm, double deltaYm) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.deltaXm = deltaXm;
        this.deltaYm = deltaYm;
        this.mapWidthM = mapWidth * deltaXm;
        this.mapHeightM = mapHeight * deltaYm;

        IMG_HEIGHT = 1000;

        terrain = new double[mapWidth][mapHeight];
        surface = new double[mapWidth][mapHeight];

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                terrain[i][j] = terrainZm[i][j];
                surface[i][j] = terrainZm[i][j];
            }
        }

        minHeightM = surface[0][0];
        maxHeightM = minHeightM;

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                double height = surface[i][j];
                minHeightM = Math.min(minHeightM, height);
                maxHeightM = Math.max(maxHeightM, height);
            }
        }

        System.out.println(minHeightM);
        System.out.println(maxHeightM);

        setMaxMinMidVaule();
    }

    public void bufferImage() {
        WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * (mapWidthM / mapHeightM)), IMG_HEIGHT);
        double scale = 255 / maxHeightM;

        double colorSection = (maxHeightM - minHeightM) / RGB_SCALE.length;
        setupColorScale(colorSection);

//        double indexX = mapWidth / terrainImgW.getWidth();
//        double indexY = mapHeight / terrainImgW.getHeight();

        double pXPerDelta = (terrainImgW.getWidth() / terrain.length);
        double pYPerDelta = (terrainImgW.getHeight() / terrain[1].length);

        PixelWriter pixelWriter = terrainImgW.getPixelWriter();

        int rgb;
        double x, y, height;
        Color color;
        Random r = new Random();

        long now = System.nanoTime();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                height = terrain[i][j];
                rgb = (int)(height * scale);
                color = Color.rgb(rgb, rgb, rgb);

                // vyber barvy
                for (int k = 0; k < RGB_SCALE.length; k++) {
                    if (height <= (minHeightM + ((k + 1) * colorSection))) {
                        System.out.println((minHeightM + colorSection * (k + 1)) - height);
                        Point rgbPoint = RGB_SCALE[k].copy().mul((minHeightM + colorSection * (k + 1)) - height);

                        color = Color.rgb((int)rgbPoint.getX(), (int)rgbPoint.getY(), (int)rgbPoint.getZ());
                        //System.out.println(rgbPoint.getX());

                        if (color.equals(Color.BLACK))
                            color = Color.rgb(210, 178, 0);

                        break;
                    }
                }

                x = i * pXPerDelta;
                y = j * pYPerDelta;

                // nastaveni barvy
                for (int k = 0; k < pXPerDelta; k++) {
                    for (int l = 0; l < pYPerDelta; l++) {
                        if (minHeightM == maxHeightM)
                            color = Color.rgb(0, r.nextInt(90) + 110, 0);

                        pixelWriter.setColor((int)(x + k), (int)(y + l), color);
                    }
                }
            }
        }

        System.out.println("Kresleni terenu = " + (System.nanoTime() - now) + " ns");
        System.out.println("Obraz mapy aktualizovan");

        terrainImgView.setImage(terrainImgW);
        terrainImg = terrainImgView.getImage();
        wImage = new WritableImage(terrainImg.getPixelReader(), (int)terrainImg.getWidth(), (int)terrainImg.getHeight());

        //System.out.println("Sirka: " + terrainImg.getWidth() +"\nVyska: " + terrainImg.getHeight());
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        terrainImg = wImage;
        g.setEffect(new GaussianBlur(3));
        g.drawImage(terrainImg, 0, 0, g.getCanvas().getWidth() + 1, g.getCanvas().getHeight() + 1);
        g.setEffect(null);
    }

    @Override
    public void update(World world) {

    }

    /**
     * @return maximalni nadmorska vyska povrchu
     */
    @Override
    public double getHeight() {
        return maxHeightM;
    }

    @Override
    public double getWidthX() {
        return mapWidthM;
    }

    @Override
    public double getWidthY() {
        return mapHeightM;
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

    public double[][] getTerrain() {
        return terrain;
    }

    public double[][] getSurface() {
        return surface;
    }

    private void setMaxMinMidVaule() {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (maxHeightM < surface[i][j])
                    maxHeightM = surface[i][j];
                if (minHeightM > surface[i][j])
                    minHeightM = surface[i][j];
            }
        }

        midHeightM = (maxHeightM - minHeightM) / 2;
    }

    public void setTerrain(Explosion explosion) {
    }

    public void addToMap(IHittable object) {
        double columnCount = object.getWidthX() / deltaXm;
        double rowCount = object.getHeight() / deltaYm;

        int x = (int)(object.getX() / deltaXm);
        int y = (int)(object.getY() / deltaYm);

        if (columnCount < 1)
            columnCount = 1;

        if (rowCount < 1)
            rowCount = 1;

        for (int i = (int)-Math.floor(columnCount / 2); i < (int)Math.ceil(columnCount / 2); i ++)
            for (int j = (int)-Math.floor(rowCount / 2); j < (int)Math.ceil(rowCount / 2); j++)
                surface[x + i][y + j] += object.getHeight();
    }

    public void removeFromMap(IHittable object) {
        double columnCount = object.getWidthX() / deltaXm;
        double rowCount = object.getHeight() / deltaYm;

        int x = (int)(object.getX() / deltaXm);
        int y = (int)(object.getY() / deltaYm);

        if (columnCount < 1)
            columnCount = 1;

        if (rowCount < 1)
            rowCount = 1;

        for (int i = (int)-Math.floor(columnCount / 2); i < (int)Math.ceil(columnCount / 2); i ++)
            for (int j = (int)-Math.floor(rowCount / 2); j < (int)Math.ceil(rowCount / 2); j++)
                surface[x + i][y + j] -= object.getHeight();
    }

    private void setupColorScale(double colorSection) {
        RGB_SCALE[0] = new Point(209 / (colorSection), 178 / (colorSection) ,0);
        RGB_SCALE[1] = new Point(0, 147 / ((colorSection )), 0);
        RGB_SCALE[2] = new Point(86 / ((colorSection)),
                55 / ((colorSection)),0);
        RGB_SCALE[3] = new Point(255 / ((colorSection)),
                255 / ((colorSection)), 255 / ((colorSection)));
    }
}
