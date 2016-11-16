package umleditor.lib.tool;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by ee830804 on 2016/11/16.
 */
public class AuxiliaryLine extends Line {
    public AuxiliaryLine(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);

        this.setStroke(Color.GRAY);
        this.setStrokeWidth(1);
        this.setMouseTransparent(true);
    }

    public void setStart(double x, double y) {
        this.setStartX(x);
        this.setStartY(y);
    }

    public void setEnd(double x, double y) {
        this.setEndX(x);
        this.setEndY(y);
    }

    public void initialize(double x, double y) {
        this.setStart(x, y);
        this.setEnd(x, y);
    }
}
