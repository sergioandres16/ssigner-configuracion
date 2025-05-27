package Formularios;
import java.awt.Color;
import javax.swing.JButton;
import static Formularios.frmConfigurador.configuracion;

public final class frmValidaciones extends javax.swing.JDialog {

    public frmValidaciones(javax.swing.JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ctrl_chk_certificado_tsl_validar.setSelected(configuracion.isVg_validar_con_tsl());
        ctrl_txt_tsl_servicio_url.setText(configuracion.getVg_url_tsl());
        ctrl_chk_certificado_no_repudio_validar.setSelected(configuracion.isVg_validar_con_no_repudio());
        if(ctrl_chk_certificado_tsl_validar.isSelected()){
            ctrl_txt_tsl_servicio_url.setEnabled(true);
            ctrl_txt_tsl_servicio_url.requestFocus();
            if(ctrl_txt_tsl_servicio_url.getText().trim().length()>0){
                ctrl_txt_tsl_servicio_url.select(0, ctrl_txt_tsl_servicio_url.getText().trim().length());
            }
        }
        else
            ctrl_txt_tsl_servicio_url.setEnabled(false);
         setLocationRelativeTo(parent);
    }
    private void m_ventana_cerrar(){
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
            ((javax.swing.JButton) obj).setForeground(colorletra);
            ((javax.swing.JButton) obj).setOpaque(true);
        } 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlConfiguracion = new javax.swing.JPanel();
        pnlBarra3 = new javax.swing.JPanel();
        lblCerrar = new javax.swing.JLabel();
        lbltitulo = new javax.swing.JLabel();
        ctrl_lbl_logo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        pnlArchivo = new javax.swing.JPanel();
        ctrl_chk_certificado_tsl_validar = new javax.swing.JCheckBox();
        ctrl_chk_certificado_no_repudio_validar = new javax.swing.JCheckBox();
        ctrl_txt_tsl_servicio_url = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ctrl_btn_continuar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlConfiguracion.setBackground(new java.awt.Color(241, 241, 241));
        pnlConfiguracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnlBarra3.setBackground(new java.awt.Color(241, 241, 241));
        pnlBarra3.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N

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

        lbltitulo.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        lbltitulo.setText("Validaciones");

