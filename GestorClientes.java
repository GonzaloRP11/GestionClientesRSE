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
        historial.registrarAccion("AGREGAR", nombre + ";" + scoring);
    }

    // ===== ELIMINAR =====
    public void eliminarCliente(String nombre) {
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente c = clientes.obtener(i);
            System.out.println("Comparando: " + c.getNombre() + " con " + nombre);
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                historial.registrarAccion(
                        "ELIMINAR",
                        c.getNombre() + ";" + c.getScoring()
                );
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

        if (a.getTipo().equals("AGREGAR")) {
            eliminarClienteSinHistorial(datos[0]);
        }

        if (a.getTipo().equals("ELIMINAR")) {
            clientes.agregar(
                    new Cliente(datos[0], Integer.parseInt(datos[1]))
            );
        }

        System.out.println("Acción deshecha: " + a);
    }

    private void eliminarClienteSinHistorial(String nombre) {
        for (int i = 0; i < clientes.getContador(); i++) {
            if (clientes.obtener(i).getNombre().equalsIgnoreCase(nombre)) {
                clientes.eliminar(i);
                return;
            }
        }
    }

    // ===== BUSCAR =====
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

    // ===== SOLICITUDES =====
    public void enviarSolicitudSeguimiento(String origen, String destino) {
        solicitudes.encolar(new SolicitudSeguimiento(origen, destino));
        historial.registrarAccion(
                "SOLICITUD",
                origen + "->" + destino
        );
    }

    public void procesarSolicitudSeguimiento() {
        if (solicitudes.estaVacia()) {
            System.out.println("No hay solicitudes pendientes.");
            return;
        }
        SolicitudSeguimiento s = solicitudes.desencolar();
        System.out.println("Procesando solicitud: " + s);
    }

    public void imprimirLista() {
        clientes.imprimirLista();
    }
}
