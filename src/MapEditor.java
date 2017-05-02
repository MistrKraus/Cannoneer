import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

/**
 * Created by kraus on 30.04.2017.
 */
public class MapEditor {

    private double[] heights;
    private double[][] surface;

    private int mapWidth;
    private int mapHeight;
    private int playerX;
    private int playerY;
    private int targetX;
    private int targetY;

    private double deltaXm;
    private double deltaYm;
    private double minHeightM;
    private double maxHeightM;
    private double width_heght_scale;

    private ImageView terrainImgView = new ImageView();
    private Image terrainImg;
    WritableImage wImage;

    public MapEditor(int mapWidth, int mapHeight, double deltaXm, double deltaYm, double minHeightM, double maxHeightM,
            int playerX, int playerY, int targetX, int targetY) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.deltaXm = deltaXm;
        this.deltaYm = deltaYm;
        this.minHeightM = minHeightM;
        this.maxHeightM = maxHeightM;
        this.playerX = playerX;
        this.playerY = playerY;
        this.targetX = targetX;
        this.targetY = targetY;

        this.width_heght_scale = (mapWidth * deltaXm) / (mapHeight * deltaYm);
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
        this.width_heght_scale = (mapWidth * deltaXm) / (mapHeight * deltaYm);
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
        this.width_heght_scale = (mapWidth * deltaXm) / (mapHeight * deltaYm);
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public void setDeltaXm(double deltaXm) {
        this.deltaXm = deltaXm;
        this.width_heght_scale = (mapWidth * deltaXm) / (mapHeight * deltaYm);
    }

    public void setDeltaYm(double deltaYm) {
        this.deltaYm = deltaYm;
        this.width_heght_scale = (mapWidth * deltaXm) / (mapHeight * deltaYm);
    }

    public void setMinHeightM(double minHeightM) {
        this.minHeightM = minHeightM;
    }

    public void setMaxHeightM(double maxHeightM) {
        this.maxHeightM = maxHeightM;
    }

    public void setScale() {
        this.width_heght_scale = getWidthM() / getHeightM();
    }

    public void setHeights(ImageView terrainImgView) {
        int imgWidth = (int)terrainImgView.getImage().getWidth();
        int imgHeight = (int)terrainImgView.getImage().getHeight();

        heights = new double[imgWidth * imgHeight];

        PixelReader pixelReader = terrainImgView.getImage().getPixelReader();

        for (int i = 0; i < imgWidth; i++)
            for (int j = 0; j < imgHeight; j++)
                heights[j * imgWidth + i] = pixelReader.getColor(i, j).getRed();

        this.terrainImgView = terrainImgView;
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

    public double getWidthM() {
        return mapWidth * deltaXm;
    }

    public double getHeightM() {
        return mapHeight * deltaYm;
    }

    public double getMinHeightM() {
        return minHeightM;
    }

    public double getMaxHeightM() {
        return maxHeightM;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public double getScale() {
        return width_heght_scale;
    }

    public ImageView getTerrainImgView() {
        return terrainImgView;
    }
}
