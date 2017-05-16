import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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

    private final Text txtWin1;
    private final Text txtWin2;
    private final Text txtFail1;
    private final Text txtFail2;

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

    /**
     * metry na index v poli
     */
    private double scaleX;
    private double scaleY;
    /**
     * pixely na metr
     */
    private double scalePixelperMX;
    private double scalePixelperMY;

    private double visualTime;

    // TODO budouci moznost iterace
    private int playerIndex = 0;
    private boolean isRunning = false;
    private boolean isVisuializing = false;
    private boolean win = false;

    private Point vMissileFirstCoord;
    private VisualManager visualManager;
    private TerrainSide terrSide;

    private static final int DEFAULT_VISUAL_TIME = 10;

    /**
     * konstruktor
     *
     * @param graphics graficky kontext
     * @param data     nactena data
     */
    public World(GraphicsContext graphics, Data data, WindGraphics wg) {
        this.graphics = graphics;
        this.data = data;
        this.map = data.getMap();
        this.wind = new Wind(wg);

        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        graphics.setFont(Font.font("INPACT", FontWeight.BOLD, 30));
        graphics.setTextAlign(TextAlignment.CENTER);

        Font font1 = Font.font("INPACT", FontWeight.BOLD, 30);
        Font font2 = Font.font("INPACT", FontWeight.NORMAL, 25);

        txtWin1 = new Text("MISE SPLNENA!");
        txtWin1.setFont(font1);
        txtWin2 = new Text("Vsechny cile zneskodneny.");
        txtWin2.setFont(font2);

        txtFail1 = new Text("SELHAL JSTE!");
        txtFail1.setFont(font1);
        txtFail2 = new Text("Vas tank byl znicen.");
        txtFail2.setFont(font2);
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

        System.out.println("Cil znicen!");

        targetsToRemove.add(target);
    }

    public void removePlayer(Player player) {
        System.out.println("Hrac znicen!");
        playersToRemove.add(player);
    }

    /**
     * odstrani strelu
     *
     * @param missile strela
     */
    public void removeMissile(Missile missile) {
        for (Missile missileToRemove : missilesToRemove)
            if (missile.equals(missileToRemove))
                return;

        fireStats.add(new FireStat("Player1", getPlayer().coordinates, missile));

        System.out.println("Strela znicena");
        missilesToRemove.add(missile);
    }

    public void removeExplosion(Explosion explosion) {
        explosionsToRemove.add(explosion);
    }

    public void removeVisualMissile(VisualMissile vMissile) {
        for (VisualMissile vMissileToRemove : visualMissilesToRemove)
            if (vMissile.equals(vMissileToRemove))
                return;

        visualMissilesToRemove.add(vMissile);
    }

    /**
     * Odstraneni vsech strel
     */
    public void removeAllMissiles() {
        for (Missile missile :
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
        if (!isRunning) {
            players.forEach((player) -> player.update(this));
            targets.forEach((target) -> target.update(this));
            for (int i = 0; i < 8; i++)
                missiles.forEach((missile) -> missile.update(this));
            explosions.forEach((explosion) -> explosion.update(this));
        }

        players.removeAll(playersToRemove);
        playersToRemove.clear();
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

        if (players.size() <= 0) {
            win = false;
            stop();
            return;
        }

        if (targets.size() <= 0) {
            win = true;
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
            visualManager.update(this);
        }

        draw();
    }

    /**
     * vykresli vsechny objekty
     */
    public void draw() {
        if (isVisuializing) {
            visualManager.draw(graphics, visualManager.getScaleX(), visualManager.getScaleY());
            return;
        }
        scalePixelperMX = graphics.getCanvas().getWidth() / data.getMap().getMapWidthM();
        scalePixelperMY = graphics.getCanvas().getHeight() / data.getMap().getMapHeightM();

        graphics.setFill(Color.GRAY);
        graphics.fillRect(0, 0, graphics.getCanvas().getWidth(), graphics.getCanvas().getHeight());

        map.draw(graphics, scalePixelperMX, scalePixelperMY);
        targets.forEach(target -> target.draw(graphics, scalePixelperMX, scalePixelperMY));
        explosions.forEach(explosion -> explosion.draw(graphics, scalePixelperMX, scalePixelperMY));
        players.forEach(player -> player.draw(graphics, scalePixelperMX, scalePixelperMY));
        missiles.forEach(missile -> missile.draw(graphics, scalePixelperMX, scalePixelperMY));
        wind.draw(graphics, scalePixelperMX, scalePixelperMY);

        if (!isRunning)
            drawEndCard();
    }

    public void drawEndCard() {
        graphics.setFill(Color.WHITE);
        graphics.setGlobalAlpha(0.3);

        if (win) {
            graphics.fillRect(0, graphics.getCanvas().getHeight() / 2 - txtWin1.getLayoutBounds().getHeight() - 20,
                    graphics.getCanvas().getWidth(), txtWin1.getLayoutBounds().getHeight() + txtWin2.getLayoutBounds().getHeight() + 20);
            graphics.setGlobalAlpha(1);

            graphics.setFill(Color.RED);
            graphics.setFont(txtWin1.getFont());
            graphics.fillText(txtWin1.getText(), graphics.getCanvas().getWidth() / 2,
                    graphics.getCanvas().getHeight() / 2 - txtWin1.getLayoutBounds().getHeight() / 2, graphics.getCanvas().getWidth() / 2);
            graphics.setFont(txtWin2.getFont());
            graphics.fillText(txtWin2.getText(), graphics.getCanvas().getWidth() / 2,
                    graphics.getCanvas().getHeight() / 2 + txtWin2.getLayoutBounds().getHeight() / 2, graphics.getCanvas().getWidth() / 2);
        }
        else {
            graphics.fillRect(0, graphics.getCanvas().getHeight() / 2 - txtFail1.getLayoutBounds().getHeight() - 20,
                    graphics.getCanvas().getWidth(), txtFail1.getLayoutBounds().getHeight() + txtFail2.getLayoutBounds().getHeight() + 20);
            graphics.setGlobalAlpha(1);

            graphics.setFill(Color.RED);
            graphics.setFont(txtFail1.getFont());
            graphics.fillText(txtFail1.getText(), graphics.getCanvas().getWidth() / 2,
                    graphics.getCanvas().getHeight() / 2 - txtFail1.getLayoutBounds().getHeight() / 2, graphics.getCanvas().getWidth() / 2);
            graphics.setFont(txtFail2.getFont());
            graphics.fillText(txtFail2.getText(), graphics.getCanvas().getWidth() / 2,
                    graphics.getCanvas().getHeight() / 2 + txtFail2.getLayoutBounds().getHeight() / 2, graphics.getCanvas().getWidth() / 2);
        }
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
    }

    public void pause() {
        isRunning = false;
        timeline.pause();
    }

    public void visualize(double azimuth, double elevation, double speed) throws FileNotFoundException, UnsupportedEncodingException {
        visualManager = new VisualManager(azimuth, elevation, speed, this);

        update();

//        addVisualMissile(new VisualMissile(getPlayer().getCoordinates(), azimuth, elevation, speed,
//                true, this));

//        if (visualMissiles.size() == 0)
//            return;

        isVisuializing = true;

//        VisualMissile visualMissile = visualMissiles.get(0);
//
//        terrSide = new TerrainSide(map.getSurface(), visualMissile.getX(), visualMissile.getY(), visualMissile,
//                scaleX, scaleY, map.getMapWidthM() / map.getMapHeightM());
//
//        vMissileFirstCoord = visualMissile.getCoordinates().copy();
//
//        removeVisualMissile(visualMissiles.get(0));

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
        removeAllMissiles();
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
            return new Point(0, 0, 0);
        return vMissileFirstCoord;
    }

    public ObservableList<FireStat> getFireStats() {
        return fireStats;
    }

    public ObservableList<VisualMissile> getVisualMissiles() {
        return visualMissiles;
    }

    public double getVisualTime() {
        return visualTime;
    }

    public static int getDefaultVisualTime() {
        return DEFAULT_VISUAL_TIME;
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
