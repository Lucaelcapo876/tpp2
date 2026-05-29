package ar.edu.ungs.billetera;

import java.time.LocalDate;

public class VinculadaDivisa extends Inversion {
    private String divisaReferencia;
    private Double tasaInteresDivisa;

    // El constructor pide todos los datos
    public VinculadaDivisa(LocalDate fechaConstitucion, Integer plazo, Integer id, Double montoInvertido, Boolean precancelable, String divisaReferencia, Double tasaInteresDivisa) {
        super(fechaConstitucion, plazo, id, montoInvertido, precancelable);
        this.divisaReferencia = divisaReferencia;
        this.tasaInteresDivisa = tasaInteresDivisa;
    }

    @Override
    public Double calcularResultado() {
        // Lógica matemática centralizada
        double cotizacion = Utilitarios.consultarCotizacion(this.divisaReferencia);
        return this.getMontoInvertido() * this.tasaInteresDivisa * cotizacion;
    }
}