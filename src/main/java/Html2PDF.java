import com.lowagie.text.DocumentException;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by Intellij IDEA.
 * User: Sardor Navruzov
 * Date: 8/15/13
 * Time: 10:42 AM
 */
public class Html2PDF
{
    public static void main(String[] args) throws IOException, ParserConfigurationException, DocumentException
    {
        if (args.length != 2)
        {
            System.out.println("Error: You should type as: convertToPDF htmlFile pdfOut");
            System.exit(1);
        }

        String htmlFile = args[0];
        String pdfOutFile = args[1];//"/home/sardor/Downloads/CV_B/test.pdf";

        FileInputStream input = reOpenStream(htmlFile);


        //Append required HTML elements via JSoup
        HtmlValidator validator = new HtmlValidator(input, htmlFile);
        if(validator.hasError()==1)
        {
            System.out.println("Error: Something wrong with HTML file");
            System.exit(1);
        }

        //Redefine file stream
        input = reOpenStream(htmlFile);

        //Validate out html document
        Tidy tidy=new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setIndentCdata(true);
        tidy.setQuiet(true);
        tidy.setForceOutput(true);
        tidy.setTidyMark(false);
        tidy.setXHTML(true);
        tidy.setJoinClasses(true);
        tidy.setSmartIndent(true);

        //Convert to XML view
        Document xhtmlDoc = tidy.parseDOM(input, null);

        //Create PDF file
        OutputStream os = new FileOutputStream(pdfOutFile);

        //Render pdf doc
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(xhtmlDoc,null);
        renderer.layout();
        renderer.createPDF(os);

        os.close();
	}

    private static FileInputStream reOpenStream(String htmlFile)
    {
        FileInputStream input = null;
        try
        {
            input = new FileInputStream(htmlFile);
        } catch (java.io.FileNotFoundException e)
        {
            System.out.println("File not found: " + htmlFile);
        }

        return input;
    }


}
