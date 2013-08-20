/**
 * Created by Intellij IDEA.
 * User: Sardor Navruzov
 * Date: 8/20/13
 * Time: 12:12 PM
 */

import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;


import org.w3c.dom.Document;

import org.w3c.tidy.Tidy;

import org.xhtmlrenderer.swing.Java2DRenderer;


import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
public class HtmlToImgPDF
{
    public static final int PAGE_WIDTH = 2100;
       public static final int PAGE_LENGTH = 2970;
       public static final float MARGIN = 19.05f; // 0.75 inch



       public static void main(String[] args) throws IOException, FileNotFoundException, DocumentException, ParserConfigurationException, com.itextpdf.text.DocumentException
       {
           int width = PAGE_WIDTH;
           if (args.length != 2)
           {
               System.out.println("Error: You should type as: convertToPDF htmlFile pdfOut");
               System.exit(1);
           }

           String htmlFile = args[0];
           String pdfOutFile = args[1];

           FileInputStream input = null;
           try
           {
               input = new FileInputStream(htmlFile);
           } catch (java.io.FileNotFoundException e)
           {
               System.out.println("File not found: " + htmlFile);
           }

           Tidy tidy=new Tidy();
           tidy.setInputEncoding("UTF-8");
           tidy.setOutputEncoding("UTF-8");
           tidy.setQuiet(true);
           tidy.setXHTML(true);

           tidy.setJoinClasses(true);
           tidy.setSmartIndent(true);

           Document xhtmlDoc = tidy.parseDOM(input, null);
           xhtmlDoc.normalizeDocument();
           OutputStream os = new FileOutputStream(pdfOutFile);

           Java2DRenderer renderer = new Java2DRenderer(xhtmlDoc, width, -1);
           BufferedImage img = renderer.getImage();
           int height = img.getHeight();
           width = (int) Math.round(height * PAGE_WIDTH / PAGE_LENGTH);

           renderer = new Java2DRenderer(xhtmlDoc, width, height);
           img = renderer.getImage();

           com.itextpdf.text.Document pdf = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
           pdf.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
           PdfWriter.getInstance(pdf, os);
           pdf.open();
           com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(img, Color.WHITE);
           com.itextpdf.text.Rectangle ps = pdf.getPageSize();
           pdfImage.scaleAbsolute(ps.getWidth() - pdf.leftMargin() - pdf.rightMargin(), ps.getHeight() - pdf.topMargin() - pdf.bottomMargin());
           pdf.add(pdfImage);
           pdf.close();
           os.close();


       }

}
