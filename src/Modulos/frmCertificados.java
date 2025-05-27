package Modulos;

import Entidades.Constantes;
import Clases.Repositorio;
import Entidades.Firmante;
import Entidades.helperClass;
import Formularios.*;
import Global.CConstantes;
import Global.CLog;
import Metodos.ValidarCertificados;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public final class frmCertificados extends javax.swing.JDialog
{
    public static int cerrarventana = 1;
    private String _tiporepositorio;
    private String _archivo;
    private String _clave;
    int index = 1;
    int i = 0;
    int NumeroDeCertificados = 0;
    boolean Conbuffer;
    Thread hilovalidacioncertificado;
    ArrayList<JPanel> paneles = new ArrayList<>();

    public frmCertificados() { }

    public frmCertificados(Dialog parent, boolean modal, String tiporepositorio, String archivo, String clave)
    {
        super(parent, modal);
        
        try
        {
            initComponents();
            _tiporepositorio = tiporepositorio;
            _archivo = archivo;
            _clave = clave;
            ListarCertificados();
            frmElegirRepositorio.lblDesplegando.setVisible(false);
        }
        catch (CertificateEncodingException | CRLException | ParserConfigurationException | SAXException ex)
        {
            CConstantes.dialogo("Exception " + ex.getMessage());
        }
    }

    void ListarCertificados() throws CertificateEncodingException, CRLException, ParserConfigurationException, SAXException
    {
        List<Firmante> LtDeCertificados = ObtenerCertificados(_archivo, _clave);
        Dimension d = new Dimension();

        //LISTAR
        int AcumuladorTamano;
        int pointx;

        if (LtDeCertificados != null && LtDeCertificados.size() > 0) {
            NumeroDeCertificados = LtDeCertificados.size();
            frmElegirRepositorio.cargo = true;
        }
        else
        {
            lblMensajeValidacion.setText(CLog.getMensaje_error());
            lblMensajeValidacion.setToolTipText(lblMensajeValidacion.getText());
            return;
        }
        
        if (NumeroDeCertificados >= 3)
            pointx = 5;
        else
            pointx = 2;
        
        for (int j = 0; j < NumeroDeCertificados; j++)
        {
            final JPanel jp = new javax.swing.JPanel();
            jp.setName("Panel" + j);
            SpringLayout layout = new SpringLayout();
            jp.setLayout(layout);
            jp.setSize(415, 100);
            jp.setLocation(5, i);

            JButton btnImagenCertificado = new JButton();
            btnImagenCertificado.setIcon(new ImageIcon(getClass().getResource("/CertificadoLogo.png")));
            btnImagenCertificado.setBackground(java.awt.SystemColor.text);
            btnImagenCertificado.setBorder(null);
            btnImagenCertificado.setContentAreaFilled(false);
            final JLabel lblCertificado = new JLabel("Certificado");

            final JLabel lblTitle = new JLabel("titulo_o_Cargo" + (j));
            final JLabel lblO = new JLabel("Organizacion_o_Caoporacion" + (j));
            final JLabel lblValorEmisor = new JLabel("lblValorEmisor");
            final JLabel lblValorDn = new JLabel("lblValorDn");
            final JLabel lblVerCertificado = new JLabel("lblVerCertificado");
            final JLabel lblCargo = new JLabel("lblCargo");

            final JLabel lblFechaInicio = new JLabel("FechaInicio");
            final JLabel lblValorFechaInicio = new JLabel("lblValorFechaInicio");
            final JLabel lblFechaFinal = new JLabel("FechaFinal:");
            final JLabel lblValorFechaFinal = new JLabel("lblValorFechaFinal");

            final JLabel lblVersion = new JLabel("Version" + (j));
            final JLabel lblSerie = new JLabel("Serie" + (j));
            final JLabel lblUsos = new JLabel("Usos" + (j));
            final JLabel lblRoot = new JLabel("Root" + (j));

            lblValorDn.setText("");
            lblValorEmisor.setFont(new Font("Arial", 1, 14));
            lblFechaFinal.setFont(new Font("Calibri", 0, 11));
            lblFechaInicio.setFont(new Font("Calibri", 0, 11));
            lblValorFechaInicio.setFont(new Font("Calibri", 0, 11));
            lblValorFechaFinal.setFont(new Font("Calibri", 0, 11));
            lblVerCertificado.setFont(new Font("Calibri", 0, 12));
            lblCargo.setFont(new Font("Calibri", 0, 11));
            lblO.setFont(new Font("Calibri", 0, 11));
            lblTitle.setFont(new Font("Calibri", 0, 11));
            lblVerCertificado.setForeground(Color.BLUE);

            lblTitle.setText(LtDeCertificados.get(j).getCertificado().getCargo());
            lblO.setText(LtDeCertificados.get(j).getCertificado().getEmpresa());
            lblValorEmisor.setText(LtDeCertificados.get(j).getCertificado().getCn());
            lblValorEmisor.setPreferredSize(new Dimension(300, 20));
            lblValorEmisor.setToolTipText(lblValorEmisor.getText());
            lblFechaInicio.setText("Válido desde : ");
            lblValorFechaInicio.setText(" " + LtDeCertificados.get(j).getCertificado().getFechaInicial());
            lblCertificado.setText(LtDeCertificados.get(j).getCertificado().getAlias());
            lblFechaFinal.setText("Hasta : ");
            lblCargo.setText("Cargo : ");

            lblValorFechaFinal.setText("  " + LtDeCertificados.get(j).getCertificado().getFechafinal());
            lblVerCertificado.setText("Haga click para ver la propiedades de su certificado");

            lblVersion.setText(LtDeCertificados.get(j).getCertificado().getVersion());
            lblRoot.setText(LtDeCertificados.get(j).getCertificado().getDnRoot());
            lblSerie.setText(LtDeCertificados.get(j).getCertificado().getSerie());
            lblUsos.setText(LtDeCertificados.get(j).getCertificado().getUso());

            jp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btnImagenCertificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            //  lblEmpresaEmisora.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            lblValorEmisor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lblFechaInicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lblValorFechaInicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lblFechaFinal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lblValorFechaFinal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lblVerCertificado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lblCargo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            jp.add(btnImagenCertificado);
            jp.add(lblValorEmisor);
            jp.add(lblFechaInicio);
            jp.add(lblValorFechaInicio);
            jp.add(lblFechaFinal);
            jp.add(lblValorFechaFinal);
            jp.add(lblVerCertificado);
            jp.add(lblCargo);
            jp.add(lblO);
            jp.add(lblTitle);

            layout.putConstraint(SpringLayout.NORTH, lblValorEmisor, 5, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblValorEmisor, 75, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblCargo, 25, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblCargo, 75, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblTitle, 25, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblTitle, 110, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblFechaInicio, 40, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblFechaInicio, 75, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblValorFechaInicio, 40, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblValorFechaInicio, 140, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblFechaFinal, 40, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblFechaFinal, 250, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblValorFechaFinal, 40, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblValorFechaFinal, 280, SpringLayout.WEST, jp);

            layout.putConstraint(SpringLayout.NORTH, lblVerCertificado, 55, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblVerCertificado, 75, SpringLayout.WEST, jp);
            
            layout.putConstraint(SpringLayout.NORTH, lblO, 75, SpringLayout.NORTH, jp);
            layout.putConstraint(SpringLayout.WEST, lblO, 75, SpringLayout.WEST, jp);

            if (j == 0)
            {
                jp.setBackground(new Color(204, 237, 255, 255));
                paneles.add(jp);
            }
            else
                jp.setBackground(new Color(255, 255, 255));
            
            PnlCertificados.add(jp);
            
            jp.addMouseListener(new java.awt.event.MouseAdapter()
            {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt)
                {
                    paneles.get(0).setBackground(new Color(255, 255, 255));
                    paneles.add(0, jp);
                    paneles.get(0).setBackground(new Color(204, 237, 255, 255));
                    hilovalidacioncertificado = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                lblMensajeValidacion.setText(Constantes.VALIDANDOCERTIFICADO);
                                String respuesta;
                                /*VALIDANDO CERTIFICADOS*/
                                if (!frmFirmador.repositorios.validarAliasCertificado(lblCertificado.getText()))
                                {
                                    lblMensajeValidacion.setText("Alias no vàlido");
                                    return;
                                }

                                Firmante firmanteAux;
                                List<Firmante> firmantes;

                                firmanteAux = null;
                                firmantes = frmFirmador.repositorios.getFirmantes();

                                for (Firmante firmante : firmantes)
                                    if (firmante.getCertificado().getAlias().equalsIgnoreCase(lblCertificado.getText()))
                                    {
                                        firmanteAux = firmante;
                                        break;
                                    }

                                if (firmanteAux == null)
                                    throw new Exception("No se han listado los certificados");

                                frmFirmador.repositorios.setFirmanteElegido(firmanteAux);
                                respuesta = ValidarCertificados.filtrarCertificado(frmFirmador.repositorios.getFirmanteElegido().getCertificado().getCertificadosConfianza(), new Date(), 
                                        frmFirmador.configuracion.getVg_url_tsl(), 
                                        frmFirmador.configuracion.getVg_carpeta_salida_crl_tsl() + File.separator, 
                                        frmFirmador.configuracion.getUrlProxy(), frmFirmador.configuracion.getPuertoProxy(),
                                        "" + frmFirmador.configuracion.isVg_validar_con_no_repudio());

                                /*FIN VALIDACION*/
                                if (respuesta.equals("0"))
                                {
                                    Firmante Firmante = new Entidades.Firmante();
                                    Firmante.Certificado certificado = Firmante.new Certificado();
                                    certificado.setCn(frmFirmador.repositorios.getFirmanteElegido().getCertificado().getCn());
                                    certificado.setAlias(frmFirmador.repositorios.getFirmanteElegido().getCertificado().getAlias());
                                    certificado.setCargo(frmFirmador.repositorios.getFirmanteElegido().getCertificado().getCargo());
                                    certificado.setEmpresa(frmFirmador.repositorios.getFirmanteElegido().getCertificado().getEmpresa());
                                    certificado.setOu(frmFirmador.repositorios.getFirmanteElegido().getCertificado().getOu());
                                    Firmante.setCertificado(certificado);
                                    frmFirmador.configuracion.setVg_repositorio_tipo(_tiporepositorio);
                                    frmFirmador.configuracion.setVg_clave_certificado(new helperClass().encrypt(_clave));
                                    frmFirmador.configuracion.setFirmante(Firmante);
                                    new frmOtrosDatos(frmCertificados.this, true).setVisible(true);
                                    
                                    if (cerrarventana == 0)
                                        return;
                                    
                                    frmElegirRepositorio.cerrarventana = 1;
                                    setVisible(false);
                                    dispose();
                                }
                                else
                                {
                                    lblMensajeValidacion.setText(Constantes.CERTIFICADO_NO_VALIDO);
                                    //JOptionPane.showMessageDialog(frmCertificados.this, ValidarCertificados.Mensaje(), CConstantes.APLICACION_NOMBRE, JOptionPane.WARNING_MESSAGE);
                                    //JOptionPane.showMessageDialog(frmCertificados.this, "El certificado seleccionado no es válido. Por favor, elija otro certificado digital o verifique su estado.", CConstantes.APLICACION_NOMBRE, JOptionPane.WARNING_MESSAGE);
                                    frmMensaje.mensaje_mostrar(frmCertificados.this, Metodos.ValidarCertificados.Mensaje());
                                }

                            }
                            catch (Exception ex)
                            {
                                //JOptionPane.showMessageDialog(frmCertificados.this, ex.toString());
                                frmMensaje.mensaje_mostrar(frmCertificados.this, ex.toString());
                            }
                        }

                    });
                    hilovalidacioncertificado.setDaemon(true);
                    hilovalidacioncertificado.start();
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    //jp.setBackground(new Color(204, 237, 255, 255));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    //jp.setBackground(Color.white);
                }
            });
            lblVerCertificado.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    /*lblVerCertificado.setForeground(Color.blue);*/
                    //  jp.setBackground(new Color(204, 237, 255, 255));
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    lblVerCertificado.setForeground(Color.red);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    lblVerCertificado.setForeground(Color.blue);
                }
            });

            jp.setVisible(true);
            i += jp.getHeight() + 2;
            AcumuladorTamano = jp.getHeight() + jp.getY() + 5;
            jp.setLocation(new Point(pointx, AcumuladorTamano - 100));
            d.setSize(PnlCertificados.getWidth() - 20, AcumuladorTamano + 10);
            PnlCertificados.setPreferredSize(new Dimension(d));
            JSCCER.setPreferredSize(new Dimension(d));
            JSCCER.repaint();
        }
    }

    List<Firmante> ObtenerCertificados(String Token, String clave) throws CertificateEncodingException, CRLException, ParserConfigurationException, SAXException
    {
        if (_tiporepositorio.equals(Repositorio.TOKEN))
        {
            String libreria = "";
            for (int j = 0; j < frmFirmador.configuracion.getLibrerias().size(); j++)
            {
                libreria = frmFirmador.configuracion.getLibrerias().get(j).getRuta();

                frmFirmador.repositorios.cargarCertificado(clave, _tiporepositorio, libreria);
                List<String> vgl_alias = frmFirmador.repositorios.obtenerAliases();
                if (vgl_alias != null && !vgl_alias.isEmpty())
                    break;
                
                if (frmFirmador.repositorios.isLibreria_con_error())
                    break;
            }
            
            if (frmFirmador.repositorios.getFirmantes() != null && frmFirmador.repositorios.getFirmantes().size() > 0)
            {
                frmFirmador.configuracion.setVg_ruta_libreria_nombre(new File(libreria).getName());
                frmFirmador.configuracion.setVg_ruta_libreria(libreria);
                return frmFirmador.repositorios.getFirmantes();
            }
        }
        else
            frmFirmador.repositorios.cargarCertificado(clave, _tiporepositorio, frmFirmador.configuracion.getVg_ruta_libreria());
        
        //Documento.repositorios.cargarCertificado(clave, _tiporepositorio,Token);
        frmFirmador.repositorios.obtenerAliases();
        return frmFirmador.repositorios.getFirmantes();
    }

    private static final String[] keyUsageLabels = new String[]
    {
        "digitalSignature", "nonRepudiation", "keyEncipherment",
        "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign",
        "encipherOnly", "decipherOnly"
    };

    static List<String> getKeyUsage(final X509Certificate cert)
    {
        final boolean[] keyUsage = cert.getKeyUsage();
        
        if (keyUsage != null)
        {
            final List<String> ret = new LinkedList<String>();
            
            for (int i = 0; i < keyUsage.length; ++i)
                if (keyUsage[i])
                {
                    if (i < keyUsageLabels.length)
                        ret.add(keyUsageLabels[i]);
                    else
                        ret.add(String.valueOf(i));
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
        JSCCER = new javax.swing.JScrollPane();
        PnlCertificados = new javax.swing.JPanel();
        pnlBarraTitulo = new javax.swing.JPanel();
        lblMensajeValidacion = new javax.swing.JLabel();
        lblCerrar3 = new javax.swing.JLabel();
        btnCargar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        pnlPrincipal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        JSCCER.setPreferredSize(new java.awt.Dimension(130, 130));
        JSCCER.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                JSCCERMouseWheelMoved(evt);
            }
        });

        PnlCertificados.setBackground(new java.awt.Color(255, 255, 255));
        PnlCertificados.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
            .addGap(0, 403, Short.MAX_VALUE)
        );
        PnlCertificadosLayout.setVerticalGroup(
            PnlCertificadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        JSCCER.setViewportView(PnlCertificados);

        lblMensajeValidacion.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        lblMensajeValidacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensajeValidacion.setText("SELECCIONE UN CERTIFICADO");
        lblMensajeValidacion.setOpaque(true);
        lblMensajeValidacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblMensajeValidacionKeyPressed(evt);
            }
        });

        lblCerrar3.setBackground(new java.awt.Color(255, 153, 153));
        lblCerrar3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCerrar3.setForeground(new java.awt.Color(255, 255, 255));
        lblCerrar3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar3.setText("x");
        lblCerrar3.setToolTipText("Cerrar");
        lblCerrar3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar3.setOpaque(true);
        lblCerrar3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrar3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCerrar3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCerrar3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlBarraTituloLayout = new javax.swing.GroupLayout(pnlBarraTitulo);
        pnlBarraTitulo.setLayout(pnlBarraTituloLayout);
        pnlBarraTituloLayout.setHorizontalGroup(
            pnlBarraTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarraTituloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMensajeValidacion, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(lblCerrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBarraTituloLayout.setVerticalGroup(
            pnlBarraTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarraTituloLayout.createSequentialGroup()
                .addGroup(pnlBarraTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCerrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMensajeValidacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCargar2.setBackground(new java.awt.Color(211, 111, 66));
        btnCargar2.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnCargar2.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo Ssigner-01.png"))); // NOI18N
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

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(JSCCER, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(btnCargar2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarraTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBarraTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCargar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(JSCCER, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        
        if (caracter == KeyEvent.VK_ESCAPE)
            setVisible(false);
    }//GEN-LAST:event_formKeyPressed

    private void lblCerrar3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar3MouseClicked
        frmElegirRepositorio.cerrarventana = 0;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_lblCerrar3MouseClicked

    private void lblCerrar3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar3MouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource(), Color.red, Color.white);
    }//GEN-LAST:event_lblCerrar3MouseEntered

    private void lblCerrar3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar3MouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(255, 153, 153), Color.white);
    }//GEN-LAST:event_lblCerrar3MouseExited

    private void btnCargar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar2MouseEntered

    private void btnCargar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar2MouseExited

    private void btnCargar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JSCCER;
    private javax.swing.JPanel PnlCertificados;
    public javax.swing.JButton btnCargar2;
    private javax.swing.JLabel lblCerrar3;
    public static javax.swing.JLabel lblMensajeValidacion;
    private javax.swing.JPanel pnlBarraTitulo;
    private javax.swing.JPanel pnlPrincipal;
    // End of variables declaration//GEN-END:variables
}
