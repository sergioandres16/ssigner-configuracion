/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Clases.Repositorio;
import Interfaces.AlmacenProveedor;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 *
 * @author CESAR
 */
public class AlmacenJava implements AlmacenProveedor{

    @Override
    public KeyStore RecuperarAlmacen() throws KeyStoreException {
       try {
           KeyStore keyStore = KeyStore.getInstance(Repositorio.JKS);
           keyStore.load(null);
           return keyStore;
       }  catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
           throw new KeyStoreException(ex);
       }//To 
    
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, String Archivo) throws KeyStoreException {
         try {
           KeyStore keyStore = KeyStore.getInstance(Repositorio.JKS);
           keyStore.load(new FileInputStream(Archivo), Clave);
           return keyStore;
       }  catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
           throw new KeyStoreException(ex);
       }//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, byte[] Archivo) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLibreria_con_error() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
