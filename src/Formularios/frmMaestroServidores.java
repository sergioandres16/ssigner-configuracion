/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import Global.CConstantes;
import Metodos.helperClass;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author usuario
 */
public final class frmMaestroServidores extends javax.swing.JDialog {

    helperClass helper_class = new helperClass();

    private boolean m_validar_ingreso_datos() {

        if (txtTsaNombre.getText().trim().equals("")) {
            CConstantes.dialogo("Ingrese nombre del servicio");
            txtTsaNombre.requestFocus();
            return false;
        }
        if (txtTsaUrl.getText().trim().equals("")) {
            CConstantes.dialogo("Ingrese la url del servicio");
            txtTsaUrl.requestFocus();
            return false;
        }
        return true;
    }

    private void m_limpiar_controles() {
        ctrl_btn_actualizar_registro.setEnabled(false);
        ctrl_btn_eliminar_registro.setEnabled(false);
        ctrl_btn_guardar_registro.setEnabled(true);
        txtTsaNombre.setText("");
        txtTsaUrl.setText("");
        txtTsaUsuario.setText("");
        txtTsaClave.setText("");
        ctrl_btn_cancelar_registro.setVisible(false);
        txtTsaNombre.requestFocus();
    }

    public frmMaestroServidores(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        m_limpiar_controles();
        dgvServidoresTsa.getColumnModel().getColumn(3).setPreferredWidth(0);
        m_armar_tabla_servicios_sello_tiempo();
        setLocationRelativeTo(parent);
    }

    void m_armar_tabla_servicios_sello_tiempo() {
        String cadenaServicios = frmConfigurador.configuracion.getVg_cadena_matriz_servicios_sello_tiempo();
        if (cadenaServicios != null) {
            String[] servicios = cadenaServicios.split(";");
            for (String servicio : servicios) {
                String[] servicioDatos = servicio.split(",");
                m_agregar_registro_sello_tiempo(servicioDatos);
            }
        }

    }

    DefaultTableModel m_agregar_registro_sello_tiempo(String[] servicioDatos) {
        if (servicioDatos.length == 0) {
            return null;
        }
        if (servicioDatos.length == 1) {
            return null;
        }
        if (servicioDatos.length == 2) {
            servicioDatos = new String[]{
                servicioDatos[0],
                servicioDatos[1],
                "",
                ""
            };
        }
        if (servicioDatos.length == 3) {
            servicioDatos = new String[]{
                servicioDatos[0],
                servicioDatos[1],
                servicioDatos[2],
                ""
            };
        }
        DefaultTableModel modeloTsa = (DefaultTableModel) dgvServidoresTsa.getModel();
        Object[] registro = new Object[]{
            servicioDatos[0],
            servicioDatos[1],
            servicioDatos[2],
            servicioDatos[3]
        };
        modeloTsa.addRow(registro);
        return modeloTsa;
    }

    DefaultTableModel m_agregar_registro_sello_tiempo(String[] vg_fila_servicios_columna, int p_index) {
        DefaultTableModel v_modelo = (DefaultTableModel) dgvServidoresTsa.getModel();
        Object[] v_fila = new Object[4];
        v_fila[0] = vg_fila_servicios_columna[0].trim().equals("") ? "" : vg_fila_servicios_columna[0].trim();
        v_fila[1] = vg_fila_servicios_columna[1].trim().equals("") ? "" : vg_fila_servicios_columna[1].trim();
        v_fila[2] = vg_fila_servicios_columna[2].trim().equals("") ? "" : vg_fila_servicios_columna[2].trim();
        v_fila[3] = vg_fila_servicios_columna[3].trim().equals("") ? "" : vg_fila_servicios_columna[3].trim();
        v_modelo.removeRow(p_index);
        v_modelo.insertRow(p_index, v_fila);
        return v_modelo;
    }

