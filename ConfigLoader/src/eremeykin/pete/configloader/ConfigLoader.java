/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.configloader;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
/**
 *
 * @author Pete
 */

import javax.swing.JOptionPane;

public class ConfigLoader {

    public static String load(String type, String name) {
        InputStream confStream = (ConfigLoader.class.getClassLoader().getResourceAsStream("res/config.xml"));
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        try {
            DocumentBuilder docBuilder = f.newDocumentBuilder();
            Document doc = docBuilder.parse(confStream);
            NodeList nodeList = doc.getElementsByTagName(type);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currNode = nodeList.item(i);
                String currName = currNode.getAttributes().getNamedItem("name").getNodeValue();
                if (currName.equals(name)){
                    return currNode.getTextContent();
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при чтении файла конфигурации.\n"
                    + "Программа будет закрыта.");
        }

        String value = "";
        return value;
    }
}
