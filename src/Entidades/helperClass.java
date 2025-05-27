package Entidades;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import static java.lang.Math.abs;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class helperClass{
    // http://stackoverflow.com/a/35452697/111110
    public boolean path(String path){
        if(path.toLowerCase().equals("null"))
            return false;
                
        try{
            Paths.get(path);
        }
        catch (InvalidPathException |  NullPointerException ex){
            return false;
        }
        return true;
    }
    
    public DefaultTableModel ResulsetToData(ResultSet rs) throws Exception{
        DefaultTableModel dTable = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        
        int numcols = rs.getMetaData().getColumnCount();  

        for (int i = 0; i < numcols; i++)
            dTable.addColumn(rs.getMetaData().getColumnName(i + 1));
        while (rs.next()){    
            Object [] rowData = new Object[numcols];
            for (int i = 0; i < rowData.length; i++)
               rowData[i] = rs.getObject(i+1);
            dTable.addRow(rowData);                
        }
        return dTable;
    }

    // http://stackoverflow.com/a/7762892
    private final String ALGORITHM = "AES";
    private final byte[] keyValue = new byte[] { '2', '-', 'q', '+', '8', 'b', 'j', 'D', '#', 'E', 'g', '9', '.', 'R', '-', 'y' };

    File lockedFile;
    FileLock fileLock;
    RandomAccessFile randomAccessFile;
    
    public String encrypt(String valueToEnc) {
        try {
            if(valueToEnc == null || valueToEnc.length() == 0)
                return "";
            Key key = new SecretKeySpec(keyValue, ALGORITHM);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            String encryptedValue = Base64.encodeBase64String(encValue);
            return encryptedValue;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            return "";
        }
    }

    public String decrypt(String encryptedValue)  {
        try {
            if(encryptedValue == null || encryptedValue.length() == 0)
                return "";
            Key key = new SecretKeySpec(keyValue, ALGORITHM);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.decodeBase64(encryptedValue);
            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        } catch (Exception ex) {
//            Logger.getLogger(helperClass.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return "";
    }
    
    public String addPathSeparator(String ruta){
        if(ruta.length() == 0)
            return ruta;
        if (!String.valueOf(ruta.charAt(ruta.length() - 1)).equals(File.separator))
            ruta += File.separator;
        return ruta;
    }
    
    public String getCertFolder(){
        return getFilesFolder("mSignerCerts");
    }
    
    public String getImgFolder(){
        return getFilesFolder("mSignerImages");
    }
    
    // esto es para que haya una sola instancia de la aplicación, creo un lock exclusivo a un archivo específico
    public void getLockFile() throws FileNotFoundException, IOException, Exception{
        String nombreF =  getFilesFolder("mSignerLockFile") + File.separator + "mSignerLock.dat";
        lockedFile = new File(nombreF);
        randomAccessFile = new RandomAccessFile(lockedFile, "rw");
        fileLock = randomAccessFile.getChannel().tryLock();
        if (fileLock == null)
            throw new Exception();
    }
    
    public void releaseLockFile(){
        if (fileLock != null) 
            try{
                fileLock.release();
                randomAccessFile.close();
                lockedFile.delete();
            } catch (Exception e) {
            }
    }
        
     // guardar la imagen al disco duro y devuelve la ruta al nuevo archivo
    public String guardarImagen(int pid, String nombre, byte[] imagen){
        return guardarFile(pid, nombre, "mSignerImages", imagen);
    }
    
    public String guardarCertificdo(int pid, String nombre, byte[] certificado){
        return guardarFile(pid, nombre, "mSignerCerts", certificado);
    }
    
    private String getFilesFolder(String nombreFolder){
        String ruta = "";
        
        // en Win2003 devuelve nulo
        if(System.getenv("LOCALAPPDATA") == null || System.getenv("LOCALAPPDATA").length() == 0)
            // intentamos buscar acá
            ruta = new File(System.getProperty("java.io.tmpdir")).getParent() + File.separator + nombreFolder;
        else
            ruta = System.getenv("LOCALAPPDATA") + File.separator + nombreFolder;
        
        // intento aceder al directorio, puede que ya exista
        final File fRuta = new File(ruta);
        
        if (!fRuta.exists()){
            try{
                Files.createDirectories(Paths.get(ruta)); // si no existe, lo creo
                if (!fRuta.exists()){
                    // un último intento, let's pray
                    final File f = new File(".");
                    ruta = f.getCanonicalPath() + File.separator + nombreFolder;
                    Files.createDirectories(Paths.get(ruta));
                }
            }catch (IOException ex){
                Logger.getLogger(helperClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // si a este punto no existe el directorio ya no intento nada más
        if (!fRuta.exists())
            return "";
        
        return ruta;
    }
    
    private String guardarFile(int pid, String nombre, String nombreFolder, byte[] archivo){
        String ruta = getFilesFolder(nombreFolder);
        
        if (ruta.length() == 0)
            return "";
        Path pRuta = Paths.get(ruta);
        ruta = pRuta.resolve(String.valueOf(pid) + nombre).toString();
        
        //http://www.java-examples.com/write-byte-array-file-using-fileoutputstream
        try (FileOutputStream fos = new FileOutputStream(ruta)){
            fos.write(archivo);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException ex){
            Logger.getLogger(helperClass.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex){
            Logger.getLogger(helperClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ruta;
    }
    
    public List<float[]> getCoordenadas(byte[] archivoPDF, String rutaPdf, String elPassword, String etiqueta) throws Exception
    {
        try (InputStream fis = new ByteArrayInputStream(archivoPDF); PDDocument document1 = elPassword == null? PDDocument.load(fis) : PDDocument.load(fis, elPassword))
        {
            RandomAccessFileOrArray  raf = new RandomAccessFileOrArray(rutaPdf, false, true);
            
            PdfReader reader = elPassword == null ? new PdfReader(raf, null) :  new PdfReader(raf, elPassword.getBytes());
            AcroFields acroFields = reader.getAcroFields();
            List<String> signatureNames = acroFields.getSignatureNames();
            
            int numfirmas = signatureNames.size();
            float[][] fieldPositions = new float[numfirmas][5];
            
            int i = 0;
            for(String sig : signatureNames)
            {
                List<AcroFields.FieldPosition> positions = acroFields.getFieldPositions(sig);
                com.itextpdf.text.Rectangle rect = positions.get(0).position; // In points:
                fieldPositions[i] = new float[]{positions.get(0).page, rect.getLeft(), rect.getTop(), rect.getRight(), rect.getBottom()};
                i++;
            }
            
            reader.close();
            reader = null;
            PDFTextStripper pdfStripper = new GetCharLocalizationAndSize();
            
            float[] coords = new float[] {-1, -1, -1};
            List<float[]> coordenadas = new ArrayList<>();
            
            etiqueta = "[" + etiqueta + "]";
            
            ((GetCharLocalizationAndSize)pdfStripper).setTag(etiqueta);
            
            for (int k = 1; k <= document1.getNumberOfPages(); k++)
            {
                pdfStripper.setStartPage(k);
                pdfStripper.setEndPage(k);
                String text01 = pdfStripper.getText(document1);

                String[] text = text01.split("\\r?\\n");
                
                for (int ii = 0; ii < text.length; ii++)
                {
                    if(text[ii] == null || text[ii].trim().isEmpty())
                        continue;
                    
                    if(text[ii].toUpperCase().contains(etiqueta.toUpperCase()))
                    {
                        String[] temp = text[ii].split("\\Q][\\E"); // a veces se dan las coordenadas de más de un texto

                        for (int j = 0; j < temp.length; j++)
                        {
                            if(temp[j] == null || temp[j].trim().isEmpty())
                                continue;
                            
                            if(temp[j].toUpperCase().contains(etiqueta.toUpperCase()))
                            {
                                String[] temp2 = temp[j].split(",");
                                coords = new float[] { k, 
                                    Float.valueOf(temp2[0].replace("[", "").replace("]", "")) - 20, 
                                    document1.getPage(k - 1).getMediaBox().getHeight() - Float.valueOf(temp2[1].replace("[", "").replace("]", "")) - 6};
                                   
                                if(numfirmas == 0)
                                    coordenadas.add(coords);
                                else
                                {
                                    boolean coorFirFound = false;
                                    
                                    for(int i0 = 0; i0 < numfirmas; i0++)
                                    {
                                        int coord = (int)(abs(coords[1] - fieldPositions[i0][1]) + abs(coords[2] - fieldPositions[i0][4]));

                                        if(k == (int)fieldPositions[i0][0] && coord < 5 && (int)fieldPositions[i0][0] == (int)coords[0])
                                        {
                                            coorFirFound = true;
                                            break;
                                        }
                                    }
                                    
                                    if(!coorFirFound)
                                        coordenadas.add(coords);
                                }
                            }
                        }
                    }
                }

            }
            
            return coordenadas;
        }
    }
    
}
