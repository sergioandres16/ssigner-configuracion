/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Almacen;

/**
 *
 * @author subinfo
 */
import javax.security.auth.callback.*;

/**
 * This simple callback handler presents the given user name and password.
 * @version 1.0 2004-09-14
 * @author Cay Horstmann
 */
public class SimpleCallbackHandler2 implements CallbackHandler{
   /**
    * Constructs the callback handler.
    * @param username the user name
    * @param password a character array containing the password
    */
   public SimpleCallbackHandler2(String username, char[] password)
   {
      this.username = username;
      this.password = password;
   }

   public void handle(Callback[] callbacks)
   {
      for (Callback callback : callbacks)
      {
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
