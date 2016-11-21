package umleditor.lib.handler;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import umleditor.gui.UIController;
import umleditor.lib.object.BasicObject;
import umleditor.lib.object.Entity;


/**
 * Created by ee830804 on 2016/10/30.
 */
public class DefaultMouseEventHandler implements MouseEventHandler {

    private DefaultMouseEventHandler() {}

    private static MouseEventHandler INSTANCE;
    public static MouseEventHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultMouseEventHandler();

        }
        return INSTANCE;
    }

    private UIController uiController;
    private Point2D beginScene = new Point2D(0, 0);
    private Point2D translate = new Point2D(0,0);
    private Point2D beginLocal = new Point2D(0, 0);
    private BasicObject fromObject;
    private int fromPortIdx;

    @Override
    public void setUIController(UIController uic) {
        this.uiController = uic;
    }

    @Override
    public EventHandler<MouseEvent> getOnMousePressedEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                beginScene = new Point2D(e.getSceneX(), e.getSceneY());
                beginLocal = new Point2D(e.getX(), e.getY());

                if(e.getSource() instanceof Pane) {
                    if(uiController.mode == UIController.Mode.SELECT) {
                        uiController.clearSelected();
                    }
                    else if(uiController.mode == UIController.Mode.OBJECT) {
                        uiController.addBasicObject(e.getX(), e.getY());
                    }
                }
                else if(e.getSource() instanceof Entity) {
                    Entity entity = ((Entity) e.getSource());
                    translate = new Point2D(entity.getTranslateX(), entity.getTranslateY());

                    if(uiController.mode == UIController.Mode.SELECT) {
                        // select entity
                        uiController.clearSelected();
                        uiController.addSelected(entity);
                        e.consume();
                    }
                    else if(uiController.mode == UIController.Mode.CONNECTION
                            && entity instanceof BasicObject) {
                        // only connect basicObjects
                        uiController.auxiliaryLine.initialize(translate.getX() + e.getX(), translate.getY() + e.getY());
                    }
                }
                e.consume();
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnDragDetectedEventHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                if(uiController.mode == UIController.Mode.CONNECTION) {
                    fromObject = (BasicObject) (e.getSource());
                    fromPortIdx = choosePort(fromObject, e.getX(), e.getY());

                    fromObject.startFullDrag();
                    fromObject.setMouseTransparent(true);
                }
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseDraggedEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                double offsetX = e.getSceneX() - beginScene.getX();
                double offsetY = e.getSceneY() - beginScene.getY();
                double newTranslateX = translate.getX() + offsetX;
                double newTranslateY = translate.getY() + offsetY;

                if(e.getSource() instanceof Pane) {
                    if(uiController.mode == UIController.Mode.SELECT) {
                        uiController.multiSelectRect.toFront();
                        uiController.multiSelectRect.setRegion(e.getX(), e.getY(), beginLocal.getX(), beginLocal.getY());
                    }
                }
                else if(e.getSource() instanceof Entity) {
                    Entity entity = (Entity) (e.getSource());

                    if(uiController.mode == UIController.Mode.SELECT) {
                        // move entity
                        entity.setTranslate(newTranslateX, newTranslateY);

                        // set entity movable area
                        double dx = uiController.getBounds().getMinX() - entity.getBoundsInParent().getMinX();
                        double dy = uiController.getBounds().getMinY() - entity.getBoundsInParent().getMinY();
                        if(dx >= 0) {
                            newTranslateX = entity.getTranslateX() + dx + 5;
                        }
                        if(dy >= 0) {
                            newTranslateY = entity.getTranslateY() + dy + 5;
                        }
                        entity.setTranslate(newTranslateX, newTranslateY);

                    }
                    else if(uiController.mode == UIController.Mode.CONNECTION
                            && entity instanceof BasicObject) {
                        uiController.auxiliaryLine.toFront();
                        uiController.auxiliaryLine.setEnd(translate.getX() + e.getX(), translate.getY() + e.getY());
                    }
                }
                e.consume();
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseReleasedEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(e.getSource() instanceof Pane) {
                    if(uiController.mode == UIController.Mode.SELECT) {
                        // multi select
                        Bounds selectBounds = uiController.multiSelectRect.getBoundsInParent();
                        uiController.clearSelected();

                        for(Entity obj : uiController.getObjects()) {
                            if(selectBounds.contains(obj.getBoundsInParent())) {
                                uiController.addSelected(obj);
                            }
                        }
                        uiController.multiSelectRect.initialize();
                    }
                }
                else if(e.getSource() instanceof BasicObject) {
                    if (uiController.mode == UIController.Mode.CONNECTION) {
                        // release auxiliary line
                        uiController.auxiliaryLine.initialize(0, 0);

                        BasicObject releasedObject = (BasicObject) (e.getSource());
                        releasedObject.setMouseTransparent(false);
                    }
                }
                e.consume();
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseDragReleasedEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // create connection line
                BasicObject toObject = (BasicObject) (e.getSource());
                int toPortIdx = choosePort(toObject, e.getX(), e.getY());
                uiController.addConnection(fromObject, fromPortIdx, toObject, toPortIdx);

                e.consume();
            }
        };
    }

    public int choosePort(BasicObject obj, double x, double y) {
        // choose nearest port
        Point2D mousePoint = new Point2D(x, y);
        Rectangle port = new Rectangle(0, 0);
        double minDist = Double.MAX_VALUE;

        for(Rectangle rect : obj.pList) {
            Point2D fp = new Point2D(rect.getX(), rect.getY());
            if(fp.distance(mousePoint) < minDist) {
                port = rect;
                minDist = fp.distance(mousePoint);
            }
        }
        return obj.pList.indexOf(port);
    }

}
