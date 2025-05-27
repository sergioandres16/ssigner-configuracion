package Entidades;

import Clases.Libreria;
import java.io.File;
import java.util.List;


public class Configuracion
{
    private List<Libreria> librerias;
    private boolean conNumeroCal;
    private List<String> rutasdocumentos;
    private boolean vg_validar_con_tsl;
    private String vg_url_tsl = "";
    private boolean vg_validar_con_no_repudio;
    private boolean vg_cerrar_documento;
    private boolean vg_firma_visible;
    private int vg_firma_visible_estilo_firma;
    private boolean vg_firma_visible_firmar_todas_paginas;
    private int vg_firma_visible_pagina;
    private String vg_firma_visible_fuente = "";
    private float vg_firma_visible_x;
    private float vg_firma_visible_y;
    private float vg_firma_visible_x1;
    private float vg_firma_visible_y1;
       
    private float vg_firma_visible_tamanio_fuente;
    private float vg_firma_visible_ancho_base = 916;
    private float vg_firma_visible_alto_base = 1296;
    private boolean vg_con_imagen;
    private String vg_firma_visible_ruta_imagen = "";
    private String vg_firma_visible_ruta_image_marca_de_agua = "";
    private boolean vg_con_sello;
    private String textoFirma1 = "";
    private String vg_tsa_url = "";
    private String vg_tsa_usuario = "";
    private String vg_tsa_clave = "";
    private javax.swing.JTable vg_tabla_servicios_sello_tiempo;
    private javax.swing.JTable vg_tabla_token_drivers;
    private String vg_cadena_matriz_servicios_sello_tiempo = "";
    private String vg_cadena_matriz_token_drivers = "";
    private String vg_tsa_seleccionado = "";
    private String vg_ruta_libreria_nombre = "";
    private String vg_ruta_libreria = "";
    private boolean vg_cargo_visible;
    private boolean vg_empresa_visible;
    private boolean vg_documento_extension_inicio;
    private String vg_motivo = "";
    private String vg_localidad = "";
    private String vg_algoritmo = "";
    private String vg_carpeta_entrada = "";
    private String vg_carpeta_salida = "";
    private String vg_carpeta_salida_crl_tsl = "";
    private String vg_ruta_documento = "";
    private String vg_documento_nombre = "";
    private String vg_documento_ruta_padre = "";
    private String vg_documento_extension_salida = "";
    private String vg_ruta_certificado = "";
    private String vg_clave_token = "";
    private String vg_clave_certificado = "";
    private String vg_sistema_usuario = "";
    private String vg_sistema_clave = "";
    private String vg_repositorio_tipo = "";
    private boolean vg_ventana_volver_abrir;
    private static String vg_so;
    private String vg_licencia_key = "";
    private String vg_licencia_serie = "";
    private String modalidad;
    private static final String server = "-s";
    private static final String user = "-u";
    private String documentoUltimo = "";
    private String carpetaPadre;
    private String rutaJdk;
    private String archivoConfig;
    private String archivoPropiedades;
    private List<String> repositorioTipos;
    private Firmante firmante;
    private boolean conCarmpoArea = true;
    private boolean conCarmpoAreaFH = true;
    private boolean conQR = false;
    private String etiqueta_firma;
    private int indexFirmanteElegido = 0;
    private String vg_texto_firmante = "";
    private boolean conCampoEmpresa = true;
    private String puertoProxy;
    private String urlProxy;

    public String getPuertoProxy()
    {
        return puertoProxy;
    }

    public void setPuertoProxy(String puertoProxy)
    {
        this.puertoProxy = puertoProxy;
    }

    public String getUrlProxy()
    {
        return urlProxy;
    }

    public void setUrlProxy(String urlProxy)
    {
        this.urlProxy = urlProxy;
    }

    public boolean getConCampoEmpresa()
    {
        return conCampoEmpresa;
    }

    public void setConCampoEmpresa(boolean conCampoEmpresa)
    {
        this.conCampoEmpresa = conCampoEmpresa;
    }
    

    public String getVg_texto_firmante()
    {
        return vg_texto_firmante;
    }

    public void setVg_texto_firmante(String vg_texto_firmante)
    {
        this.vg_texto_firmante = vg_texto_firmante;
    }

