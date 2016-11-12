package umleditor.lib.connection;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import umleditor.lib.object.BasicObject;
import umleditor.lib.connection.symbol.Symbol;

/**
 * Created by ee830804 on 2016/10/30.
 */
public class ConnectionLine extends Group {
    public static enum Type {ASSOCIATION, GENERALIZATION, COMPOSITION};

    private BasicObject from;
    private int fromPortidx;
    private BasicObject to;
    private int toPortidx;

    public ConnectionLine(){
        super();
    }

    public void setFrom(BasicObject obj, int idx){
        from = obj;
        fromPortidx = idx;
    }

    public void setTo(BasicObject obj, int idx){
        to = obj;
        toPortidx = idx;
    }

    public Point2D getFromPort()
    {
        Point2D pTranslate =from.getTranslatePoint();
        Rectangle rect = from.pList.get(fromPortidx);
        return (new Point2D(rect.getX(), rect.getY())).add(pTranslate);
    }

    public Point2D getToPort()
    {
        Point2D pTranslate =to.getTranslatePoint();
        Rectangle rect = to.pList.get(toPortidx);
        return (new Point2D(rect.getX(), rect.getY())).add(pTranslate);
    }

    public void update(){
        Point2D fromPort = getFromPort();
        Point2D toPort = getToPort();

        for(Node obj: getChildren()){
            if(obj instanceof Line){
                Line line = (Line)obj;
                line.setStartX(fromPort.getX());
                line.setStartY(fromPort.getY());
                line.setEndX(toPort.getX());
                line.setEndY(toPort.getY());
            }
            else if(obj instanceof Symbol)
            {
                ((Symbol)obj).setAngleAndPosition(fromPort.getX(), toPort.getX(), fromPort.getY(), toPort.getY());
            }
        }
    }

}
