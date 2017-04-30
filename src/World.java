import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kraus on 19.03.2017.
 */
public class World {
    private final Duration duration = Duration.millis(30);
    private final KeyFrame oneFrame = new KeyFrame(duration, event -> update());
    private Timeline timeline;
    private final GraphicsContext graphics;
    private final Data data;
    private final Map map;
    private final Wind wind;

    private final ObservableList<Player> players = FXCollections.observableArrayList();
    private final ObservableList<Target> targets = FXCollections.observableArrayList();
    private final ObservableList<Missile> missiles = FXCollections.observableArrayList();
    private final ObservableList<Explosion> explosions = FXCollections.observableArrayList();
    private final ObservableList<VisualMissile> visualMissiles = FXCollections.observableArrayList();
    private final ObservableList<FireStat> fireStats = FXCollections.observableArrayList();

    private final List<Player> playersToRemove = new ArrayList<>();
    private final List<Target> targetsToRemove = new ArrayList<>();
    private final List<Missile> missilesToRemove = new ArrayList<>();
    private final List<Explosion> explosionsToRemove = new ArrayList<>();
    private final List<VisualMissile> visualMissilesToRemove = new ArrayList<>();

    private final List<VisualMissile> visualMissilesToAdd = new ArrayList<>();

    /** metry na index v poli*/
    private double scaleX;
    private double scaleY;
    /** pixely na metr*/
    private double scalePixelperMX;
    private double scalePixelperMY;

    // TODO budouci moznost iterace
    private int playerIndex = 0;

    private boolean isRunning = false;
    private boolean isVisuializing = false;

    private Point vMissileFirstCoord;

    private TerrainSide terrSide;

