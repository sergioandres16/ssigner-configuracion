/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR
 */
public class So {

    private final String[] expresionRegularSO = new String[]{"Win", "Windows XP", "Lin", "Mac", "XP"};
    private final String[] propiedadesSistema = new String[]{
        "os.name", //0
        "os.arch", //1
        "os.version", //2
        "usr.home", //3
        "file.separator", //4
        "java.home", //5
        "java.version", //6 
        "user.home" //7
    };

    private String so;
    private String extension;

    public So() {
    }

    public void configurarExtensionesLibreriasToken() {
        so = ObtenerSistemaOperativo();
        if (so.startsWith(expresionRegularSO[0]) || so.startsWith(expresionRegularSO[1])) {
            extension = ".dll";
        } else if (so.startsWith(expresionRegularSO[2])) {
            extension = ".so";
        } else if (so.startsWith(expresionRegularSO[3])) {
            extension = ".cfg";
        }
    }

    private String ObtenerSistemaOperativo() {
        return System.getProperty(propiedadesSistema[0]);
    }

    public String ObtenerCarpetaPersonal() {
        return System.getProperty(propiedadesSistema[3]);
    }

    public String getExtension() {
        return extension;
    }

    public String getSo() {
        return so;
    }
}
