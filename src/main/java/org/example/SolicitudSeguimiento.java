package org.example;

public class SolicitudSeguimiento {
    private String origen;
    private String destino;

    public SolicitudSeguimiento(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    @Override
    public String toString() {
        return origen + " -> " + destino;
    }
}