    void m_eliminar_registro_servicio_sello_tiempo() {
        int index = dgvServidoresTsa.getSelectedRow();
        if (this.dgvServidoresTsa.getRowCount() == 0) {
            CConstantes.dialogo("No hay información para eliminar");
            return;
        }

        if (index < 0) {
            CConstantes.dialogo("Error: Seleccione un registro por favor");
            return;
        }
        DefaultTableModel dtm = (DefaultTableModel) this.dgvServidoresTsa.getModel();
        dtm.removeRow(index);
        if (this.dgvServidoresTsa.getRowCount() == 0) {
            frmConfigurador.configuracion.setVg_tsa_seleccionado("");
            frmConfigurador.configuracion.setVg_cadena_matriz_servicios_sello_tiempo("");
            frmConfigurador.configuracion.setVg_tsa_url("");
            frmConfigurador.configuracion.setVg_tsa_usuario("");
            frmConfigurador.configuracion.setVg_tsa_clave("");
            m_limpiar_controles();
            return;
        }
        this.dgvServidoresTsa.setModel(dtm);
        String v_cadena_registro = "";
        String v_cadena_matriz_servicios_sello_tiempo;
        for (int i = 0; i < this.dgvServidoresTsa.getRowCount(); i++) {
            if (this.dgvServidoresTsa.getValueAt(i, 0) != null) {
                v_cadena_registro = v_cadena_registro + this.dgvServidoresTsa.getValueAt(i, 0) + ",";
            } else {
                v_cadena_registro = v_cadena_registro + ",";
            }
            if (this.dgvServidoresTsa.getValueAt(i, 1) != null) {
                v_cadena_registro = v_cadena_registro + this.dgvServidoresTsa.getValueAt(i, 1) + ",";
            } else {
                v_cadena_registro = v_cadena_registro + ",";
            }
            if (this.dgvServidoresTsa.getValueAt(i, 2) != null) {
                v_cadena_registro = v_cadena_registro + this.dgvServidoresTsa.getValueAt(i, 2) + ",";
            } else {
                v_cadena_registro = v_cadena_registro + ",";
            }
            if (this.dgvServidoresTsa.getValueAt(i, 3) != null) {
                v_cadena_registro = v_cadena_registro + this.dgvServidoresTsa.getValueAt(i, 3) + ";";
            } else {
                v_cadena_registro = v_cadena_registro + ";";
            }
            if ((frmConfigurador.configuracion.getVg_tsa_seleccionado() + ";").equals(v_cadena_registro)) {
                frmConfigurador.configuracion.setVg_tsa_seleccionado(v_cadena_registro);
            }
        }
        v_cadena_matriz_servicios_sello_tiempo = v_cadena_registro.substring(0, v_cadena_registro.lastIndexOf(";"));
        frmConfigurador.configuracion.setVg_cadena_matriz_servicios_sello_tiempo(v_cadena_matriz_servicios_sello_tiempo);
        frmConfigurador.configuracion.setVg_tabla_servicios_sello_tiempo(dgvServidoresTsa);
        m_limpiar_controles();
    }