    public void setIndexFirmanteElegido(int indexFirmanteElegido)
    {
        this.indexFirmanteElegido = indexFirmanteElegido;
    }

    public int getIndexFirmanteElegido()
    {
        return indexFirmanteElegido;
    }    

    public String getEtiqueta_firma()
    {
        return etiqueta_firma;
    }

    public void setEtiqueta_firma(String etiqueta_firma) {
        this.etiqueta_firma = etiqueta_firma;
    }
    
    public boolean getConCarmpoArea()
    {
        return conCarmpoArea;
    }

    public void setConCarmpoArea(boolean conCarmpoArea)
    {
        this.conCarmpoArea = conCarmpoArea;
    }
    public boolean getConCarmpoAreaFH()
    {
        return conCarmpoAreaFH;
    }

    public void setConCarmpoAreaFH(boolean conCarmpoAreaFH)
    {
        this.conCarmpoAreaFH = conCarmpoAreaFH;
    }
    
    public boolean getConQR()
    {
        return conQR;
    }

    public void setConQR(boolean conQR)
    {
        this.conQR = conQR;
    }
    
    public boolean getConNumeroCal()
    {
        return conNumeroCal;
    }

    public void setConNumeroCal(boolean conNumeroCal)
    {
        this.conNumeroCal = conNumeroCal;
    }
    
    public List<String> getRutasdocumentos()
    {
        return rutasdocumentos;
    }

    public void setRutasdocumentos(List<String> rutasdocumentos) {
        this.rutasdocumentos = rutasdocumentos;
    }

    public boolean isVg_validar_con_tsl() {
        return vg_validar_con_tsl;
    }

    public void setVg_validar_con_tsl(boolean vg_validar_con_tsl) {
        this.vg_validar_con_tsl = vg_validar_con_tsl;
    }

    public boolean isVg_validar_con_no_repudio() {
        return vg_validar_con_no_repudio;
    }

    public void setVg_validar_con_no_repudio(boolean vg_validar_con_no_repudio) {
        this.vg_validar_con_no_repudio = vg_validar_con_no_repudio;
    }

    public boolean isVg_cerrar_documento() {
        return vg_cerrar_documento;
    }

    public void setVg_cerrar_documento(boolean vg_cerrar_documento) {
        this.vg_cerrar_documento = vg_cerrar_documento;
    }

    public boolean isVg_firma_visible() {
        return vg_firma_visible;
    }

    public void setVg_firma_visible(boolean vg_firma_visible) {
        this.vg_firma_visible = vg_firma_visible;
    }

    public boolean isVg_firma_visible_firmar_todas_paginas() {
        return vg_firma_visible_firmar_todas_paginas;
    }

    public void setVg_firma_visible_firmar_todas_paginas(boolean vg_firma_visible_firmar_todas_paginas) {
        this.vg_firma_visible_firmar_todas_paginas = vg_firma_visible_firmar_todas_paginas;
    }

    public float getVg_firma_visible_x() {
        return vg_firma_visible_x;
    }

    public void setVg_firma_visible_x(float vg_firma_visible_x) {
        this.vg_firma_visible_x = vg_firma_visible_x;
    }

    public float getVg_firma_visible_y() {
        return vg_firma_visible_y;
    }

    public void setVg_firma_visible_y(float vg_firma_visible_y) {
        this.vg_firma_visible_y = vg_firma_visible_y;
    }

    public float getVg_firma_visible_x1() {
        return vg_firma_visible_x1;
    }

    public void setVg_firma_visible_x1(float vg_firma_visible_x1) {
        this.vg_firma_visible_x1 = vg_firma_visible_x1;
    }

    public float getVg_firma_visible_y1() {
        return vg_firma_visible_y1;
    }

    public void setVg_firma_visible_y1(float vg_firma_visible_y1) {
        this.vg_firma_visible_y1 = vg_firma_visible_y1;
    }

    public float getVg_firma_visible_tamanio_fuente() {
        return vg_firma_visible_tamanio_fuente;
    }

    public void setVg_firma_visible_tamanio_fuente(float vg_firma_visible_tamanio_fuente) {
        this.vg_firma_visible_tamanio_fuente = vg_firma_visible_tamanio_fuente;
    }

