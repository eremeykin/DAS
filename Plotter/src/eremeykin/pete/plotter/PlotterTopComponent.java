/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.plotter;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.plotter//Plotter//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "PlotterTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.plotter.PlotterTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_PlotterAction",
        preferredID = "PlotterTopComponent"
)
@Messages({
    "CTL_PlotterAction=Plotter",
    "CTL_PlotterTopComponent=Plotter Window",
    "HINT_PlotterTopComponent=This is a Plotter window"
})
public final class PlotterTopComponent extends TopComponent {

    public PlotterTopComponent() {
        initComponents();
        setName(Bundle.CTL_PlotterTopComponent());
        setToolTipText(Bundle.HINT_PlotterTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        setContentPane(chartPanel);
//        JPanel jPanel1 = new JPanel();
        setLayout(new java.awt.BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        validate();
//        jPanel1.setVisible(true);
//        add(jPanel1);
//        this.setVisible(true);
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

    private XYDataset createDataset() {

//        final XYSeries series1 = new XYSeries("First");
//        series1.add(1.0, 1.0);
//        series1.add(2.0, 4.0);
//        series1.add(3.0, 3.0);
//        series1.add(4.0, 5.0);
//        series1.add(5.0, 5.0);
//        series1.add(6.0, 7.0);
//        series1.add(7.0, 7.0);
//        series1.add(8.0, 8.0);

        final XYSeries series2 = new XYSeries("Second");
        series2.add(0.0, 545.516E-06);
        series2.add(5.26413E-03, 456.574E-06);
        series2.add(10.5119E-03, 251.08E-06);
        series2.add(15.7459E-03, 125.884E-06);
        series2.add(20.9713E-03, 288.566E-06);
        series2.add(26.1895E-03, 446.961E-06);
        series2.add(31.4003E-03, 526.576E-06);
        series2.add(36.6038E-03, 491.13E-06);
        series2.add(41.807E-03, 316.659E-06);
        series2.add(47.0312E-03, 134.269E-06);
        series2.add(52.2851E-03, 330.25E-06);
        series2.add(57.5441E-03, 485.617E-06);
        series2.add(62.7959E-03, 515.149E-06);
        series2.add(68.0428E-03, 442.273E-06);
        series2.add(73.2877E-03, 294.961E-06);
        series2.add(78.5313E-03, 129.164E-06);
        series2.add(83.7708E-03, 238.052E-06);
        series2.add(88.993E-03, 460.24E-06);
        series2.add(94.1847E-03, 560.814E-06);
        series2.add(99.3765E-03, 460.24E-06);
        series2.add(104.599E-03, 238.052E-06);
        series2.add(109.838E-03, 129.164E-06);
        series2.add(115.082E-03, 294.961E-06);
        series2.add(120.327E-03, 442.273E-06);
        series2.add(125.574E-03, 515.149E-06);
        series2.add(130.825E-03, 485.617E-06);
        series2.add(136.084E-03, 330.25E-06);
        series2.add(141.338E-03, 134.269E-06);
        series2.add(146.563E-03, 316.659E-06);
        series2.add(151.766E-03, 491.13E-06);
        series2.add(156.969E-03, 526.576E-06);
        series2.add(162.18E-03, 446.961E-06);
        series2.add(167.398E-03, 288.566E-06);
        series2.add(172.624E-03, 125.884E-06);
        series2.add(177.858E-03, 251.08E-06);
        series2.add(183.105E-03, 456.574E-06);

//        final XYSeries series3 = new XYSeries("Third");
//        series3.add(3.0, 4.0);
//        series3.add(4.0, 3.0);
//        series3.add(5.0, 2.0);
//        series3.add(6.0, 3.0);
//        series3.add(7.0, 6.0);
//        series3.add(8.0, 3.0);
//        series3.add(9.0, 4.0);
//        series3.add(10.0, 3.0);

        final XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(series1);
        dataset.addSeries(series2);
//        dataset.addSeries(series3);

        return dataset;

    }

    private JFreeChart createChart(final XYDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Деформации детали", // chart title
                "Расстояние", // x axis label
                "Величина деформации", // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
        //      legend.setDisplaySeriesShapes(true);
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
//        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

}
