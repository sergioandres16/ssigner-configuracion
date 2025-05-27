package Entidades;

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
    
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException
    {
        String text2 = text.toUpperCase();
        
        if(text.toUpperCase().contains(tag.toUpperCase()))
        {
            Object[] t = textPositions.toArray();
            int ind = text2.indexOf(tag.toUpperCase());
            writeString(String.format("[%s , %s , %s]", ((TextPosition)t[ind]).getX(), ((TextPosition)t[ind]).getY(), text));
        }
    }
    
}
