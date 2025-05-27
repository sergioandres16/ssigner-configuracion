package detachedtimestamp;

import Digests.DigestAlgorithm;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

public class TimeStamper
{
    public byte[] addTimeStampDetached(String filePath, String tsaURL, String tsaUsername, String tsaPassword) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, Exception
    {
        byte[] bFile = Files.readAllBytes(Paths.get(filePath));

        MessageDigest sha1 = MessageDigest.getInstance("SHA-256", new BouncyCastleProvider());
        sha1.update(bFile);
        byte[] hashData = sha1.digest();
        System.out.print("generated hash data");
        // debe devolver los bytes del TimeStampResponse
        TimeStampResponse tsp = getTimeStampResponse(hashData, tsaURL, tsaUsername, tsaPassword);
        return tsp.getEncoded();
    }
    
    public TimeStampToken GetTimeStampToken(byte[] imprint, String tsaURL, String tsaUsername, String tsaPassword) throws Exception
    {
        TimeStampResponse response = getTimeStampResponse(imprint, tsaURL, tsaUsername, tsaPassword);
        // @todo: validate the time stap certificate chain (if we want assure we do not sign using an invalid timestamp).
        // extract just the time stamp token (removes communication status info)
        TimeStampToken tsToken = response.getTimeStampToken();

        if (tsToken == null)
            throw new Exception("tsa.1.failed.to.return.time.stamp.token.2");

        // TimeStampTokenInfo info = tsToken.TimeStampInfo; // to view details
        // byte[] encoded = tsToken.GetEncoded();
        // Update our token size estimate for the next call (padded to be safe)
        // int tokSzEstimate = encoded.Length + 32;
        return tsToken;
    }    
    
    private TimeStampResponse getTimeStampResponse(byte[] imprint, String tsaURL, String tsaUsername, String tsaPassword) throws TSPException, IOException, Exception
    {
        // Setup the time stamp request
        TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
        tsqGenerator.setCertReq(true);

        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        TimeStampRequest request = tsqGenerator.generate(DigestAlgorithm.SHA256.getOid(), imprint, nonce);

        byte[] requestBytes = request.getEncoded();
        // Call the communications layer
        byte[] respBytes = getTSAResponse(requestBytes, tsaURL, tsaUsername, tsaPassword);

        // Handle the TSA response
        TimeStampResponse response = new TimeStampResponse(respBytes);
        // validate communication level attributes (RFC 3161 PKIStatus)
        response.validate(request);

        PKIFailureInfo failure = response.getFailInfo();

        int value = (failure == null) ? 0 : failure.intValue();

        if (value != 0)
            // @todo: Translate value of 15 error codes defined by PKIFailureInfo to string
            throw new Exception("invalid.tsa.1.response.code.2");

        return response;
    }
    
    private byte[] getTSAResponse(byte[] requestBytes, String tsaURL, String tsaUsername, String tsaPassword) throws IOException 
    {
        // Setup the TSA connection
        URL tspUrl = new URL(tsaURL);
        URLConnection tsaConnection = tspUrl.openConnection();

        tsaConnection.setDoInput(true);
        tsaConnection.setDoOutput(true);
        tsaConnection.setUseCaches(false);
        tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
        String userPassword = tsaUsername + ":" + tsaPassword;
        tsaConnection.setRequestProperty("Authorization", "Basic " + userPassword.getBytes());
        tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
        
        OutputStream out = tsaConnection.getOutputStream();
        out.write(requestBytes);
        InputStream inp;
        String encoding;
        URLConnection tsaConnection2 = null;
        
        try
        {
            inp = tsaConnection.getInputStream();
        }
        catch(IOException ee)
        {
            out.close();
            
            tsaConnection2 = tspUrl.openConnection();

            tsaConnection2.setDoInput(true);
            tsaConnection2.setDoOutput(true);
            tsaConnection2.setUseCaches(false);
            tsaConnection2.setRequestProperty("Content-Type", "application/timestamp-query");
            tsaConnection2.setRequestProperty("Authorization", "Basic " + Base64.encodeBase64String(userPassword.getBytes()) );
                                    
            out = tsaConnection2.getOutputStream();
            out.write(requestBytes);
            // Get TSA response as a byte array
            inp = tsaConnection2.getInputStream();
        }
        
        encoding = tsaConnection.getContentEncoding();
        out.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        
        while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) 
           baos.write(buffer, 0, bytesRead);
        
        byte[] respBytes = baos.toByteArray();

        encoding = tsaConnection2 == null ? tsaConnection.getContentEncoding() : tsaConnection2.getContentEncoding();
        
        if (encoding != null && encoding.equalsIgnoreCase("base64"))
           respBytes = base64Decode(respBytes);
        
        return respBytes;
    }
    
    private static byte[] base64Decode(byte[] binaryData)
    {
        return Base64.decodeBase64(binaryData);
    }
}
