package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Cuenta {
    private String cvu;
    private String alias;
    private Double saldo;
    private String dniTitular;
    private ArrayList<Inversion> inversiones;
    private ArrayList<Actividad> historial;

    public Cuenta(String cvu, String alias, Double saldoInicial, String dniTitular) {
        if (cvu == null || cvu.isEmpty() || alias == null || alias.isEmpty()) {
            throw new IllegalArgumentException("Error: El CVU y el alias no pueden ser nulos ni vacíos.");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Error: El saldo inicial no puede ser negativo.");
        }
        
        this.cvu = cvu;
        this.alias = alias;
        this.saldo = saldoInicial;
        this.dniTitular = dniTitular;
        this.inversiones = new ArrayList<>();
        this.historial = new ArrayList<>();
    }

    public Double obtenerSaldo() {
        return this.saldo;
    }
    
    //Modelamos ambos metodos en las clases heredadas
    public abstract Boolean transferir(Cuenta destino, double monto);
    public abstract Boolean depositar(double monto);
    
    public void registrarActividad(Actividad a) {
        if (a != null) {
            this.historial.add(a);
        }
    }

    @Override
    public String toString() {
        return "Alias: " + this.alias + " (CVU: " + this.cvu + ")";
    }


    @Override
	public int hashCode() {
		return Objects.hash(cvu);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		return Objects.equals(cvu, other.cvu);
	}

    public String getCvu() {
        return this.cvu;
    }

    public String getAlias() {
        return this.alias;
    }
    
    public String getDniTitular() {
    	return this.dniTitular;
    }

    public ArrayList<Inversion> getInversiones() {
        return this.inversiones;
    }

    public ArrayList<Actividad> getHistorial() {
        return this.historial;
    }

    protected void setSaldo(Double nuevoSaldo) {
        this.saldo = nuevoSaldo;
    }
}