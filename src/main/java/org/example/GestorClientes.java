package org.example;

public class GestorClientes {
    private ListaDinamica<Cliente> clientes;
    private HistorialAcciones historial;
    //private Cola<SolicitudSeguimiento> solicitudes;

    public GestorClientes() {
        clientes = new ListaDinamica<>();
        historial = new HistorialAcciones();
        //solicitudes = new Cola<>();
    }

    public void agregarCliente(String nombre, int scoring) {
        clientes.agregar(new Cliente(nombre, scoring));
        historial.registrarAccion("Agregar cliente", nombre);
    }


    public Cliente buscarPorNombre(String nombre) {
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente c = clientes.obtener(i);
            if (c.getNombre().equals(nombre)) {
                return c;
            }
        }
        return null;
    }

    public Cliente buscarPorScoring(int scoring) {
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente c = clientes.obtener(i);
            if (c.getScoring() == scoring) {
                return c;
            }
        }
        return null;
    }
    public void imprimirLista()
    {
        clientes.imprimirLista();
    }

    public HistorialAcciones getHistorial() {
        return historial;
    }

    // ===== SOLICITUDES DE SEGUIMIENTO =====
    /*public void enviarSolicitudSeguimiento(String origen, String destino) {
        solicitudes.encolar(new SolicitudSeguimiento(origen, destino));
        historial.registrarAccion(
                "Solicitud enviada",
                origen + " -> " + destino
        );
    }

    public void procesarSolicitudSeguimiento() {
        if (solicitudes.estaVacia()) {
            return;
        }

        SolicitudSeguimiento s = solicitudes.desencolar();
        Cliente origen = buscarPorNombre(s.getOrigen());
        Cliente destino = buscarPorNombre(s.getDestino());

        if (origen != null && destino != null) {
            origen.seguir(destino.getNombre());
            historial.registrarAccion(
                    "Seguimiento procesado",
                    s.getOrigen() + " -> " + s.getDestino()
            );
        }
    }*/
}
