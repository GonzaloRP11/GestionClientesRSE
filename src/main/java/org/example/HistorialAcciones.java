package org.example;

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

    

        public void mostrarHistorial() {

            if (acciones.estaVacia()) {

                System.out.println("No hay acciones registradas en el historial.");

            } else {

                System.out.println("\n--- HISTORIAL COMPLETO DE ACCIONES ---");

                acciones.mostrarElementos();

                System.out.println("--------------------------------------");

            }

        }

    }

    
