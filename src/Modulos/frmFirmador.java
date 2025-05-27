package Modulos;

import Clases.Libreria;
import Entidades.Configuracion;
import Clases.Repositorio;
import Entidades.Firmante;
import Entidades.So;
import Entidades.helperClass;
import Formularios.frmConfigurador;
import Global.CConstantes;
import Global.ClsCanvasPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.lang.Runnable;

public class frmFirmador extends javax.swing.JFrame implements ActionListener 
{
    String modalidad = Configuracion.getUser();
    private static final long serialVersionUID = 1L;
    protected static Properties properties = new Properties();
    public static Configuracion configuracion;
    public So SistemaOperativo;
    protected static List<String> listaTipoRepositorios;
    public static Repositorio repositorios;
    protected static int pagina;
    private int paginas;
    ClsCanvasPanel cPanel;
    private String rutaPdf;

    private int anchoformulariodefecto;
    private int anchoMinimoformulariodefecto;

    JScrollPane scrollPane;
    
    public static boolean estaAbierto = true;
    protected static So so = new So();
    private float Zoom;
    private int tipoZoom;

    void agrandarFormulario()
    {
        try
        {
            String sentido = lblCambiarTamano.getText();
            String tamano = lblMinimizar2.getText();
            //si esta abierto
            if (sentido.equals(">"))
            {
                try
                {
                    lblCambiarTamano.setText("<");
                    pnlConfiguracion.setVisible(true);
                    
                    //if(tamano.equals("Max"))
                        for (int i = getWidth(); i < anchoformulariodefecto + 50; i += 50)
                        {
                            Thread.sleep(20, 50);

                            if (i >= anchoformulariodefecto)
                            {
                                i = anchoformulariodefecto;
                                setSize(new Dimension(i, getHeight()));
                                i = anchoformulariodefecto + 50;
                            } 
                            else
                                setSize(new Dimension(i, getHeight()));
                        }
                }
                catch (Exception ex) { }
            }
            else
            {
                //Cuando el formulario esta en todo su ancho <
                lblCambiarTamano.setText(">");
                
                if(tamano.equals("Max"))
                {
                    for (int i = getWidth(); i >= anchoMinimoformulariodefecto; i -= 50)
                    {
                        Thread.sleep(20, 50);
                        setSize(new Dimension(i, getHeight()));
                    }

                    if (anchoMinimoformulariodefecto < getWidth())
                        for (int i = getWidth(); i >= anchoMinimoformulariodefecto; i -= 1)
                        {
                            try
                            {
                                Thread.sleep(1, 50);
                                setSize(new Dimension(i, getHeight()));
                            } 
                            catch (Exception ex) {  }
                        }
                }
                                
                pnlConfiguracion.setVisible(false);
            }
            
            setLocationRelativeTo(null);
        } 
        catch (Exception ex)
        {
            CConstantes.dialogo("Exception de cambio de tamaño " + ex.getMessage());
            Logger.getLogger(frmFirmador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void leerConfiguracion(int anchodefecto) throws Exception
    {
        String[] respuesta = leerConfig();
        configuracion = new Configuracion();
        configuracion.setCarpetaPadre(respuesta[0]);
        configuracion.setRutaJdk(respuesta[1]);

        if (!crearCarpetaTemporal(configuracion.getCarpetaPadre()).exists())
            throw new Exception("No se ha creado la carpeta temporal");

        if (!new File(CConstantes.RUTAPROPIEDADES).exists())
            crearArchivoPropiedades();

        Firmante firmante = new Firmante();
        Firmante.Certificado certificado = firmante.new Certificado();
        properties.load(new FileInputStream(new File(CConstantes.RUTAPROPIEDADES)));
               
        Set<String> set = properties.stringPropertyNames();
        Iterator iterator = set.iterator();

        while (iterator.hasNext())
        {
            Object next = iterator.next();
            switch ((String) next)
            {
                case CConstantes.BOLCERRARDOCUMENTO:
                    configuracion.setVg_cerrar_documento(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLTODASPAGINAS:
                    configuracion.setVg_firma_visible_firmar_todas_paginas(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLTSL:
                    configuracion.setVg_validar_con_tsl(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLREPUDIO:
                    configuracion.setVg_validar_con_no_repudio(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLTSA:
                    configuracion.setVg_con_sello(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLVISIBLE:
                    configuracion.setVg_firma_visible(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLCARGO:
                    configuracion.setVg_cargo_visible(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLEMPRESA:
                    configuracion.setVg_cargo_visible(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOLIMAGEN:
                    configuracion.setVg_con_imagen(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.FILRUTACARPETASALIDA:
                    configuracion.setVg_carpeta_salida(properties.getProperty((String) next));
                    break;
                case CConstantes.FILRUTACARPETAENTRADA:
                    configuracion.setVg_carpeta_entrada(properties.getProperty((String) next));
                    break;
                case CConstantes.FILRUTACARPETASALIDATSLCRL:
                    configuracion.setVg_carpeta_salida_crl_tsl(properties.getProperty((String) next));
                    break;
                case CConstantes.FILRUTADOCUMENTO:
                    configuracion.setVg_ruta_documento(properties.getProperty((String) next));
                    break;
                case CConstantes.FILIMAGEN:
                    configuracion.setVg_firma_visible_ruta_imagen(properties.getProperty((String) next));
                    break;
                case CConstantes.FILIMAGENMARCAAGUA:
                    configuracion.setVg_firma_visible_ruta_image_marca_de_agua(properties.getProperty((String) next));
                    break;
                case CConstantes.HSHCLAVEACCESOALSISTEMA:
                    configuracion.setVg_sistema_clave(properties.getProperty((String) next));
                    break;
                case CConstantes.INTTAMANOFUENTE:
                    configuracion.setVg_firma_visible_tamanio_fuente((int) CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.INTPAGINA:
                    configuracion.setVg_firma_visible_pagina(CConstantes.Yconvertiranumero(properties.getProperty((String) next)));
                    break;
                case CConstantes.INTX:
                    configuracion.setVg_firma_visible_x(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.INTY:
                    configuracion.setVg_firma_visible_y(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.FLTANCHO:
                    configuracion.setVg_firma_visible_x1(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.FLTALTO:
                    configuracion.setVg_firma_visible_y1(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                /*case CConstantes.INTXp:
                    configuracion.setVg_firma_visible_x(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.INTYp:
                    configuracion.setVg_firma_visible_y(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.FLTANCHOp:
                    configuracion.setVg_firma_visible_x1(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;
                case CConstantes.FLTALTOp:
                    configuracion.setVg_firma_visible_y1(CConstantes.Yconvertiranumerofloat(properties.getProperty((String) next)));
                    break;*/
                case CConstantes.STRTIPOREPOSITORIO:
                    configuracion.setVg_repositorio_tipo(properties.getProperty((String) next));
                    break;
                case CConstantes.STRFUENTE:
                    configuracion.setVg_firma_visible_fuente(properties.getProperty((String) next));
                    break;

                case CConstantes.STR_BD_SERVIDORES_TSA:
                    configuracion.setVg_cadena_matriz_servicios_sello_tiempo(properties.getProperty((String) next));

                    for (int i = 0; i < cboFuente.getItemCount(); i++)
                        if (configuracion.getVg_firma_visible_fuente().equalsIgnoreCase(cboFuente.getItemAt(i).toString()))
                            cboFuente.setSelectedIndex(i);
                    break;

                case CConstantes.STRALGORITMO:
                    configuracion.setVg_algoritmo(properties.getProperty((String) next));

                    for (int i = 0; i < cboalgoritmo.getItemCount(); i++)
                        if (configuracion.getVg_algoritmo().equalsIgnoreCase(cboalgoritmo.getItemAt(i).toString()))
                            cboalgoritmo.setSelectedIndex(i);
                    break;

                case CConstantes.CERTALIAS:
                    certificado.setAlias(properties.getProperty((String) next));
                    break;
                case CConstantes.CERTFIRMANTE:
                    certificado.setCn(properties.getProperty((String) next));
                    break;
                case CConstantes.CERTCARGO:
                    certificado.setCargo(properties.getProperty((String) next));
                    break;
                case CConstantes.CERTEMPRESA:
                    certificado.setEmpresa(properties.getProperty((String) next));
                    break;
                case CConstantes.CERTMOTIVO:
                    configuracion.setVg_motivo(properties.getProperty((String) next));
                    break;
                case CConstantes.CERTLOCACION:
                    configuracion.setVg_localidad(properties.getProperty((String) next));
                    break;
                case CConstantes.URLTSANOMBRE:
                    configuracion.setVg_tsa_seleccionado(properties.getProperty((String) next));
                    break;
                case CConstantes.URLTSA:
                    configuracion.setVg_tsa_url(properties.getProperty((String) next));
                    break;
                case CConstantes.URLTSAUSUARIO:
                    configuracion.setVg_tsa_usuario(properties.getProperty((String) next));
                    break;
                case CConstantes.HSHCLAVETSA:
                    configuracion.setVg_tsa_clave(properties.getProperty((String) next));
                    break;
//                  case "url.ocsp":txtValidarocsp.setText(properties.getProperty((String) next));break;
                case CConstantes.URLTSL:
                    configuracion.setVg_url_tsl(properties.getProperty((String) next));
                    break;
                case CConstantes.INTESTILO:
                    configuracion.setVg_firma_visible_estilo_firma(Integer.parseInt(properties.getProperty((String) next)));
                    break;
                case "cert.rutacertificado":
                    configuracion.setVg_ruta_certificado(properties.getProperty((String) next));
                    break;
                case CConstantes.CERTNOMBRELIBRERIA:
                    configuracion.setVg_ruta_libreria_nombre(properties.getProperty((String) next));
                    break;
                case "cert.rutalibreria":
                    configuracion.setVg_ruta_libreria(properties.getProperty((String) next));
                    break;
                case "cert.clavecertificado":
                    configuracion.setVg_clave_certificado(properties.getProperty((String) next));
                    break;
                case "cert.clavetoken":
                    configuracion.setVg_clave_token(properties.getProperty((String) next));
                    break;
                case CConstantes.BOLVENTANAVOLVERABRIR:
                    configuracion.setVg_ventana_volver_abrir(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.STR_MATRIZ_TOKEN_DRIVERS:
                    configuracion.setVg_cadena_matriz_token_drivers(properties.getProperty((String) next));
                    break;
                case CConstantes.LICENCIA_KEY:
                    configuracion.setVg_licencia_key(properties.getProperty((String) next));
                    break;
                case CConstantes.STR_EXTENSION:
                    configuracion.setVg_documento_extension_salida(properties.getProperty((String) next));
                    break;
                case CConstantes.BOL_EXTENSION_INI:
                    configuracion.setVg_documento_extension_inicio(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOL_CAL:
                    configuracion.setConNumeroCal(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOL_AREA:
                    configuracion.setConCarmpoArea(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOL_FH:
                    configuracion.setConCarmpoAreaFH(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
                case CConstantes.BOL_QR:
                    configuracion.setConQR(Boolean.parseBoolean(properties.getProperty((String) next)));
                    break;
            }
        }
        
        configuracion.setVg_texto_firmante(properties.containsKey(CConstantes.TextoFirmante) && 
                    properties.getProperty(CConstantes.TextoFirmante) != null 
                    ? properties.getProperty(CConstantes.TextoFirmante) : "");
        
        configuracion.setConCampoEmpresa(properties.containsKey(CConstantes.conCampoEmpresa) && 
                    properties.getProperty(CConstantes.conCampoEmpresa) != null 
                    ? Boolean.parseBoolean(properties.getProperty(CConstantes.conCampoEmpresa)) : true);

        configuracion.setPuertoProxy(properties.containsKey(CConstantes.PROXY_PUERTO) && 
                    properties.getProperty(CConstantes.PROXY_PUERTO) != null 
                    ? properties.getProperty(CConstantes.PROXY_PUERTO) : "");
        
        configuracion.setUrlProxy(properties.containsKey(CConstantes.PROXY_URL) && 
                    properties.getProperty(CConstantes.PROXY_URL) != null 
                    ? properties.getProperty(CConstantes.PROXY_URL) : "");
        
        chkCampoEmpresa.setSelected(configuracion.getConCampoEmpresa());

        pnlConfiguracion.setVisible(false);
        if (configuracion.getVg_carpeta_salida() == null || configuracion.getVg_carpeta_salida().equals(""))
            configuracion.setVg_carpeta_salida(SistemaOperativo.ObtenerCarpetaPersonal() + File.separator + CConstantes.APLICACION_NOMBRE + File.separator + CConstantes.APLICACION_FIRMADOS_CARPETA);

        if (configuracion.getVg_carpeta_salida_crl_tsl() == null || configuracion.getVg_carpeta_salida_crl_tsl().equals(""))
            configuracion.setVg_carpeta_salida_crl_tsl(SistemaOperativo.ObtenerCarpetaPersonal() + File.separator + CConstantes.APLICACION_NOMBRE + File.separator + CConstantes.APLICACION_CACHE_CARPETA);

        configuracion.setFirmante(firmante);
        configuracion.setRepositorioTipos(Repositorio.obtenerTiposRepositorio());
        chkFirmaVisible.setSelected(configuracion.isVg_firma_visible());
        chkConImagen.setSelected(configuracion.isVg_con_imagen());
        txtrutaImagen.setText(configuracion.getVg_firma_visible_ruta_imagen());
        cboEstiloFirma.setSelectedIndex(configuracion.getVg_firma_visible_estilo_firma());
        cboEstiloFirma.setSelectedItem(configuracion.getVg_firma_visible_fuente());
        txtTamanioFuente.setText("" + configuracion.getVg_firma_visible_tamanio_fuente());
        chkCerrarDocumento.setSelected(configuracion.isVg_cerrar_documento());
        chkEnTodasPaginas.setSelected(configuracion.isVg_firma_visible_firmar_todas_paginas());
        chkConSello.setSelected(configuracion.isVg_con_sello());
        txtTsaUrl.setText(configuracion.getVg_tsa_url());
        txtTsaUsuario.setText(configuracion.getVg_tsa_usuario());
        txtTsaClave.setText(new Entidades.helperClass().decrypt(configuracion.getVg_tsa_clave()));
        chkConTsl.setSelected(configuracion.isVg_validar_con_tsl());
        txtTslUrls.setText(configuracion.getVg_url_tsl());
        chkConNoRepudio.setSelected(configuracion.isVg_validar_con_no_repudio());
        chkCalNumber.setSelected(configuracion.getConNumeroCal());
        chkCampoArea.setSelected(configuracion.getConCarmpoArea());        
        chkCampoArea1.setSelected(configuracion.getConCarmpoAreaFH());
        chkIncluirQR.setSelected(configuracion.getConQR());
        txtTextoFIrmante.setText(configuracion.getVg_texto_firmante());

        //Listar la bd de librerias
        String cadenaDrivers = configuracion.getVg_cadena_matriz_token_drivers();
        String drivers[] = cadenaDrivers.length() == 0 ? new String[]{} : cadenaDrivers.split(";");
        List<Libreria> listaDriver = new ArrayList<>();

        for (String driver : drivers)
        {
            String[] driverDato = driver.split(",");
            System.out.println("" + Arrays.toString(driverDato));

            if (driverDato.length != 2)
                continue;

            Libreria libreria = new Libreria(driverDato[0], driverDato[1]);
            listaDriver.add(libreria);
        }
        
        configuracion.setEtiqueta_firma(properties.containsKey(CConstantes.BOL_ETIQUETA) && 
                    properties.getProperty(CConstantes.BOL_ETIQUETA) != null 
                    ? properties.getProperty(CConstantes.BOL_ETIQUETA) : "");
        
        chkAplicarValidaciones.setSelected(!configuracion.getEtiqueta_firma().isEmpty());
        chkEtiqueta.setSelected(!configuracion.getEtiqueta_firma().isEmpty());        
        txtEtiqueta.setText(configuracion.getEtiqueta_firma());
        
        if (!chkAplicarValidaciones.isSelected())
            lyaplicarValidaciones.setVisible(false);
        else
            lyaplicarValidaciones.setVisible(true);
        
        validarEtiquetaFirma();

        configuracion.setLibrerias(listaDriver);
        validarConfiguracion();
        frmFirmador.this.setSize(new Dimension(anchodefecto, getHeight()));
        setLocationRelativeTo(null);
    }

    void validarConfiguracion()
    {
        validarFirmaVisible();
        validarSelloDeTiempo();
        validarTsl();
    }

    void validarFirmaVisible()
    {
        boolean con_firma_visible = chkFirmaVisible.isSelected();
        int estilo_index = cboEstiloFirma.getSelectedIndex();
        
        if (con_firma_visible)
        {
            lyFirmaVisible.setVisible(true);
            cboEstiloFirma.setEnabled(true);
            cboFuente.setEnabled(true);
            chkEnTodasPaginas.setEnabled(true);
            txtTamanioFuente.setEnabled(true);
            
            switch (estilo_index)
            {
                case 0:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(true);
                    break;
                case 1:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(false);
                    break;
                default:
                    chkConImagen.setEnabled(true);
                    chkConImagen.setSelected(true);
                    break;
            }
            //  configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            switch (estilo_index)
            {
                case 0:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(true);
                    break;
                case 1:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(false);
                    break;
                default:
                    chkConImagen.setEnabled(true);
                    chkConImagen.setSelected(true);
                    break;
            }
            /*Solo imagen*/
            if (estilo_index == 0)
            {
                if (chkConImagen.isSelected())
                    btnExaminar.setEnabled(true);
                else
                {
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            
            if (estilo_index == 1)
            {
                if (!chkConImagen.isSelected())
                {
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            } 
            else if (estilo_index == 2)
            {
                if (chkConImagen.isSelected())
                    btnExaminar.setEnabled(true);
                else
                {
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
        } 
        else
        {
            lyFirmaVisible.setVisible(false);
            chkConImagen.setEnabled(false);
            btnExaminar.setEnabled(false);
            cboEstiloFirma.setEnabled(false);
            cboFuente.setEnabled(false);
            txtTamanioFuente.setEnabled(false);
            chkEnTodasPaginas.setSelected(false);
        }
        
        configuracion.setVg_firma_visible(chkFirmaVisible.isSelected());
        configuracion.setVg_firma_visible_estilo_firma(cboEstiloFirma.getSelectedIndex());
        configuracion.setVg_con_imagen(chkConImagen.isSelected());
        configuracion.setVg_firma_visible_ruta_imagen(txtrutaImagen.getText());
        configuracion.setVg_firma_visible_ruta_image_marca_de_agua(txtrutaImagen.getText());
        configuracion.setVg_firma_visible_fuente(cboFuente.getSelectedItem().toString());
        configuracion.setVg_firma_visible_tamanio_fuente(CConstantes.Yconvertiranumerofloat(txtTamanioFuente.getText()));
        configuracion.setVg_firma_visible_firmar_todas_paginas(chkEnTodasPaginas.isSelected());
    }

    void validarSelloDeTiempo()
    {
        if (!chkConSello.isSelected())
        {
            //cboalgoritmo.setSelectedIndex(0);
            txtTsaUrl.setEnabled(false);
            txtTsaUsuario.setEnabled(false);
            txtTsaClave.setEnabled(false);
            cboalgoritmo.setEnabled(false);
            lySelloDeTiempo.setVisible(false);
            configuracion.setVg_tsa_url("");
            configuracion.setVg_tsa_usuario("");
            configuracion.setVg_tsa_clave("");
        }
        else
        {
            txtTsaUrl.requestFocus();
            //cboalgoritmo.setSelectedIndex(1);
            txtTsaUrl.setEnabled(true);
            txtTsaUsuario.setEnabled(true);
            txtTsaClave.setEnabled(true);
            cboalgoritmo.setEnabled(true);
            lySelloDeTiempo.setVisible(true);
            configuracion.setVg_tsa_url(txtTsaUrl.getText());
            configuracion.setVg_tsa_usuario(txtTsaUsuario.getText());
            configuracion.setVg_tsa_clave(new String(txtTsaClave.getPassword()));
           // configuracion.setVg_tsa_clave(new helperClass().encrypt(new String(txtTsaClave.getPassword())));
        }
        configuracion.setVg_con_sello(chkConSello.isSelected());
    }

    void validarTsl()
    {
        if (chkConTsl.isSelected())
        {
            txtTslUrls.setEnabled(true);
            configuracion.setVg_url_tsl(txtTslUrls.getText());
        }
        else
        {
            txtTslUrls.setEnabled(false);
            configuracion.setVg_url_tsl("");
        }
        configuracion.setVg_validar_con_tsl(chkConTsl.isSelected());
    }

    void grabarPropiedad(String key, String valor)
    {
        try (FileOutputStream fos = new FileOutputStream(new File(configuracion.getArchivoPropiedades())))
        {
            properties.setProperty(key, valor);
            properties.store(fos, "SSigner");
            fos.close();
        }
        catch (IOException ex) { }
    }

    private void crearArchivoPropiedades() throws Exception
    {       
        try(FileOutputStream fous = new FileOutputStream(Global.CConstantes.RUTAPROPIEDADES))
        {
            fous.write(Global.CConstantes.ESTRUCTURAPARAMETROS.getBytes());
            fous.close();
        } 
        catch (FileNotFoundException ex) 
        {
            throw new Exception("No se ha logrado ubicar el archivo [" + CConstantes.RUTAPROPIEDADES + "]");
        }
        catch (IOException ex) 
        {
            throw new Exception("No se ha logrado crear el archivo [" + CConstantes.RUTAPROPIEDADES + "]");
        }
    }

    public frmFirmador(String documento, Point inicioArrastre, Point finArrastre) throws IOException, URISyntaxException, Exception
    {
        this(documento, "-u", "", "", inicioArrastre, finArrastre);
    }

    private void inicializarControles()
    {
        lyFirmaVisible.setVisible(false);
        lySelloDeTiempo.setVisible(false);
        lyProtegerArchivos.setVisible(false);
        lyaplicarValidaciones.setVisible(false);
        chkDarConstancia.setVisible(true);
        btnPaginaPrimera.setVisible(false);
        btnPaginaUltima.setVisible(false);
        chkIncluirQR.setVisible(false);
        lblInfo.setText(null);
    }

    private void aplicarEstilos()
    {
        anchoMinimoformulariodefecto = 465;
        cPanel = new ClsCanvasPanel();
        anchoformulariodefecto = getWidth();
    }
    
    public frmFirmador(String documento, String modalidad, String carpetaEntrada, String carpetaDestino, Point inicioArrastre, Point finArrastre) 
    {
        initComponents();
        
        SistemaOperativo = new So();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        chkCalNumber.setVisible(false);
        chkCampoArea.setVisible(true);
        chkCampoArea1.setVisible(true);
        btnZoomIn.setVisible(true);
        btnZoomOut.setVisible(true);
        scrollPane = new JScrollPane(cPanel);
        pnlPrincipal.add(scrollPane);

        try 
        {
            inicializarControles();
            aplicarEstilos();
            leerConfiguracion(anchoMinimoformulariodefecto);
            Zoom = 1;
            tipoZoom = 1;
            
            this.modalidad = modalidad;
            configuracion.setModalidad(this.modalidad);
            repositorios = new Repositorio();
            cPanel = new ClsCanvasPanel();
            if (configuracion.getModalidad().equals(Configuracion.getServer()))
            {
                dgvDocumentos.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
                    @Override
                    public void valueChanged(ListSelectionEvent event)
                    {
                        if (dgvDocumentos.getSelectedRow() > -1)
                        {
                            lblTitulo.setText(dgvDocumentos.getValueAt(dgvDocumentos.getSelectedRow(), 1).toString());
                            lblTitulo.setToolTipText(lblTitulo.getText());
                        }
                    }
                });
                
                btnPaginaSiguiente.addActionListener(frmFirmador.this);
                btnPaginaAnterior.addActionListener(frmFirmador.this);
                btnPaginaPrimera.addActionListener(frmFirmador.this);
                btnPaginaUltima.addActionListener(frmFirmador.this);
                btnGrabarCambios.addActionListener(frmFirmador.this);
                btnCargar.setVisible(false);
                lblInfo.setVisible(false);
                
                btnPaginaSiguiente.setVisible(false);
                btnPaginaAnterior.setVisible(false);
                btnPaginaPrimera.setVisible(false);
                btnPaginaUltima.setVisible(false);
                cboPaginaActual.setVisible(false);
                lblPaginaHasta.setVisible(false);
                
                capaTotalArchivosListados.setVisible(true);
                pnlCoordenadas.setVisible(false);
                capaTotalArchivosErrados.setVisible(true);
                capaTotalArchivosFirmados.setVisible(true);
                lblPagina.setVisible(true);
                txtPagina.setVisible(true);
                txtPagina.setText(String.valueOf(configuracion.getVg_firma_visible_pagina()));
            }
            else
            {
                pnlPrincipal.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseMoved(java.awt.event.MouseEvent evt)
                    {
                        mousemove(evt);
                    }

                    @Override
                    public void mouseReleased(MouseEvent evtEvent)
                    {
                        mousereleased(evtEvent);
                    }
                });
                
                pnlPrincipal.addMouseMotionListener(new MouseMotionAdapter()
                {
                    @Override
                    public void mouseDragged(MouseEvent evtEvent)
                    {
                        mousedragged(evtEvent);
                    }
                });
                
                pnlPrincipal.setVisible(true);
                cPanel.setBounds(0, 0, pnlPrincipal.getWidth(), pnlPrincipal.getHeight());
                cPanel.setVisible(true);
                cPanel.setBackground(new Color(204, 204, 255));
                pnlPrincipal.add(cPanel);
                pnlPrincipal.setAutoscrolls(true);
               
                btnPaginaSiguiente.addActionListener(frmFirmador.this);
                btnPaginaAnterior.addActionListener(frmFirmador.this);
                btnPaginaPrimera.addActionListener(frmFirmador.this);
                btnPaginaUltima.addActionListener(frmFirmador.this);
                btnGrabarCambios.addActionListener(frmFirmador.this);
                
                btnPaginaSiguiente.setVisible(true);
                btnPaginaAnterior.setVisible(true);
                btnPaginaPrimera.setVisible(true);
                btnPaginaUltima.setVisible(true);
                cboPaginaActual.setVisible(true);
                lblPaginaHasta.setVisible(true);
                
                btnCargarMasivo.setVisible(false);
                btnFirmarMasivamente.setVisible(false);
                btnBorrarTabla.setVisible(false);
                tbDocumentosFirmar.setVisible(false);
                capaTotalArchivosListados.setVisible(false);
                capaTotalArchivosErrados.setVisible(false);
                capaTotalArchivosFirmados.setVisible(false);
                pnlCoordenadas.setVisible(true);
                btnAbrirCarpetaFirmados.setVisible(false);
                txtEtiqueta.setEnabled(false);
                configuracion.setEtiqueta_firma("");
                lblPagina.setVisible(false);
                txtPagina.setVisible(false);
                //minimizarVentana();
            }

            if (configuracion.getVg_algoritmo().contains("256"))
                cboalgoritmo.setSelectedIndex(1);
            else
                cboalgoritmo.setSelectedIndex(0);  
        }
        catch (Exception ex)
        {
            CConstantes.dialogo("Exception " + ex.getMessage());
        }
    }
    
    private void resetPdfPreview()
    {
        Zoom = 1;
        tipoZoom = 1;
        
        cPanel.clearPanel();
        cPanel.setPDF(rutaPdf);
        
        if (pagina > 0)
            cPanel.setPage(pagina - 1, Zoom);
        else
            cPanel.setPage(0, Zoom);
        
        repaint();
    }

    private File crearCarpetaTemporal(String ruta_origen)
    {
        new File(ruta_origen + "temporal" + File.separator).mkdirs();
        return new File(ruta_origen + "temporal" + File.separator);
    }

    private void limpiarTemporales(File[] archivosaeliminar)
    {
        for (File archivosaeliminar1 : archivosaeliminar)
            archivosaeliminar1.delete();
    }
    
    private void minimizarVentana()
    {
        try
        {
            this.setExtendedState(NORMAL);
            
            javax.swing.GroupLayout canvasPrincipalLayout = new javax.swing.GroupLayout(canvasPrincipal);
            canvasPrincipal.setLayout(canvasPrincipalLayout);
            //jspane.setViewportView(pnlPrincipal);
            canvasPrincipalLayout.setHorizontalGroup(
                canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, canvasPrincipalLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        
                        .addComponent(pnlPaginas)
                        .addComponent(pnlPrincipal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                        .addComponent(pnlCoordenadas))
                    .addGap(0, 0, 0)
                    .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            
            canvasPrincipalLayout.setVerticalGroup(
                canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(canvasPrincipalLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(canvasPrincipalLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(canvasPrincipalLayout.createSequentialGroup()
                                    .addComponent(pnlPaginas)
                                    .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(3, 3, 3)
                                    .addComponent(pnlCoordenadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    )
                                .addGroup(canvasPrincipalLayout.createSequentialGroup()
                                    .addGap(163, 163, 163)
                                    .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addContainerGap())
            );
            
            pnlPrincipal.setBounds(0, 0, 409, 500);
            cPanel.setBounds(0, 0, 409, 500);

            try
            {
                if (cPanel.getAnchoReal(pagina - 1) > cPanel.getAltoReal(pagina - 1) || 
                    cPanel.getRotacionPagina(pagina - 1) == 90 || cPanel.getRotacionPagina(pagina - 1) == 270)
                {
                    double minfactor = (double)cPanel.getAltoReal(pagina - 1) / (double)cPanel.getAnchoReal(pagina - 1);

                    if(cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
                    {
                        cPanel.setBounds(0, 0, (int)(cPanel.getHeight() / minfactor), cPanel.getHeight());
                        cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() / minfactor), cPanel.getHeight()));
                    }
                    else
                    {
                        cPanel.setBounds(0, 0, (int)(cPanel.getHeight() * minfactor), cPanel.getHeight());
                        cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() * minfactor), cPanel.getHeight()));
                    }
                }
            }
            catch(Exception e) { }
            
            setScrollPane();
            lblMinimizar2.setText("Max");
            resetPdfPreview();
            ZoomDocumento(1, 1);
            repaint();
        }
        catch(Exception ex)
        {
            CConstantes.dialogo("Exception " + ex.getMessage());
        }
    }

    private void viewPage()
    {
        setScrollPane();
        cPanel.setPage(pagina - 1, Zoom);        
        cboPaginaActual.setSelectedItem("" + pagina);
        repaint();
    }

    void cargarDocumento(final String archivoConfigurado, final JButton btn)
    {
        configuracion.setModalidad(Configuracion.getUser());

        final JFileChooser elegir = new JFileChooser(configuracion.getVg_ruta_documento());
        FilenameFilter fileNameFilter = (File dir, String name1) ->
        {
            dispose();
            if (name1.lastIndexOf('.') > 0)
            {
                int lastIndex = name1.lastIndexOf('.');
                String str = name1.substring(lastIndex);

                if (str.equalsIgnoreCase(".pdf"))
                    return true;
            }
            return false;
        };

        elegir.setFileSelectionMode(JFileChooser.FILES_ONLY);
        elegir.setAcceptAllFileFilterUsed(false);
        elegir.addChoosableFileFilter(new FileNameExtensionFilter("Documentos PDF ", "pdf"));
        elegir.setMultiSelectionEnabled(true);
        elegir.setDialogTitle("Abrir un documento para su visualización");

        try
        {
            int select;// = elegir.showOpenDialog(Documento.this);
            if (archivoConfigurado.equals(""))
            {
                select = elegir.showOpenDialog(frmFirmador.this);
                if (select == JFileChooser.APPROVE_OPTION)
                {
                    //txtPaginaHasta.setEnabled(true);
                    btnPaginaPrimera.setEnabled(true);
                    btnPaginaSiguiente.setEnabled(true);
                    btnPaginaAnterior.setEnabled(true);
                    btnPaginaUltima.setEnabled(true);
                    btnPaginaPrimera.setEnabled(true);

                    /*if (pnlPrincipal.getComponentCount() > 0)
                        pnlPrincipal.remove(0);*/
                }
                else
                {
                    lblInfo.setText("CARGUE UN DOCUMENTO PARA VISUALIZARLO");
                    btnCargar.setText("ARCHIVO");
                    return;
                }
            }
            else
            {
                lblInfo.setText(null);
                btnCargar.setText("ARCHIVO");
            }

            List<String> listarutas = new ArrayList<>();
            File[] archivos = (!archivoConfigurado.equals("") ? new File[]{new File(archivoConfigurado)} : elegir.getSelectedFiles());

            lblTitulo.setText("Cargando documento....");

            chkDarConstancia.setText("Doy constancia de haber visto todos los " + archivos.length + " documentos.");
            chkDarConstancia.setToolTipText(chkDarConstancia.getText());
            chkDarConstancia.setVisible(true);

            for (File archivo : archivos)
            {
                if (archivo.isDirectory())
                {
                    File[] archivocarpeta = archivo.listFiles(fileNameFilter);
                    for (File archivo1 : archivocarpeta)
                        if (archivo1.isFile())
                            listarutas.add(archivo1.getAbsolutePath());
                }
                else
                    listarutas.add(archivo.getAbsolutePath());
            }

            btnCargar.setText("ARCHIVO");
            lblTitulo.setText("" + listarutas.size() + " DOCUMENTO(S) PARA FIRMAR");
            configuracion.setDocumentoUltimo(listarutas.get(0));
            String nombreArchivo;

            if (configuracion.getDocumentoUltimo().length() > 0)
            {
                nombreArchivo = new File(configuracion.getDocumentoUltimo()).getName();
                lblTitulo.setText(nombreArchivo);
                lblTitulo.setToolTipText(nombreArchivo);
                
                new Thread(() ->
                {
                    try(FileOutputStream out = new FileOutputStream(new File(configuracion.getCarpetaPadre() + "Temporal" + File.separator + nombreArchivo), true);
                        RandomAccessFile raf1 = new RandomAccessFile(configuracion.getDocumentoUltimo(), "r"))
                    {
                        int indice = 0;
                        byte[] bytes = new byte[2048];
                        int leidos = raf1.read(bytes);

                        while (leidos > 0)
                        {
                            raf1.read(bytes, 0, indice);
                            out.write(bytes);
                            leidos = raf1.read(bytes);
                        }
                        raf1.close();
                        out.close();
                    }
                    catch(Exception e) {}
                }).start();
            }
            else
                return;

            //pnlPrincipal.remove(scrollPane);
            //scrollPane = new JScrollPane(cPanel);
            //pnlPrincipal.add(scrollPane);
            
            File file = new File(configuracion.getDocumentoUltimo());
            File file1 = new File(listarutas.get(0));
            String tamano = lblMinimizar2.getText();
            
            if(tamano.equalsIgnoreCase("Normal"))
            {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double wt = screenSize.width/1.6;
                double ht = screenSize.height/1.18;
                pnlPrincipal.setBounds(0, 0, (int)wt, (int)ht);
                cPanel.setBounds(0, 0, (int)wt, (int)ht);
                cPanel.clearPanel();
                cPanel.setPreferredSize(new Dimension((int)wt, (int)ht));
            }
            else
            {
                cPanel.setBounds(0, 0, pnlPrincipal.getWidth(), pnlPrincipal.getHeight());
                cPanel.clearPanel();
                cPanel.setPreferredSize(new Dimension(pnlPrincipal.getWidth(), pnlPrincipal.getHeight()));
            }
            
            setScrollPane();
            
            configuracion.setVg_documento_nombre(file.getName());
            configuracion.setVg_ruta_documento(file1.getAbsolutePath());
            configuracion.setRutasdocumentos(listarutas);
            
            rutaPdf = configuracion.getDocumentoUltimo();
            
            cPanel.setPDF(configuracion.getDocumentoUltimo());
            paginas = cPanel.getNumPages();
            pagina = 1;
            cboPaginaActual.removeAllItems();

            for (int i = 1; i <= paginas; i++)
                cboPaginaActual.addItem(i);

            lblPaginaHasta.setText(" /" + paginas);
            grabarPropiedad(CConstantes.FILRUTADOCUMENTO, configuracion.getVg_ruta_documento());
            
            if(!tamano.equalsIgnoreCase("Normal"))
            {
                if (cPanel.getAnchoReal(pagina - 1) > cPanel.getAltoReal(pagina - 1) || 
                    cPanel.getRotacionPagina(pagina - 1) == 90 || cPanel.getRotacionPagina(pagina - 1) == 270)
                {
                    double minfactor = (double)cPanel.getAltoReal(pagina - 1) / (double)cPanel.getAnchoReal(pagina - 1);

                    if(cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
                    {
                        cPanel.setBounds(0, 0, (int)(cPanel.getHeight() / minfactor), cPanel.getHeight());
                        cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() / minfactor), cPanel.getHeight()));
                    }
                    else
                    {
                        cPanel.setBounds(0, 0, (int)(cPanel.getHeight() * minfactor), cPanel.getHeight());
                        cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() * minfactor), cPanel.getHeight()));
                    }
                }
            }
            else
            {
                if (cPanel.getAnchoReal(pagina - 1) < cPanel.getAltoReal(pagina - 1) || 
                    cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
                {
                    double minfactor = 1.6 * (double)cPanel.getAltoReal(pagina - 1) / (double)cPanel.getAnchoReal(pagina - 1);

                    if(cPanel.getRotacionPagina(pagina - 1) == 90 || cPanel.getRotacionPagina(pagina - 1) == 270)
                    {
                        cPanel.setBounds(0, 0, (int)(cPanel.getHeight() * minfactor), cPanel.getHeight());
                        cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() * minfactor), cPanel.getHeight()));
                    }
                    else
                    {
                        cPanel.setBounds(0, 0, (int)(cPanel.getHeight() / minfactor), cPanel.getHeight());
                        cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() / minfactor), cPanel.getHeight()));
                    }
                }
            }

            resetPdfPreview();
            ZoomDocumento(1, 2);
            repaint();            

            limpiarTemporales(new File(configuracion.getCarpetaPadre() + "Temporal").listFiles());
            
            configuracion.setRutasdocumentos(listarutas);

            if (!configuracion.isVg_firma_visible()) 
                new frmElegirRepositorio(frmFirmador.this, true, configuracion.getVg_clave_token(), "" + configuracion.getVg_firma_visible_pagina(), configuracion.getVg_ruta_documento()).setVisible(true);
        }
        catch (Exception e)
        {
            if (e.getMessage().equalsIgnoreCase("map failed"))
            {
                CConstantes.dialogo("El tamaño del archivo es demasiado grande. Elija otro por favor");
                lblTitulo.setText("");
            }
            else
                CConstantes.dialogo(e.getMessage());
        }

        Zoom = 1;
        tipoZoom = 1;
    }

    class CELL_RENDERER implements TableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable arg0,
                Object arg1, boolean arg2, boolean arg3, int arg4, int arg5)
        {
            return (JProgressBar) arg1;
        }
    }

    List<String> importarArchivos(JTable tabla, Configuracion configuracion) throws IOException {
        return importarArchivos(0, tabla, configuracion);
    }

    List<String> importarArchivos(int flag, JTable tabla, Configuracion configuracion) throws IOException {
        return importarArchivos(0, null, tabla, configuracion);
    }

    List<String> importarArchivos(int flag, File[] archivo_elegido, JTable tabla, Configuracion configuracion) throws IOException {
        return importarArchivos(0, null, "", tabla, configuracion);
    }

    List<String> importarArchivos(int flag, File[] archivo_elegido, String archivoDefecto, JTable tabla, Configuracion configuracion) throws IOException
    {
        List<String> archivador = configuracion.getRutasdocumentos() == null ? new ArrayList<String>() : configuracion.getRutasdocumentos();
        DefaultTableCellRenderer v_celda_renderizar_documento = new DefaultTableCellRenderer();
        DefaultTableCellRenderer v_celda_renderizar_estado = new DefaultTableCellRenderer();
        DefaultTableCellRenderer v_celda_renderizar_eliminar = new DefaultTableCellRenderer();
        JFileChooser v_frm_archivos_elegir = null;
        int v_opcion_seleccionar = 0;
        /*Para abrir la ventana que lista los archivos*/
        if (flag == 0)
        {
            v_frm_archivos_elegir = new JFileChooser(configuracion.getVg_ruta_documento());
            v_frm_archivos_elegir.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//0 = > solo archivos
//            v_frm_archivos_elegir.setFileSelectionMode(JFileChooser.FILES_ONLY);
            v_frm_archivos_elegir.setAcceptAllFileFilterUsed(false);
            // v_frm_archivos_elegir.addChoosableFileFilter(new FileNameExtensionFilter("Archivos a firmar", new String[] { "*.*" }));
            v_frm_archivos_elegir.setDialogTitle("Seleccionar el/los archivo(s) o la carpeta de entrada de documentos a firmar");
            v_frm_archivos_elegir.setMultiSelectionEnabled(true);
            v_opcion_seleccionar = v_frm_archivos_elegir.showOpenDialog(frmFirmador.this);
        }
        
        /*Para listar los archivos arrastrados*/
        if (flag == 1)
        {
            txtProcesoListado.setText("");
            File f = new File(".");
            String laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
            String icon_ruta_error = laRuta + "icono_error.png";
            String icon_ruta_firmado = laRuta + "icono_firmado.png";
            ImageIcon img_error = new ImageIcon(icon_ruta_error);
            ImageIcon img_firmado = new ImageIcon(icon_ruta_firmado);
            JProgressBar pb_progreso = new JProgressBar();
            File[] v_archivos_seleccionados = archivo_elegido;
            DefaultTableModel v_tabla_modelo = (DefaultTableModel) tabla.getModel();

            tabla.getColumnModel().getColumn(3).setWidth(tabla.getColumnModel().getColumn(2).getWidth());
            tabla.getColumnModel().getColumn(3).setMaxWidth(tabla.getColumnModel().getColumn(2).getWidth());
            tabla.getColumnModel().getColumn(3).setMinWidth(tabla.getColumnModel().getColumn(2).getWidth());
            tabla.getColumnModel().getColumn(3).setPreferredWidth(tabla.getColumnModel().getColumn(2).getWidth());

            tabla.getColumnModel().getColumn(2).setWidth(0);
            tabla.getColumnModel().getColumn(2).setMaxWidth(0);
            tabla.getColumnModel().getColumn(2).setMinWidth(0);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(0);

            for (File vi_archivo_seleccionado : v_archivos_seleccionados)
                archivos_listar_recusivamente(vi_archivo_seleccionado, v_tabla_modelo, archivador);
            
            for (int i = 0; i < v_tabla_modelo.getRowCount(); i++)
                v_tabla_modelo.setValueAt(i + 1, i, 0);
            
//                tabla.setModel(v_tabla_modelo);
            TableColumn tc = tabla.getColumnModel().getColumn(1);
            tc.setCellRenderer(v_celda_renderizar_documento);
//                v_celda_renderizar_estado.setIcon(img_firmado);
//                v_celda_renderizar_estado.setHorizontalAlignment(0);
//                tabla.getColumnModel().getColumn(2).setCellRenderer(v_celda_renderizar_estado);
            tabla.getColumnModel().getColumn(2).setCellRenderer(new CELL_RENDERER());
            v_celda_renderizar_eliminar.setIcon(img_error);
            v_celda_renderizar_eliminar.setHorizontalAlignment(0);
            tabla.getColumnModel().getColumn(4).setCellRenderer(v_celda_renderizar_eliminar);

            tabla.getColumnModel().getColumn(2).setWidth(tabla.getColumnModel().getColumn(3).getWidth());
            tabla.getColumnModel().getColumn(2).setMaxWidth(tabla.getColumnModel().getColumn(3).getWidth());
            tabla.getColumnModel().getColumn(2).setMinWidth(tabla.getColumnModel().getColumn(3).getWidth());
            tabla.getColumnModel().getColumn(2).setPreferredWidth(tabla.getColumnModel().getColumn(3).getWidth());

            tabla.getColumnModel().getColumn(3).setWidth(0);
            tabla.getColumnModel().getColumn(3).setMaxWidth(0);
            tabla.getColumnModel().getColumn(3).setMinWidth(0);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(0);

            tabla.repaint();
        }
        
        /*Selecciona aceptar*/
        if (v_opcion_seleccionar == 0)
        {
            txtProcesoListado.setText("");
            File f = new File(".");
            String laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
            String icon_ruta_error = laRuta + "icono_error.png";
            String icon_ruta_firmado = laRuta + "icono_firmado.png";
            ImageIcon img_error = new ImageIcon(icon_ruta_error);
            ImageIcon img_firmado = new ImageIcon(icon_ruta_firmado);
            File[] v_archivos_seleccionados = v_frm_archivos_elegir.getSelectedFiles();
            DefaultTableModel v_tabla_modelo = (DefaultTableModel) tabla.getModel();

            tabla.getColumnModel().getColumn(3).setWidth(tabla.getColumnModel().getColumn(2).getWidth());
            tabla.getColumnModel().getColumn(3).setMaxWidth(tabla.getColumnModel().getColumn(2).getWidth());
            tabla.getColumnModel().getColumn(3).setMinWidth(tabla.getColumnModel().getColumn(2).getWidth());
            tabla.getColumnModel().getColumn(3).setPreferredWidth(tabla.getColumnModel().getColumn(2).getWidth());

            tabla.getColumnModel().getColumn(2).setWidth(0);
            tabla.getColumnModel().getColumn(2).setMaxWidth(0);
            tabla.getColumnModel().getColumn(2).setMinWidth(0);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(0);

            for (File vi_archivo_seleccionado : v_archivos_seleccionados)
                archivos_listar_recusivamente(vi_archivo_seleccionado, v_tabla_modelo, archivador);
            
            //Repaso de nuevo la tabla para ponerle la columna de items
            for (int i = 0; i < v_tabla_modelo.getRowCount(); i++)
                v_tabla_modelo.setValueAt(i + 1, i, 0);

            tabla.getColumnModel().getColumn(2).setCellRenderer(new CELL_RENDERER());
            v_celda_renderizar_eliminar.setIcon(img_error);
            v_celda_renderizar_eliminar.setHorizontalAlignment(0);
            tabla.getColumnModel().getColumn(4).setCellRenderer(v_celda_renderizar_eliminar);

            tabla.getColumnModel().getColumn(2).setWidth(tabla.getColumnModel().getColumn(3).getWidth());
            tabla.getColumnModel().getColumn(2).setMaxWidth(tabla.getColumnModel().getColumn(3).getWidth());
            tabla.getColumnModel().getColumn(2).setMinWidth(tabla.getColumnModel().getColumn(3).getWidth());
            tabla.getColumnModel().getColumn(2).setPreferredWidth(tabla.getColumnModel().getColumn(3).getWidth());

            tabla.getColumnModel().getColumn(3).setWidth(0);
            tabla.getColumnModel().getColumn(3).setMaxWidth(0);
            tabla.getColumnModel().getColumn(3).setMinWidth(0);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(0);

            tabla.repaint();
        }

        return archivador;
    }

    public static void archivos_listar_recusivamente(File p_carpeta_ruta_origen, DefaultTableModel v_tabla_modelo, List<String> archivador) throws IOException
    {
        if (p_carpeta_ruta_origen.isDirectory())
        {
            File[] archivos_rutas = p_carpeta_ruta_origen.listFiles();
            
            for (File archivos_ruta : archivos_rutas)
            {
                if (archivos_ruta.isDirectory())
                {
                    archivos_listar_recusivamente(archivos_ruta, v_tabla_modelo, archivador);
                } 
                else
                {
                    Object[] fila = new Object[5];
                    boolean v_fila_archivo_existe = false;
                    for (int i = 0; i < v_tabla_modelo.getRowCount(); i++)
                    {
                        //  String vi_archivo_seleccionado_nombre = archivos_ruta.getName();
                        File vi_tabla_fila_archivo_nombre = new File(v_tabla_modelo.getValueAt(i, 1).toString());
                        if (archivos_ruta.getPath().equals(vi_tabla_fila_archivo_nombre.getPath()))
                        {
                            v_fila_archivo_existe = true;
                            break;
                        }
                    }
                    
                    if (!v_fila_archivo_existe)
                    {
                        fila[0] = 0;
                        fila[1] = archivos_ruta;
                        //fila[2] = "Pendiente";
                        fila[2] = new JProgressBar();
                        fila[3] = "Procesando";
                        fila[4] = "Eliminar";
                        v_tabla_modelo.addRow(fila);
                        archivador.add(archivos_ruta.getAbsolutePath());
                        contarListados();
                        txtProcesoListado.setText(txtProcesoListado.getText() + "\n" + archivos_ruta.getPath());
                    }
                }
            }
        } 
        else 
        {

            Object[] fila = new Object[5];
            boolean v_fila_archivo_existe = false;
            
            for (int i = 0; i < v_tabla_modelo.getRowCount(); i++)
            {
                //  String vi_archivo_seleccionado_nombre = archivos_ruta.getName();
                File vi_tabla_fila_archivo_nombre = new File(v_tabla_modelo.getValueAt(i, 1).toString());
                if (p_carpeta_ruta_origen.getPath().equals(vi_tabla_fila_archivo_nombre.getPath()))
                {
                    v_fila_archivo_existe = true;
                    break;
                }
            }
            
            if (!v_fila_archivo_existe)
            {
                fila[0] = 0;
                fila[1] = p_carpeta_ruta_origen;
                fila[2] = new JProgressBar();
                fila[3] = "Procesando";
                fila[4] = "Eliminar";
                v_tabla_modelo.addRow(fila);
                archivador.add(p_carpeta_ruta_origen.getAbsolutePath());
                contarListados();
                txtProcesoListado.setText(txtProcesoListado.getText() + "\n" + p_carpeta_ruta_origen.getPath());
            }
        }
    }

    public void keyPressed(KeyEvent el)
    {
        if (el.getKeyCode() == KeyEvent.VK_A)
            cPanel.clearPanel();
    }
    
    public void mousemove(MouseEvent e)
    {
        cPanel.setOffSetY(scrollPane.getVerticalScrollBar().getValue());
        cPanel.setOffSetX(scrollPane.getHorizontalScrollBar().getValue());
        cPanel.mouseMoved(e);
    }
    
    private void setConfigValues(Rectangle rect)
    {
        float y22, x22;
        float x11 = (float)rect.getX();
        float y11 = (float)rect.getY();

        float ancho0 = (float)rect.getWidth();
        float alto0 = (float)rect.getHeight();

        if(cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
        {
            x11 = x11 * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth();
            y11 = y11 * cPanel.getAltoReal(pagina - 1) / cPanel.getHeight();
            y11 = cPanel.getAltoReal(pagina - 1) - y11 - alto0 * (cPanel.getAltoReal(pagina - 1)) / cPanel.getHeight();

            y22 = alto0 * cPanel.getAltoReal(pagina - 1) / cPanel.getHeight();
            x22 = ancho0 * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth();
            x11 += (scrollPane.getHorizontalScrollBar().getValue() * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth());
            y11 -= (scrollPane.getVerticalScrollBar().getValue() * (cPanel.getAltoReal(pagina - 1)) / cPanel.getHeight());
        }
        else
        {
            x11 = x11 * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth();
            y11 = y11 * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight();
            y11 = cPanel.getAnchoReal(pagina - 1) - y11  - alto0 * (cPanel.getAnchoReal(pagina - 1))/ cPanel.getHeight();

            y22 = alto0 * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight();
            x22 = ancho0 * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth();
            x11 += (scrollPane.getHorizontalScrollBar().getValue() * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth());
            y11 -= (scrollPane.getVerticalScrollBar().getValue() * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight());
        }

        y22 += y11;
        x22 += x11;

        configuracion.setVg_firma_visible_ancho_base(cPanel.getAnchoReal(pagina - 1));
        configuracion.setVg_firma_visible_alto_base(cPanel.getAltoReal(pagina - 1));
        configuracion.setVg_firma_visible_x(x11);
        configuracion.setVg_firma_visible_y(y11);
        configuracion.setVg_firma_visible_x1(x22);
        configuracion.setVg_firma_visible_y1(y22);
    }
   
    public void mousereleased(MouseEvent e)
    {
        cPanel.setOffSetY(scrollPane.getVerticalScrollBar().getValue());
        cPanel.setOffSetX(scrollPane.getHorizontalScrollBar().getValue());
        cPanel.mouseReleased(e);
        
        if (cPanel.getRectangleWidth() < 10 || cPanel.getRectangleHeight() < 10)
        {
            lblX.setText("x : " + cPanel.getCoordenadas()[0].x + "");
            lblY.setText("y : " + cPanel.getCoordenadas()[0].y + "");
            lblAncho.setText("Ancho : " + cPanel.getRectangleWidth() + "");
            lblAlto.setText("Alto : " + cPanel.getRectangleHeight() + "");
            configuracion.setVg_firma_visible_x(0);
            configuracion.setVg_firma_visible_y(0);
            configuracion.setVg_firma_visible_x1(0);
            configuracion.setVg_firma_visible_y1(0);
            //grabarPropiedad(CConstantes.INTX, "" + configuracion.getVg_firma_visible_x());
            //grabarPropiedad(CConstantes.INTY, "" + configuracion.getVg_firma_visible_y());
            //grabarPropiedad(CConstantes.FLTANCHO, "" + configuracion.getVg_firma_visible_x1());
            //grabarPropiedad(CConstantes.FLTALTO, "" + configuracion.getVg_firma_visible_y1());
        }
        else
        {
            if (!validarDatos())
            {
                String sentido = lblCambiarTamano.getText();
                if (sentido.equals("<"))
                    return;
                
                agrandarFormulario();
                return;
            }

            configuracion.setVg_firma_visible_pagina(pagina);
            configuracion.setConCampoEmpresa(chkCampoEmpresa.isSelected());
            setConfigValues(cPanel.getRectangle());
            
            lblX.setText("x : " + cPanel.getCoordenadas()[0].x + "");
            lblY.setText("y : " + cPanel.getCoordenadas()[0].y + "");
            lblAncho.setText("Ancho : " + cPanel.getRectangleWidth() + "");
            lblAlto.setText("Alto : " + cPanel.getRectangleHeight() + "");
            
            //grabarPropiedad(CConstantes.INTX, "" + configuracion.getVg_firma_visible_x());
            //grabarPropiedad(CConstantes.INTY, "" + configuracion.getVg_firma_visible_y());
            //grabarPropiedad(CConstantes.FLTANCHO, "" + configuracion.getVg_firma_visible_x1());
            //grabarPropiedad(CConstantes.FLTALTO, "" + configuracion.getVg_firma_visible_y1());
            
            if (chkDarConstancia.isVisible())
            {
                /*SI PRESIONA OK PUEDE FIRMAR TODO.sS*/
                /*int total = configuracion.getRutasdocumentos().size() - 1;
                    for (int i = total; i >= 1; i--)
                        configuracion.getRutasdocumentos().remove(i);*/
                    
                lblTitulo.setText("");
                
                if (!chkDarConstancia.isSelected())
                {
                    if (preguntarDarConstancia() == javax.swing.JOptionPane.YES_OPTION)
                    {
                        new Thread(() ->
                        {
                            new frmElegirRepositorio(frmFirmador.this, true, configuracion.getVg_clave_token(), "" + configuracion.getVg_firma_visible_pagina(), configuracion.getVg_ruta_documento()).setVisible(true);
                        }).start();
                    }
                }
                else
                {
                    new Thread(() ->
                    {
                        new frmElegirRepositorio(frmFirmador.this, true, configuracion.getVg_clave_token(), "" + configuracion.getVg_firma_visible_pagina(), configuracion.getVg_ruta_documento()).setVisible(true);
                    }).start();
                }
            }
            else
            {
                new Thread(() ->
                {
                    new frmElegirRepositorio(frmFirmador.this, true, configuracion.getVg_clave_token(), "" + configuracion.getVg_firma_visible_pagina(), configuracion.getVg_ruta_documento()).setVisible(true);
                }).start();
            }
        }
    }

    public void mousedragged(MouseEvent e)
    {
        cPanel.setOffSetY(scrollPane.getVerticalScrollBar().getValue());
        cPanel.setOffSetX(scrollPane.getHorizontalScrollBar().getValue());
        cPanel.mouseDragged(e);
        lblX.setText("x : " + cPanel.getCoordenadas()[0].x + "");
        lblY.setText("y : " + cPanel.getCoordenadas()[0].y + "");
        lblAncho.setText("Ancho : " + cPanel.getRectangleWidth() + "");
        lblAlto.setText("Alto : " + cPanel.getRectangleHeight() + "");
    }

    private void guardarCambios() throws Exception
    {
        try(FileOutputStream foss = new FileOutputStream(new File(CConstantes.RUTAPROPIEDADES)))
        {
            properties.setProperty(CConstantes.BOLVISIBLE, "" + configuracion.isVg_firma_visible());
            properties.setProperty(CConstantes.INTESTILO, "" + configuracion.getVg_firma_visible_estilo_firma());
            properties.setProperty(CConstantes.BOLIMAGEN, "" + configuracion.isVg_con_imagen());
            properties.setProperty(CConstantes.FILIMAGEN, "" + configuracion.getVg_firma_visible_ruta_imagen());
            properties.setProperty(CConstantes.FILIMAGENMARCAAGUA, "" + configuracion.getVg_firma_visible_ruta_image_marca_de_agua());
            properties.setProperty(CConstantes.STRFUENTE, "" + configuracion.getVg_firma_visible_fuente());
            properties.setProperty(CConstantes.INTTAMANOFUENTE, "" + configuracion.getVg_firma_visible_tamanio_fuente());
            properties.setProperty(CConstantes.BOLTODASPAGINAS, "" + configuracion.isVg_firma_visible_firmar_todas_paginas());
            properties.setProperty(CConstantes.BOLTSA, "" + configuracion.isVg_con_sello());
            
            if (chkConSello.isSelected()) {
                properties.setProperty(CConstantes.URLTSA, txtTsaUrl.getText());
                properties.setProperty(CConstantes.URLTSAUSUARIO, txtTsaUsuario.getText());
                properties.setProperty(CConstantes.HSHCLAVETSA, new helperClass().encrypt(new String(txtTsaClave.getPassword())));
                configuracion.setVg_tsa_url(txtTsaUrl.getText());
                configuracion.setVg_tsa_usuario(txtTsaUsuario.getText());
                configuracion.setVg_tsa_clave(new String(txtTsaClave.getPassword()));
            } else {
                configuracion.setVg_tsa_url("");
                configuracion.setVg_tsa_usuario("");
                configuracion.setVg_tsa_clave("");
            }
            
            properties.setProperty(CConstantes.BOLCERRARDOCUMENTO, "" + configuracion.isVg_cerrar_documento());
            properties.setProperty(CConstantes.STRALGORITMO, "" + configuracion.getVg_algoritmo());
            properties.setProperty(CConstantes.BOLTSL, "" + configuracion.isVg_validar_con_tsl());
            properties.setProperty(CConstantes.URLTSL, txtTslUrls.getText());
            properties.setProperty(CConstantes.BOLREPUDIO, "" + configuracion.isVg_validar_con_no_repudio());
            properties.setProperty(CConstantes.BOL_CAL, "" + configuracion.getConNumeroCal());
            properties.setProperty(CConstantes.BOL_AREA, "" + configuracion.getConCarmpoArea());         
            properties.setProperty(CConstantes.BOL_FH, "" + configuracion.getConCarmpoAreaFH());         
            properties.setProperty(CConstantes.BOL_QR, "" + configuracion.getConQR());
            properties.setProperty(CConstantes.TextoFirmante, "" + configuracion.getVg_texto_firmante());
            properties.setProperty(CConstantes.BOL_ETIQUETA, "" + configuracion.getEtiqueta_firma());
            properties.setProperty(CConstantes.conCampoEmpresa, "" + configuracion.getConCampoEmpresa());
            
            if (!txtTextoFIrmante.getText().isEmpty()) {
                configuracion.setTextoFirma1(txtTextoFIrmante.getText());                
            }else{
                txtTextoFIrmante.setText(""); // Establecer el texto a vacío
                configuracion.setTextoFirma1(txtTextoFIrmante.getText());
            }
            properties.store(foss, CConstantes.APLICACION_NOMBRE);
            foss.close();
        }
        
        CConstantes.dialogo("Se han guardado los cambios");
    }
    
    void mensajeerror(String contenido, Color color)
    {
        lblInfo.setText(contenido);
        lblInfo.setForeground(color);
        
        if (!contenido.equals(""))
            lblInfo.setToolTipText("<html><p width=\"300\">" + lblInfo.getText() + "</p></html>");
        else
            lblInfo.setToolTipText(null);
        
//        txtRespuesta.setBorder(null);
//        txtRespuesta.setBackground(lblInfo.getBackground());
//        txtRespuesta.setFont(lblInfo.getFont());
//        txtRespuesta.setText(lblInfo.getText());
    }

    void mensajeproceso(String contenido)
    {
        lblInfo.setVisible(false);
        lblInfo.setText(contenido);
        lblInfo.setForeground(Color.black);
        
        if (!contenido.equals(""))
            lblInfo.setToolTipText("<html><p width=\"300\">" + lblInfo.getText() + "</p></html>");
        else
            lblInfo.setToolTipText(null);
        
//        txtRespuesta.setBorder(null);
//        txtRespuesta.setBackground(lblInfo.getBackground());
//        txtRespuesta.setFont(lblInfo.getFont());
//        txtRespuesta.setText(lblInfo.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlpadre = new javax.swing.JPanel();
        pnlBarra3 = new javax.swing.JPanel();
        lblCerrar = new javax.swing.JLabel();
        lblMinimizar = new javax.swing.JLabel();
        btnLogo = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        lblMinimizar2 = new javax.swing.JLabel();
        canvasPrincipal = new javax.swing.JPanel();
        pnlConfiguracion = new javax.swing.JPanel();
        chkConSello = new javax.swing.JCheckBox();
        chkFirmaVisible = new javax.swing.JCheckBox();
        lyFirmaVisible = new javax.swing.JLayeredPane();
        cboFuente = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        chkConImagen = new javax.swing.JCheckBox();
        cboEstiloFirma = new javax.swing.JComboBox();
        btnExaminar = new javax.swing.JButton();
        txtrutaImagen = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTamanioFuente = new javax.swing.JTextField();
        txtPagina = new javax.swing.JTextField();
        lblPagina = new javax.swing.JLabel();
        txtTextoFIrmante = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lySelloDeTiempo = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTsaUrl = new javax.swing.JTextField();
        txtTsaUsuario = new javax.swing.JTextField();
        txtTsaClave = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        chkProtegerArchivos = new javax.swing.JCheckBox();
        lyProtegerArchivos = new javax.swing.JLayeredPane();
        jLabel4 = new javax.swing.JLabel();
        cboalgoritmo = new javax.swing.JComboBox();
        chkCerrarDocumento = new javax.swing.JCheckBox();
        chkAplicarValidaciones = new javax.swing.JCheckBox();
        lyaplicarValidaciones = new javax.swing.JLayeredPane();
        chkConTsl = new javax.swing.JCheckBox();
        txtTslUrls = new javax.swing.JTextField();
        chkConNoRepudio = new javax.swing.JCheckBox();
        chkEtiqueta = new javax.swing.JCheckBox();
        txtEtiqueta = new javax.swing.JTextField();
        chkCampoArea = new javax.swing.JCheckBox();
        chkCalNumber = new javax.swing.JCheckBox();
        chkEnTodasPaginas = new javax.swing.JCheckBox();
        chkCampoEmpresa = new javax.swing.JCheckBox();
        chkCampoArea1 = new javax.swing.JCheckBox();
        chkIncluirQR = new javax.swing.JCheckBox();
        pnlPrincipal = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        tbDocumentosFirmar = new javax.swing.JTabbedPane();
        scDgvDocumentos = new javax.swing.JScrollPane();
        dgvDocumentos = new javax.swing.JTable();
        pnlProcesoDeListado = new javax.swing.JPanel();
        scProcesoListado = new javax.swing.JScrollPane();
        txtProcesoListado = new javax.swing.JTextArea();
        btnFirmarMasivamente = new javax.swing.JToggleButton();
        btnBorrarTabla = new javax.swing.JToggleButton();
        capaTotalArchivosListados = new javax.swing.JLayeredPane();
        lblCargados = new javax.swing.JLabel();
        lblTotalListados = new javax.swing.JLabel();
        capaTotalArchivosFirmados = new javax.swing.JLayeredPane();
        lblFirmados = new javax.swing.JLabel();
        lblTotalFirmados = new javax.swing.JLabel();
        capaTotalArchivosErrados = new javax.swing.JLayeredPane();
        lblErrados = new javax.swing.JLabel();
        lblTotalErrados = new javax.swing.JLabel();
        btnAbrirCarpetaFirmados = new javax.swing.JToggleButton();
        lblCambiarTamano = new javax.swing.JLabel();
        pnlPaginas = new javax.swing.JPanel();
        btnPaginaUltima = new javax.swing.JButton();
        btnPaginaSiguiente = new javax.swing.JButton();
        lblPaginaHasta = new javax.swing.JLabel();
        cboPaginaActual = new javax.swing.JComboBox();
        btnPaginaAnterior = new javax.swing.JButton();
        btnPaginaPrimera = new javax.swing.JButton();
        pnlCoordenadas = new javax.swing.JLayeredPane();
        lblX = new javax.swing.JLabel();
        lblY = new javax.swing.JLabel();
        lblAncho = new javax.swing.JLabel();
        lblAlto = new javax.swing.JLabel();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        chkDarConstancia = new javax.swing.JCheckBox();
        jToolBar1 = new javax.swing.JToolBar();
        btnCargar = new javax.swing.JButton();
        btnCargarMasivo = new javax.swing.JButton();
        btnIrConfiguracion = new javax.swing.JToggleButton();
        btnGrabarCambios = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);

        pnlpadre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlpadre.setMinimumSize(new java.awt.Dimension(440, 0));

        pnlBarra3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlBarra3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlBarra3MouseClicked(evt);
            }
        });

        lblCerrar.setBackground(new java.awt.Color(255, 153, 0));
        lblCerrar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCerrar.setForeground(new java.awt.Color(255, 255, 255));
        lblCerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar.setText("x");
        lblCerrar.setToolTipText("Cerrar");
        lblCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar.setOpaque(true);
        lblCerrar.setPreferredSize(new java.awt.Dimension(6, 30));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCerrarMouseExited(evt);
            }
        });

        lblMinimizar.setBackground(new java.awt.Color(204, 204, 204));
        lblMinimizar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMinimizar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMinimizar.setText("_");
        lblMinimizar.setToolTipText("Minimizar");
        lblMinimizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMinimizar.setOpaque(true);
        lblMinimizar.setPreferredSize(new java.awt.Dimension(39, 30));
        lblMinimizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMinimizarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblMinimizarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblMinimizarMouseExited(evt);
            }
        });

        btnLogo.setBackground(new java.awt.Color(211, 111, 66));
        btnLogo.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnLogo.setForeground(new java.awt.Color(255, 255, 255));
        btnLogo.setAutoscrolls(true);
        btnLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btnLogo.setBorderPainted(false);
        btnLogo.setContentAreaFilled(false);
        btnLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogo.setFocusPainted(false);
        btnLogo.setPreferredSize(new java.awt.Dimension(55, 26));
        btnLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoMouseExited(evt);
            }
        });
        btnLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblMinimizar2.setBackground(new java.awt.Color(204, 204, 204));
        lblMinimizar2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblMinimizar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMinimizar2.setText("Max");
        lblMinimizar2.setToolTipText("Maximizar");
        lblMinimizar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMinimizar2.setOpaque(true);
        lblMinimizar2.setPreferredSize(new java.awt.Dimension(39, 30));
        lblMinimizar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMinimizar2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlBarra3Layout = new javax.swing.GroupLayout(pnlBarra3);
        pnlBarra3.setLayout(pnlBarra3Layout);
        pnlBarra3Layout.setHorizontalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarra3Layout.createSequentialGroup()
                .addComponent(lblMinimizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMinimizar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlBarra3Layout.setVerticalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlBarra3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMinimizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMinimizar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        canvasPrincipal.setBackground(new java.awt.Color(241, 241, 241));
        canvasPrincipal.setPreferredSize(new java.awt.Dimension(200, 605));

        pnlConfiguracion.setBackground(new java.awt.Color(241, 241, 241));
        pnlConfiguracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlConfiguracion.setMaximumSize(new java.awt.Dimension(405, 526));
        pnlConfiguracion.setMinimumSize(new java.awt.Dimension(405, 526));

        chkConSello.setBackground(new java.awt.Color(255, 255, 255));
        chkConSello.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkConSello.setForeground(new java.awt.Color(97, 120, 134));
        chkConSello.setSelected(true);
        chkConSello.setText("Incluir Sello de Tiempo(Opcional)");
        chkConSello.setContentAreaFilled(false);
        chkConSello.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkConSello.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConSelloActionPerformed(evt);
            }
        });

        chkFirmaVisible.setBackground(new java.awt.Color(255, 255, 255));
        chkFirmaVisible.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkFirmaVisible.setForeground(new java.awt.Color(97, 120, 134));
        chkFirmaVisible.setSelected(true);
        chkFirmaVisible.setText("Firma Visible");
        chkFirmaVisible.setBorderPaintedFlat(true);
        chkFirmaVisible.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkFirmaVisible.setOpaque(false);
        chkFirmaVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFirmaVisibleActionPerformed(evt);
            }
        });

        lyFirmaVisible.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), null));

        cboFuente.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        cboFuente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Arial", "Calibri" }));
        cboFuente.setPreferredSize(new java.awt.Dimension(60, 28));
        cboFuente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFuenteActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(97, 97, 106));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Fuente");

        chkConImagen.setBackground(new java.awt.Color(255, 255, 255));
        chkConImagen.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkConImagen.setForeground(new java.awt.Color(97, 97, 106));
        chkConImagen.setSelected(true);
        chkConImagen.setText("Imagen");
        chkConImagen.setContentAreaFilled(false);
        chkConImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkConImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConImagenActionPerformed(evt);
            }
        });

        cboEstiloFirma.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        cboEstiloFirma.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solo imagen", "Solo descripción", "Imagen y descripción", "Imagen con texto al lado" }));
        cboEstiloFirma.setMinimumSize(new java.awt.Dimension(135, 28));
        cboEstiloFirma.setPreferredSize(new java.awt.Dimension(135, 28));
        cboEstiloFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEstiloFirmaActionPerformed(evt);
            }
        });

        btnExaminar.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btnExaminar.setText("...");
        btnExaminar.setToolTipText("Examinar");
        btnExaminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExaminar.setFocusPainted(false);
        btnExaminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExaminarActionPerformed(evt);
            }
        });

        txtrutaImagen.setEditable(false);
        txtrutaImagen.setBackground(new java.awt.Color(220, 220, 220));
        txtrutaImagen.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        txtrutaImagen.setText("C:/imagen.jpg");
        txtrutaImagen.setToolTipText("Ruta de Imagen");
        txtrutaImagen.setPreferredSize(new java.awt.Dimension(85, 28));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(97, 97, 106));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Estilo");

        txtTamanioFuente.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTamanioFuente.setText("10");
        txtTamanioFuente.setToolTipText("Tamaño de fuente");
        txtTamanioFuente.setPreferredSize(new java.awt.Dimension(18, 25));
        txtTamanioFuente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTamanioFuenteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTamanioFuenteFocusLost(evt);
            }
        });

        txtPagina.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtPagina.setToolTipText("Colocar la etiqueta correspondiente");
        txtPagina.setPreferredSize(new java.awt.Dimension(275, 28));

        lblPagina.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblPagina.setForeground(new java.awt.Color(97, 97, 106));
        lblPagina.setText("Página");

        txtTextoFIrmante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTextoFIrmanteActionPerformed(evt);
            }
        });

        jLabel5.setText("Texto junto al nombre del firmante:");

        lyFirmaVisible.setLayer(cboFuente, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(jLabel12, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(chkConImagen, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(cboEstiloFirma, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(btnExaminar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(txtrutaImagen, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(txtTamanioFuente, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(txtPagina, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(lblPagina, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(txtTextoFIrmante, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyFirmaVisible.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lyFirmaVisibleLayout = new javax.swing.GroupLayout(lyFirmaVisible);
        lyFirmaVisible.setLayout(lyFirmaVisibleLayout);
        lyFirmaVisibleLayout.setHorizontalGroup(
            lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lyFirmaVisibleLayout.createSequentialGroup()
                .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(lyFirmaVisibleLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chkConImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addGroup(lyFirmaVisibleLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboEstiloFirma, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboFuente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtrutaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnExaminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTamanioFuente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(lyFirmaVisibleLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTextoFIrmante)
                        .addGap(25, 25, 25)
                        .addComponent(lblPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        lyFirmaVisibleLayout.setVerticalGroup(
            lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lyFirmaVisibleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboEstiloFirma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtrutaImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(btnExaminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chkConImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboFuente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTamanioFuente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(lyFirmaVisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPagina)
                    .addComponent(txtTextoFIrmante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lySelloDeTiempo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), null));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(97, 97, 106));
        jLabel1.setText("Servicio");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(97, 97, 106));
        jLabel3.setText("Clave");

        txtTsaUrl.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaUrl.setToolTipText("URL de servicio de Sello de Tiempo");
        txtTsaUrl.setPreferredSize(new java.awt.Dimension(275, 28));
        txtTsaUrl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTsaUrlFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTsaUrlFocusLost(evt);
            }
        });
        txtTsaUrl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtTsaUrlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtTsaUrlMouseExited(evt);
            }
        });
        txtTsaUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTsaUrlActionPerformed(evt);
            }
        });

        txtTsaUsuario.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaUsuario.setToolTipText("Usuario del servicio de Sello de Tiempo");
        txtTsaUsuario.setPreferredSize(new java.awt.Dimension(275, 28));
        txtTsaUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTsaUsuarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTsaUsuarioFocusLost(evt);
            }
        });

        txtTsaClave.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaClave.setToolTipText("Clave del Servicio de Sello de Tiempo");
        txtTsaClave.setPreferredSize(new java.awt.Dimension(275, 28));
        txtTsaClave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTsaClaveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTsaClaveFocusLost(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(97, 97, 106));
        jLabel2.setText("Usuario");

        lySelloDeTiempo.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lySelloDeTiempo.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lySelloDeTiempo.setLayer(txtTsaUrl, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lySelloDeTiempo.setLayer(txtTsaUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lySelloDeTiempo.setLayer(txtTsaClave, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lySelloDeTiempo.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lySelloDeTiempoLayout = new javax.swing.GroupLayout(lySelloDeTiempo);
        lySelloDeTiempo.setLayout(lySelloDeTiempoLayout);
        lySelloDeTiempoLayout.setHorizontalGroup(
            lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lySelloDeTiempoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtTsaUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                    .addComponent(txtTsaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTsaClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        lySelloDeTiempoLayout.setVerticalGroup(
            lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lySelloDeTiempoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTsaUrl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTsaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(lySelloDeTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTsaClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        chkProtegerArchivos.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkProtegerArchivos.setForeground(new java.awt.Color(97, 120, 134));
        chkProtegerArchivos.setText("Proteger archivos(Solo para documentos de formato PDF)");
        chkProtegerArchivos.setOpaque(false);
        chkProtegerArchivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkProtegerArchivosActionPerformed(evt);
            }
        });

        lyProtegerArchivos.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), null));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(97, 97, 106));
        jLabel4.setText("Algoritmo de protección");

        cboalgoritmo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cboalgoritmo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SHA-1", "SHA-256" }));
        cboalgoritmo.setToolTipText("Algoritmo de resúmen");
        cboalgoritmo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboalgoritmoActionPerformed(evt);
            }
        });

        chkCerrarDocumento.setBackground(new java.awt.Color(255, 255, 255));
        chkCerrarDocumento.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCerrarDocumento.setForeground(new java.awt.Color(97, 97, 106));
        chkCerrarDocumento.setSelected(true);
        chkCerrarDocumento.setText("Cerrar documento");
        chkCerrarDocumento.setContentAreaFilled(false);
        chkCerrarDocumento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCerrarDocumento.setHideActionText(true);
        chkCerrarDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCerrarDocumentoActionPerformed(evt);
            }
        });

        lyProtegerArchivos.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyProtegerArchivos.setLayer(cboalgoritmo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyProtegerArchivos.setLayer(chkCerrarDocumento, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lyProtegerArchivosLayout = new javax.swing.GroupLayout(lyProtegerArchivos);
        lyProtegerArchivos.setLayout(lyProtegerArchivosLayout);
        lyProtegerArchivosLayout.setHorizontalGroup(
            lyProtegerArchivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lyProtegerArchivosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lyProtegerArchivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkCerrarDocumento)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboalgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        lyProtegerArchivosLayout.setVerticalGroup(
            lyProtegerArchivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lyProtegerArchivosLayout.createSequentialGroup()
                .addComponent(chkCerrarDocumento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lyProtegerArchivosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboalgoritmo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        chkAplicarValidaciones.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkAplicarValidaciones.setForeground(new java.awt.Color(97, 120, 134));
        chkAplicarValidaciones.setText("Aplicar las siguientes validaciones");
        chkAplicarValidaciones.setOpaque(false);
        chkAplicarValidaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAplicarValidacionesActionPerformed(evt);
            }
        });

        lyaplicarValidaciones.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), new java.awt.Color(235, 235, 235), null));

        chkConTsl.setBackground(new java.awt.Color(255, 255, 255));
        chkConTsl.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkConTsl.setForeground(new java.awt.Color(97, 97, 106));
        chkConTsl.setSelected(true);
        chkConTsl.setText("Con TSL");
        chkConTsl.setContentAreaFilled(false);
        chkConTsl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkConTsl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConTslActionPerformed(evt);
            }
        });

        txtTslUrls.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTslUrls.setText("http://iofe.com.pe");
        txtTslUrls.setToolTipText("URL de descarga de Lista de Servicio de Confianza");
        txtTslUrls.setPreferredSize(new java.awt.Dimension(275, 28));
        txtTslUrls.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTslUrlsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTslUrlsFocusLost(evt);
            }
        });

        chkConNoRepudio.setBackground(new java.awt.Color(255, 255, 255));
        chkConNoRepudio.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkConNoRepudio.setForeground(new java.awt.Color(97, 97, 106));
        chkConNoRepudio.setSelected(true);
        chkConNoRepudio.setText("Verificar marca de no repudio en el certificado");
        chkConNoRepudio.setContentAreaFilled(false);
        chkConNoRepudio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkConNoRepudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConNoRepudioActionPerformed(evt);
            }
        });

        chkEtiqueta.setBackground(new java.awt.Color(255, 255, 255));
        chkEtiqueta.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkEtiqueta.setForeground(new java.awt.Color(97, 97, 106));
        chkEtiqueta.setText("Con Etiqueta");
        chkEtiqueta.setContentAreaFilled(false);
        chkEtiqueta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkEtiqueta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEtiquetaActionPerformed(evt);
            }
        });

        txtEtiqueta.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtEtiqueta.setToolTipText("Colocar la etiqueta correspondiente");
        txtEtiqueta.setPreferredSize(new java.awt.Dimension(275, 28));
        txtEtiqueta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEtiquetaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEtiquetaFocusLost(evt);
            }
        });

        lyaplicarValidaciones.setLayer(chkConTsl, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyaplicarValidaciones.setLayer(txtTslUrls, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyaplicarValidaciones.setLayer(chkConNoRepudio, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyaplicarValidaciones.setLayer(chkEtiqueta, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lyaplicarValidaciones.setLayer(txtEtiqueta, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lyaplicarValidacionesLayout = new javax.swing.GroupLayout(lyaplicarValidaciones);
        lyaplicarValidaciones.setLayout(lyaplicarValidacionesLayout);
        lyaplicarValidacionesLayout.setHorizontalGroup(
            lyaplicarValidacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lyaplicarValidacionesLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(lyaplicarValidacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lyaplicarValidacionesLayout.createSequentialGroup()
                        .addComponent(chkConTsl)
                        .addGap(25, 25, 25)
                        .addComponent(txtTslUrls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(lyaplicarValidacionesLayout.createSequentialGroup()
                        .addComponent(chkConNoRepudio)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(lyaplicarValidacionesLayout.createSequentialGroup()
                        .addComponent(chkEtiqueta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEtiqueta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        lyaplicarValidacionesLayout.setVerticalGroup(
            lyaplicarValidacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lyaplicarValidacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lyaplicarValidacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTslUrls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkConTsl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkConNoRepudio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lyaplicarValidacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkEtiqueta)
                    .addComponent(txtEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        chkCampoArea.setBackground(new java.awt.Color(241, 241, 241));
        chkCampoArea.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCampoArea.setForeground(new java.awt.Color(97, 120, 134));
        chkCampoArea.setText("Incluir Etiqueta \"Cargo\"");
        chkCampoArea.setBorderPaintedFlat(true);
        chkCampoArea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCampoArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCampoAreaActionPerformed(evt);
            }
        });

        chkCalNumber.setBackground(new java.awt.Color(255, 255, 255));
        chkCalNumber.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCalNumber.setForeground(new java.awt.Color(97, 120, 134));
        chkCalNumber.setText("Incluir Número CAL");
        chkCalNumber.setBorderPaintedFlat(true);
        chkCalNumber.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCalNumber.setOpaque(false);
        chkCalNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCalNumberActionPerformed(evt);
            }
        });

        chkEnTodasPaginas.setBackground(new java.awt.Color(255, 255, 255));
        chkEnTodasPaginas.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkEnTodasPaginas.setForeground(new java.awt.Color(97, 97, 106));
        chkEnTodasPaginas.setSelected(true);
        chkEnTodasPaginas.setText("En todas las páginas");
        chkEnTodasPaginas.setToolTipText("");
        chkEnTodasPaginas.setContentAreaFilled(false);
        chkEnTodasPaginas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkEnTodasPaginas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEnTodasPaginasActionPerformed(evt);
            }
        });

        chkCampoEmpresa.setBackground(new java.awt.Color(241, 241, 241));
        chkCampoEmpresa.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCampoEmpresa.setForeground(new java.awt.Color(97, 120, 134));
        chkCampoEmpresa.setText("Incluir Etiqueta \"Empresa\"");
        chkCampoEmpresa.setBorderPaintedFlat(true);
        chkCampoEmpresa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCampoEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCampoEmpresaActionPerformed(evt);
            }
        });

        chkCampoArea1.setBackground(new java.awt.Color(241, 241, 241));
        chkCampoArea1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCampoArea1.setForeground(new java.awt.Color(97, 120, 134));
        chkCampoArea1.setText("Incluir Etiqueta \"Fecha y hora\"");
        chkCampoArea1.setBorderPaintedFlat(true);
        chkCampoArea1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCampoArea1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCampoArea1ActionPerformed(evt);
            }
        });

        chkIncluirQR.setBackground(new java.awt.Color(255, 255, 255));
        chkIncluirQR.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkIncluirQR.setForeground(new java.awt.Color(97, 97, 106));
        chkIncluirQR.setSelected(true);
        chkIncluirQR.setText("Incluir Código QR");
        chkIncluirQR.setToolTipText("");
        chkIncluirQR.setContentAreaFilled(false);
        chkIncluirQR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkIncluirQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIncluirQRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConfiguracionLayout = new javax.swing.GroupLayout(pnlConfiguracion);
        pnlConfiguracion.setLayout(pnlConfiguracionLayout);
        pnlConfiguracionLayout.setHorizontalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lyFirmaVisible)
            .addComponent(lySelloDeTiempo, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(lyProtegerArchivos, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(lyaplicarValidaciones)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(chkFirmaVisible, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkCampoArea)
                            .addComponent(chkCampoEmpresa))
                        .addGap(10, 10, 10)
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkIncluirQR, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkCampoArea1))
                        .addGap(18, 18, 18)
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkEnTodasPaginas)
                            .addComponent(chkCalNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkConSello)
                            .addComponent(chkProtegerArchivos, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkAplicarValidaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlConfiguracionLayout.setVerticalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkFirmaVisible, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkCampoArea)
                    .addComponent(chkCampoArea1)
                    .addComponent(chkEnTodasPaginas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCampoEmpresa)
                    .addComponent(chkIncluirQR)
                    .addComponent(chkCalNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lyFirmaVisible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(chkConSello, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lySelloDeTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkProtegerArchivos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lyProtegerArchivos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAplicarValidaciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lyaplicarValidaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlPrincipal.setBackground(new java.awt.Color(153, 153, 153));
        pnlPrincipal.setForeground(new java.awt.Color(102, 153, 255));
        pnlPrincipal.setAutoscrolls(true);
        pnlPrincipal.setPreferredSize(new java.awt.Dimension(424, 491));

        lblInfo.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(67, 73, 111));
        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfo.setText("CARGUE UN DOCUMENTO PARA VISUALIZARLO");

        dgvDocumentos.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        dgvDocumentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Archivo", "Estado", "Estado", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dgvDocumentos.setColumnSelectionAllowed(true);
        dgvDocumentos.setFillsViewportHeight(true);
        dgvDocumentos.setRowHeight(20);
        dgvDocumentos.setSurrendersFocusOnKeystroke(true);
        dgvDocumentos.getTableHeader().setResizingAllowed(false);
        dgvDocumentos.getTableHeader().setReorderingAllowed(false);
        dgvDocumentos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                dgvDocumentosMouseMoved(evt);
            }
        });
        dgvDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dgvDocumentosMouseClicked(evt);
            }
        });
        scDgvDocumentos.setViewportView(dgvDocumentos);
        dgvDocumentos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (dgvDocumentos.getColumnModel().getColumnCount() > 0) {
            dgvDocumentos.getColumnModel().getColumn(0).setMinWidth(0);
            dgvDocumentos.getColumnModel().getColumn(0).setPreferredWidth(0);
            dgvDocumentos.getColumnModel().getColumn(0).setMaxWidth(0);
            dgvDocumentos.getColumnModel().getColumn(3).setMinWidth(0);
            dgvDocumentos.getColumnModel().getColumn(3).setPreferredWidth(0);
            dgvDocumentos.getColumnModel().getColumn(3).setMaxWidth(0);
            dgvDocumentos.getColumnModel().getColumn(5).setMinWidth(0);
            dgvDocumentos.getColumnModel().getColumn(5).setPreferredWidth(0);
            dgvDocumentos.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        tbDocumentosFirmar.addTab("Documentos a firmar", scDgvDocumentos);

        txtProcesoListado.setColumns(20);
        txtProcesoListado.setRows(5);
        txtProcesoListado.setWrapStyleWord(true);
        scProcesoListado.setViewportView(txtProcesoListado);

        javax.swing.GroupLayout pnlProcesoDeListadoLayout = new javax.swing.GroupLayout(pnlProcesoDeListado);
        pnlProcesoDeListado.setLayout(pnlProcesoDeListadoLayout);
        pnlProcesoDeListadoLayout.setHorizontalGroup(
            pnlProcesoDeListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scProcesoListado, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
        );
        pnlProcesoDeListadoLayout.setVerticalGroup(
            pnlProcesoDeListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scProcesoListado, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );

        tbDocumentosFirmar.addTab("Proceso de listado", pnlProcesoDeListado);

        btnFirmarMasivamente.setBackground(new java.awt.Color(211, 111, 66));
        btnFirmarMasivamente.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        btnFirmarMasivamente.setForeground(new java.awt.Color(255, 255, 255));
        btnFirmarMasivamente.setText("FIRMAR");
        btnFirmarMasivamente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 111, 66)));
        btnFirmarMasivamente.setContentAreaFilled(false);
        btnFirmarMasivamente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFirmarMasivamente.setOpaque(true);
        btnFirmarMasivamente.setPreferredSize(new java.awt.Dimension(91, 26));
        btnFirmarMasivamente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFirmarMasivamenteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFirmarMasivamenteMouseExited(evt);
            }
        });
        btnFirmarMasivamente.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                btnFirmarMasivamenteComponentResized(evt);
            }
        });
        btnFirmarMasivamente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirmarMasivamenteActionPerformed(evt);
            }
        });

        btnBorrarTabla.setBackground(new java.awt.Color(211, 111, 66));
        btnBorrarTabla.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        btnBorrarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrarTabla.setText("BORRAR");
        btnBorrarTabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 111, 66)));
        btnBorrarTabla.setContentAreaFilled(false);
        btnBorrarTabla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrarTabla.setOpaque(true);
        btnBorrarTabla.setPreferredSize(new java.awt.Dimension(91, 26));
        btnBorrarTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBorrarTablaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBorrarTablaMouseExited(evt);
            }
        });
        btnBorrarTabla.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                btnBorrarTablaComponentResized(evt);
            }
        });
        btnBorrarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarTablaActionPerformed(evt);
            }
        });

        capaTotalArchivosListados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 111, 66)));
        capaTotalArchivosListados.setPreferredSize(new java.awt.Dimension(110, 25));

        lblCargados.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblCargados.setForeground(new java.awt.Color(97, 97, 106));
        lblCargados.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblCargados.setText("   Total Listados  : ");
        lblCargados.setOpaque(true);

        lblTotalListados.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblTotalListados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalListados.setText("0 ");
        lblTotalListados.setOpaque(true);

        capaTotalArchivosListados.setLayer(lblCargados, javax.swing.JLayeredPane.DEFAULT_LAYER);
        capaTotalArchivosListados.setLayer(lblTotalListados, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout capaTotalArchivosListadosLayout = new javax.swing.GroupLayout(capaTotalArchivosListados);
        capaTotalArchivosListados.setLayout(capaTotalArchivosListadosLayout);
        capaTotalArchivosListadosLayout.setHorizontalGroup(
            capaTotalArchivosListadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capaTotalArchivosListadosLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblCargados)
                .addGap(0, 0, 0)
                .addComponent(lblTotalListados, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        capaTotalArchivosListadosLayout.setVerticalGroup(
            capaTotalArchivosListadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capaTotalArchivosListadosLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(capaTotalArchivosListadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCargados, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(lblTotalListados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        capaTotalArchivosFirmados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 111, 66)));
        capaTotalArchivosFirmados.setPreferredSize(new java.awt.Dimension(110, 25));

        lblFirmados.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblFirmados.setForeground(new java.awt.Color(97, 97, 106));
        lblFirmados.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFirmados.setText("   Total Firmados : ");
        lblFirmados.setOpaque(true);

        lblTotalFirmados.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblTotalFirmados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalFirmados.setText("0 ");
        lblTotalFirmados.setOpaque(true);

        capaTotalArchivosFirmados.setLayer(lblFirmados, javax.swing.JLayeredPane.DEFAULT_LAYER);
        capaTotalArchivosFirmados.setLayer(lblTotalFirmados, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout capaTotalArchivosFirmadosLayout = new javax.swing.GroupLayout(capaTotalArchivosFirmados);
        capaTotalArchivosFirmados.setLayout(capaTotalArchivosFirmadosLayout);
        capaTotalArchivosFirmadosLayout.setHorizontalGroup(
            capaTotalArchivosFirmadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capaTotalArchivosFirmadosLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblFirmados)
                .addGap(0, 0, 0)
                .addComponent(lblTotalFirmados, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        capaTotalArchivosFirmadosLayout.setVerticalGroup(
            capaTotalArchivosFirmadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capaTotalArchivosFirmadosLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(capaTotalArchivosFirmadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFirmados, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(lblTotalFirmados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        capaTotalArchivosErrados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 111, 66)));
        capaTotalArchivosErrados.setPreferredSize(new java.awt.Dimension(110, 25));

        lblErrados.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblErrados.setForeground(new java.awt.Color(97, 97, 106));
        lblErrados.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblErrados.setText("   Total Errados    : ");
        lblErrados.setOpaque(true);

        lblTotalErrados.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        lblTotalErrados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalErrados.setText("0 ");
        lblTotalErrados.setOpaque(true);

        capaTotalArchivosErrados.setLayer(lblErrados, javax.swing.JLayeredPane.DEFAULT_LAYER);
        capaTotalArchivosErrados.setLayer(lblTotalErrados, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout capaTotalArchivosErradosLayout = new javax.swing.GroupLayout(capaTotalArchivosErrados);
        capaTotalArchivosErrados.setLayout(capaTotalArchivosErradosLayout);
        capaTotalArchivosErradosLayout.setHorizontalGroup(
            capaTotalArchivosErradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capaTotalArchivosErradosLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblErrados)
                .addGap(0, 0, 0)
                .addComponent(lblTotalErrados, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        capaTotalArchivosErradosLayout.setVerticalGroup(
            capaTotalArchivosErradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(capaTotalArchivosErradosLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(capaTotalArchivosErradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrados, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(lblTotalErrados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        btnAbrirCarpetaFirmados.setBackground(new java.awt.Color(211, 111, 66));
        btnAbrirCarpetaFirmados.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        btnAbrirCarpetaFirmados.setForeground(new java.awt.Color(255, 255, 255));
        btnAbrirCarpetaFirmados.setText("ABRIR CARPETA FIRMADOS");
        btnAbrirCarpetaFirmados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(211, 111, 66)));
        btnAbrirCarpetaFirmados.setContentAreaFilled(false);
        btnAbrirCarpetaFirmados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAbrirCarpetaFirmados.setOpaque(true);
        btnAbrirCarpetaFirmados.setPreferredSize(new java.awt.Dimension(91, 26));
        btnAbrirCarpetaFirmados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirCarpetaFirmadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbDocumentosFirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(btnAbrirCarpetaFirmados, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74)
                        .addComponent(btnBorrarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnFirmarMasivamente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(capaTotalArchivosListados, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(capaTotalArchivosFirmados, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(capaTotalArchivosErrados, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(tbDocumentosFirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAbrirCarpetaFirmados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBorrarTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFirmarMasivamente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(capaTotalArchivosListados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(capaTotalArchivosFirmados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(capaTotalArchivosErrados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblInfo))
        );

        lblCambiarTamano.setBackground(new java.awt.Color(255, 153, 0));
        lblCambiarTamano.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
        lblCambiarTamano.setForeground(new java.awt.Color(255, 255, 255));
        lblCambiarTamano.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCambiarTamano.setText(">");
        lblCambiarTamano.setAutoscrolls(true);
        lblCambiarTamano.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblCambiarTamano.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCambiarTamano.setOpaque(true);
        lblCambiarTamano.setPreferredSize(new java.awt.Dimension(8, 100));
        lblCambiarTamano.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseReleased(evt);
            }
        });

        btnPaginaUltima.setBackground(new java.awt.Color(255, 255, 255));
        btnPaginaUltima.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaUltima.setText("⇉‖");
        btnPaginaUltima.setToolTipText("Último");
        btnPaginaUltima.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPaginaUltima.setBorderPainted(false);
        btnPaginaUltima.setContentAreaFilled(false);
        btnPaginaUltima.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaUltima.setFocusPainted(false);
        btnPaginaUltima.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPaginaUltima.setMaximumSize(new java.awt.Dimension(35, 30));
        btnPaginaUltima.setMinimumSize(new java.awt.Dimension(35, 30));
        btnPaginaUltima.setPreferredSize(new java.awt.Dimension(35, 30));
        btnPaginaUltima.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPaginaUltimaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPaginaUltimaMouseExited(evt);
            }
        });

        btnPaginaSiguiente.setBackground(new java.awt.Color(255, 255, 255));
        btnPaginaSiguiente.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaSiguiente.setText("⇉");
        btnPaginaSiguiente.setToolTipText("Siguiente");
        btnPaginaSiguiente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPaginaSiguiente.setBorderPainted(false);
        btnPaginaSiguiente.setContentAreaFilled(false);
        btnPaginaSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaSiguiente.setFocusPainted(false);
        btnPaginaSiguiente.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPaginaSiguiente.setMaximumSize(new java.awt.Dimension(35, 30));
        btnPaginaSiguiente.setMinimumSize(new java.awt.Dimension(35, 30));
        btnPaginaSiguiente.setPreferredSize(new java.awt.Dimension(35, 30));
        btnPaginaSiguiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPaginaSiguienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPaginaSiguienteMouseExited(evt);
            }
        });

        lblPaginaHasta.setAutoscrolls(true);
        lblPaginaHasta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPaginaHasta.setPreferredSize(new java.awt.Dimension(41, 30));

        cboPaginaActual.setEditable(true);
        cboPaginaActual.setMinimumSize(new java.awt.Dimension(133, 30));
        cboPaginaActual.setPreferredSize(new java.awt.Dimension(120, 30));
        cboPaginaActual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPaginaActualActionPerformed(evt);
            }
        });
        cboPaginaActual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboPaginaActualKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cboPaginaActualKeyTyped(evt);
            }
        });

        btnPaginaAnterior.setBackground(new java.awt.Color(255, 255, 255));
        btnPaginaAnterior.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaAnterior.setText("⇇");
        btnPaginaAnterior.setToolTipText("Anterior");
        btnPaginaAnterior.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPaginaAnterior.setBorderPainted(false);
        btnPaginaAnterior.setContentAreaFilled(false);
        btnPaginaAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaAnterior.setFocusPainted(false);
        btnPaginaAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPaginaAnterior.setMaximumSize(new java.awt.Dimension(35, 30));
        btnPaginaAnterior.setMinimumSize(new java.awt.Dimension(35, 30));
        btnPaginaAnterior.setPreferredSize(new java.awt.Dimension(35, 30));
        btnPaginaAnterior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPaginaAnteriorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPaginaAnteriorMouseExited(evt);
            }
        });

        btnPaginaPrimera.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaPrimera.setText("‖⇇");
        btnPaginaPrimera.setToolTipText("Primero");
        btnPaginaPrimera.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPaginaPrimera.setBorderPainted(false);
        btnPaginaPrimera.setContentAreaFilled(false);
        btnPaginaPrimera.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaPrimera.setFocusPainted(false);
        btnPaginaPrimera.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPaginaPrimera.setMaximumSize(new java.awt.Dimension(35, 30));
        btnPaginaPrimera.setMinimumSize(new java.awt.Dimension(35, 30));
        btnPaginaPrimera.setPreferredSize(new java.awt.Dimension(35, 30));
        btnPaginaPrimera.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPaginaPrimeraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPaginaPrimeraMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlPaginasLayout = new javax.swing.GroupLayout(pnlPaginas);
        pnlPaginas.setLayout(pnlPaginasLayout);
        pnlPaginasLayout.setHorizontalGroup(
            pnlPaginasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPaginasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPaginaPrimera, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPaginaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPaginaActual, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPaginaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnPaginaSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPaginaUltima, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPaginasLayout.setVerticalGroup(
            pnlPaginasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPaginasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnPaginaSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPaginaUltima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(lblPaginaHasta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPaginasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cboPaginaActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPaginaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnPaginaPrimera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblX.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblX.setForeground(new java.awt.Color(97, 120, 134));
        lblX.setText("x : 0");

        lblY.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblY.setForeground(new java.awt.Color(97, 120, 134));
        lblY.setText("y :  0");

        lblAncho.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblAncho.setForeground(new java.awt.Color(97, 120, 134));
        lblAncho.setText(" Ancho : 0");

        lblAlto.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblAlto.setForeground(new java.awt.Color(97, 120, 134));
        lblAlto.setText("Alto : 0");

        btnZoomIn.setBackground(new java.awt.Color(255, 255, 255));
        btnZoomIn.setForeground(new java.awt.Color(67, 73, 111));
        btnZoomIn.setText("Zoom +");
        btnZoomIn.setToolTipText("Zoom Dentro");
        btnZoomIn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnZoomIn.setContentAreaFilled(false);
        btnZoomIn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnZoomIn.setFocusPainted(false);
        btnZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnZoomIn.setMaximumSize(new java.awt.Dimension(35, 30));
        btnZoomIn.setMinimumSize(new java.awt.Dimension(35, 30));
        btnZoomIn.setPreferredSize(new java.awt.Dimension(35, 30));
        btnZoomIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnZoomInMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnZoomInMouseExited(evt);
            }
        });
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        btnZoomOut.setBackground(new java.awt.Color(255, 255, 255));
        btnZoomOut.setForeground(new java.awt.Color(67, 73, 111));
        btnZoomOut.setText("Zoom -");
        btnZoomOut.setToolTipText("Zoom Dentro");
        btnZoomOut.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnZoomOut.setContentAreaFilled(false);
        btnZoomOut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnZoomOut.setFocusPainted(false);
        btnZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnZoomOut.setMaximumSize(new java.awt.Dimension(35, 30));
        btnZoomOut.setMinimumSize(new java.awt.Dimension(35, 30));
        btnZoomOut.setPreferredSize(new java.awt.Dimension(35, 30));
        btnZoomOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnZoomOutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnZoomOutMouseExited(evt);
            }
        });
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });

        chkDarConstancia.setBackground(new java.awt.Color(255, 255, 255));
        chkDarConstancia.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkDarConstancia.setForeground(new java.awt.Color(97, 97, 106));
        chkDarConstancia.setSelected(true);
        chkDarConstancia.setText("Doy constancia de haber visto todos los documentos.");
        chkDarConstancia.setContentAreaFilled(false);
        chkDarConstancia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkDarConstancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDarConstanciaActionPerformed(evt);
            }
        });

        pnlCoordenadas.setLayer(lblX, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCoordenadas.setLayer(lblY, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCoordenadas.setLayer(lblAncho, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCoordenadas.setLayer(lblAlto, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCoordenadas.setLayer(btnZoomIn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCoordenadas.setLayer(btnZoomOut, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCoordenadas.setLayer(chkDarConstancia, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout pnlCoordenadasLayout = new javax.swing.GroupLayout(pnlCoordenadas);
        pnlCoordenadas.setLayout(pnlCoordenadasLayout);
        pnlCoordenadasLayout.setHorizontalGroup(
            pnlCoordenadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCoordenadasLayout.createSequentialGroup()
                .addGroup(pnlCoordenadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCoordenadasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblX)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblY)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAncho)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAlto))
                    .addGroup(pnlCoordenadasLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(btnZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnZoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCoordenadasLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(chkDarConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        pnlCoordenadasLayout.setVerticalGroup(
            pnlCoordenadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCoordenadasLayout.createSequentialGroup()
                .addGroup(pnlCoordenadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblX)
                    .addComponent(lblY)
                    .addComponent(lblAncho)
                    .addComponent(lblAlto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCoordenadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnZoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkDarConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout canvasPrincipalLayout = new javax.swing.GroupLayout(canvasPrincipal);
        canvasPrincipal.setLayout(canvasPrincipalLayout);
        canvasPrincipalLayout.setHorizontalGroup(
            canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(canvasPrincipalLayout.createSequentialGroup()
                        .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlCoordenadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(pnlConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        canvasPrincipalLayout.setVerticalGroup(
            canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(canvasPrincipalLayout.createSequentialGroup()
                        .addComponent(pnlPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlCoordenadas))
                    .addGroup(canvasPrincipalLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jToolBar1.setBorder(null);
        jToolBar1.setRollover(true);

        btnCargar.setBackground(new java.awt.Color(255, 153, 0));
        btnCargar.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        btnCargar.setText("ARCHIVO");
        btnCargar.setToolTipText("Abrir un archivo para visualizar");
        btnCargar.setAutoscrolls(true);
        btnCargar.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        btnCargar.setBorderPainted(false);
        btnCargar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargar.setFocusable(false);
        btnCargar.setMaximumSize(new java.awt.Dimension(80, 30));
        btnCargar.setMinimumSize(new java.awt.Dimension(80, 30));
        btnCargar.setPreferredSize(new java.awt.Dimension(80, 30));
        btnCargar.setVerifyInputWhenFocusTarget(false);
        btnCargar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCargarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCargarMouseExited(evt);
            }
        });
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCargar);

        btnCargarMasivo.setBackground(new java.awt.Color(255, 153, 0));
        btnCargarMasivo.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        btnCargarMasivo.setText("ARCHIVO");
        btnCargarMasivo.setToolTipText("Cargar documentos para firmar.");
        btnCargarMasivo.setAutoscrolls(true);
        btnCargarMasivo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        btnCargarMasivo.setBorderPainted(false);
        btnCargarMasivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargarMasivo.setFocusPainted(false);
        btnCargarMasivo.setMaximumSize(new java.awt.Dimension(80, 30));
        btnCargarMasivo.setPreferredSize(new java.awt.Dimension(80, 30));
        btnCargarMasivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCargarMasivoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCargarMasivoMouseExited(evt);
            }
        });
        btnCargarMasivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarMasivoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCargarMasivo);

        btnIrConfiguracion.setBackground(new java.awt.Color(255, 153, 0));
        btnIrConfiguracion.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        btnIrConfiguracion.setForeground(new java.awt.Color(255, 255, 255));
        btnIrConfiguracion.setText("  CONFIGURACIÓN  ");
        btnIrConfiguracion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        btnIrConfiguracion.setBorderPainted(false);
        btnIrConfiguracion.setContentAreaFilled(false);
        btnIrConfiguracion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIrConfiguracion.setMaximumSize(new java.awt.Dimension(110, 26));
        btnIrConfiguracion.setMinimumSize(new java.awt.Dimension(0, 0));
        btnIrConfiguracion.setOpaque(true);
        btnIrConfiguracion.setPreferredSize(new java.awt.Dimension(91, 26));
        btnIrConfiguracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIrConfiguracionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIrConfiguracionMouseExited(evt);
            }
        });
        btnIrConfiguracion.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                btnIrConfiguracionComponentResized(evt);
            }
        });
        btnIrConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrConfiguracionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnIrConfiguracion);

        btnGrabarCambios.setBackground(new java.awt.Color(255, 153, 0));
        btnGrabarCambios.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        btnGrabarCambios.setForeground(new java.awt.Color(255, 255, 255));
        btnGrabarCambios.setText("   GUARDAR   ");
        btnGrabarCambios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        btnGrabarCambios.setBorderPainted(false);
        btnGrabarCambios.setContentAreaFilled(false);
        btnGrabarCambios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrabarCambios.setMaximumSize(new java.awt.Dimension(80, 30));
        btnGrabarCambios.setOpaque(true);
        btnGrabarCambios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGrabarCambiosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGrabarCambiosMouseExited(evt);
            }
        });
        btnGrabarCambios.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                btnGrabarCambiosComponentResized(evt);
            }
        });
        btnGrabarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarCambiosActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGrabarCambios);

        javax.swing.GroupLayout pnlpadreLayout = new javax.swing.GroupLayout(pnlpadre);
        pnlpadre.setLayout(pnlpadreLayout);
        pnlpadreLayout.setHorizontalGroup(
            pnlpadreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBarra3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(canvasPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1151, Short.MAX_VALUE)
        );
        pnlpadreLayout.setVerticalGroup(
            pnlpadreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlpadreLayout.createSequentialGroup()
                .addComponent(pnlBarra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(canvasPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlpadre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlpadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPaginaUltimaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaUltimaMouseEntered
        //cambiarColoFondo(btnPaginaUltima);
        btnPaginaUltima.setBorderPainted(true);
    }//GEN-LAST:event_btnPaginaUltimaMouseEntered

    private void btnPaginaSiguienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaSiguienteMouseEntered
        // cambiarColoFondo(btnPaginaSiguiente);
        btnPaginaSiguiente.setBorderPainted(true);
    }//GEN-LAST:event_btnPaginaSiguienteMouseEntered

    private void btnPaginaAnteriorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaAnteriorMouseEntered
        // cambiarColoFondo(btnPaginaAnterior);
        btnPaginaAnterior.setBorderPainted(true);
    }//GEN-LAST:event_btnPaginaAnteriorMouseEntered

    private void btnPaginaPrimeraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaPrimeraMouseEntered
