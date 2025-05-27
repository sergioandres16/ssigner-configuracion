package Formularios;

import Clases.Libreria;
import java.awt.Color;
import java.io.File;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Clases.Repositorio;
import Entidades.Firmante;
import Global.CConstantes;
import Global.CLog;
import Metodos.helperClass;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class frmPin extends javax.swing.JDialog
{
    Thread hiloExaminar;
    Thread hilocargando;
    private final String tiporepositorio;
    private String rutaCert;

    public frmPin(javax.swing.JFrame parent, boolean modal, String tiporepositorio)
    {
        super(parent, modal);
        initComponents();
        this.tiporepositorio = tiporepositorio;
        cboLibreria.setVisible(false);
        
        if (tiporepositorio.equalsIgnoreCase(Repositorio.TOKEN))
        {
            pnlArchivo.setVisible(false);
            String[] nombresLibrerias = new String[]{"bit4xpki.dll", "bit4ipki.dll", "bit4npki.dll", "bit4opki.dll"};
            File[] rutalibrerias = new File[nombresLibrerias.length];
            
            for (int i = 0; i < nombresLibrerias.length; i++)
                rutalibrerias[i] = new File(frmConfigurador.carpetaDestinoLibrerias  + nombresLibrerias[i]);
            
//                    File[] rutalibrerias = new File(frmConfigurador.carpetaDestinoLibrerias).listFiles(new FilenameFilter() {
//                        @Override
//                        public boolean accept(File dir, String name) {
//                            String[] nombresLibrerias = new String[]{
//                                "bit4xpki.dll", "bit4ipki.dll", "bit4npki.dll", "bit4opki.dll"};
//                            return name.toLowerCase().endsWith(frmConfigurador.vcg_so.getVg_extension().substring(1, 4));
//                        }
//                    });
            lblmensajelibreria.setText("Adjunte librería del token : ");
            lbltitulo.setText(Repositorio.TOKEN + " - TOKEN");
            
            if (rutalibrerias == null || rutalibrerias.length == 0)
            {
                lbRespuesta.setText("No se encontraron librerias en la ruta [" + frmConfigurador.vcg_so.ObtenerRutadll() + "]");
                txt_pin.setEnabled(false);
            }
            else
            {
                agregarlibreriasalcombobox(rutalibrerias);
                txt_pin.setText(new helperClass().decrypt(frmConfigurador.configuracion.getVg_clave_token()));
            }
        }
        else
        {
            lbltitulo.setText(Repositorio.ARCHIVO + " - ARCHIVO");
            //lblRutaArchivo.setText(frmConfigurador.configuracion.getVg_ruta_certificado());
            //txt_pin.setText(new helperClass().decrypt(frmConfigurador.configuracion.getVg_clave_certificado()));
//            txt_pin.setText(frmConfigurador.configuracion.getVg_clave_certificado());
            lblmensajelibreria.setText("Adjunte certificado : ");
            lbletiquetapin.setText("Pin : ");
        }
        
        setSize(pnlConfiguracion.getWidth(), pnlConfiguracion.getHeight() - 10);
        setResizable(true);
        setLocationRelativeTo(parent);
        txt_pin.requestFocus();
        
        if (txt_pin.getPassword() != null)
            txt_pin.select(0, txt_pin.getPassword().length);
    }

    void agregarlibreriasalcombobox(File[] librerias)
    {
        List<Libreria> listalibrerias = new ArrayList<>();
        
        for (File lib : librerias)
        {
            Libreria clslibrerias = new Libreria();
            clslibrerias.setRuta(lib.getAbsolutePath());
            clslibrerias.setNombre(lib.getName());
            listalibrerias.add(clslibrerias);
            cboLibreria.addItem(lib.getName());
        }
        
        frmConfigurador.configuracion.setVg_lista_librerias(listalibrerias);
    }

    private void cerrarVentana()
    {
        Repositorio.cerrarventana = 0;
        setVisible(false);
        dispose();
    }

    protected void cambiarColor(Object obj) {
        cambiarColor(obj, Color.lightGray, Color.white);
    }

    protected void cambiarColor(Object obj, Color colorfondo, Color colorletra) {
        if (obj instanceof javax.swing.JLabel) {
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        } else if (obj instanceof javax.swing.JButton) {
            ((javax.swing.JButton) obj).setBackground(colorfondo);
            ((javax.swing.JButton) obj).setForeground(colorletra);
            ((javax.swing.JButton) obj).setOpaque(true);
        }
    }

    List<Firmante> ObtenerCertificados(String Token, String clave) throws CertificateEncodingException, CRLException, ParserConfigurationException, SAXException {
        System.out.println("Cargar Certificado");
        frmConfigurador.repositorios.cargarCertificado(clave, tiporepositorio, Token);
        frmConfigurador.repositorios.obtenerAliases();
        return frmConfigurador.repositorios.getFirmantes();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        pnlConfiguracion = new javax.swing.JPanel();
        lbletiquetapin = new javax.swing.JLabel();
        txt_pin = new javax.swing.JPasswordField();
        pnlArchivo = new javax.swing.JPanel();
        lblmensajelibreria = new javax.swing.JLabel();
        lblRutaArchivo = new javax.swing.JLabel();
        btnexaminar = new javax.swing.JButton();
        lbllistando = new javax.swing.JLabel();
        cboLibreria = new javax.swing.JComboBox();
        pnlBarra3 = new javax.swing.JPanel();
        ctrl_lbl_cerrar = new javax.swing.JLabel();
        lbltitulo = new javax.swing.JLabel();
        lbRespuesta = new java.awt.TextArea();
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

        pnlConfiguracion.setBackground(new java.awt.Color(241, 241, 241));
        pnlConfiguracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlConfiguracion.setPreferredSize(new java.awt.Dimension(355, 290));

        lbletiquetapin.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin.setText("Ingrese el PIN token y [ENTER] : ");

        txt_pin.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        txt_pin.setForeground(new java.awt.Color(211, 111, 66));
        txt_pin.setPreferredSize(new java.awt.Dimension(111, 27));
        txt_pin.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                txt_pinKeyPressed(evt);
            }
        });

        pnlArchivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        pnlArchivo.setOpaque(false);

        lblmensajelibreria.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lblmensajelibreria.setForeground(new java.awt.Color(0, 83, 154));
        lblmensajelibreria.setText("Adjunte librería del token : ");

        lblRutaArchivo.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        lblRutaArchivo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        btnexaminar.setText("...");
        btnexaminar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnexaminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlArchivoLayout = new javax.swing.GroupLayout(pnlArchivo);
        pnlArchivo.setLayout(pnlArchivoLayout);
        pnlArchivoLayout.setHorizontalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlArchivoLayout.createSequentialGroup()
                        .addComponent(lblRutaArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnexaminar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblmensajelibreria))
                .addContainerGap())
        );
        pnlArchivoLayout.setVerticalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblmensajelibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnexaminar, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(lblRutaArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbllistando.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbllistando.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        pnlBarra3.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
        pnlBarra3.setOpaque(false);

        ctrl_lbl_cerrar.setBackground(new java.awt.Color(255, 153, 153));
        ctrl_lbl_cerrar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        ctrl_lbl_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        ctrl_lbl_cerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ctrl_lbl_cerrar.setText("x");
        ctrl_lbl_cerrar.setToolTipText("Cerrar");
        ctrl_lbl_cerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_lbl_cerrar.setOpaque(true);
        ctrl_lbl_cerrar.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                ctrl_lbl_cerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                ctrl_lbl_cerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                ctrl_lbl_cerrarMouseExited(evt);
            }
        });

        lbltitulo.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        lbltitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltitulo.setText("SSIGNER");

        javax.swing.GroupLayout pnlBarra3Layout = new javax.swing.GroupLayout(pnlBarra3);
        pnlBarra3.setLayout(pnlBarra3Layout);
        pnlBarra3Layout.setHorizontalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(lbltitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ctrl_lbl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBarra3Layout.setVerticalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ctrl_lbl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbltitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbRespuesta.setEditable(false);
        lbRespuesta.setMinimumSize(new java.awt.Dimension(100, 50));
        lbRespuesta.setPreferredSize(new java.awt.Dimension(200, 50));

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
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBarra3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboLibreria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbRespuesta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                                .addComponent(lbletiquetapin, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                .addComponent(txt_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOk)
                                .addGap(21, 21, 21))
                            .addComponent(lbllistando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        pnlConfiguracionLayout.setVerticalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addComponent(pnlBarra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbletiquetapin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(cboLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbllistando, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbRespuesta, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ctrl_lbl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_cerrarMouseClicked
        cerrarVentana();
    }//GEN-LAST:event_ctrl_lbl_cerrarMouseClicked

    private void ctrl_lbl_cerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_cerrarMouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource(), Color.red, Color.white);
    }//GEN-LAST:event_ctrl_lbl_cerrarMouseEntered

    private void ctrl_lbl_cerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_cerrarMouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new Color(255, 153, 153), Color.white);
    }//GEN-LAST:event_ctrl_lbl_cerrarMouseExited

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//         Repositorio.lblDesplegando.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void txt_pinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pinKeyPressed
        char caracter = evt.getKeyChar();
        checkCertificado(caracter);
    }//GEN-LAST:event_txt_pinKeyPressed

    private void btnexaminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexaminarActionPerformed
        JFileChooser elegir = new JFileChooser(rutaCert);
        elegir.setFileSelectionMode(JFileChooser.FILES_ONLY);
        elegir.setAcceptAllFileFilterUsed(false);
        
        if (tiporepositorio.equals(Repositorio.TOKEN))
        {
            elegir.addChoosableFileFilter(new FileNameExtensionFilter("Librería del token (*." + frmConfigurador.vcg_so.getVg_extension() + ")", frmConfigurador.vcg_so.getVg_extension()));
            elegir.setDialogTitle("Seleccionar la librería del proveedor del token");
        }
        else
        {
            elegir.addChoosableFileFilter(new FileNameExtensionFilter("Certificado de archivo (*.p12,*.pfx)", "p12", "pfx"));
            elegir.setDialogTitle("Seleccionar el certificado de archivo para firmar");
        }
        
        elegir.setMultiSelectionEnabled(true);
        int opcion = elegir.showOpenDialog(frmPin.this);
        
        if (opcion == JFileChooser.APPROVE_OPTION)
        {
            rutaCert = elegir.getSelectedFile().getPath();
            
            if (rutaCert.length() > 70)
                lblRutaArchivo.setText(rutaCert.substring(0, 70) + "...");
            else
                lblRutaArchivo.setText(rutaCert);
        }
        
        txt_pin.requestFocus();
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnexaminarActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOkActionPerformed
    {//GEN-HEADEREND:event_btnOkActionPerformed
        checkCertificado((char)KeyEvent.VK_ENTER);
    }//GEN-LAST:event_btnOkActionPerformed

    private void checkCertificado(char caracter)
    {
        if (caracter == KeyEvent.VK_ENTER)
        {
            new Thread(() ->
            {
                char[] pin = txt_pin.getPassword();

                if (tiporepositorio.equals(Repositorio.TOKEN))
                {
                    try
                    {
                        if (pin == null || pin.length == 0)
                        {
                            CLog.setMensaje_error("Ingrese pin del token");
                            new frmMensaje(frmPin.this, true, CLog.getMensaje_error(), lbltitulo.getText()).setVisible(true);
                            return;
                        }
                        
                        if (cboLibreria.getItemCount() > 0)
                        {
                            rutaCert = frmConfigurador.configuracion.getVg_lista_librerias().get(cboLibreria.getSelectedIndex()).getRuta();
                            
                            if (rutaCert.length() > 70)
                                lblRutaArchivo.setText(rutaCert.substring(0, 70) + "...");
                            else
                                lblRutaArchivo.setText(rutaCert);

                            frmConfigurador.configuracion.setVg_lista_librerias(frmConfigurador.configuracion.getVg_lista_librerias());
                        }
                        else
                        {
                            CLog.setMensaje_error("No se han configruado las librerias para el uso del token.");
                            new frmMensaje(frmPin.this, true, CLog.getMensaje_error(), lbltitulo.getText()).setVisible(true);
                            return;
                        }
                        
                        frmConfigurador.configuracion.setTipoRepositorio(tiporepositorio);
                        frmConfigurador.configuracion.setVg_ruta_libreria(rutaCert);
                        frmConfigurador.configuracion.setVg_clave_token(new helperClass().encrypt(new String(txt_pin.getPassword())));
                        
                        new frmCertificados(frmPin.this, true, tiporepositorio, frmConfigurador.configuracion.getVg_ruta_libreria(), new helperClass().decrypt(frmConfigurador.configuracion.getVg_clave_token())).setVisible(true);
                    }
                    catch (Exception ex)
                    {
                        new CLog().archivar_log("[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]|" + CConstantes.X_ADVERTENCIA + "|" + CConstantes.X_EXCEPCION + "|" + ex + "|" + ex.getMessage() + "|");
                        new frmMensaje(frmPin.this, true, ex.getMessage(), lbltitulo.getText()).setVisible(true);
                    }
                }
                else
                {
                    try
                    {
                        if (pin == null || pin.length == 0)
                        {
                            CLog.setMensaje_error("Ingrese pin del certificado");
                            new frmMensaje(frmPin.this, true, CLog.getMensaje_error(), lbltitulo.getText()).setVisible(true);
                            return;
                        }
                        
                        if (!new File(rutaCert).exists())
                        {
                            CLog.setMensaje_error("El certificado [" + rutaCert + "] no existe.");
                            new frmMensaje(frmPin.this, true, CLog.getMensaje_error(), lbltitulo.getText()).setVisible(true);
                            return;
                        }
                        
                        List<Firmante> LtDeCertificados = ObtenerCertificados(rutaCert, new String(txt_pin.getPassword()));
                        
                        if (LtDeCertificados == null || LtDeCertificados.isEmpty())
                        {
                            new frmMensaje(frmPin.this, true, CLog.getMensaje_error(), lbltitulo.getText()).setVisible(true);
                            return;
                        }
                        else
                        {
                            String respuesta;
                            /*VALIDANDO CERTIFICADOS*/
                            if (!frmConfigurador.repositorios.validarAliasCertificado(LtDeCertificados.get(0).getCertificado().getAlias()))
                            {
                                CLog.setMensaje_error("Alias no válido.");
                                new frmMensaje(frmPin.this, true, CLog.getMensaje_error(), lbltitulo.getText()).setVisible(true);
                                return;
                            }
                            
                            String tsl = "";
                            String norepudio = "FALSE";
                            
                            if (frmConfigurador.configuracion.isVg_validar_con_tsl())
                                tsl = frmConfigurador.configuracion.getVg_url_tsl().trim();
                            
                            if (frmConfigurador.configuracion.isVg_validar_con_no_repudio())
                                norepudio = CConstantes.X_TRUE;
                            
                            frmConfigurador.repositorios.setFirmanteElegido(LtDeCertificados.get(0));

                            respuesta = Metodos.ValidarCertificados.filtrarCertificado(frmConfigurador.repositorios.getFirmanteElegido().getCertificado().getCertificadosConfianza(),
                                    new Date(), tsl, frmConfigurador.vcg_so.ObtenerSistemaOperativo_user_home() + File.separator, 
                                    frmConfigurador.configuracion.getUrlProxy(), frmConfigurador.configuracion.getPuertoProxy(),
                                    CConstantes.YconvertiranumeroBooleanoString("" + norepudio));
                        
                            /*FIN VALIDACION*/
                            if (respuesta.equals("0"))
                            {
                                frmConfigurador.txtResultado.setText(Metodos.ValidarCertificados.getMensajeDeError());
                                frmConfigurador.configuracion.setTipoRepositorio(tiporepositorio);
                                
                                String sv;
                                
                                if(frmConfigurador.configuracion.getVg_ruta_certificado().trim().isEmpty())
                                    sv = rutaCert;
                                else
                                    sv = frmConfigurador.configuracion.getVg_ruta_certificado().trim() + ";" + rutaCert;

                                frmConfigurador.configuracion.setVg_ruta_certificado(sv);
                                
                                if(frmConfigurador.configuracion.getVg_clave_certificado().trim().isEmpty())
                                    sv = new helperClass().encrypt(new String(txt_pin.getPassword()));
                                else
                                    sv = frmConfigurador.configuracion.getVg_clave_certificado().trim() + ";" + new helperClass().encrypt(new String(txt_pin.getPassword()));

                                frmConfigurador.configuracion.setVg_clave_certificado(sv);
                                
                                Repositorio.cerrarventana = 1;
                                setVisible(false);
                                dispose();
                            }
                            else
                            {
                                new frmMensaje(frmPin.this, true, Metodos.ValidarCertificados.Mensaje(), lbltitulo.getText()).setVisible(true);
                                return;
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        new frmMensaje(frmPin.this, true, Metodos.ValidarCertificados.Mensaje(), lbltitulo.getText()).setVisible(true);
                        return;
                    }
                }
                
                setVisible(false);
            }).start();
        }
    }
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnexaminar;
    private javax.swing.JComboBox cboLibreria;
    private javax.swing.JLabel ctrl_lbl_cerrar;
    private java.awt.TextArea lbRespuesta;
    private javax.swing.JLabel lblRutaArchivo;
    private javax.swing.JLabel lbletiquetapin;
    private javax.swing.JLabel lbllistando;
    private javax.swing.JLabel lblmensajelibreria;
    private javax.swing.JLabel lbltitulo;
    private javax.swing.JPanel pnlArchivo;
    private javax.swing.JPanel pnlBarra3;
    private javax.swing.JPanel pnlConfiguracion;
    private javax.swing.JPasswordField txt_pin;
    // End of variables declaration//GEN-END:variables
}