    public String getVg_firma_visible_ruta_imagen() {
        return vg_firma_visible_ruta_imagen;
    }

    public void setVg_firma_visible_ruta_imagen(String vg_firma_visible_ruta_imagen) {
        this.vg_firma_visible_ruta_imagen = vg_firma_visible_ruta_imagen;
    }

    public boolean isVg_con_sello() {
        return vg_con_sello;
    }

    public void setVg_con_sello(boolean vg_con_sello) {
        this.vg_con_sello = vg_con_sello;
    }
    
    public String getTextoFirma1() {
        return textoFirma1;
    }

    public void setTextoFirma1(String textoFirma1) {
        this.textoFirma1 = textoFirma1;
    }

    public String getVg_tsa_url() {
        return vg_tsa_url;
    }

    public void setVg_tsa_url(String vg_tsa_url) {
        this.vg_tsa_url = vg_tsa_url;
    }

    public String getVg_tsa_usuario() {
        return vg_tsa_usuario;
    }

    public void setVg_tsa_usuario(String vg_tsa_usuario) {
        this.vg_tsa_usuario = vg_tsa_usuario;
    }

    public String getVg_tsa_clave() {
        return vg_tsa_clave;
    }

    public void setVg_tsa_clave(String vg_tsa_clave) {
        this.vg_tsa_clave = vg_tsa_clave;
    }

    public String getVg_ruta_libreria() {
        return vg_ruta_libreria;
    }

    public void setVg_ruta_libreria(String vg_ruta_libreria) {
        this.vg_ruta_libreria = vg_ruta_libreria;
    }

    public String getVg_motivo() {
        return vg_motivo;
    }

    public void setVg_motivo(String vg_motivo) {
        this.vg_motivo = vg_motivo;
    }

    public String getVg_localidad() {
        return vg_localidad;
    }

    public void setVg_localidad(String vg_localidad) {
        this.vg_localidad = vg_localidad;
    }

    public String getVg_algoritmo() {
        return vg_algoritmo;
    }

    public void setVg_algoritmo(String vg_algoritmo) {
        this.vg_algoritmo = vg_algoritmo;
    }

    public boolean isVg_cargo_visible() {
        return vg_cargo_visible;
    }

    public void setVg_cargo_visible(boolean vg_cargo_visible) {
        this.vg_cargo_visible = vg_cargo_visible;
    }

    public boolean isVg_empresa_visible() {
        return vg_empresa_visible;
    }

    public void setVg_empresa_visible(boolean vg_empresa_visible) {
        this.vg_empresa_visible = vg_empresa_visible;
    }

    public boolean isVg_con_imagen() {
        return vg_con_imagen;
    }

    public void setVg_con_imagen(boolean vg_con_imagen) {
        this.vg_con_imagen = vg_con_imagen;
    }

    public String getVg_carpeta_entrada() {
        return vg_carpeta_entrada;
    }

    public void setVg_carpeta_entrada(String vg_carpeta_entrada) {
        this.vg_carpeta_entrada = vg_carpeta_entrada;
    }

    public String getVg_carpeta_salida() {
        return vg_carpeta_salida;
    }

    public void setVg_carpeta_salida(String vg_carpeta_salida) {
        this.vg_carpeta_salida = vg_carpeta_salida;
    }

    public String getVg_ruta_documento() {
        return vg_ruta_documento;
    }

    public void setVg_ruta_documento(String vg_ruta_documento) {
        this.vg_ruta_documento = vg_ruta_documento;
    }

    public String getVg_firma_visible_ruta_image_marca_de_agua() {
        return vg_firma_visible_ruta_image_marca_de_agua;
    }

    public void setVg_firma_visible_ruta_image_marca_de_agua(String vg_firma_visible_ruta_image_marca_de_agua) {
        this.vg_firma_visible_ruta_image_marca_de_agua = vg_firma_visible_ruta_image_marca_de_agua;
    }

    public String getVg_sistema_usuario() {
        return vg_sistema_usuario;
    }

    public void setVg_sistema_usuario(String vg_sistema_usuario) {
        this.vg_sistema_usuario = vg_sistema_usuario;
    }

    public String getVg_sistema_clave() {
        return vg_sistema_clave;
    }

    public void setVg_sistema_clave(String vg_sistema_clave) {
        this.vg_sistema_clave = vg_sistema_clave;
    }

