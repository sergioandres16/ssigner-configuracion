package Modulos;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;


public class GetCharLocalizationAndSize extends PDFTextStripper
{
    public GetCharLocalizationAndSize() throws IOException { }

    String tag = "";
    
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    
    /**
    * Override the default functionality of PDFTextStripper.
    */
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException
    {
        TextPosition firstProsition = textPositions.get(0);
        
        String text2 = text.toUpperCase();
        
        if(text.toUpperCase().contains(tag.toUpperCase()))
        {
            /*
            for (TextPosition t : textPositions)
                System.out.println("String[" + t.getX()+"," + t.getY()+"] " + t.getUnicode());
            */
            
            Object[] t = textPositions.toArray();
            int ind = text2.indexOf(tag.toUpperCase());
            
            //System.out.println("String[" + ((TextPosition)t[ind]).getX()+"," + ((TextPosition)t[ind]).getY()+"] " + ((TextPosition)t[ind]).getUnicode());

            writeString(String.format("[%s , %s , %s]", ((TextPosition)t[ind]).getX(), ((TextPosition)t[ind]).getY(), text));
        }
        
        //System.out.println(text);
    }
    
}
