package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import servidor.Servidor;

public class Administrador {

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm:ss" );
		System.setProperty( "javax.net.ssl.trustStore", "./certificadosPractica/AlmacenClientePractica" );
		System.setProperty( "javax.net.ssl.trustStorePassword" , "clientediego");
		
		SSLSocketFactory fabricaSSLSocket = ( SSLSocketFactory ) SSLSocketFactory.getDefault(); 
		try {
			SSLSocket sock = (SSLSocket) fabricaSSLSocket.createSocket( "127.0.0.1", Servidor.PUERTO );
			Scanner s = new Scanner ( System.in );
			System.out.println("Bienvenido administrador/a!!");//A continuación ingrese los datos de un espectaculo...");
			DataOutputStream salida = new DataOutputStream( sock.getOutputStream() );
			boolean continuar = true;//continuar ingresando espectaculos
			while ( continuar ) {
				boolean valido;
				System.out.println("Nuevo espectaculo...\nIngrese el nombre:");
				salida.writeUTF( s.nextLine() );
				String fecha = null;
				do {
					System.out.println("Ingrese la fecha y hora ('aaaa-mm-dd hh:mm:ss', formato 24 horas sin comillas):");
					fecha = s.nextLine();
					valido = fecha.matches( "^([0-9]){4}\\-([0-9]){2}\\-([0-9]){2}( )([0-9]){2}\\:([0-9]){2}\\:([0-9]){2}$" );
					if ( valido ) {
						String horaIngresada = fecha.substring( fecha.indexOf(" ") + 1 );
						int horas = Integer.parseInt( horaIngresada.charAt(0) + "" + horaIngresada.charAt(1) );
						int minutos = Integer.parseInt( horaIngresada.charAt(3) + "" + horaIngresada.charAt(4) );
						int segundos = Integer.parseInt( horaIngresada.charAt(6) + "" + horaIngresada.charAt(7) );
						if ( horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59 && segundos >= 0 && segundos <= 59 )
							valido = true;
						else {
							valido = false;
							System.out.println("Error. Debe de ingresar una hora válida");
						}
					}
					else System.out.println("Error. Debe de ingresar el siguiente formato: 'aaaa-mm-dd hh:mm:ss' (24 horas sin comillas)");
				}
				while( ! valido );
				salida.writeUTF( fecha );
				//
				String precioEntrada = null;
				do{
					System.out.println("Ingrese el precio de entrada:");
					precioEntrada = s.nextLine();
					valido = precioEntrada.matches("^([0-9])+((\\.|\\,)([0-9])+)?$");
					if ( ! valido ) System.out.println("Error. Debe de ingresar un valor numérico");
					else precioEntrada = precioEntrada.replace(",", ".");
				}
				while( ! valido );
				salida.writeUTF( precioEntrada );
				//
				String numFilas = null;
				do{
					System.out.println("Ingrese el número de filas:");
					numFilas = s.nextLine();
					valido = numFilas.matches("^([0-9])+$");
					if ( ! valido ) System.out.println("Error. Debe de ingresar un valor numérico entero");
				}
				while( ! valido );
				salida.writeUTF( numFilas );
				//
				String numBut = null;
				do{
					System.out.println("Ingrese el número de butacas por fila:");
					numBut = s.nextLine();
					valido = numBut.matches("^([0-9])+$");
					if ( ! valido ) System.out.println("Error. Debe de ingresar un valor numérico entero");
				}
				while( ! valido );
				salida.writeUTF( numBut );
				System.out.println("¿Desea continuar?\nSí es así escriba s, de lo contrario escriba n");
				String res = s.nextLine();
				continuar = ( res.toLowerCase().equals("s") ) ? true : false;
				salida.writeUTF( res );
			}
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
