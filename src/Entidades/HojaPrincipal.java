package Entidades;

public class HojaPrincipal {

    private final float ancho;
    private final float alto;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float altounit;  
    private float anchounit; 
    float porcx1;
    float porcx2;
    float porcy1;
    float porcy2;
    float x1final;
    float y1final;
    float x2final;  
    float y2final; 
       
    public HojaPrincipal(float ancho, float alto) {
        this.ancho = ancho;
        this.alto = alto;
    }
    
    public void calcularmedidasunitarias()
    {
        altounit=this.alto/12;
        anchounit=this.ancho/12;
    }
    
    public static float[] calcularporcentajehojasecundaria(float valorcoordenada,float p_medida_base){
        float[] flt=new float[2];
        float porcentaje=valorcoordenada/p_medida_base;
        flt[0]=porcentaje;
        flt[1]=(porcentaje-(int)porcentaje)*100;
        return flt;
    }
    
    public float m_calcular_escala(float porcentaje,float p_medida_base){
        return (p_medida_base*porcentaje);
    }
    
    public float calcularescala(int porcentajeentero,float pocentajefracionario,float valororiginal){
        return ((porcentajeentero*valororiginal)+(pocentajefracionario*valororiginal/100));
    }
    
    public float getAncho() {
        return ancho;
    }
    
    public float getAlto() {
        return alto;
    }
    
    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getAltounit() {
        return altounit;
    }

    public float getAnchounit() {
        return anchounit;
    }

}
