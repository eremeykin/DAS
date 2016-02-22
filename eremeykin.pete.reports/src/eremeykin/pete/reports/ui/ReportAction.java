/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.reports.ui;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.api.model.ModelParameter;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "eremeykin.pete.reports.ui.ReportAction"
)
@ActionRegistration(
        iconBase = "resources/report16.png",
        displayName = "#CTL_ReportAction"
)
@ActionReference(path = "Toolbars/File", position = 300)
@Messages("CTL_ReportAction=CreateReport")
public final class ReportAction implements ActionListener, LookupListener {

    private Model model;
    Lookup.Result<Model> modelResult = CentralLookup.getDefault().lookupResult(Model.class);

    public ReportAction() {
        modelResult.addLookupListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resultChanged(null);
        if (model == null) {
            return;
        }

        XWPFDocument doc = new XWPFDocument();

        XWPFParagraph p1 = doc.createParagraph();
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r1 = p1.createRun();
        r1.setBold(true);
        r1.setText("Отчет");
        r1.setBold(true);
        r1.setFontFamily("Times New Roman");
        r1.setFontSize(24);
        r1.setTextPosition(10);

        XWPFParagraph p2 = doc.createParagraph();
        p2.setAlignment(ParagraphAlignment.LEFT);
        p2.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun r2 = p2.createRun();
        r2.setText("Таблица исходных данных: ");
        r2.setBold(false);
        r2.setFontFamily("Times New Roman");
        r2.setFontSize(14);
        r2.setTextPosition(10);

        XWPFTable table = doc.createTable(1, 2);
        table.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(9000));
        ModelParameter root = model.getRoot();

        int row = 1;
        Map.Entry<ModelParameter, Integer> kv = model.getParameterAndLevelByID(root, 0);
        ModelParameter parameter = kv.getKey();
        Integer level = kv.getValue();

        ArrayList<Integer> ids = new ArrayList(model.asMap().keySet());
        Collections.sort(ids);
        for (Integer each : ids) {
            table.createRow();
            String text = "";
            kv = model.getParameterAndLevelByID(root, each);
            parameter = kv.getKey();
            level = kv.getValue();
            for (int c = 0; c < level; c++) {
                text += "        ";
            }
            table.getRow(row - 1).getCell(0).setText(text + parameter.toString());
            table.getRow(row - 1).getCell(1).setText(parameter.getValue());
            row++;
        }
        table.setWidth(80);

        XWPFParagraph p3 = doc.createParagraph();
        p3.setAlignment(ParagraphAlignment.LEFT);
        p3.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun r3 = p3.createRun();
        r3.addBreak();
        r3.setText("\nДиаграмма деформаций: ");
        r3.setBold(false);
        r3.setFontFamily("Times New Roman");
        r3.setFontSize(14);

        File uPlotFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "/uplot.png");
        try {
            byte[] picbytes = IOUtils.toByteArray(new FileInputStream(uPlotFile));
            doc.addPictureData(picbytes, XWPFDocument.PICTURE_TYPE_PNG);
            XWPFRun pr = doc.createParagraph().createRun();
            pr.addPicture(new FileInputStream(uPlotFile), Document.PICTURE_TYPE_PNG, "plot.png", Units.toEMU(450), Units.toEMU(337));
            pr.addCarriageReturn();
            pr.addBreak(BreakType.PAGE);
            pr.addBreak(BreakType.TEXT_WRAPPING);

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        XWPFParagraph p4 = doc.createParagraph();
        p4.setAlignment(ParagraphAlignment.LEFT);
        p4.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun r4 = p4.createRun();
        r4.addBreak();
        r4.setText("\nДиаграмма напряжений: ");
        r4.setBold(false);
        r4.setFontFamily("Times New Roman");
        r4.setFontSize(14);

        File sPlotFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "/splot.png");
        try {
            byte[] picbytes = IOUtils.toByteArray(new FileInputStream(sPlotFile));
            doc.addPictureData(picbytes, XWPFDocument.PICTURE_TYPE_PNG);
            XWPFParagraph pp = doc.createParagraph();
            pp.createRun().addPicture(new FileInputStream(sPlotFile), Document.PICTURE_TYPE_PNG, "plot.png", Units.toEMU(450), Units.toEMU(337));
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        File reportFile = new File("report.docx");
        try (FileOutputStream out = new FileOutputStream(reportFile)) {
            doc.write(out);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().edit(reportFile);
            } else {
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        if (!modelResult.allInstances().isEmpty()) {
            model = modelResult.allInstances().iterator().next();
        }
    }
}
