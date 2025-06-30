package Entidades;

import Clases.Repositorio;
import static Formularios.frmConfigurador.btnValidarCertificado;
import static Formularios.frmConfigurador.configuracion;
import static Formularios.frmConfigurador.repositorios;
import Clases.Certificado;
import Formularios.frmConfigurador;
import Global.CConstantes;
import Global.CLog;
import Metodos.ValidarSelloTiempo;
import Modulos.frmFirmador;
import static Modulos.frmFirmador.configuracion;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CrlClient;
import com.itextpdf.text.pdf.security.CrlClientOnline;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.LtvVerification;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.OcspClient;
import com.itextpdf.text.pdf.security.OcspClientBouncyCastle;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.TSAClientBouncyCastle;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.Math.abs;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.bouncycastle.tsp.TimeStampToken;

public class Firmador
{
    float porcentajex;
    float porcentajey;
    float porcentajex1;
    float porcentajey1;
     
    private int baseancho;
    private int basealto;
    
    private boolean conNumeroCal;
    private boolean conCampoArea = true;
    private boolean conCampoEmpresa = true;
    private String posiciones=""; 
    private String nro_pagina;
    private String url_tsa="";
    private String usuario_tsa="";
    private String clave_tsa="";
    private String carpeta_salida="";
    private int alto;
    private int ancho;
    private int x;
    private int y;
    private byte[] bytesdocumentopdf;
    private List<byte[]> listabytesdocumentopdf;
    private String ruta_imagen="";
    private String titulo_firma="";
    private String tamano_letra;
    private boolean varias_firmas;
    private boolean firma_visible;
    private boolean firma_visible_solo_texto ;
    private boolean firma_visible_solo_imagen ;
    private boolean firma_visible_todas_paginas ;
    private boolean firma_visible_imagen_texto ;
    private boolean firma_visible_imagen_texto_lado ;
    private String c_nombre="";
    private String c_titulo="";
    private String c_empresa="";
    private String c_locacion="";
    private String c_motivo="";
    private String c_otros="";
    private String c_area = "";
    private String textoJuntoFirma = "";
    private String tipo_resumen="SHA-1";
    private String textoFirmador = "Firmado digitalmente por:";
    private JLabel contador;
    private static List<String> listarutas=new ArrayList<>();
    private String doc_carpeta_salida_ruta="";
    
    public Firmador(){}

    public Firmador(String documentopdf) {}

    public Firmador(byte[] bytesdocumentopdf) {
        this.bytesdocumentopdf = bytesdocumentopdf;
    }

    public Firmador(URI URIdocumentopdf) {}
    
    public Firmador(File rutapdf) {}

    public boolean getConCampoEmpresa()
    {
        return conCampoEmpresa;
    }

    public void setConCampoEmpresa(boolean conCampoEmpresa)
    {
        this.conCampoEmpresa = conCampoEmpresa;
    }

    public boolean getConCampoArea()
    {
        return conCampoArea;
    }

    public void setConCampoArea(boolean conCampoArea)
    {
        this.conCampoArea = conCampoArea;
    }
    
    public boolean getConNumeroCal()
    {
        return conNumeroCal;
    }

    public void setConNumeroCal(boolean conNumeroCal)
    {
        this.conNumeroCal = conNumeroCal;
    } 

    public byte[] getBytesdocumentopdf() {
        return bytesdocumentopdf;
    }

    public void setBytesdocumentopdf(byte[] bytesdocumentopdf) {
        this.bytesdocumentopdf = bytesdocumentopdf;
    }
    
    public  List<String> getListarutas() {
        return listarutas;
    }

    public  void setListarutas(List<String> aListarutas) {
        listarutas = aListarutas;
    }
    
    public String getUrl_tsa() {
        return url_tsa;
    }

    public void setUrl_tsa(String url_tsa) {
        this.url_tsa = url_tsa;
    }

    public String getUsuario_tsa() {
        return usuario_tsa;
    }

    public void setUsuario_tsa(String usuario_tsa) {
        this.usuario_tsa = usuario_tsa;
    }

    public String getPassword_tsa() {
        return clave_tsa;
    }

    public void setPassword_tsa(String clave_tsa) {
        this.clave_tsa = clave_tsa;
    }

    public String getNro_pagina() {
        return nro_pagina;
    }

