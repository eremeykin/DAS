/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.plotter;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.api.model.ModelChangedEvent;
import eremeykin.pete.api.model.ModelChangedListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Platform;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.renderer.PolarItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
        preferredID = "PolarPlotterTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "eremeykin.pete.plotter.PolarPlotterTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_PolarPlotterAction",
        preferredID = "PolarPlotterTopComponent"
)
@Messages({
    "CTL_PolarPlotterAction=PolarPlotter",
    "CTL_PolarPlotterTopComponent=PolarPlotter Window",
    "HINT_PolarPlotterTopComponent=This is a PolarPlotter window"
})
public final class PolarPlotterTopComponent extends TopComponent implements LookupListener, ModelChangedListener, FileWatcher.Updateable {

    private static final Logger LOGGER = LoggerManager.getLogger(PlotterTopComponent.class);
    private File home;
    Model model;
    private Lookup.Result<Model> modelResult = null;

    private final FileWatcher watchThread = new FileWatcher(this);
    private ChartPanel chartPanel;
    private PolarPlot plot;
    private double scale=1;

    public PolarPlotterTopComponent() {
        initComponents();
        setName(Bundle.CTL_PolarPlotterTopComponent());
        setToolTipText(Bundle.HINT_PolarPlotterTopComponent());
        final XYSeriesCollection dataset = new XYSeriesCollection();

        final XYSeries toleranceSeries = new XYSeries("Tolerance");
        final XYSeries dataSeries = new XYSeries("U");

        dataset.addSeries(dataSeries);
        dataset.addSeries(toleranceSeries);
        final JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(chartPanel, BorderLayout.CENTER);
        validate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 417, Short.MAX_VALUE)
        );

        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PolarPlotterTopComponent.class, "PolarPlotterTopComponent.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        // TODO add your handling code here:
        JSpinner source = (JSpinner)(evt.getSource());
        scale = Double.valueOf(source.getValue().toString());
        update();
    }//GEN-LAST:event_jSpinner1StateChanged

    @Override
    public void componentOpened() {
        modelResult = CentralLookup.getDefault().lookupResult(Model.class);
        modelResult.addLookupListener(this);
        home = WorkspaceManager.INSTANCE.getWorkspace();
        try {
            watchThread.start();
        } catch (IllegalThreadStateException exc) {
        }
    }

    @Override
    public void componentClosed() {
        model.removeModelChangedListener(this);
        watchThread.stopWatch();
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    private void clear() {
        plot.setDataset(new XYSeriesCollection());
//        toleranceSeries.clear();
//        dataSeries.clear();
    }

    @Override
    public void update() {
        // for first rpt file
        if (model == null) {
            clear();
            return;
        }
        File[] rptFiles = home.listFiles(filter());
        // catch if there is no such file
        if (rptFiles.length == 0) {
            clear();
            return;
        }
        File firstRPT = rptFiles[0];

        Scanner scanner;
        try {
            scanner = new Scanner(firstRPT);
            scanner.useDelimiter("\\s+|\n");
        } catch (FileNotFoundException ex) {
            clear();
            return;
        }
        List<Map.Entry<Double, Double>> tmpList = new ArrayList<>();
        for (int i = 0; scanner.hasNext(); i++) {
            String line = scanner.next();
            try {
                double x1 = Double.valueOf(line);
                line = scanner.next();
                double x2 = Double.valueOf(line);
//                System.out.println("x1=" + x1 + "\nx2=" + x2);
                tmpList.add(new AbstractMap.SimpleEntry<>(x1, x2));
            } catch (NumberFormatException ex) {
                // only if it is the third or following line
                if (i > 1) {
                    LOGGER.error("Error while parsing double from file: " + firstRPT.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Error while parsing result file.", "Parsing error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
        if (tmpList.isEmpty()) {
            clear();
            return;
        }
        fillData(tmpList);

//        fillData(tmpList, dataSeries, toleranceSeries);        
    }

    protected void fillData(List<Map.Entry<Double, Double>> tmpList) {
        XYSeries toleranceSeries1 = new XYSeries("ei");
        XYSeries toleranceSeries2 = new XYSeries("es");
        XYSeries dataSeries = new XYSeries("U");
        XYSeries diameter = new XYSeries("d");
        Double x1 = tmpList.get(0).getKey();
        Double x2 = tmpList.get(0).getKey();
        double delta = x2 - x1;

        Double y1 = tmpList.get(0).getValue();
        Double xLast = tmpList.get(tmpList.size() - 1).getKey();

        tmpList.add(new AbstractMap.SimpleEntry<>(x1, y1));
        double min = Double.MAX_VALUE;
        for (Map.Entry<Double, Double> point : tmpList) {
            Double xv = point.getKey();
            Double yv = point.getValue();
            if (yv < min) {
                min = yv;
            }
        }
        diameter.clear();
        dataSeries.clear();
        toleranceSeries1.clear();
        toleranceSeries2.clear();
        double d = Double.valueOf(model.getParameterByID(model.getRoot(), 3).getValue());
        for (Map.Entry<Double, Double> point : tmpList) {
            Double xv = point.getKey();
            Double yv = point.getValue();
            if (xv == null || yv == null) {
                continue;
            }
            dataSeries.add(xv * 360 / (xLast + delta), d + scale*yv);
            toleranceSeries1.add(xv * 360 / (xLast + delta), d + scale*Double.valueOf(model.getParameterByID(model.getRoot(), 5).getValue()));
            toleranceSeries2.add(xv * 360 / (xLast + delta), d + scale*Math.copySign(Double.valueOf(model.getParameterByID(model.getRoot(), 5).getValue()), -1));
            diameter.add(xv * 360 / (xLast + delta), d);
            XYSeriesCollection ds = new XYSeriesCollection();
            ds.addSeries(dataSeries);
            ds.addSeries(toleranceSeries1);
            ds.addSeries(toleranceSeries2);
            ds.addSeries(diameter);
            plot.setDataset(ds);
        }
    }

    JFreeChart createChart(XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createPolarChart("Узловые перемещения", dataset, true, true, false);
        chart.setBackgroundPaint(Color.white);
        plot = (PolarPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setAngleGridlinePaint(Color.BLACK);
        plot.setRadiusGridlinePaint(Color.LIGHT_GRAY);
        final DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setSeriesShape(1, ShapeUtilities.createDiamond(1));
        renderer.setSeriesShape(2, ShapeUtilities.createDiamond(1));
        renderer.setSeriesShape(3, ShapeUtilities.createDiamond(1));
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesPaint(3, Color.BLACK);
        plot.setRenderer(renderer);
        chart.setTitle(
                new org.jfree.chart.title.TextTitle("Узловые перемещения",
                        new java.awt.Font("Arial", java.awt.Font.PLAIN, 16)));
        return chart;
    }

    FilenameFilter filter() {
        return new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("Z.rpt");
            }
        };
    }

    @Override
    public void modelChanged(ModelChangedEvent evt) {
        update();
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends Model> allModels = modelResult.allInstances();
        if (!allModels.isEmpty()) {
            model = allModels.iterator().next();
            modelChanged(null);
        }
    }

    @Override
    public File home() {
        return home;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
}