    void m_grabar_servicio_sello_tiempo() {
        try {
            if (!m_validar_ingreso_datos()) {
                return;
            }
            DefaultTableModel v_DefaultTableModel = (DefaultTableModel) this.dgvServidoresTsa.getModel();
            dgvServidoresTsa.setModel(v_DefaultTableModel);

            m_agregar_registro_sello_tiempo(new String[]{
                txtTsaNombre.getText().trim(),
                txtTsaUrl.getText().trim(),
                txtTsaUsuario.getText().trim(),
                helper_class.encrypt(new String(txtTsaClave.getPassword()).trim())
            });
            String v_cadena_registro = "";
            String v_cadena_matriz_servicios_sello_tiempo;
            for (int i = 0; i < dgvServidoresTsa.getRowCount(); i++) {
                if (dgvServidoresTsa.getValueAt(i, 0) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 0) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (dgvServidoresTsa.getValueAt(i, 1) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 1) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (dgvServidoresTsa.getValueAt(i, 2) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 2) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (dgvServidoresTsa.getValueAt(i, 3) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 3) + ";";
                } else {
                    v_cadena_registro = v_cadena_registro + ";";
                }
                if ((frmConfigurador.configuracion.getVg_tsa_seleccionado() + ";").equals(v_cadena_registro)) {
                    frmConfigurador.configuracion.setVg_tsa_seleccionado(v_cadena_registro);
                }
            }
            v_cadena_matriz_servicios_sello_tiempo = v_cadena_registro.substring(0, v_cadena_registro.lastIndexOf(";"));
            frmConfigurador.configuracion.setVg_cadena_matriz_servicios_sello_tiempo(v_cadena_matriz_servicios_sello_tiempo);
            m_limpiar_controles();
//        m_armar_tabla_servicios_sello_tiempo();
            frmConfigurador.configuracion.setVg_tabla_servicios_sello_tiempo(dgvServidoresTsa);
        } catch (Exception ex) {
            Logger.getLogger(frmMaestroServidores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void m_actualizar_servicio_sello_tiempo() {
        try {
            if (!m_validar_ingreso_datos()) {
                return;
            }
            int index = dgvServidoresTsa.getSelectedRow();
            DefaultTableModel v_DefaultTableModel = (DefaultTableModel) this.dgvServidoresTsa.getModel();
            dgvServidoresTsa.setModel(v_DefaultTableModel);
            m_agregar_registro_sello_tiempo(new String[]{
                txtTsaNombre.getText().trim(),
                txtTsaUrl.getText().trim(),
                txtTsaUsuario.getText().trim(),
                helper_class.encrypt(new String(txtTsaClave.getPassword()).trim())
            }, index);
            String v_cadena_registro = "";
            String v_cadena_matriz_servicios_sello_tiempo;
            for (int i = 0; i < dgvServidoresTsa.getRowCount(); i++) {
                if (dgvServidoresTsa.getValueAt(i, 0) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 0) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (dgvServidoresTsa.getValueAt(i, 1) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 1) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (dgvServidoresTsa.getValueAt(i, 2) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 2) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (dgvServidoresTsa.getValueAt(i, 3) != null) {
                    v_cadena_registro = v_cadena_registro + dgvServidoresTsa.getValueAt(i, 3) + ";";
                } else {
                    v_cadena_registro = v_cadena_registro + ";";
                }
                if ((v_cadena_registro).contains(frmConfigurador.configuracion.getVg_tsa_seleccionado())) {
                    frmConfigurador.configuracion.setVg_tsa_seleccionado(v_cadena_registro);
                }
            }
            v_cadena_matriz_servicios_sello_tiempo = v_cadena_registro.substring(0, v_cadena_registro.lastIndexOf(";"));
            frmConfigurador.configuracion.setVg_cadena_matriz_servicios_sello_tiempo(v_cadena_matriz_servicios_sello_tiempo);
            m_limpiar_controles();
            frmConfigurador.configuracion.setVg_tabla_servicios_sello_tiempo(dgvServidoresTsa);
//        m_armar_tabla_servicios_sello_tiempo();
        } catch (Exception ex) {
            Logger.getLogger(frmMaestroServidores.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel3 = new javax.swing.JPanel();
        ctrl_btn_ventana_logo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbletiquetapin = new javax.swing.JLabel();
        lbletiquetapin1 = new javax.swing.JLabel();
        lbletiquetapin2 = new javax.swing.JLabel();
        lbletiquetapin3 = new javax.swing.JLabel();
        txtTsaClave = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dgvServidoresTsa = new javax.swing.JTable();
        ctrl_btn_guardar_registro = new javax.swing.JButton();
        ctrl_btn_actualizar_registro = new javax.swing.JButton();
        ctrl_btn_eliminar_registro = new javax.swing.JButton();
        ctrl_btn_cancelar_registro = new javax.swing.JButton();
        txtTsaNombre = new javax.swing.JTextField();
        txtTsaUrl = new javax.swing.JTextField();
        txtTsaUsuario = new javax.swing.JTextField();
        pnlBarraTitulo1 = new javax.swing.JPanel();
        lblMensajeValidacion1 = new javax.swing.JLabel();
        lblCerrar1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(241, 241, 241));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        ctrl_btn_ventana_logo.setPreferredSize(new java.awt.Dimension(55, 25));
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

        jPanel1.setBackground(new java.awt.Color(241, 241, 241));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lbletiquetapin.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin.setText("Nombre de Servicio de Sello de Tiempo:");

        lbletiquetapin1.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin1.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin1.setText("URL del Servicio de Sello de Tiempo:");

        lbletiquetapin2.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin2.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin2.setText("Usuario:");

        lbletiquetapin3.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin3.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin3.setText("Clave :");

        txtTsaClave.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaClave.setForeground(new java.awt.Color(211, 111, 66));
        txtTsaClave.setToolTipText("Clave del Servicio de Sello de Tiempo");
        txtTsaClave.setPreferredSize(new java.awt.Dimension(190, 28));
        txtTsaClave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTsaClaveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTsaClaveFocusLost(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(241, 241, 241));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Lista de Autoridades de Servicio de Sello de Tiempo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 1, 11))); // NOI18N

        dgvServidoresTsa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dgvServidoresTsa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "URL Servicio", "Usuario", "Clave"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dgvServidoresTsa.setFillsViewportHeight(true);
        dgvServidoresTsa.setRowHeight(20);
        dgvServidoresTsa.getTableHeader().setResizingAllowed(false);
        dgvServidoresTsa.getTableHeader().setReorderingAllowed(false);
        dgvServidoresTsa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dgvServidoresTsaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dgvServidoresTsa);
        if (dgvServidoresTsa.getColumnModel().getColumnCount() > 0) {
            dgvServidoresTsa.getColumnModel().getColumn(0).setResizable(false);
            dgvServidoresTsa.getColumnModel().getColumn(0).setPreferredWidth(100);
            dgvServidoresTsa.getColumnModel().getColumn(1).setResizable(false);
            dgvServidoresTsa.getColumnModel().getColumn(1).setPreferredWidth(400);
            dgvServidoresTsa.getColumnModel().getColumn(2).setResizable(false);
            dgvServidoresTsa.getColumnModel().getColumn(2).setPreferredWidth(130);
            dgvServidoresTsa.getColumnModel().getColumn(3).setResizable(false);
            dgvServidoresTsa.getColumnModel().getColumn(3).setPreferredWidth(0);
        }

