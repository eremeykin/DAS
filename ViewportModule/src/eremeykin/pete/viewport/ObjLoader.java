package eremeykin.pete.viewport;

import com.interactivemesh.j3d.community.utils.navigation.orbit.OrbitBehaviorInterim;
import com.sun.j3d.exp.swing.JCanvas3D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Vector3f;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import eremeykin.pete.configloader.ConfigLoader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.Reader;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Text3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/*
 * Beethoven.obj
 * 
 # Tue Oct 27 19:18:13 1992
 #
 #	A Bust of Beethoven
 #
 # Courtesy of:
 #
 #	Viewpoint Animation Engineering
 #	870 West Center
 #	Orem, Utah 84057
 #	(801)224-2222
 #	1-800-DATASET
 #
 #	(c) Copyright 1992 Viewpoint Animation Engineering Inc.
 #	Sun Microsystems has been authorized to freely distribute
 #	these Datasets.  They are provided for unlimited use in
 #	existing or modified form.  The actual Dataset (ie, geometry)
 #	may not, however, be resold in existing or modified form.
 #
 */
/**
 * @author Copyright (c) 2007 August Lammersdorf, www.InteractiveMesh.com
 * @since March 5, 2009
 * @version 1.1

 Please create your own implementation. You are allowed to copy all lines you
 like of this source code, but you may not modify, compile, or distribute this
 'ObjLoader'.
 *
 */
public final class ObjLoader {

    static {
        System.out.println("AppletObjLoader: Copyright (c) 2009 August Lammersdorf, www.InteractiveMesh.com.");
    }

    private boolean isJ3D = false;
    private boolean isJ3DLoader = false;

    private BoundingSphere globalBounds;

    private View view;
    private Canvas3D canvas3D;

    private VirtualUniverse vu;
    private Locale locale;

    private BranchGroup sceneBranch;
    private BranchGroup viewBranch;
    private BranchGroup enviBranch;

    private TransformGroup rotTG;
    private TransformGroup sceneTG;

    private Vector<Shape3D> shapesVec;

    private ClassLoader classLoader;
    private SimpleUniverse universe;
    private TransformGroup objRot;
    

    public ObjLoader(TopComponent window, Reader objReader) {
        try {
            BranchGroup bg = new BranchGroup();
            ColorCube cube = new ColorCube(0.9f);
            bg.addChild(cube);
            // JCanvas
            JCanvas3D jCanvas = new JCanvas3D();
            jCanvas.setLayout(new BorderLayout());
            jCanvas.setResizeMode(JCanvas3D.RESIZE_IMMEDIATELY);
            window.setLayout(new BorderLayout());
            Dimension dim = new Dimension(500, 500);
            jCanvas.setPreferredSize(dim);
            jCanvas.setSize(dim);
            TransformGroup listenerGroup = new TransformGroup();

            window.add("Center", jCanvas);

            canvas3D = jCanvas.getOffscreenCanvas3D();

            universe = new SimpleUniverse(canvas3D);
            BranchGroup scene = createSceneGraph(objReader);

            universe.getViewingPlatform().setNominalViewingTransform();
            universe.addBranchGraph(scene);
            universe.addBranchGraph(sceneBranch);

            OrbitBehaviorInterim obi = new OrbitBehaviorInterim(jCanvas, objRot);
            obi.setRotateEnable(true);
            obi.setRotationCenter(new Point3d(0, 0, 0));
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

//TOdo    @Override
    public void destroy() {
        System.out.println("AppletObjLoader : destroy");
        view.removeAllCanvas3Ds();
        view.attachViewPlatform(null);
        vu.removeAllLocales();
    }

    // Loads *.obj files
    // relativePath: path relative to package of this class
    private Scene loadScene(Reader objReader) throws MalformedURLException {
        Scene scene = null;
        File f = new File(ConfigLoader.load("path", "obj model"));
        URL sceneUrl = f.toURI().toURL();
        if (sceneUrl == null) {
            System.out.println("sceneUrl = null");
            return null;
        }

        String sceneUrlString = sceneUrl.toString();
        String baseUrlString = sceneUrlString.substring(0, sceneUrlString.lastIndexOf('/') + 1); // V 1.1

        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUrlString);
        } catch (MalformedURLException e) {
        }

        System.out.println("Scene URL = " + sceneUrlString);

        Loader objLoader = new ObjectFile(); // No flag: Shapes only

        // BaseUrl = null works as well !!
        objLoader.setBaseUrl(baseUrl);
        System.out.println("Base Url  = " + objLoader.getBaseUrl().toString());

        try {
            scene = objLoader.load(objReader);
        } catch (FileNotFoundException e) {
        } catch (IncorrectFormatException e) {
        } catch (ParsingErrorException e) {
        }

        return scene;
    }

