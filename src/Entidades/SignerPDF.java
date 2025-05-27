package Entidades;

import Global.CConstantes;
import Global.CLog;
import Modulos.frmFirmador;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.TSAClientBouncyCastle;
import detachedsignature.SignatureStamper;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Math.abs;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;
import org.bouncycastle.tsp.TimeStampToken;

public class SignerPDF extends Thread
{
    private final String rootDest;
    private final String rootSource;
    private final String file_name;

    private final PrivateKey key;
    private final Certificate[] certificateChain;
    private final X509Certificate certificate;
    private final String aliasoficial;
    private final String c_name;
    private final boolean big_file;

    private final Map<String, String> xparametros;
    private final CConstantes xconstantes;
    private final String rutaCache;
    private int indice;
    private final String nombreProveedor;
    
    public SignerPDF()
    {
        rootDest = "";
        rootSource = "";
        file_name = "";
        certificateChain = null;
        certificate = null;
        aliasoficial = "";
        c_name = "";
        big_file = false;
        key = null;
        xconstantes = new CConstantes();
        xparametros = new HashMap<>();
        rutaCache = "";
        nombreProveedor = "";
    }

    private String getCN(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String cn = "";

        try
        {
            LdapName ln = new LdapName(dn);

            for (Rdn rdn : ln.getRdns())
            {
                if (rdn.getType().equalsIgnoreCase("CN"))
                {
                    cn = rdn.getValue().toString();
                    break;
                }
            }
        }
        catch (Exception e) { }

        return cn;
    }
    
    private String getCargo(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String titulo = "";

        try
        {
            LdapName ln = new LdapName(dn);

            for(Rdn rdn : ln.getRdns())
            {
                if(rdn.getType().equalsIgnoreCase("T")) 
                {
                    titulo = rdn.getValue().toString();
                    
                    if (!titulo.isEmpty())
                        break;
                }
            }                
        }
        catch(Exception e) {}
        
        return titulo;
    }
    
    private String getEmpresa(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String titulo = "";

        try
        {
            LdapName ln = new LdapName(dn);

            for(Rdn rdn : ln.getRdns())
                if(rdn.getType().equalsIgnoreCase("O")) 
                {
                    titulo = rdn.getValue().toString();
                    break;
                }
        }
        catch(Exception e) {}
        
        return titulo;
    }

    public SignerPDF(Map<String, String> xparam, String filename, String root, String dest, String aliasOficial, PrivateKey key0, Certificate[] cadenaCert, X509Certificate cert, boolean big_pdf, String cn, String rutacache, int indice, String proveedor) {
        rootDest = dest;
        rootSource = root;
        file_name = filename;
        certificateChain = cadenaCert;
        aliasoficial = aliasOficial;
        c_name = cn;
        big_file = big_pdf;
        key = key0;
        xconstantes = new CConstantes(rutacache);
        xparametros = xparam;
        certificate = cert;
        rutaCache = rutacache;
        this.indice = indice;
        nombreProveedor = proveedor;
    }

