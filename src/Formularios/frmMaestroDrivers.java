/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import Global.CConstantes;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author usuario
 */
public final class frmMaestroDrivers extends javax.swing.JDialog {
    String v_valorConcatenado = "";
    
    public frmMaestroDrivers(java.awt.Frame parent, boolean modal) throws Exception {
        super(parent, modal);
        initComponents();
        m_limpiar_controles();
        listarDriversToken();
        setLocationRelativeTo(parent);
    }
    private boolean m_validar_ingreso_datos(){
            
        if(ctrl_txt_token_nombre.getText().trim().equals("")){
            CConstantes.dialogo("Ingrese nombre del driver");
            ctrl_txt_token_nombre.requestFocus();
            return false;
        }
        if(ctrl_txt_token_driver_ruta.getText().trim().equals("")){
            CConstantes.dialogo("Ingrese la ruta del driver");
            ctrl_txt_token_driver_ruta.requestFocus();
            return false;
        }
        return true;
    }
    private void m_limpiar_controles()
    {
        ctrl_btn_actualizar_registro.setEnabled(false);
        ctrl_btn_eliminar_registro.setEnabled(false);
        ctrl_btn_guardar_registro.setEnabled(true);
        ctrl_txt_token_nombre.setText("");
        ctrl_txt_token_driver_ruta.setText("");
        ctrl_btn_cancelar_registro.setVisible(false);
        ctrl_txt_token_nombre.requestFocus();
    }
    void listarDriversToken() throws Exception{
        String[] vg_fila_servicios_columna;
        if(frmConfigurador.configuracion == null)
            throw new Exception("ERROR !! : No se han cargado los valores del archivo configuracion.properties. el archivo no puede ser nulo");
        String v_bd_servidores_tsa=frmConfigurador.configuracion.getVg_cadena_matriz_token_drivers();
        String[] vg_fila_servicios; 
        vg_fila_servicios = v_bd_servidores_tsa.split(";");
        for (String vg_fila_servicio : vg_fila_servicios) {
            vg_fila_servicios_columna = vg_fila_servicio.split(",");
            m_agregar_registro_sello_tiempo(vg_fila_servicios_columna);
        }
    }
    
    DefaultTableModel m_agregar_registro_sello_tiempo(String[] vg_fila_servicios_columna){
        if(vg_fila_servicios_columna.length!=2){
            return null;
        }
        DefaultTableModel v_modelo = (DefaultTableModel)ctrl_dgv_lista_tokens_drivers.getModel();
        Object[] v_fila = new Object[2];
        v_fila[0] = vg_fila_servicios_columna[0].trim().equals("") ? " " : vg_fila_servicios_columna[0].trim();
        v_fila[1] = vg_fila_servicios_columna[1].trim().equals("") ? " " : vg_fila_servicios_columna[1].trim();
        v_modelo.addRow(v_fila);
        return v_modelo;
    }
    DefaultTableModel m_agregar_registro_sello_tiempo(String[] vg_fila_servicios_columna,int p_index){
        DefaultTableModel v_modelo = (DefaultTableModel)ctrl_dgv_lista_tokens_drivers.getModel();
        Object[] v_fila = new Object[2];
        v_fila[0] = vg_fila_servicios_columna[0].trim().equals("") ? " " : vg_fila_servicios_columna[0].trim();
        v_fila[1] = vg_fila_servicios_columna[1].trim().equals("") ? " " : vg_fila_servicios_columna[1].trim();
        v_modelo.removeRow(p_index);
        v_modelo.insertRow(p_index,v_fila);
        return v_modelo;
    }
    
