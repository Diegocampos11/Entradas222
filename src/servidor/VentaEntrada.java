package servidor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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

public class VentaEntrada extends Thread {
	private Socket socket;
	private boolean continuar = true;

	public VentaEntrada(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		super.run();
		System.out.println("Cliente nuevo conectado...");//kendrajao
		Entrada entradaSeleccionada = null;
		try {
			DataOutputStream salida = new DataOutputStream ( socket.getOutputStream() );
			DataInputStream entrada = new DataInputStream( socket.getInputStream() );
			String answerClient = null;
			boolean seleccionarEspactaculo = true;//si hay que pedir espectaculo o no al usuario
			Espectaculo e = null;
			while( continuar ) {//
				if ( seleccionarEspactaculo ) {
					salida.writeUTF("Bienvenido!!\nSeleccione el espectaculo de su interés:\n" + espectaculosDisponibles() );
					answerClient = entrada.readUTF();//le resto uno y obtendré el espectaculo
					e = Servidor.listaEspectaculo.get( Integer.parseInt( answerClient ) - 1 );//espectaculo seleccionado
				}
				salida.writeUTF( ( ( ! seleccionarEspactaculo ) ? "Lo sentimos, la butaca seleccionada ya ha sido vendida\n" : "" ) + ( "Entradas disponibles del espectaculo: " + e.getNombre() + "\n" + entradasPorFilasDisponibles( e ) + "\nA continuación seleccione el número de fila:" ) );
				answerClient = entrada.readUTF();//número de fila seleccionada
				int filaSeleccionada = Integer.parseInt( answerClient );
				salida.writeUTF( "Butacas disponibles en la fila: " + answerClient + "\n" + entradasPorFilaDisponibles( e, filaSeleccionada ) + "\nA continuación seleccione el número de butaca:" );
				answerClient = entrada.readUTF();//número de butaca seleccionada
				int butacaSeleccionada = Integer.parseInt( answerClient );
				//reservarEntrada
				//the same as entradasPorFilaDisponibles: //el filaSeleccionada -1 es debido a: obtengo donde comienza esa fila :D, no donde termina, ejemplo, selecciono fila 5: 4*5 = 20 (inicio de la quinta fila, primera butaca), no 5*5 = 25//fin de la fila bueno e incluso se sale del indice xd
				entradaSeleccionada = ( (Entrada) e.getListaEntradas().get( ( filaSeleccionada > 1 ) ? ( ( ( filaSeleccionada - 1 ) * e.getNumButacasPorFila() ) + butacaSeleccionada ) - 1 : butacaSeleccionada - 1 ) );//al final -1 en ambos since the first element in an arraylist is 0 xd
				synchronized( entradaSeleccionada ) {
					if ( ! ( entradaSeleccionada.getFechaReserva() == null ) ) {//sé si sigue disponible por la fecha de venta... o reserva?
						seleccionarEspactaculo = false;
						continue;
					}
					else {
						boolean confirmacionIncorrecta = false;
						entradaSeleccionada.setFechaReserva( new Date(  ) );
						do {
							salida.writeUTF( ( ( confirmacionIncorrecta ) ? "Datos introducidos incorrectos. Imprimiendo datos de la entrada seleccionada:" : "Esta a punto de comprar la siguiente entrada:" )
											+ "\n\tSecuencial: " + entradaSeleccionada.getIdSecuencial()
											+ "\n\tEspectaculo: " + e.getNombre()
											+ "\n\tFila: " + entradaSeleccionada.getFila() 
											+ "\n\tButaca: " + entradaSeleccionada.getButaca()
											+ "\n\tPrecio: " + e.getPrecioEntrada()
											+ "\n¿Desea confirmar la compra de está entrada?\nEscriba \"si\" (sin comillas) para confirmar, de lo contrario escriba \"no\"" );
							confirmacionIncorrecta = false;//lo vuelvo a colocar en falso y despues de la impresion del mensaje ya que lo utilizo y necesito su estado
							answerClient = entrada.readUTF().toLowerCase();//confirmacion
							if ( answerClient.equals("si") ) {//confirmacion
								//se establece la fecha de venta
								entradaSeleccionada.setFechaVenta( new Date(  ) );
								//Generar hash
								MessageDigest md = MessageDigest.getInstance("SHA-256");
								byte[] hashSHA = md.digest( entradaSeleccionada.toString().getBytes() );
								entradaSeleccionada.setHash( hashSHA );//hash generado almacenado en el objeto entrada :D
								//CIFRAR
								String password = "DiegoCampos";
								md = MessageDigest.getInstance("SHA-256");
								byte[] bytesHashPassword = md.digest( password.getBytes() );
								SecretKeySpec claveSecreta = new SecretKeySpec( bytesHashPassword, "AES");
								Cipher cifrador = Cipher.getInstance( "AES/ECB/PKCS5Padding" );//método de cifrado, diferentes versiones de AES
								cifrador.init( Cipher.ENCRYPT_MODE, claveSecreta );//la misma clave para encriptar o descencripar, en la vida real se debe de intercambiar entre los usuarios
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								ObjectOutputStream ous = new ObjectOutputStream( bos );
								ous.writeObject( entradaSeleccionada );
								byte entradaCifrada[] = cifrador.doFinal( bos.toByteArray() );//encriptar los bytes resultantes de la entrada completa
								//salida de datos :D
								salida.writeUTF( "Finish!!!" );//primero envio la señal de que todo ha terminado :D
								salida.writeUTF( new String( entradaCifrada ) );//luego envio la entrada cifrada :D
								salida.writeUTF( "¿Desea comprar otra entrada?\nEscriba \"si\" (sin comillas) para confirmar, de lo contrario escriba \"no\"" );
								answerClient = entrada.readUTF().toLowerCase();//si desea seguir comprando entradas
								if ( answerClient.equals("si") ) continuar = true;//se volvera ejecutar todo el bucle principal
								else if ( answerClient.equals("no") ) continuar = false;
							}
							else if ( answerClient.equals("no") ) {
								entradaSeleccionada.setFechaReserva( null );
								seleccionarEspactaculo = true;//comenzara desde el principio xd
							}
							else confirmacionIncorrecta = true;
						}
						while( confirmacionIncorrecta );
					}
				}
			}
			System.out.println("Cliente desconectado...");
			salida.close();
			entrada.close();
		} catch ( java.net.SocketException e ) {
			entradaSeleccionada.setFechaReserva( null );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private String entradasPorFilaDisponibles(Espectaculo espectaculo, int numFilaSeleccionada) {
		String res = "";
		int numButacas = espectaculo.getNumButacasPorFila(), entradaIndex = ( numFilaSeleccionada - 1 ) * numButacas;//el -1 es debido a: obtengo donde comienza esa fila :D, no donde termina, ejemplo, selecciono fila 5: 4*5 = 20(inicio de la quinta fila, primera butaca), no 5*5 = 25//fin de la fila bueno e incluso se sale del indice xd
		for (int numButaca = 0; numButaca < numButacas; numButaca++, entradaIndex++) {
			Entrada entrada = ( ( Entrada ) espectaculo.getListaEntradas().get(entradaIndex) );
			if ( entrada.getFechaReserva() == null ) {
				res += "Butaca: " + ( numButaca + 1 ) + " disponible\n";
			}
		}
		return res;
	}

	private String entradasPorFilasDisponibles(Espectaculo espectaculo) {
		String res = "";
		Object[] entradas = espectaculo.getListaEntradas().toArray();
		int numFilas = espectaculo.getNumFilas(), numButacas = espectaculo.getNumButacasPorFila(), disponiblesFila = 0;
		//los for me sirven para controlar hasta donde llega cada fila y para mostrar cada butaca claro, utilizo solo un contador porque es un array de una sola dimension
		//y el ++ must be done in the for nested :D
		for (int numFila = 0, entradaIndex = 0; numFila < numFilas; numFila++) {
			for (int numButaca = 0; numButaca < numButacas; numButaca++, entradaIndex++) {
				Entrada entrada = ( ( Entrada ) entradas[entradaIndex] );
				if ( entrada.getFechaReserva() == null ) {
					disponiblesFila++;
				}
			}
			if ( disponiblesFila != 0 ) res += "Fila: " + ( numFila + 1 ) + " posee " + disponiblesFila + " butacas disponibles\n";
			disponiblesFila = 0;
		}
		return res;
	}

	private String espectaculosDisponibles() {
		String response = "";
		Date fechaServidor = new Date( );
		int numeroEspectaculo = 1;
		if ( Servidor.listaEspectaculo.size() == 0 ) return "No hay espectaculos disponibles";
		for ( Espectaculo espectaculo : Servidor.listaEspectaculo ) {
			if ( fechaServidor.compareTo( espectaculo.getFechaEvento() ) == -1 ) {
				response += numeroEspectaculo++ + ".\tNombre espactaculo: " + espectaculo.getNombre() + "\n  \tFecha y hora del espectáculo: " + espectaculo.getFechaEvento() + "\n  \tNúmero de entradas disponibles: " + entradasDisponibles( espectaculo ) + "\n  \tPrecio de las entradas: " + espectaculo.getPrecioEntrada() + "\n";
			}
		}
		return response + "\nA continuación seleccione un espectaculo:";
	}

	private String entradasDisponibles(Espectaculo espectaculo) {//0=it means without taking into account each row
		int res = 0;
		for ( Entrada entrada : espectaculo.getListaEntradas() ) res++;	
		return String.valueOf( res );
	}
}
