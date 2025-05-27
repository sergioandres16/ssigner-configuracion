/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public class x509Certificado {

    private KeyStore _ks;
    private ArrayList<Firmante.Certificado> ltCertificados;
    private Firmante.Certificado certificados;

    public x509Certificado() {

    }

    public x509Certificado(KeyStore ks) {
        try {
            ltCertificados = new ArrayList<>();
            _ks = ks;
            Enumeration<String> enumeration = _ks.aliases();
            while (enumeration.hasMoreElements()) {
                Firmante firmante = new Firmante();
                certificados = firmante.getCertificado();
                String alias = enumeration.nextElement();
                certificados.setAlias(alias);

                PrivateKey privateKey = (PrivateKey) _ks.getKey(alias, null);
                if (privateKey == null) {
                    continue;
                }
                X509Certificate certificadocad = (X509Certificate) _ks.getCertificate(alias);
                String dn = certificadocad.getSubjectDN().toString();
                LdapName ln = new LdapName(dn);
                certificados.setFechaInicial(formatearfecha(certificadocad.getNotBefore()));
                certificados.setFechafinal(formatearfecha(certificadocad.getNotAfter()));
                certificados.setVersion("" + certificadocad.getVersion());
                certificados.setDnRoot("" + certificadocad.getIssuerX500Principal());
                certificados.setSerie("" + certificadocad.getSerialNumber());

                for (Rdn rdn : ln.getRdns())
                {
                    if (rdn.getType().equalsIgnoreCase(Constantes.CN))
                        certificados.setCn(rdn.getValue().toString());

                    if (rdn.getType().equalsIgnoreCase(Constantes.CARGOTITLE) || rdn.getType().equalsIgnoreCase(Constantes.CARGOT))
                        certificados.setCargo(rdn.getValue().toString());
                    
                    if (rdn.getType().equalsIgnoreCase(Constantes.O))
                        certificados.setEmpresa(rdn.getValue().toString());
                    
                    if (rdn.getType().equalsIgnoreCase(Constantes.OU) && !rdn.getValue().toString().toLowerCase().contains("issued"))
                        certificados.setEmpresa(rdn.getValue().toString());
                }
                String Uso = "";
                List<String> lista = getKeyUsage(certificadocad);
                if (lista != null) {
                    for (String lista1 : lista) {
                        Uso = Uso + " " + lista1;
                    }
                    certificados.setUso(Uso);
                } else {
                    certificados.setUso(Constantes.GUION);
                }
                /*CARGO*/
                if (certificados.getCargo() == null) {
                    certificados.setCargo(Constantes.GUION);
                }

                ltCertificados.add(certificados);
            }
        } catch (InvalidNameException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            Logger.getLogger(x509Certificado.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<Firmante.Certificado> ObtenerListax509Certificados() {
        return ltCertificados;
    }

    public static final String formatearfecha() {
        return new SimpleDateFormat(Constantes.FORMATOFECHA_DD_MMYYYY).format(new Date());
    }

    public static final String formatearfecha(X509Certificate certificadocad) {
        return new SimpleDateFormat(Constantes.FORMATOFECHA_DD_MMYYYY).format(certificadocad.getNotBefore());
    }

    public static final String formatearfecha(Date fehaDate) {
        return new SimpleDateFormat(Constantes.FORMATOFECHA_DD_MMYYYY).format(fehaDate);
    }

    private static final String[] keyUsageLabels = new String[]{
        "digitalSignature", "nonRepudiation", "keyEncipherment",
        "dataEncipherment", "keyAgreement", "keyCertSign", "cRLSign",
        "encipherOnly", "decipherOnly"};

    private static List<String> getKeyUsage(final X509Certificate cert) {
        final boolean[] keyUsage = cert.getKeyUsage();
        if (keyUsage != null) {
            final List<String> ret = new LinkedList<String>();
            for (int i = 0; i < keyUsage.length; ++i) {
                if (keyUsage[i]) {
                    if (i < keyUsageLabels.length) {
                        ret.add(keyUsageLabels[i]);
                    } else {
                        ret.add(String.valueOf(i));
                    }
                }
            }
            return ret;
        } else {
            return null;
        }

    }

}
