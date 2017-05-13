import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

/**
 * Created by kraus on 14.05.2017.
 */
public class Banner implements IDrawable {

    private double x;
    private double y;
    private double textWidth;
    private double textHeight;
    private Text txt;

    private Font font = Font.font("INPACT", FontWeight.BOLD,  10);
    private IHittable hittableThing;

    private static final int BORDER = 5;

    public Banner(IHittable hittableThing) {
        this.hittableThing = hittableThing;

        txt = new Text(Integer.toString(hittableThing.getHp()));
        txt.setFont(font);

        textWidth = txt.getLayoutBounds().getWidth();
        textHeight = txt.getLayoutBounds().getHeight();

        x = 0;
        y = 0;
    }

    @Override
    public void draw(GraphicsContext g, double scaleMperPixelX, double scaleMperPixelY) {

        g.setFill(Color.WHITE);
        g.setGlobalAlpha(0.3);

        g.fillRect(x, y, 2 * BORDER + textWidth, BORDER + textHeight);

        g.setGlobalAlpha(1);

        g.setFill(Color.BLACK);
        g.setFont(font);
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(txt.getText(), x + BORDER + textWidth / 2, y + BORDER + textHeight / 2);

    }

    @Override
    public void update(World world) {
        double mapX = hittableThing.getMapX();
        double mapY = hittableThing.getMapY();

        x = mapX + hittableThing.getWidthX() + BORDER;
        y = mapY - 2 * BORDER - textHeight;

        Canvas c = world.getGraphics().getCanvas();

        if ((x + 4 * BORDER + textWidth) > c.getWidth())
            x = mapX - 4 * BORDER - textWidth;

        if ((y - 4 * BORDER + textHeight) < 0)
            y = mapY + 2 * BORDER;

        txt.setText(Integer.toString(hittableThing.getHp()));
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
