/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author usuario
 */
import java.security.cert.Certificate;

public class Certificado {
    private String alias;
    private String dn;
    private String dnRoot ;
    private String cn ;
    private String cargo ;
    private String empresa ;
    private String uso;
    private String serie;
    private String fechaInicial;
    private String fechafinal;
    private String version;
    private Certificate[] cer;

    public Certificado() {
    }
    public Certificado(String alias, String cn,String fechafinal) {
        this.alias = alias;
        this.cn = cn;
        this.fechafinal = fechafinal;
    }
    
    public Certificado(String alias, String cn, String fechaInicial, String fechafinal) {
        this.alias = alias;
        this.cn = cn;
        this.fechaInicial = fechaInicial;
        this.fechafinal = fechafinal;
    }
    
    public Certificate[] getCer() {
        return cer;
    }

    public void setCer(Certificate[] cer) {
        this.cer = cer;
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

    public void setCn(String cn) {
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
        if(uso==null)
            this.uso = "-";
        else
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
    public void setVersion(String version) {
        this.version = version;
    }
     public String getVersion() {
        return version;
    }
}