    void m_eliminar_registro_servicio_sello_tiempo()
    {
        int index=ctrl_dgv_lista_tokens_drivers.getSelectedRow();
        if (this.ctrl_dgv_lista_tokens_drivers.getRowCount() == 0)
        {
            frmConfigurador.configuracion.setVg_ruta_libreria("");
            frmConfigurador.configuracion.setVg_ruta_libreria_nombre("");
            frmConfigurador.configuracion.setVg_cadena_matriz_token_drivers("");
            CConstantes.dialogo("No hay informacion para eliminar");
            m_limpiar_controles();
            return;
        }
        
        if (index < 0)
        {
            CConstantes.dialogo("Error: Seleccione un registro por favor");
            return;
        }
        DefaultTableModel dtm = (DefaultTableModel)this.ctrl_dgv_lista_tokens_drivers.getModel();
        dtm.removeRow(index);
        if (this.ctrl_dgv_lista_tokens_drivers.getRowCount() == 0)
        {
            frmConfigurador.configuracion.setVg_ruta_libreria_nombre("");
            frmConfigurador.configuracion.setVg_ruta_libreria("");
            frmConfigurador.configuracion.setVg_cadena_matriz_token_drivers("");
            m_limpiar_controles();
            return;
        }
        this.ctrl_dgv_lista_tokens_drivers.setModel(dtm);
        String v_cadena_registro = "";
        String v_cadena_matriz_servicios_sello_tiempo ;
        for (int i = 0; i < this.ctrl_dgv_lista_tokens_drivers.getRowCount(); i++)
        {
            if (this.ctrl_dgv_lista_tokens_drivers.getValueAt(i, 0) != null) {
                v_cadena_registro = v_cadena_registro + this.ctrl_dgv_lista_tokens_drivers.getValueAt(i, 0) + ",";
            } else {
                v_cadena_registro = v_cadena_registro + ",";
            }
            if (this.ctrl_dgv_lista_tokens_drivers.getValueAt(i, 1) != null) {
                v_cadena_registro = v_cadena_registro + this.ctrl_dgv_lista_tokens_drivers.getValueAt(i, 1) + ";";
            } else {
                v_cadena_registro = v_cadena_registro + ";";
            }
            if((frmConfigurador.configuracion.getVg_ruta_libreria()+";").equals(v_cadena_registro)){
                frmConfigurador.configuracion.setVg_ruta_libreria(v_cadena_registro); 
            }
        }
        v_cadena_matriz_servicios_sello_tiempo=v_cadena_registro.substring(0, v_cadena_registro.lastIndexOf(";"));
        frmConfigurador.configuracion.setVg_cadena_matriz_token_drivers(v_cadena_matriz_servicios_sello_tiempo);
        frmConfigurador.configuracion.setVg_tabla_token_drivers(ctrl_dgv_lista_tokens_drivers);
        m_limpiar_controles();
    }
    
