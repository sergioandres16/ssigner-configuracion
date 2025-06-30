package Formularios;

import Clases.Certificado;
import Clases.Configuracion;
import Clases.Repositorio;
import Clases.So;
import Entidades.Firma;
import Entidades.Firmante;
import Entidades.Firmador;
import Metodos.ValidadorLicencia;
import Global.CConstantes;
import static Global.CConstantes.APLICACION_NOMBRE;
import Global.CLog;
import Metodos.helperClass;
import Modulos.frmFirmador;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import static java.time.Instant.now;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public final class frmConfigurador extends javax.swing.JFrame
{
    static String carpetaLibrerias;
    public static String carpetaDestinoLibrerias;
    public static Configuracion configuracion;
    public static So vcg_so = new So();

    public static boolean estaAbierto = true;
    public static int contador = 0;
    public static boolean firmados = false;
    public static int ok = 0;
    public static Repositorio repositorios = new Repositorio();
    Properties propiedades;
    private DefaultTableModel v_modelo;

    public frmConfigurador()
    {
        /* try 
            {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                    if ("Windows".equals(info.getName()))
                    {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
            } 
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) 
            {
                java.util.logging.Logger.getLogger(frmFirmador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }*/
        
        initComponents();
        v_modelo = new DefaultTableModel();
        
        // http://stackoverflow.com/a/3711786
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        
        try
        {
            iniciar();
        }
        catch (Exception ex)
        {
            txtResultado.setText(ex.getMessage());
        }
    }
    
    private void agregar_firmador_a_tabla()
    {
        try
        {
            v_modelo = new DefaultTableModel();
            tblFirmante.removeAll();
            v_modelo = (DefaultTableModel) tblFirmante.getModel();
            File f = new File(".");
            String laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
            String icon_ruta = laRuta + "icono_ok.png";
            ImageIcon img = new ImageIcon(icon_ruta);
            
            icon_ruta = laRuta + "icono_error.png";
            ImageIcon img2 = new ImageIcon(icon_ruta);
            
            DefaultTableCellRenderer renderizadorDeCeldas = new DefaultTableCellRenderer();
            DefaultTableCellRenderer renderizadorDeCeldas2 = new DefaultTableCellRenderer();
           
            renderizadorDeCeldas.setIcon(img);
            renderizadorDeCeldas.setHorizontalAlignment(0);
            
            renderizadorDeCeldas2.setIcon(img2);
            renderizadorDeCeldas2.setHorizontalAlignment(0);
            
            tblFirmante.getColumnModel().getColumn(3).setCellRenderer(renderizadorDeCeldas);
            
            String[] alias, cN, fechas;
            helperClass helpc = new helperClass();
            
            if(configuracion.getFirmanteElegido() == null)
            {
                String[] certs = configuracion.getVg_ruta_certificado().split(";");
                String[] certpw = configuracion.getVg_clave_certificado().split(";");
                
                for (int i = 0; i < certs.length; i++)
                {
                    String rep = configuracion.getTipoRepositorio().isEmpty() ? Repositorio.ARCHIVO : configuracion.getTipoRepositorio();

                    if (!rep.equals(Repositorio.ARCHIVO))
                        break;
                    
                    repositorios.cargarCertificado(helpc.decrypt(certpw[i]), rep, certs[i]);
                    repositorios.obtenerAliases();
                    
                    if(repositorios.getFirmantes() != null)
                    {
                        List<Firmante> LtDeCertificados = repositorios.getFirmantes();
                        
                        for (Firmante firmante : LtDeCertificados)
                        {
                            Object[] v_fila = new Object[3];
                            
                            v_fila[0] = firmante.getCertificado().getAlias();
                            v_fila[1] = firmante.getCertificado().getCn();
                            v_fila[2] = firmante.getCertificado().getFechafinal();
                            v_modelo.addRow(v_fila);
                        }
                    }
                }
            }
            else
            {
                alias = configuracion.getFirmanteElegido().getCertificado().getAlias().split(";");
                cN = configuracion.getFirmanteElegido().getCertificado().getCn().split(";");
                fechas = configuracion.getFirmanteElegido().getCertificado().getFechafinal().split(";");

                for (int i = 0; i < alias.length; i++)
                {
                    Object[] v_fila = new Object[3];
                    v_fila[0] = alias[i];
                    v_fila[1] = cN[i];
                    v_fila[2] = fechas[i];
                    v_modelo.addRow(v_fila);
                }
            }
        }
            
        catch (IOException ex)
        {
            Logger.getLogger(frmPin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getMacAddress(InetAddress ip) 
    {
        String address = null;
        try 
        {
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            if (mac == null)
                return "";
            
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < mac.length; i++)
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            
            address = sb.toString();
        } 
        catch (SocketException ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }

        return address;
    }
    
    private static boolean isVMMac(byte[] mac)
    {
        if(null == mac) return false;
        
        byte invalidMacs[][] = 
        {
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte)0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid: invalidMacs)        
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) 
                return true;
        
        return false;
    }
    
    private void iniciar() throws Exception
    {
        /*Concateno la ruta de la carpeta home del usuario con la carpeta de la aplicacion, generando la carpeta de origen*/
        File carpetaPadre = new File(frmConfigurador.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//            File carpetaPadre = new File("C:\\Program Files (x86)\\Saeta\\SSigner\\Configuracion\\dev\\SSignerUser.jar");//
        String rutaDestino = System.getProperty("user.home") + File.separator + APLICACION_NOMBRE;//+File.separator+"configuracion"+File.separator+"configuracion.propiedades";//     vcg_so.ObtenerSistemaOperativo_user_home()+File.separator + CConstantes.APLICACION_NOMBRE ;
        crearCarpetas(rutaDestino);
        /*Copiar las dlls*/
        carpetaLibrerias = carpetaPadre.getParentFile().getParentFile().getAbsolutePath() + File.separator + CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA;// f.getCanonicalPath() + File.separator + CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA;
        if (!setPathLibrerias())
            throw new Exception("Excepción al establecer la ruta de librerias : " + carpetaLibrerias);

        if (!leerArchivoPropiedades(0))
            bloquearControles(false);
    }
    
    private static boolean setPathLibrerias()
    {
        if (System.getProperty("os.name").toLowerCase().startsWith("win"))
            carpetaDestinoLibrerias = "C:\\Windows\\System32";
        else
        {
            String tmpSistema = System.getProperty("java.io.tmpdir");
            if (!tmpSistema.endsWith(File.separator))
                tmpSistema += File.separator;
            
            carpetaDestinoLibrerias = tmpSistema + "librerias";
            new File(carpetaDestinoLibrerias).mkdirs();
//            String rutaCarpetaLibrerias = carpetaDestinoLibrerias + "librerias";
            try
            {
                copiarArchivosEn(new File(carpetaLibrerias), new File(carpetaLibrerias), new File(carpetaDestinoLibrerias));
            } catch (Exception ex) { }
        }
        
        if (!carpetaDestinoLibrerias.endsWith(File.separator))
            carpetaDestinoLibrerias += File.separator;

        return new File(carpetaDestinoLibrerias).exists();
    }

    void crearCarpetas(String rutaDestino)
    {
        /*Crear Carpetas*/
        if (!new File(rutaDestino).exists())
            if (!new File(rutaDestino).mkdir())
            {
                escribirMensaje("No se pudo crear el siguiente archivo " + rutaDestino + ", revise la política de sus sistema operativos");
                return;
            }
        
        String[] carpetas = new String[]{
            CConstantes.APLICACION_LOG_CARPETA,
            CConstantes.APLICACION_PROPIEDADES_CARPETA,
            CConstantes.APLICACION_TEMPORAL_CARPETA,
            CConstantes.APLICACION_FIRMADOS_CARPETA,
            CConstantes.APLICACION_RECHAZADOS_CARPETA,
            CConstantes.APLICACION_CACHE_CARPETA,
            CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA,
            CConstantes.APLICACION_LICENCIA_CARPETA
        };
        
        for (String carpeta : carpetas)
        {
            CConstantes.mensajeln(rutaDestino + File.separator + carpeta + ", se creó ("
                    + new File(rutaDestino + File.separator + carpeta).mkdir() + ")");
        }
    }

    File[] validarLibrerias() throws Exception
    {
        if (!new File(carpetaLibrerias).exists())
            throw new Exception("ADVERTENCIA !! : No se logrado hallar la carpeta de librerias de token [" + new File(carpetaLibrerias).getAbsolutePath() + "].");
        
        File[] librerias = new File(carpetaLibrerias).listFiles();
        if (librerias.length == 0)
            throw new Exception("ADVERTENCIA !! : No se cuenta con librerias para el uso de tokens de firma. Por favor registre sus librerias en la opción [Sello de tiempo y librerías token/Librerías de dispositivos tokens (PKCS11)]");
        
        return librerias;
    }

    static void copiarArchivosEn(File rutaOrigen, File rutaDestino, File archivo) throws IOException {
        Path origen = Paths.get(rutaOrigen.toURI());
        Path destino = Paths.get(new File(rutaDestino + File.separator + archivo.getName()).toURI());
        CopyOption[] options = new CopyOption[]{
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES
        };
        Files.copy(origen, destino, options);
    }

    void escribirMensaje(String p_mensaje_cuerpo, Object p_objeto) {
        if (p_objeto instanceof javax.swing.JTextField) {
            ((javax.swing.JTextField) p_objeto).setText("");
            ((javax.swing.JTextField) p_objeto).setText(p_mensaje_cuerpo);
        }
    }

    void escribirMensaje(String p_mensaje_cuerpo) {
        txtResultado.setText(p_mensaje_cuerpo + CConstantes.SALTO_LINEA);
    }
    
    
    public static void validarCertificado1(Firmante.Certificado certificado) {
        String tipoRepositorio;
        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat logDateFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat entryDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //tipoRepositorio = configuracion.getTipoRepositorio();

        System.out.println("Certificado: " + certificado);
        String cn = certificado.getCn();
        String alias = certificado.getAlias();
        String dn = certificado.getDn();
        String ou = certificado.getOu();
        String cargo = certificado.getCargo();
        String email = certificado.getEmail();
        String empresa = certificado.getEmpresa();
        String uso = certificado.getUso();
        String serie = certificado.getSerie();
        String fechaInicial = certificado.getFechaInicial();
        String fechaFinal = certificado.getFechafinal();
        String version = certificado.getVersion();        
        String userHome = System.getProperty("user.name"); 
        String logDirectory = "C:\\Users\\" + userHome;
        String logFileName = logDirectory + "\\" + "SSigner Configurador_" + logDateFormat.format(new Date()) + ".log"; 

        if (!cn.isEmpty()) {
            try {
                //repositorios = new Repositorio();
                      
                try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {
                    //log
                    
                    logWriter.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    //JOptionPane.showMessageDialog(null, "Este es un mensaje de alerta", "Alerta", JOptionPane.WARNING_MESSAGE);

                    logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | El certificado cuenta con vigencia de " + fechaInicial + " hasta " + fechaFinal + ".");

                    try {
                        Date fechaInicio = dateFormat.parse(fechaInicial);
                        Date fechaFin = dateFormat.parse(fechaFinal);
                        Date now = new Date(); 
                        
                        if (!now.before(fechaInicio) && !now.after(fechaFin)) {
                            // Si la fecha/hora actual está entre fechaInicio y fechaFin (inclusive)
                            logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | AVISO: Certificado en periodo válido.");
                        } else {
                            logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date())  + " | AVISO: Certificado en periodo no válido.");
                            //JOptionPane.showMessageDialog(null, "Este es un mensaje de alerta", "Alerta", JOptionPane.WARNING_MESSAGE);
                            logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | ERROR:Cod:17>Certificado no puede ser utilizada para firma digital.");
                        }
                    } catch (ParseException e) {
                        logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | ERROR:Cod:17>Certificado no puede ser utilizada para firma digital.");
                        e.printStackTrace(); 
                    }
                    logWriter.println("[SSignerConfiguracion] | Hora: "+dateFormat.format(new Date()) + " | AVISO: Cod:18> El certificado es para encriptar - No se usa TSL");
                    logWriter.println("[SSignerConfiguracion] | Hora: "+dateFormat.format(new Date()) + " | DETALLES: "+certificado.getCertificadoX509().getSubjectDN().toString());
                    logWriter.println("[SSignerConfiguracion] | Hora: "+dateFormat.format(new Date()) + " | DETALLES: "+certificado.getCertificadoX509().getIssuerDN().toString());
                    if(uso.equals("digitalSignature,nonRepudiation")){
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: Certificado de No REPUDIO.");
                    }
                  
                    String crlFilePathString = logDirectory + "\\SSigner\\cache\\globalchambersignroot-2016.crl"; 
                    Path crlFilePath =  Paths.get(crlFilePathString);
                    File crlFile = crlFilePath.toFile();
                    // Verificar si el archivo existe
                    if (crlFile.exists()) {
                        BasicFileAttributes attrs = Files.readAttributes(crlFilePath, BasicFileAttributes.class);
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: La direccion del CRL es "+ crlFile.getAbsolutePath());
                        long lastModified = crlFile.lastModified();
                        Date creationTime = new Date(attrs.creationTime().toMillis());
                        String creationDateStr = logDateFormat1.format(creationTime);
                        String lastModifiedDateStr = logDateFormat1.format(new Date(lastModified));
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: OK - Fecha de creación del CRL: "+creationDateStr);
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: OK - Fecha de modificación del CRL: "+lastModifiedDateStr);
                    } else {
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | ERROR: El archivo CRL no existe.");                     
                    }
                    logWriter.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                   
                } catch (IOException e) {
                    System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
                }
                

            } catch (Exception ex) {
                txtResultado.setText("Excepción General de Validación de Certificados: " + ex);
                btnValidarCertificado.setText("Validar Certificado");
                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_EXCEPCION + "|" + CConstantes.X_ERROR + "|" + ex + "Excepción General de Validación de Certificados. " + ex);
            }
        } else {
            System.err.println("Error");
        }
    }

    
    
    public boolean validarCertificado()
    {
        String tipoRepositorio;
        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat logDateFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat entryDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        
        
        if (configuracion.getFirmanteElegido() == null)
        {
            escribirMensaje("No se ha selecionado ningun certificado para validar.");
            bloquearControles(true);
            return false;
        }
        System.out.println("Configuracion: " + configuracion);
        System.out.println("Firmante elegido: " + configuracion.getFirmanteElegido());
        // ...resto de tu código...

        String cn = configuracion.getFirmanteElegido().getCertificado().getCn();
        String alias = configuracion.getFirmanteElegido().getCertificado().getAlias();
        String dn = configuracion.getFirmanteElegido().getCertificado().getDn();
        String ou = configuracion.getFirmanteElegido().getCertificado().getOu();
        Firmante.Certificado cert = configuracion.getFirmanteElegido().getCertificado();
        String cargo = configuracion.getFirmanteElegido().getCertificado().getCargo();
        String email = configuracion.getFirmanteElegido().getCertificado().getEmail();
        String empresa = configuracion.getFirmanteElegido().getCertificado().getEmpresa();
        String uso = configuracion.getFirmanteElegido().getCertificado().getUso();
        String serie = configuracion.getFirmanteElegido().getCertificado().getSerie();
        String fechaInicial = configuracion.getFirmanteElegido().getCertificado().getFechaInicial();
        String fechaFinal = configuracion.getFirmanteElegido().getCertificado().getFechafinal();
        String version = configuracion.getFirmanteElegido().getCertificado().getVersion();        
        
        tipoRepositorio = configuracion.getTipoRepositorio();
        
        
        
        String userHome = System.getProperty("user.name"); 
        String logDirectory = "C:\\Users\\" + userHome;
        String logFileName = logDirectory + "\\" + "SSigner Configurador_"+ logDateFormat.format(new Date()) + ".log"; 

        
        if (!cn.equals(""))
        {
            try
            {
                bloquearControles(false);
                btnValidarCertificado.setText("Validando");
                txtResultado.setText("Validando certificado [" + cn + "]");
                Thread.sleep(2000);
                repositorios = new Repositorio();
                
                if (!repositorios.validarTipoRepositorio(tipoRepositorio))
                {
                    escribirMensaje("El almacén " + tipoRepositorio + " no es válido.");
                    btnValidarCertificado.setText("Validar Certificado");
                    return false;
                }
                
                if (tipoRepositorio.equalsIgnoreCase(Repositorio.TOKEN)) {
                    repositorios.cargarCertificado(new helperClass().decrypt(configuracion.getVg_clave_token()), tipoRepositorio, configuracion.getVg_ruta_libreria());
                }
                else if (tipoRepositorio.equalsIgnoreCase(Repositorio.ARCHIVO)) {
                    repositorios.cargarCertificado(new helperClass().decrypt(configuracion.getVg_clave_certificado()), tipoRepositorio, configuracion.getVg_ruta_certificado());
                }
                else
                    repositorios.cargarCertificado(new helperClass().decrypt(configuracion.getVg_clave_token()), tipoRepositorio, configuracion.getVg_ruta_certificado());
                
                List<Firmante> listaCertificados = repositorios.getFirmantes();
                
                try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFileName, true))) {
                    
                if (listaCertificados == null || listaCertificados.isEmpty()) 
                {
                    bloquearControles(true);
                    //escribirMensaje("El almacén " + tipoRepositorio + " no cuenta con certificados disponibles." + CLog.getMensaje_error());
                    escribirMensaje("El certificado de " + cn + " es válido.");
                    btnValidarCertificado.setText("Validar Certificado");
                    //log
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    logWriter.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    
                    logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | El certificado cuenta con vigencia de " + fechaInicial + " hasta " + fechaFinal + ".");

                    try {
                        Date fechaInicio = dateFormat.parse(fechaInicial);
                        Date fechaFin = dateFormat.parse(fechaFinal);
                        Date now = new Date(); 
                        
                        if (!now.before(fechaInicio) && !now.after(fechaFin)) {
                            // Si la fecha/hora actual está entre fechaInicio y fechaFin (inclusive)
                            logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | AVISO: Certificado en periodo válido.");
                        } else {
                            logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date())  + " | AVISO: Certificado en periodo no válido.");
                            logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | ERROR:Cod:17>Certificado no puede ser utilizada para firma digital.");
                        }
                    } catch (ParseException e) {
                        logWriter.println("[SSignerConfiguracion]  | Hora: " + dateFormat.format(new Date()) + " | ERROR:Cod:17>Certificado no puede ser utilizada para firma digital.");
                        e.printStackTrace(); 
                    }
                    logWriter.println("[SSignerConfiguracion] | Hora: "+dateFormat.format(new Date()) + " | AVISO: Cod:18> El certificado es para encriptar - No se usa TSL");
                    logWriter.println("[SSignerConfiguracion] | Hora: "+dateFormat.format(new Date()) + " | AVISO: "+cert.getCertificadoX509().getSubjectDN().toString());
                    if(uso.equals("digitalSignature,nonRepudiation")){
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: Certificado de No REPUDIO.");
                    }
                  
                    String crlFilePathString = logDirectory + "\\SSigner\\cache\\globalchambersignroot-2016.crl"; 
                    Path crlFilePath =  Paths.get(crlFilePathString);
                    File crlFile = crlFilePath.toFile();
                    // Verificar si el archivo existe
                    if (crlFile.exists()) {
                        BasicFileAttributes attrs = Files.readAttributes(crlFilePath, BasicFileAttributes.class);
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: La direccion del CRL es "+ crlFile.getAbsolutePath());
                        long lastModified = crlFile.lastModified();
                        Date creationTime = new Date(attrs.creationTime().toMillis());
                        String creationDateStr = logDateFormat1.format(creationTime);
                        String lastModifiedDateStr = logDateFormat1.format(new Date(lastModified));
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: OK - Fecha de creación del CRL: "+creationDateStr);
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | AVISO: OK - Fecha de modificación del CRL: "+lastModifiedDateStr);
                    } else {
                        logWriter.println("[SSignerConfiguracion]  | Hora: "+dateFormat.format(new Date()) + " | ERROR: El archivo CRL no existe.");                     
                    }
                    logWriter.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    return false;
                }
                } catch (IOException e) {
                    System.err.println("Error al escribir en el archivo de log: " + e.getMessage());
                }
                
                if (!repositorios.validarAliasCertificado(alias))
                {
                    bloquearControles(true);
                    escribirMensaje("El alias[" + alias + "] preconfigurado  del certificado [" + cn + "] no es válido.");
                    btnValidarCertificado.setText("Validar Certificado");
                    return false;
                }
                
                m_agregar_a_tabla_firmante(configuracion, 2);
                String respuesta;
                String tsl = "";
                String norepudio = "FALSE";
                
                if (configuracion.isVg_validar_con_tsl())
                    tsl = configuracion.getVg_url_tsl();
                
                if (configuracion.isVg_validar_con_no_repudio())
                    norepudio = CConstantes.X_TRUE;
                
                //             m_escribir_notificacion("validando certificado");
                bloquearControles(false);
                respuesta = Metodos.ValidarCertificados.filtrarCertificado(repositorios.getFirmanteElegido().getCertificado().getCertificadosConfianza(), 
                        new Date(), tsl, configuracion.getVg_carpeta_salida_crl_tsl() + File.separator, 
                        txtUrlProxy.getText(), txtPuertoProxy.getText(), norepudio);

                if (respuesta.equals("0"))
                {
                    frmConfigurador.txtResultado.setText(Metodos.ValidarCertificados.getMensajeDeError());
                    frmConfigurador.configuracion.setFirmanteElegido(repositorios.getFirmanteElegido());
                    m_agregar_a_tabla_firmante(configuracion, 0);
                    grabarPropiedad(CConstantes.STRTIPOREPOSITORIO, tipoRepositorio.trim());
                    grabarPropiedad(CConstantes.CERTALIAS, configuracion.getFirmanteElegido().getCertificado().getAlias().trim());
                    grabarPropiedad(CConstantes.CERTCLAVECERTIFICADO, configuracion.getVg_clave_certificado().trim());
                    grabarPropiedad(CConstantes.CERTCLAVETOKEN, configuracion.getVg_clave_token().trim());
                    grabarPropiedad(CConstantes.CERTFIRMANTE, configuracion.getFirmanteElegido().getCertificado().getCn().trim());
                    grabarPropiedad(CConstantes.CERTEMPRESA, configuracion.getFirmanteElegido().getCertificado().getEmpresa().trim());
                    grabarPropiedad(CConstantes.CERTCARGO, configuracion.getFirmanteElegido().getCertificado().getCargo().trim());
                    //             grabarPropiedad(CConstantes.CERTLOCACION, configuracion.getVg_localidad().trim());
                    //             grabarPropiedad(CConstantes.CERTMOTIVO, configuracion.getVg_motivo().trim());

                    grabarPropiedad(CConstantes.PROXY_PUERTO, txtPuertoProxy.getText().trim());
                    grabarPropiedad(CConstantes.PROXY_URL, txtUrlProxy.getText().trim());
                    grabarPropiedad(CConstantes.CERTNOMBRELIBRERIA, configuracion.getVg_ruta_libreria_nombre().trim());
                    grabarPropiedad(CConstantes.CERTRUTALIBRERIA, configuracion.getVg_ruta_libreria().trim());
                    grabarPropiedad(CConstantes.CERTRUTACERTIFICADO, configuracion.getVg_ruta_certificado().trim());
                    
                    btnValidarCertificado.setText("Validar Certificado");
                    txtResultado.setText(Metodos.ValidarCertificados.getMensajeDeError());
                    bloquearControles(true);
                    //                 m_escribir_notificacion(Metodos.ValidarCertificados.getMensajeDeError());
                    return true;
                }
                else 
                {
                    bloquearControles(true);
                    btnValidarCertificado.setText("Validar Certificado");
                    txtResultado.setText(Metodos.ValidarCertificados.getMensajeDeError());
                    m_agregar_a_tabla_firmante(configuracion, 1);
                }
            } 
            catch (Exception ex) {
                bloquearControles(true);
                txtResultado.setText("Excepción General de Validación de Certificados: " + ex);
                m_agregar_a_tabla_firmante(configuracion, -1);
                btnValidarCertificado.setText("Validar Certificado");
                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_EXCEPCION + "|" + CConstantes.X_ERROR + "|" + ex + "Excepción General de Validación de Certificados. " + ex);
            }
        }
        else
            bloquearControles(true);

        return false;
    }
    

    boolean leerArchivoPropiedades(int anchodefecto) throws Exception
    {
        try
        {
            Certificado certificado;
            String nombrePropiedad;
            Firma firmaDefecto;
            Firma.Coordenada coordenada;
            Firma.Dimension dimension;

            certificado = new Certificado();
            configuracion = new Configuracion();
            propiedades = new Properties();
            firmaDefecto = new Firma();
            coordenada = firmaDefecto.new Coordenada();
            dimension = firmaDefecto.new Dimension();

            File f = new File(Global.CConstantes.RUTAPROPIEDADES);
            
            if (!f.exists() || f.length() < 32)
                crearArchivoPropiedades();
            
            try (FileInputStream fisPropiedades = new FileInputStream(new File(Global.CConstantes.RUTAPROPIEDADES))) 
            {
                propiedades.load(fisPropiedades);
            }

            nombrePropiedad = "bol.cerrardocumento";
            configuracion.setVg_cerrar_documento(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.todaspaginas";
            configuracion.setVg_firma_visible_firmar_todas_paginas(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.tsl";
            configuracion.setVg_validar_con_tsl(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.repudio";
            configuracion.setVg_validar_con_no_repudio(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.tsa";
            configuracion.setVg_con_sello(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.visible";
            configuracion.setVg_firma_visible(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.cargo";
            configuracion.setVg_cargo_visible(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.empresa";
            configuracion.setVg_empresa_visible(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.imagen";
            configuracion.setVg_con_imagen(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "fil.rutacarpetasalida";
            configuracion.setVg_carpeta_salida(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "fil.rutacrl";
            configuracion.setVg_carpeta_salida_crl_tsl(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "fil.documento";
            configuracion.setVg_ruta_documento(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "fil.imagen";
            configuracion.setVg_firma_visible_ruta_imagen(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "fil.imagenmarcaagua";
            configuracion.setVg_firma_visible_ruta_image_marca_de_agua(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "hsh.claveaccesoalsistema";
            configuracion.setVg_sistema_clave(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "int.tamanofuente";
            configuracion.setVg_firma_visible_tamanio_fuente(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "int.pagina";
            configuracion.setVg_firma_visible_pagina(CConstantes.Yconvertiranumero(propiedades.getProperty((String) nombrePropiedad)));
            
            nombrePropiedad = "int.x";
            coordenada.setX(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_x(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "int.y";
            coordenada.setY(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_y(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "flt.ancho";
            dimension.setAncho(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_x1(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "flt.alto";
            dimension.setAlto(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_y1(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            
            nombrePropiedad = "int.xp";
            coordenada.setX(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_xp(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "int.yp";
            coordenada.setY(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_yp(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "flt.anchop";
            dimension.setAncho(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_x1p(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "flt.altop";
            dimension.setAlto(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            configuracion.setVg_firma_visible_y1p(CConstantes.Yconvertiranumerofloat(propiedades.getProperty((String) nombrePropiedad)));
            
            nombrePropiedad = "str.tiporepositorio";
            configuracion.setTipoRepositorio(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "str.fuente";
            configuracion.setVg_firma_visible_fuente(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "str.bd_servidores_tsa";
            configuracion.setVg_cadena_matriz_servicios_sello_tiempo(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "str.algoritmo";
            configuracion.setVg_algoritmo(propiedades.getProperty((String) nombrePropiedad));
            certificado.setAlias(propiedades.getProperty((String) nombrePropiedad));
            certificado.setCn(propiedades.getProperty((String) nombrePropiedad));
            certificado.setCargo(propiedades.getProperty((String) nombrePropiedad));
            certificado.setEmpresa(propiedades.getProperty((String) nombrePropiedad));
            configuracion.setFirmaDefecto(firmaDefecto);
            nombrePropiedad = "cert.motivo";
            configuracion.setVg_motivo(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "cert.locacion";
            configuracion.setVg_localidad(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "url.tsa.nombre";
            configuracion.setVg_tsa_seleccionado(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "url.tsa";
            configuracion.setVg_tsa_url(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "url.tsa.usuario";
            configuracion.setVg_tsa_usuario(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "hsh.clavetsa";
            configuracion.setVg_tsa_clave(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "url.tsl";
            configuracion.setVg_url_tsl(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "int.estilo";
            configuracion.setVg_firma_visible_estilo_firma(propiedades.getProperty((String) nombrePropiedad) != null ?Integer.parseInt(
                    propiedades.getProperty((String) nombrePropiedad)) : 1);
            nombrePropiedad = "cert.rutacertificado";
            configuracion.setVg_ruta_certificado(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "cert.nombre.libreria";
            configuracion.setVg_ruta_libreria_nombre(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "cert.rutalibreria";
            configuracion.setVg_ruta_libreria(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "hsh.clavecertificado";
            configuracion.setVg_clave_certificado(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "cert.clavecertificado";
            configuracion.setVg_clave_certificado(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "hsh.clavetoken";
            configuracion.setVg_clave_token(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "bol.ventana.volver.abrir";
            configuracion.setVg_ventana_volver_abrir(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "str_matriz_token_drivers";
            configuracion.setVg_cadena_matriz_token_drivers(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "licencia.key";
            configuracion.setVg_licencia_key(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "str.extension";
            configuracion.setVg_documento_extension_salida(propiedades.getProperty((String) nombrePropiedad));
            nombrePropiedad = "bol.extension.ini";
            configuracion.setVg_documento_extension_inicio(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.cal";
            configuracion.setConNumeroCal(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "bol.area";
            configuracion.setConCarmpoArea(Boolean.parseBoolean(propiedades.getProperty((String) nombrePropiedad)));
            nombrePropiedad = "firmante.texto";
            
            if (propiedades.containsKey(nombrePropiedad)) {
                configuracion.setVg_texto_firmante(propiedades.getProperty(nombrePropiedad));
            }
            
            nombrePropiedad = CConstantes.PROXY_URL;
            
            if (!propiedades.containsKey(nombrePropiedad) || propiedades.getProperty(nombrePropiedad) == null)
            {
                configuracion.setUrlProxy(txtUrlProxy.getText());
                grabarPropiedad(nombrePropiedad, txtUrlProxy.getText());
            }
            else
                configuracion.setUrlProxy(propiedades.getProperty(nombrePropiedad));
            
            nombrePropiedad = CConstantes.PROXY_PUERTO;
            
            if (!propiedades.containsKey(nombrePropiedad) || propiedades.getProperty(nombrePropiedad) == null)
            {
                configuracion.setPuertoProxy(txtPuertoProxy.getText());
                grabarPropiedad(nombrePropiedad, txtPuertoProxy.getText());
            }
            else
                configuracion.setPuertoProxy(propiedades.getProperty(nombrePropiedad));
            
            try (FileInputStream fisPropiedades = new FileInputStream(new File(Global.CConstantes.RUTAPROPIEDADES))) 
            {
                propiedades.load(fisPropiedades);
            }
            
            if (configuracion.getVg_carpeta_salida() == null || configuracion.getVg_carpeta_salida().equals("")) {
                configuracion.setVg_carpeta_salida(vcg_so.ObtenerSistemaOperativo_user_home() + File.separator + CConstantes.APLICACION_NOMBRE + File.separator + CConstantes.APLICACION_FIRMADOS_CARPETA);
                grabarPropiedad(CConstantes.FILRUTACARPETASALIDA, configuracion.getVg_carpeta_salida());
            }
            if (configuracion.getVg_carpeta_salida_crl_tsl() == null || configuracion.getVg_carpeta_salida_crl_tsl().equals("")) {
                configuracion.setVg_carpeta_salida_crl_tsl(vcg_so.ObtenerSistemaOperativo_user_home() + File.separator + CConstantes.APLICACION_NOMBRE + File.separator + CConstantes.APLICACION_CACHE_CARPETA);
                grabarPropiedad(CConstantes.FILRUTACARPETASALIDATSLCRL, configuracion.getVg_carpeta_salida_crl_tsl());
            }
            
            configuracion.setFirmanteElegido(repositorios.getFirmanteElegido());
            setLocationRelativeTo(null);            
            agregar_firmador_a_tabla();
            return true;
        }
        catch (FileNotFoundException ex)
        {
            escribirMensaje("Excepción de ubicación del archivo de configuracion>" + ex);
            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_EXCEPCION + "|" + CConstantes.X_ERROR + "|" + ex + "|Excepción de ubicación del archivo de configuracion. " + ex);
        }
        catch (IOException ex)
        {
            escribirMensaje("Excepción de lectura y escritura del archivo de configuracion>" + ex);
            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_EXCEPCION + "|" + CConstantes.X_ERROR + "|" + ex + "|Excepción de lectura y escritura del archivo de configuracion. " + ex);
        }
        return false;
    }

    private void crearArchivoPropiedades()throws Exception
    {       
        try (FileOutputStream fous = new FileOutputStream(Global.CConstantes.RUTAPROPIEDADES))
        {
            fous.write(Global.CConstantes.ESTRUCTURAPARAMETROS.getBytes());
        }
        catch (Exception ex)
        {
            throw new Exception("Excepción de ubicación del archivo de configuracion a crear>" + ex);
        }
    }

    private void grabarPropiedad(String key, String valor)
    {
        try (FileOutputStream fosArchivoPropiedades = new FileOutputStream(new File(Global.CConstantes.RUTAPROPIEDADES)))
        {
            propiedades.setProperty(key, valor);
            propiedades.store(fosArchivoPropiedades, CConstantes.APLICACION_NOMBRE);
        }
        catch (IOException ex)
        {
            escribirMensaje("Excepción de lectura y escritura al grabar el archivo de configuracion>" + ex);
            new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_EXCEPCION + "|" + CConstantes.X_ERROR + "|" + ex + "Excepción de lectura y escritura al grabar el archivo de propieades. " + ex);
        }
    }
    
    void bloquearControles(boolean valor)
    {
        ctrl_Menu.setEnabled(valor);
        btnValidarCertificado.setEnabled(valor);
        btnFirmaIndividual.setEnabled(valor);
        btnFirmaMasiva.setEnabled(valor);
        ctrl_menu_datos.setEnabled(valor);
        ctrl_menu_ir_frm_carpetas.setEnabled(valor);
        ctrl_menu_sello_tiempo_librerias.setEnabled(valor);
        ctrl_menu_ir_frm_Certificado.setEnabled(valor);
    }

//    private void conSinTsa() {
//        if (rbSoloFirma.isSelected()) {
//            cboSelloDeTiempo.setEnabled(false);
//            configuracion.setVg_con_sello(false);
//        } else {
//            cboSelloDeTiempo.setEnabled(true);
//            configuracion.setVg_con_sello(true);
//        }
//        grabarPropiedad(CConstantes.BOLTSA, "" + configuracion.isVg_con_sello());
//    }
//    void cargarSelloDeTiempoAlCombobox() {
//
//        for (int i = cboSelloDeTiempo.getItemCount() - 1; i >= 1; i--) {
//            cboSelloDeTiempo.removeItemAt(i);
//        }
//        tblTemporal = new JTable();
//        tblTemporal.setModel(new javax.swing.table.DefaultTableModel(
//                new Object[][]{},
//                new String[]{
//                    "Firmante", "Certificado", "Fecha de Expiración", "Estado"
//                }
//        ) {
//            boolean[] canEdit = new boolean[]{
//                false, false, false, false
//            };
//
//            @Override
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                return canEdit[columnIndex];
//            }
//        });
//        DefaultTableModel modeloTabla = (DefaultTableModel) tblTemporal.getModel();
//        String[] vg_fila_servicios_columna;
//        String v_bd_servidores_tsa = frmConfigurador.configuracion.getVg_cadena_matriz_servicios_sello_tiempo();
//        String[] vg_fila_servicios;
//        vg_fila_servicios = v_bd_servidores_tsa.split(";");
//        for (String vg_fila_servicio : vg_fila_servicios) {
//            try {
//                vg_fila_servicios_columna = vg_fila_servicio.split(",");
//                m_agregar_registro_sello_tiempo(vg_fila_servicios_columna, modeloTabla);
//            } catch (Exception ex) {
//                escribirMensaje("Excepción al listar los servicios de sello de tiempo>" + ex);
//                new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_EXCEPCION + "|" + CConstantes.X_ERROR + "|" + ex + "Excepción al listar los servicios de sello de tiempo. " + ex);
//
//            }
//        }
//        tblTemporal.setModel(modeloTabla);
//        configuracion.setVg_tabla_servicios_sello_tiempo(tblTemporal);
//    }
    void m_agregar_registro_sello_tiempo(String[] servicioDatos, DefaultTableModel p_modelo)
    {
        if (servicioDatos.length == 0)
            return;
        
        if (servicioDatos.length == 1)
            return;
        
        if (servicioDatos.length == 2)
        {
            servicioDatos = new String[]{
                servicioDatos[0],
                servicioDatos[1],
                "",
                ""
            };
        }
        
        if (servicioDatos.length == 3)
        {
            servicioDatos = new String[]{
                servicioDatos[0],
                servicioDatos[1],
                servicioDatos[2],
                ""
            };
        }
        
        Object[] registro = new Object[]
        {
            servicioDatos[0],
            servicioDatos[1],
            servicioDatos[2],
            servicioDatos[3]
        };
        
        p_modelo.addRow(registro);
    }

    void obtSelloDeTiempo(String nombreSelloTiempo)
    {
        JTable v_tabla_auxiliar = configuracion.getVg_tabla_servicios_sello_tiempo();
        DefaultTableModel v_modelo = (DefaultTableModel) v_tabla_auxiliar.getModel();
        if (v_modelo.getRowCount() > 0) {
            for (int i = 0; i < v_modelo.getRowCount(); i++) {
                String vi_valor_celda_tsa_nombre = (String) v_modelo.getValueAt(i, 0).toString();
                String vi_valor_celda_tsa_url = (String) v_modelo.getValueAt(i, 1).toString();
                String vi_valor_celda_tsa_usuario = (String) v_modelo.getValueAt(i, 2).toString();
                String vi_valor_celda_clave = (String) v_modelo.getValueAt(i, 3).toString();
                if (nombreSelloTiempo.equalsIgnoreCase(vi_valor_celda_tsa_nombre)) {
                    configuracion.setVg_tsa_seleccionado(vi_valor_celda_tsa_nombre);
                    configuracion.setVg_tsa_url(vi_valor_celda_tsa_url);
                    configuracion.setVg_tsa_usuario(vi_valor_celda_tsa_usuario);
                    configuracion.setVg_tsa_clave(vi_valor_celda_clave);
//                    /*Guardo servicio de sello de tiempo seleccionado*/
                    grabarPropiedad(CConstantes.URLTSANOMBRE, configuracion.getVg_tsa_seleccionado());
                    grabarPropiedad(CConstantes.URLTSA, configuracion.getVg_tsa_url());
                    grabarPropiedad(CConstantes.URLTSAUSUARIO, configuracion.getVg_tsa_usuario());
                    grabarPropiedad(CConstantes.HSHCLAVETSA, configuracion.getVg_tsa_clave());
                    break;
                }
            }
        }
    }

    DefaultTableModel m_agregar_a_tabla_firmante(Configuracion pc_certificado, int p_estado_certificado) {
        try {
            DefaultTableModel v_modelo = (DefaultTableModel) tblFirmante.getModel();
            DefaultTableCellRenderer renderizadorDeCeldas = new DefaultTableCellRenderer();
            Object[] v_fila = new Object[3];
            File f = new File(frmConfigurador.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            String laRuta = "/";//f.getPath()+ File.separator + "imagenes" + File.separator;
            String icon_ruta = laRuta + "icono_espera.png";
            if (p_estado_certificado == 0) {
                icon_ruta = laRuta + "icono_ok.png";
            }
            if (p_estado_certificado == 1) {
                icon_ruta = laRuta + "icono_error.png";
            }
            if (p_estado_certificado == -1) {
                icon_ruta = laRuta + "icono_warning.png";
            }
            ImageIcon img = new ImageIcon(getClass().getResource(icon_ruta));
            renderizadorDeCeldas.setIcon(img);
            renderizadorDeCeldas.setHorizontalAlignment(0);
            tblFirmante.getColumnModel().getColumn(3).setCellRenderer(renderizadorDeCeldas);
            v_fila[0] = pc_certificado.getFirmanteElegido().getCertificado().getCn();
            v_fila[1] = pc_certificado.getFirmanteElegido().getCertificado().getAlias();
            v_fila[2] = pc_certificado.getFirmanteElegido().getCertificado().getFechafinal();
            if (v_modelo.getRowCount() > 0) {
                v_modelo.removeRow(0);
                v_modelo.insertRow(0, v_fila);
            } else {
                v_modelo.addRow(v_fila);
            }
            return v_modelo;
        } catch (URISyntaxException ex) {
            txtResultado.setText("Excepción : " + ex.toString());
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ctrl_grp_firma = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnLogo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblEtiquetaIngresePin3 = new javax.swing.JLabel();
        lblNotificacion = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtResultado = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFirmante = new javax.swing.JTable();
        btnValidarCertificado = new javax.swing.JButton();
        btnFirmaIndividual = new javax.swing.JButton();
        btnFirmaMasiva = new javax.swing.JButton();
        btnEliminarCert = new javax.swing.JButton();
        txtPuertoProxy = new javax.swing.JTextField();
        lblEtiquetaIngresePin2 = new javax.swing.JLabel();
        lblEtiquetaIngresePin1 = new javax.swing.JLabel();
        txtUrlProxy = new javax.swing.JTextField();
        ctrl_Menu = new javax.swing.JMenuBar();
        ctrl_menu_ir_frm_Certificado = new javax.swing.JMenu();
        sub_menu_ir_windows = new javax.swing.JMenuItem();
        sub_menu_ir_pkcs11 = new javax.swing.JMenuItem();
        sub_menu_ir_pkcs12 = new javax.swing.JMenuItem();
        sub_menu_ir_validaciones = new javax.swing.JMenuItem();
        ctrl_menu_sello_tiempo_librerias = new javax.swing.JMenu();
        ctrl_menu_ir_frm_maestro_sello_tiempo = new javax.swing.JMenuItem();
        ctrl_menu_ir_frm_maestro_drivers = new javax.swing.JMenuItem();
        ctrl_menu_ir_frm_visor_pdf = new javax.swing.JMenu();
        ctrl_menu_ir_frm_carpetas = new javax.swing.JMenu();
        ctrl_menu_datos = new javax.swing.JMenu();
        ctrl_sub_menu_motivo_locacion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SSigner Configurador");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setOpaque(false);

        btnLogo.setBackground(new java.awt.Color(241, 241, 241));
        btnLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ssigner_logo.png"))); // NOI18N
        btnLogo.setBorder(null);
        btnLogo.setBorderPainted(false);
        btnLogo.setContentAreaFilled(false);
        btnLogo.setFocusPainted(false);
        btnLogo.setFocusable(false);
        btnLogo.setVerifyInputWhenFocusTarget(false);
        btnLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 153, 0));
        jLabel1.setFont(new java.awt.Font("sansserif", 1, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Creado por : Soluciones Aplicadas En Tecnología Avanzada S.A.C");
        jLabel1.setOpaque(true);

        lblEtiquetaIngresePin3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEtiquetaIngresePin3.setText("v2.5.1.1");
        lblEtiquetaIngresePin3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblEtiquetaIngresePin3.setOpaque(true);
        lblEtiquetaIngresePin3.setPreferredSize(new java.awt.Dimension(17, 16));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
            .addComponent(btnLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(497, 497, 497)
                    .addComponent(lblEtiquetaIngresePin3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(232, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(61, 61, 61)
                    .addComponent(lblEtiquetaIngresePin3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(21, Short.MAX_VALUE)))
        );

        lblNotificacion.setForeground(new java.awt.Color(0, 83, 154));
        lblNotificacion.setOpaque(true);

        jPanel5.setBackground(new java.awt.Color(241, 241, 241));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        txtResultado.setEditable(false);
        txtResultado.setBackground(new java.awt.Color(240, 240, 240));
        txtResultado.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        jScrollPane3.setViewportView(txtResultado);

        tblFirmante.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        tblFirmante.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Firmante", "Certificado", "Fecha de Expiración", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFirmante.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblFirmante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFirmanteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblFirmante);
        if (tblFirmante.getColumnModel().getColumnCount() > 0) {
            tblFirmante.getColumnModel().getColumn(0).setResizable(false);
            tblFirmante.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblFirmante.getColumnModel().getColumn(1).setResizable(false);
            tblFirmante.getColumnModel().getColumn(2).setResizable(false);
            tblFirmante.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblFirmante.getColumnModel().getColumn(3).setResizable(false);
            tblFirmante.getColumnModel().getColumn(3).setPreferredWidth(10);
        }

        btnValidarCertificado.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        btnValidarCertificado.setText("Validar Certificado");
        btnValidarCertificado.setPreferredSize(new java.awt.Dimension(111, 41));
        btnValidarCertificado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarCertificadoActionPerformed(evt);
            }
        });

        btnFirmaIndividual.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        btnFirmaIndividual.setText("Firma Individual");
        btnFirmaIndividual.setPreferredSize(new java.awt.Dimension(111, 41));
        btnFirmaIndividual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirmaIndividualActionPerformed(evt);
            }
        });

        btnFirmaMasiva.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        btnFirmaMasiva.setText("Firma Masiva");
        btnFirmaMasiva.setPreferredSize(new java.awt.Dimension(111, 41));
        btnFirmaMasiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirmaMasivaActionPerformed(evt);
            }
        });

        btnEliminarCert.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        btnEliminarCert.setText("Eliminar Certificado");
        btnEliminarCert.setPreferredSize(new java.awt.Dimension(111, 41));
        btnEliminarCert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarCertActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEliminarCert, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFirmaIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnValidarCertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFirmaMasiva, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminarCert, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnValidarCertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFirmaIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFirmaMasiva, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblEtiquetaIngresePin2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEtiquetaIngresePin2.setText("Puerto Proxy:");
        lblEtiquetaIngresePin2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblEtiquetaIngresePin2.setOpaque(true);
        lblEtiquetaIngresePin2.setPreferredSize(new java.awt.Dimension(17, 16));

        lblEtiquetaIngresePin1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEtiquetaIngresePin1.setText("Url Proxy:");
        lblEtiquetaIngresePin1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblEtiquetaIngresePin1.setOpaque(true);
        lblEtiquetaIngresePin1.setPreferredSize(new java.awt.Dimension(17, 16));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNotificacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEtiquetaIngresePin1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEtiquetaIngresePin2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUrlProxy)
                            .addComponent(txtPuertoProxy, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUrlProxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEtiquetaIngresePin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPuertoProxy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEtiquetaIngresePin2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblNotificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ctrl_menu_ir_frm_Certificado.setText("   Certificado   ");
        ctrl_menu_ir_frm_Certificado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_menu_ir_frm_CertificadoActionPerformed(evt);
            }
        });

        sub_menu_ir_windows.setText("Ordenador");
        sub_menu_ir_windows.setName("WINDOWS-MY"); // NOI18N
        sub_menu_ir_windows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sub_menu_ir_windowsActionPerformed(evt);
            }
        });
        ctrl_menu_ir_frm_Certificado.add(sub_menu_ir_windows);

        sub_menu_ir_pkcs11.setText("Dispositivo");
        sub_menu_ir_pkcs11.setName("PKCS11"); // NOI18N
        sub_menu_ir_pkcs11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sub_menu_ir_pkcs11ActionPerformed(evt);
            }
        });
        ctrl_menu_ir_frm_Certificado.add(sub_menu_ir_pkcs11);

        sub_menu_ir_pkcs12.setText("Archivo");
        sub_menu_ir_pkcs12.setName("PKCS12"); // NOI18N
        sub_menu_ir_pkcs12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sub_menu_ir_pkcs12ActionPerformed(evt);
            }
        });
        ctrl_menu_ir_frm_Certificado.add(sub_menu_ir_pkcs12);

        sub_menu_ir_validaciones.setText("Validaciones");
        sub_menu_ir_validaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sub_menu_ir_validacionesActionPerformed(evt);
            }
        });
        ctrl_menu_ir_frm_Certificado.add(sub_menu_ir_validaciones);

        ctrl_Menu.add(ctrl_menu_ir_frm_Certificado);

        ctrl_menu_sello_tiempo_librerias.setText("Sello de tiempo y librerías token");
        ctrl_menu_sello_tiempo_librerias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_menu_sello_tiempo_libreriasActionPerformed(evt);
            }
        });

        ctrl_menu_ir_frm_maestro_sello_tiempo.setText("Servicios de Sello de Tiempo (TimeStamp)");
        ctrl_menu_ir_frm_maestro_sello_tiempo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_menu_ir_frm_maestro_sello_tiempoActionPerformed(evt);
            }
        });
        ctrl_menu_sello_tiempo_librerias.add(ctrl_menu_ir_frm_maestro_sello_tiempo);

        ctrl_menu_ir_frm_maestro_drivers.setText("Librerías de dispositivos tokens (PKCS11)");
        ctrl_menu_ir_frm_maestro_drivers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_menu_ir_frm_maestro_driversActionPerformed(evt);
            }
        });
        ctrl_menu_sello_tiempo_librerias.add(ctrl_menu_ir_frm_maestro_drivers);

        ctrl_Menu.add(ctrl_menu_sello_tiempo_librerias);

        ctrl_menu_ir_frm_visor_pdf.setText("   Dibujar firma    ");
        ctrl_menu_ir_frm_visor_pdf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctrl_menu_ir_frm_visor_pdfMouseClicked(evt);
            }
        });
        ctrl_Menu.add(ctrl_menu_ir_frm_visor_pdf);

        ctrl_menu_ir_frm_carpetas.setText("Carpetas");
        ctrl_menu_ir_frm_carpetas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctrl_menu_ir_frm_carpetasMouseClicked(evt);
            }
        });
        ctrl_menu_ir_frm_carpetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_menu_ir_frm_carpetasActionPerformed(evt);
            }
        });
        ctrl_Menu.add(ctrl_menu_ir_frm_carpetas);

        ctrl_menu_datos.setText("Datos extras");
        ctrl_menu_datos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_menu_datosActionPerformed(evt);
            }
        });

        ctrl_sub_menu_motivo_locacion.setText("Motivo y localidad");
        ctrl_sub_menu_motivo_locacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_sub_menu_motivo_locacionActionPerformed(evt);
            }
        });
        ctrl_menu_datos.add(ctrl_sub_menu_motivo_locacion);

        ctrl_Menu.add(ctrl_menu_datos);

        setJMenuBar(ctrl_Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("SSigner");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ctrl_menu_ir_frm_CertificadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_menu_ir_frm_CertificadoActionPerformed
        bloquearControles(false);
        new frmCertificados().setVisible(true);
        bloquearControles(true);
    }//GEN-LAST:event_ctrl_menu_ir_frm_CertificadoActionPerformed

    private void btnValidarCertificadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarCertificadoActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                validarCertificado();
            }

        }).start();

    }//GEN-LAST:event_btnValidarCertificadoActionPerformed

    private void sub_menu_ir_windowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sub_menu_ir_windowsActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                vg_cargo = true;
                while (vg_cargo) {
                    try {
                        Thread.sleep(600);
                        lblNotificacion.setText("Listando certificados.");
                        Thread.sleep(600);
                        lblNotificacion.setText("Listando certificados..");
                        Thread.sleep(600);
                        lblNotificacion.setText("Listando certificados...");
                        Thread.sleep(600);
                        lblNotificacion.setText("Listando certificados....");
                        Thread.sleep(600);
                        lblNotificacion.setText("Listando certificados.....");
                    } catch (InterruptedException ex) {
                    }
                }
                lblNotificacion.setText("");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                bloquearControles(false);
                new frmCertificados(frmConfigurador.this, true, sub_menu_ir_windows.getName().toUpperCase(), "", "").setVisible(true);
                grabarPropiedad(CConstantes.STRTIPOREPOSITORIO, configuracion.getTipoRepositorio().trim());
                if (configuracion.getFirmanteElegido() != null)
                {
                grabarPropiedad(CConstantes.CERTALIAS, configuracion.getFirmanteElegido().getCertificado().getAlias().trim());
                grabarPropiedad(CConstantes.CERTFIRMANTE, configuracion.getFirmanteElegido().getCertificado().getCn().trim());
                grabarPropiedad(CConstantes.CERTEMPRESA, configuracion.getFirmanteElegido().getCertificado().getEmpresa().trim());
                grabarPropiedad(CConstantes.CERTCARGO, configuracion.getFirmanteElegido().getCertificado().getCargo().trim());
                }
                bloquearControles(true);
                vg_cargo = false;
            }
        }).start();
    }//GEN-LAST:event_sub_menu_ir_windowsActionPerformed

    private void sub_menu_ir_pkcs11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sub_menu_ir_pkcs11ActionPerformed
        new Thread(() ->
        {
            try
            {
                validarLibrerias();
                bloquearControles(false);
                Repositorio.cerrarventana = 0;
                new frmPin(frmConfigurador.this, true, sub_menu_ir_pkcs11.getName().toUpperCase()).setVisible(true);
                bloquearControles(true);
                vg_cargo = false;
                
                if (Repositorio.cerrarventana == 0)
                    return;
                
                grabarPropiedad(CConstantes.STRTIPOREPOSITORIO, configuracion.getTipoRepositorio().trim());
                grabarPropiedad(CConstantes.CERTALIAS, configuracion.getFirmanteElegido().getCertificado().getAlias().trim());
                grabarPropiedad(CConstantes.CERTCLAVETOKEN, configuracion.getVg_clave_token().trim());
                grabarPropiedad(CConstantes.CERTFIRMANTE, configuracion.getFirmanteElegido().getCertificado().getCn().trim());
                grabarPropiedad(CConstantes.CERTEMPRESA, configuracion.getFirmanteElegido().getCertificado().getEmpresa().trim());
                grabarPropiedad(CConstantes.CERTCARGO, configuracion.getFirmanteElegido().getCertificado().getCargo().trim());
                grabarPropiedad(CConstantes.CERTNOMBRELIBRERIA, configuracion.getVg_ruta_libreria_nombre().trim());
                grabarPropiedad(CConstantes.CERTRUTALIBRERIA, configuracion.getVg_ruta_libreria().trim());
            }
            catch (Exception ex)
            {
                txtResultado.setText("Excepción al iniciar módulo de Dispositivo > " + ex.getMessage());
            }
        }).start();
    }//GEN-LAST:event_sub_menu_ir_pkcs11ActionPerformed
    static boolean vg_cargo = false;
    private void sub_menu_ir_pkcs12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sub_menu_ir_pkcs12ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                bloquearControles(false);
                Repositorio.cerrarventana = 0;
                new frmPin(frmConfigurador.this, true, sub_menu_ir_pkcs12.getName().toUpperCase()).setVisible(true);
                bloquearControles(true);
                vg_cargo = false;

                if (Repositorio.cerrarventana == 0)
                    return;
                
                grabarPropiedad(CConstantes.STRTIPOREPOSITORIO, configuracion.getTipoRepositorio());
                //grabarPropiedad(CConstantes.CERTALIAS, configuracion.getFirmanteElegido().getCertificado().getAlias().trim());
                //grabarPropiedad(CConstantes.CERTCLAVECERTIFICADO, new helperClass().encrypt(configuracion.getVg_clave_certificado()));
                //grabarPropiedad(CConstantes.CERTFIRMANTE, configuracion.getFirmanteElegido().getCertificado().getCn().trim());
                //grabarPropiedad(CConstantes.CERTEMPRESA, configuracion.getFirmanteElegido().getCertificado().getEmpresa().trim());
                //grabarPropiedad(CConstantes.CERTCARGO, configuracion.getFirmanteElegido().getCertificado().getCargo().trim());
                
                grabarPropiedad(CConstantes.CERTCLAVECERTIFICADO, configuracion.getVg_clave_certificado().trim());
                grabarPropiedad(CConstantes.CERTRUTACERTIFICADO, configuracion.getVg_ruta_certificado().trim());
                agregar_firmador_a_tabla();
            }
        }).start();
    }//GEN-LAST:event_sub_menu_ir_pkcs12ActionPerformed


    private void ctrl_menu_sello_tiempo_libreriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_menu_sello_tiempo_libreriasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_menu_sello_tiempo_libreriasActionPerformed

    private void ctrl_menu_ir_frm_maestro_sello_tiempoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_menu_ir_frm_maestro_sello_tiempoActionPerformed

        new frmMaestroServidores(frmConfigurador.this, true).setVisible(true);
        grabarPropiedad(CConstantes.URLTSANOMBRE, configuracion.getVg_tsa_seleccionado() == null ? "" : configuracion.getVg_tsa_seleccionado());
        grabarPropiedad(CConstantes.URLTSA, configuracion.getVg_tsa_url() == null ? "" : configuracion.getVg_tsa_url());
        grabarPropiedad(CConstantes.URLTSAUSUARIO, configuracion.getVg_tsa_usuario() == null ? "" : configuracion.getVg_tsa_usuario());
        grabarPropiedad(CConstantes.HSHCLAVETSA, configuracion.getVg_tsa_clave() == null ? "" : configuracion.getVg_tsa_clave());
        grabarPropiedad(CConstantes.STR_BD_SERVIDORES_TSA, configuracion.getVg_cadena_matriz_servicios_sello_tiempo());
