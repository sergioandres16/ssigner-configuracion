package Metodos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Base64;

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
}
