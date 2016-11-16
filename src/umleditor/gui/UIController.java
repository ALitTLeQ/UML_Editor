package umleditor.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import umleditor.lib.object.BasicObject;
import umleditor.lib.object.Composite;
import umleditor.lib.object.Entity;
import umleditor.lib.connection.ConnectionLine;
import umleditor.lib.factory.ConnectionFactory;
import umleditor.lib.factory.ObjectFactory;
import umleditor.lib.handler.DefaultMouseEventHandler;
import umleditor.lib.handler.MouseEventHandler;
import umleditor.lib.connection.AlignedLine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UIController implements Initializable {
    public enum Mode { SELECT, CONNECTION, OBJECT };
    public Mode mode = null;
    private BasicObject.Type objectType = BasicObject.Type.Class;
    private ConnectionLine.Type connectionType;
    private Button selectedBtn = null;

    public static ArrayList<Entity> objects = new ArrayList<>();
    public static ArrayList<Entity> selectedObjects = new ArrayList<>();

    public Rectangle multiSelectRect;
    public AlignedLine alignedLine;

    @FXML
    private Pane pane;
    @FXML
    private Group root;

    private Bounds bounds;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MouseEventHandler handler = DefaultMouseEventHandler.instance();
        handler.setUIController(this);

        pane.setOnMousePressed(handler.getOnMousePressedEvent());
        pane.setOnMouseDragged(handler.getOnMouseDraggedEvent());
        pane.setOnMouseReleased(handler.getOnMouseReleasedEvent());
        bounds = pane.getBoundsInLocal();

        multiSelectRect = new Rectangle(0, 0, 0, 0);
        multiSelectRect.setStroke(Color.BLUE);
        multiSelectRect.setStrokeWidth(1);
        multiSelectRect.setStrokeLineCap(StrokeLineCap.ROUND);
        multiSelectRect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

        alignedLine = new AlignedLine(0, 0, 0, 0);


        pane.setStyle("-fx-background-color: #FFFFFF;");

        root.getChildren().addAll(alignedLine, multiSelectRect );

    }

    public void selectButton(Button btn){
        if(selectedBtn != null)
        {
            selectedBtn.getStyleClass().remove("btn_black");
        }
        btn.getStyleClass().add("btn_black");
        selectedBtn = btn;
    }

    @FXML
    private void exit(ActionEvent e)
    {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void selectClick(ActionEvent e)
    {
        mode = Mode.SELECT;
        selectButton(((Button)e.getSource()));
        e.consume();
    }

    @FXML
    private void connectionClick(ActionEvent e) {
        mode = Mode.CONNECTION;
        String connectionId = ((Button) e.getSource()).getId();
        if(connectionId.equals("association"))
        {
            connectionType = ConnectionLine.Type.ASSOCIATION;
        }
        else if(connectionId.equals("generalization"))
        {
            connectionType = ConnectionLine.Type.GENERALIZATION;
        }
        else if(connectionId.equals("composition"))
        {
            connectionType = ConnectionLine.Type.COMPOSITION;
        }
        selectButton(((Button)e.getSource()));
        e.consume();
    }

    @FXML
    private void objectClick(ActionEvent e) {
        mode = Mode.OBJECT;
        String objectId = ((Button) e.getSource()).getId();
        if(objectId.equals("class"))
        {
            objectType = BasicObject.Type.Class;
        }
        else if(objectId.equals("useCase"))
        {
            objectType = BasicObject.Type.UseCase;
        }
        selectButton(((Button)e.getSource()));
        e.consume();
    }

    @FXML
    public void changObjectName(ActionEvent e)
    {
        if(selectedObjects.size() == 1)
        {
            if(selectedObjects.get(0) instanceof BasicObject)
            {
                BasicObject obj = (BasicObject)selectedObjects.get(0);
                TextInputDialog dialog = new TextInputDialog(obj.getName());
                dialog.setTitle("Edit");
                dialog.setHeaderText("Change Object's Name");
                dialog.setContentText("Please enter the object's name:");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(name -> obj.setName(name));
            }
        }


    }

    @FXML
    public void groupObjects(ActionEvent e)
    {
        if(selectedObjects.size() > 0)
        {
            Composite composite = ObjectFactory.createComposite(selectedObjects);
            objects.removeAll(composite.getChildren());
            objects.add(composite);
            root.getChildren().add(composite);
        }
    }

    @FXML
    public void ungroupObjects(ActionEvent e)
    {
        if(selectedObjects.size() == 1)
        {
            if(selectedObjects.get(0) instanceof Composite)
            {

                System.out.println(selectedObjects.get(0));
                Composite composite = (Composite)selectedObjects.get(0);

                Group parent = (Group)composite.getParent();

                composite.unGroup();

                objects.addAll(composite.getSiblingObjects());
                objects.remove(composite);

                parent.getChildren().addAll(composite.getChildren());
                parent.getChildren().remove(composite);


            }
        }

    }

    public ArrayList<Entity> getObjects(){
        return objects;
    }

    public Bounds getBounds(){
        return bounds;
    }

    public void addBasicObject(double x, double y){
        BasicObject newObject = ObjectFactory.create(objectType, x, y);
        objects.add(newObject);
        System.out.println("create");
        root.getChildren().add(newObject);
    }

    public void addSelected(Entity obj){
        if(!selectedObjects.contains(obj))
        {
            obj.onSelected();
            selectedObjects.add(obj);
        }

    }

    public void addConnection(BasicObject fromObject, int fromPortIdx, BasicObject toObject, int toPortIdx)
    {
        root.getChildren().add(ConnectionFactory.create(connectionType, fromObject, fromPortIdx, toObject, toPortIdx));
    }

    public void clearSelected()
    {
        for(Entity obj : objects){
            obj.unSelected();
        }
        selectedObjects.clear();
    }


}
