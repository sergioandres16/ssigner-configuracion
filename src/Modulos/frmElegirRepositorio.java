package Modulos;

import Clases.Repositorio;
import java.awt.Color;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JButton;

public final class frmElegirRepositorio extends javax.swing.JDialog
{
    public static boolean cargo;
    public static int cerrarventana = 1;

    public frmElegirRepositorio(java.awt.Frame parent, boolean modal, String clave, String pagina, String documento)
    {
        super(parent, modal);
        initComponents();
        lblDesplegando.setVisible(false);
        cargo = false;
        iniciarBotones(clave, pagina, documento);
        
        if (frmFirmador.configuracion.getLibrerias() == null || frmFirmador.configuracion.getLibrerias().isEmpty())
        {
            lblDesplegando.setVisible(true);
            lblDesplegando.setText("Por favor registre la(s) librería(s) en SSignerConfiguracion, para utilizar la firma con token");
            lblDesplegando.setToolTipText(lblDesplegando.getText());
            btnToken.setEnabled(false);
            btnToken.setContentAreaFilled(true);
            cambiarColor(btnToken, java.awt.Color.gray, java.awt.Color.gray);
        }
    }

    public frmElegirRepositorio(java.awt.Dialog parent, boolean modal, String clave, String pagina, String documento)
    {
        super(parent, modal);
        initComponents();
        lblDesplegando.setVisible(false);
        cargo = false;
        iniciarBotones(clave, pagina, documento);
    }

