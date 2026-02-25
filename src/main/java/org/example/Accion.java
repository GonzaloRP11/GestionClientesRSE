package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Accion {
    private String tipo;
    private String detalle;
    private LocalDateTime fechaHora;

    public Accion(String tipo, String detalle) {
        this.tipo = tipo;
        this.detalle = detalle;
        this.fechaHora = LocalDateTime.now();
    }

    public String getTipo() {
        return tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + fechaHora.format(formatter) + "] " + tipo + ": " + detalle;
    }
}

