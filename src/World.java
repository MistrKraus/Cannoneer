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
import java.util.DoubleSummaryStatistics;
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

    private final List<Player> playersToRemove = new ArrayList<>();
    private final List<Target> targetsToRemove = new ArrayList<>();
    private final List<Missile> missilesToRemove = new ArrayList<>();
    private final List<Explosion> explosionsToRemove = new ArrayList<>();

    private double scaleX;
    private double scaleY;
    private double scalePixelperMX;
    private double scalePixelperMY;

    // TODO budouci moznost iterace
    private int playerIndex = 0;

    private boolean runs = false;

    public World(GraphicsContext graphics, Data data) {
        this.graphics = graphics;
        this.data = data;
        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void addPlayer(Player player) {
//        for (int i = 0; i < (int)(player.getWidthX() * scaleX); i++)
//            for (int j = 0; j < (int)(player.getWidthY() * scaleY); j++)
//                setSurface((int)(player.getX() * getScalePixelperMX()) + i, (int)(player.getY() * scalePixelperMY) + j,
//                        getSurface()[(int)(player.getX() * getScalePixelperMX()) + i][(int)(player.getY() * scalePixelperMY) + j] +
//                                player.getHeight());
/*
        double columnCount = player.getWidthX() / data.getDeltaXm();
        double rowCount = player.getHeight() / data.getDeltaYm();

        int x = (int)(player.getX() / data.getDeltaXm());
        int y = (int)(player.getY() / data.getDeltaYm());

        if (columnCount < 1)
            columnCount = 1;

        if (rowCount < 1)
            rowCount = 1;

        for (int i = (int)-Math.floor(columnCount / 2); i < (int)Math.ceil(columnCount / 2); i ++)
            for (int j = (int)-Math.floor(rowCount / 2); j < (int)Math.ceil(rowCount / 2); j++)
                setSurface(x + i,y + j, getSurface()[i][j] + player.getHeight());
*/
        players.add(player);
    }

    public void addTarget(Target target) {
        double columnCount = target.getWidthX() / data.getDeltaXm();
        double rowCount = target.getHeight() / data.getDeltaYm();

        int x = (int)(target.getX() / data.getDeltaXm());
        int y = (int)(target.getY() / data.getDeltaYm());

        if (columnCount < 1)
            columnCount = 1;

        if (rowCount < 1)
            rowCount = 1;

        for (int i = (int)-Math.floor(columnCount / 2); i < (int)Math.ceil(columnCount / 2); i ++)
            for (int j = (int)-Math.floor(rowCount / 2); j < (int)Math.ceil(rowCount / 2); j++)
                setSurface(x + i, y + j, getSurface()[x + i][y + j] + target.getHeight());

        targets.add(target);
    }

    public void addMissile(Missile missile) {
        missiles.add(missile);
    }

    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    public void removeTarget(Target target) {
        double columnCount = target.getWidthX() / data.getDeltaXm();
        double rowCount = target.getHeight() / data.getDeltaYm();

        int x = (int)(target.getX() / data.getDeltaXm());
        int y = (int)(target.getY() / data.getDeltaYm());

        if (columnCount < 1)
            columnCount = 1;

        if (rowCount < 1)
            rowCount = 1;

        for (int i = (int)-Math.floor(columnCount / 2); i < (int)Math.ceil(columnCount / 2); i ++)
            for (int j = (int)-Math.floor(rowCount / 2); j < (int)Math.ceil(rowCount / 2); j++)
                setSurface(x + i, y + j, getSurface()[x + i][y + j] - target.getHeight());

        targetsToRemove.add(target);
    }

    public void removeMissile(Missile missile) {
        System.out.println("Strela dopadla");
        missilesToRemove.add(missile);
    }

    public void removeExplosion(Explosion explosion) {
        explosionsToRemove.add(explosion);
    }

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

        players.forEach((player1) -> player1.update(this));
        targets.forEach((target1) -> target1.update(this));
        missiles.forEach((missile1) -> missile1.update(this));
        explosions.forEach((explosion1) -> explosion1.update(this));

        draw();
    }

    public void draw() {
        scalePixelperMX =  graphics.getCanvas().getWidth() / data.getMapWidthM();
        scalePixelperMY =  graphics.getCanvas().getHeight() / data.getMapHeightM();
        scaleX = 1 / data.getDeltaXm();
        scaleY = 1 / data.getDeltaYm();

        graphics.setFill(Color.LIGHTGRAY);
        graphics.fillRect(0, 0, graphics.getCanvas().getWidth(), graphics.getCanvas().getHeight());

        players.forEach(player -> player.draw(graphics, scalePixelperMX, scalePixelperMY));
        targets.forEach(target -> target.draw(graphics, scalePixelperMX, scalePixelperMY));
        missiles.forEach(missile -> missile.draw(graphics, scalePixelperMX, scalePixelperMY));
        explosions.forEach(explosion -> explosion.draw(graphics, scalePixelperMX, scalePixelperMY));
    }

    public void start() {
        runs = true;
        timeline.play();
    }

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

    public double[][] getSurface() {
        return data.getTerrainZm();
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

    public void setSurface(int x, int y, double value) {
        data.setTerrainZm(x, y, value);
    }

}
