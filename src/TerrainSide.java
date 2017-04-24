import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by kraus on 18.04.2017.
 */
public class TerrainSide implements IDrawable {

    private double maxWidth;
    private double maxHeight;
    private double scaleX;
    private double scaleY;

    private ImageView terrainImgView = new ImageView();
    private Image terrainImg;
    WritableImage wImage;

    private static final int IMG_HEIGHT = 500;

    /**
     *
     *
     * @param surface mapa povrchu
     * @param missileCoord seznam vsech souradnic, kudy poletí střela
     * @param scaleX pomer na prepocet metru z x-ove souradnice na index
     * @param scaleY pomer na prepocet metru z y-ove souradnice na index
     * @param mapScale pomer stran mapy (sirka / vyska)
     */
    //TODO vyresit -90, 0, 180, 270 stupnu
    public TerrainSide(double surface[][], ArrayList<Point> missileCoord, double scaleX, double scaleY, double mapScale) {
        double startCoordX = missileCoord.get(0).getX();
        double startCoordY = missileCoord.get(0).getX();
        double endCoordX = missileCoord.get(missileCoord.size() - 1).getX();
        double endCoordY = missileCoord.get(missileCoord.size() - 1).getY();

        int startX = (int)(startCoordX * scaleX);
        int startY = (int)(startCoordY * scaleY);
        int endX = (int) Math.floor(endCoordX * scaleX);
        int endY = (int) Math.floor(endCoordY * scaleY);

        double distanceX = startX - endX;
        double distanceY = startY - endY;

        maxHeight = 0;
        for (Point point:missileCoord)
            maxHeight = Math.max(point.getZ(), maxHeight);

        //double scale = distanceY / distanceX;
        double scale = Math.min(distanceX, distanceY) / Math.max(distanceX, distanceY);
        System.out.println("Scale: " + scale);

        ArrayList<Double> heights = new ArrayList<>();

        //long t1 = System.nanoTime();
        int x = startX;
        int y = startY;

//        while (y < endY && x < endX) {
//        //while (x != endX) {
//            for (int i = 0; i < scale; i++) {
//                //heights.add(surface[x][y]);
//                //heights.add(surface[x][y++]);
//                //System.out.println(surface[x][y]);
//                //System.out.println(x++ + " | " + y);
//                x++;
//            }
//
//            y++;
//            //x++;
//        }
//        //System.out.println(System.nanoTime() - t1);
//        System.out.println(x + " | " + y);
//        System.out.println(surface[x][y]);
//        System.out.println("--------");
        //heights = new ArrayList<>();

        double j = 0;
//        x = startX;
//        y = startY;
        while (x < endX || y < endY) {
//            if (distanceY < distanceY) {
                while (j < scale) {
                    heights.add(surface[x][y]);
                    j++;

                    if (distanceY < distanceX)
                        y++;
                    else
                        x++;
                    //System.out.println(x + " | " + y++);
                }
                j %= scale;

            if (distanceY < distanceX)
                x++;
            else
                y++;

//            } else {
//               while (false){};
//            }

            if (x == surface[0].length)
                x--;

            if (y == surface[1].length)
                y--;
        }
        System.out.println(x + " | " + y);
        System.out.println(surface[x][y]);
        System.out.println(missileCoord.get(missileCoord.size() - 1).getZ());

//        int x = startX;
//        int y = startY;
//        x = startX;
//        y = startY;
//
//        heights = new ArrayList<>();
//
//        t1 = System.nanoTime();
//
//        int maxX = surface[0].length - 1;
//        int maxY = surface[1].length - 1;
//
//        for (Point coord:missileCoord) {
//            if (heights.size() == 0) {
//                heights.add(surface[x][y]);
//                continue;
//            }
//
//            if (!(x == (int)(coord.getX() * scaleX) && y == (int)(coord.getY() * scaleY))) {
//                heights.add(surface[x][y]);
//
//                //System.out.println(x + " " + (int)(coord.getX() * scaleX) + " | " + y + " " + (int)(coord.getY() * scaleY));
//            }
//
//            x = (int)(coord.getX() * scaleX);
//            y = (int)(coord.getY() * scaleY);
//
//            if (x > maxX)
//                x = maxX;
//
//            if (y > maxY)
//                y = maxY;
//        }
//        System.out.println(System.nanoTime() - t1);
//        System.out.println(x + " | " + y);

        bufferImg(heights, Math.sqrt(endCoordX * endCoordX + endCoordY * endCoordY), maxHeight, mapScale);
    }

    /**
     *
     *
     * @param heights list s vyskami v metrech ve vyrezu mapy, podle trajektorie strely
     * @param maxWidth maximalni sirka, kam zasahuje vizualizace
     * @param maxHeight maximalni vyska, kam zasahuje vizualizace
     * @param mapScale pomer stran mapy
     */
    private void bufferImg(ArrayList<Double> heights, double maxWidth, double maxHeight, double mapScale) {
        //WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * (mapWidthM / mapHeightM)), IMG_HEIGHT);
        WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * mapScale), IMG_HEIGHT);
        PixelWriter pixelWriter = terrainImgW.getPixelWriter();

        for (double x:heights)
            maxHeight = Math.max(maxHeight, x);

        double maxY = (9 * terrainImgW.getHeight()) / 10;
        this.scaleY = maxY / maxHeight;
        double delta = terrainImgW.getWidth() / heights.size();
        this.scaleX = terrainImgW.getWidth() / maxWidth;

        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;

        //System.out.println(maxY + " x " + maxWidth * scaleY);

        int x = 0;
        int imgWidth = (int) terrainImgW.getWidth();
        int imgHeight = (int) terrainImgW.getHeight();
        int index = 0;
        int pX = 0;
        int pY = 0;

        Color color = Color.BLACK;

        //System.out.println(terrainImgW.getWidth() + " - " + IMG_HEIGHT + "\n---------------");

        while (x * delta < imgWidth) {
            for (int i = 0; i < delta; i++) {
                for (int y = 0; y < heights.get(index) * scaleY; y++) {
                    if ((int)(x * delta + i) < imgWidth)
                        pX = (int)(x * delta + i);

                    if (imgHeight - 1 - y < imgHeight)
                        pY = imgHeight - 1 - y;

                    //System.out.println(pX + " - " + pY);
                    pixelWriter.setColor(pX, pY, color);
                }
            }
            x++;
            index++;

            if (index > heights.size() - 1)
                index = heights.size() - 1;
        }

        terrainImgView.setImage(terrainImgW);
        terrainImg = terrainImgView.getImage();
        wImage = new WritableImage(terrainImg.getPixelReader(), (int)terrainImg.getWidth(), (int)terrainImg.getHeight());
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        terrainImg = wImage;
        g.drawImage(terrainImg, 0, 0, g.getCanvas().getWidth() + 1, g.getCanvas().getHeight() + 1);
        scaleX = (g.getCanvas().getWidth()) / maxWidth;
        scaleY = ((9 * g.getCanvas().getHeight() + 1) / 10) / maxHeight;
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

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }
}
