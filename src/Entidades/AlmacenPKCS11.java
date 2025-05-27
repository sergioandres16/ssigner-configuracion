/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Clases.Repositorio;
import Global.CLog;
import Interfaces.AlmacenProveedor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.util.Arrays;
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKF_HW_SLOT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKF_REMOVABLE_DEVICE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKF_RW_SESSION;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKF_SERIAL_SESSION;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKF_TOKEN_PRESENT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKU_USER;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CK_EFFECTIVELY_INFINITE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CK_UNAVAILABLE_INFORMATION;
import sun.security.pkcs11.wrapper.PKCS11Exception;
public class AlmacenPKCS11  implements  AlmacenProveedor{
    
    private boolean libreria_con_error=false;
    private  static String windowsDir_iKey;
    private static byte[] PKCS11_CONFIG_IKEY;
    private static final String SUN_PKCS11_PROVIDER_CLASS = "sun.security.pkcs11.SunPKCS11";
 
    
    @Override
    public KeyStore RecuperarAlmacen() throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public KeyStore RecuperarAlmacen(char[] pin, String p_libreria_ruta) throws KeyStoreException {
         PKCS11 p11 = null;
        long hSession = -1;
        Provider libreria_proveedor = null;
        File libreria_ruta = null ;
        libreria_con_error = false;
        ByteArrayInputStream configStream = null;
        try { 
            libreria_ruta=new File(p_libreria_ruta);
            windowsDir_iKey="name=SmartCard2\n" + "library="+libreria_ruta.getPath()+"\nshowInfo=true";///home/christofer/Escritorio/libacsccid.so";// Archivo+
            PKCS11_CONFIG_IKEY  = windowsDir_iKey.getBytes();
            System.out.println(windowsDir_iKey);
            configStream = new ByteArrayInputStream(PKCS11_CONFIG_IKEY);
            Object obj_proveedor=createSunPKCS11Provider(configStream); 
            if(obj_proveedor instanceof String){
                configStream.close();
                return null;
            }
            libreria_con_error=true;
            libreria_proveedor = (Provider)obj_proveedor;    
            
            if (Security.getProvider(libreria_proveedor.getName())!=null) {// Provider name + SunPKCS11 prefix
                Security.removeProvider(libreria_proveedor.getName());
            }
//            libreria_proveedor.remove(libreria_ruta.getName());
            Security.addProvider(libreria_proveedor);
            System.out.println("La libreria fue inicializada.");
            CK_C_INITIALIZE_ARGS initArgs = new CK_C_INITIALIZE_ARGS();
            
            p11 = PKCS11.getInstance(p_libreria_ruta, "C_GetFunctionsList", initArgs, false);
            System.out.println("Mostrando info del proveedor");
            System.out.println(""+p11.C_GetInfo());
            long[] Slots =  p11.C_GetSlotList(false);
            long[] Slots_con_dispositivos=  p11.C_GetSlotList(true);
            long Slot_dispositivo_seleccionado = 0;
            if(Slots.length==0){
                new CLog().archivar_log("No se encontrado slots presentes validos para disposisitvos.");
                System.out.println("No se encontrado slots presentes validos para disposisitvos.");
                return null;
            }
            else{
                if(Slots_con_dispositivos.length==0){
                    new CLog().archivar_log("Inserte dispositivo o DNIe para firmar.");
                    System.out.println("Inserte dispositivo o DNIe para firmar.");
                    return null;
                }else
                if(Slots_con_dispositivos.length>1){
                    System.out.println("Se hallaron dos dispositivos de firma validos.");
                    for (long slot : Slots_con_dispositivos){
                         CK_SLOT_INFO token_Info = p11.C_GetSlotInfo(slot);
                         System.out.println(""+Arrays.toString(token_Info.manufacturerID));
                    }
                }else{
                    Slot_dispositivo_seleccionado=Slots_con_dispositivos[0];
                }
            }
//            long session=CK_INVALID_HANDLE;
//            long object = CK_INVALID_HANDLE;
            for (long Slot : Slots){
                /*Capturamos el slot que tiene un dispositivo de firma*/
                if(Slot==Slot_dispositivo_seleccionado){
                    CK_SLOT_INFO Slot_Info = p11.C_GetSlotInfo(Slot_dispositivo_seleccionado);
                    if(Slot_Info.flags!=(CKF_TOKEN_PRESENT | CKF_REMOVABLE_DEVICE | CKF_HW_SLOT )){
                        if((Slot_Info.flags-(CKF_REMOVABLE_DEVICE | CKF_HW_SLOT))==CKF_TOKEN_PRESENT){
                            System.out.println("El token no esta presente");
                        }
                        else if((Slot_Info.flags-( CKF_HW_SLOT| CKF_TOKEN_PRESENT))==CKF_REMOVABLE_DEVICE){
                            System.out.println("El tarjeta ha sido removido ");
                        }
                        else if((Slot_Info.flags-(CKF_REMOVABLE_DEVICE | CKF_TOKEN_PRESENT))==CKF_HW_SLOT){
                            System.out.println("El slot para el dispositivo no es valido");
                        }
                        else {
                            System.out.println("Todo correcto.");
                            System.out.println("Utilizando Slot con dispositivo correcto.");
                        }
                    }
                    else {
                            System.out.println("Todo correcto.");
                            System.out.println("Utilizando Slot con dispositivo correcto."+new String(Slot_Info.manufacturerID));
                    }
                    break;
                }
            }
            hSession = p11.C_OpenSession(Slot_dispositivo_seleccionado, CKF_SERIAL_SESSION| CKF_RW_SESSION   , null, null);       
            
            CK_TOKEN_INFO token_Info = p11.C_GetTokenInfo(Slot_dispositivo_seleccionado);
           
            if(token_Info.ulMaxSessionCount == CK_UNAVAILABLE_INFORMATION){
                System.out.println("La sesion ha sido abierta en un proceso anterior");
            } 
            else if (token_Info.ulMaxSessionCount == CK_EFFECTIVELY_INFINITE){
                System.out.println("La aplicación puede abrir varias sesiones si lo desea.");
            }
            p11.C_Login( hSession, CKU_USER,pin);
//            p11.C_Logout(hSession);
//            p11.C_CloseSession(hSession);
            KeyStore ks= KeyStore.getInstance(Repositorio.TOKEN);//,libreria_proveedor);
            ks.load(new FileInputStream(libreria_ruta),pin);
            configStream.close();
            return ks;
        } catch (IOException ex) {
            new CLog().archivar_log(ex.toString());
            System.out.println(""+ex.toString());
            return null;
           
        } catch (PKCS11Exception ex) {
            switch (ex.getMessage()) {
                case " CKR_CRYPTOKI_NOT_INITIALIZED":
                    new CLog().archivar_log("El dispositivo criptografico no esta inicializado.");
                    System.out.println("El dispositivo criptografico no esta inicializado.");
                    CLog.setMensaje_error("El dispositivo criptografico no esta inicializado.");
                    break;
                case "CKR_PIN_LOCKED":
                    System.out.println("El pin del token está bloqueado.");
                    new CLog().archivar_log("El pin del token está bloqueado.");
                    CLog.setMensaje_error("El pin del token está bloqueado.");
                    libreria_con_error = true;
                    break;
                case "CKR_ARGUMENTS_BAD":
                    System.out.println("Argumento no valido para lo atributos del modulo de la libreria.");
                    new CLog().archivar_log("Argumento no valido para lo atributos del modulo de la libreria.");
                    CLog.setMensaje_error("Argumento no valido para lo atributos del modulo de la libreria.");
                    libreria_con_error = true;
                    break;
                case "CKR_PIN_INCORRECT":
                    System.out.println("El pin es incorrecto.");
                    new CLog().archivar_log("El pin es incorrecto.");
                    CLog.setMensaje_error("El pin es incorrecto.");
                    libreria_con_error = true;
                    break;
                case "CKR_TOKEN_NOT_PRESENT":
                    System.out.println("El token no esta insertado verifique por favor.");
                    new CLog().archivar_log("El token no esta insertado verifique por favor.");
                    CLog.setMensaje_error("El token no esta insertado verifique por favor.");
                    libreria_con_error = true;
                    break;
                case "CKR_MECHANISM_INVALID":
                    System.out.println("El mecanismo para firmar no es valido.");
                    new CLog().archivar_log("El mecanismo para firmar no es valido.");
                    CLog.setMensaje_error("El mecanismo para firmar no es valido.");
                    libreria_con_error = true;
                    break;
                case "CKR_SESSION_HANDLE_INVALID":
                    System.out.println("La sesion a recuperar no es válida.");
                    new CLog().archivar_log("La sesion a recuperar no es válida.");
                    CLog.setMensaje_error("La sesión a recuperar no es válida.");
                    libreria_con_error = true;
                    break;
                case "CKR_DEVICE_ERROR":
                    System.out.println("Surgio un error al encontrar el dispositivo.");
                    new CLog().archivar_log("Surgio un error al encontrar el dispositivo.");
                    CLog.setMensaje_error("Surgió un error al encontrar el dispositivo.");
                    libreria_con_error = true;
                    break;
                case "CKR_TOKEN_NOT_RECOGNIZED":
                  try {
                    KeyStore keystorePkcs11;
                    KeyStore.CallbackHandlerProtection callback = new KeyStore.CallbackHandlerProtection(new SimpleCallbackHandler(null,pin)) ;
                    KeyStore.Builder builder = KeyStore.Builder.newInstance(Repositorio.TOKEN, libreria_proveedor, callback);
                    keystorePkcs11 = builder.getKeyStore();
                    KeyStore.PasswordProtection pp = new KeyStore.PasswordProtection(pin);
                    InputStream in = getClass().getResourceAsStream(windowsDir_iKey);
                    keystorePkcs11.load(in, pp.getPassword());
                } catch (IOException ex1) {
                    Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex1);
                } catch (NoSuchAlgorithmException ex1) {
                    Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex1);
                } catch (CertificateException ex1) {
                    Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex1);
                }
                    break;
                case "CKR_USER_ALREADY_LOGGED_IN":
                    try {
                        System.out.println("El sesion del el token ya está iniciado");
                        KeyStore ks= KeyStore.getInstance(Repositorio.TOKEN,libreria_proveedor);
                        ks.load(new FileInputStream(libreria_ruta),pin);
                        return ks;
                    } catch (IOException ex1) {
                        new CLog().archivar_log(ex1.getMessage());
                        Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex1);                       
                    } catch (NoSuchAlgorithmException ex1) {
                        new CLog().archivar_log(ex1.getMessage());
                        Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (CertificateException ex1) {
                        new CLog().archivar_log(ex1.getMessage());
                        Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                case "CKR_SESSION_PARALLEL_NOT_SUPPORTED":
                    System.out.println("No soporta la sesion en paralelo. Ingrese solo un dispositivo de firma.");
                    new CLog().archivar_log("No soporta la sesion en paralelo. Ingrese solo un dispositivo de firma.");
                    CLog.setMensaje_error("No soporta la sesion en paralelo. Ingrese solo un dispositivo de firma.");
                    libreria_con_error = true;
                    break;
                default:
                    new CLog().archivar_log(ex.getMessage());
                    Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            }
        } catch (NoSuchAlgorithmException ex) {
            new CLog().archivar_log(ex.getMessage());
            Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            new CLog().archivar_log(ex.getMessage());
            Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            if(ex.getMessage().equals("Token has been removed")){
                new CLog().archivar_log(ex.getMessage());
                System.out.println("El token ha sido removido.");
                CLog.setMensaje_error("El token ha sido removido.");
            }
            else{
                new CLog().archivar_log(ex.getMessage());
                Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        finally{
             try {
                 if(configStream!=null)
                     configStream.close();
             } catch (IOException ex) {
                 Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
        try {
            if(p11!=null){
                if(hSession!=-1){
                    p11.C_CloseSession(hSession);
                }
            }  
        } catch (PKCS11Exception ex) {
            new CLog().archivar_log(ex.getMessage());
            Logger.getLogger(AlmacenPKCS11.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;       
	}

    @Override
    public KeyStore RecuperarAlmacen(char[] Clave, byte[] Archivo) throws KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private Object createSunPKCS11Provider(InputStream configStream) throws KeyStoreException {
	try {
            Class sunPkcs11Class = Class.forName(SUN_PKCS11_PROVIDER_CLASS);
            Constructor pkcs11Constr = sunPkcs11Class.getConstructor(InputStream.class);
            Provider pkcs11Provider = (Provider) pkcs11Constr.newInstance(configStream);
            return pkcs11Provider;
	} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ex) {
            if(ex.getCause().getMessage().equals("Initialization failed")){
                new CLog().archivar_log("Error al inicializar el proveedor del token.");
                return "Error al inicializar el proveedor del token.";
            }
            else if(ex.getCause().getMessage().equals("java.lang.reflect.InvocationTargetException")){
                new CLog().archivar_log("No se logró obtener los datos del chip  en  la tarjeta o token.");
                return "No se logró obtener los datos del chip  en  la tarjeta o token.";
            }
            else{
                new CLog().archivar_log(ex.toString());
                return ex.toString();
            }
	}
    }

    public boolean isLibreria_con_error() {
        return libreria_con_error;
    }
    
}
