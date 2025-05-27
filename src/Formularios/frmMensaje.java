/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import Global.CConstantes;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author usuario
 */
public class frmMensaje extends javax.swing.JDialog {
    /*Constantes de opciones*/
    public static final int NO=1;
    public static final int SI=2;
    public static final int ACEPTAR=3;
    public static final int CANCELAR=4;
    public static final int CERRAR=5;
    /*Constantes de icono de cuerpo de la ventana*/
    public static final int ICONO_INFORMACION=0;
    public static final int ICONO_BIEN=1;
    public static final int ICONO_ERROR=2;
    public static final int ICONO_PREGUNTA=3;
    public static final int ICONO_ADVERTENCIA=4;
    public static final int ICONO_ESPERA=5;
    
    public static final int VENTANA_ICONO_DEFECTO=0;
    
    /*Constantes de titulo de la ventana*/
    public static final String TITULO=CConstantes.APLICACION_NOMBRE;
    
    /*Variable global privada para obtner la respuesta de los botones*/
    private static  int vg_valor_enviar;
    
    public static int mensaje_mostrar(java.awt.Dialog parent,String p_mensaje){
        frmMensaje frm = new frmMensaje(parent,true,p_mensaje,TITULO);
        formulario_mostrar(frm);
        return frm.vg_valor_enviar;
    }
    
    public static int mensaje_mostrar(java.awt.Frame parent,String p_mensaje){
        frmMensaje frm = new frmMensaje(parent,true,p_mensaje,TITULO);
        formulario_mostrar(frm);
        return frm.vg_valor_enviar;
    }
    
    public static int mensaje_mostrar(java.awt.Dialog parent,String p_mensaje,String p_titulo){
        frmMensaje frm = new frmMensaje(parent,true,p_mensaje,p_titulo);
        formulario_mostrar(frm);
        return frm.vg_valor_enviar;
    }
    
    public static int mensaje_mostrar(java.awt.Frame parent,String p_mensaje,String p_titulo){
        frmMensaje frm = new frmMensaje(parent,true,p_mensaje,p_titulo);
        formulario_mostrar(frm);
        return frm.vg_valor_enviar;
    }
    
    public static int mensaje_pregunta_mostrar(java.awt.Dialog parent,String p_mensaje){
        frmMensaje frm = new frmMensaje(parent,true,p_mensaje,TITULO);
        formulario_mostrar(frm);
        return frm.vg_valor_enviar;
    }
    
    public static int mensaje_pregunta_mostrar(java.awt.Frame parent,String p_mensaje){
        frmMensaje frm = new frmMensaje(parent,true,p_mensaje,TITULO);
        formulario_mostrar(frm);
        return frm.vg_valor_enviar;
    }
    
    public frmMensaje(java.awt.Frame parent, boolean modal,String p_mensaje,String p_titulo) {
        super(parent, modal);
        mensaje_setear(p_mensaje,p_titulo);
        setLocationRelativeTo(parent);
    }
    
    public frmMensaje(java.awt.Dialog parent, boolean modal,String p_mensaje,String p_titulo) {
        super(parent, modal);
        mensaje_setear(p_mensaje,p_titulo);
        btn_vtn_si.setVisible(false);
        btn_vtn_no.setVisible(false);
        btn_vtn_can.setVisible(false);
        setLocationRelativeTo(parent);
    }
    
    private void mensaje_setear(String p_mensaje,String p_titulo){
        initComponents();
        inicializarControles(ICONO_ERROR,p_mensaje,p_titulo);
    }
    
