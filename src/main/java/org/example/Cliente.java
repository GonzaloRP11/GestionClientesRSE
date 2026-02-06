package org.example;

public class Cliente {
    private String nombre;
    private int scoring;
    private ListaDinamica<String> siguiendo;
    private ListaDinamica<String> conexiones;

    public Cliente(String nombre, int scoring) {
        this.nombre = nombre;
        this.scoring = scoring;
        this.siguiendo = new ListaDinamica<>();
        this.conexiones = new ListaDinamica<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getScoring() {
        return scoring;
    }

    public ListaDinamica<String> getSiguiendo() {
        return siguiendo;
    }

    public ListaDinamica<String> getConexiones() {
        return conexiones;
    }

    public void seguir(String cliente) {
        siguiendo.agregar(cliente);
    }

    public void agregarConexion(String cliente) {
        conexiones.agregar(cliente);
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

                nombre, 

                scoring,

                siguiendo.toString(),

                conexiones.toString()

            );

        }

    }

    