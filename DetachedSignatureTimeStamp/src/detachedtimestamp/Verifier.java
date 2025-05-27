package detachedtimestamp;

import Digests.DigestAlgorithm;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.TimeStampTokenInfo;

public class Verifier
{
    public boolean verifyTimeStampDetached(String filepath, String trspath) throws TSPException, IOException, NoSuchAlgorithmException, NoSuchProviderException
    {
        FileInputStream inresp = new FileInputStream(trspath);// response

        TimeStampResponse resp = new TimeStampResponse(inresp);
        TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();
        reqGen.setCertReq(true);

        byte[] bFile = Files.readAllBytes(Paths.get(filepath));

        MessageDigest sha1 = MessageDigest.getInstance("SHA1", new BouncyCastleProvider());
        sha1.update(bFile);         
        byte[] hashData = sha1.digest();
        BigInteger nonce = resp.getTimeStampToken().getTimeStampInfo().getNonce();
        TimeStampRequest req = reqGen.generate(DigestAlgorithm.SHA1.getOid(), hashData, nonce);

        try
        {
            resp.validate(req);  // if it fails a TSPException is raised
            TimeStampToken tsToken = resp.getTimeStampToken();
            TimeStampTokenInfo tsInfo = tsToken.getTimeStampInfo();
            SignerId signer_id = tsToken.getSID();
            BigInteger cert_serial_number = signer_id.getSerialNumber();
            
            System.out.println ("Generation time " + tsInfo.getGenTime());
            System.out.println ("Signer ID serial "+signer_id.getSerialNumber());
            System.out.println ("Signer ID issuer "+signer_id.getIssuer().toString());
            return true;
        }
        catch(Exception ex)
        {
            System.err.println(ex);
            return false;
        }

        
    }
}
