/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.fxtreetabletest;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.paint.Color;
import eremeykin.pete.fxtreetabletest.Xform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.fxtreetabletest//Test//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "TestTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.fxtreetabletest.TestTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_TestAction",
        preferredID = "TestTopComponent"
)
@Messages({
    "CTL_TestAction=Test",
    "CTL_TestTopComponent=Test Window",
    "HINT_TestTopComponent=This is a Test window"
})
public final class TestTopComponent extends TopComponent {

    final JFXPanel fxPanel = new JFXPanel();

    List<Employee> employees = Arrays.<Employee>asList(new Employee(
            "Ethan Williams", "ethan.williams@example.com"), new Employee(
                    "Emma Jones", "emma.jones@example.com"), new Employee("Michael Brown",
                    "michael.brown@example.com"), new Employee("Anna Black",
                    "anna.black@example.com"), new Employee("Rodger York",
                    "roger.york@example.com"), new Employee("Susan Collins",
                    "susan.collins@example.com"));

    final TreeItem<Employee> root = new TreeItem<>(new Employee(
            "Sales Department", ""));

    public TestTopComponent() {
        initComponents();
        setName(Bundle.CTL_TestTopComponent());
        setToolTipText(Bundle.HINT_TestTopComponent());

        jPanel1.setLayout(new BorderLayout());

        root.setExpanded(true);
        employees.stream().forEach((employee) -> {
            root.getChildren().add(new TreeItem<>(employee));
        });

        TreeTableColumn<Employee, String> empColumn = new TreeTableColumn<>(
                "Employee");
        empColumn
                .setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {

                    public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                        return new ReadOnlyStringWrapper(
                                param.getValue().getValue().getName());
                    }
                });

        TreeTableColumn<Employee, String> emailColumn = new TreeTableColumn<>(
                "Email");
        emailColumn
                .setCellValueFactory((
                                TreeTableColumn.CellDataFeatures<Employee, String> param) -> new ReadOnlyStringWrapper(
                                param.getValue().getValue().getEmail()));

        TreeTableView<Employee> treeTableView = new TreeTableView<>(root);
        treeTableView.getColumns().setAll(empColumn, emailColumn);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < 2; i++) {
            treeTableView.getColumns().get(i).setStyle("-fx-border-color: lightblue;\n"
                    + "    -fx-border-style: solid;"
                    + "    -fx-background-insets: 1, 0 0 1 0;\n"
                    + "    -fx-padding: 0.5em;\n"
                    + "    -fx-text-fill: -fx-text-inner-color;");
        }
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(treeTableView);
        VBox.setVgrow(treeTableView, Priority.ALWAYS);
        Scene scene = new Scene(vbox, 800, 800);

        Platform.setImplicitExit(false);
        jPanel1.add(fxPanel);
        fxPanel.setVisible(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxPanel.setScene(scene);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public class Employee {

//        private SimpleStringProperty name;
        private SimpleStringProperty email;

        public SimpleStringProperty nameProperty() {
//            if (name == null) {
//                name = new SimpleStringProperty(this, "name");
//            }
//            return name;
            return new SimpleStringProperty("Test value");
        }

        public SimpleStringProperty emailProperty() {
            if (email == null) {
                email = new SimpleStringProperty(this, "email");
            }
            return email;
        }

        private Employee(String name, String email) {
//            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
        }

        public String getName() {
            return "Test value get";//name.get();
        }

        public void setName(String fName) {
//            name.set(fName);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String fName) {
            email.set(fName);
        }
    }
}
