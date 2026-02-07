package org.example;

import com.google.gson.Gson;

public class GestorClientes {
    private Diccionario<String, Cliente> diccionarioPorNombre;
    private Diccionario<Integer, ListaDinamica<Cliente>> diccionarioPorScoring;
    private HistorialAcciones historial;
    private Cola<SolicitudSeguimiento> solicitudes;

    public GestorClientes() {
        diccionarioPorNombre = new Diccionario<>();
        diccionarioPorScoring = new Diccionario<>();
        historial = new HistorialAcciones();
        solicitudes = new Cola<>();
        cargarDesdeJson();
    }

    // ================== CLIENTES ==================

    private void agregarClienteAlSistema(Cliente cliente) {
        diccionarioPorNombre.insertar(cliente.getNombre().toLowerCase(), cliente);

        ListaDinamica<Cliente> lista = diccionarioPorScoring.obtener(cliente.getScoring());
        if (lista == null) {
            lista = new ListaDinamica<>();
            diccionarioPorScoring.insertar(cliente.getScoring(), lista);
        }
        lista.agregar(cliente);
    }

    private void eliminarClienteDelSistema(Cliente cliente) {
        diccionarioPorNombre.eliminar(cliente.getNombre().toLowerCase());

        ListaDinamica<Cliente> lista = diccionarioPorScoring.obtener(cliente.getScoring());
        if (lista != null) {
            for (int i = 0; i < lista.getContador(); i++) {
                if (lista.obtener(i).getNombre().equalsIgnoreCase(cliente.getNombre())) {
                    lista.eliminar(i);
                    break;
                }
            }
        }
    }

    public void agregarCliente(String nombre, int scoring) {
        Cliente c = new Cliente(nombre, scoring);
        agregarClienteAlSistema(c);
        historial.registrarAccion("AGREGAR", nombre + ";" + scoring);
    }

    public void eliminarCliente(String nombre) {
        Cliente c = diccionarioPorNombre.obtener(nombre.toLowerCase());
        if (c != null) {
            historial.registrarAccion("ELIMINAR", c.getNombre() + ";" + c.getScoring());
            eliminarClienteDelSistema(c);
        }
    }

    // ================== SOLICITUDES ==================

    public void enviarSolicitudSeguimiento(String origen, String destino) {
        solicitudes.encolar(new SolicitudSeguimiento(origen, destino));
        historial.registrarAccion("SOLICITUD_ENVIADA", origen + ";" + destino);
        System.out.println("Solicitud enviada: " + origen + " -> " + destino);
    }

    public void procesarSolicitudSeguimiento() {
        if (solicitudes.estaVacia()) {
            System.out.println("No hay solicitudes.");
            return;
        }

        SolicitudSeguimiento s = solicitudes.desencolar();
        Cliente origen = buscarPorNombre(s.getOrigen());
        Cliente destino = buscarPorNombre(s.getDestino());

        if (origen != null && destino != null) {
            origen.seguir(destino.getNombre());
            historial.registrarAccion("SOLICITUD_ACEPTADA", s.getOrigen() + ";" + s.getDestino());
            System.out.println("Solicitud aceptada.");
        }
    }

    // ================== DESHACER ==================

    public void deshacerUltimaAccion() {
        Accion a = historial.deshacerAccion();
        if (a == null) return;

        String[] datos = a.getDetalle().split(";");
        String tipo = a.getTipo();

        switch (tipo) {
            case "AGREGAR":
                Cliente c = buscarPorNombre(datos[0]);
                if (c != null) eliminarClienteDelSistema(c);
                break;

            case "ELIMINAR":
                agregarClienteAlSistema(new Cliente(datos[0], Integer.parseInt(datos[1])));
                break;

            case "SOLICITUD_ENVIADA":
                solicitudes.desencolar(); // elimina la última enviada
                System.out.println("Deshecho envío de solicitud.");
                break;

            case "SOLICITUD_ACEPTADA":
                Cliente origen = buscarPorNombre(datos[0]);
                if (origen != null) {
                    origen.dejarDeSeguir(datos[1]);
                    System.out.println("Deshecha aceptación de seguimiento.");
                }
                break;
        }
    }

    // ================== BUSQUEDAS ==================

    public Cliente buscarPorNombre(String nombre) {
        return diccionarioPorNombre.obtener(nombre.toLowerCase());
    }
    public ListaDinamica<Cliente> buscarPorScoring(int scoring) {
        ListaDinamica<Cliente> resultado = diccionarioPorScoring.obtener(scoring);
        return (resultado != null) ? resultado : new ListaDinamica<>();
    }


    public void imprimirLista() {
        diccionarioPorNombre.valores().imprimirLista();
    }
    public HistorialAcciones getHistorial() { return historial; }

    // ================== JSON ==================

    private void cargarDesdeJson() {
        try {
            Gson gson = new Gson();
            java.io.FileReader lector = new java.io.FileReader("clientes.json");
            var json = gson.fromJson(lector, com.google.gson.JsonObject.class);
            if (json.has("clientes")) {
                for (var c : json.getAsJsonArray("clientes")) {
                    var o = c.getAsJsonObject();
                    agregarClienteAlSistema(new Cliente(
                            o.get("nombre").getAsString(),
                            o.get("scoring").getAsInt()
                    ));
                }
            }
            lector.close();
        } catch (Exception e) {
            System.out.println("Error JSON");
        }
    }
}
