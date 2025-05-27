/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import Clases.Tsa;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;

/**
 *
 * @author usuario
 */
public class ValidarSelloTiempo {
    public ValidarSelloTiempo() {
    }
     public String validarServicioSelloDeTiempo(String urltsa,String usuario,String clave) throws IOException{
         return validarServicioSelloDeTiempo(urltsa,usuario,clave,0);
     }   
    public String validarServicioSelloDeTiempo(String urltsa,String usuario,String clave,int flag) throws IOException{
        try{
            try{
                URL tspUrl = new URL(urltsa);
            }catch (MalformedURLException ex){
                return "La url es incorrecta -> "+urltsa;
            }
             // hago esto para saber si el servidor de sello de tiempo configurado estÃ¡ respondiendo y si el usuario y la contraseÃ±a son correctos
            // Setup the time stamp request
            TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
            tsqGenerator.setCertReq(true);

            BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
            TimeStampRequest request = tsqGenerator.generate("1.3.14.3.2.26", "12345678912345678900".getBytes(), nonce);
            byte[] requestBytes = request.getEncoded();
            // Call the communications layer
            byte[] respBytes = new Tsa(urltsa,usuario,new helperClass().decrypt(clave)).getTSAResponse(requestBytes);
            //  flag==0 ?
//                    :new Tsa(urltsa,Base64.encodeBase64String(usuario.getBytes()),Base64.encodeBase64String(new helperClass().decrypt(clave).getBytes())).getTSAResponse(requestBytes);
            // Handle the TSA response
            TimeStampResponse response = new TimeStampResponse(respBytes);
            PKIFailureInfo pki = response.getFailInfo();
            if(pki != null){
                return "No hubo respuesta del servicio de sello de tiempo para el servicio: ["+urltsa + "] con el valor de usuario:["+usuario + "] o las credenciales son incorrectas.";
            }
            return "1" ;
        }catch(IOException e){ 
            if(e.getMessage().contains("401"))
                 return "Las credenciales ingresadas no son válidas para el servicio: ["+urltsa + "] con el valor de usuario:["+usuario + "]" ;
            else
                 return "El servicio  de sello de tiempo: ["+urltsa + "] con el valor de usuario:["+usuario + "] no está disponible!";
        }catch (TSPException ex){
             return "El servicio  de sello de tiempo: ["+urltsa + "] con el valor de usuario:["+usuario + "] no está disponible!";
        }  
    }
}
