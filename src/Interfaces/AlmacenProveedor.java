/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 *
 * @author CESAR
 */
public interface AlmacenProveedor {
    public KeyStore RecuperarAlmacen() throws KeyStoreException;
    public KeyStore RecuperarAlmacen(char[] Clave) throws KeyStoreException;
    public KeyStore RecuperarAlmacen(char[] Clave,String Archivo) throws KeyStoreException;
    public KeyStore RecuperarAlmacen(char[] Clave,byte [] Archivo) throws KeyStoreException;
    public boolean isLibreria_con_error();
}
