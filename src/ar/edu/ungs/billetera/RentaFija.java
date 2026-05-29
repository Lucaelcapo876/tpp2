package ar.edu.ungs.billetera;

import java.time.LocalDate;

public class RentaFija extends Inversion {
    private Double tasaInteres;

    // Utiliza los datos del padre y el suyo propio
    public RentaFija(LocalDate fechaConstitucion, Integer plazo, Integer id, Double montoInvertido, Boolean precancelable, Double tasaInteres) {
        
        // Inyectamos los atributos generales a la clase abstracta padre
        super(fechaConstitucion, plazo, id, montoInvertido, precancelable);

        if (tasaInteres < 0) {
            throw new IllegalArgumentException("Error: La tasa de interés para Renta Fija debe ser >= 0.");
        }
        
        this.tasaInteres = tasaInteres;
    }

    // Implementacion obligatoria del metodo abstracto de la superclase
    @Override
    public Double calcularResultado() {
        // Obtenemos el monto usando el getter de la clase padre y lo multiplicamos por la tasa
        double resultado = this.getMontoInvertido() * this.tasaInteres;
        
        return resultado;
    }
    
    
}