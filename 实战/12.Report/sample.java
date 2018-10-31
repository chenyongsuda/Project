package com.c4.report;

import com.c4.report.entity.Label2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chnho02796 on 2018/10/29.
 */
public class Main {
    public static void main(String[] args) throws JRException {
        /*String destFilePath = System.getProperty("user.dir")+"/src/main/resources/jasper/report1.pdf";
        String jasper = System.getProperty("user.dir")+"/src/main/resources/jasper/report1.jasper";
        File file = new File(jasper);
        List<Label> labels = new ArrayList<Label>();
        Label l = new Label();
        l.setName("水晶杯");
        l.setBorthplace("福州");
        l.setItemBarcode("301150001");
        l.setItemBarcode("301150001");
        l.setSpecification("1235");
        l.setLevel("合格");
        l.setPrice(26.9);
        l.setUnits("个");
        labels.add(l);

        Label l2 = new Label();
        l2.setName("水晶杯");
        l2.setBorthplace("福州");
        l2.setItemBarcode("301150001");
        l2.setItemBarcode("301150001");
        l2.setSpecification("1235");
        l2.setLevel("合格");
        l2.setPrice(26.9);
        l2.setUnits("个");
        labels.add(l2);

        JRDataSource data= new JRBeanCollectionDataSource(labels);
        Map<String, Object> parameters=new HashMap<String, Object>();
        parameters.put("dataset3", data);

        JasperReport jasperReport=(JasperReport)JRLoader.loadObject(file);

        JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        JasperExportManager.exportReportToPdfFile(jasperPrint,destFilePath);
        */
//        String destFilePath = System.getProperty("user.dir")+"/src/main/resources/jasper/report66.pdf";
//        String jasper = System.getProperty("user.dir")+"/src/main/resources/jasper/report66.jasper";
//        File file = new File(jasper);
//
//        List<Label2> labels = new ArrayList<Label2>();
//        Label2 lb = new Label2();
//        lb.setName("Hello Carrefour");
//        lb.setUrl("");
//        lb.setCode("11111111111");
//        labels.add(lb);
//        JRDataSource data= new JRBeanCollectionDataSource(labels);
//        Map<String, Object> parameters=new HashMap<String, Object>();
//        parameters.put("haha","asdsad");
//        JasperReport jasperReport=(JasperReport)JRLoader.loadObject(file);
//        JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, parameters, data);
//        JasperExportManager.exportReportToPdfFile(jasperPrint,destFilePath);

        //General Jasper Report
        String destFilePath = System.getProperty("user.dir")+"/src/main/resources/jasper/report1.pdf";
        String jasper = System.getProperty("user.dir")+"/src/main/resources/jasper/report9.jasper";
        File file = new File(jasper);

        List<Label2> labels = new ArrayList<Label2>();
        for(int i=0; i <2; i++) {
            Label2 lb = new Label2();
            lb.setName("Hello Carrefour "+ i);
            lb.setUrl("");
            lb.setCode("11111111111");
            labels.add(lb);
        }
        JRDataSource data= new JRBeanCollectionDataSource(labels);
        Map<String, Object> parameters=new HashMap<String, Object>();
        parameters.put("haha","asdsad");
        parameters.put("hello",data);
        JasperReport jasperReport=(JasperReport) JRLoader.loadObject(file);
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());


//        JasperExportManager.exportReportToPdfFile(jasperPrint,destFilePath);
        //print
        PrintService printerService = PrintServiceLookup.lookupDefaultPrintService();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(new PrinterName(printerService.getName(), null));
        JRAbstractExporter je = new MyJRPrintServiceExporter();
        // 设置打印内容
        je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        // 设置指定打印机
        je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printerService);
        je.exportReport();


//        String jasper = System.getProperty("user.dir")+"/src/main/resources/jasper/report22.jasper";
//        File file = new File(jasper);
//        List<Label2> labels = new ArrayList<Label2>();
//        Label2 lb = new Label2();
//        lb.setName("Hello Carrefour11111");
//        lb.setUrl("");
//        lb.setCode("11111111111");
//        labels.add(lb);
//        JRDataSource data= new JRBeanCollectionDataSource(labels);
//        Map<String, Object> parameters=new HashMap<String, Object>();
//        parameters.put("haha","asdsad");
//        JasperReport jasperReport=(JasperReport) JRLoader.loadObject(file);
//        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parameters, data);
//
//        ArrayList<JasperPrint> pList = new ArrayList<JasperPrint>();
//        pList.add(jasperPrint);
//
//        //print
//        PrintService printerService = PrintServiceLookup.lookupDefaultPrintService();
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//        printServiceAttributeSet.add(new PrinterName(printerService.getName(), null));
//        JRAbstractExporter je = new MyJRPrintServiceExporter();
//        // 设置打印内容
//        je.setParameter(JRExporterParameter.JASPER_PRINT_LIST, pList);
//        // 设置指定打印机
//        je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printerService);
//        je.exportReport();

    }
}
