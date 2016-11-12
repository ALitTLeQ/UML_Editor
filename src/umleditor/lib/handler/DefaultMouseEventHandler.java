package umleditor.lib.handler;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import umleditor.gui.UIController;
import umleditor.lib.object.BasicObject;
import umleditor.lib.object.Composite;
import umleditor.lib.object.Entity;

import static java.lang.Math.*;


/**
 * Created by ee830804 on 2016/10/30.
 */
public class DefaultMouseEventHandler implements MouseEventHandler{

    private DefaultMouseEventHandler() {
    }


    private static MouseEventHandler INSTANCE;
    public static MouseEventHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultMouseEventHandler();
        }
        return INSTANCE;
    }

    private UIController uiController;
    @Override
    public void setUIController(UIController uic){
        this.uiController = uic;
    }

    private double beginSceneX, beginSceneY, translateX, translateY, beginLocalX, beginLocalY;
    private BasicObject fromObject;
    private int fromPortIdx;

    @Override
    public EventHandler<MouseEvent> getOnMousePressedEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                beginSceneX = e.getSceneX();
                beginSceneY = e.getSceneY();
                beginLocalX = e.getX();
                beginLocalY = e.getY();

                if(e.getSource() instanceof Pane) {
                    System.out.println("pane");
                    if (uiController.mode == UIController.Mode.SELECT) {
                        uiController.clearSelected();


                    } else if (uiController.mode == UIController.Mode.OBJECT) {
                        uiController.addObject(e.getX(), e.getY());
                    }
                }
                else if(e.getSource() instanceof Composite) {
                    if (uiController.mode == UIController.Mode.SELECT) {
                        System.out.println("composite Pressed");
                        Composite composite = ((Composite) e.getSource());
                        uiController.clearSelected();
                        uiController.addSelected(composite);


                        translateX = composite.getTranslateX();
                        translateY = composite.getTranslateY();
                        e.consume();
                    }
                }
                else if(e.getSource() instanceof BasicObject) {
                    BasicObject pressedObject = (BasicObject) (e.getSource());

                    translateX = pressedObject.getTranslateX();
                    translateY = pressedObject.getTranslateY();

                    if(uiController.mode == UIController.Mode.SELECT){
                        uiController.clearSelected();
                        uiController.addSelected(pressedObject);
                    }
                    else if(uiController.mode == UIController.Mode.CONNECTION){
                        uiController.pullLine.setStartX(translateX + e.getX());
                        uiController.pullLine.setStartY(translateY + e.getY());
                        uiController.pullLine.setEndX(translateX + e.getX());
                        uiController.pullLine.setEndY(translateY + e.getY());
                    }
                }
                e.consume();
            }
        };
    }


    @Override
    public EventHandler<MouseEvent> getOnDragDetectedEventHandler(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                if(uiController.mode == UIController.Mode.CONNECTION){
                    BasicObject dragObject = (BasicObject) (e.getSource());
                    fromObject = dragObject;
                    fromPortIdx = choosePort(dragObject, e.getX(), e.getY());

                    dragObject.startFullDrag();
                    dragObject.setMouseTransparent(true);
                }
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseDraggedEvent() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                double offsetX = e.getSceneX() - beginSceneX;
                double offsetY = e.getSceneY() - beginSceneY;
                double newTranslateX = translateX + offsetX;
                double newTranslateY = translateY + offsetY;

                if(e.getSource() instanceof Pane) {
                    if (uiController.mode == UIController.Mode.SELECT) {
                        double minX = min(e.getX(),beginLocalX);
                        double minY = min(e.getY(),beginLocalY);
                        double maxX = max(e.getX(),beginLocalX);
                        double maxY = max(e.getY(),beginLocalY);

                        uiController.multiSelectRect.toFront();
                        uiController.multiSelectRect.setVisible(true);
                        uiController.multiSelectRect.relocate(minX, minY);
                        uiController.multiSelectRect.setWidth(maxX - minX);
                        uiController.multiSelectRect.setHeight(maxY - minY);
                    }
                    e.consume();
                }
                else if(e.getSource() instanceof Composite) {
                    //System.out.println("Composite Drgged");
                        if (uiController.mode == UIController.Mode.SELECT) {
                            Composite composite = (Composite) (e.getSource());

                            System.out.println(newTranslateX);
                            System.out.println(composite.getBoundsInParent().getMinX());

                            composite.setTranslate(newTranslateX, newTranslateY);
                            /*
                            if(uiController.getBounds().getMinX() < composite.getBoundsInParent().getMinX())
                            {

                                composite.setTranslate(newTranslateX, newTranslateY);
                            }
                            else if(uiController.getBounds().getMinX() >= composite.getBoundsInParent().getMinX()){
                                //newTranslateX +=  5;
                                composite.setTranslate(composite.getLayoutX()+5, newTranslateY);
                            }

                            System.out.println(newTranslateX + "\n--------\n");

                            */
                    }
                    e.consume();

                }
                else if(e.getSource() instanceof BasicObject) {
                    if (uiController.mode == UIController.Mode.SELECT) {
                        BasicObject draggedObject = (BasicObject) (e.getSource());

                        draggedObject.setTranslate(newTranslateX, newTranslateY);

                        if(uiController.getBounds().getMinX() >= draggedObject.getBoundsInParent().getMinX()) {
                            newTranslateX = uiController.getBounds().getMinX() + 5;
                        }
                        if(uiController.getBounds().getMinY() >= draggedObject.getBoundsInParent().getMinY()) {
                            newTranslateY =  uiController.getBounds().getMinY() + 5;
                        }

                        draggedObject.setTranslate(newTranslateX, newTranslateY);

                        e.consume();
                    }
                    else if (uiController.mode == UIController.Mode.CONNECTION) {
                        uiController.pullLine.toFront();
                        uiController.pullLine.setEndX(translateX + e.getX());
                        uiController.pullLine.setEndY(translateY + e.getY());
                    }
                    e.consume();
                }


            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseReleasedEvent()
    {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(e.getSource() instanceof Pane) {
                    if (uiController.mode == UIController.Mode.SELECT) {

                        Bounds selectBounds = uiController.multiSelectRect.getBoundsInParent();
                        uiController.clearSelected();

                        for(Entity obj : uiController.getObjects())
                        {
                            if(selectBounds.contains(obj.getBoundsInParent()))
                            {
                                uiController.addSelected(obj);
                            }
                        }

                        uiController.multiSelectRect.setWidth(0);
                        uiController.multiSelectRect.setHeight(0);
                        uiController.multiSelectRect.setVisible(false);

                    }
                }
                else if(e.getSource() instanceof Composite) {
                    if (uiController.mode == UIController.Mode.SELECT) {
                        e.consume();
                    }
                }
                else if(e.getSource() instanceof BasicObject) {
                    if (uiController.mode == UIController.Mode.CONNECTION) {

                        uiController.pullLine.setStartX(0);
                        uiController.pullLine.setStartY(0);
                        uiController.pullLine.setEndX(0);
                        uiController.pullLine.setEndY(0);

                        BasicObject releasedObject = (BasicObject) (e.getSource());
                        releasedObject.setMouseTransparent(false);
                    }
                }
                e.consume();
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> getOnMouseDragReleasedEvent(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                BasicObject dropObject = (BasicObject) (e.getSource());
                System.out.println(dropObject.getName());

                int toPortIdx = choosePort(dropObject, e.getX(), e.getY());
                uiController.addConnection(fromObject, fromPortIdx, dropObject, toPortIdx);

                e.consume();
            }
        };
    }


    public int choosePort(BasicObject obj, double x, double y){
        Point2D mousePoint = new Point2D(x, y);
        Rectangle port = new Rectangle(0, 0);
        double minDist = Double.MAX_VALUE;
        for(Rectangle rect : obj.pList)
        {
            Point2D fp = new Point2D(rect.getX(), rect.getY());
            if(fp.distance(mousePoint) < minDist)
            {
                port = rect;
                minDist = fp.distance(mousePoint);
            }
        }
        return obj.pList.indexOf(port);

    }

}
