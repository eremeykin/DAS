/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.propertiestree;

import java.io.File;

import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 *
 * @author eremeykin
 */
public class XMLParser {

    private File xmlFile;
    private Document document;

    public XMLParser(File xmlFile, File dbFile) throws XMLParsingException, ClassNotFoundException, SQLException {

        this.xmlFile = xmlFile;
        document = this.getDocument();
    }

    private void parseToResult(org.w3c.dom.Node xmlNode, Node node) throws SQLException {
        class Helper {

            public DefaultCellEditor makeEditorForXMLNode(org.w3c.dom.Node node) throws SQLException {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    org.w3c.dom.Node currNode = node.getChildNodes().item(i);
                    //Если обнаружен узел value
                    if (currNode.getNodeName().equals("value")) {
                        // Получить тип редактора
                        String editorType = getAttribute(currNode, "editor");
                        // Если указан редактор comboBox
                        if (editorType != null && editorType.equals("comboBox")) {
                            //ToDo Получить из список из БД!!
                            String source = getAttribute(currNode, "source");
                            String[] items = new String[0];
                            if (source.equals("dataBase")) {
                                String table = getAttribute(currNode, "table");
                                String name = getAttribute(currNode, "name");
                                items = new String[]{"test1", "test2"}; //getDataBase().getItemsList("part_material", "name");
                            }

                            return new DefaultCellEditor(new JComboBox(items));
                        }
                        // Если указан редактор textField
                        if (editorType != null && editorType.equals("textField")) {
                            return new DefaultCellEditor(new JTextField());
                        }
                    }
                }
                // По умолчанию ячейка не редактируема
                return null;
            }
        }
        //Для всех потомков узла
        for (int i = 0; i < xmlNode.getChildNodes().getLength(); i++) {
            //Устанавливаем текущий XML узел
            org.w3c.dom.Node currXMLNode = xmlNode.getChildNodes().item(i);
            //Если этот узел типа элемент (комментарии и текст отбрасывается)
            if (currXMLNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                if (currXMLNode.getNodeName().equals("node") || currXMLNode.getNodeName().equals("root")) {

                    String name = getSign(currXMLNode, "name");
                    String description = getSign(currXMLNode, "description");
                    String value = getSign(currXMLNode, "value");
                    String id = getAttribute(currXMLNode, "id");

                    DefaultCellEditor editor = new Helper().makeEditorForXMLNode(currXMLNode);

                    Node currNode = new Node(name, description, value, id, editor);
                    //Добавляем текущий TreeTable узел в соответствующий пердыдущий узел TreeTable
                    node.getChildren().add(currNode);
                    //Вызываем для метод для текущих узлов
                    parseToResult(currXMLNode, currNode);
                }
            }
        }
    }

    private String getAttribute(org.w3c.dom.Node node, String attrName) {
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            if (node.getAttributes().item(i).getNodeName().equals(attrName)) {
                return node.getAttributes().item(i).getTextContent();
            }
        }
        return null;
    }

    private String getSign(org.w3c.dom.Node node, String signName) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (signName.equals(children.item(i).getNodeName())) {
                return children.item(i).getTextContent();
            }
        }
        return "undefined";
    }

    private Document getDocument() throws XMLParsingException {
        Document doc = null;
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            doc = builder.parse(this.getXmlFile());
        } catch (Exception e) {

            throw new XMLParsingException(e);
        }
        return doc;
    }

    public Node getResultNode() throws SQLException {
        Node result = new Node("Root", "", "", null, null);
        parseToResult(document, result);
        return result;
    }

    /**
     * @return the xmlFile
     */
    public File getXmlFile() {
        return xmlFile;
    }

    /**
     * @param xmlFile the xmlFile to set
     */
    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * @return the dataBase
     */
    public class XMLParsingException extends Exception {

        public XMLParsingException(Throwable e) {
            super(e);
        }
    }
}
