import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kraus on 18.04.2017.
 */
public class TerrainSide implements IDrawable {

    private ImageView terrainImgView = new ImageView();
    private Image terrainImg;
    WritableImage wImage;

    private static final int IMG_HEIGHT = 500;

    /**
     *
     *
     * @param surface mapa povrchu
     * @param playerCoord souradnice hrace
     * @param targetCoord souradnice cile
     * @param scaleX
     * @param scaleY
     * @param mapScale pomer stran mapy (sirka / vyska)
     */
    public TerrainSide(double surface[][], Point playerCoord, Point targetCoord, double scaleX, double scaleY, double mapScale) {
        int playerX = (int)(playerCoord.getX() * scaleX);
        int playerY = (int)(playerCoord.getY() * scaleY);
        int targetX = (int)(targetCoord.getX() * scaleX);
        int targetY = (int)(targetCoord.getY() * scaleY);

        double scale = (playerX - targetX) / (playerY - targetY);

        ArrayList<Double> heights = new ArrayList<>();

        int x = playerX;
        int y = playerY;

        while (y != targetY) {
            for (int i = 0; i < scale; i++) {
                heights.add(surface[x++][y]);
            }

            y++;
        }

        bufferImg(heights, mapScale);
    }

    private void bufferImg(ArrayList<Double> heights, double mapScale) {
        //WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * (mapWidthM / mapHeightM)), IMG_HEIGHT);
        WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * mapScale), IMG_HEIGHT);
        PixelWriter pixelWriter = terrainImgW.getPixelWriter();

        double max = 0;
        for (double x:heights)
            max = Math.max(max, x);

        double maxY = (9 * terrainImgW.getHeight()) / 10;
        double scaleY = maxY / max;
        double delta = terrainImgW.getWidth() / heights.size();

        int x = 0;
        int imgWidth = (int) terrainImgW.getWidth();
        int imgHeight = (int) terrainImgW.getHeight();
        int index = 0;

        Color color = Color.BLACK;

        while (x * delta < imgWidth) {
            for (int i = 0; i < delta; i++) {
                for (int y = 0; y < heights.get(index) * scaleY; y++) {
                    pixelWriter.setColor((int) (x * delta + i), imgHeight - 1 - y, color);
                }
            }
            x++;
            index++;
        }

        terrainImgView.setImage(terrainImgW);
        terrainImg = terrainImgView.getImage();
        wImage = new WritableImage(terrainImg.getPixelReader(), (int)terrainImg.getWidth(), (int)terrainImg.getHeight());
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        terrainImg = wImage;
        g.drawImage(terrainImg, 0, 0, g.getCanvas().getWidth() + 1, g.getCanvas().getHeight() + 1);
    }

    @Override
    public void update(World world) {

    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidthX() {
        return 0;
    }

    @Override
    public double getWidthY() {
        return 0;
    }
}