    public void iniciarBotones(final String clave, final String pagina, final String documento)
    {
        Enumeration<AbstractButton> enumeration = grupoRepositorio.getElements();
        while (enumeration.hasMoreElements())
        {
            final JButton obj = (JButton) enumeration.nextElement();
            obj.addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    JButton btnTipoRepositorio = (JButton) evt.getSource();
                    if (btnTipoRepositorio.getName().equalsIgnoreCase(Repositorio.WINDOWS_MY))
                    {
                        lblDesplegando.setVisible(true);
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                while (!cargo)
                                {
                                    try
                                    {
                                        Thread.sleep(600);
                                        lblDesplegando.setText("Listando certificados.");
                                        Thread.sleep(600);
                                        lblDesplegando.setText("Listando certificados..");
                                        Thread.sleep(600);
                                        lblDesplegando.setText("Listando certificados...");
                                        Thread.sleep(600);
                                        lblDesplegando.setText("Listando certificados....");
                                        Thread.sleep(600);
                                        lblDesplegando.setText("Listando certificados.....");
                                    } catch (InterruptedException ex) { }
                                }
                                lblDesplegando.setText("");
                            }
                        }).start();
                    }
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (!frmFirmador.repositorios.validarTipoRepositorio(obj.getName().toUpperCase()))
                            {
                                Global.CConstantes.dialogo("El repositorio no existe.");
                                return;
                            }

                            if (obj.getName().equalsIgnoreCase(Repositorio.WINDOWS_MY))
                                new frmCertificados(frmElegirRepositorio.this, true, obj.getName().toUpperCase(), "", "").setVisible(true);
                            else
                                new pin(frmElegirRepositorio.this, true, obj.getName().toUpperCase()).setVisible(true);
                            
                            if (cerrarventana == 0)
                                return;
                            
                            setVisible(false);
                            dispose();
                        }
                    }).start();
                }
            });
        }
    }

    void cerrarVentana() 
    {
        setVisible(false);
        dispose();
    }

    protected void cambiarColor(Object obj, Color colorfondo, Color colorletra) {
        if (obj instanceof javax.swing.JLabel) {
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        } else if (obj instanceof JButton) {
            ((JButton) obj).setBackground(colorfondo);
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
    private void initComponents()
    {

        grupoRepositorio = new javax.swing.ButtonGroup();
        pnlPadreTipoRepositorios = new javax.swing.JPanel();
        btnWindows = new javax.swing.JButton();
        btnToken = new javax.swing.JButton();
        btnArchivo = new javax.swing.JButton();
        lblDesplegando = new javax.swing.JLabel();
        pnlBarra3 = new javax.swing.JPanel();
        lblCerrar = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        btnLogo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REPOSITORIOS");
        setUndecorated(true);
        addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                formFocusGained(evt);
            }
        });

        pnlPadreTipoRepositorios.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlPadreTipoRepositorios.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                pnlPadreTipoRepositoriosFocusGained(evt);
            }
        });

        btnWindows.setBackground(new java.awt.Color(211, 111, 66));
        btnWindows.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnWindows.setForeground(new java.awt.Color(255, 255, 255));
        btnWindows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Modulos/Windows_Vista_logo.png"))); // NOI18N
        btnWindows.setText("WINDOWS-MY");
        btnWindows.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        grupoRepositorio.add(btnWindows);
        btnWindows.setContentAreaFilled(false);
        btnWindows.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWindows.setName("Windows-My"); // NOI18N
        btnWindows.setOpaque(true);
        btnWindows.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnWindowsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                btnWindowsMouseExited(evt);
            }
        });
        btnWindows.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnWindowsActionPerformed(evt);
            }
        });

        btnToken.setBackground(new java.awt.Color(211, 111, 66));
        btnToken.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnToken.setForeground(new java.awt.Color(255, 255, 255));
        btnToken.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Modulos/CRYPTOMATE64.png"))); // NOI18N
        btnToken.setText("TOKEN");
        btnToken.setAutoscrolls(true);
        btnToken.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        grupoRepositorio.add(btnToken);
        btnToken.setContentAreaFilled(false);
        btnToken.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnToken.setName("PKCS11"); // NOI18N
        btnToken.setOpaque(true);
        btnToken.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnTokenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                btnTokenMouseExited(evt);
            }
        });
        btnToken.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTokenActionPerformed(evt);
            }
        });

        btnArchivo.setBackground(new java.awt.Color(211, 111, 66));
        btnArchivo.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnArchivo.setForeground(new java.awt.Color(255, 255, 255));
        btnArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Certificado.png"))); // NOI18N
        btnArchivo.setText("ARCHIVO");
        btnArchivo.setAutoscrolls(true);
        btnArchivo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        grupoRepositorio.add(btnArchivo);
        btnArchivo.setContentAreaFilled(false);
        btnArchivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArchivo.setName("PKCS12"); // NOI18N
        btnArchivo.setOpaque(true);
        btnArchivo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnArchivoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                btnArchivoMouseExited(evt);
            }
        });
        btnArchivo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnArchivoActionPerformed(evt);
            }
        });

        lblDesplegando.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        lblDesplegando.setForeground(new java.awt.Color(67, 73, 111));
        lblDesplegando.setText("Listando certificados");

        lblCerrar.setBackground(new java.awt.Color(255, 153, 153));
        lblCerrar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCerrar.setForeground(new java.awt.Color(255, 255, 255));
        lblCerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar.setText("x");
        lblCerrar.setToolTipText("Cerrar");
        lblCerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar.setOpaque(true);
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                lblCerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                lblCerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                lblCerrarMouseExited(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("REPOSITORIOS");

        btnLogo.setBackground(new java.awt.Color(211, 111, 66));
        btnLogo.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnLogo.setForeground(new java.awt.Color(255, 255, 255));
        btnLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo Ssigner-01.png"))); // NOI18N
        btnLogo.setToolTipText("Cargar documentos para visualzar.");
        btnLogo.setAutoscrolls(true);
        btnLogo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btnLogo.setBorderPainted(false);
        btnLogo.setContentAreaFilled(false);
        btnLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogo.setFocusPainted(false);
        btnLogo.setPreferredSize(new java.awt.Dimension(55, 26));
        btnLogo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                btnLogoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                btnLogoMouseExited(evt);
            }
        });
        btnLogo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLogoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBarra3Layout = new javax.swing.GroupLayout(pnlBarra3);
        pnlBarra3.setLayout(pnlBarra3Layout);
        pnlBarra3Layout.setHorizontalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarra3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBarra3Layout.setVerticalGroup(
            pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarra3Layout.createSequentialGroup()
                .addGroup(pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBarra3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBarra3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlPadreTipoRepositoriosLayout = new javax.swing.GroupLayout(pnlPadreTipoRepositorios);
        pnlPadreTipoRepositorios.setLayout(pnlPadreTipoRepositoriosLayout);
        pnlPadreTipoRepositoriosLayout.setHorizontalGroup(
            pnlPadreTipoRepositoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBarra3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPadreTipoRepositoriosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlPadreTipoRepositoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPadreTipoRepositoriosLayout.createSequentialGroup()
                        .addComponent(lblDesplegando, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPadreTipoRepositoriosLayout.createSequentialGroup()
                        .addGroup(pnlPadreTipoRepositoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnToken, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnWindows, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59))))
        );
        pnlPadreTipoRepositoriosLayout.setVerticalGroup(
            pnlPadreTipoRepositoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPadreTipoRepositoriosLayout.createSequentialGroup()
                .addComponent(pnlBarra3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnWindows, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnToken, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblDesplegando)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPadreTipoRepositorios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPadreTipoRepositorios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnWindowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWindowsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnWindowsActionPerformed

    private void btnArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArchivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArchivoActionPerformed

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        cerrarVentana();
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void lblCerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseEntered
        cambiarColor((javax.swing.JLabel) evt.getSource(), java.awt.Color.red, java.awt.Color.white);
    }//GEN-LAST:event_lblCerrarMouseEntered

    private void lblCerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseExited
        cambiarColor((javax.swing.JLabel) evt.getSource(), new java.awt.Color(255, 153, 153), java.awt.Color.white);
    }//GEN-LAST:event_lblCerrarMouseExited

    private void btnArchivoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnArchivoMouseEntered
        if (((javax.swing.JButton) evt.getSource()).isEnabled()) {
            cambiarColor((javax.swing.JButton) evt.getSource(), java.awt.Color.white, new Color(211, 111, 66));
        }
    }//GEN-LAST:event_btnArchivoMouseEntered

    private void btnTokenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTokenMouseEntered
        if (((javax.swing.JButton) evt.getSource()).isEnabled()) {
            cambiarColor((javax.swing.JButton) evt.getSource(), java.awt.Color.white, new Color(211, 111, 66));
        }
    }//GEN-LAST:event_btnTokenMouseEntered

    private void btnWindowsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWindowsMouseEntered
        if (((javax.swing.JButton) evt.getSource()).isEnabled()) {
            cambiarColor((javax.swing.JButton) evt.getSource(), java.awt.Color.white, new Color(211, 111, 66));
        }
    }//GEN-LAST:event_btnWindowsMouseEntered

    private void btnWindowsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnWindowsMouseExited
        if (((javax.swing.JButton) evt.getSource()).isEnabled()) {
            cambiarColor((javax.swing.JButton) evt.getSource(), new Color(211, 111, 66), java.awt.Color.white);
        }
    }//GEN-LAST:event_btnWindowsMouseExited

    private void btnTokenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTokenMouseExited
        if (((javax.swing.JButton) evt.getSource()).isEnabled()) {
            cambiarColor((javax.swing.JButton) evt.getSource(), new Color(211, 111, 66), java.awt.Color.white);
        }
    }//GEN-LAST:event_btnTokenMouseExited

    private void btnArchivoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnArchivoMouseExited
        if (((javax.swing.JButton) evt.getSource()).isEnabled()) {
            cambiarColor((javax.swing.JButton) evt.getSource(), new Color(211, 111, 66), java.awt.Color.white);
        }
    }//GEN-LAST:event_btnArchivoMouseExited

    private void btnLogoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoMouseEntered

    private void btnLogoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoMouseExited

    private void btnLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoActionPerformed

    private void pnlPadreTipoRepositoriosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pnlPadreTipoRepositoriosFocusGained

    }//GEN-LAST:event_pnlPadreTipoRepositoriosFocusGained

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void btnTokenActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTokenActionPerformed
    {//GEN-HEADEREND:event_btnTokenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTokenActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArchivo;
    public javax.swing.JButton btnLogo;
    private javax.swing.JButton btnToken;
    private javax.swing.JButton btnWindows;
    private javax.swing.ButtonGroup grupoRepositorio;
    private javax.swing.JLabel lblCerrar;
    public static javax.swing.JLabel lblDesplegando;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBarra3;
    private javax.swing.JPanel pnlPadreTipoRepositorios;
    // End of variables declaration//GEN-END:variables
}
