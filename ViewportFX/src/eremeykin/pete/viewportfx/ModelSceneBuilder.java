package eremeykin.pete.viewportfx;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import java.io.File;
import java.net.URL;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 *
 * @author cmcastil
 */
public class ModelSceneBuilder extends SceneBuilder {

    final Group root = new Group();
    final Xform axisGroup = new Xform();
    final Xform modelGroup = new Xform();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -500;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 200.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 5.0;
    private static final double TRACK_SPEED = 0.3;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    @Override
    public void buildModel(File modelFile) {
        MeshView[] meshViews;
        if (modelFile!=null){
        ObjModelImporter objImporter = new ObjModelImporter();
            try {
//                URL modelUrl = this.getClass().getClassLoader().getResource("resources/test.obj");
                objImporter.read(modelFile);
            } catch (Exception e) {
            }
            meshViews = objImporter.getImport();
        }
        else{
            meshViews = new MeshView[1];
            meshViews[0]= new MeshView();
        }
        final PhongMaterial modelMaterial = new PhongMaterial();
        modelMaterial.setDiffuseColor(Color.LIGHTGRAY);
        modelMaterial.setSpecularColor(Color.LIGHTGREY);

        meshViews[0].setMaterial(modelMaterial);
        modelGroup.getChildren().addAll(meshViews);
        modelGroup.setScale(200);
        world.getChildren().add(modelGroup);
    }

    @Override
    public void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    @Override
    public void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        Cylinder cx = new Cylinder(0.25, AXIS_LENGTH, 20);
        Cylinder cy = new Cylinder(0.25, AXIS_LENGTH, 20);
        Cylinder cz = new Cylinder(0.25, AXIS_LENGTH, 20);
        cx.setMaterial(greenMaterial);
        cy.setMaterial(blueMaterial);
        cz.setMaterial(redMaterial);
        cy.setRotate(90);
        cz.setRotationAxis(Rotate.X_AXIS);
        cz.setRotate(90);

        axisGroup.getChildren().addAll(cx, cy, cz);
        world.getChildren().addAll(axisGroup);
    }

    // <editor-fold defaultstate="collapsed" desc="handle mouse and keyboard code">
    private void setHandleMouse(Scene scene) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = SHIFT_MULTIPLIER;;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }

                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED);
                } else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
                }
            }
        });

        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double z = camera.getTranslateZ();
                double newZ = z + event.getDeltaY() / 2;
                camera.setTranslateZ(newZ);
            }
        });
    }

    private void setHandleKeyboard(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                }
            }
        });
    }

    // </editor-fold>
    @Override
    public void buildScene() {
        buildScene(true);
    }

    @Override
    public void buildScene(boolean antialiasing) {
        root.getChildren().add(world);
        System.out.println(Platform.isSupported(ConditionalFeature.SCENE3D));
        scene = new Scene(root, 1024, 1024, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTBLUE);
        setHandleKeyboard(scene);
        setHandleMouse(scene);
        scene.setCamera(camera);
    }

}
