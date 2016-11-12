package umleditor.lib.handler;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import umleditor.gui.UIController;

/**
 * Created by ee830804 on 2016/10/30.
 */
public interface MouseEventHandler {
    public void setUIController(UIController uic);

    public EventHandler<MouseEvent> getOnMousePressedEvent();
    public EventHandler<MouseEvent> getOnMouseReleasedEvent();
    public EventHandler<MouseEvent> getOnMouseDraggedEvent();
    public EventHandler<MouseEvent> getOnMouseDragReleasedEvent();
    public EventHandler<MouseEvent> getOnDragDetectedEventHandler();

}
