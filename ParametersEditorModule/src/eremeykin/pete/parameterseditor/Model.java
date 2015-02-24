/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import eremeykin.pete.calculator.Calculator;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import org.netbeans.swing.outline.Outline;

/**
 *
 * @author Pete
 */
public class Model {

    private File mainDir;
    private final XMLParser parser;
//    private final AnsysLauncher aLauncher;
//    private final CalcLoader cLoader;
//    private final Calculator calc;
    private final Node root;
    private final Outline outline;

    private static ArrayDeque<Model> models = new ArrayDeque<>();

    public Model(File mainDir) throws IOException, SQLException, XMLParser.XMLParsingException, ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!mainDir.isDirectory()) {
            throw new IllegalArgumentException("It is not a directory: " + mainDir);
        }
        this.mainDir = mainDir;
        // Find all matching files
        File[] xmlFiles = mainDir.listFiles(getFilter("xml"));
        File[] sqliteFiles = mainDir.listFiles(getFilter("sqlite"));
        File[] classesDirs = mainDir.listFiles(getFilter("classes"));
        // Select on instance of files
        File xmlFile = xmlFiles.length > 0 ? xmlFiles[0] : null;
        File sqliteFile = sqliteFiles.length > 0 ? sqliteFiles[0] : null;
        File classesDir = classesDirs.length > 0 ? classesDirs[0] : null;
        // Create root
        parser = new XMLParser(xmlFile, sqliteFile);
//        cLoader = new CalcLoader(classesDir, getConnection());
        root = parser.getResultNode();
        outline = new OutlineCreator(root).getOutline();

        File ansysDir = new File(System.getenv("ANSYS150_DIR") + "\\bin\\winx64\\ANSYS150.exe");
        File workingDir = new File(System.getProperty("user.home") + "\\Desktop\\AnsysTestWorkingDir");
//        aLauncher = new AnsysLauncher(ansysDir, workingDir);
//        calc = cLoader.getCalc();
        models.add(this);
        //loadCalculator();
    }

    public void calculate() {
        throw new Error("Функция calculate() ещё не реализована");
//        calc.calculate(root);
//        outline.repaint();
    }

    public void run() throws IOException {
        calculate();
        File[] macFiles = mainDir.listFiles(getFilter("mac"));
        File macFile = macFiles.length > 0 ? macFiles[0] : null;
        throw new Error("Функция run() ещё не реализована");
//        File output = calc.printToFile(macFile);
//        aLauncher.setOutput(output);
//        aLauncher.start();
    }

    /**
     * @return the calcFile
     */
    public File getClassesDir() {
                throw new Error("Функция getClassesDir() ещё не реализована");
//        return cLoader.getClassesDir();
    }

    /**
     * @return the outline
     */
    public Outline getOutline() {
        return outline;
    }

    private FileFilter getFilter(String ext) {
        if (ext.equals("classes")) {
            return new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() && pathname.getName().equals(ext);
                }
            };
        }
        return new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                String[] tokens = pathname.getName().split("\\.");
                String extension = tokens[tokens.length - 1];
                return extension.equals(ext);
            }
        };
    }

    /**
     * @return the root
     */
    public final Node getRoot() {
        return root;
    }

    public final Connection getConnection() {
        return parser.getDataBase().getConnection();
    }

    /**
     * @return the mainDir
     */
    public File getMainDir() {
        return mainDir;
    }

    /**
     * @param mainDir the mainDir to set
     */
    public void setMainDir(File mainDir) {
        this.mainDir = mainDir;
    }

    /**
     * @return the macFile
     */
    public File getMacFile() {
        return mainDir.listFiles(getFilter("mac"))[0];
    }

    public File getSqliteFile() {
        return parser.getDataBase().getSqliteFile();
    }

    public File getXmlFile() {
        return parser.getXmlFile();
    }

    public File getAnsysDir() {
        throw new Error("Функция getAnsysDir() ещё не реализована");
//        return aLauncher.getAnsysDir();
    }

    public File getWorkingDir() {
        throw new Error("Функция getWorkingDir() ещё не реализована");
//        return aLauncher.getWorkingDir();
    }

}
