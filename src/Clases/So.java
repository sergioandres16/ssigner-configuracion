/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Global.CConstantes;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuario
 */
public final class So {
private int index=0;
    private String vg_extension;
    private String vg_ruta_user_home;
    private final String vg_Separador= File.separator;
    private String vg_nombre_sistema_operativo;
    private final String[] vg_sistema_operativo=new String[]{"Win","Windows XP","Lin","Mac","XP"};
    private final String[] vg_propiedades_sistema_operativo=new String[]{
        "os.name",          //0
        "os.arch",          //1
        "os.version",       //2
        "user.home",         //3
        "file.separator",   //4
        "java.home",        //5
        "java.version",     //6 
        "user.home"         //7
    };  
     private final  String[]  RutArhdll=new String[]{System.getProperty("user.home")+"/"+CConstantes.APLICACION_NOMBRE+"/"+CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA+"/",
    System.getProperty("user.home")+"/"+CConstantes.APLICACION_NOMBRE+"/"+CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA+"/",
    System.getProperty("user.home")+"/"+CConstantes.APLICACION_NOMBRE+"/"+CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA+"",
    System.getProperty("user.home")+"/"+CConstantes.APLICACION_NOMBRE+"/"+CConstantes.APLICACION_LIBRERIAS_TOKEN_CARPETA+""
    };
    
    public So(){
        String so = ObtenerSistemaOperativo(); 
        vg_nombre_sistema_operativo=so;
        if(so.startsWith(vg_sistema_operativo[0])){
            if(so.startsWith(vg_sistema_operativo[1])){
                index=1;
            }
            else{
                index=0;
            }
            vg_extension=".dll";
        }
        else if(so.startsWith(vg_sistema_operativo[2])){
            index=2;
            vg_extension=".so";
        }
        else if(so.startsWith(vg_sistema_operativo[3])){
            index=3;
            vg_extension=".cfg";
        }
    }
    //OBTENGO EL SISTEMA OPERATIVO
    public String ObtenerSistemaOperativo(){
         return System.getProperty(vg_propiedades_sistema_operativo[0]);
    }
    //OBTENGO EL SISTEMA OPERATIVO
    public String ObtenerSistemaOperativo_arquitectura(){
         return System.getProperty(vg_propiedades_sistema_operativo[1]);
    }
        //OBTENGO EL SISTEMA OPERATIVO
    public String ObtenerSistemaOperativo_version(){
         return System.getProperty(vg_propiedades_sistema_operativo[2]);
    }
        //OBTENGO EL SISTEMA OPERATIVO
    public String ObtenerSistemaOperativo_user_home(){
         return System.getProperty(vg_propiedades_sistema_operativo[3]);
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getVg_extension() {
        return vg_extension;
    }

    public void setVg_extension(String vg_extension) {
        this.vg_extension = vg_extension;
    }

    public String getVg_ruta_user_home() {
        return vg_ruta_user_home;
    }

    public void setVg_ruta_user_home(String vg_ruta_user_home) {
        this.vg_ruta_user_home = vg_ruta_user_home;
    }

    public String getVg_Separador() {
        return vg_Separador;
    }

    public String[] getVg_sistema_operativo() {
        return vg_sistema_operativo;
    }

    public String[] getVg_propiedades_sistema_operativo() {
        return vg_propiedades_sistema_operativo;
    } 
    
    public List<Libreria> ObtenerDlls(){
        File file=new File(RutArhdll[index]);
        File[] files=file.listFiles();
        Libreria dlls=new Libreria();
        List<Libreria> ltfiles =new ArrayList<Libreria>();
        if(files!=null){
            for(File f:files){
                String nombrearchivo= f.getName();
                String rutaarchivo= f.getPath();
                if(nombrearchivo.endsWith(vg_extension)){
                    dlls.setNombre(nombrearchivo);
                    dlls.setRuta(rutaarchivo);
                }
            }
        }
        return ltfiles;
    }
        //OBTENGO LA RUTA DE LA LICENCIA
    public String ObtenerRutadll(){
         File file=new File( RutArhdll[index]);
         return Validacion(file);
    }
    
       //METODO PARA VALIDAR EXISTENCIA DEL ARCHIVO
    public  String Validacion(File file){
        try {
            return file.getCanonicalPath();
        } catch (IOException ex) {
            //JOptionPane.showMessageDialog(null, ex);
            return "";
        }
     }

    public String getVg_nombre_sistema_operativo() {
        return vg_nombre_sistema_operativo;
    }

    public void setVg_nombre_sistema_operativo(String vg_nombre_sistema_operativo) {
        this.vg_nombre_sistema_operativo = vg_nombre_sistema_operativo;
    }
    
}
