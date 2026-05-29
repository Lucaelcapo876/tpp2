package ar.edu.ungs.billetera;

import java.time.LocalDate;

public class FondoLiquidezEmpresarial extends VinculadaDivisa {

    public FondoLiquidezEmpresarial(LocalDate fechaConstitucion, Integer plazo, Integer id, Double montoInvertido) {
        
        // Llamamos al constructor de VinculadaDivisa y le pasamos los datos obligatorios por regla de negocio:
        // precancelable = false, divisaReferencia = "FLE", tasaInteresDivisa = 0.08
        super(fechaConstitucion, plazo, id, montoInvertido, false, "FLE", 0.08);

        if (montoInvertido < 20000000.0) {
            throw new IllegalArgumentException("Error: El monto para un Fondo de Liquidez Empresarial debe ser >= 20.000.000");
        }
    }
}