    public int getVg_firma_visible_pagina() {
        return vg_firma_visible_pagina;
    }

    public void setVg_firma_visible_pagina(int vg_firma_visible_pagina) {
        this.vg_firma_visible_pagina = vg_firma_visible_pagina;
    }

    public String getVg_repositorio_tipo() {
        return vg_repositorio_tipo;
    }

    public void setVg_repositorio_tipo(String vg_repositorio_tipo) {
        this.vg_repositorio_tipo = vg_repositorio_tipo;
    }

    public String getVg_firma_visible_fuente() {
        return vg_firma_visible_fuente;
    }

    public void setVg_firma_visible_fuente(String vg_firma_visible_fuente) {
        this.vg_firma_visible_fuente = vg_firma_visible_fuente;
    }

    public String getVg_url_tsl() {
        return vg_url_tsl;
    }

    public void setVg_url_tsl(String vg_url_tsl) {
        this.vg_url_tsl = vg_url_tsl;
    }

    public int getVg_firma_visible_estilo_firma() {
        return vg_firma_visible_estilo_firma;
    }

    public void setVg_firma_visible_estilo_firma(int vg_firma_visible_estilo_firma) {
        this.vg_firma_visible_estilo_firma = vg_firma_visible_estilo_firma;
    }

    public String getVg_ruta_certificado() {
        return vg_ruta_certificado;
    }

    public void setVg_ruta_certificado(String vg_ruta_certificado) {
        this.vg_ruta_certificado = vg_ruta_certificado;
    }

    public String getVg_clave_token() {
        return vg_clave_token;
    }

    public void setVg_clave_token(String vg_clave_token) {
        this.vg_clave_token = vg_clave_token;
    }

    public String getVg_clave_certificado() {
        return vg_clave_certificado;
    }

    public void setVg_clave_certificado(String vg_clave_certificado) {
        this.vg_clave_certificado = vg_clave_certificado;
    }

    public String getVg_cadena_matriz_servicios_sello_tiempo() {
        return vg_cadena_matriz_servicios_sello_tiempo;
    }

    public void setVg_cadena_matriz_servicios_sello_tiempo(String vg_cadena_matriz_servicios_sello_tiempo) {
        this.vg_cadena_matriz_servicios_sello_tiempo = vg_cadena_matriz_servicios_sello_tiempo;
    }

    public javax.swing.JTable getVg_tabla_servicios_sello_tiempo() {
        return vg_tabla_servicios_sello_tiempo;
    }

    public void setVg_tabla_servicios_sello_tiempo(javax.swing.JTable vg_tabla_servicios_sello_tiempo) {
        this.vg_tabla_servicios_sello_tiempo = vg_tabla_servicios_sello_tiempo;
    }

    public String getVg_tsa_seleccionado() {
        return vg_tsa_seleccionado;
    }

    public void setVg_tsa_seleccionado(String vg_tsa_seleccionado) {
        this.vg_tsa_seleccionado = vg_tsa_seleccionado;
    }

    public static String getVg_so() {
        return vg_so;
    }

    public static void setVg_so(String aVg_so) {
        vg_so = aVg_so;
    }

    public float getVg_firma_visible_ancho_base() {
        return vg_firma_visible_ancho_base;
    }

    public void setVg_firma_visible_ancho_base(float vg_firma_visible_ancho_base) {
        this.vg_firma_visible_ancho_base = vg_firma_visible_ancho_base;
    }

    public float getVg_firma_visible_alto_base() {
        return vg_firma_visible_alto_base;
    }

    public void setVg_firma_visible_alto_base(float vg_firma_visible_alto_base) {
        this.vg_firma_visible_alto_base = vg_firma_visible_alto_base;
    }

    public String getVg_carpeta_salida_crl_tsl() {
        return vg_carpeta_salida_crl_tsl;
    }

    public void setVg_carpeta_salida_crl_tsl(String vg_carpeta_salida_crl_tsl) {
        this.vg_carpeta_salida_crl_tsl = vg_carpeta_salida_crl_tsl + File.separator;
    }

    public String getVg_documento_nombre() {
        return vg_documento_nombre;
    }

    public void setVg_documento_nombre(String vg_documento_nombre) {
        this.vg_documento_nombre = vg_documento_nombre;
    }

