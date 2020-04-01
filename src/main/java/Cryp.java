import javax.crypto.*;
import javax.crypto.spec.*;

import com.sun.org.apache.xml.internal.security.utils.Base64;



public class Cryp {

	/**
	 * @param args
	 */
	
	final String encryptedValue = "I saw the real you" ;
	String secKey = "ubutru";

	
	public static void main(String[] args) {
		
		String encryptedVal = null;
		String valueEnc = "jesuispassechezsoh"; //valeur à encoder

	    try {
	    		    	
	    	 KeyGenerator generator = KeyGenerator.getInstance("AES"); //générateur de clé pour AES
	            generator.init(128); //init clé AES
	            SecretKey key = generator.generateKey(); //créé la clé
	            Cipher cipher = Cipher.getInstance("AES"); //précise la méthode de chiffrement
	            cipher.init(Cipher.ENCRYPT_MODE, key); //mode de chiffrement avec clé
	            byte[] res = cipher.doFinal(valueEnc.getBytes()); //valeur de départ transformée en un tableau d'octets et sort le texte chiffré crypto=res
	            String res_str =  Base64.encode(res);//new String(res); //affiche le tableau d’octets en chaîne de caractères
	           
	            cipher.init(Cipher.DECRYPT_MODE, key); // mode déchiffrement avec clé
	               
	            byte[] res2 = cipher.doFinal(Base64.decode(res_str)); 
	                //byte[] res2 = cipher.doFinal(res_str.getBytes("utf-8"));
	            String res_str2 =  new String(res2);
	           
	            System.out.println("source:"+valueEnc); //valeur départ
	            System.out.println("enc:"+res_str); //valeur chiffrée
	            System.out.println("dec:"+res_str2); // on retrouve la valeur de départ issu du déchiffrement
	           
	            byte[] enck = key.getEncoded(); //tableau d’octets qu’on transforme en chaîne de caractères, travaille sur la représentation en octet du texte
	            System.out.println(Base64.encode(enck)); //representation textuelle de la clé

                // le code pourrait s'arrêter là, la suite peut se faire sur une autre machine
	           
	           
	            String encoded = "r1peJOWYRRod8IibmrYoPA==";// résultat de chiffrement fait antérieurement
	            String key_str = "+WHQtDsr9LJQ05/2MHZkQQ=="; //resultat de la représentation textuelle de la clé
	           
	            byte[] kb = Base64.decode(key_str.getBytes()); //transforme en tableau
	            SecretKeySpec ksp = new SecretKeySpec(kb, "AES"); //recréé une clé AES qu’on appelle ksp
	           
	            Cipher cipher2 = Cipher.getInstance("AES");
	            cipher2.init(Cipher.DECRYPT_MODE, ksp); //mode déchiffrement
	            byte[] res3 = cipher2.doFinal(Base64.decode(encoded));
	            String res_str3 =  new String(res3);
	            System.out.println("obtained: "+res_str3);
	        
	       
	    } catch(Exception ex) {
	        System.out.println("The Exception is=" + ex);
	    }

	    

	}

}
