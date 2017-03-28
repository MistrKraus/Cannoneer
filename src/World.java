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

    private final List<Player> playersToRemove = new ArrayList<>();
    private final List<Target> targetsToRemove = new ArrayList<>();
    private final List<Missile> missilesToRemove = new ArrayList<>();
    private final List<Explosion> explosionsToRemove = new ArrayList<>();

    /** metry na index v poli*/
    private double scaleX;
    private double scaleY;
    /** pixely na metr*/
    private double scalePixelperMX;
    private double scalePixelperMY;

    // TODO budouci moznost iterace
    private int playerIndex = 0;

    private boolean runs = false;

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
        //map.addToMap(player);

        players.add(player);
    }

    /**
     * prida instanci cile
     *
     * @param target cil
     */
    public void addTarget(Target target) {
        map.addToMap(target);

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
        System.out.println("Strela znicena");
        missilesToRemove.add(missile);
    }

    public void removeExplosion(Explosion explosion) {
        explosionsToRemove.add(explosion);
    }

    public void initialGraphis() {
        map.bufferImage();
    }

    /**
     * aktualizuje vsechny objekty
     */
    public void update() {
        targets.removeAll(targetsToRemove);
        targetsToRemove.clear();
        missiles.removeAll(missilesToRemove);
        missilesToRemove.clear();
        explosions.removeAll(explosionsToRemove);
        explosionsToRemove.clear();

        if (targets.size() <= 0) {
            stop();
            return;
        }
        scaleX = 1 / data.getMap().getDeltaXm();
        scaleY = 1 / data.getMap().getDeltaYm();

        players.forEach((player1) -> player1.update(this));
        targets.forEach((target1) -> target1.update(this));
        missiles.forEach((missile1) -> missile1.update(this));
        explosions.forEach((explosion1) -> explosion1.update(this));
        wind.update(this);

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

        map.draw(graphics, scalePixelperMX, scalePixelperMY);
        players.forEach(player -> player.draw(graphics, scalePixelperMX, scalePixelperMY));
        targets.forEach(target -> target.draw(graphics, scalePixelperMX, scalePixelperMY));
        missiles.forEach(missile -> missile.draw(graphics, scalePixelperMX, scalePixelperMY));
        explosions.forEach(explosion -> explosion.draw(graphics, scalePixelperMX, scalePixelperMY));
        wind.draw(graphics, scalePixelperMX, scalePixelperMY);
    }

    /**
     * stusti herni smycku
     */
    public void start() {
        runs = true;
        timeline.play();
    }

    /**
     * zastavi herni smycku
     */
    public void stop() {
        runs = false;
        timeline.stop();

        draw();

        graphics.setFill(Color.RED);
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.fillText("KONEC HRY!", graphics.getCanvas().getWidth() / 2,
                graphics.getCanvas().getHeight() / 2, graphics.getCanvas().getWidth() / 2);


    }

    public void pause() {
        runs = false;
        timeline.pause();
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

    public boolean isRunning() {
        return runs;
    }

}