//       cambiarColoFondo(btnPrimeraPagina);
        btnPaginaPrimera.setBorderPainted(true);
    }//GEN-LAST:event_btnPaginaPrimeraMouseEntered

    private void chkCerrarDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCerrarDocumentoActionPerformed
        configuracion.setVg_cerrar_documento(chkCerrarDocumento.isSelected());
//        grabarPropiedad(CConstantes.BOLCERRARDOCUMENTO,Boolean.toString(chkCerrarDocumento.isSelected()));
    }//GEN-LAST:event_chkCerrarDocumentoActionPerformed

    private void chkFirmaVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFirmaVisibleActionPerformed
        validarFirmaVisible();
    }//GEN-LAST:event_chkFirmaVisibleActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
        cargarDocumento("", btnCargar);
    }//GEN-LAST:event_btnCargarActionPerformed

    private void chkEnTodasPaginasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEnTodasPaginasActionPerformed
        configuracion.setVg_firma_visible_firmar_todas_paginas(chkEnTodasPaginas.isSelected());
        // grabarPropiedad(CConstantes.BOLTODASPAGINAS,Boolean.toString(chkEnTodasPaginas.isSelected()));
    }//GEN-LAST:event_chkEnTodasPaginasActionPerformed

    private void lblCambiarTamanoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseClicked
        String sentido = lblCambiarTamano.getText();

        if (sentido.equals("<"))
            if (!validarDatos())
                return;
        
        agrandarFormulario();
        validarEtiquetaFirma();
    }//GEN-LAST:event_lblCambiarTamanoMouseClicked

    private void chkConSelloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConSelloActionPerformed
        validarSelloDeTiempo();
    }//GEN-LAST:event_chkConSelloActionPerformed

    private void chkConImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConImagenActionPerformed
        int estiloActual = cboEstiloFirma.getSelectedIndex();
        if (!chkConImagen.isSelected()) {
            if (estiloActual == 0 || estiloActual == 2 || estiloActual == 3) {
                cboEstiloFirma.setSelectedIndex(1); // "Solo descripción"
            }
            btnExaminar.setEnabled(false);
        } else {
                if (estiloActual == 1) {
                cboEstiloFirma.setSelectedIndex(2); // "Imagen y descripción"
            }
            btnExaminar.setEnabled(true);
        }
        configuracion.setVg_con_imagen(chkConImagen.isSelected());
        configuracion.setVg_firma_visible(chkFirmaVisible.isSelected());
        configuracion.setVg_firma_visible_estilo_firma(cboEstiloFirma.getSelectedIndex());
