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
public class Licencia implements  java.io.Serializable{
    private static final long serialVersionUID = 8799656478674716638L;
    private String v_licencia_serial_numero;
    private String v_licencia_fecha_inicio;

    public Licencia(String v_licencia_serial_numero, String v_licencia_fecha_inicio) {
        this.v_licencia_serial_numero = v_licencia_serial_numero;
        this.v_licencia_fecha_inicio = v_licencia_fecha_inicio;
    }

    public Licencia(String v_licencia_serial_numero) {
        this.v_licencia_serial_numero = v_licencia_serial_numero;
    }

    public String getV_licencia_serial_numero() {
        return v_licencia_serial_numero;
    }

    public void setV_licencia_serial_numero(String v_licencia_serial_numero) {
        this.v_licencia_serial_numero = v_licencia_serial_numero;
    }

    public String getV_licencia_fecha_inicio() {
        return v_licencia_fecha_inicio;
    }

    public void setV_licencia_fecha_inicio(String v_licencia_fecha_inicio) {
        this.v_licencia_fecha_inicio = v_licencia_fecha_inicio;
    }
    
}
