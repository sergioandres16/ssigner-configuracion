package Almacen;

import javax.security.auth.callback.*;

public class SimpleCallbackHandler implements CallbackHandler{
   /**
    * Constructs the callback handler.
    * @param username the user name
    * @param password a character array containing the password
    */
   public SimpleCallbackHandler(String username, char[] password)
   {
      this.username = username;
      this.password = password;
   }

   public void handle(Callback[] callbacks)
   {
      for (Callback callback : callbacks)
      {
           try {  
                for (int i = 0; i < callbacks.length; i++) {  
                    System.err.println("CALLBACK at "+i+" "+callbacks[i].toString());  
                    if (callbacks[i] instanceof PasswordCallback) {  
                       // System.err.println("CALLBACK at "+i+" "+callbacks[i].toString()+" matches ");  
                        PasswordCallback pc = (PasswordCallback) callbacks[i];  
                        //System.err.print(pc.getPrompt());  
                        //System.err.flush();  
                        pc.setPassword(password);  
                    } else {  
                        System.err.println(" "+i+" "+callbacks[i].toString()+" no coinciden ");  
                    }  
                }  
            } catch(Exception ex) {  
               // throw new Exception(ex);
               // Logger.getLogger(SimpleCallbackHandler.class.getName()).log(Level.SEVERE, null, ex);  
            } 
          
          
          
         if (callback instanceof NameCallback)
         {
            ((NameCallback) callback).setName(username);
         }
         else if (callback instanceof PasswordCallback)
         {
            ((PasswordCallback) callback).setPassword(password);
         }
      }
   }

   private String username;
   private char[] password;
}
