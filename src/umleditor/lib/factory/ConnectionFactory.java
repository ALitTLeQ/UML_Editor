package umleditor.lib.factory;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import umleditor.lib.object.BasicObject;
import umleditor.lib.connection.ConnectionLine;
import umleditor.lib.connection.symbol.Diamond;
import umleditor.lib.connection.symbol.Triangle;


/**
 * Created by ee830804 on 2016/10/30.
 */
public class ConnectionFactory {

    public static ConnectionLine create(ConnectionLine.Type type, BasicObject fromObject, int fromPortIdx, BasicObject toObject, int toPortIdx) {
        ConnectionLine g = new ConnectionLine();

        Rectangle fromRect = fromObject.pList.get(fromPortIdx);
        Rectangle toRect = toObject.pList.get(toPortIdx);

        Point2D fromPort = new Point2D(fromRect.getX(), fromRect.getY());
        Point2D toPort = new Point2D(toRect.getX(), toRect.getY());
        Point2D fromTranslate = fromObject.getAbsolute();
        Point2D toTranslate = toObject.getAbsolute();

        fromPort = fromPort.add(fromTranslate);
        toPort = toPort.add(toTranslate);

        // render Line
        Line line;
        line = new Line(fromPort.getX(), fromPort.getY(), toPort.getX(), toPort.getY());
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(1);
        line.setFill(null);

        g.getChildren().add(line);

        // render Symbol
        if(type == ConnectionLine.Type.GENERALIZATION) {
            Triangle triangle = new Triangle(fromPort.getX(), toPort.getX(), fromPort.getY(), toPort.getY());
            g.getChildren().add(triangle);
        }
        else if(type == ConnectionLine.Type.COMPOSITION) {
            Diamond diamond = new Diamond(fromPort.getX(), toPort.getX(), fromPort.getY(), toPort.getY());
            g.getChildren().add(diamond);
        }

        g.setFrom(fromObject, fromPortIdx);
        g.setTo(toObject, toPortIdx);
        fromObject.addConnectionTo(g);
        toObject.addConnectionFrom(g);

        return g;
    }
}
