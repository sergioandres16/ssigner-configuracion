package Formularios;
import java.awt.Color;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public final class frmCarpetas extends javax.swing.JDialog {
    /*HILOS*/
    Thread hiloExaminar;

    public frmCarpetas(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
        ctrl_txt_carpeta_salida.setText(frmConfigurador.configuracion.getVg_carpeta_salida());
        ctrl_txt_carpeta_salida_tsl_crl.setText(frmConfigurador.configuracion.getVg_carpeta_salida_crl_tsl());
        ctrl_txt_conf_ext.setText(frmConfigurador.configuracion.getVg_documento_extension_salida());
        ctrl_lbl_ventana_titulo.setText("Configurar carpetas");
        ctrl_rb_ext_ini.setSelected(frmConfigurador.configuracion.isVg_documento_extension_inicio());
        foco_textbox();
        setLocationRelativeTo(owner);
    }

    private void cerrarVentana(){
        setVisible(false);
        dispose();
    }

    protected void cambiarColor(Object obj){
        cambiarColor(obj,Color.lightGray,Color.white);
    }
    protected void cambiarColor(Object obj,Color colorfondo,Color colorletra){
        if(obj instanceof javax.swing.JLabel){
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        }
        else if(obj instanceof JButton){
            ((JButton) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        } 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grp_ext = new javax.swing.ButtonGroup();
        pnlConfiguracion = new javax.swing.JPanel();
        pnlArchivo = new javax.swing.JPanel();
        lblmensajelibreria = new javax.swing.JLabel();
        ctrl_txt_carpeta_salida = new javax.swing.JLabel();
        ctrl_btn_examinar_carpeta_salida = new javax.swing.JButton();
        ctrl_txt_carpeta_salida_tsl_crl = new javax.swing.JLabel();
        lblmensajelibreria1 = new javax.swing.JLabel();
        ctrl_btn_examinar_carpeta_tsl_crl = new javax.swing.JButton();
        ctrl_btn_guardar_cambios = new javax.swing.JButton();
        ctrl_txt_conf_ext = new javax.swing.JTextField();
        lblmensajelibreria2 = new javax.swing.JLabel();
        ctrl_rb_ext_ini = new javax.swing.JRadioButton();
        ctrl_rb_ext_fin = new javax.swing.JRadioButton();
        pnlBarra3 = new javax.swing.JPanel();
        ctrl_btn_ventana_logo = new javax.swing.JButton();
        ctrl_lbl_ventana_cerrar = new javax.swing.JLabel();
        ctrl_lbl_ventana_titulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlConfiguracion.setBackground(new java.awt.Color(241, 241, 241));
        pnlConfiguracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnlArchivo.setBackground(new java.awt.Color(241, 241, 241));
        pnlArchivo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 1, 11))); // NOI18N

        lblmensajelibreria.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        lblmensajelibreria.setForeground(new java.awt.Color(67, 73, 111));
        lblmensajelibreria.setText("Indique la carpeta de salida para los documentos firmados");

        ctrl_txt_carpeta_salida.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        ctrl_txt_carpeta_salida.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        ctrl_btn_examinar_carpeta_salida.setText("..");
        ctrl_btn_examinar_carpeta_salida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_examinar_carpeta_salidaActionPerformed(evt);
            }
        });

        ctrl_txt_carpeta_salida_tsl_crl.setFont(new java.awt.Font("Segoe UI Light", 0, 11)); // NOI18N
        ctrl_txt_carpeta_salida_tsl_crl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        lblmensajelibreria1.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        lblmensajelibreria1.setForeground(new java.awt.Color(67, 73, 111));
        lblmensajelibreria1.setText("Indique la carpeta de salida para la TSL y la CRL");

        ctrl_btn_examinar_carpeta_tsl_crl.setText("..");
        ctrl_btn_examinar_carpeta_tsl_crl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_examinar_carpeta_tsl_crlActionPerformed(evt);
            }
        });

        ctrl_btn_guardar_cambios.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        ctrl_btn_guardar_cambios.setText("Aceptar");
        ctrl_btn_guardar_cambios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ctrl_btn_guardar_cambios.setPreferredSize(new java.awt.Dimension(183, 42));
        ctrl_btn_guardar_cambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_guardar_cambiosActionPerformed(evt);
            }
        });

        ctrl_txt_conf_ext.setPreferredSize(new java.awt.Dimension(181, 28));
        ctrl_txt_conf_ext.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                ctrl_txt_conf_extCaretUpdate(evt);
            }
        });

        lblmensajelibreria2.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        lblmensajelibreria2.setForeground(new java.awt.Color(67, 73, 111));
        lblmensajelibreria2.setText("Configure la extensión del documento firmado");

        grp_ext.add(ctrl_rb_ext_ini);
        ctrl_rb_ext_ini.setText("Inicio");
        ctrl_rb_ext_ini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_rb_ext_iniActionPerformed(evt);
            }
        });

        grp_ext.add(ctrl_rb_ext_fin);
        ctrl_rb_ext_fin.setSelected(true);
        ctrl_rb_ext_fin.setText("Final");
        ctrl_rb_ext_fin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_rb_ext_finActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlArchivoLayout = new javax.swing.GroupLayout(pnlArchivo);
        pnlArchivo.setLayout(pnlArchivoLayout);
        pnlArchivoLayout.setHorizontalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlArchivoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblmensajelibreria, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                            .addGroup(pnlArchivoLayout.createSequentialGroup()
                                .addComponent(ctrl_txt_carpeta_salida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(ctrl_btn_examinar_carpeta_salida, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblmensajelibreria1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlArchivoLayout.createSequentialGroup()
                                .addComponent(ctrl_txt_carpeta_salida_tsl_crl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(ctrl_btn_examinar_carpeta_tsl_crl, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlArchivoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlArchivoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(ctrl_rb_ext_ini)
                                .addGap(18, 18, 18)
                                .addComponent(ctrl_rb_ext_fin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ctrl_txt_conf_ext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblmensajelibreria2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(6, 6, 6))
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(ctrl_btn_guardar_cambios, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlArchivoLayout.setVerticalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblmensajelibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ctrl_btn_examinar_carpeta_salida, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(ctrl_txt_carpeta_salida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblmensajelibreria2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctrl_txt_conf_ext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_rb_ext_ini)
                    .addComponent(ctrl_rb_ext_fin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblmensajelibreria1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ctrl_btn_examinar_carpeta_tsl_crl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ctrl_txt_carpeta_salida_tsl_crl, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ctrl_btn_guardar_cambios, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlBarra3.setBackground(new java.awt.Color(241, 241, 241));
        pnlBarra3.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N

        javax.swing.GroupLayout pnlBarra3Layout = new javax.swing.GroupLayout(pnlBarra3);
        pnlBarra3.setLayout(pnlBarra3Layout);
        pnlBarra3Layout.setHorizontalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        pnlBarra3Layout.setVerticalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        ctrl_btn_ventana_logo.setBackground(new java.awt.Color(211, 111, 66));
        ctrl_btn_ventana_logo.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        ctrl_btn_ventana_logo.setForeground(new java.awt.Color(255, 255, 255));
        ctrl_btn_ventana_logo.setToolTipText("Cargar documentos para visualzar.");
        ctrl_btn_ventana_logo.setAutoscrolls(true);
        ctrl_btn_ventana_logo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        ctrl_btn_ventana_logo.setBorderPainted(false);
        ctrl_btn_ventana_logo.setContentAreaFilled(false);
        ctrl_btn_ventana_logo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_btn_ventana_logo.setFocusPainted(false);
        ctrl_btn_ventana_logo.setPreferredSize(new java.awt.Dimension(55, 26));
        ctrl_btn_ventana_logo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ctrl_btn_ventana_logoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ctrl_btn_ventana_logoMouseExited(evt);
            }
        });
        ctrl_btn_ventana_logo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_ventana_logoActionPerformed(evt);
            }
        });

        ctrl_lbl_ventana_cerrar.setBackground(new java.awt.Color(255, 153, 153));
        ctrl_lbl_ventana_cerrar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        ctrl_lbl_ventana_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        ctrl_lbl_ventana_cerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ctrl_lbl_ventana_cerrar.setText("x");
        ctrl_lbl_ventana_cerrar.setToolTipText("Cerrar");
        ctrl_lbl_ventana_cerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_lbl_ventana_cerrar.setOpaque(true);
        ctrl_lbl_ventana_cerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctrl_lbl_ventana_cerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ctrl_lbl_ventana_cerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ctrl_lbl_ventana_cerrarMouseExited(evt);
            }
        });

        ctrl_lbl_ventana_titulo.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        ctrl_lbl_ventana_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ctrl_lbl_ventana_titulo.setText("SSIGNER");

        javax.swing.GroupLayout pnlConfiguracionLayout = new javax.swing.GroupLayout(pnlConfiguracion);
        pnlConfiguracion.setLayout(pnlConfiguracionLayout);
        pnlConfiguracionLayout.setHorizontalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(ctrl_btn_ventana_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(ctrl_lbl_ventana_titulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarra3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ctrl_lbl_ventana_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(pnlArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnlConfiguracionLayout.setVerticalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBarra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_btn_ventana_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_lbl_ventana_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_lbl_ventana_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConfiguracion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ctrl_btn_examinar_carpeta_salidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_examinar_carpeta_salidaActionPerformed
        hiloExaminar=new Thread( new Runnable() {
            @Override
            public void run() {
                JFileChooser elegir = new JFileChooser(ctrl_txt_carpeta_salida.getText());
                elegir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                elegir.setAcceptAllFileFilterUsed(false);
                int opcion = elegir.showOpenDialog(frmCarpetas.this);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    ctrl_txt_carpeta_salida.setText(elegir.getSelectedFile().getPath());
                    //frmConfigurador.configuracion.setVg_carpeta_salida(ctrl_txt_carpeta_salida.getText());
                }
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
        hiloExaminar.setDaemon(true);
        hiloExaminar.start();          
    }//GEN-LAST:event_ctrl_btn_examinar_carpeta_salidaActionPerformed

    private void ctrl_lbl_ventana_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_ventana_cerrarMouseClicked
        cerrarVentana();
    }//GEN-LAST:event_ctrl_lbl_ventana_cerrarMouseClicked

    private void ctrl_lbl_ventana_cerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_ventana_cerrarMouseEntered
        cambiarColor((javax.swing.JLabel)evt.getSource(),Color.red,Color.white);
    }//GEN-LAST:event_ctrl_lbl_ventana_cerrarMouseEntered

    private void ctrl_lbl_ventana_cerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_ventana_cerrarMouseExited
        cambiarColor((javax.swing.JLabel)evt.getSource(),new Color(255,153,153),Color.white);
    }//GEN-LAST:event_ctrl_lbl_ventana_cerrarMouseExited

    private void ctrl_btn_ventana_logoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoMouseEntered

    private void ctrl_btn_ventana_logoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoMouseExited

    private void ctrl_btn_ventana_logoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//         Repositorio.lblDesplegando.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void ctrl_btn_examinar_carpeta_tsl_crlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_examinar_carpeta_tsl_crlActionPerformed
        hiloExaminar=new Thread( new Runnable() {
            @Override
            public void run() {
                JFileChooser elegir = new JFileChooser(ctrl_txt_carpeta_salida_tsl_crl.getText());
                elegir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                elegir.setAcceptAllFileFilterUsed(false);
                int opcion = elegir.showOpenDialog(frmCarpetas.this);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    ctrl_txt_carpeta_salida_tsl_crl.setText(elegir.getSelectedFile().getPath());
                   // frmConfigurador.configuracion.setVg_carpeta_salida_crl_tsl(ctrl_txt_carpeta_salida_tsl_crl.getText());
                }
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
        hiloExaminar.setDaemon(true);
        hiloExaminar.start();  
    }//GEN-LAST:event_ctrl_btn_examinar_carpeta_tsl_crlActionPerformed

    private void ctrl_btn_guardar_cambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_guardar_cambiosActionPerformed
        if(ctrl_rb_ext_ini.isSelected()){
             frmConfigurador.configuracion.setVg_documento_extension_inicio(true);
        }
        if(ctrl_rb_ext_fin.isSelected()){
             frmConfigurador.configuracion.setVg_documento_extension_inicio(false);
        }
                       
        frmConfigurador.configuracion.setVg_carpeta_salida(ctrl_txt_carpeta_salida.getText());
        frmConfigurador.configuracion.setVg_documento_extension_salida(ctrl_txt_conf_ext.getText().trim());
        frmConfigurador.configuracion.setVg_carpeta_salida_crl_tsl(ctrl_txt_carpeta_salida_tsl_crl.getText());
        dispose();
        setVisible(false);
    }//GEN-LAST:event_ctrl_btn_guardar_cambiosActionPerformed

    private void ctrl_txt_conf_extCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_ctrl_txt_conf_extCaretUpdate
        
    }//GEN-LAST:event_ctrl_txt_conf_extCaretUpdate
    void foco_textbox(){
        ctrl_txt_conf_ext.requestFocus();
        if(!ctrl_txt_conf_ext.getText().trim().equals("")){
            ctrl_txt_conf_ext.setSelectionStart(0);
             ctrl_txt_conf_ext.setSelectionEnd(ctrl_txt_conf_ext.getText().length());
        }
    }
    private void ctrl_rb_ext_iniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_rb_ext_iniActionPerformed
        foco_textbox();
    }//GEN-LAST:event_ctrl_rb_ext_iniActionPerformed

    private void ctrl_rb_ext_finActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_rb_ext_finActionPerformed
        foco_textbox();
    }//GEN-LAST:event_ctrl_rb_ext_finActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ctrl_btn_examinar_carpeta_salida;
    private javax.swing.JButton ctrl_btn_examinar_carpeta_tsl_crl;
    private javax.swing.JButton ctrl_btn_guardar_cambios;
    public javax.swing.JButton ctrl_btn_ventana_logo;
    private javax.swing.JLabel ctrl_lbl_ventana_cerrar;
    private javax.swing.JLabel ctrl_lbl_ventana_titulo;
    private javax.swing.JRadioButton ctrl_rb_ext_fin;
    private javax.swing.JRadioButton ctrl_rb_ext_ini;
    private javax.swing.JLabel ctrl_txt_carpeta_salida;
    private javax.swing.JLabel ctrl_txt_carpeta_salida_tsl_crl;
    private javax.swing.JTextField ctrl_txt_conf_ext;
    private javax.swing.ButtonGroup grp_ext;
    private javax.swing.JLabel lblmensajelibreria;
    private javax.swing.JLabel lblmensajelibreria1;
    private javax.swing.JLabel lblmensajelibreria2;
    private javax.swing.JPanel pnlArchivo;
    private javax.swing.JPanel pnlBarra3;
    private javax.swing.JPanel pnlConfiguracion;
    // End of variables declaration//GEN-END:variables
}
