package umleditor.lib.object;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Created by ee830804 on 2016/11/1.
 */
public class Composite extends Entity {
    private Rectangle show;

    @Override
    public void setTranslate(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setAbsolute(this.getTranslateX(), this.getTranslateY());
    }

    @Override
    public void setAbsolute(double offsetX, double offsetY) {
        this.getSiblingObjects().forEach( entity -> {
            entity.setAbsolute(offsetX + entity.getTranslateX(), offsetY + entity.getTranslateY());
        });
    }

    public void group(ArrayList<Entity>objList) {
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

    public void unGroup() {
        this.getChildren().remove(show);

        double offsetX = this.getTranslateX();
        double offsetY = this.getTranslateY();

        this.getSiblingObjects().forEach( entity -> {
            entity.setTranslate(offsetX + entity.getTranslateX(), offsetY + entity.getTranslateY());

            // restore absolute
            if(entity instanceof BasicObject) {
                BasicObject basicObject = ((BasicObject)entity);
                Point2D absolute = basicObject.getAbsolute();
                basicObject.setAbsolute(absolute.getX() - offsetX, absolute.getY() - offsetY);
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
