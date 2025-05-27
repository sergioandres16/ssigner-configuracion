package Clases;

import Entidades.AlmacenJava;
import Entidades.AlmacenPFX;
import Entidades.AlmacenPKCS11;
import Entidades.AlmacenSO;
import Entidades.Firmante;
import Global.CLog;
import Interfaces.AlmacenProveedor;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public class Repositorio {

    public CLog cLog = new CLog();
    public static final String WINDOWS_MY = "WINDOWS-MY";
    public static final String TOKEN = "PKCS11";
    public static final String ARCHIVO = "PKCS12";
    public static final String JKS = "JKS";

    public static boolean cargo;
    public static int cerrarventana;
    private boolean libreria_con_error = false;

    private KeyStore repositorio;
    private String NombreAlmacen;
    private String claveToken;
    private List<Firmante> firmantes;
    private Firmante firmanteElegido;

    public Repositorio() {
    }

    public static List<String> obtenerTiposRepositorio() {
        List<String> lista = new ArrayList<>();
        lista.add(WINDOWS_MY);
        lista.add(TOKEN);
        lista.add(ARCHIVO);
        lista.add(JKS);
        return lista;
    }

    public KeyStore cargarCertificado(String pin, String tipoCertificado, String urlCertificado)
    {
        libreria_con_error = false;
        AlmacenProveedor almacenProveedor;
        
        try
        {
            switch (tipoCertificado)
            {
                case Repositorio.WINDOWS_MY:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);
                    repositorio = almacenProveedor.RecuperarAlmacen();
                    break;
                    
                case Repositorio.JKS:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);
                    
                    if (claveToken == null)
                        repositorio = almacenProveedor.RecuperarAlmacen();
                    else
                        repositorio = almacenProveedor.RecuperarAlmacen(pin.toCharArray(), urlCertificado);
                    
                    break;
                    
                case Repositorio.TOKEN:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);//java.security.KeyStore.getInstance(NombreTipoCertificado); 
                    repositorio = almacenProveedor.RecuperarAlmacen(pin.toCharArray(), urlCertificado);
                    libreria_con_error = almacenProveedor.isLibreria_con_error();
                    break;
                    
                case Repositorio.ARCHIVO:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);//java.security.KeyStore.getInstance(NombreTipoCertificado); 
                    repositorio = almacenProveedor.RecuperarAlmacen(pin.toCharArray(), urlCertificado);
                    break;
            }

            this.claveToken = pin;
        }
        catch (KeyStoreException ex)
        {
            cLog.archivar_log(ex.getMessage());
            repositorio = null;
        }
        
        return repositorio;
    }

    public KeyStore cargarCertificado(String pin, String tipoCertificado, byte[] BytesCertificado)
    {
        KeyStore keyStore = null;
        AlmacenProveedor almacenProveedor;
        try
        {
            switch (tipoCertificado)
            {
                case Repositorio.WINDOWS_MY:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);
                    keyStore = almacenProveedor.RecuperarAlmacen();
                    break;
                    
                case Repositorio.JKS:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);
                    keyStore = almacenProveedor.RecuperarAlmacen(pin.toCharArray(), BytesCertificado);
                    break;
                    
                case Repositorio.TOKEN:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);//java.security.KeyStore.getInstance(NombreTipoCertificado); 
                    keyStore = almacenProveedor.RecuperarAlmacen(pin.toCharArray(), BytesCertificado);
                    libreria_con_error = almacenProveedor.isLibreria_con_error();
                    break;
                
                case Repositorio.ARCHIVO:
                    almacenProveedor = this.RecuperarAlmacenProveedor(tipoCertificado);//java.security.KeyStore.getInstance(NombreTipoCertificado); 
                    keyStore = almacenProveedor.RecuperarAlmacen(pin.toCharArray(), BytesCertificado);
                    break;
            }

            repositorio = keyStore;
        }
        catch (GeneralSecurityException ex)
        {
            cLog.archivar_log(ex.getMessage());
            repositorio = null;
        }
        
        return repositorio;
    }

    private AlmacenProveedor RecuperarAlmacenProveedor(String tipo)
    {
        switch (tipo)
        {
            case WINDOWS_MY:
                NombreAlmacen = WINDOWS_MY;
                return new AlmacenSO();
            case JKS:
                NombreAlmacen = "Java";
                return new AlmacenJava();
            case TOKEN:
                NombreAlmacen = TOKEN;
                return new AlmacenPKCS11();//PKCS11
            case ARCHIVO:
                NombreAlmacen = ARCHIVO;
                return new AlmacenPFX();
            default:
                return new AlmacenSO();
        }
    }

    public List<String> obtenerAliases()
    {
        try
        {
            if (repositorio == null)
                return new ArrayList<>();

            firmantes = new ArrayList<>();
            Enumeration<String> aliases = repositorio.aliases();
            
            while (aliases.hasMoreElements())
            {
                String alias;
                boolean privateKey;

                alias = aliases.nextElement();
                privateKey = repositorio.isCertificateEntry(alias);

                if (privateKey)
                    continue;
                
                Firmante firmante;
                Firmante.Certificado certificado;
                Certificate[] certificadosConfianza;
                PrivateKey llavePrivada;
                X509Certificate CertificadoX509;
                
                firmante = new Firmante();
                certificado = firmante. new Certificado();
                certificadosConfianza = repositorio.getCertificateChain(alias);
                llavePrivada = (PrivateKey) repositorio.getKey(alias, claveToken.toCharArray());
                CertificadoX509 = (X509Certificate) repositorio.getCertificate(alias);
                
                certificado.setAlias(alias);
                certificado.setLlavePrivada(llavePrivada);
                certificado.setCertificadoX509(CertificadoX509);                
                certificado.setCn(getCN(CertificadoX509));
                certificado.setOu(getOU(CertificadoX509));
                certificado.setDn(getIssuerDN(CertificadoX509));
                certificado.setCargo(getTitulo(CertificadoX509));
                certificado.setEmpresa(getEmpresa(CertificadoX509));
                certificado.setUso((getUsos(CertificadoX509)));
                certificado.setVersion(String.valueOf(CertificadoX509.getVersion()));
                certificado.setCertificadosConfianza(certificadosConfianza);
                certificado.setFechaInicial(Global.CConstantes.formatearFechaConHora(CertificadoX509.getNotBefore()));
                certificado.setFechafinal(Global.CConstantes.formatearFechaConHora(CertificadoX509.getNotAfter()));
                certificado.setSerie(String.valueOf(CertificadoX509.getSerialNumber()));
                firmante.setCertificado(certificado);
                firmantes.add(firmante);
            }
        }
        catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            cLog.archivar_log(ex.getMessage());
        }
        List<String> aliases = new ArrayList<>();
        firmantes.stream().map((firmante) -> firmante.getCertificado()).map((certificadoAux) -> certificadoAux.getAlias()).forEach((alias) -> {
            aliases.add(alias);
        });
        return aliases;
    }

    public boolean validarAliasCertificado(String alias) {
        boolean esValido;
        esValido = false;
        firmanteElegido = new Firmante();

        try {
            for (Firmante firmanteAux : firmantes) {
                Firmante.Certificado certificado = firmanteAux.getCertificado();
                String aliasAux = certificado.getAlias();

                if (alias.equalsIgnoreCase(aliasAux)) {

                    Certificate[] certificadosConfianza;
                    PrivateKey llavePrivada;
                    X509Certificate CertificadoX509;

                    certificadosConfianza = repositorio.getCertificateChain(alias);
                    llavePrivada = (PrivateKey) repositorio.getKey(alias, claveToken.toCharArray());
                    CertificadoX509 = (X509Certificate) repositorio.getCertificate(alias);

                    certificado.setAlias(alias);
                    certificado.setCertificadoX509(CertificadoX509);
                    certificado.setLlavePrivada(llavePrivada);
                    certificado.setCn(getCN(CertificadoX509));
                    certificado.setOu(getOU(CertificadoX509));
                    certificado.setDn(getIssuerDN(CertificadoX509));
                    certificado.setCargo(getTitulo(CertificadoX509));
                    certificado.setEmpresa(getEmpresa(CertificadoX509));
                    certificado.setUso((getUsos(CertificadoX509)));
                    certificado.setVersion(String.valueOf(CertificadoX509.getVersion()));
                    certificado.setCertificadosConfianza(certificadosConfianza);
                    certificado.setFechaInicial(Global.CConstantes.formatearFechaConHora(CertificadoX509.getNotBefore()));
                    certificado.setFechafinal(Global.CConstantes.formatearFechaConHora(CertificadoX509.getNotAfter()));
                    certificado.setSerie(String.valueOf(CertificadoX509.getSerialNumber()));

                    esValido = true;
                    break;
                }
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            cLog.archivar_log("Error al validar el alias en la lista de certificados listados: " + alias + "..." + ex.getMessage());
        }
        return esValido;
    }

    public boolean validarTipoRepositorio(String TipoRepositorio) {
        boolean esValido;

        esValido = false;
        switch (TipoRepositorio) {
            case WINDOWS_MY:
            case TOKEN:
            case ARCHIVO:
            case JKS:
                esValido = true;
                break;
            default:
                break;
        }
        return esValido;
    }
    
     public static String getOU(X509Certificate certificado) {
        String dn = certificado.getSubjectDN().toString();
        String cn = "";

        try
        {
            LdapName ln = new LdapName(dn);

            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("OU"))
                {
                    cn = rdn.getValue().toString();
                    
                    if (!cn.toLowerCase().contains("issued"))
                        break;
                }
            }
        } catch (Exception e) {
        }
        return cn;
    }

    public static String getCN(X509Certificate certificado) {
        String dn = certificado.getSubjectDN().toString();
        String cn = "";

        try {
            LdapName ln = new LdapName(dn);

            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    cn = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e) {
        }
        return cn;
    }

    public static String getTitulo(X509Certificate certificado) {
        String dn = certificado.getSubjectDN().toString();
        String titulo = "";

        try {
            LdapName ln = new LdapName(dn);

            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("T") || rdn.getType().equalsIgnoreCase("TITLE")) {
                    titulo = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e) {
        }
        return titulo;
    }

    public static String getEmpresa(X509Certificate certificado) {
        String dn = certificado.getSubjectDN().toString();
        String titulo = "";

        try {
            LdapName ln = new LdapName(dn);

            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("O")) {
                    titulo = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e) {
        }
        return titulo;
    }

    public static String getIssuerDN(X509Certificate certificado) {
        String dn = certificado.getIssuerDN().toString();
        String cn = "";

        try {
            LdapName ln = new LdapName(dn);

            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    cn = rdn.getValue().toString();
                    break;
                }
            }
        } catch (Exception e) {
        }
        return cn;
    }

    public static String getUsos(X509Certificate certificado) {
        List<String> usoscert = Metodos.ValidarCertificados.getKeyUsage(certificado);
        if (usoscert == null) {
            return "";
        }
        Iterator iterator = usoscert.iterator();
        String usos = "";
        while (iterator.hasNext()) {
            usos += iterator.next() + ",";
        }
        return usos.substring(0, usos.lastIndexOf(","));
    }

    public KeyStore getAlmacen() {
        return repositorio;
    }

    public Provider getProveedor() {
        return repositorio.getProvider();
    }

    public String ObtenerNombreTipoAlmacen() {
        return NombreAlmacen;
    }

    public boolean isLibreria_con_error() {
        return libreria_con_error;
    }

    public KeyStore getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(KeyStore repositorio) {
        this.repositorio = repositorio;
    }

    public String getNombreAlmacen() {
        return NombreAlmacen;
    }

    public void setNombreAlmacen(String NombreAlmacen) {
        this.NombreAlmacen = NombreAlmacen;
    }

    public String getClaveToken() {
        return claveToken;
    }

    public void setClaveToken(String claveToken) {
        this.claveToken = claveToken;
    }

    public List<Firmante> getFirmantes() {
        return firmantes;
    }

    public void setFirmantes(List<Firmante> firmantes) {
        this.firmantes = firmantes;
    }

    public Firmante getFirmanteElegido()
    {
        return firmanteElegido;
    }

    public void setFirmanteElegido(Firmante firmanteElegido) 
    {
        this.firmanteElegido = firmanteElegido;
    }
}
