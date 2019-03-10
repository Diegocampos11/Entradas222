package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Servidor {
	/*
	 * almacenservidor=practicadiego
		almacencliente=clientediego
		
		la clase servidor solo lanzar el hilo y luego hago un wait
		cada metodo tiene sus getters y sus setters
		GeneradorEspectaculos creara el socket
	 * */

	public static final int PUERTO = 4321;//puerto para administrador
	public static final int PUERTOV = 4322;//puerto para venta de entradas
	public static ArrayList<Espectaculo> listaEspectaculo;
	private static GeneradorEspectaculos hiloGenerador;

	public static void main(String[] args) throws ParseException {
		/*Date fechaactual = new Date( ( (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2020-05-09 21:59:59" ) ).getTime() );;
		Date fechaComparar = new Date( ( (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2020-05-09 21:00:00" ) ).getTime() );
		System.out.println( fechaactual );
		System.out.println( fechaComparar );
		System.out.println( fechaactual.compareTo( fechaComparar ) ); imprime 1 si es mayor la fecha actual, imprime -1 si es menor y 0 igual*/
		hiloGenerador = new GeneradorEspectaculos();
		listaEspectaculo = new ArrayList<Espectaculo>();
		////
		Servidor.listaEspectaculo.add( new Espectaculo( "xd", new Date( ( (java.util.Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2019-05-05 22:00:00" ) ).getTime() ), 5, 5/*numButacas*/, 3.9 ) );
		Servidor.listaEspectaculo.add( new Espectaculo( "xd2", new Date( ( (java.util.Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2019-04-04 22:00:00" ) ).getTime() ), 5, 5/*numButacas*/, 3.9 ) );
		////pruebas Cliente xd
		hiloGenerador.start();
		try {
			ServerSocket servSock = new ServerSocket( PUERTOV );
			while ( true ) {
				System.out.println("Esperando cliente...");
				Socket socket = servSock.accept();
				VentaEntrada myt = new VentaEntrada( socket );
				myt.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
