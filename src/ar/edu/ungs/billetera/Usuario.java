package ar.edu.ungs.billetera;

import java.util.Map;
import java.util.HashMap;

public class Usuario {
	private String nombre;
	private String dni;
	private String email;
	private String telefono;
	private HashMap<String, Cuenta> cuentas;
	
	public Usuario(String nombre, String dni, String email, String telefono) {
		this.nombre = nombre; 
		this.dni = dni;
		this.email = email;
		this.telefono = telefono;
		this.cuentas = new HashMap<>();
	}

	public String getNombre() {
		return nombre;
	}

	public String getDni() {
		return dni;
	}

	public String getEmail() {
		return email;
	}

	public String getTelefono() {
		return telefono;
	}

	public Map<String, Cuenta> getCuentas() {
		return cuentas;
	}
	
	public void agregarCuenta(Cuenta c) {
	    // Validar que la cuenta recibida no sea nula
	    if (c == null) {
	        throw new IllegalArgumentException("Error: La cuenta a agregar no puede ser nula.");
	    }

	    this.cuentas.put(c.getCvu(), c);
	}
	
	public Double calcularTotalInvertido() {
	    Double total = 0.0;

	    // Utilizamos foreach para iterar sobre los valores del mapa de cuentas
	    for (Cuenta cuenta : this.cuentas.values()) {
	        
	        // Obtenemos la lista de inversiones de cada cuenta y la recorremos
	        for (Inversion inversion : cuenta.getInversiones()) {
	            
	            // Sumamos el monto invertido de cada inversión activa
	            total += inversion.getMontoInvertido();
	        }
	    }

	    return total;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Nombre: ").append(this.nombre)
	      .append(" - DNI: ").append(this.dni)
	      .append(" - Telefono: ").append(this.telefono)
	      .append(" - Email: ").append(this.email).append("\n");
	    
	    // Mostramos las cuentas (los TADs relacionados)
	    if (this.cuentas == null || this.cuentas.isEmpty()) {
	        sb.append("  [Sin cuentas registradas]\n");
	    } else {
	        sb.append("  Cuentas:\n");
	        // Iteramos sobre las cuentas para llamar al toString() de cada una
	        for (Cuenta c : this.cuentas.values()) {
	            sb.append("    ").append(c.toString()).append("\n");
	        }
	    }
	    return sb.toString();
	}
	
	
}
