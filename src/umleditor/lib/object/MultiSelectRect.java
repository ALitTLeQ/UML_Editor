package umleditor.lib.object;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by ee830804 on 2016/11/16.
 */
public class MultiSelectRect extends Rectangle {
    public MultiSelectRect(double x, double y, double w, double h){
        super(x, y, w, h);

        this.setStroke(Color.BLUE);
        this.setStrokeWidth(1);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
    }

    public void setRegion(double x1, double y1, double x2, double y2){
        double minX = min(x1,x2);
        double minY = min(y1,y2);
        double maxX = max(x1,x2);
        double maxY = max(y1,y2);

        this.setVisible(true);
        this.relocate(minX, minY);
        this.setWidth(maxX - minX);
        this.setHeight(maxY - minY);
    }

    public void initialize(){
        this.setWidth(0);
        this.setHeight(0);
        this.setVisible(false);
    }
}
