/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import eremeykin.pete.calculator.Calculator;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;

/**
 *
 * @author Pete
 */
public class CalcLoader {

    private File classesDir;//Calc loader
    private Calculator calc;

    public CalcLoader(File classesDir, Connection connection) throws ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.classesDir = classesDir;
        loadCalculator(connection);
    }

    private void loadCalculator(Connection connection) throws ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //m.getRoot().setValueById("JAW_HEIGHT", "test");
        URL[] urls = {getClassesDir().toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class cls = classLoader.loadClass("ansexp.calculator.DefaultCalculator");
        Constructor<?>[] constructors = cls.getConstructors();
        //Class[] requiredParametersTypes = {DataSource.class};
        for (Constructor constr : constructors) {
            if (constr.getParameterTypes().length == 0) {
                setCalc((Calculator) constr.newInstance());
                getCalc().setConnection(connection);
                break;
            }
        }
    }

    /**
     * @return the calc
     */
    public Calculator getCalc() {
        return calc;
    }

    /**
     * @param calc the calc to set
     */
    public void setCalc(Calculator calc) {
        this.calc = calc;
    }

    /**
     * @return the classesDir
     */
    public File getClassesDir() {
        return classesDir;
    }

    /**
     * @param classesDir the classesDir to set
     */
    public void setClassesDir(File classesDir) {
        this.classesDir = classesDir;
    }
}