//        grabarPropiedad(CConstantes.BOLIMAGEN,Boolean.toString(chkConImagen.isSelected()));
    }//GEN-LAST:event_chkConImagenActionPerformed

    private void chkConTslActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConTslActionPerformed
        validarTsl();
    }//GEN-LAST:event_chkConTslActionPerformed

    private void chkConNoRepudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConNoRepudioActionPerformed
        configuracion.setVg_validar_con_no_repudio(chkConNoRepudio.isSelected());
        //grabarPropiedad(CConstantes.BOLREPUDIO,Boolean.toString(chkConNoRepudio.isSelected()));
    }//GEN-LAST:event_chkConNoRepudioActionPerformed

    private void cboalgoritmoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboalgoritmoActionPerformed
        configuracion.setVg_algoritmo(cboalgoritmo.getSelectedItem().toString());
//        grabarPropiedad(CConstantes.STRALGORITMO,cboalgoritmo.getSelectedItem().toString());
    }//GEN-LAST:event_cboalgoritmoActionPerformed

    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExaminarActionPerformed
        final JFileChooser elegirimagen = new JFileChooser(txtrutaImagen.getText());
        elegirimagen.setFileSelectionMode(JFileChooser.FILES_ONLY);
        elegirimagen.setAcceptAllFileFilterUsed(false);
        elegirimagen.addChoosableFileFilter(new FileNameExtensionFilter("Image(*.jpeg|*.jpg|*.png|*.gif) ", "jpeg", "jpg", "gif", "png"));
        elegirimagen.setDialogTitle("Seleccione una imagen");
        elegirimagen.setMultiSelectionEnabled(true);
        int opcion = elegirimagen.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            txtrutaImagen.setText(elegirimagen.getSelectedFile().getAbsolutePath());
            configuracion.setVg_firma_visible_ruta_imagen(txtrutaImagen.getText());
