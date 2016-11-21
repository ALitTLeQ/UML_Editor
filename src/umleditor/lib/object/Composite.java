package umleditor.lib.object;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ee830804 on 2016/11/1.
 */
public class Composite extends Entity {
    private Rectangle show;

    public Composite() {
        super();
    }

    public void addAllObject(ArrayList<Entity>objList) {
        for(Entity obj: objList)
        {
            this.getChildren().add(obj);
        }

        // add group region
        Bounds bounds = getBoundsInLocal();
        show = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        show.setFill(Color.TRANSPARENT);
        show.setStroke(Color.RED);
        show.setStrokeWidth(1.5);
        show.toFront();

        this.getChildren().add(show);
    }

    @Override
    public void setTranslate(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.updateChildrenOffset(this.getTranslateX(), this.getTranslateY());
    }

    public void updateChildrenOffset(double offsetX, double offsetY) {
        this.getChildren().forEach( obj -> {
            if(obj instanceof BasicObject) {
                BasicObject basicObject = ((BasicObject)obj);
                basicObject.setOffset(offsetX, offsetY);

                ((BasicObject)obj).updateConnection();
            }
            else if(obj instanceof Composite) {
                Composite composite = ((Composite)obj);
                composite.updateChildrenOffset(offsetX + composite.getTranslateX(), offsetY + composite.getTranslateY());
            }
        });
    }

    public void unGroup() {
        this.getChildren().remove(show);

        double offsetX = this.getTranslateX();
        double offsetY = this.getTranslateY();

        this.getChildren().forEach( obj -> {
            if(obj instanceof BasicObject) {
                BasicObject basicObject = ((BasicObject)obj);
                basicObject.setTranslate(offsetX + basicObject.getTranslateX(), offsetY + basicObject.getTranslateY());
                basicObject.setOffset(0, 0);
                basicObject.updateConnection();
            }
            else if(obj instanceof Composite) {
                Composite composite = ((Composite)obj);
                composite.setTranslate(offsetX + composite.getTranslateX(), offsetY + composite.getTranslateY());
            }
        });
        this.show = null;

    }

    public ArrayList<Entity> getSiblingObjects() {
        ArrayList<Entity> objects= new ArrayList<>();
        this.getChildren().forEach( obj -> {
            if (obj instanceof Entity) {
                objects.add((Entity) obj);
            }
        });
        return objects;
    }

    @Override
    public void onSelected() {
        this.getSiblingObjects().forEach(Entity::onSelected);
        show.setStroke(Color.RED);
    }

    @Override
    public void unSelected() {
        this.getSiblingObjects().forEach(Entity::unSelected);
        show.setStroke(Color.TRANSPARENT);
    }



}
