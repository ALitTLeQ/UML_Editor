package umleditor.lib.object;

import javafx.scene.Group;

/**
 * Created by ee830804 on 2016/11/4.
 */
public abstract class Entity extends Group{
    public Entity() {
        super();
    }

    public abstract void onSelected();
    public abstract void unSelected();
    public abstract void setTranslate(double x, double y);

}
