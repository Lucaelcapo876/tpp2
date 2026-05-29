package ar.edu.ungs.billetera;

import java.util.HashSet;

public class Empresa {
    private String cuit;
    private String nombreFantasia;
    private String telefono;
    private String email;
    private String nombreContacto;
    private HashSet<String> autorizados; //Guarda DNI del autorizado

    // Constructor
    public Empresa(String cuit, String nombreFantasia, String telefono, String email, String nombreContacto) {
    	if (cuit == null || cuit.isEmpty() || nombreFantasia == null || nombreFantasia.isEmpty()) {
            throw new IllegalArgumentException("Error: El CUIT y el nombre de fantasía no pueden ser nulos.");
        }
        this.cuit = cuit;
        this.nombreFantasia = nombreFantasia;
        this.telefono = telefono;
        this.email = email;
        this.nombreContacto = nombreContacto;
        this.autorizados = new HashSet<>(); 
    }
    
    public String getCuit() {
		return cuit;
	}


	public void setCuit(String cuit) {
		this.cuit = cuit;
	}


	public String getNombreFantasia() {
		return nombreFantasia;
	}


	public void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getNombreContacto() {
		return nombreContacto;
	}


	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}


	public HashSet<String> getAutorizados() {
		return autorizados;
	}


	public void setAutorizados(HashSet<String> autorizados) {
		this.autorizados = autorizados;
	}
    
    public void agregarAutorizado(String dni) {
        this.autorizados.add(dni);
    }

    public boolean estaAutorizado(String dni) {
        return this.autorizados.contains(dni);
    }

	@Override
    public String toString() {
        return "Empresa: " + this.nombreFantasia + " - CUIT: " + this.cuit + " - Contacto: " + this.nombreContacto +
        		" - Telefono: " + this.telefono + " - Email: " + this.email; 
    }
 
}