        ctrl_btn_guardar_registro.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        ctrl_btn_guardar_registro.setText("Guardar registro");
        ctrl_btn_guardar_registro.setPreferredSize(new java.awt.Dimension(183, 42));
        ctrl_btn_guardar_registro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_guardar_registroActionPerformed(evt);
            }
        });

        ctrl_btn_actualizar_registro.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        ctrl_btn_actualizar_registro.setText("Actualizar registro");
        ctrl_btn_actualizar_registro.setEnabled(false);
        ctrl_btn_actualizar_registro.setPreferredSize(new java.awt.Dimension(183, 42));
        ctrl_btn_actualizar_registro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_actualizar_registroActionPerformed(evt);
            }
        });

        ctrl_btn_eliminar_registro.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        ctrl_btn_eliminar_registro.setText("Eliminar registro");
        ctrl_btn_eliminar_registro.setEnabled(false);
        ctrl_btn_eliminar_registro.setPreferredSize(new java.awt.Dimension(183, 42));
        ctrl_btn_eliminar_registro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_eliminar_registroActionPerformed(evt);
            }
        });

        ctrl_btn_cancelar_registro.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        ctrl_btn_cancelar_registro.setText("Cancelar operación");
        ctrl_btn_cancelar_registro.setPreferredSize(new java.awt.Dimension(183, 42));
        ctrl_btn_cancelar_registro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_cancelar_registroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ctrl_btn_cancelar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ctrl_btn_guardar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ctrl_btn_actualizar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ctrl_btn_eliminar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(228, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctrl_btn_guardar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_btn_actualizar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_btn_eliminar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_btn_cancelar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(39, Short.MAX_VALUE)))
        );

        txtTsaNombre.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaNombre.setForeground(new java.awt.Color(211, 111, 66));
        txtTsaNombre.setToolTipText("URL de servicio de Sello de Tiempo");
        txtTsaNombre.setPreferredSize(new java.awt.Dimension(361, 28));

        txtTsaUrl.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaUrl.setForeground(new java.awt.Color(211, 111, 66));
        txtTsaUrl.setToolTipText("URL de servicio de Sello de Tiempo");
        txtTsaUrl.setMinimumSize(new java.awt.Dimension(361, 28));
        txtTsaUrl.setPreferredSize(new java.awt.Dimension(381, 28));

        txtTsaUsuario.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtTsaUsuario.setForeground(new java.awt.Color(211, 111, 66));
        txtTsaUsuario.setToolTipText("URL de servicio de Sello de Tiempo");
        txtTsaUsuario.setPreferredSize(new java.awt.Dimension(170, 28));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbletiquetapin, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                    .addComponent(lbletiquetapin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTsaNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTsaUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbletiquetapin2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTsaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbletiquetapin3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTsaClave, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbletiquetapin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTsaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbletiquetapin1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTsaUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbletiquetapin2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbletiquetapin3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTsaClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTsaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBarraTitulo1.setOpaque(false);

        lblMensajeValidacion1.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        lblMensajeValidacion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensajeValidacion1.setText("Registro de Autoridades de Sello de Tiempo (TSA)");
        lblMensajeValidacion1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblMensajeValidacion1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlBarraTitulo1Layout = new javax.swing.GroupLayout(pnlBarraTitulo1);
        pnlBarraTitulo1.setLayout(pnlBarraTitulo1Layout);
        pnlBarraTitulo1Layout.setHorizontalGroup(
            pnlBarraTitulo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarraTitulo1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMensajeValidacion1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE))
        );
        pnlBarraTitulo1Layout.setVerticalGroup(
            pnlBarraTitulo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMensajeValidacion1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        lblCerrar1.setBackground(new java.awt.Color(255, 153, 153));
        lblCerrar1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        lblCerrar1.setForeground(new java.awt.Color(255, 255, 255));
        lblCerrar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar1.setText("x");
        lblCerrar1.setToolTipText("Cerrar");
        lblCerrar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCerrar1.setOpaque(true);
        lblCerrar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrar1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCerrar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCerrar1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ctrl_btn_ventana_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarraTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(lblCerrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBarraTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(ctrl_btn_ventana_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblCerrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTsaClaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaClaveFocusGained
//        focodentro((javax.swing.JTextField)evt.getSource());
    }//GEN-LAST:event_txtTsaClaveFocusGained

    private void txtTsaClaveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTsaClaveFocusLost
//        focofuera((javax.swing.JTextField)evt.getSource());
    }//GEN-LAST:event_txtTsaClaveFocusLost

    private void lblMensajeValidacion1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblMensajeValidacion1KeyPressed
//        formKeyPressed(evt);
    }//GEN-LAST:event_lblMensajeValidacion1KeyPressed

    private void lblCerrar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar1MouseClicked
        //Repositorio.cerrarventana=0;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_lblCerrar1MouseClicked

    private void lblCerrar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar1MouseEntered
        //  cambiarColor((javax.swing.JLabel)evt.getSource(),Color.red,Color.white);
    }//GEN-LAST:event_lblCerrar1MouseEntered

    private void lblCerrar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrar1MouseExited
        // cambiarColor((javax.swing.JLabel)evt.getSource(),new Color(255,153,153),Color.white);
    }//GEN-LAST:event_lblCerrar1MouseExited

    private void ctrl_btn_ventana_logoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoMouseEntered

    private void ctrl_btn_ventana_logoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoMouseExited

    private void ctrl_btn_ventana_logoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoActionPerformed
    String v_valorConcatenado = "";
    private void dgvServidoresTsaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dgvServidoresTsaMouseClicked
        if (dgvServidoresTsa.getRowCount() > 0) {
            int index = dgvServidoresTsa.getSelectedRow();

            if (index > -1) {
                if (!dgvServidoresTsa.getValueAt(index, 0).toString().isEmpty()) {
                    v_valorConcatenado = dgvServidoresTsa.getValueAt(index, 0).toString() + ",";
                    txtTsaNombre.setText(dgvServidoresTsa.getValueAt(index, 0).toString());
                }
                if (!dgvServidoresTsa.getValueAt(index, 1).toString().isEmpty()) {
                    v_valorConcatenado += dgvServidoresTsa.getValueAt(index, 1).toString() + ",";
                    txtTsaUrl.setText(dgvServidoresTsa.getValueAt(index, 1).toString());
                }
                if (!dgvServidoresTsa.getValueAt(index, 2).toString().isEmpty()) {
                    v_valorConcatenado += dgvServidoresTsa.getValueAt(index, 2).toString() + ",";
                    txtTsaUsuario.setText(dgvServidoresTsa.getValueAt(index, 2).toString());

                }
                if (!dgvServidoresTsa.getValueAt(index, 3).toString().isEmpty()) {
                    try {
                        v_valorConcatenado += dgvServidoresTsa.getValueAt(index, 3).toString();
                        txtTsaClave.setText(helper_class.decrypt(dgvServidoresTsa.getValueAt(index, 3).toString()));
                    } catch (Exception ex) {
                        Logger.getLogger(frmMaestroServidores.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                ctrl_btn_actualizar_registro.setEnabled(true);
                ctrl_btn_eliminar_registro.setEnabled(true);
                ctrl_btn_guardar_registro.setEnabled(false);
                ctrl_btn_cancelar_registro.setVisible(true);
            }
        }
    }//GEN-LAST:event_dgvServidoresTsaMouseClicked

    private void ctrl_btn_cancelar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_cancelar_registroActionPerformed
        m_limpiar_controles();
    }//GEN-LAST:event_ctrl_btn_cancelar_registroActionPerformed

    private void ctrl_btn_guardar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_guardar_registroActionPerformed
        m_grabar_servicio_sello_tiempo();
    }//GEN-LAST:event_ctrl_btn_guardar_registroActionPerformed

    private void ctrl_btn_actualizar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_actualizar_registroActionPerformed
        m_actualizar_servicio_sello_tiempo();
    }//GEN-LAST:event_ctrl_btn_actualizar_registroActionPerformed

    private void ctrl_btn_eliminar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_eliminar_registroActionPerformed
        m_eliminar_registro_servicio_sello_tiempo();
    }//GEN-LAST:event_ctrl_btn_eliminar_registroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ctrl_btn_actualizar_registro;
    private javax.swing.JButton ctrl_btn_cancelar_registro;
    private javax.swing.JButton ctrl_btn_eliminar_registro;
    private javax.swing.JButton ctrl_btn_guardar_registro;
    public javax.swing.JButton ctrl_btn_ventana_logo;
    private javax.swing.JTable dgvServidoresTsa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCerrar1;
    public static javax.swing.JLabel lblMensajeValidacion1;
    private javax.swing.JLabel lbletiquetapin;
    private javax.swing.JLabel lbletiquetapin1;
    private javax.swing.JLabel lbletiquetapin2;
    private javax.swing.JLabel lbletiquetapin3;
    private javax.swing.JPanel pnlBarraTitulo1;
    protected static javax.swing.JPasswordField txtTsaClave;
    protected static javax.swing.JTextField txtTsaNombre;
    protected static javax.swing.JTextField txtTsaUrl;
    protected static javax.swing.JTextField txtTsaUsuario;
    // End of variables declaration//GEN-END:variables
}