    void m_grabar_servicio_sello_tiempo()
    {
        try {
            if(!m_validar_ingreso_datos()){
                return;
            }
            DefaultTableModel v_DefaultTableModel = (DefaultTableModel)this.ctrl_dgv_lista_tokens_drivers.getModel();
            ctrl_dgv_lista_tokens_drivers.setModel(v_DefaultTableModel);
            
            m_agregar_registro_sello_tiempo(new String[]{
                ctrl_txt_token_nombre.getText().trim(),
                ctrl_txt_token_driver_ruta.getText().trim()
            });
            String v_cadena_registro = "";
            String v_cadena_matriz_token_drivers ;
            for (int i = 0; i < ctrl_dgv_lista_tokens_drivers.getRowCount(); i++)
            {
                if (ctrl_dgv_lista_tokens_drivers.getValueAt(i, 0) != null) {
                    v_cadena_registro = v_cadena_registro + ctrl_dgv_lista_tokens_drivers.getValueAt(i, 0) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (ctrl_dgv_lista_tokens_drivers.getValueAt(i, 1) != null) {
                    v_cadena_registro = v_cadena_registro + ctrl_dgv_lista_tokens_drivers.getValueAt(i, 1) + ";";
                } else {
                    v_cadena_registro = v_cadena_registro + ";";
                }

                if((frmConfigurador.configuracion.getVg_ruta_libreria()+";").equals(v_cadena_registro)){
                    frmConfigurador.configuracion.setVg_ruta_libreria(v_cadena_registro);
                }
            }
            v_cadena_matriz_token_drivers=v_cadena_registro.substring(0, v_cadena_registro.lastIndexOf(";"));
            frmConfigurador.configuracion.setVg_cadena_matriz_token_drivers(v_cadena_matriz_token_drivers);
            m_limpiar_controles();
//        m_armar_tabla_servicios_sello_tiempo();
            frmConfigurador.configuracion.setVg_tabla_token_drivers(ctrl_dgv_lista_tokens_drivers);
        } catch (Exception ex) {
            CConstantes.mensajeln(ex.toString());
        }
         
    }
    
    void m_actualizar_servicio_sello_tiempo()
    {
        try {
            if(!m_validar_ingreso_datos()){
                return;
            }
            int index = ctrl_dgv_lista_tokens_drivers.getSelectedRow();
            DefaultTableModel v_DefaultTableModel = (DefaultTableModel)this.ctrl_dgv_lista_tokens_drivers.getModel();
            ctrl_dgv_lista_tokens_drivers.setModel(v_DefaultTableModel);
            m_agregar_registro_sello_tiempo(new String[]{
                ctrl_txt_token_nombre.getText().trim(),
                ctrl_txt_token_driver_ruta.getText().trim()
            },index);
            String v_cadena_registro = "";
            String v_cadena_matriz_token_drivers ;
            for (int i = 0; i < ctrl_dgv_lista_tokens_drivers.getRowCount(); i++)
            {
                if (ctrl_dgv_lista_tokens_drivers.getValueAt(i, 0) != null) {
                    v_cadena_registro = v_cadena_registro + ctrl_dgv_lista_tokens_drivers.getValueAt(i, 0) + ",";
                } else {
                    v_cadena_registro = v_cadena_registro + ",";
                }
                if (ctrl_dgv_lista_tokens_drivers.getValueAt(i, 1) != null) {
                    v_cadena_registro = v_cadena_registro + ctrl_dgv_lista_tokens_drivers.getValueAt(i, 1) + ";";
                } else {
                    v_cadena_registro = v_cadena_registro + ";";
                }
                if((frmConfigurador.configuracion.getVg_ruta_libreria()+";").equals(v_cadena_registro)){
                    frmConfigurador.configuracion.setVg_ruta_libreria(v_cadena_registro);
                }
            }
            v_cadena_matriz_token_drivers=v_cadena_registro.substring(0, v_cadena_registro.lastIndexOf(";"));
            frmConfigurador.configuracion.setVg_cadena_matriz_token_drivers(v_cadena_matriz_token_drivers);
            m_limpiar_controles();
            frmConfigurador.configuracion.setVg_tabla_token_drivers(ctrl_dgv_lista_tokens_drivers);
//        m_armar_tabla_servicios_sello_tiempo();
        } catch (Exception ex) {
            CConstantes.mensajeln(ex.toString());
        }
//        ctrl_dgv_lista_tokens_drivers.setEnabled(true);
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
        jPanel4 = new javax.swing.JPanel();
        lbletiquetapin = new javax.swing.JLabel();
        ctrl_txt_token_driver_ruta = new javax.swing.JTextField();
        lbletiquetapin1 = new javax.swing.JLabel();
        btnExaminar = new javax.swing.JButton();
        ctrl_txt_token_nombre = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ctrl_dgv_lista_tokens_drivers = new javax.swing.JTable();
        ctrl_btn_guardar_registro = new javax.swing.JButton();
        ctrl_btn_actualizar_registro = new javax.swing.JButton();
        ctrl_btn_eliminar_registro = new javax.swing.JButton();
        ctrl_btn_cancelar_registro = new javax.swing.JButton();
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

        jPanel4.setBackground(new java.awt.Color(241, 241, 241));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        lbletiquetapin.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin.setText("Nombre de libreria: ");

        ctrl_txt_token_driver_ruta.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        ctrl_txt_token_driver_ruta.setForeground(new java.awt.Color(211, 111, 66));
        ctrl_txt_token_driver_ruta.setToolTipText("Ruta del driver");
        ctrl_txt_token_driver_ruta.setMinimumSize(new java.awt.Dimension(6, 28));
        ctrl_txt_token_driver_ruta.setPreferredSize(new java.awt.Dimension(468, 28));

        lbletiquetapin1.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lbletiquetapin1.setForeground(new java.awt.Color(0, 83, 154));
        lbletiquetapin1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbletiquetapin1.setText("Ruta de libreria (.dll,.so): ");

        btnExaminar.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btnExaminar.setText("...");
        btnExaminar.setToolTipText("Examinar");
        btnExaminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExaminar.setFocusPainted(false);
        btnExaminar.setMaximumSize(new java.awt.Dimension(41, 28));
        btnExaminar.setMinimumSize(new java.awt.Dimension(41, 28));
        btnExaminar.setPreferredSize(new java.awt.Dimension(41, 28));
        btnExaminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExaminarActionPerformed(evt);
            }
        });

        ctrl_txt_token_nombre.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        ctrl_txt_token_nombre.setForeground(new java.awt.Color(211, 111, 66));
        ctrl_txt_token_nombre.setToolTipText("Nombre del driver");
        ctrl_txt_token_nombre.setMinimumSize(new java.awt.Dimension(6, 28));
        ctrl_txt_token_nombre.setPreferredSize(new java.awt.Dimension(468, 28));

        jPanel2.setBackground(new java.awt.Color(241, 241, 241));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Lista de librerías registrados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Light", 1, 11))); // NOI18N

        ctrl_dgv_lista_tokens_drivers.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ctrl_dgv_lista_tokens_drivers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre librería", "Ruta librería"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ctrl_dgv_lista_tokens_drivers.setFillsViewportHeight(true);
        ctrl_dgv_lista_tokens_drivers.setRowHeight(20);
        ctrl_dgv_lista_tokens_drivers.getTableHeader().setResizingAllowed(false);
        ctrl_dgv_lista_tokens_drivers.getTableHeader().setReorderingAllowed(false);
        ctrl_dgv_lista_tokens_drivers.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                ctrl_dgv_lista_tokens_driversHierarchyChanged(evt);
            }
        });
        ctrl_dgv_lista_tokens_drivers.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                ctrl_dgv_lista_tokens_driversAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        ctrl_dgv_lista_tokens_drivers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctrl_dgv_lista_tokens_driversMouseClicked(evt);
            }
        });
        ctrl_dgv_lista_tokens_drivers.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ctrl_dgv_lista_tokens_driversPropertyChange(evt);
            }
        });
        ctrl_dgv_lista_tokens_drivers.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ctrl_dgv_lista_tokens_driversKeyPressed(evt);
            }
        });
        ctrl_dgv_lista_tokens_drivers.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                ctrl_dgv_lista_tokens_driversVetoableChange(evt);
            }
        });
        jScrollPane1.setViewportView(ctrl_dgv_lista_tokens_drivers);
        if (ctrl_dgv_lista_tokens_drivers.getColumnModel().getColumnCount() > 0) {
            ctrl_dgv_lista_tokens_drivers.getColumnModel().getColumn(0).setResizable(false);
            ctrl_dgv_lista_tokens_drivers.getColumnModel().getColumn(0).setPreferredWidth(100);
            ctrl_dgv_lista_tokens_drivers.getColumnModel().getColumn(1).setResizable(false);
            ctrl_dgv_lista_tokens_drivers.getColumnModel().getColumn(1).setPreferredWidth(300);
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
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(ctrl_btn_cancelar_registro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                    .addContainerGap(49, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbletiquetapin)
                            .addComponent(lbletiquetapin1))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ctrl_txt_token_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctrl_txt_token_driver_ruta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExaminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbletiquetapin)
                    .addComponent(ctrl_txt_token_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbletiquetapin1)
                    .addComponent(ctrl_txt_token_driver_ruta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExaminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBarraTitulo1.setBackground(new java.awt.Color(204, 204, 204));
        pnlBarraTitulo1.setOpaque(false);

        lblMensajeValidacion1.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        lblMensajeValidacion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMensajeValidacion1.setText("Registro de librerías de dispositivos tokens (PKCS11)");
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
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(lblMensajeValidacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlBarraTitulo1Layout.setVerticalGroup(
            pnlBarraTitulo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBarraTitulo1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMensajeValidacion1)
                .addContainerGap())
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
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ctrl_btn_ventana_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlBarraTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCerrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ctrl_btn_ventana_logoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoMouseEntered

    private void ctrl_btn_ventana_logoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoMouseExited

    private void ctrl_btn_ventana_logoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_ventana_logoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_btn_ventana_logoActionPerformed

    private void ctrl_dgv_lista_tokens_driversMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_dgv_lista_tokens_driversMouseClicked
//        String dato=String.valueOf(ctrl_dgv_documentos.getValueAt(ctrl_dgv_documentos.getSelectedRow(),ctrl_dgv_documentos.getSelectedColumn()));
        if (ctrl_dgv_lista_tokens_drivers.getRowCount() > 0)
        {
            int index = ctrl_dgv_lista_tokens_drivers.getSelectedRow();

            if (index > -1)
            {
                if (!ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString().isEmpty()) {
                    v_valorConcatenado = ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString()+",";
                    ctrl_txt_token_nombre.setText(ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString());
                }
                if (!ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString().isEmpty()) {
                    v_valorConcatenado += ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString()+",";
                    ctrl_txt_token_driver_ruta.setText(ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString());
                }
                ctrl_btn_actualizar_registro.setEnabled(true);
                ctrl_btn_eliminar_registro.setEnabled(true);
                ctrl_btn_guardar_registro.setEnabled(false);
                ctrl_btn_cancelar_registro.setVisible(true);
//                ctrl_dgv_lista_tokens_drivers.setEnabled(false);
            }
        }
    }//GEN-LAST:event_ctrl_dgv_lista_tokens_driversMouseClicked

    private void ctrl_btn_guardar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_guardar_registroActionPerformed
        m_grabar_servicio_sello_tiempo();
    }//GEN-LAST:event_ctrl_btn_guardar_registroActionPerformed

    private void ctrl_btn_actualizar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_actualizar_registroActionPerformed
        m_actualizar_servicio_sello_tiempo();
    }//GEN-LAST:event_ctrl_btn_actualizar_registroActionPerformed

    private void ctrl_btn_eliminar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_eliminar_registroActionPerformed
        m_eliminar_registro_servicio_sello_tiempo();
    }//GEN-LAST:event_ctrl_btn_eliminar_registroActionPerformed

    private void ctrl_btn_cancelar_registroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_cancelar_registroActionPerformed
        m_limpiar_controles();
    }//GEN-LAST:event_ctrl_btn_cancelar_registroActionPerformed

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

    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExaminarActionPerformed
        String vi_ruta_temporal;
        if(!frmConfigurador.configuracion.getVg_ruta_libreria().equals("")){
            vi_ruta_temporal=frmConfigurador.configuracion.getVg_ruta_libreria();
        }
        else{
            vi_ruta_temporal=ctrl_txt_token_driver_ruta.getText().trim();
        }
        final JFileChooser elegirimagen = new JFileChooser(vi_ruta_temporal);
        elegirimagen.setFileSelectionMode(JFileChooser.FILES_ONLY);
        elegirimagen.setAcceptAllFileFilterUsed(false);
        
        elegirimagen.addChoosableFileFilter( new FileNameExtensionFilter("Librería de Token(*."+frmConfigurador.vcg_so.getVg_extension().substring(1,4)+")", frmConfigurador.vcg_so.getVg_extension().substring(1,4)));
        elegirimagen.setDialogTitle("Seleccione el driver para utilizar su token");
        // elegirimagen.setPreferredSize(new Dimension(getWidth(),getHeight()));
        elegirimagen.setMultiSelectionEnabled(true);
        int opcion=elegirimagen.showOpenDialog(this);
        if(opcion==JFileChooser.APPROVE_OPTION){
            ctrl_txt_token_driver_ruta.setText(elegirimagen.getSelectedFile().getAbsolutePath());
            frmConfigurador.configuracion.setVg_ruta_libreria_nombre(ctrl_txt_token_nombre.getText());
            frmConfigurador.configuracion.setVg_ruta_libreria(ctrl_txt_token_driver_ruta.getText());
//            frmConfigurador.configuracion.setVg_firma_visible_ruta_image_marca_de_agua(ctrl_txt_token_driver_ruta.getText());
            //grabarPropiedad(Constantes.FILIMAGEN,txtrutaImagen.getText());
        }
    }//GEN-LAST:event_btnExaminarActionPerformed

    private void ctrl_dgv_lista_tokens_driversAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_ctrl_dgv_lista_tokens_driversAncestorMoved
          if (ctrl_dgv_lista_tokens_drivers.getRowCount() > 0)
        {
            int index = ctrl_dgv_lista_tokens_drivers.getSelectedRow();

            if (index > -1)
            {
                if (!ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString().isEmpty()) {
                    v_valorConcatenado = ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString()+",";
                    ctrl_txt_token_nombre.setText(ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString());
                }
                if (!ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString().isEmpty()) {
                    v_valorConcatenado += ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString()+",";
                    ctrl_txt_token_driver_ruta.setText(ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString());
                }
                ctrl_btn_actualizar_registro.setEnabled(true);
                ctrl_btn_eliminar_registro.setEnabled(true);
                ctrl_btn_guardar_registro.setEnabled(false);
                ctrl_btn_cancelar_registro.setVisible(true);
//                ctrl_dgv_lista_tokens_drivers.setEnabled(false);
            }
        }
    }//GEN-LAST:event_ctrl_dgv_lista_tokens_driversAncestorMoved

    private void ctrl_dgv_lista_tokens_driversPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ctrl_dgv_lista_tokens_driversPropertyChange
        
    }//GEN-LAST:event_ctrl_dgv_lista_tokens_driversPropertyChange

    private void ctrl_dgv_lista_tokens_driversHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_ctrl_dgv_lista_tokens_driversHierarchyChanged
        
    }//GEN-LAST:event_ctrl_dgv_lista_tokens_driversHierarchyChanged

    private void ctrl_dgv_lista_tokens_driversVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_ctrl_dgv_lista_tokens_driversVetoableChange
        
    }//GEN-LAST:event_ctrl_dgv_lista_tokens_driversVetoableChange

    private void ctrl_dgv_lista_tokens_driversKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctrl_dgv_lista_tokens_driversKeyPressed
        char caracter=evt.getKeyChar();
        if(caracter==KeyEvent.VK_DOWN || caracter==KeyEvent.VK_UP){
             if (ctrl_dgv_lista_tokens_drivers.getRowCount() > 0)
        {
            int index = ctrl_dgv_lista_tokens_drivers.getSelectedRow();
            if (index > -1)
            {
                if (!ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString().isEmpty()) {
                    v_valorConcatenado = ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString()+",";
                    ctrl_txt_token_nombre.setText(ctrl_dgv_lista_tokens_drivers.getValueAt(index, 0).toString());
                }
                if (!ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString().isEmpty()) {
                    v_valorConcatenado += ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString()+",";
                    ctrl_txt_token_driver_ruta.setText(ctrl_dgv_lista_tokens_drivers.getValueAt(index, 1).toString());
                }
                ctrl_btn_actualizar_registro.setEnabled(true);
                ctrl_btn_eliminar_registro.setEnabled(true);
                ctrl_btn_guardar_registro.setEnabled(false);
                ctrl_btn_cancelar_registro.setVisible(true);
//                ctrl_dgv_lista_tokens_drivers.setEnabled(false);
            }
        }
        }
       
    }//GEN-LAST:event_ctrl_dgv_lista_tokens_driversKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExaminar;
    private javax.swing.JButton ctrl_btn_actualizar_registro;
    private javax.swing.JButton ctrl_btn_cancelar_registro;
    private javax.swing.JButton ctrl_btn_eliminar_registro;
    private javax.swing.JButton ctrl_btn_guardar_registro;
    public javax.swing.JButton ctrl_btn_ventana_logo;
    private javax.swing.JTable ctrl_dgv_lista_tokens_drivers;
    protected static javax.swing.JTextField ctrl_txt_token_driver_ruta;
    protected static javax.swing.JTextField ctrl_txt_token_nombre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCerrar1;
    public static javax.swing.JLabel lblMensajeValidacion1;
    private javax.swing.JLabel lbletiquetapin;
    private javax.swing.JLabel lbletiquetapin1;
    private javax.swing.JPanel pnlBarraTitulo1;
    // End of variables declaration//GEN-END:variables
}
