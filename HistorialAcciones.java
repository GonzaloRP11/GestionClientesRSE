public class HistorialAcciones {
    private Pila<Accion> acciones = new Pila<>();

    public void registrarAccion(String tipo, String detalle) {
        acciones.apilar(new Accion(tipo, detalle));
    }

    public Accion deshacerAccion() {
        return acciones.desapilar();
    }
}
