/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.calculator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

/**
 *
 * @author Pete
 */
public interface Calculator {

    public DataStore calculate(DataStore ds);

    public File printToFile(File file) throws IOException;

    public void setConnection(Connection cnctn);
}
