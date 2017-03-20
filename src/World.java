import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kraus on 19.03.2017.
 */
public class World {

    private final Duration duration = Duration.millis(30);
    /** krok*/
    private final KeyFrame oneFrame = new KeyFrame(duration, event -> update());
    private Timeline timeline;
    private final GraphicsContext graphics;
    private final Data data;

    private final ObservableList<Player> players = FXCollections.observableArrayList();
    private final ObservableList<Target> targets = FXCollections.observableArrayList();
    private final ObservableList<Missile> missiles = FXCollections.observableArrayList();
    private final ObservableList<Explosion> explosions = FXCollections.observableArrayList();

    private final List<Missile> missilesToRemove = new ArrayList<>();
    private final List<Explosion> explosionsToRemove = new ArrayList<>();

    private double scalePixelperMX;
    private double scalePixelperMY;

    // TODO budouci moznost iterace
    private int playerIndex = 0;


    public World(GraphicsContext graphics, Data data) {
        this.graphics = graphics;
        this.data = data;
        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void addPlayer(Player player) {
        for (int i = 0; i < (int)(player.getWidthX() * getScalePixelperMX()); i++)
            for (int j = 0; j < (int)(player.getWidthY() * getScalePixelperMY()); j++)
                getSurface()[(int)(player.getX() * getScalePixelperMX()) + i][(int)(player.getY() * scalePixelperMY) + j] += player.getHeight();

        players.add(player);
    }

    public void addTarget(Target target) {
        for (int i = 0; i < (int)(target.getWidthX() * getScalePixelperMX()); i++)
            for (int j = 0; j < (int)(target.getWidthY() * getScalePixelperMY()); j++)
                getSurface()[(int)(target.getX() * getScalePixelperMX()) + i][(int)(target.getY() * scalePixelperMY) + j] += target.getHeight();

        targets.add(target);
    }

    public void addMissile(Missile missile) {
        missiles.add(missile);
    }

    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    public void removeMissile(Missile missile) {
        missilesToRemove.add(missile);
    }

    public void removeExplosion(Explosion explosion) {
        explosionsToRemove.add(explosion);
    }

    public void update() {
        missiles.removeAll(missilesToRemove);
        missilesToRemove.clear();
        explosions.removeAll(explosionsToRemove);
        explosionsToRemove.clear();

        players.forEach((player1) -> player1.update(this));
        targets.forEach((target1) -> target1.update(this));
        missiles.forEach((missile1) -> missile1.update(this));
        explosions.forEach((explosion1) -> explosion1.update(this));

        scalePixelperMX =  graphics.getCanvas().getWidth() / data.getMapWidthM();
        scalePixelperMY =  graphics.getCanvas().getHeight() / data.getMapHeightM();

        graphics.setFill(Color.LIGHTGRAY);
        graphics.fillRect(0, 0, graphics.getCanvas().getWidth(), graphics.getCanvas().getHeight());

        players.forEach(player -> player.draw(graphics, scalePixelperMX, scalePixelperMY));
        targets.forEach(target -> target.draw(graphics, scalePixelperMX, scalePixelperMY));
        missiles.forEach(missile -> missile.draw(graphics, scalePixelperMX, scalePixelperMY));
        explosions.forEach(explosion -> explosion.draw(graphics, scalePixelperMX, scalePixelperMY));
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void pause() {
        timeline.pause();
    }

    public Data getData() {
        return data;
    }

    public Player getPlayer() {
        return players.get(playerIndex);
    }

    public Target getTarget() {
        return targets.get(playerIndex);
    }

    public double[][] getSurface() {
        return data.getTerrainZm();
    }

    public double getScalePixelperMX() {
        return scalePixelperMX;
    }

    public double getScalePixelperMY() {
        return scalePixelperMY;
    }
}