//           grabarPropiedad(CConstantes.FILIMAGEN,txtrutaImagen.getText());
        }
    }//GEN-LAST:event_btnExaminarActionPerformed

    private void cboEstiloFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEstiloFirmaActionPerformed
        boolean con_firma_visible = chkFirmaVisible.isSelected();
        int estilo_index = cboEstiloFirma.getSelectedIndex();
        if (con_firma_visible) {
            switch (estilo_index) {
                case 0: // "Solo imagen"
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(true);
                    btnExaminar.setEnabled(true);
                    break;

                case 1: // "Solo descripción"
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(false);
                    btnExaminar.setEnabled(false);
                    break;

                case 2: // "Imagen y descripción"
                case 3: // "Imagen con texto al lado"
                    chkConImagen.setEnabled(true);
                    // Si selecciona un estilo con imagen, marcar el checkbox
                    if (!chkConImagen.isSelected()) {
                        chkConImagen.setSelected(true);
                    }
                    btnExaminar.setEnabled(true);
                    break;
            }

            configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            configuracion.setVg_con_imagen(chkConImagen.isSelected());
        }

        grabarPropiedad(CConstantes.INTESTILO, cboEstiloFirma.getSelectedIndex() + "");
    }//GEN-LAST:event_cboEstiloFirmaActionPerformed

    private void cboFuenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFuenteActionPerformed
        grabarPropiedad(CConstantes.STRFUENTE, cboFuente.getSelectedItem().toString());
    }//GEN-LAST:event_cboFuenteActionPerformed
    boolean validarDatos()
    {
        boolean correcto = true;
//        if(chkFirmaVisible.isSelected()){
//            if(DibujoAncho<10 || DibujoAlto<10){
//                chkFirmaVisible.setSelected(false);
//                configuracion.setVg_firma_visible(false);
//            }
//        }
//        else

        configuracion.setVg_texto_firmante(txtTextoFIrmante.getText());
        configuracion.setEtiqueta_firma(txtEtiqueta.getText());

        if (chkConSello.isSelected()) {
            if (txtTsaUrl.getText().trim().length() == 0) {
                CConstantes.dialogo("Ingrese el link del servicio de sello de tiempo");
                focodentro(txtTsaUrl);
                txtTsaUrl.requestFocus();
                correcto = false;
            }
        } else if (chkConTsl.isSelected()) {
            if (txtTslUrls.getText().trim().length() == 0) {
                CConstantes.dialogo("Ingrese el link del servicio de TSL");
                focodentro(txtTslUrls);
                txtTslUrls.requestFocus();
                correcto = false;
            } else {
                focofuera(txtTslUrls);
            }
        } else if (modalidad.equals("-u") && chkConImagen.isSelected()) {
            if (txtrutaImagen.getText().trim().length() == 0) {
                CConstantes.dialogo("Seleccione una imagen  para la firma visible.");
                focodentro(txtrutaImagen);
                txtrutaImagen.requestFocus();
                correcto = false;
            } else if (!new File(txtrutaImagen.getText()).exists()) {
                CConstantes.dialogo("La ruta de la imagen " + txtrutaImagen.getText() + "  no se encuentra.");
                focodentro(txtrutaImagen);
                txtrutaImagen.requestFocus();
                correcto = false;
            }
        }
        return correcto;
    }
    private void btnGrabarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarCambiosActionPerformed
        configuracion.setConCampoEmpresa(chkCampoEmpresa.isSelected());
        
        try
        {
            btnGrabarCambios.setText("GUARDANDO");
            
            if (!validarDatos())
            {
                btnGrabarCambios.setText("GUARDAR");
                return;
            }
            
            guardarCambios();
            //agrandarFormulario();
            btnGrabarCambios.setText("GUARDAR");
        }
        catch (Exception ex)
        {
            CConstantes.dialogo(ex.getMessage());
        }
    }//GEN-LAST:event_btnGrabarCambiosActionPerformed

    private void lblCambiarTamanoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseEntered
        lblCambiarTamano.setBackground(Color.lightGray);
    }//GEN-LAST:event_lblCambiarTamanoMouseEntered

    private void lblCambiarTamanoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCambiarTamanoMouseReleased

    private void lblCambiarTamanoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseExited
        //lblCambiarTamano.setBackground(new Color(240,240,240));
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(211, 111, 66), java.awt.Color.white);
    }//GEN-LAST:event_lblCambiarTamanoMouseExited

    private void btnGrabarCambiosComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnGrabarCambiosComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGrabarCambiosComponentResized

    void focodentro(javax.swing.JTextField cajatexto) {
        cajatexto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(67, 73, 111), 2, true));
    }

    void focofuera(javax.swing.JTextField cajatexto) {
        cajatexto.setBorder(txtrutaImagen.getBorder());
    }

    private void txtTsaUrlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTsaUrlMouseEntered

    }//GEN-LAST:event_txtTsaUrlMouseEntered

    private void txtTsaUrlFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaUrlFocusGained
        focodentro((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTsaUrlFocusGained

    private void txtTsaUrlFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaUrlFocusLost
        focofuera((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTsaUrlFocusLost

    private void txtTsaUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaUsuarioFocusGained
        focodentro((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTsaUsuarioFocusGained

    private void txtTsaUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaUsuarioFocusLost
        focofuera((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTsaUsuarioFocusLost

    private void txtTsaClaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaClaveFocusGained
        focodentro((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTsaClaveFocusGained

    private void txtTsaClaveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaClaveFocusLost
        focofuera((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTsaClaveFocusLost

    private void txtTslUrlsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTslUrlsFocusGained
        focodentro((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTslUrlsFocusGained

    private void txtTslUrlsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTslUrlsFocusLost
        focofuera((javax.swing.JTextField) evt.getSource());
    }//GEN-LAST:event_txtTslUrlsFocusLost

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        cerrarVentana();
    }//GEN-LAST:event_lblCerrarMouseClicked
    
    private void cerrarVentana()
    {
        if (!frmConfigurador.estaAbierto)
            System.exit(0);
        else
            dispose();
    }

    private int preguntarCancelarInstalacion() {
        return javax.swing.JOptionPane.showConfirmDialog(rootPane, "¿Está seguro de cerrar la aplicación?", "SSIGNER", javax.swing.JOptionPane.OK_OPTION, 3);
    }

    private int preguntarDarConstancia() {
        return javax.swing.JOptionPane.showConfirmDialog(rootPane, "¿Da constancia de haber revisado todos los documentos seleccionados.?", "SSIGNER", javax.swing.JOptionPane.OK_OPTION, 3);
    }

    protected void cambiarColor(Object obj) {
        cambiarColor(obj, Color.lightGray, Color.white);
    }

    protected void cambiarColor(Object obj, Color colorfondo, Color colorletra) {
        if (obj instanceof javax.swing.JLabel) {
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        } else if (obj instanceof JButton) {
            ((JButton) obj).setBackground(colorfondo);
            ((JButton) obj).setForeground(colorletra);
            ((JButton) obj).setOpaque(true);
            ((javax.swing.JButton) obj).setBorder(javax.swing.BorderFactory.createLineBorder(colorletra));
        } else if (obj instanceof JToggleButton) {
            ((JToggleButton) obj).setBackground(colorfondo);
            ((JToggleButton) obj).setForeground(colorletra);
            ((JToggleButton) obj).setOpaque(true);
            ((javax.swing.JToggleButton) obj).setBorder(javax.swing.BorderFactory.createLineBorder(colorletra));
        }
    }

    private Color cambiarColorPorDefecto() {
        return new Color(240, 240, 240);
    }

    private void iconofiedVentana() {
        this.setExtendedState(ICONIFIED);
    }

    void borrarTabla() {
        if (dgvDocumentos.getRowCount() > 0) {
            int RowsCount = dgvDocumentos.getRowCount() - 1;
            DefaultTableModel v_tabla_modelo = (DefaultTableModel) dgvDocumentos.getModel();
            for (int a = RowsCount; a >= 0; a--) {
                v_tabla_modelo.removeRow(a);
            }
            dgvDocumentos.setModel(v_tabla_modelo);
            configuracion.setRutasdocumentos(new ArrayList<String>());
        }
    }
    private void lblCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource(), Color.red, Color.white);
    }//GEN-LAST:event_lblCerrarMouseEntered

    private void lblCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(255, 153, 153), Color.white);
    }//GEN-LAST:event_lblCerrarMouseExited

    private void lblMinimizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizarMouseClicked
        iconofiedVentana();
    }//GEN-LAST:event_lblMinimizarMouseClicked

    private void lblMinimizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizarMouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource());
    }//GEN-LAST:event_lblMinimizarMouseEntered

    private void lblMinimizarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizarMouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(204, 204, 204), Color.black);
    }//GEN-LAST:event_lblMinimizarMouseExited

    private void txtTsaUrlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTsaUrlMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTsaUrlMouseExited

    private void btnCargarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarMouseEntered
        btnCargar.setBorderPainted(true);
//         cambiarColor((javax.swing.JButton)evt.getSource(),java.awt.Color.white,new Color(211,111,66));
    }//GEN-LAST:event_btnCargarMouseEntered

    private void btnCargarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarMouseExited
        btnCargar.setBorderPainted(false);
        //        cambiarColor((javax.swing.JButton)evt.getSource(),new Color(211,111,66),java.awt.Color.white);
    }//GEN-LAST:event_btnCargarMouseExited

    private void btnGrabarCambiosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGrabarCambiosMouseEntered
        btnGrabarCambios.setBorderPainted(true);
    }//GEN-LAST:event_btnGrabarCambiosMouseEntered

    private void btnGrabarCambiosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGrabarCambiosMouseExited
        btnGrabarCambios.setBorderPainted(false);
    }//GEN-LAST:event_btnGrabarCambiosMouseExited

    private void btnLogoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoMouseEntered

    private void btnLogoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoMouseExited

    private void btnLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoActionPerformed

    private void chkDarConstanciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDarConstanciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDarConstanciaActionPerformed

    private void btnIrConfiguracionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIrConfiguracionMouseEntered
        btnIrConfiguracion.setBorderPainted(true);
    }//GEN-LAST:event_btnIrConfiguracionMouseEntered

    private void btnIrConfiguracionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIrConfiguracionMouseExited
        btnIrConfiguracion.setBorderPainted(false);
    }//GEN-LAST:event_btnIrConfiguracionMouseExited

    private void btnIrConfiguracionComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnIrConfiguracionComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIrConfiguracionComponentResized

    private void btnIrConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrConfiguracionActionPerformed
        String etiquetaConfiguracion = btnIrConfiguracion.getText();
        try
        {
            btnIrConfiguracion.setText("INICIANDO...");
            frmConfigurador frm = new frmConfigurador();
            frm.setVisible(true);
            estaAbierto = false;
            setVisible(false);
            leerConfiguracion(anchoMinimoformulariodefecto);
            configuracion.setModalidad(modalidad);

        } catch (IOException ex) {
            CConstantes.dialogo(ex.toString());
        } catch (Exception ex) {
            CConstantes.dialogo(ex.toString());
        } finally {
            //////System.out.println("Fin de proceso");
            lblCambiarTamano.setText(">");
            btnIrConfiguracion.setText(etiquetaConfiguracion);
        }
    }//GEN-LAST:event_btnIrConfiguracionActionPerformed

    private void dgvDocumentosMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dgvDocumentosMouseMoved
        if (dgvDocumentos.getRowCount() > 0)
        {
            java.awt.Point p = evt.getPoint();
            int rowIndex = dgvDocumentos.rowAtPoint(p);
            int colIndex = dgvDocumentos.columnAtPoint(p);
            
            if (rowIndex > -1 && colIndex > -1)
            {
                int realColumnIndex = dgvDocumentos.convertColumnIndexToModel(colIndex);
                if (realColumnIndex == 1)
                {
                    String dato = dgvDocumentos.getValueAt(rowIndex, colIndex).toString();
                    dgvDocumentos.setToolTipText(dato);
                }
                else if (realColumnIndex == 2)
                {
                    String dato = ((JProgressBar) dgvDocumentos.getValueAt(rowIndex, 2)).getString();
                    dgvDocumentos.setToolTipText(dato);
                }
                else if (realColumnIndex == 4)
                {
                    String dato = dgvDocumentos.getValueAt(rowIndex, 4).toString();
                    dgvDocumentos.setToolTipText(dato);
                }
                else
                    dgvDocumentos.setToolTipText(null);
            }
        }
    }//GEN-LAST:event_dgvDocumentosMouseMoved

    private void dgvDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dgvDocumentosMouseClicked
        if (dgvDocumentos.getRowCount() > 0)
            if (dgvDocumentos.getSelectedRow() > -1 && (dgvDocumentos.getSelectedColumn() > -1 && dgvDocumentos.getSelectedColumn() == 4))
            {
                String dato = String.valueOf(dgvDocumentos.getValueAt(dgvDocumentos.getSelectedRow(), 4));
                
                if (dato.toLowerCase().trim().equals("eliminar"))
                    eliminarFila();
                
                if (dato.toLowerCase().trim().equalsIgnoreCase("Ver firma"))
                    if (Desktop.isDesktopSupported())
                    {
                        try
                        {
                            Desktop.getDesktop().open(new File(dgvDocumentos.getValueAt(dgvDocumentos.getSelectedRow(), 5).toString()));
                            // Desktop.getDesktop().open(new File(firmar.getDoc_carpeta_salida_ruta()));
                        }
                        catch (IOException ex)
                        {
                            JOptionPane.showMessageDialog(this, ex);
                        }
                    }
            }
    }//GEN-LAST:event_dgvDocumentosMouseClicked

    private void btnCargarMasivoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarMasivoMouseEntered
        // TODO add your handling code here:
        btnCargarMasivo.setBorderPainted(true);
    }//GEN-LAST:event_btnCargarMasivoMouseEntered

    private void btnCargarMasivoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarMasivoMouseExited
        btnCargarMasivo.setBorderPainted(false);
    }//GEN-LAST:event_btnCargarMasivoMouseExited

    private void btnCargarMasivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarMasivoActionPerformed
        try
        {
            configuracion.setRutasdocumentos(importarArchivos(dgvDocumentos, configuracion));
            btnCargarMasivo.setText("ARCHIVO");
        }
        catch (IOException ex)
        {
            CConstantes.dialogo("Exception " + ex.getMessage());
        }
    }//GEN-LAST:event_btnCargarMasivoActionPerformed

    private void btnFirmarMasivamenteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFirmarMasivamenteMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFirmarMasivamenteMouseEntered

    private void btnFirmarMasivamenteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFirmarMasivamenteMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFirmarMasivamenteMouseExited

    private void btnFirmarMasivamenteComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnFirmarMasivamenteComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFirmarMasivamenteComponentResized

    private void btnFirmarMasivamenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirmarMasivamenteActionPerformed
        if (!validarDatos())
        {
            String sentido = lblCambiarTamano.getText();
            
            if (sentido.equals("<"))
                return;
            
            agrandarFormulario();
            return;
        }

        if (dgvDocumentos.getRowCount() == 0)
        {
            CConstantes.dialogo("No hay archivos para firmar.");
            return;
        }
        
        int totalFirmados = 0;
        for (int i = 0; i < dgvDocumentos.getRowCount(); i++)
        {
            JProgressBar progreso = (JProgressBar) dgvDocumentos.getValueAt(i, 2);
            String estado = progreso.getString();
            
            if (estado.equalsIgnoreCase("Firmado") || estado.equalsIgnoreCase("Error"))
                totalFirmados++;
        }
        
        if (dgvDocumentos.getRowCount() == totalFirmados)
        {
            CConstantes.dialogo("Los archivos ya fueron firmados");
            return;
        }
        
        chkDarConstancia.setSelected(false);
        configuracion.setModalidad(Configuracion.getServer());
        configuracion.setConCampoEmpresa(chkCampoEmpresa.isSelected());
        
        new frmElegirRepositorio(this,
                true,
                configuracion.getVg_clave_token(),
                configuracion.getVg_firma_visible_pagina() + "",
                configuracion.getVg_ruta_documento()
        ).setVisible(true);
    }//GEN-LAST:event_btnFirmarMasivamenteActionPerformed

    private void txtTsaUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTsaUrlActionPerformed
        configuracion.setVg_tsa_url(txtTsaUrl.getText());
        configuracion.setVg_tsa_url(txtTsaUsuario.getText());
        configuracion.setVg_tsa_url(new helperClass().encrypt(new String(txtTsaClave.getPassword())));
    }//GEN-LAST:event_txtTsaUrlActionPerformed

    private void cboPaginaActualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPaginaActualKeyPressed
        int caracter = (int) evt.getKeyChar();
        
        if (caracter == KeyEvent.VK_ENTER)
            if (cboPaginaActual.getSelectedItem().toString().trim().length() > 0)
            {
                pagina = Integer.parseInt(cboPaginaActual.getSelectedItem().toString());
                if (pagina > paginas || pagina < 0)
                    pagina = 1;
                
                viewPage();
            }
    }//GEN-LAST:event_cboPaginaActualKeyPressed

    private void cboPaginaActualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPaginaActualActionPerformed
        int paginaActual = 0;
        try
        {
            if (cboPaginaActual.getSelectedItem().toString().trim().length() > 0)
            {
                if (CConstantes.Yconvertiranumero(cboPaginaActual.getSelectedItem().toString()) == 0)
                {
                    CConstantes.dialogo("No hay página con el numero [" + cboPaginaActual.getSelectedItem() + "]");
                    cboPaginaActual.setSelectedItem(pagina);
                }
                paginaActual = Integer.parseInt(cboPaginaActual.getSelectedItem().toString());
            }
        }
        catch (Exception ex)
        {
            paginaActual = 0;
        }
        
        if (paginas > 0 && paginaActual > 0)
        {
            pagina = paginaActual;
            
            if (pagina > paginas || pagina < 0)
            {
                pagina = 1;
                return;
            }

            viewPage();
        }

        configuracion.setVg_firma_visible_pagina(pagina);
    }//GEN-LAST:event_cboPaginaActualActionPerformed

    private void cboPaginaActualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboPaginaActualKeyTyped
        char caracter = evt.getKeyChar();
        
        if (caracter < KeyEvent.VK_0 || caracter > KeyEvent.VK_9)
            evt.consume();

    }//GEN-LAST:event_cboPaginaActualKeyTyped

    private void btnPaginaPrimeraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaPrimeraMouseExited
        btnPaginaPrimera.setBorderPainted(false);
    }//GEN-LAST:event_btnPaginaPrimeraMouseExited

    private void btnPaginaAnteriorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaAnteriorMouseExited
        btnPaginaAnterior.setBorderPainted(false);
    }//GEN-LAST:event_btnPaginaAnteriorMouseExited

    private void btnPaginaSiguienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaSiguienteMouseExited
        btnPaginaSiguiente.setBorderPainted(false);
    }//GEN-LAST:event_btnPaginaSiguienteMouseExited

    private void btnPaginaUltimaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaUltimaMouseExited
        btnPaginaUltima.setBorderPainted(false);
    }//GEN-LAST:event_btnPaginaUltimaMouseExited

    private void btnBorrarTablaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarTablaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarTablaMouseEntered

    private void btnBorrarTablaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarTablaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarTablaMouseExited

    private void btnBorrarTablaComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnBorrarTablaComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrarTablaComponentResized

    private void btnBorrarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarTablaActionPerformed
        borrarTabla();
        limpiarContadores();
    }//GEN-LAST:event_btnBorrarTablaActionPerformed

    private void chkProtegerArchivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkProtegerArchivosActionPerformed
        if (!chkProtegerArchivos.isSelected())
            lyProtegerArchivos.setVisible(false);
        else
            lyProtegerArchivos.setVisible(true);
    }//GEN-LAST:event_chkProtegerArchivosActionPerformed

    private void chkAplicarValidacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAplicarValidacionesActionPerformed
        if (!chkAplicarValidaciones.isSelected())
            lyaplicarValidaciones.setVisible(false);
        else
            lyaplicarValidaciones.setVisible(true);
    }//GEN-LAST:event_chkAplicarValidacionesActionPerformed

    private void txtTamanioFuenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTamanioFuenteFocusLost
        configuracion.setVg_firma_visible_tamanio_fuente(CConstantes.Yconvertiranumerofloat(txtTamanioFuente.getText()));
    }//GEN-LAST:event_txtTamanioFuenteFocusLost

    private void txtTamanioFuenteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTamanioFuenteFocusGained
        configuracion.setVg_firma_visible_tamanio_fuente(CConstantes.Yconvertiranumerofloat(txtTamanioFuente.getText()));
    }//GEN-LAST:event_txtTamanioFuenteFocusGained

    private void btnAbrirCarpetaFirmadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirCarpetaFirmadosActionPerformed
        try
        {
            //Runtime.getRuntime().exec("explorer C:\bin");
            Desktop.getDesktop().open(new File(configuracion.getVg_carpeta_salida()));
            // Desktop.getDesktop().open(new File(firmar.getDoc_carpeta_salida_ruta()));
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_btnAbrirCarpetaFirmadosActionPerformed

    private void chkCalNumberActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkCalNumberActionPerformed
    {//GEN-HEADEREND:event_chkCalNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCalNumberActionPerformed

    private void chkCampoAreaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkCampoAreaActionPerformed
    {//GEN-HEADEREND:event_chkCampoAreaActionPerformed
        configuracion.setConCarmpoArea(chkCampoArea.isSelected());
    }//GEN-LAST:event_chkCampoAreaActionPerformed

    private void btnZoomInMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnZoomInMouseEntered
    {//GEN-HEADEREND:event_btnZoomInMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnZoomInMouseEntered

    private void btnZoomInMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnZoomInMouseExited
    {//GEN-HEADEREND:event_btnZoomInMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnZoomInMouseExited

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnZoomInActionPerformed
    {//GEN-HEADEREND:event_btnZoomInActionPerformed
        if (cPanel.getDocument() == null)
            return;
        
        this.tipoZoom = 1;
        ZoomDocumento(1.2f, tipoZoom);
    }//GEN-LAST:event_btnZoomInActionPerformed

    private void btnZoomOutMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnZoomOutMouseEntered
    {//GEN-HEADEREND:event_btnZoomOutMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnZoomOutMouseEntered

    private void btnZoomOutMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnZoomOutMouseExited
    {//GEN-HEADEREND:event_btnZoomOutMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnZoomOutMouseExited

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnZoomOutActionPerformed
    {//GEN-HEADEREND:event_btnZoomOutActionPerformed
        if (cPanel.getDocument() == null)
            return;
        
        this.tipoZoom = 2;
        ZoomDocumento(1.2f, tipoZoom);
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void chkEtiquetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEtiquetaActionPerformed
        // TODO add your handling code here:
        validarEtiquetaFirma();
    }//GEN-LAST:event_chkEtiquetaActionPerformed

    private void txtEtiquetaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEtiquetaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEtiquetaFocusGained

    private void txtEtiquetaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEtiquetaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEtiquetaFocusLost

    private void pnlBarra3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBarra3MouseClicked
              // TODO add your handling code here:
    }//GEN-LAST:event_pnlBarra3MouseClicked

    private void lblMinimizar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimizar2MouseClicked
        // TODO add your handling code here:
        if(lblMinimizar2.getText().equals("Max"))
            maximizarVentana();
        else
            minimizarVentana();
    }//GEN-LAST:event_lblMinimizar2MouseClicked

    private void chkCampoEmpresaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkCampoEmpresaActionPerformed
    {//GEN-HEADEREND:event_chkCampoEmpresaActionPerformed
        
    }//GEN-LAST:event_chkCampoEmpresaActionPerformed

    private void txtTextoFIrmanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTextoFIrmanteActionPerformed
        configuracion.setTextoFirma1(txtTextoFIrmante.getText());// TODO add your handling code here:
    }//GEN-LAST:event_txtTextoFIrmanteActionPerformed

    private void chkCampoArea1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCampoArea1ActionPerformed
        configuracion.setConCarmpoAreaFH(chkCampoArea1.isSelected());// TODO add your handling code here:
    }//GEN-LAST:event_chkCampoArea1ActionPerformed

    private void chkIncluirQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIncluirQRActionPerformed
        configuracion.setConQR(chkIncluirQR.isSelected());// TODO add your handling code here:
    }//GEN-LAST:event_chkIncluirQRActionPerformed
    
    private void ZoomDocumento(double factor, int tipo)
    {
        if (tipo != 1 && (Zoom < 0.00001 /*|| Zoom > 20*/))
            return;
             
        double w, h;
        float zoom = Zoom;

        if (tipo == 1)
        {
            w = cPanel.getWidth() * factor;
            h = cPanel.getHeight() * factor;
            zoom *= factor;
        }
        else
        {
            w = cPanel.getWidth() / factor;
            h = cPanel.getHeight() / factor;
            zoom /= factor;
        }
        
        if (w < pnlPrincipal.getWidth() / 1.2 && h < pnlPrincipal.getHeight() / 1.2)
            return;
        
        Zoom = zoom;
        setScrollPane();
        
       // pnlPrincipal.setBounds(0, 0,(int)Math.round(w), (int)Math.round(h));
       // pnlPrincipal.setPreferredSize(new Dimension((int)Math.round(w), (int)Math.round(h)));
        cPanel.setBounds(0, 0,(int)Math.round(w), (int)Math.round(h));
        cPanel.setPreferredSize(new Dimension((int)Math.round(w), (int)Math.round(h)));
       
        if (pagina > 0)
            cPanel.setPage(pagina - 1, Zoom);
        else
            cPanel.setPage(0, Zoom);
    
        repaint();
    }
    
    private void setScrollPane()
    {
        pnlPrincipal.remove(scrollPane);
        scrollPane = new JScrollPane(cPanel);
        pnlPrincipal.add(scrollPane);
       
        MouseListener[] m = scrollPane.getMouseListeners();
        
        if (scrollPane.getMouseListeners().length == 0)
        {
            scrollPane.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent evt)
                {
                    mousemove(evt);
                }

                @Override
                public void mouseReleased(MouseEvent evtEvent)
                {
                    mousereleased(evtEvent);
                }
            });

            scrollPane.addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(MouseEvent evtEvent)
                {
                    mousedragged(evtEvent);
                }
            });
        }
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 0, pnlPrincipal.getWidth(), pnlPrincipal.getHeight());
    }
    
     private void maximizarVentana()
     {
        this.setExtendedState(MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        double c = System.getProperty("os.name").toLowerCase().contains("win") ? 1.4 : 1.8;
        
        double wt = screenSize.width/2;
        double ht = screenSize.height/c;
        
        GroupLayout canvasPrincipalLayout = new javax.swing.GroupLayout(canvasPrincipal);
        canvasPrincipal.setBounds(canvasPrincipal.getX(), canvasPrincipal.getY(), this.getWidth(), this.getHeight());
        canvasPrincipal.setLayout(canvasPrincipalLayout);
        
        canvasPrincipalLayout.setHorizontalGroup(
            canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, canvasPrincipalLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)

                    .addComponent(pnlPaginas)
                    .addComponent(pnlPrincipal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, (int)wt, Short.MAX_VALUE)
                    .addComponent(pnlCoordenadas))
                .addGap(0, 0, 0)
                .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        canvasPrincipalLayout.setVerticalGroup(
            canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(canvasPrincipalLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(canvasPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                                .addComponent(pnlPaginas)
                                .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, (int)ht, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(pnlCoordenadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                )
                            .addGroup(canvasPrincipalLayout.createSequentialGroup()
                                .addGap(163, 163, 163)
                                .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        
        canvasPrincipal.setBounds(0, 0, (int)(wt * 2), (int)(ht * c));
        
        pnlpadre.setBounds(0, 0, this.getWidth(), this.getHeight());
        pnlPrincipal.setBounds(pnlPrincipal.getX(), pnlPrincipal.getY(), (int)wt, (int)ht);
        
        cPanel.setBounds(0, 0, (int)wt, (int)ht);
        lblCambiarTamano.setVisible(true);
        pnlConfiguracion.setBounds((int)wt, canvasPrincipal.getY(), pnlConfiguracion.getWidth(), canvasPrincipal.getHeight());

        try
        {
            if (cPanel.getAnchoReal(pagina - 1) < cPanel.getAltoReal(pagina - 1) || 
                cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
            {
                double minfactor = 1.6 * (double)cPanel.getAltoReal(pagina - 1) / (double)cPanel.getAnchoReal(pagina - 1);

                if(cPanel.getRotacionPagina(pagina - 1) == 90 || cPanel.getRotacionPagina(pagina - 1) == 270)
                {
                    cPanel.setBounds(0, 0, (int)(cPanel.getHeight() * minfactor), cPanel.getHeight());
                    cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() * minfactor), cPanel.getHeight()));
                }
                else
                {
                    cPanel.setBounds(0, 0, (int)(cPanel.getHeight() / minfactor), cPanel.getHeight());
                    cPanel.setPreferredSize(new Dimension((int)(cPanel.getHeight() / minfactor), cPanel.getHeight()));
                }
            }
        }
        catch (Exception e) {}

        setScrollPane();
        lblMinimizar2.setText("Normal");
        resetPdfPreview();
        ZoomDocumento(1.2, 1);
    }
    
    int eliminarFila()
    {
        int index = dgvDocumentos.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) dgvDocumentos.getModel();
        dtm.removeRow(index);
        configuracion.getRutasdocumentos().remove(index);
        dgvDocumentos.setModel(dtm);
        return dgvDocumentos.getRowCount();
    }

    private static String[] leerConfig() throws URISyntaxException, IOException
    {
        final File f = new File(".");
        Path appData = f.toPath();
        String ruta_padre = appData.toString();
        
        Path carpetaPropiedades = Paths.get(ruta_padre).resolve("propiedades");
        String archivoConfig = carpetaPropiedades.resolve("config.properties").toString();

        if (!new File(archivoConfig).exists())
        {
            JOptionPane.showMessageDialog(null, "Error al ubicar el archivo de configuración [" + archivoConfig + "]", null, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        Properties propertiesConfig = new Properties();
        try(FileInputStream fis = new FileInputStream(archivoConfig))
        {
            propertiesConfig.load(fis);
            fis.close();
        }
        
        String ruta_jdk = propertiesConfig.getProperty("java.jdk.bin") + File.separator;
        return new String[]{ruta_padre, ruta_jdk};
    }

    public void cambiarColoFondo(JButton btn)
    {
        btn.setBackground(new Color(255, 0, 0));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Metal".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Documento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Documento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Documento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Documento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try
                {
                    frmFirmador frmDocumento;
                    String documentoRuta = "";
                    String modalidad = "";
                    String carpetaEntrada = "";
                    String carpetaDestino = "";

                    if (args.length == 0)
                    {
                        throw new Exception("No ha ingresado argumentos para iniciar el proceso de firma.");
                    }
                    else if (args.length == 1)
                    {
                        if (args[0].equals(Configuracion.getServer()) || args[0].equals(Configuracion.getUser()))
                            modalidad = args[0];
                        else
                            documentoRuta = args[0];
                    }
                    else if (args.length == 2)
                    {
                        //Si hay dos argumentos 0 => <-s || -u> 1 => <rutaEntrada>
                        modalidad = args[0];
                        documentoRuta = modalidad.equals("-u") ? args[1] : "";
                        carpetaEntrada = args[1];
                    }
                    else if (args.length == 3)
                    {
                        //Si hay tres argumentos 0 => <-s || -u> 1 => <rutaEntrada Salida> 
                        modalidad = args[0];
                        carpetaEntrada = args[1];
                        carpetaDestino = args[2];
                    }
                    
                    frmDocumento = new frmFirmador(documentoRuta, modalidad, carpetaEntrada, carpetaDestino, new Point(10, 10), new Point(10, 10));
                    frmDocumento.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(frmFirmador.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(frmFirmador.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(frmFirmador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAbrirCarpetaFirmados;
    private javax.swing.JToggleButton btnBorrarTabla;
    public javax.swing.JButton btnCargar;
    public javax.swing.JButton btnCargarMasivo;
    private javax.swing.JButton btnExaminar;
    private javax.swing.JToggleButton btnFirmarMasivamente;
    private javax.swing.JToggleButton btnGrabarCambios;
    private javax.swing.JToggleButton btnIrConfiguracion;
    public javax.swing.JButton btnLogo;
    public javax.swing.JButton btnPaginaAnterior;
    public javax.swing.JButton btnPaginaPrimera;
    public javax.swing.JButton btnPaginaSiguiente;
    public javax.swing.JButton btnPaginaUltima;
    public javax.swing.JButton btnZoomIn;
    public javax.swing.JButton btnZoomOut;
    private javax.swing.JPanel canvasPrincipal;
    private javax.swing.JLayeredPane capaTotalArchivosErrados;
    private javax.swing.JLayeredPane capaTotalArchivosFirmados;
    private javax.swing.JLayeredPane capaTotalArchivosListados;
    protected static javax.swing.JComboBox cboEstiloFirma;
    protected static javax.swing.JComboBox cboFuente;
    private javax.swing.JComboBox cboPaginaActual;
    protected static javax.swing.JComboBox cboalgoritmo;
    private javax.swing.JCheckBox chkAplicarValidaciones;
    public static javax.swing.JCheckBox chkCalNumber;
    public static javax.swing.JCheckBox chkCampoArea;
    public static javax.swing.JCheckBox chkCampoArea1;
    public static javax.swing.JCheckBox chkCampoEmpresa;
    protected static javax.swing.JCheckBox chkCerrarDocumento;
    protected static javax.swing.JCheckBox chkConImagen;
    protected static javax.swing.JCheckBox chkConNoRepudio;
    protected static javax.swing.JCheckBox chkConSello;
    protected static javax.swing.JCheckBox chkConTsl;
    protected static javax.swing.JCheckBox chkDarConstancia;
    protected static javax.swing.JCheckBox chkEnTodasPaginas;
    protected static javax.swing.JCheckBox chkEtiqueta;
    public static javax.swing.JCheckBox chkFirmaVisible;
    protected static javax.swing.JCheckBox chkIncluirQR;
    private javax.swing.JCheckBox chkProtegerArchivos;
    public static javax.swing.JTable dgvDocumentos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblAlto;
    private javax.swing.JLabel lblAncho;
    private javax.swing.JLabel lblCambiarTamano;
    private javax.swing.JLabel lblCargados;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lblErrados;
    private javax.swing.JLabel lblFirmados;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblMinimizar;
    private javax.swing.JLabel lblMinimizar2;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JLabel lblPaginaHasta;
    private static javax.swing.JLabel lblTitulo;
    private static javax.swing.JLabel lblTotalErrados;
    private static javax.swing.JLabel lblTotalFirmados;
    private static javax.swing.JLabel lblTotalListados;
    private javax.swing.JLabel lblX;
    private javax.swing.JLabel lblY;
    private javax.swing.JLayeredPane lyFirmaVisible;
    private javax.swing.JLayeredPane lyProtegerArchivos;
    private javax.swing.JLayeredPane lySelloDeTiempo;
    private javax.swing.JLayeredPane lyaplicarValidaciones;
    private javax.swing.JPanel pnlBarra3;
    private javax.swing.JPanel pnlConfiguracion;
    private javax.swing.JLayeredPane pnlCoordenadas;
    private javax.swing.JPanel pnlPaginas;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JPanel pnlProcesoDeListado;
    private javax.swing.JPanel pnlpadre;
    private javax.swing.JScrollPane scDgvDocumentos;
    private javax.swing.JScrollPane scProcesoListado;
    private javax.swing.JTabbedPane tbDocumentosFirmar;
    protected static javax.swing.JTextField txtEtiqueta;
    protected static javax.swing.JTextField txtPagina;
    public static javax.swing.JTextArea txtProcesoListado;
    public static javax.swing.JTextField txtTamanioFuente;
    private javax.swing.JTextField txtTextoFIrmante;
    protected static javax.swing.JPasswordField txtTsaClave;
    protected static javax.swing.JTextField txtTsaUrl;
    protected static javax.swing.JTextField txtTsaUsuario;
    protected static javax.swing.JTextField txtTslUrls;
    protected static javax.swing.JTextField txtrutaImagen;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (paginas == 0)
            return;
        
        if (e.getSource() == btnPaginaSiguiente)
        {
            pagina += 1;

            if (pagina > paginas || pagina < 1)
                pagina = paginas;

            viewPage();
        }

        if (e.getSource() == btnPaginaAnterior)
        {
            pagina -= 1;
            
            if (pagina > paginas || pagina < 1)
                pagina = 1;

            viewPage();
        }
        
        if (e.getSource() == btnPaginaPrimera)
        {
            pagina = 1;
            viewPage();
        }
        
        if (e.getSource() == btnPaginaUltima)
        {
            pagina = paginas;
            viewPage();
        }
    }

    public static void contarListados() {
        lblTotalListados.setText((Integer.parseInt(lblTotalListados.getText().trim()) + 1) + " ");
    }

    public static void contarFirmados() {
        lblTotalFirmados.setText((Integer.parseInt(lblTotalFirmados.getText().trim()) + 1) + " ");
    }

    public static void contarErrados() {
        lblTotalErrados.setText((Integer.parseInt(lblTotalErrados.getText().trim()) + 1) + " ");
    }

    public static void limpiarContadores() {
        lblTitulo.setText("");
        lblTotalListados.setText("0 ");
        lblTotalFirmados.setText("0 ");
        lblTotalErrados.setText("0 ");
        txtProcesoListado.setText("");
    }
    
    void validarEtiquetaFirma()
    {
        if (chkEtiqueta.isSelected())
        {
            txtEtiqueta.setEnabled(true);
            configuracion.setEtiqueta_firma(txtEtiqueta.getText());
        }
        else
        {
            txtEtiqueta.setEnabled(false);
            configuracion.setEtiqueta_firma("");
        }
        
        configuracion.setVg_validar_con_tsl(chkConTsl.isSelected());
    }

}
