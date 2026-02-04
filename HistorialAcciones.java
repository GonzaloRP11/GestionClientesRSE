public class HistorialAcciones {
    private Pila<Accion> acciones;

    public HistorialAcciones() {
        this.acciones = new Pila<>();
    }

    public void registrarAccion(String tipo, String detalle) {
        acciones.apilar(new Accion(tipo, detalle));
    }

    public Accion deshacerAccion() {
        if (!acciones.estaVacia()) {
            return acciones.desapilar();
        }
        return null;
    }
}