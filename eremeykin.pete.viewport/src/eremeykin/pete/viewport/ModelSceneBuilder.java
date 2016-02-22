package eremeykin.pete.viewport;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.model.ModelChangedEvent;
import eremeykin.pete.api.model.ModelChangedListener;
import eremeykin.pete.api.model.ModelParameter;
import eremeykin.pete.api.model.ModelParameterChangedEvent;
import eremeykin.pete.api.model.ModelParameterChangedListener;
import java.io.File;
import java.util.function.Predicate;
import javafx.animation.RotateTransition;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableFloatArray;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javax.swing.JOptionPane;
import static java.lang.Math.*;

/**
 *
 * @author cmcastil
 */
public final class ModelSceneBuilder extends SceneBuilder implements ModelChangedListener {

    Group root = new Group();
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
    private Box plane = new Box();
    private Box planeR = new Box();
    private Rotate rotate = new Rotate();
    private Translate translate = new Translate();
    private double maxZ = 0;
    private final File modelFile;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    public ModelSceneBuilder(File modelFile) {
        this.modelFile = modelFile;
        buildCamera();
        buildModel();
        buildAxes();
        buildScene();
    }
    
    public ModelSceneBuilder() {
        this.modelFile = null;
        buildCamera();
        buildModel();
        buildAxes();
        buildScene();
    }
    

    private void removeRotate() {
        planeR.getTransforms().removeIf(new Predicate<Transform>() {
            @Override
            public boolean test(Transform t) {
                return t instanceof Rotate;
            }
        });
    }

    private void removeTranslate() {
        plane.getTransforms().removeIf(new Predicate<Transform>() {
            @Override
            public boolean test(Transform t) {
                return t instanceof Translate;
            }
        });
    }

    public void rotate(double angle) {
        removeRotate();
        rotate.setAngle(angle);
        planeR.getTransforms().add(rotate);
    }

    public void translate(double value) {
        removeTranslate();
        translate.setZ(-maxZ * value);
        plane.getTransforms().add(translate);
    }

    @Override
    public void buildModel() {
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
                        float y = points.get(p + 1);
                        float z = points.get(p + 2);
                        if (p < 30) {
                            System.out.println("x=" + x);
                            System.out.println("y=" + y);
                            System.out.println("z=" + z);
                        }
                        if (maxX < abs(x)) {
                            maxX = abs(x);
                        }
                        if (maxY < abs(y)) {
                            maxY = abs(y);
                        }
                        if (maxZ < abs(z)) {
                            maxZ = abs(z);
                        }
                    }
                    this.maxZ = maxZ;

                    float max = Math.max(Math.max(maxX, maxY), maxZ);
                    scale = AXIS_LENGTH / max / 3;
                }

                modelGroup.getChildren().addAll(meshViews);
                ////////////////////////////////////

                final PhongMaterial planeZMaterial = new PhongMaterial();
                planeZMaterial.setDiffuseColor(Color.web("#FF101060"));
                Box bZ = new Box(maxX * 2, maxY * 2, maxZ / 1000);
                bZ.setMaterial(planeZMaterial);

                final PhongMaterial planeRMaterial = new PhongMaterial();
                planeRMaterial.setDiffuseColor(Color.web("#1010FF60"));
                Box bR = new Box(maxZ / 1000, maxY, maxZ);
                bR.setMaterial(planeRMaterial);
                bR.translateYProperty().setValue(maxY / 2);
                bR.translateZProperty().setValue(-maxZ / 2);

                modelGroup.getChildren().addAll(bZ);
                modelGroup.getChildren().addAll(bR);
                this.plane = bZ;
                this.planeR = bR;
                this.rotate = new Rotate(0, 0, -maxY / 2, 0, Rotate.Z_AXIS);
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

    @Override
    public void modelChanged(ModelChangedEvent evt) {
        ModelParameter mp = evt.getParameterSource();
        if (mp.getId() == 29) {
            if (mp.getValue().equals("ВЫКЛ")) {
                planeR.setVisible(false);
            } else {
                planeR.setVisible(true);
            }
        }

        if (mp.getId() == 32) {
            if (mp.getValue().equals("ВЫКЛ")) {
                plane.setVisible(false);
            } else {
                plane.setVisible(true);
            }
        }

        if (mp.getId() == 30) {
            rotate(Double.valueOf(mp.getValue()));
        }
        if (mp.getId() == 33) {
            translate(Double.valueOf(mp.getValue()));
        }
    }
}
