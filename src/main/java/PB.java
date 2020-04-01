import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PB {

    static Cryptage cryp;
    
    static SecretKey key;

    public static void main(String[] args) throws FileNotFoundException, IOException, Base64DecodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        InetAddress addr;
        Socket client;
        PrintWriter out;
        BufferedReader in;
        String input;
        String userInput;
        boolean doRun = true;
        File f = new File("key.txt");
        FileReader fr = new FileReader(f.getAbsoluteFile());
        BufferedReader br = new BufferedReader(fr);
        String k2r = br.readLine();
        byte[] decodedKey = Base64.decode(k2r);;
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        //initialisation de la classe de cryptage/decryptage
        cryp = new Cryptage(key);
        br.close();
        fr.close();
        Scanner k = new Scanner(System.in);
        try {   
            client = new Socket("localhost", 4444);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            System.out.print("entrer votre msg> ");
            userInput = k.nextLine();
            //cryptage du message
            out.println(cryp.encrypt(userInput));
            out.flush();
            System.out.println("done");
            if (userInput.compareToIgnoreCase("bye") == 0) {
                System.out.println("shutting down");
                doRun = false;
            } else {
                while (doRun) {
                    input = in.readLine();
                    while (input == null) {
                        input = in.readLine();
                    }
                    /* dans notre exemple, nous vous presenterons le message codé
                    et le message décodé*/ 
                    System.out.println("message encodé : "+input);
                    input = cryp.decrypt(input);
                    
                    System.out.println("message décodé : "+ input);
                    
                    if (input.compareToIgnoreCase("bye") == 0) {
                        System.out.println("client shutting down from server request");
                        doRun = false;
                    } else {
                        System.out.print("entrer votre msg> ");
                        userInput = k.nextLine();
                        out.println(cryp.encrypt(userInput));
                        out.flush();
                        if (userInput.compareToIgnoreCase("bye") == 0) {
                            System.out.println("shutting down");
                            doRun = false;
                        }

                    }
                }
            }
            client.close();
            k.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
