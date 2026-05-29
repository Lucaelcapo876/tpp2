package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Actividad {
	private LocalDate fecha;
	private Double montoInvolucrado;
	
	public Actividad(Double montoInvolucrado) {
        if (montoInvolucrado <= 0) {
            throw new IllegalArgumentException("Error: El monto involucrado en la actividad debe ser mayor a 0.");
        }
        
	    this.fecha = Utilitarios.hoy(); 
	    this.montoInvolucrado = montoInvolucrado;
	}
	
	public abstract String obtenerDetalle();

	public LocalDate getFecha() {
		return fecha;
	}

	public Double getMontoInvolucrado() {
		return montoInvolucrado;
	}
	
}
