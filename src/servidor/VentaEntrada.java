package servidor;

import java.net.Socket;

public class VentaEntrada extends Thread {
	private Socket socket;

	public VentaEntrada(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
	}
}
