package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Inversion {
	private LocalDate fechaConstitucion;
	private Integer plazo;
	private Integer id;
	private Double montoInvertido;
	private Boolean precancelable;
	
	public Inversion(LocalDate fechaConstitucion, Integer plazo, Integer id, 
			Double montoInvertido, Boolean precancelable) {
        if (fechaConstitucion == null || plazo <= 0 || montoInvertido <= 0) {
            throw new IllegalArgumentException("Datos de inversión inválidos.");
        }
		this.fechaConstitucion = fechaConstitucion;
		this.plazo = plazo;
		this.id = id;
		this.montoInvertido = montoInvertido;
		this.precancelable = precancelable;
	}
	
	public abstract Double calcularResultado();
	
	public Double precancelar() {
        // Usamos el atributo booleano de tu diagrama para validar si se puede
        if (!this.precancelable) {
            throw new IllegalStateException("Error: Esta inversión no permite ser precancelada.");
        }
        
        // Si es precancelable, devolvemos el dinero al usuario. 
        // (Aquí devuelvo el monto original, si tuvieran alguna penalidad en sus reglas de negocio, la restan aquí).
        return this.montoInvertido;
    }
	
	public LocalDate getFechaConstitucion() {
		return fechaConstitucion;
	}

	public Integer getPlazo() {
		return plazo;
	}

	public Integer getId() {
		return id;
	}

	public Double getMontoInvertido() {
		return montoInvertido;
	}

	public Boolean getPrecancelable() {
		return precancelable;
	}
}
