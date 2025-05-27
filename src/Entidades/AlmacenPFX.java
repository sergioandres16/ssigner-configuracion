package Entidades;
import Clases.Repositorio;
import Interfaces.AlmacenProveedor;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class AlmacenPFX implements  AlmacenProveedor{
    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, String Archivo) throws KeyStoreException {
        try {
           KeyStore keyStore = KeyStore.getInstance(Repositorio.ARCHIVO);
           keyStore.load(new FileInputStream(Archivo), Clave);
           return keyStore;
        }  catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
            if(ex.getMessage().equals("keystore password was incorrect"))
                throw new KeyStoreException("El pin del certificado es incorrecto");
            else
                throw new KeyStoreException(ex);
        }
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore RecuperarAlmacen() throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, byte[] Archivo) throws KeyStoreException {
        try {
            KeyStore keyStore = KeyStore.getInstance(Repositorio.ARCHIVO);
            keyStore.load(new ByteArrayInputStream(Archivo), Clave);
           return keyStore;
        }  catch (NoSuchAlgorithmException | CertificateException | IOException ex) {
           throw new KeyStoreException(ex);
        }//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLibreria_con_error() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
