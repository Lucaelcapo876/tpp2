package ar.edu.ungs.billetera;

public class CuentaRegular extends Cuenta {
	
	private static final Double saldoLimite = 5000000.0;
	public CuentaRegular(String cvu, String alias, Double saldoInicial) {
		super(cvu, alias, saldoInicial);
		if (saldoInicial > saldoLimite) {
			throw new IllegalArgumentException("Error: El saldo inicial de una Cuenta Regular no puede superar los " + saldoLimite);
		}
	}

	@Override
    public Boolean transferir(Cuenta destino, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0.");
        }
        
        // Verificamos si hay fondos suficientes
        if (this.obtenerSaldo() < monto) {
            throw new IllegalStateException("Error: Saldo insuficiente para realizar la transferencia.");
        }

        // Restamos el dinero de esta cuenta origen
        this.setSaldo(this.obtenerSaldo() - monto);      
        destino.depositar(monto);
        return true;
    }

	 @Override
	 public Boolean depositar(double monto) {
		 if (monto <= 0) {
			 throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0.");
		 }

		 // el saldo final no puede superar los 5.000.000
		 if (this.obtenerSaldo() + monto > saldoLimite) {
			 throw new IllegalStateException("Error: El depósito supera el límite permitido de " + saldoLimite + " para una Cuenta Regular.");
		 }

		 // Utilizamos el metodo protegido que definimos en la superclase para actualizar el saldo
		 this.setSaldo(this.obtenerSaldo() + monto);
		 return true;
	 }
	 
	 @Override
	 public String toString() {
		 // Formato exigido: "Regular: alias (CVU)"
		 return "Regular: " + this.getAlias() + " (" + this.getCvu() + ")";
	 }

}
