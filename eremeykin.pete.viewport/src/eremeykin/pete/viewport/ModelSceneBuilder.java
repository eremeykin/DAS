package eremeykin.pete.viewport;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import java.io.File;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.ObservableFloatArray;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javax.swing.JOptionPane;

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
    private static final Logger LOGGER = LoggerManager.getLogger(ModelSceneBuilder.class);
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
        try {
            MeshView[] meshViews;
            if (modelFile != null) {
                ObjModelImporter objImporter = new ObjModelImporter();
                objImporter.read(modelFile);
                meshViews = objImporter.getImport();
            } else {
                meshViews = new MeshView[1];
                meshViews[0] = new MeshView();
            }
            final PhongMaterial modelMaterial = new PhongMaterial();
            modelMaterial.setDiffuseColor(Color.LIGHTGRAY);
            modelMaterial.setSpecularColor(Color.LIGHTGREY);
            double scale = 1;
            if (meshViews[0] != null) {
                MeshView meshView = meshViews[0];
                meshView.setMaterial(modelMaterial);
                float maxX = 0, maxY = 0, maxZ = 0;
                if (meshView.getMesh() instanceof TriangleMesh) {
                    TriangleMesh tMesh = (TriangleMesh) meshView.getMesh();
                    ObservableFloatArray points = tMesh.getPoints();
                    
                    for (int p = 0; p < points.size(); p += 3) {
                        float x = points.get(p);
                        float y = points.get(p);
                        float z = points.get(p);
                        if (maxX < x) {
                            maxX = x;
                        }
                        if (maxY < y) {
                            maxY = y;
                        }
                        if (maxZ < z) {
                            maxZ = z;
                        }
                    }
                    float max = Math.max(Math.max(maxX, maxY), maxZ);
                    scale = AXIS_LENGTH / max / 3;
                }

                modelGroup.getChildren().addAll(meshViews);
                ////////////////////////////////////
                TriangleMesh m = new TriangleMesh();
                m.getPoints().setAll(0, 0, 0, //P0
                        100, 0, 0, //P1
                        0, 100, 0, //P2
                        100, 100, 0 //P3
                );
                m.getTexCoords().addAll(
                        0.25f, 0, //T0
                        0.5f, 0, //T1
                        0, 0.25f, //T2
                        0.25f, 0.25f, //T3
                        0.5f, 0.25f, //T4
                        0.75f, 0.25f, //T5
                        1, 0.25f, //T6
                        0, 0.5f, //T7
                        0.25f, 0.5f, //T8
                        0.5f, 0.5f, //T9
                        0.75f, 0.5f, //T10
                        1, 0.5f, //T11
                        0.25f, 0.75f, //T12
                        0.5f, 0.75f //T13
                );
                m.getFaces().addAll(
                        
                          1, 4, 0, 3, 2, 8 //P1,T4 ,P0,T3  ,P2,T8
                        , 1, 4, 2, 8, 3, 9 //P1,T4 ,P2,T8  ,P3,T9
                        
                        , 1, 4, 2, 8, 0, 3
                        , 1, 4, 3, 9, 2, 8
                        
                );
                MeshView newMeshView = new MeshView(m);
                
                final PhongMaterial planeMaterial = new PhongMaterial();
//                planeMaterial.setDiffuseColor(Color.BLUE);
//                planeMaterial.setSpecularColor(Color.BLUE);

                planeMaterial.setDiffuseColor(Color.web("#FF101060"));
//                newMeshView.setMaterial(planeMaterial);
                Box b = new Box(maxX*2,maxY*2,maxZ/1000);
                b.translateZProperty().setValue(-0.25);
                b.setMaterial(planeMaterial);
                modelGroup.getChildren().addAll(b);
            }
            modelGroup.setScale(scale);
            world.getChildren().add(modelGroup);
        } catch (com.interactivemesh.jfx.importer.ImportException ex) {
            LOGGER.error(ex);
            JOptionPane.showMessageDialog(null, "Can not refresh viewport. There is no \"" + modelFile + "\" file", "No model file error", JOptionPane.ERROR_MESSAGE);
        }
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

                double modifier = SHIFT_MULTIPLIER;

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
        LOGGER.info("Antialiasing is supported: " + Platform.isSupported(ConditionalFeature.SCENE3D));
        root.getChildren().add(world);
        scene = new Scene(root, 1024, 1024, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTBLUE);
        setHandleKeyboard(scene);
        setHandleMouse(scene);
        scene.setCamera(camera);
    }

}
