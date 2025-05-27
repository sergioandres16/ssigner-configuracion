package Entidades;

import javax.swing.JOptionPane;

public class Constantes
{
    /*CODIGO DE MENSAJE*/
    public static final String X_TRUE="TRUE";
    public static final String X_FALSE="FALSE";
    public static final String X_AVISO="0";
    public static final String X_RUTA_CACHE="";
    public static final String X_CADENA_DIVISION_DEFAULT=">";
    public static final String X_CANTIDAD_PARAMETROS_INCORRECTO="1";
    public static final String X_PARAMETRO_CON_FORMATO_INCORRECTO="2";
    public static final String X_ARCHIVO_NO_EXISTE="3";
    public static final String X_NO_SE_ENCONTRO_ALIAS="4";
    public static final String X_NO_SE_ENCONTRO_RUTA_CRL="5";
    public static final String X_NO_PUDO_DESCARGAR_CRL="6";
    public static final String X_NO_PUDO_VALIDAR_CRL="7";    
    public static final String X_NO_PUDO_DESCARGAR_TSL="8";
    public static final String X_NO_PUDO_DESCARGAR_ARCHIVO="9";
    public static final String X_CERTIFICADO_EXPIRADO="10";
    public static final String X_CERTIFICADO_REVOCADO="11";
    public static final String X_CERTIFICADO_NOTSL="12";
    public static final String X_CERTIFICADO_NOESNOREPUDIO="13";
    public static final String X_ALIAS_CERTIFICADO_NOENCONTRADO="14";
    public static final String X_CRL_FECHA_INCORRECTA="15";
    public static final String X_CERTIFICADO_FUERA_PERIODO="16";
    public static final String X_CERTIFICADO_MARCADEUSOINVALIDA="17";
    public static final String X_NO_SE_PUDO_ELIMINAR_ARCHIVO="18";    
    public static final String X_CRL_CORRUPTA="19";
    public static final String X_RUTA_DE_SALIDA_DE_PDFS="20";
    public static final String X_ERROR_NO_CONTROLADO="100";

    
    /*FORMATO FECHAS*/
    public static final String FORMATOFECHA_DD_MMYYYY="dd-MM-yyyy";
    public static final String FORMATOFECHA_DD_MMYYYY_HH_MM_SS="dd-MM-yyyy HH:mm:ss";
    public static final String FORMATOFECHA_DD_MMYYYY_HH_MM__SS_AA="dd-MM-yyyy HH:mm:ss aa";
    
    /*PARAMETRO PROPIEDADES DEL CERTIFICADO*/
    public static final String CN="cn";
    public static final String CARGOTITLE="title";
    public static final String CARGOT="t";
    public static final String O="o";
    public static final String OU="ou";
    public static final String EMAILE="E";
    
    /*CARACTERES*/
     public static final String GUION="-";
     public static final String VACIO="";
     
    /*MENSAJES*/
    public static final String FIRMANDODOCUMENTO="Firmando documento..."; 
    public static final String VALIDANDOCERTIFICADO="VALIDANDO CERTIFICADO"; 
    public static final String MSG_ERROR_CLAVE="Ingrese la clave por favor";
    public static final String CERTIFICADO_NO_VALIDO="CERTIFICADO NO VÁLIDO";
    
    /*METODO MENSAJE*/
    public static void msgInformacion(String msg)
    {
        JOptionPane.showMessageDialog(null, msg);
    }
    
    public static Integer Yconvertiranumero(String numero)
    {
	try
        {
            Integer valor= Integer.parseInt(numero);
            return valor;
	}
        catch (NumberFormatException nfe)
        {
            return 0;
	}
    }
    
    public static float Yconvertiranumerofloat(String numero)
    {
	try
        {
            Integer valor= Integer.parseInt(numero);
            return valor;
	}
        catch (NumberFormatException nfe)
        {
            return 0;
	}
    }
    
    public static String YconvertiranumeroBooleanoString(String p_valor)
    {
	try
        {
            Boolean v_valor= Boolean.parseBoolean(p_valor);
            return v_valor+"";
	}
        catch (NumberFormatException nfe)
        {
            return X_FALSE;
	}
    }
    
    public  static final String ALTO="alto";
    public  static final String ANCHO="ancho";
    public  static final String X="x";
    public  static final String Y="y";
    
    /*public enum CLIENTE 
    {
        APJ,
        Otros;
    }*/
}   

