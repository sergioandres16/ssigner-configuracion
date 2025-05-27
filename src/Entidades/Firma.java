package Entidades;


public class Firma
{

    private String nombre;
    private Coordenada coordenada;
    private Dimension dimension;
    private Hoja Hoja;
    private boolean visible;
    private int tipoFirma;
    private String contenido;

    public Firma() {
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTipoFirma() {
        return tipoFirma;
    }

    public void setTipoFirma(int tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public class Hoja {

        private int pagina;
        private float ancho;
        private float alto;
        private boolean esVertical;

        public int getPagina() {
            return pagina;
        }

        public void setPagina(int pagina) {
            this.pagina = pagina;
        }

        public float getAncho() {
            return ancho;
        }

        public void setAncho(float ancho) {
            this.ancho = ancho;
        }

        public float getAlto() {
            return alto;
        }

        public void setAlto(float alto) {
            this.alto = alto;
        }

        public boolean isEsVertical() {
            esVertical = this.ancho < this.alto;
            return esVertical;
        }

        public void setEsVertical(boolean esVertical) {
            esVertical = this.ancho < this.alto;
            this.esVertical = esVertical;
        }
    }

    public class Coordenada {

        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    public class Dimension {

        private float ancho;
        private float alto;

        public float getAncho() {
            return ancho;
        }

        public void setAncho(float ancho) {
            this.ancho = ancho;
        }

        public float getAlto() {
            return alto;
        }

        public void setAlto(float alto) {
            this.alto = alto;
        }
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Hoja getHoja() {
        return Hoja;
    }

    public void setHoja(Hoja Hoja) {
        this.Hoja = Hoja;
    }
}
