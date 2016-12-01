package umleditor.lib.object;

import javafx.geometry.Point2D;
import javafx.scene.Group;
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
    private double absoluteX;
    private double absoluteY;

    public BasicObject() {
        super();
        connectionTo = new ArrayList<>();
        connectionFrom = new ArrayList<>();
        pList = new ArrayList<>();
        absoluteX = 0;
        absoluteY = 0;
    }

    public void addConnectionFrom(ConnectionLine c) {
        connectionFrom.add(c);
    }
    public void addConnectionTo(ConnectionLine c) {
        connectionTo.add(c);
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
        absoluteX += (x - getTranslateX());
        absoluteY += (y - getTranslateY());
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.updateConnection();
    }

    @Override
    public void setAbsolute(double x, double y) {
        absoluteX = x;
        absoluteY = y;
        this.updateConnection();
    }

    public void updateConnection() {
        connectionFrom.forEach(ConnectionLine::update);
        connectionTo.forEach(ConnectionLine::update);
    }

    public String getName() {
        return name.getText();
    }

    public Point2D getAbsolute() {
        return new Point2D(absoluteX, absoluteY);
    }

    @Override
    public void onSelected() {
        this.pList.forEach( obj -> obj.setStyle("visibility: visible;"));
    }

    @Override
    public void unSelected() {
        this.pList.forEach( obj -> obj.setStyle("visibility: hidden;"));
    }


}
