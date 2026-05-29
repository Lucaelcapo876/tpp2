package ar.edu.ungs.billetera;

public class CuentaPremium extends Cuenta {
	private static final Double minimoApertura = 500000.0;
    public CuentaPremium(String cvu, String alias, Double saldoInicial, String dniTitular) {
        super(cvu, alias, saldoInicial, dniTitular);
		if (saldoInicial < minimoApertura) {
			 throw new IllegalArgumentException("Error: El saldo inicial de una Cuenta Premium debe ser >= " + minimoApertura);
		}
	}

	@Override
    public Boolean transferir(Cuenta destino, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0.");
        }
        
        if (this.obtenerSaldo() < monto) {
            throw new IllegalStateException("Error: Saldo insuficiente para realizar la transferencia.");
        }

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
    
    @Override
    public String toString() {
    	return "Premium: " + this.getAlias() + " (" + this.getCvu() + ")";
     }
}
