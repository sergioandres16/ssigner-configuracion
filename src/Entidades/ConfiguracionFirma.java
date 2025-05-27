/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

/**
 *
 * @author cpalomino
 */
public class ConfiguracionFirma extends Firma {

    private int estilo;
    private String rutaImagen;
    private String rutaMarcaDeAgua;

    public int getEstilo() {
        return estilo;
    }

    public void setEstilo(int estilo) {
        this.estilo = estilo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getRutaMarcaDeAgua() {
        return rutaMarcaDeAgua;
    }

    public void setRutaMarcaDeAgua(String rutaMarcaDeAgua) {
        this.rutaMarcaDeAgua = rutaMarcaDeAgua;
    }

}
