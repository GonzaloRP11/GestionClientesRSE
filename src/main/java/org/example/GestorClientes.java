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

    private void cargarDesdeJson() {
        Gson gson = new Gson();
        try {
            java.io.FileReader lector = new java.io.FileReader("clientes.json");
            com.google.gson.JsonObject contenidoPrincipal = gson.fromJson(lector, com.google.gson.JsonObject.class);
            
            if (contenidoPrincipal != null && contenidoPrincipal.has("clientes")) {
                com.google.gson.JsonArray listaDeClientes = contenidoPrincipal.getAsJsonArray("clientes");
                for (int i = 0; i < listaDeClientes.size(); i++) {
                    com.google.gson.JsonObject datosCliente = listaDeClientes.get(i).getAsJsonObject();
                    
                    String nombre = datosCliente.get("nombre").getAsString();
                    int puntaje = datosCliente.get("scoring").getAsInt();
                    
                    Cliente nuevoCliente = new Cliente(nombre, puntaje);
                    
                    if (datosCliente.has("siguiendo")) {
                        com.google.gson.JsonArray siguiendo = datosCliente.getAsJsonArray("siguiendo");
                        for (int j = 0; j < siguiendo.size(); j++) {
                            nuevoCliente.seguir(siguiendo.get(j).getAsString());
                        }
                    }
                    
                    if (datosCliente.has("conexiones")) {
                        com.google.gson.JsonArray conexiones = datosCliente.getAsJsonArray("conexiones");
                        for (int k = 0; k < conexiones.size(); k++) {
                            nuevoCliente.agregarConexion(conexiones.get(k).getAsString());
                        }
                    }
                    agregarClienteAlSistema(nuevoCliente);
                }
                System.out.println("Carga de clientes finalizada con éxito desde " + "clientes.json");
            }
            lector.close();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de clientes: " + e.getMessage());
        }
    }

    private void agregarClienteAlSistema(Cliente cliente) {
        diccionarioPorNombre.insertar(cliente.getNombre().toLowerCase(), cliente);
        
        ListaDinamica<Cliente> listaScoring = diccionarioPorScoring.obtener(cliente.getScoring());
        if (listaScoring == null) {
            listaScoring = new ListaDinamica<>();
            diccionarioPorScoring.insertar(cliente.getScoring(), listaScoring);
        }
        listaScoring.agregar(cliente);
    }

    private void eliminarClienteDelSistema(Cliente cliente) {
        diccionarioPorNombre.eliminar(cliente.getNombre().toLowerCase());
        
        ListaDinamica<Cliente> listaScoring = diccionarioPorScoring.obtener(cliente.getScoring());
        if (listaScoring != null) {
            for (int i = 0; i < listaScoring.getContador(); i++) {
                if (listaScoring.obtener(i).getNombre().equalsIgnoreCase(cliente.getNombre())) {
                    listaScoring.eliminar(i);
                    break;
                }
            }
            if (listaScoring.getContador() == 0) {
                diccionarioPorScoring.eliminar(cliente.getScoring());
            }
        }
    }

    // ===== AGREGAR =====
    public void agregarCliente(String nombre, int scoring) {
        Cliente nuevo = new Cliente(nombre, scoring);
        agregarClienteAlSistema(nuevo);
        historial.registrarAccion("AGREGAR", nombre + ";" + scoring);
    }

    // ===== ELIMINAR =====
    public void eliminarCliente(String nombre) {
        Cliente c = diccionarioPorNombre.obtener(nombre.toLowerCase());
        if (c != null) {
            historial.registrarAccion("ELIMINAR", c.getNombre() + ";" + c.getScoring());
            eliminarClienteDelSistema(c);
            System.out.println("Cliente eliminado: " + nombre);
        } else {
            System.out.println("Cliente no encontrado: " + nombre);
        }
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
            Cliente c = diccionarioPorNombre.obtener(datos[0].toLowerCase());
            if (c != null) {
                eliminarClienteDelSistema(c);
            }
            System.out.println("Deshecho: Se eliminó el cliente agregado (" + datos[0] + ")");
        } else if (tipo.equals("ELIMINAR")) {
            Cliente restaurado = new Cliente(datos[0], Integer.parseInt(datos[1]));
            agregarClienteAlSistema(restaurado);
            System.out.println("Deshecho: Se restauró el cliente eliminado (" + datos[0] + ")");
        } else {
            System.out.println("La acción '" + tipo + "' no se puede deshacer o no tiene lógica de reversión.");
        }
    }

    // ===== BUSQUEDA =====
    public Cliente buscarPorNombre(String nombre) {
        return diccionarioPorNombre.obtener(nombre.toLowerCase());
    }

    public ListaDinamica<Cliente> buscarPorScoring(int scoring) {
        ListaDinamica<Cliente> resultado = diccionarioPorScoring.obtener(scoring);
        return (resultado != null) ? resultado : new ListaDinamica<>();
    }

    public void imprimirLista() {
        ListaDinamica<Cliente> lista = diccionarioPorNombre.valores();
        lista.imprimirLista();
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
