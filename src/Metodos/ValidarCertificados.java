package Metodos;

import Global.CConstantes;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import Clases.Repositorio;
import Formularios.frmConfigurador;
import Global.CLog;

public class ValidarCertificados
{
    static CConstantes xconstantes = new CConstantes();
    private static String MensajeDeError;
    private static String CodigoDeError;

    public static String filtrarCertificado(Certificate[] cadenacertificacion, Date fechahoy,
                                            String rutatsl, String pathalmacencrl,
                                            String urlProxy, String puertoProxy,
                                            String NoRepudio) throws Exception
    {
        MensajeDeError = "";
        CodigoDeError = "0";

        // Para tu archivo de log
        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String userHome = System.getProperty("user.name");
        String logDirectory = "C:\\Users\\" + userHome;
        String logFileName = logDirectory + "\\" + "SSigner Configurador_" + logDateFormat.format(new Date()) + ".log";

        try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true)))
        {
            if (cadenacertificacion.length > 0)
            {
                GrabarURL guardar_url = new GrabarURL();

                // Recorremos toda la cadena de certificados
                for (int i = 0; i < cadenacertificacion.length; i++)
                {
                    X509Certificate certificadocad;

                    // Generar X509Certificate
                    try (ByteArrayInputStream bisb = new ByteArrayInputStream(cadenacertificacion[i].getEncoded()))
                    {
                        CertificateFactory cfb = CertificateFactory.getInstance("X.509");
                        certificadocad = (X509Certificate)cfb.generateCertificate(bisb);
                        String nombreSujeto = certificadocad.getSubjectX500Principal().getName();

                        // (NUEVO) Verificar que no contenga "Adult" o "xCRL" en el subject
                        if (nombreSujeto.contains("Adult"))
                        {
                            logWriter.println("--------------------------------------------------------------------------");
                            CodigoDeError += xconstantes.X_CERTIFICADO_ADULTERADO;
                            MensajeDeError += "El certificado [" + Repositorio.getCN(certificadocad) + "] contiene 'Adult' en el subject, lo que es inválido para la firma digital." 
                                              + xconstantes.RETORNO;
                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                    + CConstantes.X_INFORMACION + "|Certificado con 'Adult' en Subject["
                                                    + Repositorio.getCN(certificadocad) + "]|" + MensajeDeError);
                            break;
                        }
                        else if (nombreSujeto.contains("xCRL"))
                        {
                            logWriter.println("--------------------------------------------------------------------------");
                            CodigoDeError += xconstantes.X_CERTIFICADO_CRL_ERRADA;
                            MensajeDeError += "El certificado [" + Repositorio.getCN(certificadocad) + "] contiene 'xCRL' en el subject, lo que es inválido para la firma digital."
                                              + xconstantes.RETORNO;
                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                    + CConstantes.X_INFORMACION + "|Certificado con 'xCRL' en Subject["
                                                    + Repositorio.getCN(certificadocad) + "]|" + MensajeDeError);
                            break;
                        }
                    }

                    // ------------------------------------------------------------------
                    // (NUEVO) REQUERIMIENTO 3: Verificar KeyUsage (propósito de firma)
                    // ------------------------------------------------------------------
                    // Se hace en el primer certificado (i=0), normalmente el del titular/firmante
                    if (i == 0)
                    {
                        List<String> usoscert = getKeyUsage(certificadocad);
                        if (usoscert != null)
                        {
                            // Debe tener al menos digitalSignature o nonRepudiation
                            if (!(usoscert.contains("digitalSignature") || usoscert.contains("nonRepudiation")))
                            {
                                logWriter.println("--------------------------------------------------------------------------");
                                CodigoDeError += xconstantes.X_CERTIFICADO_MARCADEUSOINVALIDA;
                                MensajeDeError += "Certificado [" + Repositorio.getCN(certificadocad) + "] no tiene los usos válidos para firma digital. Usos: " 
                                                  + String.join(", ", usoscert) + ". Únicos usos válidos: digitalSignature y/o nonRepudiation." + xconstantes.RETORNO;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_INFORMACION + "|Certificado con usos inválidos para firma["
                                                        + Repositorio.getCN(certificadocad) + "]|" + MensajeDeError);
                                break;
                            }

                            // Si "NoRepudio" está activo, exigir "nonRepudiation"
                            if (NoRepudio.equals(CConstantes.X_TRUE))
                            {
                                if (!usoscert.contains("nonRepudiation"))
                                {
                                    logWriter.println("--------------------------------------------------------------------------");
                                    CodigoDeError += xconstantes.X_CERTIFICADO_NOESNOREPUDIO;
                                    MensajeDeError += "El certificado [" + Repositorio.getCN(certificadocad) + "] no tiene 'nonRepudiation' en los usos, aunque NoRepudio está activado."
                                                      + xconstantes.RETORNO;
                                    new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                            + CConstantes.X_ERROR + "|NoRepudio exigido pero no presente|El Certificado["
                                                            + Repositorio.getCN(certificadocad) + "]|" + MensajeDeError);
                                    break;
                                }
                            }

                            // (NUEVO) Rechazar si hay usos NO permitidos en la firma digital
                            String[] usosNoPermitidos = {
                                "keyEncipherment", "dataEncipherment", "keyAgreement",
                                "keyCertSign", "cRLSign", "encipherOnly", "decipherOnly"
                            };
                            boolean contieneNoPermitidos = false;
                            for (String uso : usoscert)
                            {
                                if (Arrays.asList(usosNoPermitidos).contains(uso))
                                {
                                    contieneNoPermitidos = true;
                                    break;
                                }
                            }
                            if (contieneNoPermitidos)
                            {
                                logWriter.println("--------------------------------------------------------------------------");
                                CodigoDeError += xconstantes.X_CERTIFICADO_MARCADEUSOINVALIDA;
                                MensajeDeError += "Certificado [" + Repositorio.getCN(certificadocad) + "] contiene usos no permitidos para firma digital. Usos: "
                                                  + String.join(", ", usoscert) + ". Únicos usos válidos: digitalSignature y/o nonRepudiation." + xconstantes.RETORNO;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_INFORMACION + "|Usos no permitidos|Certificado["
                                                        + Repositorio.getCN(certificadocad) + "]|" + MensajeDeError);
                                break;
                            }
                        }
                        else
                        {
                            // No tiene keyUsage
                            logWriter.println("--------------------------------------------------------------------------");
                            CodigoDeError += xconstantes.X_CERTIFICADO_MARCADEUSOINVALIDA;
                            MensajeDeError += "El certificado [" + Repositorio.getCN(certificadocad) + "] no registra ninguna marca KeyUsage." + xconstantes.RETORNO;
                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                    + CConstantes.X_ERROR + "|Sin KeyUsage|"
                                                    + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                            break;
                        }
                    }

                    // ------------------------------------------------------------------
                    // Verificar vigencia con checkValidity
                    // ------------------------------------------------------------------
                    try
                    {
                        certificadocad.checkValidity(fechahoy);
                    }
                    catch (CertificateNotYetValidException ex)
                    {
                        logWriter.println("--------------------------------------------------------------------------");
                        MensajeDeError += "El certificado [" + Repositorio.getCN(certificadocad) + "] está fuera del periodo válido (NotBefore: "
                                          + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(certificadocad.getNotBefore()) + ")."
                                          + xconstantes.RETORNO;
                        CodigoDeError += xconstantes.X_CERTIFICADO_FUERA_PERIODO;
                        new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                + CConstantes.X_ADVERTENCIA + "|Fuera de periodo|"
                                                + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                    }
                    catch (CertificateExpiredException ex2)
                    {
                        logWriter.println("--------------------------------------------------------------------------");
                        MensajeDeError += "El certificado [" + Repositorio.getCN(certificadocad) + "] ha expirado (NotAfter: "
                                          + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(certificadocad.getNotAfter()) + ")."
                                          + xconstantes.RETORNO;
                        CodigoDeError += xconstantes.X_CERTIFICADO_EXPIRADO;
                        new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                + CConstantes.X_ADVERTENCIA + "|Certificado Expirado|"
                                                + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                    }
                    
                    // ------------------------------------------------------------------
                    // Validación por CRL (descargas, vigencias, etc.)
                    // ------------------------------------------------------------------
                    List listacrl = getCRLs(certificadocad);
                    int itemscrl = 0;

                    for (Object listacrl1 : listacrl)
                    {
                        itemscrl++;
                        String rutacrl = guardar_url.ubicacion_cache(pathalmacencrl, listacrl1.toString());
                        File fcachecrl = new File(rutacrl);
                        boolean descargarcrl = true;
                        boolean ycrlvalida = false;
                        boolean yadescargocrl = false;

                        // Primero revisamos si ya hay CRL en cache y si está vigente
                        if (fcachecrl.isFile())
                        {
                            CertificateFactory cf2 = CertificateFactory.getInstance("X.509");
                            FileInputStream inStream2 = new FileInputStream(rutacrl);
                            try
                            {
                                X509CRL crltempo = (X509CRL)cf2.generateCRL(inStream2);
                                if (crltempo.getNextUpdate().after(fechahoy) && crltempo.getThisUpdate().before(fechahoy))
                                {
                                    descargarcrl = false;
                                    ycrlvalida = true;
                                    yadescargocrl = true;
                                }
                            }
                            catch (Exception ex)
{
                                logWriter.println("--------------------------------------------------------------------------");
                                // CRL corrupta, se elimina y forzamos la descarga
                                fcachecrl.delete();
                                MensajeDeError += xconstantes.Yget_mensaje_error(xconstantes.X_CRL_CORRUPTA)
                                                  + "." + xconstantes.RETORNO;
                                CodigoDeError += xconstantes.X_CRL_CORRUPTA;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_EXCEPCION + "|CRL corrupta|"
                                                        + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                            }
                        }

                        // Intentar descargar CRL si no está en cache o si no era válida
                        if (descargarcrl)
                        {
                            ycrlvalida = false; // Se asume no válida hasta comprobar
                            String trutacrlt = listacrl1.toString();
                            String tfilecrl = trutacrlt.substring(trutacrlt.lastIndexOf('/') + 1);

                            // Descarga desde repositorio local / cache
                            if (pathalmacencrl.length() > 0)
                            {
                                yadescargocrl = guardar_url.descargar(pathalmacencrl, listacrl1.toString(), urlProxy, puertoProxy);
                                if (yadescargocrl)
                                {
                                    CertificateFactory cf2 = CertificateFactory.getInstance("X.509");
                                    FileInputStream inStream2 = new FileInputStream(rutacrl);
                                    try
                                    {
                                        X509CRL crltempo = (X509CRL) cf2.generateCRL(inStream2);
                                        if (!(crltempo.getNextUpdate().after(fechahoy) && crltempo.getThisUpdate().before(fechahoy)))
                                        {
                                            MensajeDeError += "Fecha de CRL (cache server local) fuera de rango: "
                                                              + crltempo.getNextUpdate().toString() + "*"
                                                              + crltempo.getThisUpdate().toString() + xconstantes.RETORNO;
                                            CodigoDeError += xconstantes.X_CRL_FECHA_INCORRECTA;
                                            logWriter.println("--------------------------------------------------------------------------");
                                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                                    + CConstantes.X_ADVERTENCIA + "|CRL fuera de rango|"
                                                                    + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                                            ycrlvalida = false;
                                        }
                                        else
                                        {
                                            ycrlvalida = true;
                                        }
                                    }
                                    catch (Exception ex)
                                    {
                                        logWriter.println("--------------------------------------------------------------------------");
                                        MensajeDeError += "El formato de la CRL (cache local) no es válido: " + rutacrl + xconstantes.RETORNO;
                                        CodigoDeError += xconstantes.X_CRL_FECHA_INCORRECTA;
                                        new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                                + CConstantes.X_ADVERTENCIA + "|CRL no válida (local)|"
                                                                + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                                        fcachecrl.delete();
                                    }
                                }
                            }

                            // Si aún no es válida, se intenta descargar de internet
                            if (!ycrlvalida)
                            {
                                yadescargocrl = guardar_url.descargar(pathalmacencrl, listacrl1.toString(), urlProxy, puertoProxy);
                                if (yadescargocrl)
                                {
                                    CertificateFactory cf2 = CertificateFactory.getInstance("X.509");
                                    FileInputStream inStream2 = new FileInputStream(rutacrl);
                                    try
                                    {
                                        X509CRL crltempo = (X509CRL)cf2.generateCRL(inStream2);
                                        if (!(crltempo.getNextUpdate().after(fechahoy) && crltempo.getThisUpdate().before(fechahoy)))
                                        {
                                            CodigoDeError += xconstantes.X_CRL_FECHA_INCORRECTA;
                                            logWriter.println("--------------------------------------------------------------------------");
                                            MensajeDeError += "CRL (internet) fuera de fecha: "
                                                              + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(crltempo.getNextUpdate())
                                                              + "*"
                                                              + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(crltempo.getThisUpdate())
                                                              + xconstantes.RETORNO;
                                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                                    + CConstantes.X_ERROR + "|CRL no válida (internet)|"
                                                                    + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                                            ycrlvalida = false;
                                        }
                                        else
                                        {
                                            ycrlvalida = true;
                                        }
                                    }
                                    catch (Exception ex)
                                    {
                                        fcachecrl.delete();
                                        CodigoDeError += CConstantes.X_ERROR;
                                        logWriter.println("--------------------------------------------------------------------------");
                                        MensajeDeError += "El formato de la CRL (internet) no es válido: " + rutacrl + xconstantes.RETORNO;
                                        new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                                + CConstantes.X_ERROR + "|CRL no válida (internet)|"
                                                                + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                                    }
                                }
                            }
                        }

                        // Si tenemos una CRL válida, revisamos si el certificado está revocado
                        if (ycrlvalida)
                        {
                            CertificateFactory cf = CertificateFactory.getInstance("X.509");
                            FileInputStream inStream = new FileInputStream(rutacrl);
                            X509CRL crl = (X509CRL)cf.generateCRL(inStream);

                            if (crl.getRevokedCertificate(certificadocad) != null)
                            {
                                CodigoDeError += xconstantes.X_CERTIFICADO_REVOCADO;
                                logWriter.println("--------------------------------------------------------------------------");
                                MensajeDeError += xconstantes.Yget_mensaje_error(xconstantes.X_CERTIFICADO_REVOCADO)
                                                  + xconstantes.RETORNO;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_ADVERTENCIA + "|Certificado revocado|"
                                                        + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                            }
                            break;
                        }
                        else
                        {
                            // No se pudo descargar o no se pudo validar
                            if (!yadescargocrl)
                            {
                                logWriter.println("--------------------------------------------------------------------------");
                                CodigoDeError += xconstantes.X_NO_PUDO_DESCARGAR_CRL;
                                MensajeDeError += xconstantes.Yget_mensaje_error(xconstantes.X_NO_PUDO_DESCARGAR_CRL)
                                                  + xconstantes.RETORNO;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_ADVERTENCIA + "|No descarga CRL|"
                                                        + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                            }
                            else
                            {
                                logWriter.println("--------------------------------------------------------------------------");
                                CodigoDeError += xconstantes.X_NO_PUDO_VALIDAR_CRL;
                                MensajeDeError += "No se pudo ubicar una CRL válida." + xconstantes.RETORNO;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_ADVERTENCIA + "|CRL no válida|"
                                                        + Repositorio.getCN(certificadocad) + "|" + MensajeDeError);
                            }
                        }
                    }

                    // ------------------------------------------------------------------
                    // Si no se encontró CRL (itemscrl==0), probar TSL si está activo
                    // ------------------------------------------------------------------
                    if (itemscrl == 0)
                    {
                        // Revisa si la validación TSL está habilitada
                        if(!frmConfigurador.configuracion.isVg_validar_con_tsl())
                            continue;

                        if(rutatsl.length() == 0)
                            continue;

                        // Verificamos si es un certificado raíz (Issuer == Subject)
                        if (certificadocad.getIssuerDN().equals(certificadocad.getSubjectDN()))
                        {
                            // Descargamos TSL
                            if (rutatsl.length() > 0)
                            {
                                String[] rtsl = rutatsl.split(",");
                                for (String rtsl1 : rtsl)
                                {
                                    boolean b = guardar_url.descargar(pathalmacencrl, rtsl1, urlProxy, puertoProxy);
                                    if (b)
                                    {
                                        String irutatsl = guardar_url.ubicacion_cache(pathalmacencrl, rtsl1);
                                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                        dbf.setNamespaceAware(true);
                                        Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(irutatsl));
                                        NodeList nList1 = doc.getElementsByTagName("tsl:X509Certificate");
                                        boolean encontrotsl = false;

                                        for (int temp = 0; temp < nList1.getLength(); temp++)
                                        {
                                            Node nNode = nList1.item(temp);
                                            X509Certificate autoridades = generaX509Certificate(nNode.getTextContent());
                                            if (autoridades != null && autoridades.equals(certificadocad))
                                            {
                                                encontrotsl = true;
                                                break;
                                            }
                                        }
                                        if (!encontrotsl)
                                        {
                                            logWriter.println("--------------------------------------------------------------------------");
                                            CodigoDeError += xconstantes.X_CERTIFICADO_NOTSL;
                                            MensajeDeError += "No se encontró el certificado raíz en la TSL. ";
                                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                                    + CConstantes.X_ADVERTENCIA + "|No hallado en TSL|"
                                                                    + certificadocad.getIssuerDN().getName() + "|"
                                                                    + MensajeDeError);
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        CodigoDeError += xconstantes.X_NO_PUDO_DESCARGAR_TSL;
                                        MensajeDeError += "No se pudo descargar la TSL. ";
                                        logWriter.println("--------------------------------------------------------------------------");
                                        new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                                + CConstantes.X_ADVERTENCIA + "|No descarga TSL|"
                                                                + "No se logró la descarga de la TSL [" + rtsl1 + "].");
                                    }
                                }
                            }
                            else
                            {
                                logWriter.println("--------------------------------------------------------------------------");
                                MensajeDeError += "No se encuentra la TSL para validar el certificado raíz.";
                                CodigoDeError += xconstantes.X_CERTIFICADO_NOTSL;
                                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                        + CConstantes.X_ADVERTENCIA + "|No TSL configurada|"
                                                        + certificadocad.getIssuerDN().getName() + "|" + MensajeDeError);
                            }
                        }
                        else
                        {
                            MensajeDeError += "No se encontró la ruta de la CRL del certificado > " + certificadocad.getSubjectDN();
                            CodigoDeError += xconstantes.X_NO_SE_ENCONTRO_RUTA_CRL;
                            logWriter.println("--------------------------------------------------------------------------");
                            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                                    + CConstantes.X_ADVERTENCIA + "|No CRL en el certificado|"
                                                    + certificadocad.getIssuerDN().getName() + "|" + MensajeDeError);
                        }
                    }
                }
            }

            // Ajuste del retorno en caso de '000' -> '0', etc.
            return CodigoDeError.contains("000") ? CodigoDeError.replace("000", "0")
                 : CodigoDeError.contains("00")  ? CodigoDeError.replace("00", "0")
                 : CodigoDeError;
        }
        catch (CertificateException | IOException | CRLException | ParserConfigurationException | SAXException ex)
        {
            MensajeDeError += ex.toString();
            CodigoDeError += "12";
            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|"
                                    + CConstantes.X_ADVERTENCIA + "|"+CodigoDeError+"|"+MensajeDeError+"|");
            return CodigoDeError;
        }
    }

    // ------------------------------------------------------------------------
    // (NUEVO) Equivalente al Java 11: Verificar los bits KeyUsage
    // ------------------------------------------------------------------------
    private static final String[] keyUsageLabels = new String[] {
        "digitalSignature", "nonRepudiation", "keyEncipherment",
        "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign",
        "encipherOnly", "decipherOnly"
    };

    public static List<String> getKeyUsage(final X509Certificate cert)
    {
        final boolean[] keyUsage = cert.getKeyUsage();
        if (keyUsage != null)
        {
            final List<String> ret = new LinkedList<String>();
            for (int i = 0; i < keyUsage.length; ++i)
            {
                if (keyUsage[i])
                {
                    if (i < keyUsageLabels.length)
                    {
                        ret.add(keyUsageLabels[i]);
                    }
                    else
                    {
                        ret.add(String.valueOf(i));
                    }
                }
            }
            return ret;
        }
        else
        {
            return null;
        }
    }

    // ------------------------------------------------------------------------
    // Obtiene las URLs de CRL (CRLDistributionPoints) (igual que en Java 11)
    // ------------------------------------------------------------------------
    private static LinkedList<String> getCRLs(X509Certificate cert) throws CertificateParsingException, IOException
    {
        byte[] crldpExt = cert.getExtensionValue(X509Extensions.CRLDistributionPoints.getId());
        if (crldpExt == null)
        {
            return new LinkedList<String>();
        }

        ASN1InputStream oAsnInStream = new ASN1InputStream(new ByteArrayInputStream(crldpExt));
        ASN1Primitive derObjCrlDP = oAsnInStream.readObject();
        DEROctetString dosCrlDP = (DEROctetString) derObjCrlDP;
        byte[] crldpExtOctets = dosCrlDP.getOctets();

        ASN1InputStream oAsnInStream2 = new ASN1InputStream(new ByteArrayInputStream(crldpExtOctets));
        ASN1Primitive derObj2 = oAsnInStream2.readObject();

        CRLDistPoint distPoint = CRLDistPoint.getInstance(derObj2);
        LinkedList<String> crlUrls = new LinkedList<String>();

        for (DistributionPoint dp : distPoint.getDistributionPoints())
        {
            DistributionPointName dpn = dp.getDistributionPoint();
            if (dpn != null && dpn.getType() == DistributionPointName.FULL_NAME)
            {
                GeneralName[] genNames = GeneralNames.getInstance(dpn.getName()).getNames();
                for (GeneralName genName : genNames)
                {
                    if (genName.getTagNo() == GeneralName.uniformResourceIdentifier)
                    {
                        String url = DERIA5String.getInstance(genName.getName()).getString();
                        crlUrls.add(url);
                    }
                }
            }
        }

        return crlUrls;
    }

    // ------------------------------------------------------------------------
    // (NUEVO) Equivalente Java 11: Generar X509Certificate desde base64 TSL
    // ------------------------------------------------------------------------
    private static X509Certificate generaX509Certificate(String certEntry) throws IOException
    {
        certEntry = "-----BEGIN CERTIFICATE-----\n" + certEntry + "\n-----END CERTIFICATE-----";
        InputStream in = null;
        X509Certificate cert = null;
        try
        {
            byte[] certEntryBytes = certEntry.getBytes();
            in = new ByteArrayInputStream(certEntryBytes);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) certFactory.generateCertificate(in);
        }
        catch (CertificateException ex)
        {
            // Manejo de excepción si no se puede parsear
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }
        return cert;
    }

    // ------------------------------------------------------------------------
    // Getters del MensajeDeError / CodigoDeError, como en tu código original
    // ------------------------------------------------------------------------
    public static String Mensaje()
    {
        return MensajeDeError;
    }

    public static String getMensajeDeError()
    {
        return MensajeDeError;
    }

    public static String getCodigoDeError()
    {
        return CodigoDeError;
    }
}