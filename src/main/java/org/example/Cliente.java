package org.example;

public class Cliente implements Comparable<Cliente> {
    private String nombre;
    private int scoring;
    private ListaDinamica<String> siguiendo;
    private ListaDinamica<String> conexiones;
    private Cola<String> solicitudesASeguir;

    public Cliente(String nombre, int scoring) {
        this.nombre = nombre;
        this.scoring = scoring;
        this.siguiendo = new ListaDinamica<>();
        this.conexiones = new ListaDinamica<>();
        this.solicitudesASeguir = new Cola<>();
    }

    public String getNombre() { return nombre; }
    public int getScoring() { return scoring; }
    public ListaDinamica<String> getSiguiendo() { return siguiendo; }
    public ListaDinamica<String> getConexiones() { return conexiones; }
    public Cola<String> getSolicitudes() { return solicitudesASeguir; }

    public void seguir(String cliente) { siguiendo.agregar(cliente); }
    public void aSeguir(String cliente) { solicitudesASeguir.encolar(cliente); }
    public void dejarSeguir() { solicitudesASeguir.desencolar(); }

    public void dejarDeSeguir(String cliente) {
        for (int i = 0; i < siguiendo.getContador(); i++) {
            if (siguiendo.obtener(i).equalsIgnoreCase(cliente)) {
                siguiendo.eliminar(i);
                return;
            }
        }
    }

    public boolean yaSigueA(String nombre) {
        for (int i = 0; i < siguiendo.getContador(); i++)
            if (siguiendo.obtener(i).equalsIgnoreCase(nombre)) return true;
        return false;
    }

    public boolean tieneConexionCon(String nombre) {
        for (int i = 0; i < conexiones.getContador(); i++)
            if (conexiones.obtener(i).equalsIgnoreCase(nombre)) return true;
        return false;
    }

    public void agregarConexion(String cliente) { 
        if (!tieneConexionCon(cliente)) {
            conexiones.agregar(cliente); 
        }
    }
    public void eliminarConexion(String cliente) {
        for (int i = 0; i < conexiones.getContador(); i++)
            if (conexiones.obtener(i).equalsIgnoreCase(cliente)) {
                conexiones.eliminar(i);
                return;
            }
    }

    @Override
    public int compareTo(Cliente otro) {
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    @Override
    public String toString() {
        return String.format(
                "-----------------------------\n" +
                        "NOMBRE:     %s\n" +
                        "SCORING:    %d puntos\n" +
                        "SIGUIENDO:  %s\n" +
                        "CONEXIONES: %s\n" +
                        "-----------------------------",
                nombre, scoring, siguiendo.toString(), conexiones.toString()
        );
    }
}
