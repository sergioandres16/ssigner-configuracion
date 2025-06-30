package Formularios;

import Clases.HojaPrincipal;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.io.File;
import Global.ClsCanvasPanel;
import Global.CConstantes;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JScrollPane;


public final class frmVisorPdf extends javax.swing.JDialog  implements ActionListener
{
    Thread hilocargadocumento;
    Thread vg_hilo_archivos_abrir;
    
    private int anchoformulariodefecto;
    protected static int pagina;
    protected static int paginas;
    public static float x;
    public static float y;
    public static float x1;
    public static float y1;
    private int pagina_numero;
    /*HOJA ORIGINAL*/
    HojaPrincipal cg_hoja_interior;
    /*HOJA PADRE*/
    HojaPrincipal cg_hoja_padre;
    ClsCanvasPanel cPanel;
    JScrollPane scrollPane;
    
    private void setScrollPane()
    {
        pnlDocumento.remove(scrollPane);
        scrollPane = new JScrollPane(cPanel);
        pnlDocumento.add(scrollPane);
       
        MouseListener[] m = scrollPane.getMouseListeners();
        
        if (scrollPane.getMouseListeners().length == 0)
        {
            scrollPane.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent evt)
                {
                    mousemove(evt);
                }

                @Override
                public void mouseReleased(MouseEvent evtEvent)
                {
                    mousereleased(evtEvent);
                }
            });

            scrollPane.addMouseMotionListener(new MouseMotionAdapter()
            {
                @Override
                public void mouseDragged(MouseEvent evtEvent)
                {
                    mousedragged(evtEvent);
                }
            });
        }
        
        cPanel.setBounds(0, 0, pnlDocumento.getWidth(), pnlDocumento.getHeight());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 0, pnlDocumento.getWidth(), pnlDocumento.getHeight());
    }
    
    public frmVisorPdf(java.awt.Frame parent, boolean modal, Point inicioArrastre, Point finArrastre) 
    {
        initComponents();
        btn_logo.setIcon(frmConfigurador.btnLogo.getIcon());
        crearCarpetaTemporal(frmConfigurador.vcg_so.getVg_ruta_user_home()+File.separator);
        
        chkFirmaVisible.setSelected(frmConfigurador.configuracion.isVg_firma_visible());
        cboFuente.setSelectedItem(frmConfigurador.configuracion.getVg_firma_visible_fuente());
        txtrutaImagen.setText(frmConfigurador.configuracion.getVg_firma_visible_ruta_imagen());
        chkCerrarDocumento.setSelected(frmConfigurador.configuracion.isVg_cerrar_documento());
        chkFirmarTodasPaginas.setSelected(frmConfigurador.configuracion.isVg_firma_visible_firmar_todas_paginas());
        cboalgoritmo.setSelectedItem(frmConfigurador.configuracion.getVg_algoritmo());
        chkConImagen.setSelected(frmConfigurador.configuracion.isVg_con_imagen());
        cboEstiloFirma.setSelectedIndex(frmConfigurador.configuracion.getVg_firma_visible_estilo_firma());
        chkCalNumber.setSelected(frmConfigurador.configuracion.getConNumeroCal());
        chkCampoArea.setSelected(frmConfigurador.configuracion.getConCarmpoArea());
        txtTamanio.setText(String.valueOf(frmConfigurador.configuracion.getVg_firma_visible_tamanio_fuente()));
        txtTextoFIrmante.setText(String.valueOf(frmConfigurador.configuracion.getVg_texto_firmante()));
//        boolean v_firma_visibleseleccionado=chkFirmaVisible.isSelected();
        chkCalNumber.setVisible(false);
        chkCampoArea.setVisible(true);

        boolean con_firma_visible=chkFirmaVisible.isSelected();
        int estilo_index=cboEstiloFirma.getSelectedIndex();
        
        if(con_firma_visible)
        {
            cboEstiloFirma.setEnabled(true);
            cboFuente.setEnabled(true);
            txtTamanio.setEnabled(true);
            
            switch (estilo_index)
            {
                case 0:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(true);
                    break;
                case 1:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(false);
                    break;
                default:
                    chkConImagen.setEnabled(true);
                    chkConImagen.setSelected(true);
                    break;
            }
            
            frmConfigurador.configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            
            switch (estilo_index)
            {
                case 0:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(true);
                    break;
                case 1:
                    chkConImagen.setEnabled(false);
                    chkConImagen.setSelected(false);
                    break;
                default:
                    chkConImagen.setEnabled(true);
                    chkConImagen.setSelected(true);
                    break;
            }
            
            frmConfigurador.configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            /*Solo imagen*/
            if(estilo_index==0)
            {
                if(chkConImagen.isSelected())
                    btnExaminar.setEnabled(true);
                
                else
                {            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            
            if(estilo_index==1)
            {
                if(!chkConImagen.isSelected())
                {      
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }            
            else if(estilo_index==2)
            {
                if(chkConImagen.isSelected())
                    btnExaminar.setEnabled(true);
                
                else
                {            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            
            frmConfigurador.configuracion.setVg_con_imagen(chkConImagen.isSelected());
        }
        else
        {
            chkConImagen.setEnabled(false);
            cboEstiloFirma.setEnabled(false);
            cboFuente.setEnabled(false);
            txtTamanio.setEnabled(false);
            btnExaminar.setEnabled(false);
        }
        
        frmConfigurador.configuracion.setVg_firma_visible(chkFirmaVisible.isSelected());
        anchoformulariodefecto=getWidth();
        chkDarConstancia.setVisible(false);
        
        ctrl_txt_visor_pagina_actual.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent e)
            {
                char caracter = e.getKeyChar();
                
                if(caracter < KeyEvent.VK_0 || caracter > KeyEvent.VK_9)
                    e.consume();
            }
            
            public void keyPressed(java.awt.event.KeyEvent e) 
            {
                int caracter = (int)e.getKeyChar();
                
                if(caracter == KeyEvent.VK_ENTER)
                {
                    if(ctrl_txt_visor_pagina_actual.getText().trim().length()>0)
                    {
                        pagina_numero = Integer.parseInt(ctrl_txt_visor_pagina_actual.getText());
                        pagina = pagina_numero;
                        
                        if (pagina > paginas || pagina < 0)
                            pagina = 1;
                        
                        viewPage();
                    }
                }
            }
        });        
       
        ctrl_txt_visor_pagina_actual.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                if(ctrl_txt_visor_pagina_actual.getText().trim().length()>0)
                    if(ctrl_txt_visor_pagina_actual.getText().trim().length()>0)
                    {
                        pagina_numero = Integer.parseInt(ctrl_txt_visor_pagina_actual.getText());
                        pagina = pagina_numero;
                        
                        if (pagina > paginas || pagina < 0)
                            pagina = 1;
                        
                        viewPage();
                    }
            }
        });
        
        scrollPane = new JScrollPane(cPanel);
        pnlDocumento.add(scrollPane);
        
        pnlDocumento.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent evt)
            {
                mousemove(evt);
            }

            @Override
            public void mouseReleased(MouseEvent evtEvent)
            {
                mousereleased(evtEvent);
            }
        });

        pnlDocumento.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent evtEvent)
            {
                mousedragged(evtEvent);
            }
        });
        
        btnPaginaSiguiente.addActionListener(frmVisorPdf.this);
	btnPaginaAnterior.addActionListener(frmVisorPdf.this);
        btnPrimeraPagina.addActionListener(frmVisorPdf.this);
	btnPaginaUltima.addActionListener(frmVisorPdf.this);
        
        cPanel = new ClsCanvasPanel();
        cPanel.setBounds(0, 0, pnlDocumento.getWidth(), pnlDocumento.getHeight());
        cPanel.setVisible(true);
        pnlDocumento.add(cPanel);
        pnlDocumento.setAutoscrolls(true);
                
        if (new File(frmConfigurador.configuracion.getVg_ruta_documento()).exists())
        {
            lblInfo.setText("Cargando ventana...");
            lblInfo.setVisible(false);
            cPanel.setPDF(frmConfigurador.configuracion.getVg_ruta_documento());
            paginas = cPanel.getNumPages();
            pagina = 1;
            txtPaginas.setText("" + paginas);
            ctrl_txt_visor_pagina_actual.setText("" + pagina + " de " + paginas);
            
            int pag = frmConfigurador.configuracion.getVg_firma_visible_pagina();
            
            if (pag <= paginas)
                pagina = pag;
            
            viewPage();
            repaint();
        }
        
        inicioArrastre.x = (int)frmConfigurador.configuracion.getVg_firma_visible_xp();
        inicioArrastre.y = (int)frmConfigurador.configuracion.getVg_firma_visible_yp();
        finArrastre.x = (int)frmConfigurador.configuracion.getVg_firma_visible_x1p();
        finArrastre.y =  (int)frmConfigurador.configuracion.getVg_firma_visible_y1p();
        cPanel.setInicioArrastre(inicioArrastre);
        cPanel.setFinArrastre(finArrastre);
                
        cPanel.repaint();
    }

    private void crearCarpetaTemporal(String ruta_origen)
    {
        new File(ruta_origen+ "temporales"+File.separator).mkdirs();
    }
    
    private void limpiarTemporales(File[] archivosaeliminar)
    {
        if(archivosaeliminar != null)
            for (File archivosaeliminar1 : archivosaeliminar)
                archivosaeliminar1.delete();
    }

    int alpha = 100;
    private Color vcg_color_relleno = new Color(245, 245,245 );
    private Color vcg_color_borde = Color.blue;
   
    protected void cambiarColor(Object obj)
    {
        cambiarColor(obj,Color.lightGray,Color.white);
    }
    
    boolean validarDatos()
    {
        boolean correcto=true;
        if(chkFirmaVisible.isSelected())
        {
            if(chkConImagen.isSelected())
            {
                if(txtrutaImagen.getText().trim().length()==0)
                {
                    CConstantes.dialogo("Seleccione una imagen  para la firma visible.");
                    txtrutaImagen.requestFocus();
                    correcto=false;
                }
                else if(!new File(txtrutaImagen.getText()).exists())
                {
                    CConstantes.dialogo("La ruta de la imagen "+txtrutaImagen.getText()+"  no se encuentra.");
                    txtrutaImagen.requestFocus();
                    correcto=false;
                }
            }
        }
        return correcto;
    }
    protected void cambiarColor(Object obj,Color colorfondo,Color colorletra){
        if(obj instanceof javax.swing.JLabel){
            ((javax.swing.JLabel) obj).setBackground(colorfondo);
            ((javax.swing.JLabel) obj).setForeground(colorletra);
            ((javax.swing.JLabel) obj).setOpaque(true);
        }
        else if(obj instanceof JButton){
            ((JButton) obj).setBackground(colorfondo);
            ((JButton) obj).setForeground(colorletra);
            ((JButton) obj).setOpaque(true);
            ((javax.swing.JButton) obj).setBorder(javax.swing.BorderFactory.createLineBorder(colorletra));
        } 
        else if(obj instanceof JToggleButton){
            ((JToggleButton) obj).setBackground(colorfondo);
            ((JToggleButton) obj).setForeground(colorletra);
            ((JToggleButton) obj).setOpaque(true);
            ((javax.swing.JToggleButton) obj).setBorder(javax.swing.BorderFactory.createLineBorder(colorletra));
        } 
    }
    void agrandarFormulario()
    {
        hilocargadocumento=new Thread(() ->
        {
            String sentido=lblCambiarTamano.getText();
            
            if(sentido.equals(">"))
            {
                try
                {
                    lblCambiarTamano.setText("<");
                    int i;//=getWidth();
                    pnlConfiguracion.setVisible(true);
                    
                    for ( i=getWidth() ; i < anchoformulariodefecto+50; i+=50)
                    {
                        Thread.sleep(20,50);
                        
                        if(i >= anchoformulariodefecto)
                        {
                            i = anchoformulariodefecto;
                            //JOptionPane.showMessageDialog(panel, i + "xxxxxx" + anchoformulariodefecto);
                            setSize(new Dimension(i,getHeight()));
                            i = anchoformulariodefecto + 50;
                        }
                        else
                            setSize(new Dimension(i,getHeight()));
                    }

                }
                catch (Exception ex) { }
            }
            else
            {
                lblCambiarTamano.setText(">");
                
                for (int i =getWidth(); i >= 460; i-=50)
                {
                    try
                    {
                        Thread.sleep(20,50);
                        setSize(new Dimension(i+5,getHeight()));
                    }
                    catch (Exception ex)
                    {
                        System.out.println(""+ex);
                    }
                }
                pnlConfiguracion.setVisible(false);
            }
            setLocationRelativeTo(null);
        });
        hilocargadocumento.setDaemon(true);
        hilocargadocumento.start();
    }
    
    private void cerrarVentana()
    {
        setVisible(false);
        dispose();
    }

    void cargarDocumento(final int p_boton_origen_accion)
    {
        vg_hilo_archivos_abrir=new Thread(() ->
        {
            ctrl_btn_visor_cargar_documento.setText("Cargando...");
            final JFileChooser vi_ventana_documento_seleccionar = new JFileChooser(frmConfigurador.configuracion.getVg_ruta_documento());
            
            FilenameFilter fileNameFilter = new FilenameFilter()
            {
                @Override
                public boolean accept(File dir, String name)
                {
                    dispose();
                    if(name.lastIndexOf('.') > 0)
                    {
                        int lastIndex = name.lastIndexOf('.');
                        String str = name.substring(lastIndex);
                        
                        if(str.equalsIgnoreCase(".pdf") && !str.equals(frmConfigurador.configuracion.getVg_documento_nombre()))
                            return true;
                    }
                    return false;
                }
            };
            
            vi_ventana_documento_seleccionar.setFileSelectionMode(JFileChooser.FILES_ONLY);
            vi_ventana_documento_seleccionar.setAcceptAllFileFilterUsed(false);
            vi_ventana_documento_seleccionar.addChoosableFileFilter( new FileNameExtensionFilter("Documentos PDF ", "pdf"));
            vi_ventana_documento_seleccionar.setMultiSelectionEnabled(true);
            vi_ventana_documento_seleccionar.setDialogTitle("Abrir un documento para su visualización");
            int select = vi_ventana_documento_seleccionar.showOpenDialog(frmVisorPdf.this);
            
            if(select == JFileChooser.APPROVE_OPTION)
            {
                lblInfo.setVisible(false);
                ctrl_txt_visor_pagina_actual.setEnabled(true);
                btnPrimeraPagina.setEnabled(true);
                btnPaginaSiguiente.setEnabled(true);
                btnPaginaAnterior.setEnabled(true);
                btnPaginaUltima.setEnabled(true);
                btnPrimeraPagina.setEnabled(true);
                                
                try
                {
                    List<String> v_lista_rutas=new ArrayList<String>();
                    File[] archivos= vi_ventana_documento_seleccionar.getSelectedFiles();
                    File v_archivo_auxiliar;
                    
                    if(archivos==null)
                    {
                        v_archivo_auxiliar = new File(frmConfigurador.configuracion.getVg_ruta_documento());
                        v_lista_rutas.add(v_archivo_auxiliar.getAbsolutePath());
                    }
                    else
                    {
                        if(archivos.length>1)
                        {
                            chkDarConstancia.setText("Doy constancia de haber visto todos los "+archivos.length+" documentos.");
                            chkDarConstancia.setToolTipText(chkDarConstancia.getText());
                            chkDarConstancia.setVisible(true);
                        }
                        else
                            chkDarConstancia.setVisible(false);
                        
                        for (File archivo : archivos)
                        {
                            if(archivo.isDirectory())
                            {
                                File[] archivocarpeta= archivo.listFiles(fileNameFilter);
                                
                                for (File archivo1 :archivocarpeta)
                                    if(archivo1.isFile())
                                        v_lista_rutas.add(archivo1.getAbsolutePath()); 
                            }
                            else
                                v_lista_rutas.add(archivo.getAbsolutePath());
                        }
                    }
                    if(v_lista_rutas.isEmpty())
                        return;
                    else
                    {
                        if(v_lista_rutas.get(0).equals(frmConfigurador.configuracion.getVg_ruta_documento()))
                        {
                            CConstantes.dialogo("El documento "+v_lista_rutas.get(0) + " ya está en el visor.");
                            ctrl_btn_visor_cargar_documento.setText("ARCHIVO");
                            return;
                        }	
                    }
                    
                    ctrl_btn_visor_cargar_documento.setText("ARCHIVO");
                    lblDocumento.setText("" + v_lista_rutas.size() + " documentos(s) para firmar");
                    frmConfigurador.configuracion.setVg_ruta_documento(v_lista_rutas.get(0));
                    String nombreArchivo;
                    
                    if(frmConfigurador.configuracion.getVg_ruta_documento().length()>0)
                    {
                        nombreArchivo=new File(frmConfigurador.configuracion.getVg_ruta_documento()).getName();
                        lblDocumento.setText(nombreArchivo);
                        lblDocumento.setToolTipText(nombreArchivo);
                    }
                    else
                        return;
                    
                    //frmConfigurador.configuracion.setVg_ruta_documento(frmConfigurador.vcg_so.ObtenerSistemaOperativo_user_home() +File.separator+"temporal"+File.separator+nombreArchivo);
                    cPanel.setPDF(frmConfigurador.configuracion.getVg_ruta_documento());
                    
                    File file = new File(frmConfigurador.configuracion.getVg_ruta_documento());
                    File file1 = new File(v_lista_rutas.get(0));

                    frmConfigurador.configuracion.setVg_documento_nombre(file.getName());
                    frmConfigurador.configuracion.setVg_ruta_documento(file1.getAbsolutePath());
                    frmConfigurador.configuracion.setVg_documento_ruta_padre(file1.getParent());
                    frmConfigurador.configuracion.setVg_firma_visible_pagina(cPanel.getNumPages());
                    pagina = 1;
                    paginas= cPanel.getNumPages();
                    txtPaginas.setText("" + paginas);
                    ctrl_txt_visor_pagina_actual.setText("" + paginas);
                    viewPage();
                    repaint();

                    limpiarTemporales(new File(frmConfigurador.vcg_so.ObtenerSistemaOperativo_user_home()+File.separator+"temporal"+File.separator).listFiles());
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else{
                lblInfo.setText("CARGUE UN DOCUMENTO PARA VISUALIZARLO");
                ctrl_btn_visor_cargar_documento.setText("ARCHIVO");
            }
        });
        vg_hilo_archivos_abrir.setDaemon(true);
        vg_hilo_archivos_abrir.start();
    }
    
    private void viewPage()
    {
        setScrollPane();
        ctrl_txt_visor_pagina_actual.setText("" + pagina);
        cPanel.setPage(pagina - 1, 1);
        repaint();
    }    
    
    public void keyPressed(KeyEvent el)
    {
        if (el.getKeyCode() == KeyEvent.VK_A)
            cPanel.clearPanel();
    }

    public void mousemove(MouseEvent e)
    {
        cPanel.mouseMoved(e);
    }
    
    public void mousedragged(MouseEvent e)
    {
        cPanel.mouseDragged(e);
    }

    public void mousereleased(MouseEvent e)
    {
        cPanel.setOffSetY(0);
        cPanel.setOffSetX(0);
        cPanel.mouseReleased(e);
        
        if(cPanel.getRectangleWidth() < 10 || cPanel.getRectangleHeight() < 10)
        {
            frmConfigurador.configuracion.setVg_firma_visible_x(0);
            frmConfigurador.configuracion.setVg_firma_visible_y(0);
            frmConfigurador.configuracion.setVg_firma_visible_x1(0);
            frmConfigurador.configuracion.setVg_firma_visible_y1(0);
        }
        else
        {
            if(!validarDatos())
            {
                String sentido=lblCambiarTamano.getText();
                
                if(sentido.equals("<"))
                    return;
                
                agrandarFormulario();
                return;
            }
            
            Rectangle rect = cPanel.getRectangle();

            float y22, x22;
            float x11 = (float)rect.getX();
            float y11 = (float)rect.getY();

            float ancho0 = (float)rect.getWidth();
            float alto0 = (float)rect.getHeight();
            
            /*if(cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
            {
                x11 = x11 * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth();
                y11 = y11 * cPanel.getAltoReal(pagina - 1) / cPanel.getHeight();
                y11 = cPanel.getAltoReal(pagina - 1) - y11 - alto0 * (cPanel.getAltoReal(pagina - 1)) / cPanel.getHeight();

                y22 = alto0 * cPanel.getAltoReal(pagina - 1) / cPanel.getHeight();
                x22 = ancho0 * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth();
            }
            else
            {
                x11 = x11 * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth();
                y11 = y11 * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight();
                y11 = cPanel.getAnchoReal(pagina - 1) - y11  - alto0 * (cPanel.getAnchoReal(pagina - 1))/ cPanel.getHeight();

                y22 = alto0 * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight();
                x22 = ancho0 * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth();
            }

            y22 += y11;
            x22 += x11;*/

            frmConfigurador.configuracion.setVg_firma_visible_ancho_base(cPanel.getAnchoReal(pagina - 1));
            frmConfigurador.configuracion.setVg_firma_visible_alto_base(cPanel.getAltoReal(pagina - 1));
            frmConfigurador.configuracion.setVg_firma_visible_x((float)rect.getX());
            frmConfigurador.configuracion.setVg_firma_visible_y((float)rect.getY());
            frmConfigurador.configuracion.setVg_firma_visible_x1(ancho0);
            frmConfigurador.configuracion.setVg_firma_visible_y1(alto0);

            if(chkDarConstancia.isVisible())
            {
                /*SI PRESIONA OK PUEDE FIRMAR TODOS*/
                if(!chkDarConstancia.isSelected())
                {
                    int total= frmConfigurador.configuracion.getRutasdocumentos().size() - 1;
                    
                    for (int i = total; i >=1; i--)
                        frmConfigurador.configuracion.getRutasdocumentos().remove(i);
                    
                    lblDocumento.setText("1");
                }
            }

        }
    }
    
    private int preguntarDarConstancia()
    {
        return JOptionPane.showConfirmDialog(rootPane, "¿Da constancia de haber revisado todos los documentos seleccionados.?","SSIGNER",JOptionPane.OK_OPTION,3);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlBarra6 = new javax.swing.JPanel();
        btnCargar4 = new javax.swing.JButton();
        ctrl_lbl_documentos_contador = new javax.swing.JLabel();
        pnlPrincipal = new javax.swing.JPanel();
        ctrl_btn_visor_cargar_documento = new javax.swing.JButton();
        pnlDocumento = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        btnPrimeraPagina = new javax.swing.JButton();
        btnPaginaAnterior = new javax.swing.JButton();
        btnPaginaSiguiente = new javax.swing.JButton();
        btnPaginaUltima = new javax.swing.JButton();
        pnlConfiguracion = new javax.swing.JPanel();
        chkFirmarTodasPaginas = new javax.swing.JCheckBox();
        chkConImagen = new javax.swing.JCheckBox();
        cboalgoritmo = new javax.swing.JComboBox();
        cboFuente = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        chkCerrarDocumento = new javax.swing.JCheckBox();
        txtTamanio = new javax.swing.JTextField();
        txtrutaImagen = new javax.swing.JTextField();
        chkFirmaVisible = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cboEstiloFirma = new javax.swing.JComboBox();
        btnExaminar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnGrabarCambios = new javax.swing.JButton();
        btn_logo = new javax.swing.JButton();
        chkCalNumber = new javax.swing.JCheckBox();
        chkCampoArea = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        txtTextoFIrmante = new javax.swing.JTextField();
        lblCambiarTamano = new javax.swing.JLabel();
        lblEtTotalPaginas = new javax.swing.JLabel();
        txtPaginas = new javax.swing.JTextField();
        ctrl_txt_visor_pagina_actual = new javax.swing.JTextField();
        lblDocumento = new javax.swing.JLabel();
        chkDarConstancia = new javax.swing.JCheckBox();
        ctrl_lbl_cerrar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(241, 241, 241));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setAutoscrolls(true);

        pnlBarra6.setBackground(new java.awt.Color(241, 241, 241));

        btnCargar4.setBackground(new java.awt.Color(211, 111, 66));
        btnCargar4.setFont(new java.awt.Font("Comic Sans MS", 1, 10)); // NOI18N
        btnCargar4.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar4.setToolTipText("Cargar documentos para visualzar.");
        btnCargar4.setAutoscrolls(true);
        btnCargar4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btnCargar4.setBorderPainted(false);
        btnCargar4.setContentAreaFilled(false);
        btnCargar4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCargar4.setFocusPainted(false);
        btnCargar4.setPreferredSize(new java.awt.Dimension(55, 26));
        btnCargar4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCargar4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCargar4MouseExited(evt);
            }
        });
        btnCargar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargar4ActionPerformed(evt);
            }
        });

        ctrl_lbl_documentos_contador.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        ctrl_lbl_documentos_contador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ctrl_lbl_documentos_contador.setText("VISOR DE DOCUMENTOS");
        ctrl_lbl_documentos_contador.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlBarra6Layout = new javax.swing.GroupLayout(pnlBarra6);
        pnlBarra6.setLayout(pnlBarra6Layout);
        pnlBarra6Layout.setHorizontalGroup(
            pnlBarra6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBarra6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(btnCargar4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ctrl_lbl_documentos_contador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnlBarra6Layout.setVerticalGroup(
            pnlBarra6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ctrl_lbl_documentos_contador, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlBarra6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(btnCargar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlPrincipal.setBackground(new java.awt.Color(241, 241, 241));
        pnlPrincipal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 0));
        pnlPrincipal.setPreferredSize(new java.awt.Dimension(847, 611));

        ctrl_btn_visor_cargar_documento.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        ctrl_btn_visor_cargar_documento.setText("ARCHIVO");
        ctrl_btn_visor_cargar_documento.setToolTipText("Cargar documentos para visualzar.");
        ctrl_btn_visor_cargar_documento.setAutoscrolls(true);
        ctrl_btn_visor_cargar_documento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_btn_visor_cargar_documento.setFocusPainted(false);
        ctrl_btn_visor_cargar_documento.setPreferredSize(new java.awt.Dimension(55, 26));
        ctrl_btn_visor_cargar_documento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ctrl_btn_visor_cargar_documentoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ctrl_btn_visor_cargar_documentoMouseExited(evt);
            }
        });
        ctrl_btn_visor_cargar_documento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrl_btn_visor_cargar_documentoActionPerformed(evt);
            }
        });

        pnlDocumento.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pnlDocumento.setForeground(new java.awt.Color(102, 153, 255));
        pnlDocumento.setAutoscrolls(true);
        pnlDocumento.setPreferredSize(new java.awt.Dimension(424, 496));

        lblInfo.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(211, 111, 66));
        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfo.setText("CARGUE UN DOCUMENTO PARA VISUALIZARLO");

        javax.swing.GroupLayout pnlDocumentoLayout = new javax.swing.GroupLayout(pnlDocumento);
        pnlDocumento.setLayout(pnlDocumentoLayout);
        pnlDocumentoLayout.setHorizontalGroup(
            pnlDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDocumentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlDocumentoLayout.setVerticalGroup(
            pnlDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDocumentoLayout.createSequentialGroup()
                .addGap(221, 221, 221)
                .addComponent(lblInfo)
                .addContainerGap(256, Short.MAX_VALUE))
        );

        btnPrimeraPagina.setForeground(new java.awt.Color(67, 73, 111));
        btnPrimeraPagina.setText("‖⇇");
        btnPrimeraPagina.setToolTipText("Primero");
        btnPrimeraPagina.setContentAreaFilled(false);
        btnPrimeraPagina.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrimeraPagina.setFocusPainted(false);
        btnPrimeraPagina.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPrimeraPagina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPrimeraPaginaMouseEntered(evt);
            }
        });

        btnPaginaAnterior.setBackground(new java.awt.Color(255, 255, 255));
        btnPaginaAnterior.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaAnterior.setText("⇇");
        btnPaginaAnterior.setToolTipText("Anterior");
        btnPaginaAnterior.setContentAreaFilled(false);
        btnPaginaAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaAnterior.setFocusPainted(false);
        btnPaginaAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPaginaAnterior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPaginaAnteriorMouseEntered(evt);
            }
        });

        btnPaginaSiguiente.setBackground(new java.awt.Color(255, 255, 255));
        btnPaginaSiguiente.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaSiguiente.setText("⇉");
        btnPaginaSiguiente.setToolTipText("Siguiente");
        btnPaginaSiguiente.setContentAreaFilled(false);
        btnPaginaSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaSiguiente.setFocusPainted(false);
        btnPaginaSiguiente.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnPaginaUltima.setBackground(new java.awt.Color(255, 255, 255));
        btnPaginaUltima.setForeground(new java.awt.Color(67, 73, 111));
        btnPaginaUltima.setText("⇉‖");
        btnPaginaUltima.setToolTipText("Último");
        btnPaginaUltima.setContentAreaFilled(false);
        btnPaginaUltima.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPaginaUltima.setFocusPainted(false);
        btnPaginaUltima.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPaginaUltima.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPaginaUltimaMouseEntered(evt);
            }
        });

        pnlConfiguracion.setBackground(new java.awt.Color(241, 241, 241));

        chkFirmarTodasPaginas.setBackground(new java.awt.Color(255, 255, 255));
        chkFirmarTodasPaginas.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        chkFirmarTodasPaginas.setForeground(new java.awt.Color(0, 83, 154));
        chkFirmarTodasPaginas.setSelected(true);
        chkFirmarTodasPaginas.setText("Firmar en todas las páginas");
        chkFirmarTodasPaginas.setToolTipText("");
        chkFirmarTodasPaginas.setContentAreaFilled(false);
        chkFirmarTodasPaginas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkFirmarTodasPaginas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFirmarTodasPaginasActionPerformed(evt);
            }
        });

        chkConImagen.setBackground(new java.awt.Color(255, 255, 255));
        chkConImagen.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        chkConImagen.setSelected(true);
        chkConImagen.setText("Imagen");
        chkConImagen.setContentAreaFilled(false);
        chkConImagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkConImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConImagenActionPerformed(evt);
            }
        });

        cboalgoritmo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cboalgoritmo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SHA-1", "SHA-256" }));
        cboalgoritmo.setToolTipText("Algoritmo de resúmen");
        cboalgoritmo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboalgoritmoActionPerformed(evt);
            }
        });

        cboFuente.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        cboFuente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Arial", "Calibri" }));
        cboFuente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFuenteActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(142, 141, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 83, 154));
        jLabel4.setText("Algoritmo de protección");

        chkCerrarDocumento.setBackground(new java.awt.Color(255, 255, 255));
        chkCerrarDocumento.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        chkCerrarDocumento.setForeground(new java.awt.Color(0, 83, 154));
        chkCerrarDocumento.setSelected(true);
        chkCerrarDocumento.setText("Cerrar documento");
        chkCerrarDocumento.setContentAreaFilled(false);
        chkCerrarDocumento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCerrarDocumento.setHideActionText(true);
        chkCerrarDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCerrarDocumentoActionPerformed(evt);
            }
        });

        txtTamanio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTamanio.setText("10");
        txtTamanio.setToolTipText("Tamaño de fuente");
        txtTamanio.setPreferredSize(new java.awt.Dimension(18, 25));
        txtTamanio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTamanioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTamanioFocusLost(evt);
            }
        });

        txtrutaImagen.setEditable(false);
        txtrutaImagen.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        txtrutaImagen.setText("C:/imagen.jpg");
        txtrutaImagen.setToolTipText("Ruta de Imagen");
        txtrutaImagen.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtrutaImagen.setEnabled(false);

        chkFirmaVisible.setBackground(new java.awt.Color(255, 255, 255));
        chkFirmaVisible.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        chkFirmaVisible.setSelected(true);
        chkFirmaVisible.setText("Firma Visible");
        chkFirmaVisible.setBorderPaintedFlat(true);
        chkFirmaVisible.setContentAreaFilled(false);
        chkFirmaVisible.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkFirmaVisible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFirmaVisibleActionPerformed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(0, 83, 154));
        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("PROTECCIONES DEL DOCUMENTO A FIRMAR");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel6.setOpaque(true);

        jLabel10.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 83, 154));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Estilo");

        cboEstiloFirma.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        cboEstiloFirma.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solo imagen", "Solo descripción", "Imagen y descripción", "Imagen con texto al lado" }));
        cboEstiloFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEstiloFirmaActionPerformed(evt);
            }
        });

        btnExaminar.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btnExaminar.setText("...");
        btnExaminar.setToolTipText("Examinar");
        btnExaminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExaminar.setEnabled(false);
        btnExaminar.setFocusPainted(false);
        btnExaminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExaminarActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 83, 154));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Fuente");

        btnGrabarCambios.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        btnGrabarCambios.setText("Continuar");
        btnGrabarCambios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGrabarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarCambiosActionPerformed(evt);
            }
        });

        btn_logo.setBackground(new java.awt.Color(241, 241, 241));
        btn_logo.setBorderPainted(false);
        btn_logo.setContentAreaFilled(false);
        btn_logo.setFocusPainted(false);
        btn_logo.setFocusable(false);
        btn_logo.setRequestFocusEnabled(false);
        btn_logo.setVerifyInputWhenFocusTarget(false);

        chkCalNumber.setBackground(new java.awt.Color(255, 255, 255));
        chkCalNumber.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCalNumber.setForeground(new java.awt.Color(97, 120, 134));
        chkCalNumber.setSelected(true);
        chkCalNumber.setText("Incluir Número CAL");
        chkCalNumber.setBorderPaintedFlat(true);
        chkCalNumber.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCalNumber.setOpaque(false);
        chkCalNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCalNumberActionPerformed(evt);
            }
        });

        chkCampoArea.setBackground(new java.awt.Color(255, 255, 255));
        chkCampoArea.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        chkCampoArea.setForeground(new java.awt.Color(97, 120, 134));
        chkCampoArea.setText("Incluir Área Organizacional");
        chkCampoArea.setBorderPaintedFlat(true);
        chkCampoArea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkCampoArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCampoAreaActionPerformed(evt);
            }
        });

        jLabel1.setText("Texto junto al nombre del firmante:");

        txtTextoFIrmante.setText("Firmado digitalmente por:");
        txtTextoFIrmante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTextoFIrmanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlConfiguracionLayout = new javax.swing.GroupLayout(pnlConfiguracion);
        pnlConfiguracion.setLayout(pnlConfiguracionLayout);
        pnlConfiguracionLayout.setHorizontalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkFirmaVisible)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGrabarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConfiguracionLayout.createSequentialGroup()
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlConfiguracionLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chkConImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                                .addComponent(cboFuente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTamanio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboEstiloFirma, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtrutaImagen))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExaminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConfiguracionLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(chkFirmarTodasPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                                .addComponent(chkCerrarDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboalgoritmo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(chkCampoArea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                            .addComponent(chkCalNumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                            .addComponent(txtTextoFIrmante, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        pnlConfiguracionLayout.setVerticalGroup(
            pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkFirmaVisible, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboEstiloFirma)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnExaminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtrutaImagen, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkConImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtTamanio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cboFuente, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCerrarDocumento)
                    .addComponent(chkFirmarTodasPaginas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboalgoritmo)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnGrabarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(chkCalNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkCampoArea, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTextoFIrmante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        lblCambiarTamano.setBackground(new java.awt.Color(211, 111, 66));
        lblCambiarTamano.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
        lblCambiarTamano.setForeground(new java.awt.Color(255, 255, 255));
        lblCambiarTamano.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCambiarTamano.setText("<");
        lblCambiarTamano.setAutoscrolls(true);
        lblCambiarTamano.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblCambiarTamano.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCambiarTamano.setOpaque(true);
        lblCambiarTamano.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblCambiarTamanoMouseReleased(evt);
            }
        });

        lblEtTotalPaginas.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        lblEtTotalPaginas.setText("Páginas: ");

        txtPaginas.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        txtPaginas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPaginas.setEnabled(false);
        txtPaginas.setFocusable(false);

        ctrl_txt_visor_pagina_actual.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N
        ctrl_txt_visor_pagina_actual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ctrl_txt_visor_pagina_actual.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 102), 1, true));
        ctrl_txt_visor_pagina_actual.setEnabled(false);
        ctrl_txt_visor_pagina_actual.setPreferredSize(new java.awt.Dimension(4, 29));
        ctrl_txt_visor_pagina_actual.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ctrl_txt_visor_pagina_actualFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ctrl_txt_visor_pagina_actualFocusLost(evt);
            }
        });

        lblDocumento.setFont(new java.awt.Font("Segoe UI Light", 1, 10)); // NOI18N

        chkDarConstancia.setBackground(new java.awt.Color(255, 255, 255));
        chkDarConstancia.setFont(new java.awt.Font("Segoe UI Light", 1, 11)); // NOI18N
        chkDarConstancia.setForeground(new java.awt.Color(67, 73, 111));
        chkDarConstancia.setSelected(true);
        chkDarConstancia.setText("Doy constancia de haber visto todos los documentos.");
        chkDarConstancia.setContentAreaFilled(false);
        chkDarConstancia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        chkDarConstancia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDarConstanciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnlPrincipalLayout.createSequentialGroup()
                            .addComponent(ctrl_btn_visor_cargar_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(20, 20, 20)
                            .addComponent(lblEtTotalPaginas)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtPaginas))
                        .addGroup(pnlPrincipalLayout.createSequentialGroup()
                            .addComponent(btnPrimeraPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnPaginaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ctrl_txt_visor_pagina_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnPaginaSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnPaginaUltima, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlPrincipalLayout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(chkDarConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlDocumento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCambiarTamano)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ctrl_btn_visor_cargar_documento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEtTotalPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPaginaSiguiente)
                            .addComponent(btnPaginaUltima)
                            .addComponent(ctrl_txt_visor_pagina_actual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPaginaAnterior)
                            .addComponent(btnPrimeraPagina))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                                .addComponent(pnlDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkDarConstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                                .addComponent(lblCambiarTamano, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(252, 252, 252)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        ctrl_lbl_cerrar.setBackground(new java.awt.Color(255, 153, 153));
        ctrl_lbl_cerrar.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        ctrl_lbl_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        ctrl_lbl_cerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ctrl_lbl_cerrar.setText("x");
        ctrl_lbl_cerrar.setToolTipText("Cerrar");
        ctrl_lbl_cerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ctrl_lbl_cerrar.setOpaque(true);
        ctrl_lbl_cerrar.setPreferredSize(new java.awt.Dimension(40, 30));
        ctrl_lbl_cerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctrl_lbl_cerrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ctrl_lbl_cerrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ctrl_lbl_cerrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 905, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlBarra6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ctrl_lbl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlBarra6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrl_lbl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 614, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ctrl_btn_visor_cargar_documentoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_visor_cargar_documentoMouseEntered
        //cambiarColor((javax.swing.JButton)evt.getSource(),java.awt.Color.white,new Color(211,111,66));
    }//GEN-LAST:event_ctrl_btn_visor_cargar_documentoMouseEntered

    private void ctrl_btn_visor_cargar_documentoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_btn_visor_cargar_documentoMouseExited
        //cambiarColor((javax.swing.JButton)evt.getSource(),new Color(211,111,66),java.awt.Color.white);
    }//GEN-LAST:event_ctrl_btn_visor_cargar_documentoMouseExited

    private void ctrl_btn_visor_cargar_documentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrl_btn_visor_cargar_documentoActionPerformed
        cargarDocumento(1);
    }//GEN-LAST:event_ctrl_btn_visor_cargar_documentoActionPerformed

    private void btnPrimeraPaginaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrimeraPaginaMouseEntered
        // cambiarColoFondo(btnPrimeraPagina);
    }//GEN-LAST:event_btnPrimeraPaginaMouseEntered

    private void btnPaginaAnteriorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaAnteriorMouseEntered
        //  cambiarColoFondo(btnPaginaAnterior);
    }//GEN-LAST:event_btnPaginaAnteriorMouseEntered

    private void btnPaginaUltimaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPaginaUltimaMouseEntered
        //  cambiarColoFondo(btnUltimaPagina);
    }//GEN-LAST:event_btnPaginaUltimaMouseEntered

    private void chkFirmarTodasPaginasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFirmarTodasPaginasActionPerformed
         //grabarPropiedad(CConstantes.BOLTODASPAGINAS,Boolean.toString(chkFirmarTodasPaginas.isSelected()));
         frmConfigurador.configuracion.setVg_firma_visible_firmar_todas_paginas(chkFirmarTodasPaginas.isSelected());
    }//GEN-LAST:event_chkFirmarTodasPaginasActionPerformed

    private void chkConImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConImagenActionPerformed
        boolean con_firma_visible=chkFirmaVisible.isSelected();
        int estilo_index=cboEstiloFirma.getSelectedIndex();
        
        if(con_firma_visible)
        {
            /*Solo imagen*/
            if(estilo_index==0)
            {
                if(chkConImagen.isSelected()){
                    btnExaminar.setEnabled(true);
                }
                else{            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            if(estilo_index==1){
                if(!chkConImagen.isSelected()){      
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }           
            else if(estilo_index==2){
                if(chkConImagen.isSelected()){
                    btnExaminar.setEnabled(true);
                }
                else{            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            frmConfigurador.configuracion.setVg_con_imagen(chkConImagen.isSelected());
        }
    }//GEN-LAST:event_chkConImagenActionPerformed

    private void cboalgoritmoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboalgoritmoActionPerformed
        frmConfigurador.configuracion.setVg_algoritmo(cboalgoritmo.getSelectedItem().toString());
    }//GEN-LAST:event_cboalgoritmoActionPerformed

    private void cboFuenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFuenteActionPerformed
        //grabarPropiedad(Constantes.STRFUENTE,cboFuente.getSelectedItem().toString());
        frmConfigurador.configuracion.setVg_firma_visible_fuente(cboFuente.getSelectedItem().toString());
    }//GEN-LAST:event_cboFuenteActionPerformed

    private void chkCerrarDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCerrarDocumentoActionPerformed
        //grabarPropiedad(Constantes.BOLCERRARDOCUMENTO,Boolean.toString(chkCerrarDocumento.isSelected()));
        frmConfigurador.configuracion.setVg_cerrar_documento(chkCerrarDocumento.isSelected());
    }//GEN-LAST:event_chkCerrarDocumentoActionPerformed

    private void chkFirmaVisibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFirmaVisibleActionPerformed
        boolean con_firma_visible=chkFirmaVisible.isSelected();
        int estilo_index=cboEstiloFirma.getSelectedIndex();
        
        if(con_firma_visible)
        {
            cboEstiloFirma.setEnabled(true);
            cboFuente.setEnabled(true);
            txtTamanio.setEnabled(true);
            if(estilo_index==0)
            {
                chkConImagen.setEnabled(false);
                chkConImagen.setSelected(true);
            }
            else if(estilo_index==1){
                chkConImagen.setEnabled(false);
                chkConImagen.setSelected(false);
            }
            else{
                chkConImagen.setEnabled(true);
                chkConImagen.setSelected(true);
            }
            
            frmConfigurador.configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            
            if(estilo_index==0)
            {
                chkConImagen.setEnabled(false);
                chkConImagen.setSelected(true);
            }
            else if(estilo_index==1)
            {
                chkConImagen.setEnabled(false);
                chkConImagen.setSelected(false);
            }
            else
            {
                chkConImagen.setEnabled(true);
                chkConImagen.setSelected(true);
            }
            
            frmConfigurador.configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            /*Solo imagen*/
            if(estilo_index==0)
            {
                if(chkConImagen.isSelected())
                {
                    btnExaminar.setEnabled(true);
                }
                else
                {            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            if(estilo_index==1)
            {
                if(!chkConImagen.isSelected())
                {      
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            else if(estilo_index==2)
            {
                if(chkConImagen.isSelected())
                {
                    btnExaminar.setEnabled(true);
                }
                else
                {            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            frmConfigurador.configuracion.setVg_con_imagen(chkConImagen.isSelected());
        }
        else
        {
            chkConImagen.setEnabled(false);
            cboEstiloFirma.setEnabled(false);
            cboFuente.setEnabled(false);
            txtTamanio.setEnabled(false);
            btnExaminar.setEnabled(false);
        }
        
        frmConfigurador.configuracion.setVg_firma_visible(chkFirmaVisible.isSelected());
    }//GEN-LAST:event_chkFirmaVisibleActionPerformed

    private void cboEstiloFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEstiloFirmaActionPerformed
        boolean con_firma_visible=chkFirmaVisible.isSelected();
        int estilo_index=cboEstiloFirma.getSelectedIndex();
        
        if(con_firma_visible)
        {
            if(estilo_index==0)
            {
                chkConImagen.setEnabled(false);
                chkConImagen.setSelected(true);
            }
            else if(estilo_index==1)
            {
                chkConImagen.setEnabled(false);
                chkConImagen.setSelected(false);
            }
            else
            {
                chkConImagen.setEnabled(true);
                chkConImagen.setSelected(true);
            }
            
            frmConfigurador.configuracion.setVg_firma_visible_estilo_firma(estilo_index);
            /*Solo imagen*/
            if(estilo_index==0)
            {
                if(chkConImagen.isSelected())
                {
                    btnExaminar.setEnabled(true);
                }
                else{            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            if(estilo_index==1)
            {
                if(!chkConImagen.isSelected())
                {      
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            else if(estilo_index==2)
            {
                if(chkConImagen.isSelected())
                {
                    btnExaminar.setEnabled(true);
                }
                else
                {            
                    cboEstiloFirma.setSelectedIndex(1);
                    cboEstiloFirma.setEnabled(true);
                    btnExaminar.setEnabled(false);
                }
            }
            frmConfigurador.configuracion.setVg_con_imagen(chkConImagen.isSelected());
        }
    }//GEN-LAST:event_cboEstiloFirmaActionPerformed

    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExaminarActionPerformed
        final JFileChooser elegirimagen = new JFileChooser(txtrutaImagen.getText());
        elegirimagen.setFileSelectionMode(JFileChooser.FILES_ONLY);
        elegirimagen.setAcceptAllFileFilterUsed(false);
        elegirimagen.addChoosableFileFilter( new FileNameExtensionFilter("Image(*.jpeg|*.jpg|*.png|*.gif) ", "jpeg","jpg","gif","png"));
        elegirimagen.setDialogTitle("Seleccione una imagen");
        // elegirimagen.setPreferredSize(new Dimension(getWidth(),getHeight()));
        elegirimagen.setMultiSelectionEnabled(true);
        int opcion=elegirimagen.showOpenDialog(this);
        
        if(opcion==JFileChooser.APPROVE_OPTION)
        {
            txtrutaImagen.setText(elegirimagen.getSelectedFile().getAbsolutePath());
            frmConfigurador.configuracion.setVg_firma_visible_ruta_imagen(txtrutaImagen.getText());
            frmConfigurador.configuracion.setVg_firma_visible_ruta_image_marca_de_agua(txtrutaImagen.getText());
            //grabarPropiedad(Constantes.FILIMAGEN,txtrutaImagen.getText());
        }
    }//GEN-LAST:event_btnExaminarActionPerformed

    private void lblCambiarTamanoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseClicked
        String sentido=lblCambiarTamano.getText();
        if(sentido.equals("<"))
        {
            if(!validarDatos())
                return;
        }
        agrandarFormulario();
    }//GEN-LAST:event_lblCambiarTamanoMouseClicked

    private void lblCambiarTamanoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseEntered
        lblCambiarTamano.setBackground(Color.lightGray);
    }//GEN-LAST:event_lblCambiarTamanoMouseEntered

    private void lblCambiarTamanoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseExited
        //lblCambiarTamano.setBackground(new Color(240,240,240));
        cambiarColor((javax.swing.JLabel)evt.getSource(),new Color(211,111,66),java.awt.Color.white);
    }//GEN-LAST:event_lblCambiarTamanoMouseExited

    private void lblCambiarTamanoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCambiarTamanoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lblCambiarTamanoMouseReleased

    private void ctrl_txt_visor_pagina_actualFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ctrl_txt_visor_pagina_actualFocusGained
        if(ctrl_txt_visor_pagina_actual.getText().trim().length()>0){
            ctrl_txt_visor_pagina_actual.select(0, ctrl_txt_visor_pagina_actual.getText().trim().length());
        }
    }//GEN-LAST:event_ctrl_txt_visor_pagina_actualFocusGained

    private void ctrl_txt_visor_pagina_actualFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ctrl_txt_visor_pagina_actualFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_ctrl_txt_visor_pagina_actualFocusLost

    private void chkDarConstanciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDarConstanciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDarConstanciaActionPerformed

    private void ctrl_lbl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_cerrarMouseClicked
        cerrarVentana();
    }//GEN-LAST:event_ctrl_lbl_cerrarMouseClicked

    private void ctrl_lbl_cerrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_cerrarMouseEntered
        cambiarColor((javax.swing.JLabel)evt.getSource(),Color.red,Color.white);
    }//GEN-LAST:event_ctrl_lbl_cerrarMouseEntered

    private void ctrl_lbl_cerrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctrl_lbl_cerrarMouseExited
        cambiarColor((javax.swing.JLabel)evt.getSource(),new Color(255,153,153),Color.white);
    }//GEN-LAST:event_ctrl_lbl_cerrarMouseExited

    private void btnCargar4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar4MouseEntered

    private void btnCargar4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargar4MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar4MouseExited

    private void btnCargar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargar4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCargar4ActionPerformed

    private void btnGrabarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarCambiosActionPerformed
        if(!validarDatos())
            return;
        
        if(!frmConfigurador.configuracion.isVg_firma_visible())
        {
            frmConfigurador.configuracion.setVg_firma_visible_x(0);
            frmConfigurador.configuracion.setVg_firma_visible_y(0);
            frmConfigurador.configuracion.setVg_firma_visible_x1(0);
            frmConfigurador.configuracion.setVg_firma_visible_y1(0);
            frmConfigurador.configuracion.setVg_firma_visible_alto_base(0);
            frmConfigurador.configuracion.setVg_firma_visible_ancho_base(0);
            
            frmConfigurador.configuracion.setVg_firma_visible_xp(0);
            frmConfigurador.configuracion.setVg_firma_visible_yp(0);
            frmConfigurador.configuracion.setVg_firma_visible_x1p(0);
            frmConfigurador.configuracion.setVg_firma_visible_y1p(0);
        }
        else
        {
            Rectangle rect = cPanel.getRectangle();
            float y22, x22;
            float x11 = (float)rect.getX();
            float y11 = (float)rect.getY();

            float ancho0 = (float)rect.getWidth();
            float alto0 = (float)rect.getHeight();
            
            frmConfigurador.configuracion.setVg_firma_visible_xp(x11);
            frmConfigurador.configuracion.setVg_firma_visible_yp(y11);
            frmConfigurador.configuracion.setVg_firma_visible_x1p(x11 + ancho0);
            frmConfigurador.configuracion.setVg_firma_visible_y1p(y11 + alto0);

            if(cPanel.getRotacionPagina(pagina - 1) == 0 || cPanel.getRotacionPagina(pagina - 1) == 180)
            {
                x11 = x11 * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth();
                y11 = y11 * cPanel.getAltoReal(pagina - 1) / cPanel.getHeight();
                y11 = cPanel.getAltoReal(pagina - 1) - y11 - alto0 * (cPanel.getAltoReal(pagina - 1)) / cPanel.getHeight();

                y22 = alto0 * cPanel.getAltoReal(pagina - 1) / cPanel.getHeight();
                x22 = ancho0 * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth();
                x11 += (scrollPane.getHorizontalScrollBar().getValue() * cPanel.getAnchoReal(pagina - 1) / cPanel.getWidth());
                y11 -= (scrollPane.getVerticalScrollBar().getValue() * (cPanel.getAltoReal(pagina - 1)) / cPanel.getHeight());
            }
            else
            {
                x11 = x11 * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth();
                y11 = y11 * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight();
                y11 = cPanel.getAnchoReal(pagina - 1) - y11  - alto0 * (cPanel.getAnchoReal(pagina - 1))/ cPanel.getHeight();

                y22 = alto0 * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight();
                x22 = ancho0 * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth();
                x11 += (scrollPane.getHorizontalScrollBar().getValue() * cPanel.getAltoReal(pagina - 1) / cPanel.getWidth());
                y11 -= (scrollPane.getVerticalScrollBar().getValue() * cPanel.getAnchoReal(pagina - 1) / cPanel.getHeight());
            }

            y22 += y11;
            x22 += x11;

            frmConfigurador.configuracion.setVg_firma_visible_ancho_base(cPanel.getAnchoReal(pagina - 1));
            frmConfigurador.configuracion.setVg_firma_visible_alto_base(cPanel.getAltoReal(pagina - 1));
            frmConfigurador.configuracion.setVg_firma_visible_x(x11);
            frmConfigurador.configuracion.setVg_firma_visible_y(y11);
            frmConfigurador.configuracion.setVg_firma_visible_x1(x22);
            frmConfigurador.configuracion.setVg_firma_visible_y1(y22);
        }
        
        frmConfigurador.configuracion.setVg_firma_visible(chkFirmaVisible.isSelected());
        frmConfigurador.configuracion.setVg_con_imagen(chkConImagen.isSelected());
        frmConfigurador.configuracion.setVg_firma_visible_ruta_imagen(txtrutaImagen.getText());
        frmConfigurador.configuracion.setVg_firma_visible_ruta_image_marca_de_agua(txtrutaImagen.getText());
        frmConfigurador.configuracion.setVg_firma_visible_estilo_firma(cboEstiloFirma.getSelectedIndex());
        frmConfigurador.configuracion.setVg_firma_visible_fuente(cboFuente.getSelectedItem().toString());
        frmConfigurador.configuracion.setVg_firma_visible_tamanio_fuente(Float.parseFloat(txtTamanio.getText()));
        frmConfigurador.configuracion.setVg_cerrar_documento(chkCerrarDocumento.isSelected());
        frmConfigurador.configuracion.setVg_firma_visible_firmar_todas_paginas(chkFirmarTodasPaginas.isSelected());
        frmConfigurador.configuracion.setConNumeroCal(chkCalNumber.isSelected());
        frmConfigurador.configuracion.setConCarmpoArea(chkCampoArea.isSelected());
        frmConfigurador.configuracion.setVg_texto_firmante(txtTextoFIrmante.getText());
        
        try(FileOutputStream foss = new FileOutputStream(new File(CConstantes.RUTAPROPIEDADES)))
        {
            Properties properties = new Properties();
            properties.setProperty(CConstantes.BOLVISIBLE, "" + frmConfigurador.configuracion.isVg_firma_visible());
            properties.setProperty(CConstantes.INTESTILO, "" + frmConfigurador.configuracion.getVg_firma_visible_estilo_firma());
            properties.setProperty(CConstantes.BOLIMAGEN, "" + frmConfigurador.configuracion.isVg_con_imagen());
            properties.setProperty(CConstantes.FILIMAGEN, "" + frmConfigurador.configuracion.getVg_firma_visible_ruta_imagen());
            properties.setProperty(CConstantes.FILIMAGENMARCAAGUA, "" + frmConfigurador.configuracion.getVg_firma_visible_ruta_image_marca_de_agua());
            properties.setProperty(CConstantes.STRFUENTE, "" + frmConfigurador.configuracion.getVg_firma_visible_fuente());
            properties.setProperty(CConstantes.INTTAMANOFUENTE, "" + frmConfigurador.configuracion.getVg_firma_visible_tamanio_fuente());
            properties.setProperty(CConstantes.BOLTODASPAGINAS, "" + frmConfigurador.configuracion.isVg_firma_visible_firmar_todas_paginas());
            properties.setProperty(CConstantes.BOLTSA, "" + frmConfigurador.configuracion.isVg_con_sello());
            //properties.setProperty(CConstantes.URLTSA, txtTsaUrl.getText());
           // properties.setProperty(CConstantes.URLTSAUSUARIO, txtTsaUsuario.getText());
            //properties.setProperty(CConstantes.HSHCLAVETSA, new helperClass().encrypt(new String(txtTsaClave.getPassword())));
            properties.setProperty(CConstantes.BOLCERRARDOCUMENTO, "" + frmConfigurador.configuracion.isVg_cerrar_documento());
            properties.setProperty(CConstantes.STRALGORITMO, "" + frmConfigurador.configuracion.getVg_algoritmo());
            properties.setProperty(CConstantes.BOLTSL, "" + frmConfigurador.configuracion.isVg_validar_con_tsl());
            //properties.setProperty(CConstantes.URLTSL, txtTslUrls.getText());
            properties.setProperty(CConstantes.BOLREPUDIO, "" + frmConfigurador.configuracion.isVg_validar_con_no_repudio());
            properties.setProperty(CConstantes.BOL_CAL, "" + frmConfigurador.configuracion.getConNumeroCal());
            properties.setProperty(CConstantes.BOL_AREA, "" + frmConfigurador.configuracion.getConCarmpoArea());
            
            properties.setProperty(CConstantes.INTX, "" + frmConfigurador.configuracion.getVg_firma_visible_x());
            properties.setProperty(CConstantes.INTY, "" + frmConfigurador.configuracion.getVg_firma_visible_y());
            properties.setProperty(CConstantes.FLTANCHO, "" + frmConfigurador.configuracion.getVg_firma_visible_x1());
            properties.setProperty(CConstantes.FLTALTO, "" + frmConfigurador.configuracion.getVg_firma_visible_y1());
            
            properties.setProperty(CConstantes.INTXp, "" + frmConfigurador.configuracion.getVg_firma_visible_xp());
            properties.setProperty(CConstantes.INTYp, "" + frmConfigurador.configuracion.getVg_firma_visible_yp());
            properties.setProperty(CConstantes.FLTANCHOp, "" + frmConfigurador.configuracion.getVg_firma_visible_x1p());
            properties.setProperty(CConstantes.FLTALTOp, "" + frmConfigurador.configuracion.getVg_firma_visible_y1p());
            properties.setProperty(CConstantes.INTPAGINA, "" + frmConfigurador.configuracion.getVg_firma_visible_pagina());
            properties.setProperty(CConstantes.INTTAMANOFUENTE, "" + frmConfigurador.configuracion.getVg_firma_visible_tamanio_fuente());
            properties.setProperty(CConstantes.TextoFirmante, "" + frmConfigurador.configuracion.getVg_texto_firmante());
            properties.store(foss, CConstantes.APLICACION_NOMBRE);
            foss.close();
        }
        catch(Exception e) 
        {
            CConstantes.dialogo("Ha ocurrido un error: " + e.getMessage());
        };
        
        cerrarVentana();
    }//GEN-LAST:event_btnGrabarCambiosActionPerformed

    private void chkCalNumberActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkCalNumberActionPerformed
    {//GEN-HEADEREND:event_chkCalNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCalNumberActionPerformed

    private void chkCampoAreaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkCampoAreaActionPerformed
    {//GEN-HEADEREND:event_chkCampoAreaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCampoAreaActionPerformed

    private void txtTamanioFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtTamanioFocusGained
    {//GEN-HEADEREND:event_txtTamanioFocusGained
        frmConfigurador.configuracion.setVg_firma_visible_tamanio_fuente(CConstantes.Yconvertiranumerofloat(txtTamanio.getText()));
    }//GEN-LAST:event_txtTamanioFocusGained

    private void txtTamanioFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtTamanioFocusLost
    {//GEN-HEADEREND:event_txtTamanioFocusLost
        frmConfigurador.configuracion.setVg_firma_visible_tamanio_fuente(CConstantes.Yconvertiranumerofloat(txtTamanio.getText()));
    }//GEN-LAST:event_txtTamanioFocusLost

    private void txtTextoFIrmanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTextoFIrmanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTextoFIrmanteActionPerformed

   
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (paginas == 0)
            return;
        
        boolean b = false;
        
        if (e.getSource() == btnPaginaSiguiente)
        {
            pagina += 1;
            if (pagina > paginas || pagina < 1)
                pagina = paginas;
            
            b = true;
        }
        
        if (e.getSource() == btnPaginaAnterior)
        {
            pagina -= 1;
            if (pagina > paginas || pagina < 1)
                pagina = 1;
            
            b = true;
        }
        
        if (e.getSource() == btnPrimeraPagina)
        {
            pagina = 1; 
            b = true;
        }
        
        if (e.getSource() == btnPaginaUltima)
        {
            pagina=paginas;
            b = true;
        }
        
        if(b)
        {
            viewPage();
            frmConfigurador.configuracion.setVg_firma_visible_pagina(pagina); 
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCargar4;
    private javax.swing.JButton btnExaminar;
    private javax.swing.JButton btnGrabarCambios;
    public javax.swing.JButton btnPaginaAnterior;
    public javax.swing.JButton btnPaginaSiguiente;
    public javax.swing.JButton btnPaginaUltima;
    public javax.swing.JButton btnPrimeraPagina;
    private javax.swing.JButton btn_logo;
    protected static javax.swing.JComboBox cboEstiloFirma;
    protected static javax.swing.JComboBox cboFuente;
    protected static javax.swing.JComboBox cboalgoritmo;
    public static javax.swing.JCheckBox chkCalNumber;
    public static javax.swing.JCheckBox chkCampoArea;
    protected static javax.swing.JCheckBox chkCerrarDocumento;
    protected static javax.swing.JCheckBox chkConImagen;
    protected static javax.swing.JCheckBox chkDarConstancia;
    protected static javax.swing.JCheckBox chkFirmaVisible;
    protected static javax.swing.JCheckBox chkFirmarTodasPaginas;
    public javax.swing.JButton ctrl_btn_visor_cargar_documento;
    private javax.swing.JLabel ctrl_lbl_cerrar;
    private javax.swing.JLabel ctrl_lbl_documentos_contador;
    private javax.swing.JTextField ctrl_txt_visor_pagina_actual;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCambiarTamano;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEtTotalPaginas;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JPanel pnlBarra6;
    private javax.swing.JPanel pnlConfiguracion;
    private javax.swing.JPanel pnlDocumento;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JTextField txtPaginas;
    protected static javax.swing.JTextField txtTamanio;
    private javax.swing.JTextField txtTextoFIrmante;
    protected static javax.swing.JTextField txtrutaImagen;
    // End of variables declaration//GEN-END:variables
}
