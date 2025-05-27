package Global;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class CConstantes {

    public static final String X_EXCEPCION = "-1";
    public static final String X_ERROR = "1";
    public static final String X_OK = "0";
    public static final String X_INFORMACION = "2";
    public static final String X_ADVERTENCIA = "3";
    public static final String SALTO_LINEA = "\r\n";
    public static final String SEPARADOR = "|";

    public static String APLICACION_NOMBRE = "SSigner";
    public static final String APLICACION_LOG_CARPETA = "log";
    public static final String APLICACION_PROPIEDADES_CARPETA = "propiedades";
    public static final String APLICACION_TEMPORAL_CARPETA = "temporal";
    public static final String APLICACION_FIRMADOS_CARPETA = "firmados";
    public static final String APLICACION_RECHAZADOS_CARPETA = "rechazados";
    public static final String APLICACION_CACHE_CARPETA = "cache";
    public static final String APLICACION_LIBRERIAS_TOKEN_CARPETA = "librerias";
    public static final String APLICACION_LICENCIA_CARPETA = "licencia";
    public static final String FORMATOFECHA_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String FORMATOFECHA_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public static final String FORMATOFECHA_DD_MM_YYYY_HH_MM__SS_AA = "dd-MM-yyyy HH:mm:ss aa";
    private Map errores = null;
    public String RutaCacheTSL;
    //Variables o parametros de la aplicacion
    public final String VAR1_TIPO_REPOSITORIO = "tipo_repositorio";
    public final String VAR2_FILE_KEYSTORE = "file_keystore";
    public final String VAR3_PIN_KEYSTORE = "pin_keystore";
    public final String VAR4_ALIAS_CERTIFICADO = "alias_certificado";
    public final String VAR5_RUTA_PDF = "ruta_pdf";
    public final String VAR6_RUTA_IMAGEN = "ruta_imagen";
    public final String VAR7_XFIRMA = "xfirma";
    public final String VAR8_YFIRMA = "yfirma";
    public final String VAR9_ALTOFIRMA = "altofirma";
    public final String VAR10_ANCHOFIRMA = "anchofirma";
    public final String VAR11_TAMANHO_LETRA_TEXTO_FIRMA = "tamanho_letra_firma";
    public final String VAR12_TEXTO_FIRMA_ARRIBA = "texto_arriba";
    public final String VAR13_TEXTO_FIRMA_LADO = "texto_lado";
    public final String VAR14_TEXTO_FIRMA_ABAJO = "texto_abajo";
    public final String VAR15_FIRMA_EN_NROPAGINA = "firma_nropagina";
    public final String VAR16_FIRMA_VISIBLE = "visible";
    public final String VAR17_RUTA_TSL = "ruta_tsl";
    public final String VAR18_RUTA_CRL = "ruta_crl";
    public final String VAR19_RUTA_AAC = "ruta_aac";
    public final String VAR20_FECHA_HORA = "fecha_hora";
    public final String VAR21_TIPO_LOG = "tipo_log";
    public final String VAR22_TEXTO_TITULO_FIRMA = "titulo_firma";
    public final String VAR23_EXTENSION_PDF = "extension_pdf";
    public final String VAR24_DEJAR_FIRMAR_POSTERIORMENTE = "firma_posterior";
    public final String VAR25_VERIFICAR_NOREPUDIO = "verificarnorepudio";
    public final String VAR26_CACHE_TSL = "cache_tsl";
    public final String VAR27_CACHE_CRL = "cache_crl";
    public final String VAR28_POSICION_FIRMA_RELATIVA = "posicion_firmarelativa";
    public final String VAR29_TIPO_OPERACION_BIBLIOTECA = "OPERACION_BIBLIOTECA";
    public final String VAR30_SEPARADOR_CADENA = "SEPARADOR_CADENA";
    public final String VAR31_RUTA_TSA = "ruta_tsa";
    public final String VAR32_USER_TSA = "user_tsa";
    public final String VAR33_PWD_TSA = "pwd_tsa";
    public final String VAR34_RUTA_CARPETA_DE_SALIDA = "RUTA_DE_CARPETA_DE_SALIDA";
    public final String VAR35_LOCALIDAD_DE_FIRMANTE = "LOCALIDAD_DE_FIRMANTE";
    public final String VAR36_VARIAS_FIRMAS = "VARIAS_FIRMAS";
    public final String VAR42_TIPO_FIRMA_VISIBLE = "VARIAS_FIRMAS";
    //Constantes para validar parametros
    public final String Z_True_False = ",TRUE,FALSE,";
    public final String Z_Todo_Error = ",TODO,ERROR,";
    public final String Z_Tipo_Repositorio = ",PKCS12,PKCS11,WINDOWS-MY,";

    //Codigo de errores
//    public static final String X_ERROR="-1";
//    public static final String X_OK="0";
    //Las dos variables que siguen estan enlazadas con Z_True_False
    public static String X_TRUE = "TRUE";
    public static String X_FALSE = "FALSE";
    public final String X_AVISO = "0";
    public final String X_RUTA_CACHE = RutaCacheTSL;
    public final String X_CADENA_DIVISION_DEFAULT = ">";
    public final String X_CANTIDAD_PARAMETROS_INCORRECTO = "1";
    public final String X_PARAMETRO_CON_FORMATO_INCORRECTO = "2";
    public final String X_ARCHIVO_NO_EXISTE = "3";
    public final String X_NO_SE_ENCONTRO_ALIAS = "4";
    public final String X_NO_SE_ENCONTRO_RUTA_CRL = "5";
    public final String X_NO_PUDO_DESCARGAR_CRL = "6";
    public final String X_NO_PUDO_VALIDAR_CRL = "7";
    public final String X_NO_PUDO_DESCARGAR_TSL = "8";
    public final String X_NO_PUDO_DESCARGAR_ARCHIVO = "9";
    public final String X_CERTIFICADO_EXPIRADO = "10";
    public final String X_CERTIFICADO_REVOCADO = "11";
    public final String X_CERTIFICADO_NOTSL = "12";
    public final String X_CERTIFICADO_NOESNOREPUDIO = "13";
    public final String X_ALIAS_CERTIFICADO_NOENCONTRADO = "14";
    public final String X_CRL_FECHA_INCORRECTA = "15";
    public final String X_CERTIFICADO_FUERA_PERIODO = "16";
    public final String X_CERTIFICADO_MARCADEUSOINVALIDA = "17";
    public final String X_NO_SE_PUDO_ELIMINAR_ARCHIVO = "18";
    public final String X_CRL_CORRUPTA = "19";
    public final String X_RUTA_DE_SALIDA_DE_PDFS = "20";
    public final String X_CERTIFICADO_ADULTERADO = "21";
    public final String X_CERTIFICADO_CRL_ERRADA = "22";
    public final String X_ERROR_NO_CONTROLADO = "100";
    public final String RETORNO = "\n";
    public String RutaCacheCRL;
    public final String firmanteElegido = "firmante.elegido";
    

    public CConstantes()
    {
        errores = new HashMap();
        //Asginar los errores
        cargar_base_datos_errores();
    }

    public CConstantes(String RutaCacheCRL) {
        this.RutaCacheCRL = RutaCacheCRL;
    }

    public CConstantes(String RutaCacheCRL, String RutaCacheTSL) {
        this.RutaCacheCRL = RutaCacheCRL;
        this.RutaCacheTSL = RutaCacheTSL;
    }

//    private void cargar_base_datos_errores()
//    {
//        errores.put(X_OK, "OK");
//        errores.put(X_AVISO, "Cod:"+X_AVISO+">Aviso");
//        errores.put(X_RUTA_CACHE, "Cache");
//        errores.put(X_CANTIDAD_PARAMETROS_INCORRECTO, "Cod:"+X_CANTIDAD_PARAMETROS_INCORRECTO+">La cantidad de parametros ingresados son incorrectos");
//        errores.put(X_PARAMETRO_CON_FORMATO_INCORRECTO, "Cod:"+X_PARAMETRO_CON_FORMATO_INCORRECTO+">Uno de los parametros que paso al modulo se encuentra en formato incorrecto");
//        errores.put(X_ARCHIVO_NO_EXISTE, "Cod:"+X_ARCHIVO_NO_EXISTE+">El archivo indicado no existe");
//        errores.put(X_NO_SE_ENCONTRO_ALIAS, "Cod:"+X_NO_SE_ENCONTRO_ALIAS+">El alias indicado no existe");
//        errores.put(X_NO_SE_ENCONTRO_RUTA_CRL, "Cod:"+X_NO_SE_ENCONTRO_RUTA_CRL+">No se encontro la ruta de la CRL");
//        errores.put(X_NO_PUDO_DESCARGAR_CRL, "Cod:"+X_NO_PUDO_DESCARGAR_CRL+">No se pudo descargar la CRL");
//        errores.put(X_NO_PUDO_VALIDAR_CRL, "Cod:"+X_NO_PUDO_VALIDAR_CRL+">No se pudo validar la CRL");
//        errores.put(X_NO_PUDO_DESCARGAR_TSL, "Cod:"+X_NO_PUDO_DESCARGAR_TSL+">No se pudo descargar la TSL");
//        errores.put(X_NO_PUDO_DESCARGAR_ARCHIVO, "Cod:"+X_NO_PUDO_DESCARGAR_ARCHIVO+">No se pudo descargar el archivo");
//        errores.put(X_CERTIFICADO_EXPIRADO, "Cod:"+X_CERTIFICADO_EXPIRADO+">Certificado expirado");
//        errores.put(X_CERTIFICADO_REVOCADO, "Cod:"+X_CERTIFICADO_REVOCADO+">Certificado revocado");
//        errores.put(X_CERTIFICADO_NOTSL, "Cod:"+X_CERTIFICADO_NOTSL+">Certificado no se encuentra en la TSL");
//        errores.put(X_CERTIFICADO_NOESNOREPUDIO, "Cod:"+X_CERTIFICADO_NOESNOREPUDIO+">Certificado no fue identificada como no repudio");
//        errores.put(X_CERTIFICADO_MARCADEUSOINVALIDA, "Cod:"+X_CERTIFICADO_MARCADEUSOINVALIDA+">Certificado no puede ser utilizada para firma digital");
//        errores.put(X_CRL_FECHA_INCORRECTA, "Cod:"+X_CRL_FECHA_INCORRECTA+">Fecha de la CRL no es valida");
//        errores.put(X_CERTIFICADO_FUERA_PERIODO, "Cod:"+X_CERTIFICADO_FUERA_PERIODO+">Certificado fuera de periodo valido");
//        errores.put(X_NO_SE_PUDO_ELIMINAR_ARCHIVO, "Cod:"+X_NO_SE_PUDO_ELIMINAR_ARCHIVO+">No se pudo eliminar archivo");
//        errores.put(X_RUTA_DE_SALIDA_DE_PDFS, "Cod:"+X_RUTA_DE_SALIDA_DE_PDFS+">La ruta de carpeta de salida no es valido");
//   
//    }
    private void cargar_base_datos_errores() {
        errores.put(X_OK, "OK");
        errores.put(X_AVISO, "Cod:" + X_AVISO + ">Aviso");
        errores.put(X_RUTA_CACHE, "Cache");
        errores.put(X_CANTIDAD_PARAMETROS_INCORRECTO, "La cantidad de parametros ingresados son incorrectos");
        errores.put(X_PARAMETRO_CON_FORMATO_INCORRECTO, "Uno de los parametros que paso al modulo se encuentra en formato incorrecto");
        errores.put(X_ARCHIVO_NO_EXISTE, "El archivo indicado no existe");
        errores.put(X_NO_SE_ENCONTRO_ALIAS, "El alias indicado no existe");
        errores.put(X_NO_SE_ENCONTRO_RUTA_CRL, "No se encontro la ruta de la CRL");
        errores.put(X_NO_PUDO_DESCARGAR_CRL, "No se pudo descargar la CRL");
        errores.put(X_NO_PUDO_VALIDAR_CRL, "No se pudo validar la CRL");
        errores.put(X_NO_PUDO_DESCARGAR_TSL, "No se pudo descargar la TSL");
        errores.put(X_NO_PUDO_DESCARGAR_ARCHIVO, "No se pudo descargar el archivo");
        errores.put(X_CERTIFICADO_EXPIRADO, "Certificado expirado");
        errores.put(X_CERTIFICADO_ADULTERADO, "El certificado ha sido adulterado");
        errores.put(X_CERTIFICADO_CRL_ERRADA, "La CRL está mal firmada");
        errores.put(X_CERTIFICADO_REVOCADO, "Certificado revocado");
        errores.put(X_CERTIFICADO_NOTSL, "Certificado no se encuentra en la TSL");
        errores.put(X_CERTIFICADO_NOESNOREPUDIO, "Certificado no fue identificada como no repudio");
        errores.put(X_CERTIFICADO_MARCADEUSOINVALIDA, "Certificado no puede ser utilizada para firma digital");
        errores.put(X_CRL_FECHA_INCORRECTA, "Fecha de la CRL no es valida");
        errores.put(X_CERTIFICADO_FUERA_PERIODO, "Certificado fuera de periodo valido");
        errores.put(X_NO_SE_PUDO_ELIMINAR_ARCHIVO, "No se pudo eliminar archivo");
        errores.put(X_RUTA_DE_SALIDA_DE_PDFS, "La ruta de carpeta de salida no es valido");

    }
    public final String SitioWeb = "http://www.saeta.pe";

    public String Yget_mensaje_error(String numerror) {
        return (String) errores.get(numerror);
    }

    public String YCadena_onull(String cadena) {
        String retorno = null;
        if (cadena.trim().length() > 0) {
            retorno = cadena.trim();
        }
        return retorno;
    }

    public boolean Yesnumero(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static Integer Yconvertiranumero(String numero) {
        try {
            Integer valor = Integer.parseInt(numero);
            return valor;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static float Yconvertiranumerofloat(String numero) {
        try {
            Integer valor = (int)Float.parseFloat(numero);
            return valor;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static String YconvertiranumeroBooleanoString(String p_valor) {
        try {
            Boolean v_valor = Boolean.parseBoolean(p_valor);
            return v_valor + "";
        } catch (NumberFormatException nfe) {
            return X_FALSE;
        }
    }
    /*PARAMETRO PROPIEDADES DEL CERTIFICADO*/
    public static final String CN = "cn";
    public static final String CARGOTITLE = "title";
    public static final String CARGOT = "t";
    public static final String O = "o";
    public static final String EMAILE = "E";

    /*CARACTERES*/
    public static final String GUION = "-";
    public static final String VACIO = "";

    /*MENSAJES*/
    public static final String FIRMANDODOCUMENTO = "Firmando documento...";
    public static final String VALIDANDOCERTIFICADO = "VALIDANDO CERTIFICADO";
    public static final String MSG_ERROR_CLAVE = "Ingrese la clave por favor";
    public static final String CERTIFICADO_NO_VALIDO = "CERTIFICADO NO VÁLIDO";

    /*METODO MENSAJE*/
    public static void msgInformacion(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static final String ALTO = "alto";
    public static final String ANCHO = "ancho";
    public static final String X = "x";
    public static final String Y = "y";

    public static final String ESTRUCTURAPARAMETROS = "#SSigner\r\n"
            + "#Tue May 10 11:42:17 COT 2016\r\n"
            + "bol.crl=false\r\n"
            + "cert.rutacertificado=\r\n"
            + "int.tamanofuente=9\r\n"
            + "bol.cargo=TRUE\r\n"
            + "bol.repudio=true\r\n"
            + "hsh.clavecertificado=\r\n"
            + "cert.cargo=\r\n"
            + "cert.alias=\r\n"
            + "fil.rutacarpetasalida=\r\n"
            + "int.pagina=1\r\n"
            + "bol.imagen=false\r\n"
            + "url.tsl=https://iofe.indecopi.gob.pe/TSL/tsl-pe.xml\r\n"
            + "url.tsa.nombre=Demo\r\n"
            + "url.tsa=\r\n"
            + "bol.ocsp=false\r\n"
            + "url.tsa.usuario=\r\n"
            + "hsh.clavetoken=\r\n"
            + "str.algoritmo=SHA-1\r\n"
            + "cert.empresa=\r\n"
            + "int.estilo=1\r\n"
            + "fil.documento=\r\n"
            + "fil.rutacarpetaentrada=\r\n"
            + "cert.clavetoken=\r\n"
            + "fil.imagen=\r\n"
            + "fil.rutatsl=\n"
            + "cert.motivo=\r\n"
            + "hsh.claveaccesoalsistema=\r\n"
            + "cert.locacion=\r\n"
            + "cert.rutalibreria=\r\n"
            + "cert.firmante=\r\n"
            + "int.y=0\r\n"
            + "int.x=0\r\n"
            + "int.yp=0\r\n"
            + "int.xp=0\r\n"
            + "hsh.clavetsa=\r\n"
            + "str.fuente=Calibri\r\n"
            + "bol.tsl=false\r\n"
            + "bol.todaspaginas=false\r\n"
            + "flt.alto=0\r\n"
            + "flt.altop=0\r\n"
            + "cert.clavecertificado=\r\n"
            + "bol.cerrardocumento=false\r\n"
            + "bol.empresa=\r\n"
            + "bol.tsa=false\r\n"
            + "fil.imagenmarcaagua=\r\n"
            + "fil.rutacrl=\r\n"
            + "fil.rutacarpeta.salida.crl.tsl=\r\n"
            + "str.tiporepositorio=WINDOWS-MY\r\n"
            + "bol.visible=false\n"
            + "flt.ancho=0\r\n"
            + "flt.anchop=0\r\n"
            + "url.ocsp=\r\n"
            + "url.ocsp=\r\n"
            + "bol.ventana.volver.abrir=false\r\n"
            + "str_matriz_token_drivers=\r\n"
            + "cert.nombre.libreria=\r\n"
            + "licencia.key=012221410.dll\r\n"
            + "str.extension=\r\n"
            + "bol.extension.ini=false\r\n"
            + "bol.cal=true\r\n"
            + "bol.area=false\r\n"
            + "servicioweb.url=https://ws.plussigner.com/WSSignerLicencias/licencia?wsdl \r\n"
            + "str.bd_servidores_tsa=Demo,http://timestamp.globalsign.com/scripts/timestamp.dll, , ";
    
    public static final String RUTAPROPIEDADES = System.getProperty("user.home") + "/" + APLICACION_NOMBRE + "/propiedades/propiedades.properties";
    public static final String BOLCERRARDOCUMENTO = "bol.cerrardocumento";
    public static final String BOLTODASPAGINAS = "bol.todaspaginas";
    public static final String BOLOCSP = "bol.ocsp";
    public static final String BOLTSL = "bol.tsl";
    public static final String BOLCRL = "bol.crl";
    public static final String BOLREPUDIO = "bol.repudio";
    public static final String BOLTSA = "bol.tsa";
    public static final String BOLVISIBLE = "bol.visible";
    public static final String BOLCARGO = "bol.cargo";
    public static final String BOLEMPRESA = "bol.empresa";
    public static final String BOLIMAGEN = "bol.imagen";
    public static final String FILRUTATSL = "fil.rutatsl";
    public static final String FILRUTACRL = "fil.rutacrl";
    public static final String FILRUTACARPETASALIDA = "fil.rutacarpetasalida";
    public static final String FILRUTACARPETAENTRADA = "fil.rutacarpetaentrada";
    public static final String FILRUTACARPETASALIDATSLCRL = "fil.rutacarpeta.salida.crl.tsl";
//    public static final String FILRUTADOCUMENTO="fil.documento";
    public static final String FILRUTADOCUMENTO = "fil.documento";
    public static final String FILIMAGEN = "fil.imagen";
    public static final String FILIMAGENMARCAAGUA = "fil.imagenmarcaagua";
    public static final String HSHCLAVETSA = "hsh.clavetsa";
    public static final String HSHCLAVEACCESOALSISTEMA = "hsh.claveaccesoalsistema";
    public static final String INTTAMANOFUENTE = "int.tamanofuente";
    public static final String INTPAGINA = "int.pagina";
    public static final String INTX = "int.x";
    public static final String INTY = "int.y";
    public static final String FLTANCHO = "flt.ancho";
    public static final String FLTALTO = "flt.alto";
    
    public static final String INTXp = "int.xp";
    public static final String INTYp = "int.yp";
    public static final String FLTANCHOp = "flt.anchop";
    public static final String FLTALTOp = "flt.altop";
    public static final String TextoFirmante = "firmante.texto";
    public static final String conCampoEmpresa = "bol.empresa";
    
    public static final String STRTIPOREPOSITORIO = "str.tiporepositorio";
    public static final String STRFUENTE = "str.fuente";
    public static final String STRALGORITMO = "str.algoritmo";
    public static final String CERTALIAS = "cert.alias";
    public static final String CERTFIRMANTE = "cert.firmante";
    public static final String CERTCARGO = "cert.cargo";
    public static final String CERTEMPRESA = "cert.empresa";
    public static final String CERTMOTIVO = "cert.motivo";
    public static final String CERTLOCACION = "cert.locacion";
    public static final String URLTSANOMBRE = "url.tsa.nombre";
    public static final String URLTSA = "url.tsa";
    public static final String URLTSAUSUARIO = "url.tsa.usuario";
    public static final String URLOCSP = "url.ocsp";
    public static final String URLTSL = "url.tsl";
    public static final String INTESTILO = "int.estilo";
    public static final String CERTRUTACERTIFICADO = "cert.rutacertificado";
    public static final String CERTNOMBRELIBRERIA = "cert.nombre.libreria";
    public static final String CERTRUTALIBRERIA = "cert.rutalibreria";
    public static final String CERTCLAVECERTIFICADO = "cert.clavecertificado";
    public static final String CERTCLAVETOKEN = "cert.clavetoken";
    public static final String BOLVENTANAVOLVERABRIR = "bol.ventana.volver.abrir";
    public static final String STR_BD_SERVIDORES_TSA = "str.bd_servidores_tsa";
    public static final String LICENCIA_KEY = "licencia.key";
    public static final String STR_MATRIZ_TOKEN_DRIVERS = "str_matriz_token_drivers";
    public static final String STR_EXTENSION = "str.extension";
    public static final String BOL_EXTENSION_INI = "bol.extension.ini";
    public static final String BOL_CAL ="bol.cal";
    public static final String BOL_AREA ="bol.area";
    public static final String BOL_FH ="bol.fecha&hora";
    public static final String BOL_QR ="bol.qr";
    public static final String BOL_ETIQUETA ="bol.etiqueta";

    public static final String PROXY_URL ="proxy.url";
    public static final String PROXY_PUERTO ="proxy.puerto";

    public static void mensaje(String msg) {
        System.out.print(msg);
    }

    public static void mensajeln(String msg) {
        System.out.println(msg);
    }

    public static void dialogo(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static String formatearSoloFecha(Date fecha) {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY).format(fecha);
    }

    public static String formatearFechaConHora(Date fecha) {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY_HH_MM_SS).format(fecha);
    }

    public static String formatearSoloFecha() {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY).format(new Date());
    }

    public static String formatearFechaConHora() {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY_HH_MM_SS).format(new Date());
    }

    public static String formatearFechaConHoraampm() {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY_HH_MM__SS_AA).format(new Date());
    }

    public static String formatearSoloFecha(String fecha) {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY).format(fecha);
    }

    public static String formatearFechaConHora(String fecha) {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY_HH_MM_SS).format(fecha);
    }

    public static String formatearFechaConHoraampm(String fecha) {
        return new SimpleDateFormat(FORMATOFECHA_DD_MM_YYYY_HH_MM__SS_AA).format(fecha);
    }

    public int intConvertiranumero(String valor)
    {
        try
        {
            double d = Double.valueOf(valor);
            return (int)d;
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public Map getErrores()
    {
        return errores;
    }

}
