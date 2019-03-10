package cliente;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import servidor.Entrada;

public class Cliente {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket( "localhost", servidor.Servidor.PUERTOV );
			DataOutputStream salida = new DataOutputStream( socket.getOutputStream() );
			DataInputStream entrada = new DataInputStream( socket.getInputStream() );
			Scanner n = new Scanner( System.in );
			String response = null;//respuesta del servidor
			//bucle hasta que el servidor finalice la comunicación
			boolean continuar = true;
			while( continuar ) {
				while( continuar ) {
					response = entrada.readUTF();
					if ( response.equals("Finish!!!") ) continuar = false;//marca de final "Finish!!!"
					else {//comportamiento normal del programa :D
						System.out.println( response );
						salida.writeUTF( n.nextLine() );
					}
				}
				//Fuera del ciclo
				String entradaCifradaString = entrada.readUTF();
				byte entradaCifrada[] = entradaCifradaString.getBytes();
				//DESCIFRAR
				String password = "DiegoCampos";
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] bytesHashPassword = md.digest( password.getBytes() );
				SecretKeySpec claveSecreta = new SecretKeySpec( bytesHashPassword, "AES");
				Cipher cifrador = Cipher.getInstance( "AES/ECB/PKCS5Padding" );//método de cifrado, diferentes versiones de AES
				cifrador.init( Cipher.DECRYPT_MODE, claveSecreta);
				byte entradaDescifrada[] = cifrador.doFinal( entradaCifrada );
				//Recomponer entrada
				ByteArrayInputStream in = new ByteArrayInputStream( entradaDescifrada );
				ObjectInputStream is = new ObjectInputStream( in );
				Entrada entradaComprada = (Entrada) is.readObject();//obtengo la entrada que he comprado
				//cierro estos Xinputstream (ya no los necesito)
				is.close();
				in.close();
				//Genero hash de la entrada recompuesta :D
				md = MessageDigest.getInstance("SHA-256");
				byte[] hashSHAEntrada = md.digest( entradaComprada.toString().getBytes() );//obtengo el hash del objeto entrada recibido
				if ( new String( entradaComprada.getHash() ).equals( new String( hashSHAEntrada ) ) ) {//los hash coinciden 
					System.out.println("Venta realizada con éxito!!");
					response = entrada.readUTF();//recibo mensaje si deseo seguir comprando más entradas
					System.out.println( response );
					String answer = n.nextLine();
					salida.writeUTF( answer );//envio respuesta en dado caso digera que si para solicitar datos de una nueva entrada :D
					//y ya que no necesito respuesta del servidor de una vez decido si continuo o no el programa 
					if ( answer.equals("si") ) continuar = true;//se volvera ejecutar todo el bucle principal
					else if ( answer.equals("no") ) continuar = false;
				}
				else System.out.println("Los hash no coinciden... xd");
			}
			System.out.println("\nGoodbye!!!");
			salida.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
