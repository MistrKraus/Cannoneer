import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.DoubleSummaryStatistics;
import java.util.StringJoiner;

/**
 * Created by kraus on 02.05.2017.
 */
public class VisualManager implements IDrawable {

    private final MyGraph graph;
    private final TerrainSide terrSide;
    private final VisualMissile visualMissile;
    private final ObservableList<VisualMissile> visualMissiles;

    private double graph1Width;
    private double graph1Height;
    private double graph2Width;
    private double graph2Height;
    private double colorScale;
    private double redScale = 53 / 255;
    private double greenScale = 173 / 255;

    private PrintWriter printWriter;

    public VisualManager (double azimuth, double elevation, double acceleration, /*boolean firstVisualMissile,*/ World world) throws FileNotFoundException, UnsupportedEncodingException {
        this(azimuth, elevation, acceleration, 0, world.getGraphics().getCanvas().getHeight() / 2,
                world.getGraphics().getCanvas().getHeight() / 2, /*firstVisualMissile,*/ world);
    }

    public VisualManager (double azimuth, double elevation, double acceleration, double splitX1, double splitY1,
                          double splitCood2, /*boolean firstVisualMissile,*/ World world) throws FileNotFoundException, UnsupportedEncodingException {
        Canvas canvas = world.getGraphics().getCanvas();
        Point coordinates = world.getPlayer().getCoordinates().copy();

        graph1Width = canvas.getWidth();
        graph1Height = canvas.getHeight() / 2;
        graph2Width = canvas.getWidth();
        graph2Height = canvas.getHeight() / 2;

        colorScale = graph2Height / 255;

//        // rozdeleni na ose X
//        if (splitX1 == splitCood2) {
//            this.graph = new MyGraph(elevation, acceleration, splitX1, canvas.getHeight(), 0, 0);
//
//            this.visualMissile = new VisualMissile(coordinates, azimuth, elevation, acceleration, canvas.getWidth() - splitX1,
//                    canvas.getHeight(), splitX1, 0, true, world);
//
//            this.terrSide = new TerrainSide(world.getMap().getSurface(), visualMissile.getX(), visualMissile.getY(), visualMissile,
//                    world.getScaleX(), world.getScaleY(), world.getMap().getMapWidthM() / world.getMap().getMapHeightM());
//
//            world.removeVisualMissile(visualMissile);
//
//            return;
//        }
//
//        // rozdeleni na ose Y
//        if (splitY1 == splitCood2) {
//            this.graph = new MyGraph(elevation, acceleration, canvas.getWidth(), splitY1, 0, 0);
//
//            this.visualMissile = new VisualMissile(coordinates, azimuth, elevation, acceleration, canvas.getWidth(),
//                    graph.getHeight() - splitY1, 0, splitY1, true, world);
//
//            world.addVisualMissile(visualMissile);
//
//            this.terrSide = new TerrainSide(world.getMap().getSurface(), visualMissile.getX(), visualMissile.getY(), visualMissile,
//                    world.getScaleX(), world.getScaleY(), world.getMap().getMapWidthM() / world.getMap().getMapHeightM());
//
//            world.removeVisualMissile(visualMissile);
//
//            return;
//        }
//
        // vychozi rozdeleni
        this.graph = new MyGraph(elevation, acceleration, graph1Width, graph1Height, 0, 0);
        this.visualMissile = new VisualMissile(coordinates, azimuth, elevation, acceleration, graph2Width,
                graph2Height, 0, graph.getHeight() / 2, true, world);

        //world.addVisualMissile(visualMissile);

        this.terrSide = new TerrainSide(world.getMap().getSurface(), visualMissile, visualMissile.getHeights(),
                world.getScaleX(), world.getScaleY(), world.getMap().getMapWidthM() / (world.getMap().getMapHeightM() / 2), 0, graph2Height);

        //world.removeVisualMissile(visualMissile);

        this.visualMissiles = world.getVisualMissiles();

//        printWriter = new PrintWriter("TestVizualizace", "UTF-8");
//
//        terrSide.getHeights().forEach(height -> printWriter.print(height + " - "));
//        printWriter.println();
//        visualMissile.getHeights().forEach(height -> printWriter.print(height + " - "));
//        printWriter.close();
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        g.setFill(Color.rgb(150, 212, 255));
        g.fillRect(0,0, g.getCanvas().getWidth(), graph1Height);

        for (int i = 0; i < 255; i++) {
            g.setFill(Color.rgb((int)(150 -(i * redScale)), (int)(212 - (i * greenScale)), 255 - i));
            g.fillRect(0, graph1Height + i * colorScale, g.getCanvas().getWidth(), graph1Height + i * colorScale);
        }

        graph.draw(g, scaleMperPixelX, scaleMperPixelY);

        if (graph.getChyba()) {
            g.setFill(Color.RED);
            g.setTextAlign(TextAlignment.CENTER);
            g.setFont(Font.font("INPACT", FontWeight.BOLD, 20));
//            g.fillText("CHYBA!", g.getCanvas().getWidth() / 2, g.getCanvas().getHeight() - 30);
//            g.setFont(Font.font("INPACT", FontWeight.NORMAL, 15));
            g.fillText("Tyto paramatry nelze\nvizualizovat", g.getCanvas().getWidth() / 2,
                    3 * g.getCanvas().getHeight() / 4);

            return;
        }

        visualMissiles.forEach(missile -> missile.draw(g, scaleMperPixelX, scaleMperPixelY));
        terrSide.draw(g, scaleMperPixelX, scaleMperPixelY);
    }

    @Override
    public void update(World world) {
        graph1Width = world.getGraphics().getCanvas().getWidth();
        graph1Height = world.getGraphics().getCanvas().getHeight() / 2;
        graph2Width = world.getGraphics().getCanvas().getWidth();
        graph2Height = world.getGraphics().getCanvas().getHeight() / 2;

        colorScale = graph2Height / 255;

        graph.update(world);

        for (int i = 0; i < 8; i++)
            visualMissiles.forEach(missile -> missile.update(world));

        terrSide.update(world);
    }

    public MyGraph getGraph() {
        return graph;
    }

    public TerrainSide getTerrSide() {
        return terrSide;
    }

    public VisualMissile getVisualMissile() {
        return visualMissile;
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
