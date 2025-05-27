package Modulos;

import Clases.Libreria;
import Entidades.Constantes;
import Clases.Repositorio;
import Entidades.Firmador;
import Entidades.Firmante;
import Entidades.helperClass;
import Formularios.frmConfigurador;
import Global.CConstantes;
import Metodos.ValidarCertificados;
import static Modulos.frmFirmador.properties;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public final class pin extends javax.swing.JDialog
{
    String tiporepositorio;
    List<Libreria> listalibrerias = new ArrayList<>();
    String[] certs, pwcerts;
    String rutaCert;

    public pin(java.awt.Dialog parent, boolean modal, String tiporepositorio)
    {
        super(parent, modal);
        initComponents();
        this.tiporepositorio = tiporepositorio;
        
        if(this.tiporepositorio.equalsIgnoreCase(Repositorio.TOKEN))
        {
            lbltitulo.setText(Repositorio.TOKEN);
            listalibrerias = frmFirmador.configuracion.getLibrerias();
            txtpin.setText(new helperClass().decrypt(frmFirmador.configuracion.getVg_clave_token()));
            lblmensajelibreria.setText("Adjunte librería del token : ");
            pnlArchivo.setVisible(false);
                        
            rutaCert = listalibrerias.get(0).getRuta();
        }
        else
        {
            lbltitulo.setText(Repositorio.ARCHIVO);
            lblmensajelibreria.setText("Adjunte certificado : ");
            lbletiquetapin.setText("Ingrese el PIN certificado y [ENTER]: ");
            cboCerts.setVisible(true);
            cboCerts.removeAllItems();
            
            if(frmConfigurador.configuracion.getVg_ruta_certificado() != null && !frmConfigurador.configuracion.getVg_ruta_certificado().isEmpty())
            {
                certs = frmConfigurador.configuracion.getVg_ruta_certificado().split(";");
            
                if(certs != null && certs.length > 0)
                {
                    for (String s : certs)
                        cboCerts.addItem(s);
                    
                    rutaCert = certs[0];
                
                    if(certs[0].length() > 70)
                        lblRutaArchivo.setText(certs[0].substring(0, 70) + "...");
                    else
                        lblRutaArchivo.setText(certs[0]);
                }
                
                pwcerts = frmConfigurador.configuracion.getVg_clave_certificado().split(";");
                
                if(certs != null && certs.length > 0)
                    txtpin.setText(new helperClass().decrypt(pwcerts[0]));
            }
        }
        
        setSize(pnlConfiguracion.getWidth(), pnlConfiguracion.getHeight());
        setResizable(true);
        setLocationRelativeTo(null);
        frmElegirRepositorio.lblDesplegando.setVisible(false);
        txtRespuesta.setVisible(true);
        txtpin.requestFocus();
        
        if (txtpin.getPassword() != null)
            txtpin.select(0, txtpin.getPassword().length);
    }

    private void cerrarVentana()
    {
        setVisible(false);
        dispose();
    }

    protected void cambiarColor(Object obj)
    {
        cambiarColor(obj, Color.lightGray, Color.white);
    }

    protected void cambiarColor(Object obj, Color colorfondo, Color colorletra)
    {
        if (obj instanceof javax.swing.JLabel) {
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        } else if (obj instanceof JButton) {
            ((JButton) obj).setBackground(colorfondo);
            ((javax.swing.JButton) obj).setForeground(colorletra);
            ((javax.swing.JButton) obj).setOpaque(true);
        }
    }

    List<Firmante> ObtenerCertificados(String Token, String clave) throws CertificateEncodingException, CRLException, ParserConfigurationException, SAXException {
        frmFirmador.repositorios.cargarCertificado(clave, tiporepositorio, Token);
        frmFirmador.repositorios.obtenerAliases();
        return frmFirmador.repositorios.getFirmantes();
    }

    void grabarPropiedad(String key, String valor)
    {
        try (FileOutputStream fos = new FileOutputStream(new File("src/propiedades.properties"))){
            
            properties.setProperty(key, valor);
            properties.store(fos, "SSigner");
            fos.close();
        }
        catch (IOException ex)
        {

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        pnlConfiguracion = new javax.swing.JPanel();
        lbletiquetapin = new javax.swing.JLabel();
        txtpin = new javax.swing.JPasswordField();
        pnlArchivo = new javax.swing.JPanel();
        lblmensajelibreria = new javax.swing.JLabel();
        lblRutaArchivo = new javax.swing.JLabel();
        btnExaminar = new javax.swing.JButton();
        cboCerts = new javax.swing.JComboBox<>();
        lblRespuesta = new javax.swing.JLabel();
        pnlBarra3 = new javax.swing.JPanel();
        lblCerrar3 = new javax.swing.JLabel();
        lbltitulo = new javax.swing.JLabel();
        btnCargar1 = new javax.swing.JButton();
        txtRespuesta = new java.awt.TextArea();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        pnlConfiguracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlConfiguracion.setPreferredSize(new java.awt.Dimension(364, 290));

        lbletiquetapin.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        lbletiquetapin.setForeground(new java.awt.Color(67, 73, 111));
        lbletiquetapin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin.setText("Ingrese el PIN token y [ENTER] : ");

        txtpin.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        txtpin.setForeground(new java.awt.Color(211, 111, 66));
        txtpin.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txtpinKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                txtpinKeyReleased(evt);
            }
        });

        pnlArchivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        lblmensajelibreria.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        lblmensajelibreria.setForeground(new java.awt.Color(67, 73, 111));
        lblmensajelibreria.setText("Adjunte librería del token : ");

        lblRutaArchivo.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        lblRutaArchivo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        btnExaminar.setText("..");
        btnExaminar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExaminarActionPerformed(evt);
            }
        });

        cboCerts.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cboCertsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlArchivoLayout = new javax.swing.GroupLayout(pnlArchivo);
        pnlArchivo.setLayout(pnlArchivoLayout);
        pnlArchivoLayout.setHorizontalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblmensajelibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlArchivoLayout.createSequentialGroup()
                        .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblRutaArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboCerts, 0, 572, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExaminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlArchivoLayout.setVerticalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblmensajelibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnExaminar, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(lblRutaArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCerts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        lblRespuesta.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        lblRespuesta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        pnlBarra3.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N

        lblCerrar3.setBackground(new java.awt.Color(255, 153, 153));
        lblCerrar3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCerrar3.setForeground(new java.awt.Color(255, 255, 255));
        lblCerrar3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar3.setText("x");
        lblCerrar3.setToolTipText("Cerrar");
        lblCerrar3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar3.setOpaque(true);
        lblCerrar3.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblCerrar3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                lblCerrar3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                lblCerrar3MouseExited(evt);
            }
        });

        lbltitulo.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        lbltitulo.setText("SSIGNER");

        javax.swing.GroupLayout pnlBarra3Layout = new javax.swing.GroupLayout(pnlBarra3);
        pnlBarra3.setLayout(pnlBarra3Layout);
        pnlBarra3Layout.setHorizontalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(lbltitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBarra3Layout.setVerticalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblCerrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbltitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCargar1.setBackground(new java.awt.Color(211, 111, 66));
        btnCargar1.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnCargar1.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo Ssigner-01.png"))); // NOI18N
        btnCargar1.setToolTipText("Cargar documentos para visualzar.");
        btnCargar1.setAutoscrolls(true);
        btnCargar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btnCargar1.setBorderPainted(false);
        btnCargar1.setContentAreaFilled(false);
        btnCargar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargar1.setFocusPainted(false);
        btnCargar1.setPreferredSize(new java.awt.Dimension(55, 26));
        btnCargar1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnCargar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                btnCargar1MouseExited(evt);
            }
        });
        btnCargar1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCargar1ActionPerformed(evt);
            }
        });

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConfiguracionLayout = new javax.swing.GroupLayout(pnlConfiguracion);
        pnlConfiguracion.setLayout(pnlConfiguracionLayout);
        pnlConfiguracionLayout.setHorizontalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(btnCargar1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarra3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConfiguracionLayout.createSequentialGroup()
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblRespuesta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlConfiguracionLayout.createSequentialGroup()
                                .addComponent(lbletiquetapin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtpin, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOk)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(19, 19, 19))
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(pnlArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(txtRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlConfiguracionLayout.setVerticalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBarra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCargar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbletiquetapin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(txtRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConfiguracion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtpinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpinKeyPressed
        char caracter = evt.getKeyChar();
        checkCertificado(caracter);
    }//GEN-LAST:event_txtpinKeyPressed

    private void checkCertificado(char caracter)
    {
        if (caracter == KeyEvent.VK_ENTER)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    char[] pins = txtpin.getPassword();
                    
                    if (pins == null)
                    {
                        Constantes.msgInformacion(Constantes.MSG_ERROR_CLAVE);
                        return;
                    }
                    
                    txtRespuesta.setVisible(true);
                    
                    if (tiporepositorio.equals(Repositorio.TOKEN))
                    {
                        txtRespuesta.setText("Listando Certificados");
                        frmFirmador.configuracion.setVg_ruta_libreria_nombre(new File(rutaCert).getName());
                        frmFirmador.configuracion.setVg_ruta_libreria(rutaCert);
                        frmFirmador.configuracion.setVg_clave_token(new helperClass().encrypt(new String(txtpin.getPassword())));
                        //grabarPropiedad(Global.CConstantes.CERTNOMBRELIBRERIA, frmFirmador.configuracion.getVg_ruta_libreria_nombre());
                        //grabarPropiedad(Global.CConstantes.CERTRUTALIBRERIA, frmFirmador.configuracion.getVg_ruta_libreria());
                        //grabarPropiedad(Global.CConstantes.CERTCLAVETOKEN, frmFirmador.configuracion.getVg_clave_token());
                        new frmCertificados(pin.this, true, tiporepositorio, frmFirmador.configuracion.getVg_ruta_libreria(), new helperClass().decrypt(frmFirmador.configuracion.getVg_clave_token())).setVisible(true);
                    }
                    else
                    {
                        try
                        {
                            frmFirmador.configuracion.setVg_ruta_certificado(rutaCert);
                            frmFirmador.configuracion.setVg_clave_certificado(new helperClass().encrypt(new String(txtpin.getPassword())));
                            //grabarPropiedad(Global.CConstantes.CERTRUTACERTIFICADO, frmFirmador.configuracion.getVg_ruta_certificado());
                            //grabarPropiedad(Global.CConstantes.CERTCLAVECERTIFICADO, frmFirmador.configuracion.getVg_clave_certificado());
                            List<Firmante> LtDeCertificados;
                            txtRespuesta.setText("Listando Certificados");
                            
                            String pw = pwcerts == null || pwcerts.length == 0 ? new String(txtpin.getPassword()) : new helperClass().decrypt(pwcerts[cboCerts.getSelectedIndex()]);
                            
                            LtDeCertificados = ObtenerCertificados(rutaCert, pw);
                            
                            if (LtDeCertificados == null || LtDeCertificados.isEmpty())
                            {
                                txtRespuesta.setText("No se logró obtener el certificado. Verifique ");
                                return;
                            }
                            else
                            {
                                txtRespuesta.setForeground(Color.BLUE);
                                txtRespuesta.setText(Constantes.VALIDANDOCERTIFICADO);
                                String respuesta;
                                /*VALIDANDO CERTIFICADOS*/
                                if (!frmFirmador.repositorios.validarAliasCertificado(LtDeCertificados.get(0).getCertificado().getAlias()))
                                {
                                    txtRespuesta.setForeground(Color.RED);
                                    txtRespuesta.setText("Alias no válido.");
                                    return;
                                }

                                try
                                {
                                    respuesta = ValidarCertificados.filtrarCertificado(
                                            LtDeCertificados.get(0).getCertificado().getCertificadosConfianza(), new Date(),
                                            frmFirmador.configuracion.getVg_url_tsl(),
                                            frmFirmador.configuracion.getVg_carpeta_salida_crl_tsl() + File.separator,
                                            frmFirmador.configuracion.getUrlProxy(), frmFirmador.configuracion.getPuertoProxy(),
                                            "" + frmFirmador.configuracion.isVg_validar_con_no_repudio());
                                }
                                catch (Exception ex)
                                {
                                    respuesta = "-1";
                                }
                                
                                /*FIN VALIDACION*/
                                if (respuesta.equals("0"))
                                {
                                    Firmador firmar = new Firmador(frmFirmador.configuracion.getDocumentoUltimo());
                                    firmar.setUrl_tsa(frmFirmador.configuracion.getVg_tsa_url());
                                    firmar.setUsuario_tsa(frmFirmador.configuracion.getVg_tsa_usuario());
                                    firmar.setPassword_tsa(new helperClass().decrypt(frmFirmador.configuracion.getVg_tsa_clave()));
                                    //***FIRMA VISIBLE***////
                                    firmar.setVarias_firmas(frmFirmador.configuracion.isVg_cerrar_documento());
                                    firmar.setFirma_visible(frmFirmador.configuracion.isVg_firma_visible());
                                    
                                    switch (frmFirmador.configuracion.getVg_firma_visible_estilo_firma())
                                    {
                                        case 0:
                                            firmar.setFirma_visible_solo_imagen(true);
                                            break;
                                        case 1:
                                            firmar.setFirma_visible_solo_texto(true);
                                            break;
                                        case 2:
                                            firmar.setFirma_visible_imagen_texto(true);
                                            break;
                                    }
                                    firmar.setNro_pagina(frmFirmador.configuracion.getVg_firma_visible_pagina() + "");
                                    firmar.setFirma_visible_todas_paginas(frmFirmador.configuracion.isVg_firma_visible_firmar_todas_paginas());
                                    firmar.setX((int) frmFirmador.configuracion.getVg_firma_visible_x());
                                    firmar.setY((int) frmFirmador.configuracion.getVg_firma_visible_y());
                                    firmar.setAlto((int) frmFirmador.configuracion.getVg_firma_visible_x1());
                                    firmar.setAncho((int) frmFirmador.configuracion.getVg_firma_visible_y1());

                                    firmar.setPosiciones("");
                                    firmar.setTamano_letra("" + frmFirmador.configuracion.getVg_firma_visible_tamanio_fuente());
                                    firmar.setRuta_imagen("" + frmFirmador.configuracion.getVg_firma_visible_ruta_imagen());
                                    firmar.setTitulo_firma("");
                                    Firmante firmante = new Firmante();
                                    Firmante.Certificado certificado = firmante.new Certificado();
                                    certificado.setCn(LtDeCertificados.get(0).getCertificado().getCn());
                                    certificado.setAlias(LtDeCertificados.get(0).getCertificado().getAlias());
                                    certificado.setCargo(LtDeCertificados.get(0).getCertificado().getCargo());
                                    certificado.setEmpresa(LtDeCertificados.get(0).getCertificado().getEmpresa());
                                    certificado.setOu(LtDeCertificados.get(0).getCertificado().getOu());
                                    frmFirmador.configuracion.setFirmante(firmante);

                                    firmar.setC_nombre(LtDeCertificados.get(0).getCertificado().getCn());//lblValorEmisor.getText());
                                    firmar.setC_area(LtDeCertificados.get(0).getCertificado().getOu());
                                    firmar.setC_titulo(LtDeCertificados.get(0).getCertificado().getCargo());//lblTitle.getText());
                                    firmar.setC_empresa(LtDeCertificados.get(0).getCertificado().getEmpresa());//lblO.getText());
                                    new frmOtrosDatos(pin.this, true, firmar, LtDeCertificados.get(0)).setVisible(true);
                                    setVisible(false);
                                }
                                else
                                {
                                    txtRespuesta.setText("Certificado no válido");
                                    JOptionPane.showMessageDialog(pin.this, ValidarCertificados.Mensaje(), CConstantes.APLICACION_NOMBRE, JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                        catch (CertificateEncodingException | CRLException | ParserConfigurationException | SAXException ex)
                        {
                            txtRespuesta.setText(ex.getMessage());
                        }
                    }

                    txtRespuesta.setVisible(true);
                    setVisible(false);
                }
            }).start();
        }
    }
    
    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExaminarActionPerformed

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                JFileChooser elegir = new JFileChooser(rutaCert);
                elegir.setFileSelectionMode(JFileChooser.FILES_ONLY);
                elegir.setAcceptAllFileFilterUsed(false);
                
                if (tiporepositorio.equals(Repositorio.TOKEN))
                {
                    elegir.addChoosableFileFilter(new FileNameExtensionFilter("Librería del token (*.dll)", "dll"));
                    elegir.setDialogTitle("Seleccionar la librería del proveedor del token");
                }
                else
                {
                    elegir.addChoosableFileFilter(new FileNameExtensionFilter("Certificado de archivo (*.p12,*.pfx)", "p12", "pfx"));
                    elegir.setDialogTitle("Seleccionar el certificado de archivo para firmar");
                }
                
                elegir.setMultiSelectionEnabled(true);
                int opcion = elegir.showOpenDialog(pin.this);
                
                if (opcion == JFileChooser.APPROVE_OPTION)
                {
                    rutaCert = elegir.getSelectedFile().getPath();
                    
                    if(rutaCert.length() > 70)
                        lblRutaArchivo.setText(rutaCert.substring(0, 70) + "...");
                    else
                        lblRutaArchivo.setText(rutaCert);
                }
                
                txtpin.requestFocus();
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }).start();
    }//GEN-LAST:event_btnExaminarActionPerformed

    private void txtpinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpinKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpinKeyReleased

    private void lblCerrar3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar3MouseClicked
        cerrarVentana();
    }//GEN-LAST:event_lblCerrar3MouseClicked

    private void lblCerrar3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar3MouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource(), Color.red, Color.white);
    }//GEN-LAST:event_lblCerrar3MouseEntered

    private void lblCerrar3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar3MouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(255, 153, 153), Color.white);
    }//GEN-LAST:event_lblCerrar3MouseExited

    private void btnCargar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar1MouseEntered

    private void btnCargar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar1MouseExited

    private void btnCargar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        frmElegirRepositorio.lblDesplegando.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void cboCertsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cboCertsActionPerformed
    {//GEN-HEADEREND:event_cboCertsActionPerformed
        if(certs != null && pwcerts != null)
        {
            rutaCert = certs[cboCerts.getSelectedIndex()];
            
            if(rutaCert.length() > 70)
                lblRutaArchivo.setText(rutaCert.substring(0, 70) + "...");
            else
                lblRutaArchivo.setText(rutaCert);
            
            txtpin.setText(new helperClass().decrypt(pwcerts[cboCerts.getSelectedIndex()]));
        }
    }//GEN-LAST:event_cboCertsActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOkActionPerformed
    {//GEN-HEADEREND:event_btnOkActionPerformed
        checkCertificado((char)KeyEvent.VK_ENTER);
    }//GEN-LAST:event_btnOkActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCargar1;
    private javax.swing.JButton btnExaminar;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox<String> cboCerts;
    private javax.swing.JLabel lblCerrar3;
    private javax.swing.JLabel lblRespuesta;
    private javax.swing.JLabel lblRutaArchivo;
    private javax.swing.JLabel lbletiquetapin;
    private javax.swing.JLabel lblmensajelibreria;
    private javax.swing.JLabel lbltitulo;
    private javax.swing.JPanel pnlArchivo;
    private javax.swing.JPanel pnlBarra3;
    private javax.swing.JPanel pnlConfiguracion;
    private java.awt.TextArea txtRespuesta;
    private javax.swing.JPasswordField txtpin;
    // End of variables declaration//GEN-END:variables
}
