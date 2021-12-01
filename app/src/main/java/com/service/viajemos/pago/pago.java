package com.service.viajemos.pago;

public class pago {

    private String idPago;
    private String mediopago;
    private double monto;

    public String getIdPago() {
        return idPago;
    }

    public String getMediopago() {
        return mediopago;
    }

    public double getMonto() {
        return monto;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public void setMediopago(String mediopago) {
        this.mediopago = mediopago;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
