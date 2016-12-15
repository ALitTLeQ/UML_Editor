package umleditor.lib.factory;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import umleditor.lib.handler.DefaultMouseEventHandler;
import umleditor.lib.handler.MouseEventHandler;
import umleditor.lib.object.BasicObject;
import umleditor.lib.object.Composite;
import umleditor.lib.object.Entity;

import java.util.ArrayList;


/**
 * Created by ee830804 on 2016/10/30.
 */
public class ObjectFactory {

    private static MouseEventHandler handler = DefaultMouseEventHandler.getInstance();
    private static double width = 80;
    private static Paint backgroundColor = Paint.valueOf("E4E4E4");

    public static BasicObject create(BasicObject.Type type , double x, double y) {
        BasicObject g = new BasicObject();
        Text text = new Text("");

        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(18));
        text.setTextOrigin(VPos.TOP);

        if (type == BasicObject.Type.Class) {
            Rectangle rectOutter = new Rectangle(width, 90, backgroundColor);
            rectOutter.setStroke(Color.BLACK);

            Rectangle rectInner = new Rectangle(width, 30);
            rectInner.setStroke(Color.BLACK);
            rectInner.setFill(Color.TRANSPARENT);
            rectInner.setY(30);

            text.setWrappingWidth( rectOutter.getWidth() );

            g.getChildren().addAll(rectOutter, rectInner, text);
        }
        else if (type == BasicObject.Type.UseCase) {
            double ellipseWidth = width - 25;
            Ellipse ellipse = new Ellipse(ellipseWidth, 25);
            ellipse.setStroke(Color.BLACK);
            ellipse.setFill(backgroundColor);

            ellipse.setLayoutX(ellipse.getRadiusX());
            ellipse.setLayoutY(ellipse.getRadiusY());

            text.setY(ellipse.getRadiusY()/2);
            text.setWrappingWidth( ellipse.getLayoutBounds().getWidth() );

            g.getChildren().addAll(ellipse, text);
        }
        g.setNameText(text);

        // add ports
        Group ports = new Group();
        ports.relocate(-5, -5);
        Bounds bounds = g.getBoundsInLocal();

        ArrayList<Point2D> points = new ArrayList<>();
        points.add(new Point2D(bounds.getWidth() / 2, 0));
        points.add(new Point2D(bounds.getWidth() / 2, bounds.getHeight()));
        points.add(new Point2D(0, bounds.getHeight() / 2));
        points.add(new Point2D(bounds.getWidth(), bounds.getHeight()/2));


        for(int i=0 ; i<points.size() ; i++) {
            Point2D p = points.get(i);
            Rectangle port = new Rectangle(p.getX(), p.getY(), 10, 10);

            port.setFill(Color.BLACK);
            ports.getChildren().add(port);
            g.pList.add(port);
        }
        g.getChildren().add(ports);
        g.unSelected();
        g.setTranslate(x, y);
        g.setCursor(Cursor.HAND);

        // add eventHandler
        g.setOnMousePressed(handler.getOnMousePressedEvent());
        g.setOnMouseDragged(handler.getOnMouseDraggedEvent());
        g.setOnMouseReleased(handler.getOnMouseReleasedEvent());
        g.setOnMouseDragReleased(handler.getOnMouseDragReleasedEvent());
        g.setOnDragDetected(handler.getOnDragDetectedEventHandler());
        return g;
    }

    public static Composite createComposite(ArrayList<Entity>objs) {
        Composite composite = new Composite();
        composite.group(objs);
        composite.setOnMousePressed(handler.getOnMousePressedEvent());
        composite.setOnMouseDragged(handler.getOnMouseDraggedEvent());
        composite.setOnMouseReleased(handler.getOnMouseReleasedEvent());
        composite.setCursor(Cursor.HAND);

        return composite;
    }
}