    private void traverseForShape3D(Enumeration e) {
        while (e.hasMoreElements()) {
            Object nodeObject = e.nextElement();
            if (nodeObject instanceof Group) {
                Enumeration children = ((Group) nodeObject).getAllChildren();
                traverseForShape3D(children);
            } else if (nodeObject instanceof Shape3D) {
                shapesVec.add((Shape3D) nodeObject);
            }
        }
    }

    private void createUniverse() {
        // Bounds
        globalBounds = new BoundingSphere();
        globalBounds.setRadius(Double.MAX_VALUE);
        //
        // Viewing
        //
        view = new View();
        view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment());

        GraphicsConfigTemplate3D gCT = new GraphicsConfigTemplate3D();
        GraphicsConfiguration gcfg = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(gCT);

        try {
            canvas3D = new Canvas3D(gcfg);
        } catch (NullPointerException e) {
            System.out.println("AppletObjLoader: Canvas3D failed !!");
            e.printStackTrace();
            System.exit(0);
        } catch (IllegalArgumentException e) {
            System.out.println("AppletObjLoader: Canvas3D failed !!");
            e.printStackTrace();
            System.exit(0);
        }

        view.addCanvas3D(canvas3D);
        //
        // SuperStructure
        //
        vu = new VirtualUniverse();
        locale = new Locale(vu);
        //
        // BranchGraphs
        //		
        sceneBranch = new BranchGroup();
        viewBranch = new BranchGroup();
        enviBranch = new BranchGroup();

        // ViewBranch
        TransformGroup viewTG = new TransformGroup();
        viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D viewTransform = new Transform3D();
        viewTransform.setTranslation(new Vector3f(0.0f, 0.0f, 5.0f));
        viewTG.setTransform(viewTransform);

        ViewPlatform vp = new ViewPlatform();
        //!!

        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(viewTG);
        viewTG.addChild(behavior);
        behavior.setSchedulingBounds(globalBounds);
        MouseWheelZoom zoomBehavior = new MouseWheelZoom();
        zoomBehavior.setTransformGroup(viewTG);
        viewTG.addChild(zoomBehavior);
        zoomBehavior.setSchedulingBounds(globalBounds);
        //!!
        view.attachViewPlatform(vp);

        DirectionalLight headLight = new DirectionalLight();
        headLight.setInfluencingBounds(globalBounds);

        viewTG.addChild(vp);
        viewTG.addChild(headLight);

        viewBranch.addChild(viewTG);

        // EnviBranch
        Background bg = new Background();
        bg.setApplicationBounds(globalBounds);
        bg.setColor(0.0f, 0.47f, 0.63f);

        enviBranch.addChild(bg);

        // SceneBranch
        rotTG = new TransformGroup();
        rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        sceneBranch.addChild(rotTG);
        
