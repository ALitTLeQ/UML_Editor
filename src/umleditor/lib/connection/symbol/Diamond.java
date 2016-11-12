package umleditor.lib.connection.symbol;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Created by ee830804 on 2016/11/3.
 */
public class Diamond extends Symbol {
    public Diamond(double startX, double endX, double startY, double endY) {
        super();
        setAngleAndPosition(startX, endX, startY, endY);
    }

    @Override
    void createShape() {
        //Polygon outter = new Polygon(new double[]{0, 0, 10, 20, -10, 20});
        Polygon outter = new Polygon(new double[]{0, 0, 10, 10, 0, 20, -10, 10});
        Polygon inner = new Polygon(new double[]{0, 1, 9, 10, 0, 19, -9, 10});
        outter.setFill(Color.BLACK);
        inner.setFill(Color.WHITE);
        getChildren().addAll(outter,inner);
    }
}
