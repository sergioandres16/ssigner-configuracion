/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.security.PrivateKey;
import javax.security.auth.x500.X500Principal;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author cpalomino
 */
public class Firmante {

    private Certificado certificado;

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public class Certificado
    {
        private String alias;
        private String dn;
        private String ou;
        private String dnRoot;
        private String cn;
        private String cargo;
        private String empresa;
        private String uso;
        private String serie;
        private String fechaInicial;
        private String fechafinal;
        private String version;
        private Certificate[] certificadosConfianza;
        private X509Certificate certificadoX509;        
        private PrivateKey llavePrivada;
        private PublicKey llavePublica;
        private String email;
        
        private String extractEmailAddress() {
            Pattern pattern = Pattern.compile("EMAILADDRESS=([^,]+)");
            Matcher matcher = pattern.matcher(this.certificadoX509.getSubjectDN().toString());

            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return "No se encontró la dirección de correo electrónico.";
            }
        }

        
        
        public String getEmail()
        {
            return this.extractEmailAddress();
        }

        
        public String getOu()
        {
            return ou;
        }

        public void setOu(String ou)
        {
            this.ou = ou;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getDn() {
            return dn;
        }

        public void setDn(String dn) {
            this.dn = dn;
        }

        public String getDnRoot() {
            return dnRoot;
        }

        public void setDnRoot(String dnRoot) {
            this.dnRoot = dnRoot;
        }

        public String getCn() {
            return cn;
        }

        public void setCn(String cn)
        {
            this.cn = cn;
        }

        public String getCargo() {
            return cargo;
        }

        public void setCargo(String cargo) {
            this.cargo = cargo;
        }

        public String getEmpresa() {
            return empresa;
        }

        public void setEmpresa(String empresa) {
            this.empresa = empresa;
        }

        public String getUso() {
            return uso;
        }

        public void setUso(String uso) {
            this.uso = uso;
        }

        public String getSerie() {
            return serie;
        }

        public void setSerie(String serie) {
            this.serie = serie;
        }

        public String getFechaInicial() {
            return fechaInicial;
        }

        public void setFechaInicial(String fechaInicial) {
            this.fechaInicial = fechaInicial;
        }

        public String getFechafinal() {
            return fechafinal;
        }

        public void setFechafinal(String fechafinal) {
            this.fechafinal = fechafinal;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Certificate[] getCertificadosConfianza() {
            return certificadosConfianza;
        }

        public void setCertificadosConfianza(Certificate[] certificadosConfianza) {
            this.certificadosConfianza = certificadosConfianza;
        }

        public X509Certificate getCertificadoX509() {
            return certificadoX509;
        }

        public void setCertificadoX509(X509Certificate certificadoX509) {
            this.certificadoX509 = certificadoX509;
        }

        public PublicKey getLlavePublica() {
            return certificadoX509.getPublicKey();
        }
        
        public PrivateKey getLlavePrivada() {
            return llavePrivada;
        }

        public void setLlavePrivada(PrivateKey llavePrivada) {
            this.llavePrivada = llavePrivada;
        }

    }
}
