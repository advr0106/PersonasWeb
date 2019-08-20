package util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hterrero
 * Date: 6/04/13
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */

public class ExportReport {

    public StreamedContent exportPdf(List<Object> dataObject,String nameFile, Map map){
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataObject);
        FacesContext context = FacesContext.getCurrentInstance();

        String realPath = context.getExternalContext().getRealPath("") + File.separator + "report"
                + File.separator;

        File file = new File(realPath + nameFile + ".jasper");
        InputStream inputStream = null;

        try {
            inputStream = new java.io.FileInputStream(file);
            JRFileVirtualizer virtualizer = new JRFileVirtualizer(3,realPath);
            map.put(JRParameter.REPORT_VIRTUALIZER,virtualizer);
            JasperPrint print = JasperFillManager.fillReport(inputStream,map,dataSource);
            virtualizer.setReadOnly(true);
            JRPdfExporter pdfExporter = new JRPdfExporter();

            pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT,print);
            pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,realPath + nameFile+ ".pdf");

            pdfExporter.exportReport();
            File file1 = new File(realPath + nameFile + ".pdf");
            InputStream inputStream1 = new java.io.FileInputStream(file1);
            virtualizer.cleanup();

            return new DefaultStreamedContent(inputStream1,"application/pdf",nameFile+ ".pdf");

        }catch (FileNotFoundException e){
            e.printStackTrace();

        }catch (JRException e){
            e.printStackTrace();

        }

       return  null;

    }

    public StreamedContent exportExcel (List<Object> dataObject,String nameFile,Map map){

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataObject);
        FacesContext context = FacesContext.getCurrentInstance();
        String realPath = context.getExternalContext().getRealPath("") + File.separator + "report"
                + File.separator;
        File file = new File(realPath + nameFile + ".jasper");
        InputStream inputStream = null;

        try {

            inputStream = new java.io.FileInputStream(file);
            JRFileVirtualizer virtualizer = new JRFileVirtualizer(3,realPath);
            map.put(JRParameter.REPORT_VIRTUALIZER,virtualizer);
            JasperPrint print = JasperFillManager.fillReport(inputStream,map,dataSource);
            JRXlsExporter jrXlsExporter = new JRXlsExporter();

            jrXlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,print);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,Boolean.TRUE);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.TRUE);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,Boolean.TRUE);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,false);
            jrXlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,false);
            jrXlsExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,realPath + nameFile + ".xls");
            jrXlsExporter.exportReport();
            File file1 = new File(realPath + nameFile + ".xls");
            InputStream inputStream1 = new java.io.FileInputStream(file1);

            return new DefaultStreamedContent(inputStream1,"application/xls",nameFile + ".xls");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (JRException e){
            e.printStackTrace();
        }

        return null;

        }


    private static ExportReport Instance = null;

    public static ExportReport getInstance(){
        if (Instance==null){
            Instance = new ExportReport();
        }

        return Instance;
    }

}