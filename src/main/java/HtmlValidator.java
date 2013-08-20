import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.*;

/**
 * Created by Intellij IDEA.
 * User: Sardor Navruzov
 * Date: 8/20/13
 * Time: 12:30 PM
 */
public class HtmlValidator
{

    private int error=0;

    public HtmlValidator()
    {

    }

    public HtmlValidator(InputStream input, String outFileName)
    {
        try{
            org.jsoup.nodes.Document doc = Jsoup.parse(input, "UTF-8", "");
            Element div = doc.select("style").first();
            if(div!=null){
                div.appendText("@page {size: 297mm 210mm; margin: 0;}");
            } else
            {
                div = doc.select("head").first();
                div.append("<style type='text/css'> @page {size: 297mm 210mm; margin: 0;} </style>");
            }

            BufferedWriter htmlWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outFileName), "UTF-8"));
            htmlWriter.write(doc.toString());
            htmlWriter.flush();
            htmlWriter.close();
        }
        catch (IOException ex)
        {
            this.error = 1;
            ex.printStackTrace();
        }
    }

    public int hasError()
    {
        return error;
    }
}
