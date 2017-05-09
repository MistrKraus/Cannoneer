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
    private double posX;
    private double posY;
    private double scaleX;
    private double scaleY;
    private double delta;

    private ImageView terrainImgView = new ImageView();
    private Image terrainImg;
    WritableImage wImage;

    private final int heights_size;
    private final int img_width;
    private final double startX;

    private static final int IMG_HEIGHT = 500;

    /**
     *
     *
     * @param surface mapa povrchu
     * @param startCoordX x-ová souřadnice počátečního bodu v metrech
     * @param startCoordY y-ová souřadnice počátečního bodu v metrech
     * @param visualMissile strela, ktera se bude vizualizovat
     * @param scaleX pomer na prepocet metru z x-ove souradnice na index
     * @param scaleY pomer na prepocet metru z y-ove souradnice na index
     * @param mapScale pomer stran mapy (sirka / vyska)
     * @param posX x-ove souradnice, odkud se bude mapa vykreslovat
     * @param posY y-ove souradnice, odkud se bude mapa vykreslovat
     */
    public TerrainSide(double surface[][], double startCoordX, double startCoordY, VisualMissile visualMissile,
                       double scaleX, double scaleY, double mapScale, double posX, double posY) {

        this.posX = posX;
        this.posY = posY;

        this.startX = Math.sqrt(startCoordX * startCoordX + startCoordY * startCoordY);

        double endCoordX = visualMissile.getCollidingPoint().getX();
        double endCoordY = visualMissile.getCollidingPoint().getY();

        //indexy v poli - surface
        int startX = (int) (startCoordX * scaleX);
        int startY = (int) (startCoordY * scaleY);
        int endX = (int) (visualMissile.getCollidingPoint().getX() * scaleX);
        int endY = (int) (visualMissile.getCollidingPoint().getY() * scaleY);

        System.out.println("Visual:");
        System.out.println(startX);
        System.out.println(startY);
        //System.out.println(visualMissile.getCollidingPoint().getX());
        //System.out.println(visualMissile.getCollidingPoint().getY());

        if (endX >= surface[0].length)
            endX = surface[0].length - 1;

        if (endY >= surface[1].length)
            endY = surface[1].length - 1;

        maxHeight = visualMissile.getMaxHeight();

        ArrayList<Double> heights = countHeights(surface, (int) visualMissile.getAzimuth(), startX, startY, endX, endY);

        /*
        //double scale = distanceY / distanceX;
        double scale = countScale(distanceX, distanceY, visualMissile.getAzimuth());

        System.out.println("Scale: " + scale);

        ArrayList<Double> heights = new ArrayList<>();

        //long t1 = System.nanoTime();
        int x = startX;
        int y = startY;


        System.out.println(endX + " - " + endY);
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

//            if (x == surface[0].length)
//                x--;
//
//            if (y == surface[1].length)
//                y--;
        }

        while (x < endX) {
            heights.add(surface[x++][y]);
        }

        while (y < endY) {
            heights.add(surface[x][y++]);
        }


        System.out.println(x + " | " + y);
        System.out.println(heights.size());
//        System.out.println(Math.min(x, surface[0].length - 1) + " | " + Math.min(y, surface[1].length - 1));
//        System.out.println(surface[Math.min(x, surface[0].length - 1)][Math.min(y, surface[1].length - 1)]);
//        System.out.println(visualMissile.getCollidingPoint().getZ());

        //-----

//        while (j < scale) {
//            if (x < surface[0].length && y < surface[1].length) {
//                //System.out.println(x);
//                heights.add(surface[x][y]);
//            }
//            j++;
//
//            if (distanceY < distanceX) {
//                y++;
//            }
//            else {
//                x++;
//            }
//            //System.out.println(x + " | " + y++);
//        }
//
//        if (distanceY < distanceX) {
//            x++;
//        }
//        else {
//            y++;
//        }
//
//        if (x < surface[0].length && y < surface[1].length) {
//            heights.add(surface[x][y]);
//        }
//
//        System.out.println(x + " | " + y);
//        System.out.println(heights.size());

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

        */

        this.heights_size = heights.size();
        this.img_width = (int)(IMG_HEIGHT * mapScale);

        bufferImg(heights, Math.sqrt(endCoordX * endCoordX + endCoordY * endCoordY), maxHeight);
    }

    /**
     *
     *
     * @param heights list s vyskami v metrech ve vyrezu mapy, podle trajektorie strely
     * @param maxWidth maximalni sirka, kam zasahuje vizualizace
     * @param maxHeight maximalni vyska, kam zasahuje vizualizace
     */
    private void bufferImg(ArrayList<Double> heights, double maxWidth, double maxHeight) {
        //WritableImage terrainImgW = new WritableImage((int)(IMG_HEIGHT * (mapWidthM / mapHeightM)), IMG_HEIGHT);

        WritableImage terrainImgW = new WritableImage(img_width, IMG_HEIGHT);
        PixelWriter pixelWriter = terrainImgW.getPixelWriter();

        for (double x:heights)
            maxHeight = Math.max(maxHeight, x);

        double maxY = (9 * terrainImgW.getHeight()) / 10;
        this.scaleY = maxY / maxHeight;
        delta = terrainImgW.getWidth() / heights.size();
        this.scaleX = (terrainImgW.getWidth() - delta) / maxWidth;

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
        g.drawImage(terrainImg, posX, posY, g.getCanvas().getWidth() + 1, (g.getCanvas().getHeight() - posY) + 1);

//        g.setStroke(Color.RED);
//        g.strokeLine(0, 20, g.getCanvas().getWidth() - delta, 20);
//        g.setStroke(Color.BLUE);
//        g.strokeLine(0, 25,g.getCanvas().getWidth(), 25);
    }

    @Override
    public void update(World world) {
        GraphicsContext g = world.getGraphics();

        delta = g.getCanvas().getWidth() / (heights_size);
        System.out.println(startX);
        this.scaleX = (g.getCanvas().getWidth() - delta) / (maxWidth - startX);
        this.scaleY = ((9 * (g.getCanvas().getHeight() / 2) + 1) / 10) / maxHeight;

        posX = 0;
        posY = g.getCanvas().getHeight() / 2;

//        double maxY = (9 * terrainImgW.getHeight()) / 10;
//        this.scaleY = maxY / maxHeight;
//        delta = terrainImgW.getWidth() / heights.size();
//        this.scaleX = (terrainImgW.getWidth() - delta) / maxWidth;
    }

