package org.example;

public class GestorClientes {
    private ListaDinamica<Cliente> clientes;
    private HistorialAcciones historial;
    private Cola<SolicitudSeguimiento> solicitudes;

    public GestorClientes() {
        clientes = new ListaDinamica<>();
        historial = new HistorialAcciones();
        solicitudes = new Cola<>();
    }

    // ===== AGREGAR =====
    public void agregarCliente(String nombre, int scoring) {
        clientes.agregar(new Cliente(nombre, scoring));
        // Guardamos nombre y scoring por si hay que deshacer (borrarlo)
        historial.registrarAccion("AGREGAR", nombre + ";" + scoring);
    }

    // ===== ELIMINAR =====
    public void eliminarCliente(String nombre) {
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente c = clientes.obtener(i);
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                // Guardamos los datos antes de borrar para poder deshacer (re-agregar)
                historial.registrarAccion("ELIMINAR", c.getNombre() + ";" + c.getScoring());
                clientes.eliminar(i);
                System.out.println("Cliente eliminado: " + nombre);
                return;
            }
        }
        System.out.println("Cliente no encontrado: " + nombre);
    }

    // ===== DESHACER =====
    public void deshacerUltimaAccion() {
        Accion a = historial.deshacerAccion();
        if (a == null) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }

        String[] datos = a.getDetalle().split(";");
        String tipo = a.getTipo();

        if (tipo.equals("AGREGAR")) {
            // Deshacer un agregado es borrarlo
            eliminarClienteSinHistorial(datos[0]);
            System.out.println("Deshecho: Se eliminó el cliente agregado (" + datos[0] + ")");
        } else if (tipo.equals("ELIMINAR")) {
            // Deshacer una eliminación es volver a agregarlo
            clientes.agregar(new Cliente(datos[0], Integer.parseInt(datos[1])));
            System.out.println("Deshecho: Se restauró el cliente eliminado (" + datos[0] + ")");
        } else {
            System.out.println("La acción '" + tipo + "' no se puede deshacer o no tiene lógica de reversión.");
        }
    }

    private void eliminarClienteSinHistorial(String nombre) {
        for (int i = 0; i < clientes.getContador(); i++) {
            if (clientes.obtener(i).getNombre().equalsIgnoreCase(nombre)) {
                clientes.eliminar(i);
                return;
            }
        }
    }

    // ===== BUSQUEDA =====
    public Cliente buscarPorNombre(String nombre) {
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente c = clientes.obtener(i);
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    public ListaDinamica<Cliente> buscarPorScoring(int scoring) {
        ListaDinamica<Cliente> resultado = new ListaDinamica<>();
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente c = clientes.obtener(i);
            if (c.getScoring() == scoring) {
                resultado.agregar(c);
            }
        }
        return resultado;
    }

    public void imprimirLista() {
        clientes.imprimirLista();
    }

    public HistorialAcciones getHistorial() {
        return historial;
    }

    // ===== SOLICITUDES DE SEGUIMIENTO =====
    public void enviarSolicitudSeguimiento(String origen, String destino) {
        solicitudes.encolar(new SolicitudSeguimiento(origen, destino));
        historial.registrarAccion("SOLICITUD", origen + " -> " + destino);
        System.out.println("Solicitud de " + origen + " para seguir a " + destino + " encolada.");
    }

    public void procesarSolicitudSeguimiento() {
        if (solicitudes.estaVacia()) {
            System.out.println("No hay solicitudes pendientes.");
            return;
        }

        SolicitudSeguimiento s = solicitudes.desencolar();
        Cliente origen = buscarPorNombre(s.getOrigen());
        Cliente destino = buscarPorNombre(s.getDestino());

        if (origen != null && destino != null) {
            origen.seguir(destino.getNombre());
            System.out.println("Solicitud procesada: " + s.getOrigen() + " ahora sigue a " + s.getDestino());
        } else {
            System.out.println("Error: Uno de los clientes de la solicitud no existe.");
        }
    }
}
