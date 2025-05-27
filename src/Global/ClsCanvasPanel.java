package Global;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.abs;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

public class ClsCanvasPanel extends JPanel implements MouseMotionListener
{
    private Point inicioArrastre = new Point(0, 0);
    private Point finArrastre = new Point(0, 0);
    private int DibujoAncho = 0;
    private int DibujoAlto = 0;

    private boolean dragged = false; // indica si se ha arrastrado el mouse. Evita que se vuelva a dibujar la imagen si sólo se hace clic, se dibuja sólo después de arrastrar el mouse
    private Image imgFirmaVisible;
    
    int fontSize = 12;
    
    private String titulo = "";
    private String motivo = "";
    private String texto1 = "";
    private String texto2 = "";
    
    private int pagina;
    private int noumpaginas;
    private int numfirmas;
    private int numfirmasVisibles;
    private Image img_page;
    private Document document;
    private float[][] fieldPositions;
    private String[] infoFirmas;
    private String[] dateFirmas;
    private com.itextpdf.text.Rectangle[] paginaSize;
    private int[] paginaRotation;
    private float zoom;
    private int offSetX;
    private int offSetY;

    public void setInicioArrastre(Point inicioArrastre)
    {
        this.inicioArrastre = inicioArrastre;
    }

    public void setFinArrastre(Point finArrastre)
    {
        this.finArrastre = finArrastre;
    }
    
    public void setOffSetX(int offSetX)
    {
        this.offSetX = offSetX;
    }

    public void setOffSetY(int offSetY)
    {
        this.offSetY = offSetY;
    }

    public int getOffSetX()
    {
        return offSetX;
    }

    public int getOffSetY()
    {
        return offSetY;
    }

    public float getZoom()
    {
        return zoom;
    }

    public void setZoom(float zoom)
    {
        this.zoom = zoom;
    }

    public Document getDocument()
    {
        return document;
    }
    
    public Point[] getCoordenadas()
    {
        return new Point[]{inicioArrastre, finArrastre};
    }
    
    public int[] getPaginasConFirma()
    {
        int[] paginasFirma = new int[numfirmas];
        
        for (int i = 0; i < numfirmas; i++)
            paginasFirma[i] = (int)fieldPositions[i][0];
        
        return Arrays.stream(paginasFirma).distinct().toArray();
    }
    
    public int getAnchoReal(int page)
    {
        if (page >= 0)
            return (int)paginaSize[page].getWidth();
        else
            return (int)paginaSize[0].getWidth();
    }
    
    public int getAltoReal(int page)
    {
        if(page >= 0)
            return (int)paginaSize[page].getHeight();
        else
            return (int)paginaSize[0].getHeight();
    }
    public int getRotacionPagina(int page)
    {
        if(page >= 0)
            return paginaRotation[page];
        else
            return paginaRotation[0];
    }
    
    public Rectangle getRectangle()
    {
        return crearRectangulo(inicioArrastre.x, inicioArrastre.y, finArrastre.x, finArrastre.y).getBounds();
    }
    
    public int getnumFirmas()
    {
        return numfirmas;
    }
    
    public int getnumFirmasVisibles()
    {
        return numfirmasVisibles;
    }
    
    public ClsCanvasPanel()
    {
        setBorder(BorderFactory.createLineBorder(Color.black));
        pagina = 0;
        noumpaginas = 0;
        setBackground(Color.WHITE);
        numfirmas = 0;
        numfirmasVisibles = 0;
        fieldPositions = null;
        img_page = null;
        inicioArrastre = new Point(0, 0);
        finArrastre = new Point(0, 0);
        document = null;
        DibujoAncho = 0;
        DibujoAlto = 0;
        paginaSize = null;
        zoom = 1;
        offSetX = 0;
        offSetY = 0;
    }
    
    public void PrevPage()
    {
        if(pagina > 0)
        {
            pagina--;
            setPage(pagina, zoom);
        }
    }
    
    public void resetearArrastre()
    {
        inicioArrastre = new Point(0, 0);
        finArrastre = new Point(0, 0);        
    }
    
    public void NextPage()
    {
        if(pagina < noumpaginas - 1)
        {
            pagina++;
            setPage(pagina, zoom);
        }
    }
    
    public void LastPage()
    {
        pagina = noumpaginas - 1;
        setPage(pagina, zoom);
    }
    
    public void FirstPage()
    {
        pagina = 0;
        setPage(pagina, zoom);
    }
    
    public void setPage(int pag, float zoom)
    {
        pagina = pag;
        
        if(document != null)
        {
            float rotation = 0f;

            try
            {
                // Paint each pages content to an image and write the image to file
                img_page =  document.getPageImage(pagina, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, rotation, zoom);
            } catch (Exception e) {  }
        }
        
        this.zoom = zoom;
        this.repaint();
    }
    
    public int getNumPages()
    {
        return noumpaginas;
    }
    
