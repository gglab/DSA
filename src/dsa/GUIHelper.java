/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsa;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author glabg
 */
public class GUIHelper {

    private static final Class helperClass = GUIHelper.class;

    public static void actionLocator(String action, File file) {
        try {
            Method actionMethod = helperClass.getMethod(action, File.class);
            actionMethod.invoke(null, file);
        } catch (Exception ex) {
            Logger.getLogger(GUIHelper.class.getName()).log(Level.SEVERE, null, ex);
            DSAMainWindow.showError(ex, "Błąd Pliku", "Błąd Pliku");
        }
    }

    public static void readFileToSign(final File file) {
        DSAMainWindow.getInstance().setInputFile(file);
    }
    
    public static void saveDS(final File file) {
        DSAMainWindow.getInstance().saveDS(file);
    }
    
    public static void readFileToVerification(final File file){
         DSAMainWindow.getInstance().setFileToVerification(file);
    }
    
    public static void readDSFromFile(final File file){
         DSAMainWindow.getInstance().setDSFromFile(file);
    }
}
