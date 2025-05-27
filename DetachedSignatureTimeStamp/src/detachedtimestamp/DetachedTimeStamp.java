package detachedtimestamp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DetachedTimeStamp
{
    public static void main(String[] args)
    {
        Verifier vData = new Verifier();
        TimeStamper ts = new TimeStamper();
       
        try
        {   
            final File f = new File(".");
            String actual_path = f.getCanonicalPath();

            String source = actual_path + "\\books.xml";
            String dest = actual_path + "\\books.xml.tsr";

            String tsUrl = "http://tsu2.camerfirma.com:8884/tsc"; //"http://190.107.182.34/ts.inx";
            String tsUser = "stsap0106"; //"cpalomino15";
            String tsPw = "V1f56&EcX9"; //"s95Ktk3Ekf";

            byte[] tok_resp = ts.addTimeStampDetached(source, tsUrl, tsUser, tsPw);
            Files.write(Paths.get(dest), tok_resp);
            boolean v_data = vData.verifyTimeStampDetached(source, dest); // devuelve true si la firma se corresponde con el cargo original
            System.out.println(v_data);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }
    
}