    public int getCurrentPage()
    {
        return pagina;
    }
    
    public void setPDF(String rutapdf)
    {
        try
        {
            document = new Document();
            document.setFile(rutapdf);
            pagina = 0;
            noumpaginas = document.getNumberOfPages();
            zoom = 1;

            PdfReader reader = new PdfReader(rutapdf);

            // https://www.codota.com/web/assistant/code/rs/5c77ec64e70f870001932487#L75
            // http://www.java2s.com/example/java-api/com/lowagie/text/pdf/acrofields/getfieldpositions-1-0.html
            // https://stackoverflow.com/a/19901784
            AcroFields acroFields = reader.getAcroFields();
            List<String> signatureNames = acroFields.getSignatureNames();
            
            numfirmas = signatureNames.size();
            numfirmasVisibles = 0;
            paginaSize = new com.itextpdf.text.Rectangle[noumpaginas];
            fieldPositions = new float[numfirmas][5];
            paginaRotation = new int[noumpaginas];
            infoFirmas = new String[numfirmas];
            dateFirmas = new String[numfirmas];
            
            int i = 0;
            for(String sig : signatureNames)
            {
                List<AcroFields.FieldPosition> positions = acroFields.getFieldPositions(sig);
                
                if(positions.isEmpty())
                    continue;
                
                com.itextpdf.text.Rectangle rect = positions.get(0).position; // In points:
                fieldPositions[i] = new float[]{positions.get(0).page, rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom()};
                
                PdfPKCS7 pk = null;
                
                try
                {
                    pk = acroFields.verifySignature(sig);
                    infoFirmas[i] = CertificateInfo.getSubjectFields(pk.getSigningCertificate()).getField("CN");
                    dateFirmas[i] = new SimpleDateFormat("yyyy.MM.dd").format(pk.getSignDate().getTime());
                }
                catch(Exception e) 
                {
                    infoFirmas[i] = "null";
                    dateFirmas[i] = "null";
                }                                                                                                                                                  
                
                if((fieldPositions[i][1] + fieldPositions[i][2] + fieldPositions[i][3] + fieldPositions[i][4]) > 0.1f)
                    numfirmasVisibles++;
                
                i++;
            }
            
            for(int j = 1; j <= reader.getNumberOfPages(); j++)
            {    
                paginaSize[j - 1] = reader.getPageSize(j);
                paginaRotation[j - 1] = reader.getPageRotation(j);
            }
            
            // https://www.icesoft.org/wiki/display/PDF/Converting+PDF+Page+Renderings
            // save page captures
            float scale = 1.0f;
            float rotation = 0f;
            // Paint each pages content to an image         
            img_page =  document.getPageImage(pagina, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
            inicioArrastre = new Point(0, 0);
            finArrastre = new Point(0, 0);
        }
        catch(Exception e) 
        {
            e.toString();
        }
        
        paintComponent(this.getGraphics());
    }
    
    public void clearPanel()
    {
        inicioArrastre = new Point(0, 0);
        finArrastre = new Point(0, 0);
        DibujoAncho = 0;
        DibujoAlto = 0;
        noumpaginas = 0;
        dragged = false;
        imgFirmaVisible = null;
        img_page = null;
        setBackground(Color.WHITE);
        document = null;
        fieldPositions = null;
        zoom = 1;
        pagina = 0;
        paginaSize = null;
        numfirmas = 0;
        numfirmasVisibles = 0;
        infoFirmas = null;
        paginaRotation = null;
        dateFirmas = null;
        offSetX = 0;
        offSetY = 0;
        this.repaint();
    }
    
    private void arreglarCoordenadas()
    {
        int ty, tx;
        
        if(inicioArrastre.x > finArrastre.x)
        {
            tx = finArrastre.x;
            finArrastre.x = inicioArrastre.x;
            inicioArrastre.x = tx;
        }
        
        if(inicioArrastre.y > finArrastre.y)
        {
            ty = finArrastre.y;
            finArrastre.y = inicioArrastre.y;
            inicioArrastre.y = ty;
        }
    }
    
    // firma relativa
    public void setCoordenadas(String firmarelativa, String separadorCadena)
    {
        String[] splitIt = firmarelativa.split(separadorCadena); // si todo está bien, devuelve 6 valores, uso los primeros cuatro
        
        if(splitIt.length < 6)
            return;
        
        inicioArrastre.x = Integer.parseInt(splitIt[0]);
        inicioArrastre.y = Integer.parseInt(splitIt[1]);
        finArrastre.x = Integer.parseInt(splitIt[2]);
        finArrastre.y = Integer.parseInt(splitIt[3]);
        this.repaint();
    }

    public int getRectangleWidth()
    {
        return DibujoAncho;
    }
    
    public int getRectangleHeight()
    {
        return DibujoAlto;
    }
    
    // firma absoluta
    public void setCoordenadas(int x, int y, int w, int h)
    { 
        inicioArrastre.x = x;
        inicioArrastre.y = y;
        finArrastre.x = inicioArrastre.x + w;
        finArrastre.y = inicioArrastre.y + h;
        this.repaint();
    }

    public void setImage(Image im)
    {
        imgFirmaVisible = im;
        dragged = true;
        this.repaint();
        dragged = false;
    }
    
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
       
        Font fuente = new Font("Monospaced", Font.BOLD, this.fontSize);
        g.setFont(fuente);
        setBackground(Color.WHITE);
        Graphics2D g2 = (Graphics2D)g;
        
        if(img_page != null)
            g2.drawImage(img_page, 0, 0, this.getWidth(), this.getHeight(), null);
              
        Shape rectangulo = crearRectangulo(inicioArrastre.x + offSetX, inicioArrastre.y + offSetY, finArrastre.x + offSetX, finArrastre.y + offSetY);
        DibujoAncho = abs(finArrastre.x - inicioArrastre.x);
        DibujoAlto = abs(finArrastre.y - inicioArrastre.y);
        
        if(DibujoAlto != 0 && DibujoAncho != 0)
        {
            g2.setPaint(Color.blue);
            g2.draw(rectangulo);

            if(!dragged && imgFirmaVisible != null)
                g2.drawImage(imgFirmaVisible, inicioArrastre.x, inicioArrastre.y, DibujoAncho, DibujoAlto, null);
        }
        
        //https://www.codota.com/web/assistant/code/rs/5c77ec64e70f870001932487#L75
        if(fieldPositions != null && fieldPositions.length > 0)
            for(int i = 0; i < fieldPositions.length; i++)
                if(pagina == (int) fieldPositions[i][0] - 1)
                {
                    int x1, y1, x2, y2;
                    
                    try
                    {
                        if(paginaRotation[pagina] == 0 || paginaRotation[pagina] == 180)
                        {
                            x1 = (int)(fieldPositions[i][1] * this.getWidth() / paginaSize[(int)fieldPositions[i][0] - 1].getWidth());
                            y1 = this.getHeight() - (int)(fieldPositions[i][2] * this.getHeight() / paginaSize[(int)fieldPositions[i][0] - 1].getHeight());
                            x2 = (int)(fieldPositions[i][3] * this.getWidth() / paginaSize[(int)fieldPositions[i][0] - 1].getWidth());
                            y2 = this.getHeight() - (int)(fieldPositions[i][4] * this.getHeight() / paginaSize[(int)fieldPositions[i][0] - 1].getHeight());
                        }
                        else
                        {
                            x1 = (int)(fieldPositions[i][1] * this.getWidth() / paginaSize[(int)fieldPositions[i][0] - 1].getHeight());
                            y1 = this.getHeight() - (int)(fieldPositions[i][2] * this.getHeight() / paginaSize[(int)fieldPositions[i][0] - 1].getWidth());
                            x2 = (int)(fieldPositions[i][3] * this.getWidth() / paginaSize[(int)fieldPositions[i][0] - 1].getHeight());
                            y2 = this.getHeight() - (int)(fieldPositions[i][4] * this.getHeight() / paginaSize[(int)fieldPositions[i][0] - 1].getWidth());
                        }

                        g2.setPaint(Color.RED);
                        Shape rectangulo2 = crearRectangulo(x1, y1, x2, y2);
                        g2.draw(rectangulo2);
                        g2.setFont(fuente);
                        g2.setPaint(Color.BLACK);
                        g2.drawString(infoFirmas[i], x1 + 1, y1 + 32);
                        g2.drawString(dateFirmas[i], x1 + 1, y1 + 21);
                        g2.drawString("Firmado por", x1 + 1, y1 + 10);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.toString());
                    }
                }
        
        g2.dispose();
        
    }
    
    private Rectangle2D.Float crearRectangulo(int x1, int y1, int x2, int y2)
    {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(document == null)
            return;
        
        if(!dragged)
            inicioArrastre = new Point(e.getX(), e.getY());

        finArrastre = new Point(e.getX(), e.getY());
        paintComponent(this.getGraphics());
        dragged = true;
    }
        
    
    public void mouseReleased(MouseEvent e)
    {
        if(document == null)
            return;
        
        if(!dragged)
            return;                

        paintComponent(this.getGraphics());
       
        Graphics2D g2 = (Graphics2D)this.getGraphics();
                     
        finArrastre = new Point(e.getX(), e.getY());
        dragged = false;
        arreglarCoordenadas();
        
        Rectangle rectangulo = crearRectangulo(inicioArrastre.x, inicioArrastre.y, finArrastre.x, finArrastre.y).getBounds();
        
        if(imgFirmaVisible != null)
            g2.drawImage(imgFirmaVisible, rectangulo.x, rectangulo.y, rectangulo.width, rectangulo.height, null);
        
        g2.setPaint(Color.blue);
        g2.draw(rectangulo);
        g2.dispose();
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}