//    /**
//     *
//     *
//     * @param distanceX vzdalenost
//     * @param distanceY
//     * @param degree
//     * @return
//     */
//    private double countScale(double distanceX, double distanceY, double degree) {
//        //if (degree < 0 && degree > -90)
//        System.out.println(distanceX + " D " + distanceY);
//
//            return Math.min(distanceX, distanceY) / Math.max(distanceX, distanceY);
//
////        if (degree == 90 || degree == 270)
////            return
////
////        return 1;
//    }
//

    private ArrayList<Double> countHeights(double[][] surface, int degree, int startX, int startY, int endX, int endY) {
        //System.out.println(degree);

        ArrayList<Double> heights = new ArrayList<>();

        int distanceX = endX - startX;
        int distanceY = endY - startY;
        int x = startX;
        int y = startY;

        double scale;

        System.out.println("->");
        System.out.println(startX);
        System.out.println(startY);

        if (degree == -90)
            degree = 270;

        System.out.println(x + " | " + y);

        if (distanceX == 0 && distanceY == 0) {
            heights.add(surface[startX][startY]);
            return heights;
        }


        switch (degree) {
            case 0:
                while (x != endX && y != endY)
                    heights.add(surface[x++][y]);
                break;

            case 90:
                while (x != endX && y != endY)
                    heights.add(surface[x][y++]);
                break;

            case 180:
                while (x != endX && y != endY)
                    heights.add(surface[x--][y]);
                break;

            case 270:
                while (x != endX && y != endY)
                    heights.add(surface[x][y--]);
                break;

            default:
                double j = 0;

                if (distanceY == 0) {
                    while (x < endX)
                        heights.add(surface[x++][y]);

                    break;
                }

                if (distanceX == 0) {
                    while (y < endY) {
                        heights.add(surface[x][y++]);
                    }

                    break;
                }

                scale = Math.abs(distanceX / distanceY);

                if (distanceX > 0 && distanceY > 0) {
                    while (x < endX && y < endY) {
                        while (j < scale) {
                            heights.add(surface[x][y]);

                            j++;

                            if (scale > 1)
                                x++;
                            else
                                y++;
                        }
                        j %= scale;

                        if (scale > 1)
                            y++;
                        else
                            x++;
                    }
                }

                if (distanceX < 0 && distanceY > 0) {
                    while (x > endX && y < endY) {
                        while (j < scale) {
                            heights.add(surface[x][y]);

                            j++;

                            if (scale > 1)
                                x--;
                            else
                                y++;
                        }

                        j %= scale;

                        if (scale > 1)
                            y++;
                        else
                            x--;
                    }
                }

                if (distanceX < 0 && distanceY < 0) {
                    while (x > endX && y > endY) {
                        while (j < scale) {
                            heights.add(surface[x][y]);

                            j++;

                            if (scale > 1)
                                x--;
                            else
                                y--;
                        }

                        j %= scale;

                        if (scale > 1)
                            y--;
                        else
                            x--;
                    }
                }

                if (distanceX > 0 && distanceY < 0) {
                    while (x < endX && y > endY) {
                        while (j < scale) {
                            heights.add(surface[x][y]);

                            j++;

                            if (scale > 1)
                                x++;
                            else
                                y--;
                        }

                        j %= scale;

                        if (scale > 1)
                            y--;
                        else
                            x++;
                    }
                }

                break;
        }

        System.out.println(x + " - " + y);

        return heights;
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
