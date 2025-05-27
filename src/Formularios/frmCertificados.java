package Formularios;

import Clases.Certificado;
import Clases.Configuracion;
import Global.CConstantes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Clases.Repositorio;
import Entidades.Firmante;
import static Formularios.frmConfigurador.tblFirmante;
import Global.CLog;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class frmCertificados extends javax.swing.JDialog {

    public static int cerrarventana = 1;
    private String vg_tiporepositorio;
    private String _archivo;
    private String _clave;
    int index = 1;
    int i = 0;
    int NumeroDeCertificados = 0;
    boolean Conbuffer;

    public frmCertificados() { }

    DefaultTableModel agregar_firmador_a_tabla(Configuracion pc_certificado)
    {
        try
        {
            DefaultTableModel v_modelo = (DefaultTableModel) tblFirmante.getModel();
            DefaultTableCellRenderer renderizadorDeCeldas = new DefaultTableCellRenderer();
            Object[] v_fila = new Object[3];
            File f = new File(".");
            String laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
            String icon_ruta = laRuta + "icono_ok.png";

            ImageIcon img = new ImageIcon(getClass().getResource("/icono_ok.png"));
            renderizadorDeCeldas.setIcon(img);
            renderizadorDeCeldas.setHorizontalAlignment(0);
            tblFirmante.getColumnModel().getColumn(3).setCellRenderer(renderizadorDeCeldas);
            v_fila[0] = pc_certificado.getFirmanteElegido().getCertificado().getCn();
            v_fila[1] = pc_certificado.getFirmanteElegido().getCertificado().getAlias();
            v_fila[2] = pc_certificado.getFirmanteElegido().getCertificado().getFechafinal();
            
            if (v_modelo.getRowCount() > 0)
            {
                v_modelo.removeRow(0);
                v_modelo.insertRow(0, v_fila);
            }
            else
                v_modelo.addRow(v_fila);
            
            return v_modelo;
        }
        catch (IOException ex)
        {
            Logger.getLogger(frmCertificados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public frmCertificados(JFrame parent, boolean modal, String tiporepositorio, String archivo, String clave)
    {
        super(parent, modal);
        try
        {
            initComponents();
            vg_tiporepositorio = tiporepositorio;
            _archivo = archivo;
            _clave = clave;
            ListarCertificados();
        }
        catch (CertificateEncodingException | CRLException | ParserConfigurationException | SAXException ex)
        {
//            Logger.getLogger(frmCertificados.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLocationRelativeTo(parent);
    }

    public frmCertificados(JDialog parent, boolean modal, String tiporepositorio, String archivo, String clave)
    {
        super(parent, modal);
        try
        {
            initComponents();
            vg_tiporepositorio = tiporepositorio;
            _archivo = archivo;
            _clave = clave;
            ListarCertificados();
        } catch (CertificateEncodingException | CRLException | ParserConfigurationException | SAXException ex) {
//            Logger.getLogger(frmCertificados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ArrayList<JPanel> paneles = new ArrayList<>();

    public void ListarCertificados() throws CertificateEncodingException, CRLException, ParserConfigurationException, SAXException {
        try {
            List<Firmante> LtDeCertificados;
            LtDeCertificados = ObtenerCertificados(_archivo, _clave);
            Dimension d = new Dimension();
            //LISTAR
            int AcumuladorTamano;
            int pointx;

            if (LtDeCertificados != null && LtDeCertificados.size() > 0) {
                NumeroDeCertificados = LtDeCertificados.size();
                Repositorio.cargo = true;
            }
            else
            {
                lblMensajeValidacion.setText(CLog.getMensaje_error());
                lblMensajeValidacion1.setText("No hay certificados.");
                return;
            }
            
            if (NumeroDeCertificados >= 3)
                pointx = 0;
            else
                pointx = 0;

            File f = new File(".");

            String laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
            String icon_ruta_error = laRuta + "CertificadoLogo.png";
            ImageIcon img_certificado_logo = new ImageIcon(getClass().getResource("/CertificadoLogo.png"));

            for (int j = 0; j < NumeroDeCertificados; j++)
            {
                final JPanel ctrl_panel_certificado = new javax.swing.JPanel();
                ctrl_panel_certificado.setName("Panel" + j);
                SpringLayout layout_certificado = new SpringLayout();

                ctrl_panel_certificado.setLayout(layout_certificado);
                ctrl_panel_certificado.setSize(403, 120);
                ctrl_panel_certificado.setLocation(5, i);
//                JOptionPane.showMessageDialog(this,i);
                ctrl_panel_certificado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(154, 207, 242)));
                JButton ctrl_btn_Imagen_Certificado = new JButton();
                ctrl_btn_Imagen_Certificado.setIcon(img_certificado_logo);
                ctrl_btn_Imagen_Certificado.setBackground(java.awt.SystemColor.text);
                ctrl_btn_Imagen_Certificado.setBorder(null);
                ctrl_btn_Imagen_Certificado.setContentAreaFilled(false);
                final JLabel ctrl_lbl_alias = new JLabel("Certificado");
//                final Certificado certificado = new Certificado();
                final JLabel ctrl_lbl_cargo_nombre = new JLabel("titulo_o_Cargo" + (j));
                final JLabel ctrl_lbl_certificado_empresa_etiqueta = new JLabel("lbl_emp");
                final JLabel ctrl_lbl_certificado_empresa = new JLabel("Organizacion_o_Caoporacion" + (j));
                final JLabel ctrl_lbl_certificado_cn = new JLabel("lblValorEmisor");
                final JLabel ctrl_lbl_certificado_dn = new JLabel("lblValorDn");
                final JLabel ctrl_lbl_certificado_configuracion_ver = new JLabel("lblVerCertificado");
                final JLabel ctrl_lbl_cargo_etiqueta = new JLabel("lblCargo");

                final JLabel ctrl_lbl_fecha_inicio_etiqueta = new JLabel("FechaInicio");
                final JLabel ctrl_lbl_fecha_inicio_valor = new JLabel("lblValorFechaInicio");
                final JLabel ctrl_lbl_fecha_fin_etiqueta = new JLabel("FechaFinal:");
                final JLabel ctrl_lbl_fecha_fin_valor = new JLabel("lblValorFechaFinal");

                final JLabel ctrl_lbl_certificado_version = new JLabel("Version" + (j));
                final JLabel ctrl_lbl_certificado_serie = new JLabel("Serie" + (j));
                final JLabel ctrl_lbl_certificado_usos = new JLabel("Usos" + (j));
                final JLabel ctrl_lbl_certificado_Root = new JLabel("Root" + (j));

                ctrl_lbl_certificado_dn.setText("");
                ctrl_lbl_certificado_cn.setFont(new Font("Arial", 1, 14));
                ctrl_lbl_certificado_empresa.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_certificado_empresa_etiqueta.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_fecha_fin_etiqueta.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_fecha_inicio_etiqueta.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_fecha_inicio_valor.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_fecha_fin_valor.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_certificado_configuracion_ver.setFont(new Font("Calibri", 0, 13));
                ctrl_lbl_cargo_etiqueta.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_cargo_nombre.setFont(new Font("Calibri", 0, 12));
                ctrl_lbl_certificado_configuracion_ver.setForeground(Color.BLUE);
                ctrl_lbl_cargo_nombre.setText(LtDeCertificados.get(j).getCertificado().getCargo());

                ctrl_lbl_certificado_empresa_etiqueta.setText("Empresa : ");

                ctrl_lbl_certificado_empresa.setText(LtDeCertificados.get(j).getCertificado().getEmpresa());
                ctrl_lbl_certificado_empresa.setPreferredSize(new Dimension(250, 20));

                ctrl_lbl_certificado_cn.setText(LtDeCertificados.get(j).getCertificado().getCn());
                ctrl_lbl_certificado_cn.setPreferredSize(new Dimension(300, 20));
                ctrl_lbl_certificado_cn.setToolTipText(ctrl_lbl_certificado_cn.getText());
                ctrl_lbl_fecha_inicio_etiqueta.setText("Válido desde : ");
                ctrl_lbl_fecha_inicio_valor.setText(" " + LtDeCertificados.get(j).getCertificado().getFechaInicial());
                ctrl_lbl_alias.setText(LtDeCertificados.get(j).getCertificado().getAlias());
                ctrl_lbl_fecha_fin_etiqueta.setText("Hasta : ");
                ctrl_lbl_cargo_etiqueta.setText("Cargo : ");

                ctrl_lbl_fecha_fin_valor.setText("  " + LtDeCertificados.get(j).getCertificado().getFechafinal());
                ctrl_lbl_certificado_configuracion_ver.setText("Haga click para ver la configuracion de su certificado");

                ctrl_lbl_certificado_version.setText(LtDeCertificados.get(j).getCertificado().getVersion());
                ctrl_lbl_certificado_Root.setText(LtDeCertificados.get(j).getCertificado().getDnRoot());
                ctrl_lbl_certificado_serie.setText(LtDeCertificados.get(j).getCertificado().getSerie());
                ctrl_lbl_certificado_usos.setText(LtDeCertificados.get(j).getCertificado().getUso());

                ctrl_panel_certificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_btn_Imagen_Certificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                //  lblEmpresaEmisora.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                ctrl_lbl_certificado_cn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_lbl_fecha_inicio_etiqueta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_lbl_fecha_inicio_valor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_lbl_fecha_fin_etiqueta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_lbl_fecha_fin_valor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_lbl_certificado_configuracion_ver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                ctrl_lbl_cargo_etiqueta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                ctrl_panel_certificado.add(ctrl_btn_Imagen_Certificado);
                ctrl_panel_certificado.add(ctrl_lbl_certificado_cn);
                ctrl_panel_certificado.add(ctrl_lbl_certificado_empresa_etiqueta);
                ctrl_panel_certificado.add(ctrl_lbl_certificado_empresa);
                ctrl_panel_certificado.add(ctrl_lbl_fecha_inicio_etiqueta);
                ctrl_panel_certificado.add(ctrl_lbl_fecha_inicio_valor);
                ctrl_panel_certificado.add(ctrl_lbl_fecha_fin_etiqueta);
                ctrl_panel_certificado.add(ctrl_lbl_fecha_fin_valor);
                ctrl_panel_certificado.add(ctrl_lbl_certificado_configuracion_ver);
                ctrl_panel_certificado.add(ctrl_lbl_cargo_etiqueta);
                ctrl_panel_certificado.add(ctrl_lbl_cargo_nombre);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_btn_Imagen_Certificado, 15, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_btn_Imagen_Certificado, 0, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_certificado_cn, 5, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_certificado_cn, 75, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_cargo_etiqueta, 35, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_cargo_etiqueta, 75, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_cargo_nombre, 35, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_cargo_nombre, 110, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_certificado_empresa_etiqueta, 60, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_certificado_empresa_etiqueta, 75, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_certificado_empresa, 58, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_certificado_empresa, 132, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_fecha_inicio_etiqueta, 85, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_fecha_inicio_etiqueta, 75, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_fecha_inicio_valor, 85, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_fecha_inicio_valor, 155, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_fecha_fin_etiqueta, 85, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_fecha_fin_etiqueta, 250, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_fecha_fin_valor, 85, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_fecha_fin_valor, 285, SpringLayout.WEST, ctrl_panel_certificado);

                layout_certificado.putConstraint(SpringLayout.NORTH, ctrl_lbl_certificado_configuracion_ver, 103, SpringLayout.NORTH, ctrl_panel_certificado);
                layout_certificado.putConstraint(SpringLayout.WEST, ctrl_lbl_certificado_configuracion_ver, 75, SpringLayout.WEST, ctrl_panel_certificado);

                if (j == 0) {
//                    ctrl_panel_certificado.setBackground(new Color(204, 237, 255, 255));
                    ctrl_panel_certificado.setBackground(new Color(221, 240, 255));
                    paneles.add(ctrl_panel_certificado);
                } else {
                    ctrl_panel_certificado.setBackground(new Color(255, 255, 255));
                }

                PnlCertificados.add(ctrl_panel_certificado);
                ctrl_panel_certificado.addMouseListener(new java.awt.event.MouseAdapter()
                {
                    public void mouseClicked(java.awt.event.MouseEvent evt)
                    {
                        paneles.get(0).setBackground(new Color(255, 255, 255));
                        paneles.add(0, ctrl_panel_certificado);
                        paneles.get(0).setBackground(new Color(221, 240, 255));
                        
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try {
                                    lblMensajeValidacion.setText(CConstantes.VALIDANDOCERTIFICADO);
                                    String v_validacion_respuesta;
                                    /*VALIDANDO CERTIFICADOS*/

                                    if (!frmConfigurador.repositorios.validarAliasCertificado(ctrl_lbl_alias.getText()))
                                    {
                                        lblMensajeValidacion.setText("Alias vàlido");
                                        return;
                                    }
                                    
                                    Firmante firmanteAux;
                                    List<Firmante> firmantes;
                                    firmanteAux = null;
                                    firmantes = frmConfigurador.repositorios.getFirmantes();

                                    for (Firmante firmante : firmantes) {
                                        if (firmante.getCertificado().getAlias().equalsIgnoreCase(ctrl_lbl_alias.getText())) {
                                            firmanteAux = firmante;
                                            break;
                                        }
                                    }
                                    
                                    if (firmanteAux == null)
                                        throw new Exception("No se han listado los certificados");
                                    
                                    frmConfigurador.repositorios.setFirmanteElegido(firmanteAux);

                                    v_validacion_respuesta = Metodos.ValidarCertificados.filtrarCertificado(
                                            frmConfigurador.repositorios.getFirmanteElegido().getCertificado().getCertificadosConfianza(), new Date(),
                                            frmConfigurador.configuracion.getVg_url_tsl(), 
                                            frmConfigurador.configuracion.getVg_carpeta_salida_crl_tsl() + File.separator, 
                                            frmConfigurador.configuracion.getUrlProxy(), frmConfigurador.configuracion.getPuertoProxy(),
                                            "" + frmConfigurador.configuracion.isVg_validar_con_no_repudio());
                                    
                                    if (v_validacion_respuesta.equals("0"))
                                    {
                                        if (cerrarventana == 0)
                                            return;
                                        
                                        frmConfigurador.txtResultado.setText(Metodos.ValidarCertificados.getMensajeDeError());
                                        frmConfigurador.configuracion.setTipoRepositorio(vg_tiporepositorio);
                                        frmConfigurador.configuracion.setFirmanteElegido(frmConfigurador.repositorios.getFirmanteElegido());
                                        frmConfigurador.configuracion.setVg_clave_certificado(_clave);
                                        agregar_firmador_a_tabla(frmConfigurador.configuracion);
                                        Repositorio.cerrarventana = 1;
                                        setVisible(false);
                                        dispose();
                                    }
                                    else {
//                                    lblMensajeValidacion.setText(CConstantes.CERTIFICADO_NO_VALIDO);
                                        frmMensaje.mensaje_mostrar(frmCertificados.this, Metodos.ValidarCertificados.Mensaje());
                                    }
                                }
                                catch (Exception ex)
                                {
                                    frmMensaje.mensaje_mostrar(frmCertificados.this, ex.toString());
                                }

                            }
                        }).start();
                    }

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt)
                    {
                        paneles.get(0).setBackground(new Color(255, 255, 255));
                        paneles.add(0, ctrl_panel_certificado);
                        paneles.get(0).setBackground(new Color(221, 240, 255));
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt)
                    {
                        //jp.setBackground(Color.white);
                    }
                });
                
                ctrl_lbl_certificado_configuracion_ver.addMouseListener(new java.awt.event.MouseAdapter()
                {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt)
                    {
                        if (frmConfigurador.repositorios.validarAliasCertificado(ctrl_lbl_alias.getText()))
                        {
                            new frmCertificadoPropiedades(frmCertificados.this, true, frmConfigurador.repositorios.getFirmanteElegido().getCertificado()).setVisible(true);
                        }
                    }

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt)
                    {
                        paneles.get(0).setBackground(new Color(255, 255, 255));
                        paneles.add(0, ctrl_panel_certificado);
                        paneles.get(0).setBackground(new Color(221, 240, 255));
                        ctrl_lbl_certificado_configuracion_ver.setForeground(Color.red);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt)
                    {
                        ctrl_lbl_certificado_configuracion_ver.setForeground(Color.blue);
                    }
                });

                ctrl_panel_certificado.setVisible(true);
                i += ctrl_panel_certificado.getHeight();
                AcumuladorTamano = ctrl_panel_certificado.getHeight() + ctrl_panel_certificado.getY();
                ctrl_panel_certificado.setLocation(new Point(pointx, AcumuladorTamano - 120));
                d.setSize(PnlCertificados.getWidth() - 200, AcumuladorTamano);
                PnlCertificados.setPreferredSize(new Dimension(d));
                JSCCER.setPreferredSize(new Dimension(d));
                JSCCER.repaint();
            }
        }
        catch (IOException ex) {
            Logger.getLogger(frmCertificados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    List<Firmante> ObtenerCertificados(String Token, String clave) throws CertificateEncodingException, CRLException, ParserConfigurationException, SAXException {
        if (vg_tiporepositorio.equals(Repositorio.TOKEN))
        {
            String libreriaToken = "";
            
            for (int j = 0; j < frmConfigurador.configuracion.getVg_lista_librerias().size(); j++)
            {
                libreriaToken = frmConfigurador.configuracion.getVg_lista_librerias().get(j).getRuta();
                frmConfigurador.repositorios.cargarCertificado(clave, vg_tiporepositorio, libreriaToken);
                List<String> vgl_alias = frmConfigurador.repositorios.obtenerAliases();
                
                if (vgl_alias != null && !vgl_alias.isEmpty())
                    break;
                
                if (frmConfigurador.repositorios.isLibreria_con_error())
                    break;
            }
            
            if (frmConfigurador.repositorios.getFirmantes() != null && frmConfigurador.repositorios.getFirmantes().size() > 0)
            {
                frmConfigurador.configuracion.setVg_ruta_libreria_nombre(new File(libreriaToken).getName());
                frmConfigurador.configuracion.setVg_ruta_libreria(libreriaToken);
                return frmConfigurador.repositorios.getFirmantes();
            }
        }
        else
        {
            frmConfigurador.repositorios.cargarCertificado(clave, vg_tiporepositorio, frmConfigurador.configuracion.getVg_ruta_certificado());
        }

        //Documento.repositorios.cargarCertificado(clave, vg_tiporepositorio,Token);
        frmConfigurador.repositorios.obtenerAliases();
        return frmConfigurador.repositorios.getFirmantes();
    }

    private static final String[] keyUsageLabels = new String[]
    {
        "digitalSignature", "nonRepudiation", "keyEncipherment",
        "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign",
        "encipherOnly", "decipherOnly"};

    static List<String> getKeyUsage(final X509Certificate cert)
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
                        ret.add(keyUsageLabels[i]);
                    else
                        ret.add(String.valueOf(i));
                }
            }
            return ret;
        }
        else
            return null;
    }

    protected void cambiarColor(Object obj, Color colorfondo, Color colorletra) {
        if (obj instanceof javax.swing.JLabel) {
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        } else if (obj instanceof JButton) {
            ((javax.swing.JButton) obj).setBackground(colorfondo);
            ((javax.swing.JButton) obj).setForeground(colorletra);
            ((javax.swing.JButton) obj).setOpaque(true);
            ((javax.swing.JButton) obj).setBorder(javax.swing.BorderFactory.createLineBorder(colorletra));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JPanel();
        pnlBarraTitulo = new javax.swing.JPanel();
        lblMensajeValidacion = new javax.swing.JLabel();
        lblCerrar = new javax.swing.JLabel();
        btnCargar2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblMensajeValidacion1 = new javax.swing.JLabel();
        JSCCER = new javax.swing.JScrollPane();
        PnlCertificados = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        pnlPrincipal.setBackground(new java.awt.Color(241, 241, 241));
        pnlPrincipal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnlBarraTitulo.setOpaque(false);

        lblMensajeValidacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblMensajeValidacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensajeValidacion.setText("SSIGNER");
        lblMensajeValidacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblMensajeValidacionKeyPressed(evt);
            }
        });

        lblCerrar.setBackground(new java.awt.Color(255, 153, 153));
        lblCerrar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCerrar.setForeground(new java.awt.Color(255, 255, 255));
        lblCerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar.setText("x");
        lblCerrar.setToolTipText("Cerrar");
        lblCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar.setOpaque(true);
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

        javax.swing.GroupLayout pnlBarraTituloLayout = new javax.swing.GroupLayout(pnlBarraTitulo);
        pnlBarraTitulo.setLayout(pnlBarraTituloLayout);
        pnlBarraTituloLayout.setHorizontalGroup(
            pnlBarraTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarraTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMensajeValidacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBarraTituloLayout.setVerticalGroup(
            pnlBarraTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarraTituloLayout.createSequentialGroup()
                .addGroup(pnlBarraTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMensajeValidacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCargar2.setBackground(new java.awt.Color(211, 111, 66));
        btnCargar2.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnCargar2.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar2.setToolTipText("Cargar documentos para visualzar.");
        btnCargar2.setAutoscrolls(true);
        btnCargar2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btnCargar2.setBorderPainted(false);
        btnCargar2.setContentAreaFilled(false);
        btnCargar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargar2.setFocusPainted(false);
        btnCargar2.setPreferredSize(new java.awt.Dimension(55, 25));
        btnCargar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCargar2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCargar2MouseExited(evt);
            }
        });
        btnCargar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargar2ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(416, 273));

        lblMensajeValidacion1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblMensajeValidacion1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMensajeValidacion1.setText(" SELECCIONE UN CERTIFICADO");
        lblMensajeValidacion1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblMensajeValidacion1KeyPressed(evt);
            }
        });

        JSCCER.setBorder(null);
        JSCCER.setPreferredSize(new java.awt.Dimension(130, 130));
        JSCCER.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                JSCCERMouseWheelMoved(evt);
            }
        });

        PnlCertificados.setBackground(new java.awt.Color(255, 255, 255));
        PnlCertificados.setBorder(null);
        PnlCertificados.setPreferredSize(new java.awt.Dimension(130, 130));
        PnlCertificados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PnlCertificadosKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout PnlCertificadosLayout = new javax.swing.GroupLayout(PnlCertificados);
        PnlCertificados.setLayout(PnlCertificadosLayout);
        PnlCertificadosLayout.setHorizontalGroup(
            PnlCertificadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );
        PnlCertificadosLayout.setVerticalGroup(
            PnlCertificadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        JSCCER.setViewportView(PnlCertificados);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMensajeValidacion1)
                    .addComponent(JSCCER, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMensajeValidacion1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JSCCER, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(btnCargar2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarraTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlBarraTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnCargar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void PnlCertificadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PnlCertificadosKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_PnlCertificadosKeyPressed

    private void JSCCERMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_JSCCERMouseWheelMoved
        JSCCER.getVerticalScrollBar().setUnitIncrement(16);
        JSCCER.getVerticalScrollBar().setBlockIncrement(100);
    }//GEN-LAST:event_JSCCERMouseWheelMoved

    private void lblMensajeValidacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblMensajeValidacionKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_lblMensajeValidacionKeyPressed


    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        char caracter = evt.getKeyChar();
        if (caracter == KeyEvent.VK_ESCAPE) {
            setVisible(false);
        }
    }//GEN-LAST:event_formKeyPressed

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        Repositorio.cerrarventana = 0;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void lblCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource(), Color.red, Color.white);
    }//GEN-LAST:event_lblCerrarMouseEntered

    private void lblCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(255, 153, 153), Color.white);
    }//GEN-LAST:event_lblCerrarMouseExited

    private void btnCargar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar2MouseEntered

    private void btnCargar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar2MouseExited

    private void btnCargar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar2ActionPerformed

    private void lblMensajeValidacion1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblMensajeValidacion1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblMensajeValidacion1KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JSCCER;
    private javax.swing.JPanel PnlCertificados;
    public javax.swing.JButton btnCargar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCerrar;
    public static javax.swing.JLabel lblMensajeValidacion;
    public static javax.swing.JLabel lblMensajeValidacion1;
    private javax.swing.JPanel pnlBarraTitulo;
    private javax.swing.JPanel pnlPrincipal;
    // End of variables declaration//GEN-END:variables
}
