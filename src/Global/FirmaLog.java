package Global;

import java.util.List;

public class FirmaLog {
    
    private String p_Codigo;
    private String p_Mensaje;
    private String p_Proyecto;
    private String p_Tipo;
    private byte[] documento;
    protected static FirmaLog errores;
    private static List<FirmaLog> listaerrores;
    
    
    public static FirmaLog getErrores() {
        return errores;
    }

    public static List<FirmaLog> getListaerrores() {
        return listaerrores;
    }

    
    
    public FirmaLog(String p_Codigo, String p_Mensaje, String p_Proyecto, String p_Tipo, byte[] documento) {
        this.p_Codigo = p_Codigo;
        this.p_Mensaje = p_Mensaje;
        this.p_Proyecto = p_Proyecto;
        this.p_Tipo = p_Tipo;
        this.documento = documento;
    }

    public String getP_Codigo() {
        return p_Codigo;
    }

    public String getP_Mensaje() {
        return p_Mensaje;
    }

    public String getP_Proyecto() {
        return p_Proyecto;
    }

    public String getP_Tipo() {
        return p_Tipo;
    }

    public byte[] getDocumento() {
        return documento;
    }

    
    public static void agregarAExcepciones(String p_Codigo, String p_Mensaje, String p_Proyecto, String p_Tipo, byte[] documento){
        errores=new FirmaLog(p_Codigo, p_Mensaje, p_Proyecto, p_Tipo, documento);
        listaerrores.add(errores);
    }


}