    public void setNro_pagina(String nro_pagina) {
        this.nro_pagina = nro_pagina;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    public String getTitulo_firma() {
        return titulo_firma;
    }

    public void setTitulo_firma(String titulo_firma) {
        this.titulo_firma = titulo_firma;
    }

    public String getTamano_letra() {
        return tamano_letra;
    }

    public void setTamano_letra(String tamano_letra) {
        this.tamano_letra = tamano_letra;
    }

    public boolean isVarias_firmas() {
        return varias_firmas;
    }

    public void setVarias_firmas(boolean varias_firmas) {
        this.varias_firmas = varias_firmas;
    }

    public boolean isFirma_visible() {
        return firma_visible;
    }

    public void setFirma_visible(boolean firma_visible) {
        this.firma_visible = firma_visible;
    }

    public boolean isFirma_visible_solo_imagen() {
        return firma_visible_solo_imagen;
    }

    public void setFirma_visible_solo_imagen(boolean firma_visible_solo_imagen) {
        this.firma_visible_solo_imagen = firma_visible_solo_imagen;
    }

    public boolean isFirma_visible_solo_texto() {
        return firma_visible_solo_texto;
    }

    public void setFirma_visible_solo_texto(boolean firma_visible_solo_texto) {
        this.firma_visible_solo_texto = firma_visible_solo_texto;
    }
    
    public boolean isFirma_visible_imagen_texto() {
        return firma_visible_imagen_texto;
    }

    public void setFirma_visible_imagen_texto(boolean firma_visible_imagen_texto) {
        this.firma_visible_imagen_texto = firma_visible_imagen_texto;
    }
    
    public void setFirma_visible_imagen_texto_lado(boolean firma_visible_imagen_texto_lado)
    {
        this.firma_visible_imagen_texto_lado = firma_visible_imagen_texto_lado;
    }

    public String getC_titulo() {
        return c_titulo;
    }

    public void setC_titulo(String c_titulo) {
        this.c_titulo = c_titulo;
    }

    public String getC_nombre() {
        return c_nombre;
    }

    public void setC_nombre(String c_nombre) {
        this.c_nombre = c_nombre;
    }

    public String getC_empresa() {
        return c_empresa;
    }

    public void setC_empresa(String c_empresa) {
        this.c_empresa = c_empresa;
    }

    public String getC_locacion() {
        return c_locacion;
    }

    public void setC_locacion(String c_locacion) {
        this.c_locacion = c_locacion;
    }

    public String getC_motivo() {
        return c_motivo;
    }

    public void setC_motivo(String c_motivo) {
        this.c_motivo = c_motivo;
    }

    public String getC_otros() {
        return c_otros;
    }

    public void setC_otros(String c_otros) {
        this.c_otros = c_otros;
    }
    
    public String getC_area()
    {
        return c_area;
    }

    public void setC_area(String c_area)
    {
        this.c_area = c_area;
    }
    
    public String getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(String posiciones) {
        this.posiciones = posiciones;
    }

    public boolean isFirma_visible_todas_paginas() {
        return firma_visible_todas_paginas;
    }

    public void setFirma_visible_todas_paginas(boolean firma_visible_todas_paginas) {
        this.firma_visible_todas_paginas = firma_visible_todas_paginas;
    }

    public String getTextoFirmador()
    {
        return textoFirmador;
    }

    public void setTextoFirmador(String textoFirmador)
    {
        this.textoFirmador = textoFirmador;
    }
    
    public List<byte[]> getListabytesdocumentopdf() {
        return listabytesdocumentopdf;
    }

    public JLabel getContador() {
        return contador;
    }

    public void setContador(JLabel contador) {
        this.contador = contador;
    }

    public int getBaseancho() {
        return baseancho;
    }

    public void setBaseancho(int baseancho) {
        this.baseancho = baseancho;
    }

    public int getBasealto() {
        return basealto;
    }

    public void setBasealto(int basealto) {
        this.basealto = basealto;
    }

    public String getDoc_carpeta_salida_ruta() {
        return doc_carpeta_salida_ruta;
    }
    
    public String getCarpeta_salida() {
        return carpeta_salida;
    }

    public void setCarpeta_salida(String carpeta_salida) {
        if (carpeta_salida.charAt(carpeta_salida.length() - 1) != File.separatorChar)
            carpeta_salida += File.separator;
        this.carpeta_salida = carpeta_salida;
    }

    public void setTipoResumen(String tipo_resumen) {
        this.tipo_resumen = tipo_resumen;
    }

    /**
     * Método para firmar el PDF (se incluye la inserción condicional de página en blanco y QR).
     */
    public String firmar(Repositorio repositorios)
    {
        return firmar(repositorios, new JTextArea());
    }

    /**
     * Método principal que, antes de firmar cada PDF:
     * - Si ConCodQR = true => 1) Agrega página en blanco al final y 2) Inserta QR
     * - Luego firma el PDF (o firma el PDF original si ConCodQR = false).
     */
    public String firmar(Repositorio repositorios, JTextArea respuesta)
    {
        final String TSA_URL = url_tsa;
        final String TSA_ACCNT = usuario_tsa;
        final String TSA_PASSW = clave_tsa;
        final boolean conTSA = (TSA_URL.length() != 0);
        String TextoJuntoFirma = frmFirmador.configuracion.getTextoFirma1();      
        boolean ConFechaHora = frmFirmador.configuracion.getConCarmpoAreaFH();
        boolean ConCodQR = frmFirmador.configuracion.getConQR();

        tipo_resumen = frmFirmador.configuracion.getVg_algoritmo();
        Iterator iterator = listarutas.iterator();
        PdfSignatureAppearance sap = null;
        PdfReader readerDocumento = null;

        FileOutputStream salidaArchivoFirmado = null;
        PdfStamper stp = null;
        OutputStream salidaTemporalDocumento = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Declaramos el raf fuera del try para cerrar adecuadamente
        RandomAccessFileOrArray raf = null;
        
        while (iterator.hasNext())
        {
            String rutaTemporalDocumento = null;
            String tempPDFParaEditar = null;
            
            try
            {
                String rutaDocumento = (String) iterator.next();
                
                // 1) Revisamos si se debe insertar QR y página en blanco
                if(ConCodQR) {
                    respuesta.setText("Agregando página en blanco + QR al PDF [" + rutaDocumento + "]");
                    
                    raf = new RandomAccessFileOrArray(rutaDocumento, false, true);
                    readerDocumento = new PdfReader(raf, null);
                    int numeroDePaginas = readerDocumento.getNumberOfPages();

                    // PDF temporal donde haremos la edición (insertar página + QR)
                    tempPDFParaEditar = carpeta_salida + "temp_edit_" + System.currentTimeMillis() + ".pdf";
                    
                    // Creamos stamper para editar el PDF (modo append)
                    PdfStamper stamperEdicion = new PdfStamper(readerDocumento, new FileOutputStream(tempPDFParaEditar), '\0', true);

                    // Insertamos una página en blanco al final
                    Rectangle ultimaPaginaRect = readerDocumento.getPageSize(numeroDePaginas);
                    stamperEdicion.insertPage(numeroDePaginas + 1, ultimaPaginaRect);

                    // Generamos el contenido para la nueva página
                    PdfContentByte cb = stamperEdicion.getOverContent(numeroDePaginas + 1);

                    // Generamos el código QR
                    String urlQR = "https://unw.plussigner.com/unw/verify/?code=16626";
                    BarcodeQRCode barcodeQRCode = new BarcodeQRCode(urlQR, 150, 150, null);
                    Image qrImage = barcodeQRCode.getImage();

                    // Posición en la esquina superior izquierda
                    float xPos = 36f; 
                    float yPos = ultimaPaginaRect.getHeight() - 36f - qrImage.getScaledHeight();
                    qrImage.setAbsolutePosition(xPos, yPos);

                    // Agregamos la imagen al contenido
                    cb.addImage(qrImage);

                    // Cerramos el stamper de edición
                    stamperEdicion.close();
                    // En este caso, ya no usaremos "raf" ni "readerDocumento" porque abriremos "tempPDFParaEditar"
                    // Por lo tanto, podemos cerrarlos sin problema
                    readerDocumento.close();
                    raf.close();
                    raf = null;  // para que no intente cerrarse de nuevo en el finally

                    // Usaremos el PDF "tempPDFParaEditar" para la firma
                    readerDocumento = new PdfReader(tempPDFParaEditar);
                }
                else {
                    // NO insertamos QR ni página en blanco: usamos el PDF original
                    respuesta.setText("ConCodQR = false. Usando el PDF original sin modificación [" + rutaDocumento + "]");
                    raf = new RandomAccessFileOrArray(rutaDocumento, false, true);
                    readerDocumento = new PdfReader(raf, null);
                    // NO cerramos raf aquí -> iText sigue usando este stream
                    // tempPDFParaEditar se queda null, o no se utiliza.
                }

                // 2) Ahora procede la firma con la lógica existente
                int numeroDePaginasNew = readerDocumento.getNumberOfPages();

                doc_carpeta_salida_ruta = carpeta_salida +
                        (!frmFirmador.configuracion.isVg_documento_extension_inicio()
                                ? new File(rutaDocumento).getName().substring(0, new File(rutaDocumento).getName().lastIndexOf(".")) 
                                  + frmFirmador.configuracion.getVg_documento_extension_salida() + ".pdf"
                                : frmFirmador.configuracion.getVg_documento_extension_salida() + new File(rutaDocumento).getName());

                rutaTemporalDocumento = doc_carpeta_salida_ruta + ".ssg";

                respuesta.setText("Creando archivo temporal para firma: " + rutaTemporalDocumento);
                salidaTemporalDocumento = new FileOutputStream(rutaTemporalDocumento);

                PdfStamper stpTodasLasHojas = null;
                
                if(firma_visible && firma_visible_todas_paginas)
                {
                    respuesta.setText("Generando temporal con firma visible en todas las páginas: " + rutaTemporalDocumento);
                    stpTodasLasHojas = new PdfStamper(readerDocumento, salidaTemporalDocumento, '\0', true);
                }

                int pagfirma = CConstantes.Yconvertiranumero(nro_pagina);

                if(readerDocumento.getCertificationLevel() == PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED)
                    throw new Exception("El documento está cerrado. [" + rutaDocumento + "]");

                boolean cerrardocumento = varias_firmas;
                int posx = 0;
                int posy = 0;
                int posx2 = 0;
                int posy2 = 0;
                String firmaposterior = null;

                // ============ FIRMAS VISIBLES: cálculo de posición, etc. ============
                if (firma_visible)
                {
                    if (pagfirma < 1 || pagfirma > numeroDePaginasNew) {
                        pagfirma = numeroDePaginasNew;
                    }

                    Rectangle area = readerDocumento.getPageSize(pagfirma);
                    String posicionrelativa = posiciones; 

                    int tamx;
                    int tamy;
                    int talto = alto;
                    int tancho = ancho;

                    String etiqueta = frmFirmador.configuracion.getEtiqueta_firma();
                    
                    // Si hay "etiqueta" para ubicar firma
                    if (!etiqueta.isEmpty())
                    {
                        List<float[]> lasCoordenadas = new ArrayList<>();
                        // Este helperClass es tu lógica que extrae coordenadas
                        lasCoordenadas = new helperClass().getCoordenadas(
                                Files.readAllBytes(Paths.get(rutaDocumento)), 
                                rutaDocumento, 
                                null, 
                                frmFirmador.configuracion.getEtiqueta_firma()
                        );

                        if(lasCoordenadas != null && lasCoordenadas.size() > 0 && lasCoordenadas.get(0).length > 2)
                        {
                            pagfirma = (int)lasCoordenadas.get(0)[0];

                            if (pagfirma < 1 || pagfirma > numeroDePaginasNew)
                                pagfirma = numeroDePaginasNew;

                            posx =  (int)lasCoordenadas.get(0)[1];
                            posy =  (int)lasCoordenadas.get(0)[2];
                            posx2 =  (int)frmFirmador.configuracion.getVg_firma_visible_x1();
                            posy2 =  (int)frmFirmador.configuracion.getVg_firma_visible_y1();

                            posx2 = posx + Math.abs(posx2 - (int)frmFirmador.configuracion.getVg_firma_visible_x());
                            posy2 = posy + Math.abs(posy2 - (int)frmFirmador.configuracion.getVg_firma_visible_y());
                        }
                        else
                        {
                            etiqueta = "";
                        }
                    }

                    // Si no hay etiqueta, se usan posiciones definidas
                    if (etiqueta.isEmpty())
                    {
                        if (posicionrelativa.length() > 0)
                        {
                            String[] nroposiciones = posicionrelativa.split(",");
                            if (nroposiciones.length == 6)
                            {
                                int xx1 = Integer.parseInt(nroposiciones[0]);
                                int yy1 = Integer.parseInt(nroposiciones[1]);
                                int xx2 = Integer.parseInt(nroposiciones[2]);
                                int yy2 = Integer.parseInt(nroposiciones[3]);
                                tamx = Integer.parseInt(nroposiciones[4]);
                                tamy = Integer.parseInt(nroposiciones[5]);

                                posx = Math.round(xx1 * area.getWidth()/ tamx);
                                posx2 = Math.round(xx2 * area.getWidth()/ tamx);

                                posy = Math.round(area.getHeight() - yy1 * area.getHeight() / tamy);
                                posy2 = Math.round(area.getHeight() - yy2 * area.getHeight() / tamy);

                                int wid = (int)(Math.abs(xx2 - xx1) * area.getHeight() / tamy);
                                int wid2 = Math.abs(posx2 - posx);

                                posx += (int)((wid2 - wid) / 2);
                                posx2 -= (int)((wid2 - wid) / 2);

                                if(area.getWidth() < area.getHeight() && posx > area.getWidth() / 2)
                                {
                                    posx2 -= Math.abs((wid - wid2) / 2);
                                    posx -= Math.abs((wid - wid2) / 2);
                                }
                                if(area.getWidth() > area.getHeight() && posx > area.getWidth() / 2)
                                {
                                    posx2 += Math.abs((wid - wid2) / 2);
                                    posx += Math.abs((wid - wid2) / 2);
                                }
                                if(area.getWidth() < area.getHeight() && posx < area.getWidth() / 2)
                                {
                                    posx2 += Math.abs((wid - wid2) / 2);
                                    posx += Math.abs((wid - wid2) / 2);
                                }
                                if(area.getWidth() > area.getHeight() && posx < area.getWidth() / 2)
                                {
                                    posx2 -= Math.abs((wid - wid2) / 2);
                                    posx -= Math.abs((wid - wid2) / 2);
                                }
                            }
                            else
                            {
                                int xx1 = x;
                                int yy1 = y;
                                int xx2 = xx1 + tancho;
                                int yy2 = yy1 + talto;

                                tamx = Integer.parseInt(nroposiciones[0]);
                                tamy = Integer.parseInt(nroposiciones[1]);

                                posx = Math.round(xx1 * area.getWidth() / tamx);
                                posx2 = Math.round(xx2 * area.getWidth() / tamx);

                                posy = Math.round(area.getHeight() - yy1 * area.getHeight() / tamy);
                                posy2 = Math.round(area.getHeight() - yy2 * area.getHeight() / tamy);
                            }
                        }
                        else
                        {
                            posx =  CConstantes.Yconvertiranumero(""+x);
                            posy =  CConstantes.Yconvertiranumero(""+y);
                            posx2 = CConstantes.Yconvertiranumero(""+ancho);
                            posy2 = CConstantes.Yconvertiranumero(""+alto);
                        }
                    }

                    // Firma visible en todas las páginas
                    if(firma_visible_todas_paginas && stpTodasLasHojas != null)
                    {
                        AcroFields acroFields = stpTodasLasHojas.getAcroFields();
                        List listaacrofieldsfirmado = acroFields.getSignatureNames();
                        PdfFormField sig = PdfFormField.createSignature(stpTodasLasHojas.getWriter());
                        sig.setWidget(new Rectangle(posx, posy, posx2, posy2), null);
                        sig.setFlags(PdfAnnotation.FLAGS_PRINT);
                        sig.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g"));
                        String firmaprevia = null;

                        if(listaacrofieldsfirmado.size() > 0)
                        {
                            firmaprevia = "Signature"+listaacrofieldsfirmado.size();
                            for (Object listaacrofieldsfirmado1 : listaacrofieldsfirmado)
                            {
                                String cajatexto = (String) listaacrofieldsfirmado1;
                                if(firmaprevia.equals(cajatexto))
                                    firmaprevia = "Signature"+ (Integer.parseInt(cajatexto.substring(cajatexto.lastIndexOf("e")+1,cajatexto.length()))+1);
                            }
                        }

                        if(firmaprevia != null)
                            firmaposterior = firmaprevia;
                        else
                            firmaposterior="Signature1";

                        sig.setFieldName(firmaposterior);
                        sig.setPage(1);

                        for(int i = 1; i <= numeroDePaginasNew; i++)
                            stpTodasLasHojas.addAnnotation(sig, i);

                        stpTodasLasHojas.close();
                        salidaTemporalDocumento.flush();
                        salidaTemporalDocumento.close();

                        respuesta.setText("Leyendo temporal con firma visible en todas las páginas. [" + rutaTemporalDocumento + "]");
                        readerDocumento = new PdfReader(rutaTemporalDocumento);
                    }
                }

                // 3) Creamos la firma final
                salidaArchivoFirmado = new FileOutputStream(doc_carpeta_salida_ruta);
                stp = PdfStamper.createSignature(readerDocumento, salidaArchivoFirmado, '\0', new File(doc_carpeta_salida_ruta + ".tmp"), true);
                sap = stp.getSignatureAppearance();

                if(cerrardocumento)
                    sap.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);

                sap.setLocation(c_locacion);
                sap.setReason(c_motivo);

                String textofirma = "";

                if(firma_visible)
                {
                    File fImg = new File(ruta_imagen);
                    if(fImg.isFile() && !firma_visible_solo_texto)
                    {
                        Image watermark = Image.getInstance(fImg.getAbsolutePath());
                        if(firma_visible_imagen_texto)
                        {
                            if(firma_visible_imagen_texto_lado)
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
                            sap.setImage(watermark);
                        }
                    }
                    else
                    {
                        BufferedImage watermark = new BufferedImage(abs(posx2 - posx), abs(posy2 - posy), BufferedImage.TRANSLUCENT);
                        Graphics2D g2d = watermark.createGraphics();
                        g2d.setComposite(AlphaComposite.Clear);
                        g2d.setColor(Color.WHITE);
                        g2d.drawRect(0, 0, posx2 - posx, posy2 - posy);
                        sap.setImage(Image.getInstance(watermark, null));
                    }

                    if(!firma_visible_solo_imagen)
                    {
                        String c_nombr0;
                        if (!this.conNumeroCal && c_nombre.toLowerCase().contains(" cal n"))
                        {
                            String cn0 = c_nombre.toLowerCase();
                            int ind = cn0.indexOf(" cal n");
                            c_nombr0 = c_nombre.substring(0, ind);
                        }
                        else
                        {
                            c_nombr0 = c_nombre;
                        }

                        textofirma += "Firmado digitalmente por: " + c_nombr0 + "\n";

                        if (!TextoJuntoFirma.isEmpty()) {
                            textofirma = textofirma + TextoJuntoFirma + "\n";
                        }

                        if(c_titulo.length() > 0){
                            if (this.conCampoArea)
                                textofirma = textofirma + "Cargo: " + c_titulo + "\n";
                        }

                        if(c_empresa.length() > 0)
                        {
                            if (this.conCampoEmpresa)
                                textofirma = textofirma + "Empresa: " + c_empresa + "\n";
                        }

                        if(c_locacion.length() > 0)
                            textofirma = textofirma +  "Lugar: " + c_locacion + "\n";

                        if(c_motivo.length() > 0)
                            textofirma = textofirma +  "Motivo : " + c_motivo + "\n";

                        // Etiqueta "Fecha/Hora" si corresponde
                        if (ConFechaHora)
                            textofirma = textofirma + "Fecha/Hora: " + CConstantes.formatearFechaConHora();
                    }
                    else
                    {
                        textofirma = " ";
                    }

                    sap.setLayer2Text(textofirma);
                    sap.setAcro6Layers(true);

                    if(firma_visible_todas_paginas)
                        sap.setVisibleSignature(firmaposterior);
                    else
                        sap.setVisibleSignature(new Rectangle(posx, posy, posx2, posy2), pagfirma, null);
                }

                // =====================
                // FIRMAMOS
                // =====================
                Date theDate = new Date();
                TSAClient tsa = null;

                if (conTSA)
                    tsa = new TSAClientBouncyCastle(TSA_URL, TSA_ACCNT, TSA_PASSW, 50000, tipo_resumen);

                String rep = repositorios.getProveedor().getName();
                if(repositorios.getNombreAlmacen().equals(Repositorio.ARCHIVO))
                    rep = "BC";

                ExternalSignature es = new PrivateKeySignature(repositorios.getFirmanteElegido().getCertificado().getLlavePrivada(), tipo_resumen, rep);
                ExternalDigest digest = new BouncyCastleDigest();

                // Evitar race-condition en algunos providers
                if (!rep.equals("SunMSCAPI") && !rep.equals("BC"))
                    Thread.sleep(200);

                MakeSignature.signDetached(sap, digest, es, 
                    repositorios.getFirmanteElegido().getCertificado().getCertificadosConfianza(), 
                    null, null, tsa, 50000, MakeSignature.CryptoStandard.CMS);

                stp.close();
                stp = null;
                sap = null;
                
                new CLog().archivar_log("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | Firma realizada exitosamente", true);
                // Cierro y limpio
                // Leer de nuevo para obtener la fecha y hora del sello de tiempo si existiera
                if (conTSA)
                {
                    RandomAccessFileOrArray raf2 = new RandomAccessFileOrArray(rutaDocumento, false, true);
                    PdfReader reader = new PdfReader(raf2, null);
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
                    reader.close();
                    raf2.close();
                    reader = null;
                    raf2 = null;
                    
                    new CLog().archivar_log("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date())
                                + " | Sello de tiempo validado correctamente: ["+TSA_URL + "], con el valor de usuario:["+TSA_ACCNT + "]", true);
                }

                if(readerDocumento != null)
                    readerDocumento.close();
                
                if(salidaArchivoFirmado != null)
                {
                    salidaArchivoFirmado.flush();
                    salidaArchivoFirmado.close();
                }
                respuesta.setText("Documento Firmado [" + doc_carpeta_salida_ruta + "]");

            }
            catch (IOException | DocumentException | NoSuchAlgorithmException | SignatureException ex)
            {
                respuesta.setText(ex.toString());
                new CLog().archivar_log(ex.getMessage());
                return CConstantes.X_ERROR;
            }
            catch (Exception ex) {
                System.out.println("url: " + TSA_URL);
                System.out.println("user: " + TSA_ACCNT);
                System.out.println("pass: " + TSA_PASSW);

                // Si conTSA es true, chequear servicio TSA.
                if (conTSA) {
                    
                    try {
                        String resultadoValidacion = new ValidarSelloTiempo().validarServicioSelloDeTiempo(TSA_URL, TSA_ACCNT, TSA_PASSW);
                        new CLog().archivar_log("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | Resultado de la validación de sello de tiempo: " + resultadoValidacion, true);
                        respuesta.setText("Ocurrió un error con el sello de tiempo: " + resultadoValidacion);
                    } catch (IOException ioEx) {
                        new CLog().archivar_log("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | Error durante la validación del sello de tiempo: " + ioEx.getMessage(), true);
                        respuesta.setText("Error validando sello de tiempo: " + ioEx.getMessage());
                    }
                }
                // Si conTSA es false, el error no tiene que ver con TSA.
                else {
                    respuesta.setText("Ocurrió un error (no es TSA): " + ex.getMessage());
                }

                return CConstantes.X_ERROR;
            }
            finally
            {
                try
                {
                    if(readerDocumento != null) {
                        readerDocumento.close();
                    }

                    if(salidaArchivoFirmado != null) {
                        salidaArchivoFirmado.close();
                    }

                    if(salidaTemporalDocumento != null) {
                        salidaTemporalDocumento.close();
                    }

                    // Cerrar raf si quedó abierto
                    if(raf != null) {
                        raf.close();
                        raf = null;
                    }

                    // Borramos el PDF temporal (con la página y QR) si se generó
                    if (tempPDFParaEditar != null) {
                        File tempFile = new File(tempPDFParaEditar);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }

                    // Borramos el .ssg
                    if (rutaTemporalDocumento != null) {
                        File ssgFile = new File(rutaTemporalDocumento);
                        if (ssgFile.exists()) {
                            ssgFile.delete();
                        }
                    }

                } 
                catch (IOException ex) 
                {
                    // Manejo de excepción
                }
            }
        }
        return CConstantes.X_OK;
    }
}
