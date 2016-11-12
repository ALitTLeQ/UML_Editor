package umleditor.lib.connection.symbol;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by ee830804 on 2016/11/2.
 */
public class Triangle extends Symbol {
    public Triangle(double startX, double endX, double startY, double endY) {
        super();
        setAngleAndPosition(startX, endX, startY, endY);
    }

    @Override
    void createShape() {
        Polygon outter = new Polygon(new double[]{0, 0, 10, 20, -10, 20});
        Polygon inner = new Polygon(new double[]{0, 1, 9, 19, -9, 19});
        outter.setFill(Color.BLACK);
        inner.setFill(Color.WHITE);
        getChildren().addAll(outter, inner);

    }
}
