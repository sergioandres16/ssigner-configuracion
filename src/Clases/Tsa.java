package Clases;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;


/**
 *
 * @author Presentaciones
 */
public class Tsa {

    private String tspServer;
    private final String usuario;
    private final String clave;
    private String policyOid;
    
    public Tsa(String tspServer,String usuario,String clave) {
      this.tspServer = tspServer;
      this.usuario = usuario;
      this.clave = clave;
   }
  
   /**
    * Set the URL of the Tsa
     * @param tspServer
    */
    public void setTspServer(String tspServer) {
        this.tspServer = tspServer;
    }
   /**
    * Set the request policy
     * @param policyOid
    */
   public void setPolicyOid(String policyOid)  {
      this.policyOid = policyOid;
   }
   
   public TimeStampResponse getTimeStampResponse(DigestAlgorithm digestAlgorithm, byte[] digest) throws IOException {
      try 
      {
         byte[] respBytes ;

         // Setup the time stamp request
         TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
         tsqGenerator.setCertReq(true);
         
         if (policyOid != null)
             tsqGenerator.setReqPolicy(policyOid);
         
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        TimeStampRequest request = tsqGenerator.generate(digestAlgorithm.getOid(), digest, nonce);
        byte[] requestBytes = request.getEncoded();
         // Call the communications layer
        respBytes = getTSAResponse(requestBytes);

         // Handle the Tsa response
        TimeStampResponse response = new TimeStampResponse(respBytes);
        return response;
      } 
      catch (TSPException ex) 
      {
         throw new IOException("Invalid TSP response");
      }

   }

  
    public byte[] getTSAResponse(byte[] requestBytes) throws IOException {

        URL tspUrl = new URL(tspServer);
        URLConnection tsaConnection = tspUrl.openConnection();
        tsaConnection.setDoInput(true);
        tsaConnection.setDoOutput(true);
        tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
        String userPassword=usuario + ":" + clave;
        tsaConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeBase64String(userPassword.getBytes()));
        tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
        OutputStream out = tsaConnection.getOutputStream();
        out.write(requestBytes);
        out.close();
        InputStream inp = tsaConnection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
           baos.write(buffer, 0, bytesRead);
        }
        byte[] respBytes = baos.toByteArray();

        String encoding = tsaConnection.getContentEncoding();
        if (encoding != null && encoding.equalsIgnoreCase("base64")) {
           respBytes = base64Decode(respBytes);
        }
        return (respBytes);
    }
   
    /**
     * Decodes a Base64 String into bytes.
     *
     * @param binaryData
     * @return
     */
    
    public static byte[] base64Decode(byte[] binaryData) 
    {
        return Base64.decodeBase64(binaryData);//Base64.decodeBase64(binaryData);
    } 
}