        javax.swing.GroupLayout pnlBarra3Layout = new javax.swing.GroupLayout(pnlBarra3);
        pnlBarra3.setLayout(pnlBarra3Layout);
        pnlBarra3Layout.setHorizontalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(lbltitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        pnlBarra3Layout.setVerticalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbltitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ctrl_lbl_logo.setBackground(new java.awt.Color(211, 111, 66));
        ctrl_lbl_logo.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        ctrl_lbl_logo.setForeground(new java.awt.Color(255, 255, 255));
        ctrl_lbl_logo.setToolTipText("Cargar documentos para visualzar.");
        ctrl_lbl_logo.setAutoscrolls(true);
        ctrl_lbl_logo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        ctrl_lbl_logo.setBorderPainted(false);
        ctrl_lbl_logo.setContentAreaFilled(false);
        ctrl_lbl_logo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_lbl_logo.setFocusPainted(false);
        ctrl_lbl_logo.setPreferredSize(new java.awt.Dimension(55, 26));
        ctrl_lbl_logo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ctrl_lbl_logoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ctrl_lbl_logoMouseExited(evt);
            }
        });
        ctrl_lbl_logo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_lbl_logoActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(241, 241, 241));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        pnlArchivo.setBackground(new java.awt.Color(241, 241, 241));
        pnlArchivo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Lista de validaciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 1, 11), new java.awt.Color(67, 73, 111))); // NOI18N
        pnlArchivo.setForeground(new java.awt.Color(0, 83, 154));
        pnlArchivo.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N

        ctrl_chk_certificado_tsl_validar.setBackground(new java.awt.Color(255, 255, 255));
        ctrl_chk_certificado_tsl_validar.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        ctrl_chk_certificado_tsl_validar.setForeground(new java.awt.Color(0, 83, 154));
        ctrl_chk_certificado_tsl_validar.setSelected(true);
        ctrl_chk_certificado_tsl_validar.setText("Validar en la Lista de Servicio de Confianza (TSL)");
        ctrl_chk_certificado_tsl_validar.setContentAreaFilled(false);
        ctrl_chk_certificado_tsl_validar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_chk_certificado_tsl_validar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_chk_certificado_tsl_validarActionPerformed(evt);
            }
        });

        ctrl_chk_certificado_no_repudio_validar.setBackground(new java.awt.Color(255, 255, 255));
        ctrl_chk_certificado_no_repudio_validar.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        ctrl_chk_certificado_no_repudio_validar.setForeground(new java.awt.Color(0, 83, 154));
        ctrl_chk_certificado_no_repudio_validar.setSelected(true);
        ctrl_chk_certificado_no_repudio_validar.setText("Validar marca de no repudio");
        ctrl_chk_certificado_no_repudio_validar.setContentAreaFilled(false);
        ctrl_chk_certificado_no_repudio_validar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_chk_certificado_no_repudio_validar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_chk_certificado_no_repudio_validarActionPerformed(evt);
            }
        });

        ctrl_txt_tsl_servicio_url.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        ctrl_txt_tsl_servicio_url.setToolTipText("Ruta de Imagen");
        ctrl_txt_tsl_servicio_url.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        ctrl_txt_tsl_servicio_url.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 83, 154));
        jLabel4.setText("Ingrese las url separados por comas (,)");

        javax.swing.GroupLayout pnlArchivoLayout = new javax.swing.GroupLayout(pnlArchivo);
        pnlArchivo.setLayout(pnlArchivoLayout);
        pnlArchivoLayout.setHorizontalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ctrl_chk_certificado_tsl_validar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ctrl_chk_certificado_no_repudio_validar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ctrl_txt_tsl_servicio_url, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlArchivoLayout.setVerticalGroup(
            pnlArchivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlArchivoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ctrl_chk_certificado_tsl_validar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(ctrl_txt_tsl_servicio_url)
                .addGap(8, 8, 8)
                .addComponent(ctrl_chk_certificado_no_repudio_validar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ctrl_btn_continuar.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        ctrl_btn_continuar.setText("Guardar cambios");
        ctrl_btn_continuar.setAutoscrolls(true);
        ctrl_btn_continuar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_btn_continuar.setFocusPainted(false);
        ctrl_btn_continuar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ctrl_btn_continuarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ctrl_btn_continuarMouseExited(evt);
            }
        });
        ctrl_btn_continuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_continuarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ctrl_btn_continuar, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ctrl_btn_continuar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlConfiguracionLayout = new javax.swing.GroupLayout(pnlConfiguracion);
        pnlConfiguracion.setLayout(pnlConfiguracionLayout);
        pnlConfiguracionLayout.setHorizontalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addComponent(ctrl_lbl_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarra3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlConfiguracionLayout.setVerticalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBarra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_lbl_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        m_ventana_cerrar();
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void lblCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseEntered
        cambiarColor((javax.swing.JLabel)evt.getSource(),Color.red,Color.white);
    }//GEN-LAST:event_lblCerrarMouseEntered

    private void lblCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseExited
        cambiarColor((javax.swing.JLabel)evt.getSource(),new Color(255,153,153),Color.white);
    }//GEN-LAST:event_lblCerrarMouseExited

    private void ctrl_lbl_logoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_logoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_lbl_logoMouseEntered

    private void ctrl_lbl_logoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_logoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_lbl_logoMouseExited

    private void ctrl_lbl_logoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_lbl_logoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_lbl_logoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//         Repositorio.lblDesplegando.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void ctrl_chk_certificado_tsl_validarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_chk_certificado_tsl_validarActionPerformed
        if(ctrl_chk_certificado_tsl_validar.isSelected()){
            ctrl_txt_tsl_servicio_url.setEnabled(true);
            ctrl_txt_tsl_servicio_url.requestFocus();
            if(ctrl_txt_tsl_servicio_url.getText().trim().length()>0){
                ctrl_txt_tsl_servicio_url.select(0, ctrl_txt_tsl_servicio_url.getText().trim().length());
            }
        }
        else
            ctrl_txt_tsl_servicio_url.setEnabled(false);
    }//GEN-LAST:event_ctrl_chk_certificado_tsl_validarActionPerformed

    private void ctrl_chk_certificado_no_repudio_validarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_chk_certificado_no_repudio_validarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_chk_certificado_no_repudio_validarActionPerformed

    private void ctrl_btn_continuarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_continuarMouseEntered
       
    }//GEN-LAST:event_ctrl_btn_continuarMouseEntered

    private void ctrl_btn_continuarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_continuarMouseExited

    }//GEN-LAST:event_ctrl_btn_continuarMouseExited

    private void ctrl_btn_continuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_continuarActionPerformed
        configuracion.setVg_validar_con_no_repudio(ctrl_chk_certificado_no_repudio_validar.isSelected());
        configuracion.setVg_validar_con_tsl(ctrl_chk_certificado_tsl_validar.isSelected());
        configuracion.setVg_url_tsl(ctrl_txt_tsl_servicio_url.getText());
        setVisible(false);
        dispose();
    }//GEN-LAST:event_ctrl_btn_continuarActionPerformed
  

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton ctrl_btn_continuar;
    protected static javax.swing.JCheckBox ctrl_chk_certificado_no_repudio_validar;
    protected static javax.swing.JCheckBox ctrl_chk_certificado_tsl_validar;
    public javax.swing.JButton ctrl_lbl_logo;
    protected static javax.swing.JTextField ctrl_txt_tsl_servicio_url;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lbltitulo;
    private javax.swing.JPanel pnlArchivo;
    private javax.swing.JPanel pnlBarra3;
    private javax.swing.JPanel pnlConfiguracion;
    // End of variables declaration//GEN-END:variables
}
