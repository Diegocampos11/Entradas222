package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.LineNumberInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import clases.Espectaculo;

public class GeneradorEspectaculos extends Thread {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		System.setProperty( "javax.net.ssl.keyStore", "./certificadosPractica/AlmacenServidorPractica" );
		System.setProperty( "javax.net.ssl.keyStorePassword" , "practicadiego");
		SSLServerSocketFactory fabricaServSocket = ( SSLServerSocketFactory ) SSLServerSocketFactory.getDefault(); 
		try {
			SSLServerSocket servSock = (SSLServerSocket) fabricaServSocket.createServerSocket( Servidor.PUERTO );
			while ( true ) {
				System.out.println("Servidor esperando conexión...");
				SSLSocket sock = (SSLSocket) servSock.accept();
				//cliente conectado
				System.out.println("Nuevo administrador conectado...");
				/*DataOutputStream salida = new DataOutputStream( sock.getOutputStream() );
				salida.writeUTF("hola Cliente, conectado de manera segura :D");
				salida.close();*/
				boolean continuar = true;//continuar ingresando espectaculos
				DataInputStream entrada = new DataInputStream( sock.getInputStream() );
				while( continuar ) {					
					String nombre = entrada.readUTF();
					Date fecha = new Date( ( (java.util.Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( entrada.readUTF() ) ).getTime() );
					Double precio = Double.parseDouble( entrada.readUTF() );
					Integer numFilas = Integer.parseInt( entrada.readUTF() );
					Integer numButacas = Integer.parseInt( entrada.readUTF() );
					Servidor.listaEspectaculo.add( new Espectaculo( nombre, fecha, numFilas, numFilas/*numButacas*/, precio ) );
					System.out.println( Servidor.listaEspectaculo.toString() );
					continuar = ( entrada.readUTF().toLowerCase().equals("s") ) ? true : false;
				}
			}
			//sock.close();
			//servSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
