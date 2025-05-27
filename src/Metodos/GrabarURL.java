package Metodos;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;


public class GrabarURL
{
    private byte[] buf;
    private String nombrearchivo;
    private String rutapadre;

    public String ubicacion_cache(String Ruta ,String urlString)
    {
        String nombrearchivo=urlString.substring(urlString.lastIndexOf("/")+1);
        return Ruta+nombrearchivo;
    }
    
    public boolean descargar(String Ruta, String urlString, String urlProxy, String puertoProxy) throws Exception
    {
        nombrearchivo = urlString.substring(urlString.lastIndexOf("/")+1);
        rutapadre = urlString.substring(0,urlString.lastIndexOf("/")+1);
        URL url = new URL(urlString);
        URLConnection con;
        
        if (urlProxy.isEmpty() && puertoProxy.isEmpty())
            con = url.openConnection();
        else
        {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(urlProxy, Integer.valueOf(puertoProxy)));
            con = url.openConnection(proxy);
        }
        
        con.setConnectTimeout(90000);
        con.setReadTimeout(90000);

        try (BufferedInputStream in = new BufferedInputStream(con.getInputStream()))
        {
            byte data[] = new byte[1024];
            int count;

            if(Ruta.equals(""))
            {
                try(ByteArrayOutputStream baos=new ByteArrayOutputStream())
                {
                    while ((count = in.read(data, 0, 1024)) != -1)
                        baos.write(data, 0, count);
                                        
                    buf=baos.toByteArray();
                    baos.close();
                    return true;
                }
            }
            else
            {
                try(FileOutputStream fout = new FileOutputStream(ubicacion_cache(Ruta,urlString)))
                {
                    while ((count = in.read(data, 0, 1024)) != -1)
                        fout.write(data, 0, count);
                }
            }

            in.close();
            return true;
        }
    }  

    public byte[] getBuf() {
        return buf;
    }

    public String getNombrearchivo() {
        return nombrearchivo;
    }

    public String getRutapadre() {
        return rutapadre;
    }




    
    
}
