package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VentaEntrada extends Thread {
	private Socket socket;

	public VentaEntrada(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		super.run();
		System.out.println("Cliente nuevo conectado...");
		try {
			DataOutputStream salida = new DataOutputStream ( socket.getOutputStream() );
			DataInputStream entrada = new DataInputStream( socket.getInputStream() );
			salida.close();
			entrada.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