    /**
     * konstruktor
     *
     * @param graphics graficky kontext
     * @param data nactena data
     */
    public World(GraphicsContext graphics, Data data) {
        this.graphics = graphics;
        this.data = data;
        this.map = data.getMap();
        this.wind = new Wind();

        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * ulozi instanci hrace
     *
     * @param player hrac
     */
    public void addPlayer(Player player) {
        // hitbox playera
        //map.addToMap(player);

        players.add(player);
    }

    /**
     * prida instanci cile
     *
     * @param target cil
     */
    public void addTarget(Target target) {
        // hitbox targetu
        //map.addToMap(target);

        targets.add(target);
    }

    /**
     * prida instanci strely
     *
     * @param missile strela
     */
    public void addMissile(Missile missile) {
        missiles.add(missile);
    }

    /**
     * prida instanci exploze
     *
     * @param explosion exploze
     */
    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    /**
     * prida instanci vizualizace strely
     *
     * @param visualMissile vizualizace strely
     */
    public void addVisualMissile(VisualMissile visualMissile) {
        visualMissilesToAdd.add(visualMissile);
    }

    /**
     * odstrani cil
     *
     * @param target cil
     */
    public void removeTarget(Target target) {
        map.removeFromMap(target);

        targetsToRemove.add(target);
    }

    /**
     * odstrani strelu
     *
     * @param missile strela
     */
    public void removeMissile(Missile missile) {
        for (Missile missileToRemove:missilesToRemove)
            if (missile.equals(missileToRemove))
                return;

        fireStats.add(new FireStat("Player1", getPlayer().coordinates, missile));

        System.out.println("Strela znicena");
        missilesToRemove.add(missile);
    }

    public void removeExplosion(Explosion explosion) {
        explosionsToRemove.add(explosion);
    }

    public  void removeVisualMissile(VisualMissile vMissile) {
        for (VisualMissile vMissileToRemove:visualMissilesToRemove)
            if (vMissile.equals(vMissileToRemove))
                return;

        visualMissilesToRemove.add(vMissile);
    }
    /**
     * Odstraneni vsech strel
     */
    public void removeAllMissiles() {
        for (Missile missile:
             missiles) {
            missilesToRemove.add(missile);
        }
    }

    public void removeAllVisualMissiles() {
        for (VisualMissile vMissile :
                visualMissiles) {
            visualMissilesToRemove.add(vMissile);
        }
    }

    public void initialGraphis() {
        map.bufferImage();
    }

    /**
     * aktualizuje vsechny objekty
     */
    public void update() {
        //if (visualMissilesToAdd.size() > 0) {
//            System.out.println("REM " + visualMissilesToRemove.size());
//            System.out.println("ADD " + visualMissilesToAdd.size());
        //}

        targets.removeAll(targetsToRemove);
        targetsToRemove.clear();
        missiles.removeAll(missilesToRemove);
        missilesToRemove.clear();
        explosions.removeAll(explosionsToRemove);
        explosionsToRemove.clear();
        visualMissiles.removeAll(visualMissilesToRemove);
        visualMissilesToRemove.clear();

        visualMissiles.addAll(visualMissilesToAdd);
        visualMissilesToAdd.clear();

        if (targets.size() <= 0) {
            stop();
            return;
        }

        scaleX = 1 / data.getMap().getDeltaXm();
        scaleY = 1 / data.getMap().getDeltaYm();

        if (!isVisuializing) {
            players.forEach((player) -> player.update(this));
            targets.forEach((target) -> target.update(this));
            for (int i = 0; i < 8; i++)
                missiles.forEach((missile) -> missile.update(this));

            explosions.forEach((explosion) -> explosion.update(this));
            wind.update(this);
        } else {
            for (int i = 0; i < 4; i++)
                visualMissiles.forEach((visualMissile -> visualMissile.update(this)));
                terrSide.update(this);
        }

        draw();
    }

    /**
     * vykresli vsechny objekty
     */
    public void draw() {
        scalePixelperMX =  graphics.getCanvas().getWidth() / data.getMap().getMapWidthM();
        scalePixelperMY =  graphics.getCanvas().getHeight() / data.getMap().getMapHeightM();

        graphics.setFill(Color.GRAY);
        graphics.fillRect(0, 0, graphics.getCanvas().getWidth(), graphics.getCanvas().getHeight());

        if (isVisuializing) {
            terrSide.draw(graphics, terrSide.getScaleX(), terrSide.getScaleY());
            visualMissiles.forEach(missile -> missile.draw(graphics, terrSide.getScaleX(), terrSide.getScaleY()));
            return;
        }

        map.draw(graphics, scalePixelperMX, scalePixelperMY);
        players.forEach(player -> player.draw(graphics, scalePixelperMX, scalePixelperMY));
        targets.forEach(target -> target.draw(graphics, scalePixelperMX, scalePixelperMY));
        explosions.forEach(explosion -> explosion.draw(graphics, scalePixelperMX, scalePixelperMY));
        missiles.forEach(missile -> missile.draw(graphics, scalePixelperMX, scalePixelperMY));
        wind.draw(graphics, scalePixelperMX, scalePixelperMY);
    }

    /**
     * stusti herni smycku
     */
    public void start() {
        isRunning = true;
        timeline.play();
    }

    /**
     * zastavi herni smycku
     */
    public void stop() {
        isRunning = false;
        timeline.stop();

        draw();

        graphics.setFill(Color.RED);
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.fillText("KONEC HRY!", graphics.getCanvas().getWidth() / 2,
                graphics.getCanvas().getHeight() / 2, graphics.getCanvas().getWidth() / 2);
    }

    public void pause() {
        isRunning = false;
        timeline.pause();
    }

    public void visualize() {
        update();

        if (visualMissiles.size() == 0)
            return;

        isVisuializing = true;

        VisualMissile visualMissile = visualMissiles.get(0);

        terrSide = new TerrainSide(map.getSurface(), visualMissile.getX(), visualMissile.getY(), visualMissile,
                scaleX, scaleY, map.getMapWidthM() / map.getMapHeightM());

        vMissileFirstCoord = visualMissile.getCoordinates().copy();

        removeVisualMissile(visualMissiles.get(0));

//        Missile missile1 = missiles.get(0);
//        for (Missile missile :missiles)
//            if (missile.getIsVisual())
//                missile1 = missile;
//
//        terrSide = new TerrainSide(map.getSurface(), missile1.getAllCoordinates(), scaleX, scaleY,
//                map.getMapWidthM() / map.getMapHeightM());
    }

    public void stopVisualize() {
        isVisuializing = false;

        removeAllVisualMissiles();
    }

    public Data getData() {
        return data;
    }

    public Player getPlayer() {
        return players.get(playerIndex);
    }

    public ObservableList<Player> getPlayers() {
        return players;
    }

    public Target getTarget() {
        return targets.get(playerIndex);
    }

    public ObservableList<Target> getTargets() {
        return targets;
    }

    public Map getMap() {
        return map;
    }

    public Wind getWind() {
        return wind;
    }

    public TerrainSide getTerrSide() {
        return terrSide;
    }

    public GraphicsContext getGraphics() {
        return graphics;
    }

    public double getScalePixelperMX() {
        return scalePixelperMX;
    }

    public double getScalePixelperMY() {
        return scalePixelperMY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public Point getvMissileFirstCoord() {
        if (vMissileFirstCoord == null)
            return new Point(0,0,0);
        return vMissileFirstCoord;
    }

    public ObservableList<FireStat> getFireStats() {
        return fireStats;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isFiring() {
        return missiles.size() > 0;
    }

    public boolean isVisuializing() {
        return isVisuializing;
    }

}
