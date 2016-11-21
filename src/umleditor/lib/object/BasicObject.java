package umleditor.lib.object;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import umleditor.lib.connection.ConnectionLine;

import java.util.ArrayList;

/**
 * Created by ee830804 on 2016/10/30.
 */
public class BasicObject extends Entity {
    public enum Type {Class, UseCase};

    private Text name;

    public ArrayList<Rectangle> pList;
    private final ArrayList<ConnectionLine> connectionTo;
    private final ArrayList<ConnectionLine> connectionFrom;
    private double offsetX;
    private double offsetY;

    public BasicObject() {
        super();
        connectionTo = new ArrayList<>();
        connectionFrom = new ArrayList<>();
        pList = new ArrayList<>();
    }

    public void addConnectionFrom(ConnectionLine c) {
        connectionFrom.add(c);
    }
    public void addConnectionTo(ConnectionLine c) {
        connectionTo.add(c);
    }
    public ArrayList<ConnectionLine> getConnectionFrom() {
        return connectionFrom;
    }
    public ArrayList<ConnectionLine> getConnectionTo() {
        return connectionTo;
    }

    public void setName(String _name)
    {
        name.setText(_name);
    }
    public void setNameText(Text _text)
    {
        name = _text;
    }

    @Override
    public void setTranslate(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.updateConnection();
    }

    @Override
    public void setOffset(double x, double y) {
        offsetX = x;
        offsetY = y;
    }

    public void updateConnection() {
        this.getConnectionFrom().forEach(ConnectionLine::update);
        this.getConnectionTo().forEach(ConnectionLine::update);
    }

    public String getName() {
        return name.getText();
    }

    public Point2D getTranslatePoint() {
        return new Point2D(offsetX+this.getTranslateX(), offsetY+this.getTranslateY());
    }

    @Override
    public void onSelected() {
        this.getChildren().forEach( obj -> {
            if(obj instanceof Group) {
                obj.setStyle("visibility: visible;");
            }
        });
    }

    @Override
    public void unSelected() {
        this.getChildren().forEach( obj -> {
            if(obj instanceof Group) {
                obj.setStyle("visibility: hidden;");
            }
        });
    }


}
