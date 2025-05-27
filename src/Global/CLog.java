package Global;

import Clases.So;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CLog {
    public final String Y_ERROR="ERROR";
    public final String Y_AVISO="AVISO";
    public final String Y_TODO="TODO";
    private static String mensaje_error="";
    public void archivar_log(String p_detalle,boolean p_escribir_log){
        archivar_log(new So().ObtenerSistemaOperativo_user_home(),"",p_detalle,p_escribir_log,false,true);
    }
    public void archivar_log(String p_detalle){
        archivar_log(new So().ObtenerSistemaOperativo_user_home(),p_detalle);
    }
    public void archivar_log(String p_log_carpeta_origen_ruta,String p_detalle)//String ytipo,String cadena,
    {
        archivar_log(p_log_carpeta_origen_ruta,p_detalle,true);
    }
    public void archivar_log(String p_log_carpeta_origen_ruta,String p_detalle,boolean p_escribir_log){
        archivar_log(p_log_carpeta_origen_ruta,p_detalle,p_escribir_log,false);
    }
    public void archivar_log(String p_log_carpeta_origen_ruta,String p_detalle,boolean p_escribir_log,boolean con_fecha){
        archivar_log(p_log_carpeta_origen_ruta,p_detalle,p_escribir_log,con_fecha,true);
    }
    public void archivar_log(String p_log_carpeta_origen_ruta,String p_detalle,boolean p_escribir_log,boolean con_fecha,boolean con_salto_linea){
        archivar_log(p_log_carpeta_origen_ruta,"",p_detalle,p_escribir_log,con_fecha,con_salto_linea);
    }
  
    public void archivar_log(String p_log_carpeta_origen_ruta,String p_codigo, String p_detalle,boolean p_escribir_log,boolean con_fecha,boolean con_salto_linea){
        try
        {
            if (p_escribir_log)
            {
                mensaje_error=p_detalle;
                /*Formatedo la fecha para el nombre del log*/
                Format formatter = new SimpleDateFormat("yyyy.MM.dd");
                Date now = new Date();
                String archivo_nombre = "SSigner Configurador_" + formatter.format(now);
                if(!p_log_carpeta_origen_ruta.endsWith(File.separator) || !p_log_carpeta_origen_ruta.endsWith("/")){
                    p_log_carpeta_origen_ruta =  p_log_carpeta_origen_ruta + File.separator;
                }
                File carpeta_archivo=new File(p_log_carpeta_origen_ruta+CConstantes.APLICACION_LOG_CARPETA);
                carpeta_archivo.mkdir();
                FileWriter fw = new FileWriter(p_log_carpeta_origen_ruta + archivo_nombre +".log",true);
                p_detalle = (p_codigo!=null && !p_codigo.trim().equals("")) ? p_codigo.trim() + CConstantes.SEPARADOR + p_detalle : p_detalle;
                if(con_fecha)
                    p_detalle = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + p_detalle;
                if(con_salto_linea)
                    p_detalle = p_detalle + CConstantes.SALTO_LINEA;
                fw.write(p_detalle);
                fw.close();
            }
        }
        catch(Exception ex)
        {
            System.out.println("Error al escribir en el archivo LOG>"+ex.toString()+"\n");
        }
    }
    
    public static String getMensaje_error() {
        return mensaje_error;
    }

    public static void setMensaje_error(String aMensaje_error) {
        mensaje_error = aMensaje_error;
    }
}