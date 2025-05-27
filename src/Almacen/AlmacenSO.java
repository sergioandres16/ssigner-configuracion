package Almacen;

import Interfaces.AlmacenProveedor;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import Clases.Repositorio;

public class AlmacenSO implements AlmacenProveedor{

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave) throws KeyStoreException {
       try {
           KeyStore keyStore = KeyStore.getInstance(Repositorio.WINDOWS_MY);
           keyStore.load(null,Clave);
           return keyStore;
       }  catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
           throw new KeyStoreException(ex);
       }
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, String Archivo) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore RecuperarAlmacen() throws KeyStoreException {
        try {
           KeyStore keyStore = KeyStore.getInstance(Repositorio.WINDOWS_MY);
           
           keyStore.load(null);

           return keyStore;
       }  catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
           throw new KeyStoreException(ex);
       } 
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