        sceneTG = new TransformGroup();
//        // Scene positioning 
        Transform3D sceneTransform = new Transform3D();
        // Rotate around Y
        sceneTransform.setTranslation(new Vector3f(0.281f, 0.5f, -0.421f));
        sceneTG.setTransform(sceneTransform);
//        rotTG.addChild(sceneTG);
    }

    Appearance app = new Appearance();

    TransformGroup makeBall(float ballSize, float ballXPos, float ballYPos, float ballZPos) {
        Sphere sph = new Sphere(ballSize, app);
        Transform3D translate = new Transform3D();
        translate.setTranslation(new Vector3f(ballXPos, ballYPos, ballZPos));
        TransformGroup holdBall = new TransformGroup(translate);
        holdBall.addChild(sph);
        return holdBall;
    }

    Shape3D createAxis() {
        Shape3D axis = new Shape3D();
        Point3f[] point = {
            new Point3f(-1.0f, 0.0f, 0.0f),//X
            new Point3f(1.0f, 0.0f, 0.0f),
            new Point3f(0.0f, -1.0f, 0.0f),
            new Point3f(0.0f, 1.0f, 0.0f),//Y

            new Point3f(0.0f, 0.0f, -1.0f),//Z
            new Point3f(0.0f, 0.0f, 1.0f),};

        LineArray geom = new LineArray(point.length, GeometryArray.COORDINATES);
        geom.setCoordinates(0, point);
        axis.setGeometry(geom);
        axis.setAppearance(app);

        return axis;
    }

    TransformGroup createName(String name, float xPos, float yPos, float zPos, int rotCase) {
        TransformGroup trans = new TransformGroup();
        TransformGroup holdRot = new TransformGroup();
        Transform3D settingScale = new Transform3D();
        Transform3D rotate = new Transform3D();

        switch (rotCase) {
            case 0:
                rotate.rotX(0);
                break;
            case 1:
                rotate.rotZ(Math.PI / 2.0d);
                break;
            case 2:
                rotate.rotY(-Math.PI / 2.0d);
                break;
        }
        settingScale.setScale(.1f);
        trans.setTransform(settingScale);
        holdRot.setTransform(rotate);
        holdRot.addChild(trans);

        Font3D font = new Font3D(new Font("TestFont", Font.PLAIN, 1),
                new FontExtrusion());
        Text3D txt = new Text3D(font, name, new Point3f(xPos, yPos, zPos));
        Shape3D someText = new Shape3D();
        someText.setGeometry(txt);
        someText.setAppearance(app);
        trans.addChild(someText);
        return holdRot;
    }

    public BranchGroup createSceneGraph(Reader objReader) throws MalformedURLException {

        BranchGroup Root = new BranchGroup();

        objRot = new TransformGroup();

        objRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        Root.addChild(objRot);
        objRot.addChild(createAxis());

        float number = 20;//Number of spheres on each axis

        for (int e = 0; e <= 3; e++) {
            float start = -1.0f;
            for (int i = 0; i <= number; i++) {
                switch (e) {
                    case 0:
                        objRot.addChild(makeBall(0.003f, start, 0.0f, 0.0f));//X
                        if (i == number - 1) {
                            objRot.addChild(createName("X", 10 * start, 0.05f, 0.0f, e));
                        }
                        break;
                    case 1:
                        objRot.addChild(makeBall(0.003f, 0.0f, start, 0.0f));//Y
                        if (i == number - 1) {
                            objRot.addChild(createName("Y", 10 * start, 0.05f, 0.0f, e));
                        }
                        break;
                    case 2:
                        objRot.addChild(makeBall(0.003f, 0.0f, 0.0f, start));//Z
                        if (i == number - 1) {
                            objRot.addChild(createName("Z", 10 * start, 0.05f, 0.0f, e));
                        }
                        break;
                }
                start += (float) (2.0f / number);
            }
        }
        createUniverse();

//        objRot.addChild(sceneTG);
        Scene fishScene = loadScene(objReader);
        BranchGroup rootGroup = null;

        if ((rootGroup = fishScene.getSceneGroup()) != null) {

            shapesVec = new Vector<Shape3D>();

            traverseForShape3D(rootGroup.getAllChildren());

            if (!shapesVec.isEmpty()) {

                Appearance appear = new Appearance();

                Material mat = new Material();
                mat.setDiffuseColor(0.7f, 0.70f, 0.80f);
                mat.setSpecularColor(0.00f, 0.0f, 0.10f);
                mat.setShininess(32.0f);

                appear.setMaterial(mat);

                for (Shape3D shape : shapesVec) {
                    shape.setAppearance(appear);
                }
            }
        }

        Color3f white = new Color3f(Color.white);
        Vector3f lDir1 = new Vector3f(2, 2, 2);
        Vector3f lDir2 = new Vector3f(-2, -2, -2);
        Vector3f lDir3 = new Vector3f(0, 2, -2);
        DirectionalLight headLight = new DirectionalLight(white, lDir1);
        BoundingSphere b = new BoundingSphere();
        b.setRadius(Double.MAX_VALUE);
        headLight.setInfluencingBounds(b);
        DirectionalLight headLight2 = new DirectionalLight(white, lDir2);
        headLight2.setInfluencingBounds(b);

        DirectionalLight headLight3 = new DirectionalLight(white, lDir3);
        headLight3.setInfluencingBounds(b);

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setInfluencingBounds(b);

        objRot.addChild(headLight);
        objRot.addChild(headLight2);
        objRot.addChild(ambientLight);

        objRot.addChild(rootGroup);

        Background bg = new Background();
        bg.setApplicationBounds(globalBounds);
        bg.setColor(0.3f, 0.67f, 0.93f);

        Root.addChild(bg);

        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objRot);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        Root.addChild(myMouseRotate);

        MouseWheelZoom myMouseZoom = new MouseWheelZoom();
        myMouseZoom.setTransformGroup(objRot);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        Root.addChild(myMouseZoom);

        Root.compile();

        return Root;
    }

}
