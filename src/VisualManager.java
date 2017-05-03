import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

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

    public VisualManager (double azimuth, double elevation, double acceleration, /*boolean firstVisualMissile,*/ World world) {
        this(azimuth, elevation, acceleration, 0, world.getGraphics().getCanvas().getHeight() / 2,
                world.getGraphics().getCanvas().getHeight() / 2, /*firstVisualMissile,*/ world);
    }

    public VisualManager (double azimuth, double elevation, double acceleration, double splitX1, double splitY1,
                          double splitCood2, /*boolean firstVisualMissile,*/ World world) {
        Canvas canvas = world.getGraphics().getCanvas();
        Point coordinates = world.getPlayer().getCoordinates().copy();

        graph1Width = canvas.getWidth();
        graph1Height = canvas.getHeight() / 2;
        graph2Width = canvas.getWidth();
        graph2Height = canvas.getHeight() / 2;

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

        // vychozi rozdeleni
        this.graph = new MyGraph(elevation, acceleration, graph1Width, graph1Height, 0, 0);
        this.visualMissile = new VisualMissile(coordinates, azimuth, elevation, acceleration, graph2Width,
                graph2Height, 0, graph.getHeight() / 2, true, world);

        //world.addVisualMissile(visualMissile);

        this.terrSide = new TerrainSide(world.getMap().getSurface(), visualMissile.getX(), visualMissile.getY(), visualMissile,
                world.getScaleX(), world.getScaleY() * 2, world.getMap().getMapWidthM() / (world.getMap().getMapHeightM() / 2), 0, graph2Height);

        //world.removeVisualMissile(visualMissile);

        this.visualMissiles = world.getVisualMissiles();
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {
        graph.draw(g, scaleMperPixelX, scaleMperPixelY);
        terrSide.draw(g, scaleMperPixelX, scaleMperPixelY);
        visualMissiles.forEach(missile -> missile.draw(g, scaleMperPixelX, scaleMperPixelY));
    }

    @Override
    public void update(World world) {
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
