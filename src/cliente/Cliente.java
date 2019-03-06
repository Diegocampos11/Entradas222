package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket( "localhost", servidor.Servidor.PUERTOV );
			DataOutputStream salida = new DataOutputStream( socket.getOutputStream() );
			DataInputStream entrada = new DataInputStream( socket.getInputStream() );
			//bucle hasta que el servidor finalice la comunicación
			String response = null;//respuesta del servidor
			while( ! ( response = entrada.readUTF() ).equals("/fin") ) {//mientras no le envie un /fin
				
			}
			salida.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