    @Override
    public void run() {
        try {
            EntryPoint(file_name, rootSource, rootDest, aliasoficial, key, certificateChain, certificate, big_file, c_name, indice, nombreProveedor);           
        } catch (Exception ex) {
            Logger.getLogger(SignerPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void EntryPoint(String archivoOriginal, String rootFolder, String destFolder, String aliasOficial, PrivateKey key0, Certificate[] cadenaCert, X509Certificate cert, boolean big_pdf, String cn, int indice, String proveedor) {
        if (certificateChain == null) {
            throw new IllegalArgumentException(); // sólo cadenas válidas
        }
        if (xparametros.isEmpty()) {
            // si los parámetros ya han sido validados a la hora de llamar a validar_antes, la ejecución nunca debe caer acá.
            CLog xlog = new CLog();
//            xlog.archivar_log(rutaCache, xlog.Y_ERROR, "Parámetros de entrada incorrectos", "");  
            xlog.archivar_log("Parámetros de entrada incorrectos", "");
//            xlog.close();
            return;
        }
        // armo la ruta completa de destino
        Path rootPath = Paths.get(rootFolder);
        Path relativePath = rootPath.relativize(Paths.get(archivoOriginal));
        //Path destFile = Paths.get(destFolder).resolve(relativePath);
        String carpeta_salida = destFolder + File.separator;
        
        String doc_carpeta_salida_ruta = carpeta_salida
                + (!frmFirmador.configuracion.isVg_documento_extension_inicio()
                        ? new File(archivoOriginal).getName().substring(0, new File(archivoOriginal).getName().lastIndexOf(".")) + frmFirmador.configuracion.getVg_documento_extension_salida() + ".pdf"
                        : frmFirmador.configuracion.getVg_documento_extension_salida() + new File(archivoOriginal).getName());

        Path destFile = Paths.get(doc_carpeta_salida_ruta);//destFolder).resolve(relativePath);
        
        File f = new File(doc_carpeta_salida_ruta).getParentFile();

        if (!f.exists()) {
            f.mkdirs();
        }

        // http://stackoverflow.com/a/3571239
        String file_ext = "";

        int pos = archivoOriginal.lastIndexOf('.');

        if (pos > 0) {
            file_ext = archivoOriginal.substring(pos).toUpperCase();
        }

        if (file_ext.equals(".PDF")) {
            firmadigital(certificateChain, key, archivoOriginal, destFile.toString(), big_pdf, cn, indice, proveedor);
        } else {
            firmadigital_anyfile(cert, key, archivoOriginal, destFile.toString(), indice);
        }
    }

    private synchronized int firmadigital_anyfile(X509Certificate cert, final PrivateKey key, final String archivoorigen, final String archivoDest, int indice) {
        SignatureStamper sig;
        //////////////// se usan si se escoge la opción con tsa /////////
        String TSA_URL = frmFirmador.configuracion.getVg_tsa_url();//xparametros.get(xconstantes.VAR31_RUTA_TSA);
        String TSA_ACCNT = frmFirmador.configuracion.getVg_tsa_usuario();
        String TSA_PASSW = new helperClass().decrypt(frmFirmador.configuracion.getVg_tsa_clave());
        /////////////////////////////////////////////////////////////////
        final JProgressBar progreso = (JProgressBar) frmFirmador.dgvDocumentos.getValueAt(indice, 2);
        try {

            try {
                new URL(TSA_URL); // testeo si la url es correcta
            } catch (Exception e) {
                TSA_ACCNT = TSA_URL = TSA_PASSW = "";
            }
            
            progreso.setValue(60);
            frmFirmador.dgvDocumentos.repaint();
            frmFirmador.dgvDocumentos.setValueAt("Insertando Sello de Tiempo", indice, 4);
            sig = new SignatureStamper();
            byte[] signature = sig.createSignature(cert, key, archivoorigen, TSA_URL, TSA_ACCNT, TSA_PASSW);

            // armo la ruta completa de destino
            final File f_origen = new File(archivoorigen);
            int pos = archivoDest.lastIndexOf('.');
            String file_name0 = "";

            if (pos > 0)
                file_name0 = archivoDest.substring(0, pos);

            String signatureFile = file_name0 + ".p7b"; // ya está en la carpeta de destino

            if (signature != null)
            {
                progreso.setValue(75);
                frmFirmador.dgvDocumentos.repaint();
                new File(archivoDest).delete();
                Files.write(Paths.get(signatureFile), signature);
                //System.out.println("Ruta de destino de la firma [" + new File(signatureFile).getAbsolutePath() + "]");
                Files.copy(f_origen.toPath(), Paths.get(archivoDest), StandardCopyOption.COPY_ATTRIBUTES.REPLACE_EXISTING);
                //System.out.println("Ruta de destino del documento original [" + new File(archivoDest).getAbsolutePath() + "]");;
                progreso.setString("Firmado");
                frmFirmador.contarFirmados();
                progreso.setStringPainted(true);
                progreso.setBackground(new Color(211, 111, 66));
                progreso.setForeground(new Color(211, 111, 66));
                progreso.setUI(new BasicProgressBarUI() {
                    @Override
                    protected Color getSelectionBackground() {
                        return null;
                    }

                    @Override
                    protected Color getSelectionForeground() {
                        return Color.white;
                    }
                });
                frmFirmador.dgvDocumentos.repaint();
                frmFirmador.dgvDocumentos.setValueAt("Ver firma", indice, 4);
                frmFirmador.dgvDocumentos.setValueAt(signatureFile, indice, 5);

                return 0;
            }
            else
                throw new Exception("Error al firmar");
            
        }
        catch (Exception ex)
        {
            frmFirmador.contarErrados();
            progreso.setString(ex.getMessage());
            progreso.setStringPainted(true);
            progreso.setForeground(Color.red);
            progreso.setBackground(Color.red);
            progreso.setUI(new BasicProgressBarUI() {
                @Override
                protected Color getSelectionBackground() {
                    return null;
                }

                @Override
                protected Color getSelectionForeground() {
                    return Color.white;
                }
            });
            frmFirmador.dgvDocumentos.repaint();
            frmFirmador.dgvDocumentos.setValueAt("Eliminar", indice, 4);

            return 1;
        }
    }

    private synchronized int firmadigital(Certificate[] cadenacer, final PrivateKey key, final String archivoOriginal, final String archivoDestino, boolean big_file, String cn, int indice, String proveedor) {
        //////////////// se usan si se escoge la opción con tsa /////////
        final String TSA_URL = frmFirmador.configuracion.getVg_tsa_url();//xparametros.get(xconstantes.VAR41_RUTA_TSA);
        final String TSA_ACCNT = frmFirmador.configuracion.getVg_tsa_usuario();//xparametros.get(xconstantes.VAR42_USER_TSA);
        final String TSA_PASSW = new helperClass().decrypt(frmFirmador.configuracion.getVg_tsa_clave());//xparametros.get(xconstantes.VAR33_PWD_TSA);
        /////////////////////////////////////////////////////////////////
        boolean conTSA = (TSA_URL.length() != 0);
        final JProgressBar progreso = (JProgressBar) frmFirmador.dgvDocumentos.getValueAt(indice, 2);
        
        try 
        {
            new URL(TSA_URL); // testeo si la url es correcta
        }
        catch (Exception e) 
        {
            conTSA = false;
        }

        FileOutputStream fout;
        File f_destpdf = new File(archivoDestino); // ya apunta a firmadosFolder
        RandomAccessFileOrArray raf;
        String firmaposterior = null;
        String newPdf = archivoDestino + ".ssg";
        
        try 
        {
            // si el archivo existe, lo sobreescribo
            if (f_destpdf.exists())
            {
                if (!f_destpdf.delete())
                    throw new Exception("No se pudo eliminar el archivo");
            }

            // http://stackoverflow.com/questions/30449348/signing-pdf-memory-consumption
            fout = new FileOutputStream(f_destpdf);

//        try
//        {
            raf = new RandomAccessFileOrArray(archivoOriginal, false, true);
            PdfReader reader = new PdfReader(raf, null);
            boolean cerrarDocumento = frmFirmador.configuracion.isVg_cerrar_documento();//.equals("TRUE");
            PdfStamper stp = null;

            File f_temp = big_file ? new File(archivoDestino + ".tmp") : null;
            
            progreso.setValue(25);
            frmFirmador.dgvDocumentos.setValueAt("Generando Temporal", indice, 4);
            frmFirmador.dgvDocumentos.repaint();

            if (reader.getCertificationLevel() == PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED)
                throw new Exception("Está cerrado");

            PdfSignatureAppearance sap = null;
            
            if (frmFirmador.configuracion.isVg_firma_visible())
            {
                int paginas = reader.getNumberOfPages();
                //Ver posicion de la Firma si es 0 ira en la ultima pagina
                int pagfirma = xconstantes.intConvertiranumero("" + frmFirmador.configuracion.getVg_firma_visible_pagina());

                if (pagfirma < 1 || pagfirma > paginas)
                    pagfirma = paginas;

                //Tamaño de la hoja donde irá la firma
                com.itextpdf.text.Rectangle area = reader.getPageSize(pagfirma);
                String posicionrelativa = "";

                int posx = 0;
                int posy = 0;
                int posx2 = 0;
                int posy2 = 0;
                int tamy = (int) area.getHeight();
                int tamx = (int) area.getWidth();

                int talto = xconstantes.intConvertiranumero("" + (int) frmFirmador.configuracion.getVg_firma_visible_y1());
                int tancho = xconstantes.intConvertiranumero("" + (int) frmFirmador.configuracion.getVg_firma_visible_x1());

                String etiqueta = frmFirmador.configuracion.getEtiqueta_firma();
                
                if (!etiqueta.isEmpty())
                {
                    List<float[]> lasCoordenadas = new ArrayList<>();
                    lasCoordenadas = new helperClass().getCoordenadas(Files.readAllBytes(Paths.get(archivoOriginal)), archivoOriginal, null, frmFirmador.configuracion.getEtiqueta_firma());

                    if(lasCoordenadas != null && lasCoordenadas.size() > 0 && lasCoordenadas.get(0).length > 2)
                    {
                        pagfirma = (int)lasCoordenadas.get(0)[0];

                        if (pagfirma < 1 || pagfirma > paginas)
                            pagfirma = paginas;

                        posx =  (int)lasCoordenadas.get(0)[1];
                        posy =  (int)lasCoordenadas.get(0)[2];
                        posx2 =  (int)frmFirmador.configuracion.getVg_firma_visible_x1();
                        posy2 =  (int)frmFirmador.configuracion.getVg_firma_visible_y1();

                        posx2 = posx + Math.abs(posx2 - (int)frmFirmador.configuracion.getVg_firma_visible_x());
                        posy2 = posy + Math.abs(posy2 - (int)frmFirmador.configuracion.getVg_firma_visible_y());
                    }
                    else
                        etiqueta = "";
                }
                
                if (etiqueta.isEmpty() || pagfirma == -1)
                {
                    String[] posiciones = posicionrelativa.split("");

                    // quiero que el ratio alto/ancho de la firma visible se mantenga sin importar el ratio alto/ancho de la página del pdf
                    if (posicionrelativa.length() > 0 && posiciones.length > 2)
                    {
                        int xx1 = 0;
                        int yy1 = 0;
                        int xx2 = 0;
                        int yy2 = 0;

                        if (posiciones.length == 6)
                        {
                            // para este caso específico, tamy es igual a tamx
                            try 
                            {
                                xx1 = Integer.parseInt(posiciones[0]);
                                yy1 = Integer.parseInt(posiciones[1]);
                                xx2 = Integer.parseInt(posiciones[2]);
                                yy2 = Integer.parseInt(posiciones[3]);

                                tamx = Integer.parseInt(posiciones[4]);
                                tamy = Integer.parseInt(posiciones[5]);
                            } 
                            catch (Exception e) 
                            {
                                xx1 = 0;
                                yy1 = 0;
                                xx2 = 0;
                                yy2 = 0;

                                tamx = 0;
                                tamy = 0;
                            }

                            //Calculando
                            posx = Math.round(xx1 * area.getWidth() / tamx);
                            posx2 = Math.round(xx2 * area.getWidth() / tamx);

                            // iText coloca el origen en la esquina inferior izquierda
                            posy = Math.round(area.getHeight() - yy1 * area.getHeight() / tamy);
                            posy2 = Math.round(area.getHeight() - yy2 * area.getHeight() / tamy);

                            // verdadero ancho (el alto ya está ajustado)
                            int wid = (int) (Math.abs(xx2 - xx1) * area.getHeight() / tamy);
                            int wid2 = Math.abs(posx2 - posx);

                            // la posición x está mal, hay que ajustarla:
                            posx += (int) ((wid2 - wid) / 2);
                            posx2 -= (int) ((wid2 - wid) / 2);

                            if (area.getWidth() < area.getHeight() && posx > area.getWidth() / 2) {
                                posx2 -= Math.abs((wid - wid2) / 2);
                                posx -= Math.abs((wid - wid2) / 2);
                            }

                            if (area.getWidth() > area.getHeight() && posx > area.getWidth() / 2) {
                                posx2 += Math.abs((wid - wid2) / 2);
                                posx += Math.abs((wid - wid2) / 2);
                            }

                            if (area.getWidth() < area.getHeight() && posx < area.getWidth() / 2) {
                                posx2 += Math.abs((wid - wid2) / 2);
                                posx += Math.abs((wid - wid2) / 2);
                            }

                            if (area.getWidth() > area.getHeight() && posx < area.getWidth() / 2) {
                                posx2 -= Math.abs((wid - wid2) / 2);
                                posx -= Math.abs((wid - wid2) / 2);
                            }
                        }
                        else // le estoy mandando sólo dos parámetros para este caso, quiero que la firma visible se estire al ratio alto/ancho de la página del pdf
                        {
                            xx1 = xconstantes.intConvertiranumero("" + frmFirmador.configuracion.getVg_firma_visible_x());//xparametros.get(xconstantes.VAR7_XFIRMA));
                            yy1 = xconstantes.intConvertiranumero("" + frmFirmador.configuracion.getVg_firma_visible_y());//xparametros.get(xconstantes.VAR8_YFIRMA));

                            xx2 = xx1 + tancho;
                            yy2 = yy1 + talto;

                            tamx = Integer.parseInt(posiciones[0]);
                            tamy = Integer.parseInt(posiciones[1]);

                            //Calculando
                            posx = Math.round(xx1 * area.getWidth() / tamx);
                            posx2 = Math.round(xx2 * area.getWidth() / tamx);

                            // iText coloca el origen en la esquina inferior izquierda
                            posy = Math.round(area.getHeight() - yy1 * area.getHeight() / tamy);
                            posy2 = Math.round(area.getHeight() - yy2 * area.getHeight() / tamy);
                        }
                    }
                    else
                    {
                        posx = xconstantes.intConvertiranumero("" + (int) frmFirmador.configuracion.getVg_firma_visible_x());
                        posy = xconstantes.intConvertiranumero("" + (int) frmFirmador.configuracion.getVg_firma_visible_y());

                        posx2 = posx + (-posx + tancho);
                        posy2 = posy + (-posy + talto);
                    }
                }
                
                if (frmFirmador.configuracion.isVg_firma_visible_firmar_todas_paginas())
                {
                    reader.close();
                    fout.close();
                    fout = null;
                    reader = null;
                    
                    raf = new RandomAccessFileOrArray(archivoOriginal, false, true);
                    reader = new PdfReader(raf, null);
                    
                    try (FileOutputStream ftempout = new FileOutputStream(newPdf))
                    {
                        stp = new PdfStamper(reader, ftempout, '\0', true);
                        AcroFields acroFields = stp.getAcroFields();
                        List listaacrofieldsfirmado = acroFields.getSignatureNames();
                        PdfFormField sig = PdfFormField.createSignature(stp.getWriter());
                        sig.setWidget(new Rectangle(posx, posy, posx2, posy2), null);
                        sig.setFlags(PdfAnnotation.FLAGS_PRINT);
                        sig.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g"));
                        String firmaprevia = null;

                        if (listaacrofieldsfirmado.size() > 0)
                        {
                            firmaprevia = "Signature" + listaacrofieldsfirmado.size();

                            for (Object listaacrofieldsfirmado1 : listaacrofieldsfirmado)
                            {
                                String cajatexto = (String) listaacrofieldsfirmado1;

                                if (firmaprevia.equals(cajatexto))
                                {
                                    firmaprevia = "Signature" + (Integer.parseInt(cajatexto.substring(cajatexto.lastIndexOf("e") + 1, cajatexto.length())) + 1);
                                    break;
                                }
                            }
                        }

                        firmaposterior = firmaprevia != null ? firmaprevia : "Signature1";

                        sig.setFieldName(firmaposterior);
                        sig.setPage(pagfirma);

                        for (int i = 1; i <= paginas; i++)
                            stp.addAnnotation(sig, i);

                        stp.close();
                        stp = null;
                        ftempout.flush();
                    }

                    raf.close();
                    reader.close();
                    reader = null;
                    raf = null;

                    Files.delete(Paths.get(archivoDestino));

                    raf = new RandomAccessFileOrArray(newPdf, false, true);
                    reader = new PdfReader(raf, null);
                }
                
                String rutaImg = frmFirmador.configuracion.getVg_firma_visible_ruta_imagen();
                File fImg = new File(rutaImg);
                sap = null;
                
                if (stp != null)
                    stp.close();
                
                stp = null;
                
                if (fout == null)
                    fout = new FileOutputStream(f_destpdf);
                
                stp = PdfStamper.createSignature(reader, fout, '\0', f_destpdf, true);
                sap = stp.getSignatureAppearance();
                sap.setAcro6Layers(true);
                
                if (frmFirmador.configuracion.isVg_firma_visible_firmar_todas_paginas())
                    sap.setVisibleSignature(firmaposterior);
                else 
                    sap.setVisibleSignature(new Rectangle(posx, posy, posx2, posy2), pagfirma, firmaposterior);

                if (fImg.isFile() && frmFirmador.configuracion.getVg_firma_visible_estilo_firma() != 1)
                {
                    Image watermark = Image.getInstance(rutaImg);

                    // una imagen con el texto al lado
                    if (frmFirmador.configuracion.getVg_firma_visible_estilo_firma() == 3)
                    {
                        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
                        sap.setSignatureGraphic(watermark);
                        sap.setImage(null);
                    }
                    else
                    {
                        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
                        sap.setImage(watermark);
                    }
                }
                else
                {
                    BufferedImage watermark = new BufferedImage(abs(posx2 - posx), abs(posy2 - posy), BufferedImage.TRANSLUCENT);
                    Graphics2D g2d = watermark.createGraphics();
                    g2d.setComposite(AlphaComposite.Clear);
                    g2d.fillRect(0, 0, posx2 - posx, posy2 - posy);
                    // http://www.coderanch.com/t/473390/open-source/convert-java-awt-Image-lowagie
                    sap.setImage(Image.getInstance(watermark, null));
                }

                String ttitulo = "";
                int ttamletra = xconstantes.intConvertiranumero(frmFirmador.txtTamanioFuente.getText());

                Font font = new Font(Font.FontFamily.HELVETICA, ttamletra);
                //sap.setVisibleSignature(new com.itextpdf.text.Rectangle(posx, posy, posx2, posy2), pagfirma, null);

                if (ttitulo.length() != 0)
                {
                    PdfTemplate template4 = sap.getLayer(4);
                    template4.setFontAndSize(font.getCalculatedBaseFont(true), ttamletra);
                    float f0 = font.getCalculatedBaseFont(true).getAscentPoint(ttitulo, ttamletra);
                    template4.showTextAligned(PdfContentByte.ALIGN_LEFT, ttitulo, 0, Math.abs(posy2 - posy) - f0, 0);
                }

                //template4.showText(ttitulo); // para que se pueda modificar el tamaño del texto en el título de la firma visible
                //sap.setLayer4Text(ttitulo); // esto no existe
                String textofirma = "";

                if (frmFirmador.configuracion.getVg_firma_visible_estilo_firma() != 0)
                {
                    String tlado = getEmpresa(certificate);

                    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String tabajo = DateFor.format(new Date());

                    /*if (frmFirmador.configuracion.getVg_firma_visible_estilo_firma() == 2)
                    {
                        float f_asc = font.getCalculatedBaseFont(false).getAscentPoint("tlp", ttamletra);

                        talto = Math.abs(posy2 - posy);
                        float f02 = f_asc;

                        // a prueba y error para cuadrar bien el texto al costado de la imagen
                        while (f02 < talto / 8)
                        {
                            textofirma += "\n";
                            f02 += f_asc;
                        }
                    }*/

                    textofirma += frmFirmador.configuracion.getVg_texto_firmante() + " " + cn + "\n";
                    
                    String cargo = getCargo(certificate);

                    if (cargo.length() > 0)
                        textofirma = textofirma + "Cargo: " + cargo + "\n";

                    // otros textos
                    if(tlado.length() > 0)
                    {
                        if (!frmFirmador.configuracion.getConCampoEmpresa())
                            textofirma = textofirma + "\n";                        
                        else
                            textofirma = textofirma + "Empresa: " + tlado + "\n";
                    }
                    
                    if (tabajo.length() > 0)
                        textofirma = textofirma + "Fecha: " + tabajo;
                }
                else
                    textofirma = " ";

                sap.setLayer2Font(font);
                sap.setLayer2Text(textofirma);
            }
            
            if (cerrarDocumento)
            {
                if (sap == null)
                {
                    stp = new PdfStamper(reader, fout, '\0', true);
                    sap = stp.getSignatureAppearance();
                }
                
                sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
            }
            
            Date theDate = new Date();
            TSAClient tsa = null;

            if (conTSA)
                tsa = new TSAClientBouncyCastle(TSA_URL, TSA_ACCNT, TSA_PASSW, 4096, frmFirmador.configuracion.getVg_algoritmo());            
            
            ExternalSignature es = new PrivateKeySignature(key, frmFirmador.configuracion.getVg_algoritmo(), proveedor);
            ExternalDigest digest = new BouncyCastleDigest();

            // https://stackoverflow.com/questions/23329392/race-condition-in-java-8-pkcs11-keystore
            if (!proveedor.equals("SunMSCAPI") && !proveedor.equals("BC"))
                Thread.sleep(200);

            MakeSignature.signDetached(sap, digest, es, cadenacer, null, null, tsa, 4*8192, MakeSignature.CryptoStandard.CMS);
            
            stp.close();
            raf.close();            
            reader.close();
            fout.close();
            sap = null;
            raf = null;
            stp = null;
            reader = null;
            fout = null;
            

            // obtengo la fecha y la hora del sello de tiempo
            if (conTSA)
            {
                raf = new RandomAccessFileOrArray(archivoDestino, false, true);
                reader = new PdfReader(raf, null);
                                
                AcroFields af = reader.getAcroFields();
                ArrayList names = af.getSignatureNames();
                PdfPKCS7 pk = null;

                for (int k = 0; k < names.size(); ++k) 
                {
                    String name = (String)names.get(k);
                    pk = af.verifySignature(name);
                    
                    TimeStampToken ts = pk.getTimeStampToken();

                    if(ts != null)
                    {
                        theDate = ts.getTimeStampInfo().getGenTime();
                        break;
                    }
                }

                raf.close();
                reader.close();
                reader = null;
                raf = null;
            }
            
            if (f_temp != null)
                f_temp.delete();
            
            Path newPath = Paths.get(newPdf);
            
            if (Files.exists(newPath))
                Files.delete(newPath);

            progreso.setValue(100);
            progreso.setString("Firmado");
            progreso.setStringPainted(true);
            progreso.setBackground(new Color(211, 111, 66));
            progreso.setForeground(new Color(211, 111, 66));
            
            progreso.setUI(new BasicProgressBarUI()
            {
                @Override
                protected Color getSelectionBackground()
                {
                    return null;
                }

                @Override
                protected Color getSelectionForeground()
                {
                    return Color.white;
                }
            });
            
            frmFirmador.dgvDocumentos.repaint();
            frmFirmador.dgvDocumentos.setValueAt("Ver firma", indice, 4);
            frmFirmador.dgvDocumentos.setValueAt(archivoDestino, indice, 5);
            frmFirmador.contarFirmados();
            return 0;
        }
        catch (DocumentException ex1)
        {
            frmFirmador.contarErrados();
            progreso.setString("Error 101 : " + ex1.getMessage());
            progreso.setStringPainted(true);
            progreso.setForeground(Color.red);
            progreso.setBackground(Color.red);
            
            progreso.setUI(new BasicProgressBarUI()
            {
                @Override
                protected Color getSelectionBackground() {
                    return null;
                }

                @Override
                protected Color getSelectionForeground() {
                    return Color.white;
                }
            });
            frmFirmador.dgvDocumentos.repaint();
            frmFirmador.dgvDocumentos.setValueAt("Eliminar", indice, 4);

            return 101;
        }
        catch (IOException ex0)
        {
            frmFirmador.contarErrados();
            progreso.setString("Error 100 : " + ex0.getMessage());
            progreso.setStringPainted(true);
            progreso.setForeground(Color.white);
            progreso.setBackground(Color.red);
            progreso.setUI(new BasicProgressBarUI() {
                @Override
                protected Color getSelectionBackground() {
                    return null;
                }

                @Override
                protected Color getSelectionForeground() {
                    return Color.white;
                }
            });
            frmFirmador.dgvDocumentos.repaint();
            return 100;
        }
        catch (Exception ex0)
        {
            frmFirmador.contarErrados();
            progreso.setString("Error 102 : " + ex0.getMessage());
            progreso.setStringPainted(true);
            progreso.setForeground(Color.red);
            progreso.setBackground(Color.red);
            progreso.setUI(new BasicProgressBarUI() {
                @Override
                protected Color getSelectionBackground() {
                    return null;
                }

                @Override
                protected Color getSelectionForeground() {
                    return Color.white;
                }
            });
            frmFirmador.dgvDocumentos.repaint();
            frmFirmador.dgvDocumentos.setValueAt("Eliminar", indice, 4);
            return 102;
        }
        catch (Error e)
        {
            frmFirmador.contarErrados();
            progreso.setString("Error 200 : " + e.getMessage());
            progreso.setStringPainted(true);
            progreso.setForeground(Color.red);
            progreso.setBackground(Color.red);
            
            progreso.setUI(new BasicProgressBarUI() {
                @Override
                protected Color getSelectionBackground() {
                    return null;
                }

                @Override
                protected Color getSelectionForeground() {
                    return Color.white;
                }
            });
            frmFirmador.dgvDocumentos.repaint();
            frmFirmador.dgvDocumentos.setValueAt("Eliminar", indice, 4);

            return 200;
        }
    }

    public Map<String, String> getXParametros()
    {
        return xparametros;
    }

}