//        cargarSelloDeTiempoAlCombobox();
//        if (cboSelloDeTiempo.getItemCount() > 1) {
//            obtSelloDeTiempo(configuracion.getVg_tsa_seleccionado());
//        }
//        cboSelloDeTiempo.setSelectedItem(configuracion.getVg_tsa_seleccionado());
    }//GEN-LAST:event_ctrl_menu_ir_frm_maestro_sello_tiempoActionPerformed

    private void ctrl_menu_ir_frm_carpetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_menu_ir_frm_carpetasActionPerformed

    }//GEN-LAST:event_ctrl_menu_ir_frm_carpetasActionPerformed

    private void ctrl_menu_ir_frm_carpetasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_menu_ir_frm_carpetasMouseClicked
        new frmCarpetas(this, true).setVisible(true);
        grabarPropiedad(CConstantes.STR_EXTENSION, configuracion.getVg_documento_extension_salida());
        grabarPropiedad(CConstantes.FILRUTACARPETASALIDA, configuracion.getVg_carpeta_salida());
        grabarPropiedad(CConstantes.FILRUTACARPETASALIDATSLCRL, configuracion.getVg_carpeta_salida_crl_tsl());
        grabarPropiedad(CConstantes.BOL_EXTENSION_INI, Boolean.toString(configuracion.isVg_documento_extension_inicio()));
    }//GEN-LAST:event_ctrl_menu_ir_frm_carpetasMouseClicked

    private void ctrl_sub_menu_motivo_locacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_sub_menu_motivo_locacionActionPerformed
        new frmDatosExtras(this, true).setVisible(true);
        grabarPropiedad(CConstantes.CERTMOTIVO, "" + configuracion.getVg_motivo());
        grabarPropiedad(CConstantes.CERTLOCACION, "" + configuracion.getVg_localidad());
        grabarPropiedad(CConstantes.BOLVENTANAVOLVERABRIR, "" + configuracion.isVg_ventana_volver_abrir());
    }//GEN-LAST:event_ctrl_sub_menu_motivo_locacionActionPerformed

    private void ctrl_menu_datosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_menu_datosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_menu_datosActionPerformed

    private void sub_menu_ir_validacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sub_menu_ir_validacionesActionPerformed
        new frmValidaciones(this, true).setVisible(true);
        grabarPropiedad(CConstantes.BOLTSL, CConstantes.YconvertiranumeroBooleanoString(configuracion.isVg_validar_con_tsl() + "").toUpperCase());
        grabarPropiedad(CConstantes.BOLREPUDIO, CConstantes.YconvertiranumeroBooleanoString(configuracion.isVg_validar_con_no_repudio() + "").toUpperCase());
        grabarPropiedad(CConstantes.URLTSL, configuracion.getVg_url_tsl());
    }//GEN-LAST:event_sub_menu_ir_validacionesActionPerformed

    private void ctrl_menu_ir_frm_maestro_driversActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_menu_ir_frm_maestro_driversActionPerformed
        try {
            new frmMaestroDrivers(this, true).setVisible(true);
            validarLibrerias();
            grabarPropiedad(CConstantes.CERTNOMBRELIBRERIA, configuracion.getVg_ruta_libreria_nombre());
            grabarPropiedad(CConstantes.CERTRUTALIBRERIA, configuracion.getVg_ruta_libreria());
            grabarPropiedad(CConstantes.STR_MATRIZ_TOKEN_DRIVERS, configuracion.getVg_cadena_matriz_token_drivers());
        } catch (Exception ex) {
            txtResultado.setText(ex.getMessage());
        }

    }//GEN-LAST:event_ctrl_menu_ir_frm_maestro_driversActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!frmFirmador.estaAbierto) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoActionPerformed

    private void tblFirmanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFirmanteMouseClicked
        if (tblFirmante.getRowCount() > 0) {
            int filaSeleccionada = tblFirmante.getSelectedRow();
            if (filaSeleccionada > -1) {
                if (frmConfigurador.repositorios.validarAliasCertificado(tblFirmante.getValueAt(filaSeleccionada, 1).toString())) {
                    new frmCertificadoPropiedades(frmConfigurador.this, true, frmConfigurador.repositorios.getFirmanteElegido().getCertificado()).setVisible(true);
                }
            }

        }
    }//GEN-LAST:event_tblFirmanteMouseClicked

    private void btnFirmaIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirmaIndividualActionPerformed

        try
        {
            btnFirmaIndividual.setText("Iniciando");
            bloquearControles(false);
            setVisible(false);
            iniciarSSignerUser();
        }
        catch (Exception ex)
        {
            CConstantes.dialogo("Excepción " + ex.getMessage());
        }
        
        btnFirmaIndividual.setText("Usuario Final");
    }//GEN-LAST:event_btnFirmaIndividualActionPerformed


    private void btnFirmaMasivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirmaMasivaActionPerformed

        try {
            btnFirmaMasiva.setText("Iniciando");
            bloquearControles(false);
            setVisible(false);
            iniciarSSigneAgente();
        } catch (Exception ex) {
            CConstantes.dialogo("Excepción " + ex.getMessage());
        }
        btnFirmaMasiva.setText("Server Agente");
    }//GEN-LAST:event_btnFirmaMasivaActionPerformed

    private void ctrl_menu_ir_frm_visor_pdfMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_ctrl_menu_ir_frm_visor_pdfMouseClicked
    {//GEN-HEADEREND:event_ctrl_menu_ir_frm_visor_pdfMouseClicked
        new frmVisorPdf(this, true, new Point((int) configuracion.getVg_firma_visible_x(), (int) configuracion.getVg_firma_visible_y()),
            new Point((int) configuracion.getVg_firma_visible_x1(), (int) configuracion.getVg_firma_visible_y1())).setVisible(true);
        
        grabarPropiedad(CConstantes.FILIMAGEN, configuracion.getVg_firma_visible_ruta_imagen());
        grabarPropiedad(CConstantes.FILIMAGENMARCAAGUA, configuracion.getVg_firma_visible_ruta_image_marca_de_agua());
        grabarPropiedad(CConstantes.FILRUTADOCUMENTO, configuracion.getVg_ruta_documento());
        grabarPropiedad(CConstantes.INTX, "" + configuracion.getVg_firma_visible_x());
        grabarPropiedad(CConstantes.INTY, "" + configuracion.getVg_firma_visible_y());
        grabarPropiedad(CConstantes.FLTANCHO, "" + configuracion.getVg_firma_visible_x1());
        grabarPropiedad(CConstantes.FLTALTO, "" + configuracion.getVg_firma_visible_y1());
        grabarPropiedad(CConstantes.INTXp, "" + configuracion.getVg_firma_visible_xp());
        grabarPropiedad(CConstantes.INTYp, "" + configuracion.getVg_firma_visible_yp());
        grabarPropiedad(CConstantes.FLTANCHOp, "" + configuracion.getVg_firma_visible_x1p());
        grabarPropiedad(CConstantes.FLTALTOp, "" + configuracion.getVg_firma_visible_y1p());
        grabarPropiedad(CConstantes.INTESTILO, configuracion.getVg_firma_visible_estilo_firma() + "");
        grabarPropiedad(CConstantes.BOLVISIBLE, Boolean.toString(configuracion.isVg_firma_visible()));
        grabarPropiedad(CConstantes.BOLCERRARDOCUMENTO, Boolean.toString(configuracion.isVg_cerrar_documento()));
        grabarPropiedad(CConstantes.STRFUENTE, configuracion.getVg_firma_visible_fuente());
        grabarPropiedad(CConstantes.STRALGORITMO, configuracion.getVg_algoritmo());
        grabarPropiedad(CConstantes.BOLIMAGEN, Boolean.toString(configuracion.isVg_con_imagen()));
        grabarPropiedad(CConstantes.BOLTODASPAGINAS, Boolean.toString(configuracion.isVg_firma_visible_firmar_todas_paginas()));
        grabarPropiedad(CConstantes.BOL_CAL, Boolean.toString(configuracion.getConNumeroCal()));
        grabarPropiedad(CConstantes.BOL_AREA, Boolean.toString(configuracion.getConCarmpoArea()));
        grabarPropiedad(CConstantes.STRALGORITMO, "" + configuracion.getVg_algoritmo());
        grabarPropiedad(CConstantes.INTPAGINA, "" + configuracion.getVg_firma_visible_pagina());
        grabarPropiedad(CConstantes.INTTAMANOFUENTE, "" + configuracion.getVg_firma_visible_tamanio_fuente());
        grabarPropiedad(CConstantes.TextoFirmante, "" + configuracion.getVg_texto_firmante());
    }//GEN-LAST:event_ctrl_menu_ir_frm_visor_pdfMouseClicked

    private void btnEliminarCertActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEliminarCertActionPerformed
    {//GEN-HEADEREND:event_btnEliminarCertActionPerformed
        if (tblFirmante.getSelectedRow() < 0)
            return;
        
        if(JOptionPane.showConfirmDialog (null, "¿Desea eliminar el certificado elegido?","", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        int r = tblFirmante.getSelectedRow();
        v_modelo.removeRow(r);
        
        String[] certs = configuracion.getVg_ruta_certificado().split(";");
        String[] certpw = configuracion.getVg_clave_certificado().split(";");
        
        String cert = "", pw = "";
        
        for (int i = 0; i < certs.length; i++)
        {
            if (i == r)
                continue;
            
            cert += certs[i] + ";";
            pw += certpw[i] + ";";
        }
        
        if (cert.endsWith(";"))
            cert = cert.substring(0, cert.length() - 1);
        
        if (pw.endsWith(";"))
            pw = pw.substring(0, pw.length() - 1);
        
        configuracion.setVg_ruta_certificado(cert);
        configuracion.setVg_clave_certificado(pw);
        
        grabarPropiedad(CConstantes.CERTCLAVECERTIFICADO, configuracion.getVg_clave_certificado().trim());
        grabarPropiedad(CConstantes.CERTRUTACERTIFICADO, configuracion.getVg_ruta_certificado().trim());

        if (tblFirmante.getColumnCount() > 0)
            tblFirmante.setEditingColumn(0);

        if (tblFirmante.getRowCount() > 0)
            tblFirmante.setEditingRow(0);
    }//GEN-LAST:event_btnEliminarCertActionPerformed

    void iniciarSSignerUser()
    {
        setearEstaAbierto();
        frmFirmador frmDocumento;
        String documentoRuta = "";
        String modalidad = "-u";
        String carpetaEntrada = "";
        String carpetaDestino = "";
        frmDocumento = new frmFirmador(documentoRuta, modalidad, carpetaEntrada, carpetaDestino, new Point(10, 10), new Point(10, 10));
        frmDocumento.setVisible(true);
    }

    void iniciarSSigneAgente()
    {
        setearEstaAbierto();
        frmFirmador frmDocumento;
        String documentoRuta = "";
        String modalidad = "-s";
        String carpetaEntrada = "";
        String carpetaDestino = "";
        frmDocumento = new frmFirmador(documentoRuta, modalidad, carpetaEntrada, carpetaDestino, new Point(10, 10), new Point(10, 10));
        frmDocumento.setVisible(true);
    }

    void setearEstaAbierto()
    {
        estaAbierto = false;
    }

    static void writelog(String msg)
    {
        File f2 = new File(System.getProperty("user.home") + File.separator + "ssigner_Configuracion_error.log");

        if(!f2.exists())
            try
            {
                f2.createNewFile();
            }
            catch (IOException ee) { }

        try(BufferedWriter bfw = new BufferedWriter(new FileWriter(f2, true)))
        {
            bfw.write(msg);
            bfw.newLine();
            bfw.flush();
            bfw.close();
        }
        catch(IOException ee) { }    
    }
    
    /**
     * @param args the command line arguments
     */
   public static void main(String args[])
{
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable()
    {
        @Override
        public void run()
        {
            // Variable de control para validación (0 = no validar, 1 = validar)
            int validarLicencia = 0; // Cambia a 0 para desactivar validación
            
            if (validarLicencia == 1) {
                // VALIDACIÓN DE LICENCIA AL INICIO
                try {
                    System.out.println("Iniciando validación de licencia...");
                    
                    // Obtener la MAC address local
                    String macLocal = ValidadorLicencia.obtenerMacLocal();
                    if (macLocal == null || macLocal.isEmpty()) {
                        JOptionPane.showMessageDialog(null, 
                            "No se pudo obtener la dirección MAC del equipo.\n" +
                            "El programa no puede continuar.",
                            "Error de Licencia", JOptionPane.ERROR_MESSAGE);
                        ValidadorLicencia.escribirLog("No se pudo obtener MAC address");
                        System.exit(1); // Salir con código de error
                    }
                    
                    System.out.println("MAC Address encontrada: " + macLocal);
                    ValidadorLicencia.escribirLog("MAC Address encontrada: " + macLocal);
                    
                    // Validar la licencia con el servicio web
                    boolean licenciaValida = ValidadorLicencia.validarLicencia(macLocal);
                    if (!licenciaValida) {
                        // La licencia no es válida, el mensaje ya fue mostrado en ValidadorLicencia
                        ValidadorLicencia.escribirLog("Licencia no válida para MAC: " + macLocal);
                        System.exit(1); // Salir con código de error
                    }
                    
                    // Si llegamos aquí, la licencia es válida
                    System.out.println("Licencia válida. Iniciando aplicación...");
                    ValidadorLicencia.escribirLog("Licencia válida para MAC: " + macLocal);
                    
                } catch (Exception e) {
                    // Error general no manejado
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                        "Error al iniciar la aplicación:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    ValidadorLicencia.escribirLog("Error no manejado: " + e.getMessage());
                    System.exit(1);
                }
            } else {
                System.out.println("Validación de licencia desactivada - Modo desarrollo");
            }
            
            // ESTA PARTE SIEMPRE SE EJECUTA (está fuera del if)
            // Continuar con la inicialización normal del formulario
            frmConfigurador formulario = new frmConfigurador();
            formulario.setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnEliminarCert;
    public static javax.swing.JButton btnFirmaIndividual;
    public static javax.swing.JButton btnFirmaMasiva;
    public static javax.swing.JButton btnLogo;
    public static javax.swing.JButton btnValidarCertificado;
    private javax.swing.JMenuBar ctrl_Menu;
    private javax.swing.ButtonGroup ctrl_grp_firma;
    private javax.swing.JMenu ctrl_menu_datos;
    private javax.swing.JMenu ctrl_menu_ir_frm_Certificado;
    private javax.swing.JMenu ctrl_menu_ir_frm_carpetas;
    private javax.swing.JMenuItem ctrl_menu_ir_frm_maestro_drivers;
    private javax.swing.JMenuItem ctrl_menu_ir_frm_maestro_sello_tiempo;
    private javax.swing.JMenu ctrl_menu_ir_frm_visor_pdf;
    private javax.swing.JMenu ctrl_menu_sello_tiempo_librerias;
    private javax.swing.JMenuItem ctrl_sub_menu_motivo_locacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblEtiquetaIngresePin1;
    private javax.swing.JLabel lblEtiquetaIngresePin2;
    private javax.swing.JLabel lblEtiquetaIngresePin3;
    public static javax.swing.JLabel lblNotificacion;
    private javax.swing.JMenuItem sub_menu_ir_pkcs11;
    private javax.swing.JMenuItem sub_menu_ir_pkcs12;
    private javax.swing.JMenuItem sub_menu_ir_validaciones;
    private javax.swing.JMenuItem sub_menu_ir_windows;
    public static javax.swing.JTable tblFirmante;
    private javax.swing.JTextField txtPuertoProxy;
    public static javax.swing.JTextPane txtResultado;
    private javax.swing.JTextField txtUrlProxy;
    // End of variables declaration//GEN-END:variables

//    private static boolean fLicenciaEstadoActualizar(java.lang.String pLicenciaSerie, java.lang.String pMaquinaRegistrada) {
//        salmoncorp.com.licencias.WsdlLicenciaValidar_Service service = new salmoncorp.com.licencias.WsdlLicenciaValidar_Service();
//        salmoncorp.com.licencias.WsdlLicenciaValidar port = service.getWsdlLicenciaValidarPort();
//        return port.fLicenciaEstadoActualizar(pLicenciaSerie, pMaquinaRegistrada);
//    }
//
//    private static LicenciaLog fObtenerLicenciaLog() {
//        salmoncorp.com.licencias.WsdlLicenciaValidar_Service service = new salmoncorp.com.licencias.WsdlLicenciaValidar_Service();
//        salmoncorp.com.licencias.WsdlLicenciaValidar port = service.getWsdlLicenciaValidarPort();
//        return port.fObtenerLicenciaLog();
//    }
//
//    private static boolean fLicenciaFechaEstaExpirada(java.lang.String pLicenciaSerie) {
//        salmoncorp.com.licencias.WsdlLicenciaValidar_Service service = new salmoncorp.com.licencias.WsdlLicenciaValidar_Service();
//        salmoncorp.com.licencias.WsdlLicenciaValidar port = service.getWsdlLicenciaValidarPort();
//        return port.fLicenciaFechaEstaExpirada(pLicenciaSerie);
//    }
//
//    private static boolean fLicenciaEstadoDesactivarUso(java.lang.String pLicenciaSerie, java.lang.String pMaquinaRegistrada) {
//        salmoncorp.com.licencias.WsdlLicenciaValidar_Service service = new salmoncorp.com.licencias.WsdlLicenciaValidar_Service();
//        salmoncorp.com.licencias.WsdlLicenciaValidar port = service.getWsdlLicenciaValidarPort();
//        return port.fLicenciaEstadoDesactivarUso(pLicenciaSerie, pMaquinaRegistrada);
//    }
}
