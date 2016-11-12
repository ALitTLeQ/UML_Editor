package umleditor.lib.connection.symbol;

import javafx.scene.Group;

/**
 * Created by ee830804 on 2016/11/2.
 */
public abstract class Symbol extends Group {
    Symbol() {
        createShape();
    }
    abstract void createShape();

    public void setAngleAndPosition(double startX, double endX, double startY, double endY) {
        double angle = Math.atan2(startY - endY, startX - endX);
        double degree = Math.toDegrees(angle);
        setRotate(degree + 90);

        setTranslateX(startX - 10*Math.cos(angle));
        setTranslateY(startY- 10*Math.sin(angle)-10);
    }

}