    public String getVg_documento_ruta_padre() {
        return vg_documento_ruta_padre;
    }

    public void setVg_documento_ruta_padre(String vg_documento_ruta_padre) {
        this.vg_documento_ruta_padre = vg_documento_ruta_padre;
    }

    public boolean isVg_ventana_volver_abrir() {
        return vg_ventana_volver_abrir;
    }

    public void setVg_ventana_volver_abrir(boolean vg_ventana_volver_abrir) {
        this.vg_ventana_volver_abrir = vg_ventana_volver_abrir;
    }

    public String getVg_cadena_matriz_token_drivers() {
        return vg_cadena_matriz_token_drivers;
    }

    public void setVg_cadena_matriz_token_drivers(String vg_cadena_matriz_token_drivers) {
        this.vg_cadena_matriz_token_drivers = vg_cadena_matriz_token_drivers;
    }

    public javax.swing.JTable getVg_tabla_token_drivers() {
        return vg_tabla_token_drivers;
    }

    public void setVg_tabla_token_drivers(javax.swing.JTable vg_tabla_token_drivers) {
        this.vg_tabla_token_drivers = vg_tabla_token_drivers;
    }

    public String getVg_ruta_libreria_nombre() {
        return vg_ruta_libreria_nombre;
    }

    public void setVg_ruta_libreria_nombre(String vg_ruta_libreria_nombre) {
        this.vg_ruta_libreria_nombre = vg_ruta_libreria_nombre;
    }

    public String getVg_licencia_key() {
        return vg_licencia_key;
    }

    public void setVg_licencia_key(String vg_licencia_key) {
        this.vg_licencia_key = vg_licencia_key;
    }

    public String getVg_licencia_serie() {
        return vg_licencia_serie;
    }

    public void setVg_licencia_serie(String vg_licencia_serie) {
        this.vg_licencia_serie = vg_licencia_serie;
    }

    public String getVg_documento_extension_salida() {
        return vg_documento_extension_salida;
    }

    public void setVg_documento_extension_salida(String vg_documento_extension_salida) {
        this.vg_documento_extension_salida = vg_documento_extension_salida;
    }

    public boolean isVg_documento_extension_inicio() {
        return vg_documento_extension_inicio;
    }

    public void setVg_documento_extension_inicio(boolean vg_documento_extension_inicio) {
        this.vg_documento_extension_inicio = vg_documento_extension_inicio;
    }

    public String getModalidad() {
        return modalidad;
    }

    public static String getServer() {
        return server;
    }

    public static String getUser() {
        return user;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getDocumentoUltimo() {
        return documentoUltimo;
    }

    public void setDocumentoUltimo(String documentoUltimo) {
        this.documentoUltimo = documentoUltimo;
    }

    public String getCarpetaPadre() {
        return carpetaPadre;
    }

    public void setCarpetaPadre(String carpetaPadre) {
        if (carpetaPadre.charAt(carpetaPadre.length() - 1) != File.separatorChar) {
            carpetaPadre += File.separator;
        }
        this.carpetaPadre = carpetaPadre;
        this.archivoConfig = this.carpetaPadre + "config.properties";
        this.archivoPropiedades = this.carpetaPadre + "propiedades" + File.separator + "propiedades.properties";
    }

    public String getRutaJdk() {
        return rutaJdk;
    }

    public void setRutaJdk(String rutaJdk) {
        this.rutaJdk = rutaJdk;
    }

    public String getArchivoConfig() {
        return archivoConfig;
    }

    public void setArchivoConfig(String archivoConfig) {
        this.archivoConfig = archivoConfig;
    }

    public List<String> getRepositorioTipos() {
        return repositorioTipos;
    }

    public void setRepositorioTipos(List<String> repositorioTipos) {
        this.repositorioTipos = repositorioTipos;
    }

    public String getArchivoPropiedades() {
        return archivoPropiedades;
    }

    public Firmante getFirmante() {
        return firmante;
    }

    public void setFirmante(Firmante firmante) {
        this.firmante = firmante;
    }

    public List<Libreria> getLibrerias() {
        return librerias;
    }

    public void setLibrerias(List<Libreria> librerias) {
        this.librerias = librerias;
    }
}
