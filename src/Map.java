import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

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
    private double maxHeightM = 0;
    private double minHeightM = Double.MAX_VALUE;
    private double midHeightM;

    private ImageView terrainImgView = new ImageView();
    private Image terrainImg;
    WritableImage wImage;

    private final int IMG_HEIGHT;

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

        setMaxMinMidVaule();
    }

    public void bufferImage() {
        WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * (mapWidthM / mapHeightM)), IMG_HEIGHT);
        double scale = 255 / maxHeightM;

//        double indexX = mapWidth / terrainImgW.getWidth();
//        double indexY = mapHeight / terrainImgW.getHeight();

        double pXPerDelta = (terrainImgW.getWidth() / terrain.length);
        double pYPerDelta = (terrainImgW.getHeight() / terrain[1].length);

        PixelWriter pixelWriter = terrainImgW.getPixelWriter();

        double x, y;

        long now = System.nanoTime();
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[1].length; j++) {
                int rgb = (int)(terrain[i][j] * scale);
                Color color = Color.rgb(rgb, rgb, rgb);
                if (minHeightM == maxHeightM)
                    color = Color.GRAY;

                x = i * pXPerDelta;
                y = j * pYPerDelta;

                for (int k = 0; k < pXPerDelta; k++) {
                    for (int l = 0; l < pYPerDelta; l++) {
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
}