    private static void inicializarControles(int p_cuerpo_icono,String p_mensaje,String p_titulo){
        try {
            txt_msj_crp.setText(p_mensaje);
            txt_vtn_tit.setText(p_titulo);
            File f = new File(".");
            String laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
            String icon_ruta ;
            /*Establece que icono voy a mostrar*/
            if(p_cuerpo_icono==ICONO_INFORMACION){
                icon_ruta =laRuta + "icono_info.png";
            }
            else if(p_cuerpo_icono==ICONO_BIEN){
                icon_ruta =laRuta + "icono_ok.png";
            }
            else if(p_cuerpo_icono==ICONO_ERROR){
                icon_ruta =laRuta + "icono_error_2.png";
            }
            else if(p_cuerpo_icono==ICONO_PREGUNTA){
                icon_ruta =laRuta + "icono_pregunta.png";
            }
            else if(p_cuerpo_icono==ICONO_ADVERTENCIA){
                icon_ruta =laRuta + "icono_advertencia.png";
            }
            else if(p_cuerpo_icono==ICONO_ESPERA){
                icon_ruta =laRuta + "icono_espera.png";
            }
            else{
                icon_ruta =laRuta + "icono_info.png";
            }
            ImageIcon img = new ImageIcon(icon_ruta);
            lbl_msj_ico.setIcon(img);
//            laRuta = f.getCanonicalPath() + File.separator + "imagenes" + File.separator;
//            icon_ruta =laRuta + "icono_defecto.png";
//            if(p_cabecera_icono==VENTANA_ICONO_DEFECTO){
//                icon_ruta =laRuta + "icono_defecto.png";
//            }
//            img = new ImageIcon(icon_ruta);
            txt_vtn_ico.setIcon(null);
        } catch (IOException ex) {
            Logger.getLogger(frmMensaje.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void formulario_mostrar(frmMensaje frm){
        btn_vtn_si.setVisible(false);
        btn_vtn_no.setVisible(false);
        btn_vtn_can.setVisible(false);
        frm.setVisible(true);
    }
   
    protected void cambiarColor(Object obj,Color colorfondo,Color colorletra){
        if(obj instanceof javax.swing.JLabel){
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        }
        else if(obj instanceof javax.swing.JButton){
            ((javax.swing.JButton) obj).setBackground(colorfondo);
            ((javax.swing.JButton) obj).setForeground(colorletra);
            ((javax.swing.JButton) obj).setOpaque(true);
            ((javax.swing.JButton) obj).setBorder(javax.swing.BorderFactory.createLineBorder(colorletra));
        } 
    }
    
//    private void grupo_recorrer(javax.swing.ButtonGroup p_btn_gpr){
//        java.util.Enumeration<javax.swing.AbstractButton> enu_abs_btn=p_btn_gpr.getElements();
//        while (enu_abs_btn.hasMoreElements()) {
//            Object v_btn = enu_abs_btn.nextElement();
//            if(v_btn==btn_vtn_ace){
//                color_asignar(Color.red,btn_vtn_ace);
//            }
//            else if(v_btn==btn_vtn_can){
//                color_asignar(Color.yellow,btn_vtn_can);
//            }
//            else if(v_btn==btn_vtn_si){
//                color_asignar(Color.gray,btn_vtn_si);
//            }
//            else if(v_btn==btn_vtn_no){
//                color_asignar(Color.blue,btn_vtn_no);
//            }        
//        }
//    }
    
    
    void color_asignar(javax.swing.JButton p_btn){
        color_asignar(new Color(201,201,201),p_btn);
    }
    void color_asignar(java.awt.Color p_color,javax.swing.JButton p_btn){
        p_btn.setBackground(p_color);
        p_btn.setOpaque(true);
    }

    void grupo_recorrer_limpiar(){
        grupo_recorrer_limpiar(grp_btn);
    }
    void grupo_recorrer_limpiar(javax.swing.ButtonGroup p_btn_gpr){
        java.util.Enumeration<javax.swing.AbstractButton> enu_abs_btn=p_btn_gpr.getElements();
        while (enu_abs_btn.hasMoreElements()) {
            color_asignar(new Color(230,230,230),(javax.swing.JButton) enu_abs_btn.nextElement());
        }
    }
    public static int ventana_cerrar(javax.swing.JFrame frm){
        frm.setVisible(false);
        return 0;
    }
    public static int ventana_cerrar(javax.swing.JDialog frm){
        frm.setVisible(false);
        return 0;
    }
    public static int ventana_cerrar(javax.swing.JFrame frm,int p_valor){
        frm.setVisible(false);
        return p_valor;
    }
    public static int ventana_cerrar(javax.swing.JDialog frm,int p_valor){
        frm.setVisible(false);
        return p_valor;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        grp_btn = new javax.swing.ButtonGroup();
        pnl_principal = new javax.swing.JPanel();
        pnl_principal_cabecera = new javax.swing.JPanel();
        txt_vtn_tit = new javax.swing.JLabel();
        txt_vtn_btn_cer = new javax.swing.JLabel();
        txt_vtn_ico = new javax.swing.JButton();
        pnl_principal_cuerpo = new javax.swing.JPanel();
        pnl_principal_cuerpo_icono = new javax.swing.JPanel();
        lbl_msj_ico = new javax.swing.JLabel();
        pnl_principal_cuerpo_contenido = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_msj_crp = new javax.swing.JTextArea();
        pnl_principal_cuerpo_pie = new javax.swing.JPanel();
        btn_vtn_ace = new javax.swing.JButton();
        btn_vtn_can = new javax.swing.JButton();
        btn_vtn_si = new javax.swing.JButton();
        btn_vtn_no = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        pnl_principal.setBackground(new java.awt.Color(241, 241, 241));
        pnl_principal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pnl_principal_cabecera.setOpaque(false);

        txt_vtn_tit.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        txt_vtn_tit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_vtn_tit.setText("TITULO");
        txt_vtn_tit.setBorder(null);

        txt_vtn_btn_cer.setBackground(new java.awt.Color(255, 153, 153));
        txt_vtn_btn_cer.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txt_vtn_btn_cer.setForeground(new java.awt.Color(255, 255, 255));
        txt_vtn_btn_cer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_vtn_btn_cer.setText("x");
        txt_vtn_btn_cer.setToolTipText("Cerrar");
        txt_vtn_btn_cer.setBorder(null);
        txt_vtn_btn_cer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txt_vtn_btn_cer.setOpaque(true);
        txt_vtn_btn_cer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_vtn_btn_cerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txt_vtn_btn_cerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txt_vtn_btn_cerMouseExited(evt);
            }
        });

        txt_vtn_ico.setBackground(new java.awt.Color(211, 111, 66));
        txt_vtn_ico.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        txt_vtn_ico.setBorder(null);
        txt_vtn_ico.setContentAreaFilled(false);
        txt_vtn_ico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txt_vtn_ico.setFocusPainted(false);
        txt_vtn_ico.setPreferredSize(new java.awt.Dimension(55, 26));

        javax.swing.GroupLayout pnl_principal_cabeceraLayout = new javax.swing.GroupLayout(pnl_principal_cabecera);
        pnl_principal_cabecera.setLayout(pnl_principal_cabeceraLayout);
        pnl_principal_cabeceraLayout.setHorizontalGroup(
            pnl_principal_cabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cabeceraLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txt_vtn_ico, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txt_vtn_tit, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(txt_vtn_btn_cer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnl_principal_cabeceraLayout.setVerticalGroup(
            pnl_principal_cabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cabeceraLayout.createSequentialGroup()
                .addGroup(pnl_principal_cabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_vtn_ico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_principal_cabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_vtn_tit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_vtn_btn_cer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnl_principal_cuerpo.setBorder(null);
        pnl_principal_cuerpo.setOpaque(false);

        pnl_principal_cuerpo_icono.setBorder(null);
        pnl_principal_cuerpo_icono.setOpaque(false);

        lbl_msj_ico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_msj_ico.setBorder(null);
        lbl_msj_ico.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnl_principal_cuerpo_iconoLayout = new javax.swing.GroupLayout(pnl_principal_cuerpo_icono);
        pnl_principal_cuerpo_icono.setLayout(pnl_principal_cuerpo_iconoLayout);
        pnl_principal_cuerpo_iconoLayout.setHorizontalGroup(
            pnl_principal_cuerpo_iconoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cuerpo_iconoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_msj_ico, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_principal_cuerpo_iconoLayout.setVerticalGroup(
            pnl_principal_cuerpo_iconoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cuerpo_iconoLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(lbl_msj_ico, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        pnl_principal_cuerpo_contenido.setBorder(null);
        pnl_principal_cuerpo_contenido.setOpaque(false);

        jScrollPane1.setBorder(null);

        txt_msj_crp.setEditable(false);
        txt_msj_crp.setBackground(new java.awt.Color(241, 241, 241));
        txt_msj_crp.setColumns(20);
        txt_msj_crp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txt_msj_crp.setLineWrap(true);
        txt_msj_crp.setRows(5);
        txt_msj_crp.setBorder(null);
        txt_msj_crp.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt_msj_crp.setFocusable(false);
        txt_msj_crp.setRequestFocusEnabled(false);
        txt_msj_crp.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(txt_msj_crp);

        javax.swing.GroupLayout pnl_principal_cuerpo_contenidoLayout = new javax.swing.GroupLayout(pnl_principal_cuerpo_contenido);
        pnl_principal_cuerpo_contenido.setLayout(pnl_principal_cuerpo_contenidoLayout);
        pnl_principal_cuerpo_contenidoLayout.setHorizontalGroup(
            pnl_principal_cuerpo_contenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cuerpo_contenidoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnl_principal_cuerpo_contenidoLayout.setVerticalGroup(
            pnl_principal_cuerpo_contenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cuerpo_contenidoLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnl_principal_cuerpoLayout = new javax.swing.GroupLayout(pnl_principal_cuerpo);
        pnl_principal_cuerpo.setLayout(pnl_principal_cuerpoLayout);
        pnl_principal_cuerpoLayout.setHorizontalGroup(
            pnl_principal_cuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cuerpoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_principal_cuerpo_icono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnl_principal_cuerpo_contenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_principal_cuerpoLayout.setVerticalGroup(
            pnl_principal_cuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_principal_cuerpoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_principal_cuerpoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnl_principal_cuerpo_icono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_principal_cuerpo_contenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnl_principal_cuerpo_pie.setBorder(null);
        pnl_principal_cuerpo_pie.setOpaque(false);

        btn_vtn_ace.setBackground(new java.awt.Color(230, 230, 230));
        btn_vtn_ace.setText("Aceptar");
        btn_vtn_ace.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 230, 230)));
        grp_btn.add(btn_vtn_ace);
        btn_vtn_ace.setContentAreaFilled(false);
        btn_vtn_ace.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_vtn_ace.setOpaque(true);
        btn_vtn_ace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_vtn_aceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_vtn_aceMouseExited(evt);
            }
        });
        btn_vtn_ace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vtn_aceActionPerformed(evt);
            }
        });

        btn_vtn_can.setBackground(new java.awt.Color(230, 230, 230));
        btn_vtn_can.setText("Cancelar");
        btn_vtn_can.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 230, 230)));
        grp_btn.add(btn_vtn_can);
        btn_vtn_can.setContentAreaFilled(false);
        btn_vtn_can.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_vtn_can.setOpaque(true);
        btn_vtn_can.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_vtn_canMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_vtn_canMouseExited(evt);
            }
        });
        btn_vtn_can.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vtn_canActionPerformed(evt);
            }
        });

        btn_vtn_si.setBackground(new java.awt.Color(230, 230, 230));
        btn_vtn_si.setText("Si");
        btn_vtn_si.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 230, 230)));
        grp_btn.add(btn_vtn_si);
        btn_vtn_si.setContentAreaFilled(false);
        btn_vtn_si.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_vtn_si.setOpaque(true);
        btn_vtn_si.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_vtn_siMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_vtn_siMouseExited(evt);
            }
        });
        btn_vtn_si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vtn_siActionPerformed(evt);
            }
        });

        btn_vtn_no.setBackground(new java.awt.Color(230, 230, 230));
        btn_vtn_no.setText("No");
        btn_vtn_no.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(232, 230, 230)));
        grp_btn.add(btn_vtn_no);
        btn_vtn_no.setContentAreaFilled(false);
        btn_vtn_no.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_vtn_no.setOpaque(true);
        btn_vtn_no.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_vtn_noMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_vtn_noMouseExited(evt);
            }
        });
        btn_vtn_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_vtn_noActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_principal_cuerpo_pieLayout = new javax.swing.GroupLayout(pnl_principal_cuerpo_pie);
        pnl_principal_cuerpo_pie.setLayout(pnl_principal_cuerpo_pieLayout);
        pnl_principal_cuerpo_pieLayout.setHorizontalGroup(
            pnl_principal_cuerpo_pieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_principal_cuerpo_pieLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_vtn_si, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_vtn_no, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_vtn_can, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_vtn_ace, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnl_principal_cuerpo_pieLayout.setVerticalGroup(
            pnl_principal_cuerpo_pieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principal_cuerpo_pieLayout.createSequentialGroup()
                .addGroup(pnl_principal_cuerpo_pieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_vtn_ace, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_vtn_can, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_vtn_si, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_vtn_no, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnl_principalLayout = new javax.swing.GroupLayout(pnl_principal);
        pnl_principal.setLayout(pnl_principalLayout);
        pnl_principalLayout.setHorizontalGroup(
            pnl_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principalLayout.createSequentialGroup()
                .addGroup(pnl_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnl_principal_cuerpo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_principal_cabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_principal_cuerpo_pie, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        pnl_principalLayout.setVerticalGroup(
            pnl_principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_principalLayout.createSequentialGroup()
                .addComponent(pnl_principal_cabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnl_principal_cuerpo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_principal_cuerpo_pie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(pnl_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnl_principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_vtn_btn_cerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_vtn_btn_cerMouseClicked
        vg_valor_enviar=CERRAR;
        frmMensaje.ventana_cerrar(this);
    }//GEN-LAST:event_txt_vtn_btn_cerMouseClicked

    private void txt_vtn_btn_cerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_vtn_btn_cerMouseEntered
       cambiarColor((javax.swing.JLabel)evt.getSource(),Color.red,Color.white);
    }//GEN-LAST:event_txt_vtn_btn_cerMouseEntered

    private void txt_vtn_btn_cerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_vtn_btn_cerMouseExited
        cambiarColor((javax.swing.JLabel)evt.getSource(),new Color(255,153,153),Color.white);
    }//GEN-LAST:event_txt_vtn_btn_cerMouseExited

    private void btn_vtn_siMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_siMouseExited
        grupo_recorrer_limpiar();
    }//GEN-LAST:event_btn_vtn_siMouseExited

    private void btn_vtn_canMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_canMouseExited
        grupo_recorrer_limpiar();
    }//GEN-LAST:event_btn_vtn_canMouseExited

    private void btn_vtn_aceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_aceMouseExited
        grupo_recorrer_limpiar();
    }//GEN-LAST:event_btn_vtn_aceMouseExited

    private void btn_vtn_noMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_noMouseExited
        grupo_recorrer_limpiar();
    }//GEN-LAST:event_btn_vtn_noMouseExited

    private void btn_vtn_siMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_siMouseEntered
        color_asignar((javax.swing.JButton)evt.getSource());
    }//GEN-LAST:event_btn_vtn_siMouseEntered

    private void btn_vtn_noMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_noMouseEntered
        color_asignar((javax.swing.JButton)evt.getSource());
    }//GEN-LAST:event_btn_vtn_noMouseEntered

    private void btn_vtn_canMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_canMouseEntered
        color_asignar((javax.swing.JButton)evt.getSource());
    }//GEN-LAST:event_btn_vtn_canMouseEntered

    private void btn_vtn_aceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_vtn_aceMouseEntered
        color_asignar((javax.swing.JButton)evt.getSource());
    }//GEN-LAST:event_btn_vtn_aceMouseEntered

    private void btn_vtn_aceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vtn_aceActionPerformed
        vg_valor_enviar=ACEPTAR;
        frmMensaje.ventana_cerrar(this);
    }//GEN-LAST:event_btn_vtn_aceActionPerformed

    private void btn_vtn_canActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vtn_canActionPerformed
        vg_valor_enviar=CANCELAR;
        frmMensaje.ventana_cerrar(this);
    }//GEN-LAST:event_btn_vtn_canActionPerformed

    private void btn_vtn_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vtn_noActionPerformed
        vg_valor_enviar=NO;
        frmMensaje.ventana_cerrar(this);
    }//GEN-LAST:event_btn_vtn_noActionPerformed

    private void btn_vtn_siActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_vtn_siActionPerformed
        vg_valor_enviar=SI;
        frmMensaje.ventana_cerrar(this);
    }//GEN-LAST:event_btn_vtn_siActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btn_vtn_ace;
    private static javax.swing.JButton btn_vtn_can;
    private static javax.swing.JButton btn_vtn_no;
    private static javax.swing.JButton btn_vtn_si;
    private javax.swing.ButtonGroup grp_btn;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JLabel lbl_msj_ico;
    private javax.swing.JPanel pnl_principal;
    private javax.swing.JPanel pnl_principal_cabecera;
    private javax.swing.JPanel pnl_principal_cuerpo;
    private javax.swing.JPanel pnl_principal_cuerpo_contenido;
    private javax.swing.JPanel pnl_principal_cuerpo_icono;
    private javax.swing.JPanel pnl_principal_cuerpo_pie;
    private static javax.swing.JTextArea txt_msj_crp;
    private javax.swing.JLabel txt_vtn_btn_cer;
    private static javax.swing.JButton txt_vtn_ico;
    private static javax.swing.JLabel txt_vtn_tit;
    // End of variables declaration//GEN-END:variables
}
