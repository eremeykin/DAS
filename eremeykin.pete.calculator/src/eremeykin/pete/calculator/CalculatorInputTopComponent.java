/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.calculator;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import java.awt.Font;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import org.kie.api.runtime.rule.FactHandle;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.calculator//CalculatorInput//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "CalculatorInputTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.calculator.CalculatorInputTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CalculatorInputTopComponentAction",
        preferredID = "CalculatorInputTopComponent"
)
@Messages({
    "CTL_CalculatorInputTopComponentAction=CalculatorInput",
    "CTL_CalculatorInputTopComponent=CalculatorInput Window",
    "HINT_CalculatorInputTopComponent=This is a rule calculator window"
})
public final class CalculatorInputTopComponent extends TopComponent {

    Logger logger = LoggerManager.getLogger(CalculatorInputTopComponent.class);
    
    private TurningStepBean step = new TurningStepBean();
    private RuleManager rManager = new RuleManager();

    public CalculatorInputTopComponent() {
        initComponents();
        jComboBox1.setModel(new DefaultComboBoxModel(TurningStepBean.TurningType.values()));
        jComboBox2.setModel(new DefaultComboBoxModel(TurningStepBean.ProcessKind.values()));
        jComboBox3.setModel(new DefaultComboBoxModel(TurningStepBean.WorkpieceMaterial.values()));
        jComboBox4.setModel(new DefaultComboBoxModel(TurningStepBean.WorkpieceType.values()));
        jComboBox5.setModel(new DefaultComboBoxModel(TurningStepBean.InstrumentMaterial.values()));
        jComboBox6.setModel(new DefaultComboBoxModel(new Double[]{20d, 30d, 40d, 60d, 90d}));
        jComboBox7.setModel(new DefaultComboBoxModel(new Double[]{0.63d, 1.25d, 2.50d, 3.2d, 6.30d, 12.5d}));
        rManager.insert(step);
        int fontSize = 12;
        ((javax.swing.border.TitledBorder) jPanel1.getBorder()).setTitleFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jPanel1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel4.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel5.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel6.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel7.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel8.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jLabel15.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jFormattedTextField1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jFormattedTextField3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox4.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox5.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox7.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jComboBox6.setFont(new Font("Tahoma", Font.PLAIN, 12));
        this.jButton1.setFont(new Font("Tahoma", Font.PLAIN, 12));
               
        setName(Bundle.CTL_CalculatorInputTopComponent());
        setToolTipText(Bundle.HINT_CalculatorInputTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(342, 400));

        jScrollPane2.setMinimumSize(new java.awt.Dimension(10, 10));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 600));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jPanel1.border.title"))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(250, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 390));

        jComboBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel6.text")); // NOI18N
        jLabel6.setMinimumSize(new java.awt.Dimension(7, 15));

        jComboBox5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel7.text")); // NOI18N
        jLabel7.setMinimumSize(new java.awt.Dimension(7, 15));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel1.text")); // NOI18N
        jLabel1.setMinimumSize(new java.awt.Dimension(7, 15));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel2.text")); // NOI18N
        jLabel2.setMinimumSize(new java.awt.Dimension(7, 15));

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel3.text")); // NOI18N
        jLabel3.setMinimumSize(new java.awt.Dimension(7, 15));

        jComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel4.text")); // NOI18N
        jLabel4.setMinimumSize(new java.awt.Dimension(7, 15));

        jComboBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel8.text")); // NOI18N
        jLabel8.setMinimumSize(new java.awt.Dimension(7, 15));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel5.text")); // NOI18N
        jLabel5.setMinimumSize(new java.awt.Dimension(7, 15));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jFormattedTextField1.setToolTipText(org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jFormattedTextField1.toolTipText")); // NOI18N

        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel15, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jLabel15.text")); // NOI18N
        jLabel15.setMinimumSize(new java.awt.Dimension(7, 15));

        jComboBox6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jComboBox7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CalculatorInputTopComponent.class, "CalculatorInputTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextField3)
                    .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jFormattedTextField1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        step = new TurningStepBean();
        try {
            step.setDiameter(Double.parseDouble(jFormattedTextField1.getText().replace(',', '.')));
        } catch (NumberFormatException ex) {
            step.setErrorMessage("Ошибка ввода диаметра.");
        }
        try {
            step.setType((TurningStepBean.TurningType) jComboBox1.getSelectedItem());
        } catch (ClassCastException ex) {
            step.setErrorMessage("Ошибка ввода типа точения.");
        }
        try {
            step.setKind((TurningStepBean.ProcessKind) jComboBox2.getSelectedItem());
        } catch (ClassCastException ex) {
            step.setErrorMessage("Ошибка ввода вида обработки.");
        }
        try {
            step.setWorkpieceMaterial((TurningStepBean.WorkpieceMaterial) jComboBox3.getSelectedItem());
        } catch (ClassCastException ex) {
            step.setErrorMessage("Ошибка ввода материала заготовки.");
        }
        try {
            step.setWorkpieceType((TurningStepBean.WorkpieceType) jComboBox4.getSelectedItem());
        } catch (ClassCastException ex) {
            step.setErrorMessage("Ошибка ввода вида заготовки.");
        }
        try {
            step.setInstrumentMaterial((TurningStepBean.InstrumentMaterial) jComboBox5.getSelectedItem());
        } catch (ClassCastException ex) {
            step.setErrorMessage("Ошибка ввода инструментального материала.");
        }
        try {
            step.setDurability(Double.parseDouble(jComboBox6.getSelectedItem().toString()));
        } catch (NumberFormatException ex) {
            step.setErrorMessage("Ошибка ввода стойкости.");
        }
        try {
            step.setRoughness(Double.parseDouble(jComboBox7.getSelectedItem().toString()));
        } catch (NumberFormatException ex) {
            step.setErrorMessage("Ошибка ввода шероховатости.");
        }
        try {
            step.setInstrumentRadius(Double.parseDouble(jFormattedTextField3.getText().replace(',', '.')));
        } catch (Exception ex) {
            step.setErrorMessage("Ошибка ввода инструментального радиуса.");
        }
        FactHandle stepFact = rManager.insert(step);
        rManager.fireAll();

        CentralLookup cl = CentralLookup.getDefault();
        Collection parameters = cl.lookupAll(TurningStepBean.class);
        if (!parameters.isEmpty()) {
            Iterator<TurningStepBean> it = parameters.iterator();
            while (it.hasNext()) {
                TurningStepBean st = it.next();
                cl.remove(st);
            }
        }
        cl.add(step);

    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
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
}
