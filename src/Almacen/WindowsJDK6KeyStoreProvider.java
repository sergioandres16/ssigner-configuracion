/*
 * Copyright (C) 2009 Libreria para Firma Digital development team.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */

package Almacen;

import Interfaces.AlmacenProveedor;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class WindowsJDK6KeyStoreProvider implements AlmacenProveedor {

	public KeyStore RecuperarAlmacen(char[] password) throws KeyStoreException {
		try {
			KeyStore keyStore = KeyStore.getInstance("Windows-MY");
			keyStore.load(null, password);
			return keyStore;
		} catch (NoSuchAlgorithmException e) {
			throw new KeyStoreException(e);
		} catch (CertificateException e) {
			throw new KeyStoreException(e);
		} catch (IOException e) {
			throw new KeyStoreException(e);
		}
	}

    @Override
    public KeyStore RecuperarAlmacen() throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, String Archivo) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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