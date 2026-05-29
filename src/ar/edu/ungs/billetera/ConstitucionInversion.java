package ar.edu.ungs.billetera;

public class ConstitucionInversion extends Actividad {
    private Inversion inversionRealizada;
    private Cuenta origen;

    public ConstitucionInversion(Cuenta origen, Inversion inversionRealizada, Double montoInvolucrado) {
        super(montoInvolucrado);
        if (origen == null || inversionRealizada == null) {
            throw new IllegalArgumentException("Error: La cuenta origen y la inversión no pueden ser nulas.");
        }

        // el monto involucrado debe ser igual al monto invertido
        if (!montoInvolucrado.equals(inversionRealizada.getMontoInvertido())) {
            throw new IllegalArgumentException("Error: El monto de la actividad no coincide con el monto de la inversión.");
        }

        this.origen = origen;
        this.inversionRealizada = inversionRealizada;
    }

    // Implementación del metodo abstracto de Actividad
    @Override
    public String obtenerDetalle() {
        StringBuilder sb = new StringBuilder();
        
        // getClass().getSimpleName() extrae si es "RentaFija" o "VinculadaDivisa" automaticamente
        String tipoInversion = this.inversionRealizada.getClass().getSimpleName();
        
        sb.append("Inversion:\n");
        sb.append("fecha: ").append(this.getFecha()).append("\n");
        sb.append("origen: ").append(this.origen.getDniTitular()).append(" (").append(this.origen.getCvu()).append(")\n");
        sb.append("desc: ").append(tipoInversion).append("\n");
        sb.append("monto: ").append(this.getMontoInvolucrado()).append("\n");
        sb.append("plazo: ").append(this.inversionRealizada.getPlazo()).append("\n");
        sb.append("Aprobado");
        
        return sb.toString();
    }
}