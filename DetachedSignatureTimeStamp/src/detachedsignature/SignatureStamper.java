package detachedsignature;

import java.io.File;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.xml.bind.ValidationException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableFile;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.util.Store;

// ******** uses black magic to make it compatible with BC 1.38 **************
public class SignatureStamper
{ 
    // http://examples.javacodegeeks.com/java-basics/java-enumeration-example/
    public enum repositorios 
    { 
        WINDOWSMY { @Override public String getName() { return "WINDOWS-MY"; } }, 
        PKCS12 { @Override public String getName() { return "PKCS12"; } }, 
        PKCS11 { @Override public String getName() { return "PKCS11"; } };
        
        public abstract String getName();
    }
    
    String aliasp12 = "";
    
    public HashMap<Integer, String[]> getWindowCertificates() throws Exception
    {
        String OS = System.getProperty("os.name", "generic").toLowerCase();
            
        if (OS.contains("win")) 
        {
            KeyStore ks = KeyStore.getInstance(repositorios.WINDOWSMY.getName());
            ks.load(null);
            Enumeration listacert = ks.aliases();

            int i = 0;
            HashMap<Integer, String[]> infoCertificados = new HashMap<>();
            
            while(listacert.hasMoreElements())
            {
                String aliast = (String)listacert.nextElement();
                //mListAliases.addElement(aliast);
                X509Certificate xcert = (X509Certificate)ks.getCertificate(aliast);
                String c_name = getCN(xcert);
                infoCertificados.put(i, new String[]{aliast, c_name});
                i++;
            }
            return infoCertificados;
        }
        else
            return null;
    }
    
    public String getP12Alias()
    {
        return aliasp12;
    }
        
    public byte[] createSignature(X509Certificate cert, final PrivateKey key, final String archivoorigen, String tsaURL, String tsaUsername, String tsaPassword) throws Exception
    {
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
	/*
	 * addSigner requires the certificate to be able to calculate the key selector.
	 */
	generator.addSigner(key, cert, CMSSignedDataGenerator.DIGEST_SHA256);
	List<X509Certificate> certList = new LinkedList<>();
	certList.add(cert);
	CertStore certStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList));
	generator.addCertificatesAndCRLs(certStore);
	CMSProcessable content = new CMSProcessableFile(new File(archivoorigen));
	CMSSignedData sigData = generator.generate(content, false, (String) null);

        // (byte[])gen.getGeneratedDigests().get("1.3.14.3.2.26") es el hash para sha1

        if (tsaURL.length() > 0)
            sigData = addTimeStamp(sigData, tsaURL, tsaUsername, tsaPassword);
        
        return sigData.getEncoded();
    }    
    
    private CMSSignedData addTimeStamp(CMSSignedData signedData, String tsaURL, String tsaUsername, String tsaPassword) throws Exception
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        Collection ss = signedData.getSignerInfos().getSigners();
        SignerInformation si = (SignerInformation) ss.iterator().next();
        
        Store certStore = signedData.getCertificates();
        //CertStore certStore = signedData.getCertificatesAndCRLs("Collection", "BC");
        
        Collection certCollection = certStore.getMatches(si.getSID());
        //Collection certCollection = certStore.getCertificates(si.getSID());
        Iterator certIt = certCollection.iterator();
        X509CertificateHolder certHolder = (X509CertificateHolder)certIt.next();
        X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
        boolean b = si.verify(cert, "BC");
        //boolean b = si.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert));
        
        if (!b)
            throw new ValidationException("verification failed!");

        // hash the signature
        byte[] signDigest = MessageDigest.getInstance("SHA-256", "BC").digest(si.getSignature());

        TimeStampToken tok = (new detachedtimestamp.TimeStamper()).GetTimeStampToken(signDigest, tsaURL, tsaUsername, tsaPassword);
        
        ASN1InputStream asn1InputStream = new ASN1InputStream(tok.getEncoded());
        ASN1Object tstDER = asn1InputStream.readObject();
        DERSet ds = new DERSet(tstDER);

        org.bouncycastle.asn1.cms.Attribute a = new org.bouncycastle.asn1.cms.Attribute(new DERObjectIdentifier("1.2.840.113549.1.9.16.2.14"), ds);
        DEREncodableVector dv = new DEREncodableVector();
        dv.add(a);
        AttributeTable at = new AttributeTable(dv);
        si = SignerInformation.replaceUnsignedAttributes(si, at);
        ss.clear();
        ss.add(si);
        SignerInformationStore sis = new SignerInformationStore(ss);

        signedData = CMSSignedData.replaceSigners(signedData, sis);
        return signedData;
    }
            
    private String getCN(X509Certificate certificado)
    {
        String dn = certificado.getSubjectDN().toString();
        String cn = "";

        try
        {
            LdapName ln = new LdapName(dn);

            for(Rdn rdn : ln.getRdns())
                if(rdn.getType().equalsIgnoreCase("CN")) 
                {
                    cn = rdn.getValue().toString();
                    break;
                }
        }
        catch(Exception e) {}
        
        return cn;
    }
    
}
