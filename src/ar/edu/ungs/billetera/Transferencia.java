package ar.edu.ungs.billetera;

public class Transferencia extends Actividad {
    private Cuenta origen;
    private Cuenta destino;

    public Transferencia(Cuenta origen, Cuenta destino, Double montoInvolucrado) {
        super(montoInvolucrado);
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Error: La cuenta de origen y destino no pueden ser nulas.");
        }

        if (origen.getCvu().equals(destino.getCvu())) {
            throw new IllegalArgumentException("Error: No se puede transferir a la misma cuenta (los CVU deben ser distintos).");
        }

        this.origen = origen;
        this.destino = destino;
    }

    @Override
    public String obtenerDetalle() {
        // Usamos StringBuilder para cumplir con el requerimiento técnico del TP
        StringBuilder sb = new StringBuilder();
        
        // Formato estricto multilínea
        sb.append("Transferencia:\n");
        sb.append("fecha: ").append(this.getFecha()).append("\n"); // Asumiendo que getFecha() devuelve el LocalDate
        sb.append("origen: ").append(this.origen.getDniTitular()).append(" (").append(this.origen.getCvu()).append(")\n");
        sb.append("destino: ").append(this.destino.getDniTitular()).append(" (").append(this.destino.getCvu()).append(")\n");
        sb.append("monto: ").append(this.getMontoInvolucrado()).append("\n");
        sb.append("Aprobado"); // Asumimos "Aprobado" porque si se instanció, superó las validaciones de saldo
        
        return sb.toString();
    }
}