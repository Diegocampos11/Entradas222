
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CifrarAESClaveString {

	public static void main(String[] args) {
		try {
			String password = "12345678";
			MessageDigest md = null;
			md = MessageDigest.getInstance("SHA-256");
			byte[] bytesHashPassword = md.digest( password.getBytes() );
			SecretKeySpec claveSecreta = new SecretKeySpec( bytesHashPassword, "AES");
			//estos bytes lo pasas a string y lo envias
			
			/*KeyGenerator generadorClaves = KeyGenerator.getInstance( "AES" );//paso el algoritmo como STRING
			generadorClaves.init( 256 );//muy seguro
			SecretKey claveSecreta = generadorClaves.generateKey();//de 256, lo guay seria guardarlo en un fichero y pasarlo entre los usuarios y asi todo blue*/
			
			Cipher cifrador = Cipher.getInstance( "AES/ECB/PKCS5Padding" );//método de cifrado, diferentes versiones de AES
			cifrador.init( Cipher.ENCRYPT_MODE, claveSecreta );//la misma clave para encriptar o descencripar, en la vida real se debe de intercambiar entre los usuarios
			
			//HASTA AQUI TIENE QUE SER IGUAL EN AMBOS PROGRAMAS
			//cifrar
			String mensaje = "Hola esto va a ser encriptado";
			System.out.println( "A encriptar: " + mensaje );
			byte mensajeCifrado[] = cifrador.doFinal( mensaje.getBytes() );
			
			String mensajeCifradotxt = new String( mensajeCifrado );
			System.out.println( mensajeCifradotxt );
			
			cifrador.init( Cipher.DECRYPT_MODE, claveSecreta);
			byte mensajeDescifrado[] = cifrador.doFinal( mensajeCifradotxt.getBytes() );
			System.out.println( "Resultado descencriptar " + new String( mensajeDescifrado ) );
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {//doFinal
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {//cipher get instance
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
