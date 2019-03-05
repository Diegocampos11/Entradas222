package clases;

import java.util.ArrayList;
import java.util.Date;

public class Espectaculo {
	private String nombre;
	private Date fechaEvento;
	private ArrayList<Entrada> listaEntradas;
	private Integer numFilas;
	
	public Espectaculo(String nombre, Date fechaEvento, Integer numFilas,
			Integer numButacasPorFila, Double precioEntrada) {
		super();
		this.nombre = nombre;
		this.fechaEvento = fechaEvento;
		this.listaEntradas = new ArrayList<Entrada>() {
			@Override
			public String toString() {
				String salida = "";
				for (Entrada entrada : this) {
					salida += entrada.toString() + "\n";
				}
				return salida;
			}
		};
		this.numFilas = numFilas;
		this.numButacasPorFila = numButacasPorFila;
		this.precioEntrada = precioEntrada;
		inicializarButacas();
	}
	
	private void inicializarButacas() {
		int idSecuancialXEvento = 0;
		for ( int i = 0; i < numFilas; i++ ) {
			for ( int ii = 0; ii < numButacasPorFila; ii++ ) 
				listaEntradas.add( new Entrada( ++idSecuancialXEvento, i + 1, ii + 1, new Date(), null, this ) );
		}
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechaEvento() {
		return fechaEvento;
	}
	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}
	public ArrayList<Entrada> getListaEntradas() {
		return listaEntradas;
	}
	public void setListaEntradas(ArrayList<Entrada> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}
	public Integer getNumFilas() {
		return numFilas;
	}
	public void setNumFilas(Integer numFilas) {
		this.numFilas = numFilas;
	}
	public Integer getNumButacasPorFila() {
		return numButacasPorFila;
	}
	public void setNumButacasPorFila(Integer numButacasPorFila) {
		this.numButacasPorFila = numButacasPorFila;
	}
	public Double getPrecioEntrada() {
		return precioEntrada;
	}
	public void setPrecioEntrada(Double precioEntrada) {
		this.precioEntrada = precioEntrada;
	}
	@Override
	public String toString() {
		return "Espectaculo [nombre=" + nombre + ", fechaEvento=" + fechaEvento + ", listaEntradas=" + listaEntradas
				+ ", numFilas=" + numFilas + ", numButacasPorFila=" + numButacasPorFila + ", precioEntrada="
				+ precioEntrada + "]";
	}
	private Integer numButacasPorFila;
	private Double precioEntrada;
	
	
}
