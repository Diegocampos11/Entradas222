package clases;

import java.io.Serializable;
import java.util.Date;

public class Entrada implements Serializable{
	private Integer idSecuencial;
	private Integer fila;
	private Integer butaca;
	private Date fechaGeneracion;
	private Date fechaVenta;
	private Espectaculo evento;
	
	public Entrada(Integer idSecuencial, Integer fila, Integer butaca, Date fechaGeneracion, Date fechaVenta,
			Espectaculo evento) {
		super();
		this.idSecuencial = idSecuencial;
		this.fila = fila;
		this.butaca = butaca;
		this.fechaGeneracion = fechaGeneracion;
		this.fechaVenta = fechaVenta;
		this.evento = evento;
	}
	
	@Override
	public String toString() {//stackOverflow cause i try to print evento which it would be endless due to the fact that it refers to the same ofject on and on
		return "Entrada [idSecuencial=" + idSecuencial + ", fila=" + fila + ", butaca=" + butaca + ", fechaGeneracion="
				+ fechaGeneracion + ", fechaVenta=" + fechaVenta + ", evento=" + evento.getNombre() + "]";
	}
	public Integer getIdSecuencial() {
		return idSecuencial;
	}
	public void setIdSecuencial(Integer idSecuencial) {
		this.idSecuencial = idSecuencial;
	}
	public Integer getFila() {
		return fila;
	}
	public void setFila(Integer fila) {
		this.fila = fila;
	}
	public Integer getButaca() {
		return butaca;
	}
	public void setButaca(Integer butaca) {
		this.butaca = butaca;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public Espectaculo getEvento() {
		return evento;
	}
	public void setEvento(Espectaculo evento) {
		this.evento = evento;
	}
}
