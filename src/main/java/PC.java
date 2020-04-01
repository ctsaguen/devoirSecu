import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class PC {

    static ServerSocket server;
    static int clientID = 0;
    static SecretKey key;

    public static void main(String ard[]) {
        //lancement de l'algo de creation de clé de chiffrement
            createKey();
        //lancement du serveur 
            go(); 
    }
    
    public static void createKey(){
        try {
            File file = new File("key.txt");
            //génération de la clé dans un fichier txt
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            SecretKey key = generator.generateKey();
            System.out.println("la clé disponible dans le fichier key.txt");
            String k2w;
            byte[] encoded = key.getEncoded();
            k2w = Base64.encode(encoded);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(k2w);
            bw.close();
            fw.close();
            System.out.println("la clé est : " + k2w);
            server = new ServerSocket(4444, 5);//5 connexions clientes au plus sur le serveur 4444
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    public static void go() {

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true)//
                    {
                        try {
                            Socket client = server.accept();
                            // Faire tourner le socket qui s'occupe de ce client dans son propre thread et revenir en attente de la prochaine connexion
                            // Le chat avec l'entit� connect�e est encapsul� par une instance de ChatServer
                            Thread tAccueil = new Thread(new ChatServer(client, clientID));
                            tAccueil.start();
                            clientID++;
                        } catch (Exception e) {
                        }
                    }
                }
            });
            t.start();

        } catch (Exception i) {
            System.out.println("Impossible d'�couter sur le port 4444: serait-il occup�?");
            i.printStackTrace();
        }
    }
}
