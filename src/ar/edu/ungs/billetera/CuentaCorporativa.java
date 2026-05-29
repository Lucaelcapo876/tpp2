package ar.edu.ungs.billetera;

public class CuentaCorporativa extends Cuenta{
	private String cuitEmpresa;
	private String usuarioAutorizado;
	
    public CuentaCorporativa(String cvu, String alias, Double saldoInicial, String cuitEmpresa, String usuarioAutorizado, String dniTitular) {
        super(cvu, alias, saldoInicial, dniTitular);
		if (cuitEmpresa == null || cuitEmpresa.isEmpty() || usuarioAutorizado == null || usuarioAutorizado.isEmpty()) {
            throw new IllegalArgumentException("Error: El CUIT de la empresa y el usuario autorizado no pueden ser nulos ni vacíos.");
        }
		this.cuitEmpresa = cuitEmpresa;
        this.usuarioAutorizado = usuarioAutorizado;
	}

    public Boolean verificarAutorizacion(Usuario u) {
        if (u == null) {
            return false;
        }
        // Registramos el DNI de la persona autorizada
        return this.usuarioAutorizado.equals(u.getDni());
    }
    
    @Override
    public Boolean transferir(Cuenta destino, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0.");
        }
        
        if (this.obtenerSaldo() < monto) {
            throw new IllegalStateException("Error: Saldo insuficiente para realizar la transferencia.");
        }

        // Restamos de esta cuenta y usamos polimorfismo para depositar en el destino
        this.setSaldo(this.obtenerSaldo() - monto);
        destino.depositar(monto);
        
        return true;
    }

	@Override
    public Boolean depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0.");
        }

        this.setSaldo(this.obtenerSaldo() + monto);
        return true;
    }

    public String getCuitEmpresa() {
        return this.cuitEmpresa;
    }

    public String getUsuarioAutorizado() {
        return this.usuarioAutorizado;
    }
    
    @Override
    public String toString() {
    	return "Premium: " + this.getAlias() + " (" + this.getCvu() + ")";
     }
}
