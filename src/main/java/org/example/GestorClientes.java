package org.example;

import org.example.AVL.*;

import com.google.gson.Gson;

public class GestorClientes {
    private Diccionario<String, Cliente> diccionarioPorNombre;
    private Diccionario<Integer, ListaDinamica<Cliente>> diccionarioPorScoring;
    private HistorialAcciones historial;

    public GestorClientes() {
        diccionarioPorNombre = new Diccionario<>();
        diccionarioPorScoring = new Diccionario<>();
        historial = new HistorialAcciones();
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
        if (scoring > 100) {
            System.out.println("Error: El scoring no puede ser mayor a 100.");
            return;
        }

        if (diccionarioPorNombre.contiene(nombre.toLowerCase())) {
            System.out.println("Aviso: Ya existe un cliente con el nombre '" + nombre + "'. Se sobrescribirán sus datos.");
        }

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

    public void enviarSolicitudSeguimiento(Cliente cliente,ListaEstatica<String> seguidores) {
        
        for (int i = 0; i < seguidores.getContador(); i++) {
            //solicitudes.encolar(new SolicitudSeguimiento(cliente, seguidores.obtener(i)));
            historial.registrarAccion("SOLICITUD_ENVIADA", cliente .getNombre()+ ";" + seguidores.obtener(i));
            System.out.println("Solicitud enviada: " + cliente.getNombre() + " -> " + seguidores.obtener(i));
        }
    }

    /* public void enviarSolicitudSeguimiento(String origen, String destino) {
        solicitudes.encolar(new SolicitudSeguimiento(origen, destino));
        historial.registrarAccion("SOLICITUD_ENVIADA", origen + ";" + destino);
        System.out.println("Solicitud enviada: " + origen + " -> " + destino);
    }
    */

    public void procesarSolicitudSeguimiento(Cliente cliente) {
        if (cliente.getSolicitudes().estaVacia()) {
            System.out.println("No hay solicitudes a procesar.");
            return;
        }

        Cola<String> procesarSolicitudes = cliente.getSolicitudes();

        while (!procesarSolicitudes.estaVacia())
        {
            String aSeguir = procesarSolicitudes.desencolar();
            Cliente origen = buscarPorNombre(cliente.getNombre());
            Cliente destino = buscarPorNombre(aSeguir);
    
            if (origen != null && destino != null) {
                origen.seguir(destino.getNombre());
    
                if (destino.yaSigueA(origen.getNombre())) {
                    origen.agregarConexion(destino.getNombre());
                    destino.agregarConexion(origen.getNombre());
                    System.out.println("Conexión mutua entre " + origen.getNombre() + " y " + destino.getNombre() + "!");
                }
    
                historial.registrarAccion("SOLICITUD_ACEPTADA", cliente.getNombre() + ";" + destino.getNombre());
                System.out.println("Solicitud aceptada.");
            }
        }
    }
    /*
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

            if (destino.yaSigueA(origen.getNombre())) {
                origen.agregarConexion(destino.getNombre());
                destino.agregarConexion(origen.getNombre());
                System.out.println("Conexión mutua entre " + origen.getNombre() + " y " + destino.getNombre() + "!");
            }

            historial.registrarAccion("SOLICITUD_ACEPTADA", s.getOrigen() + ";" + s.getDestino());
            System.out.println("Solicitud aceptada.");
        }
    }
    */

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
                
                //solicitudes.desencolar(); // elimina la última enviada
                Cliente cliente = buscarPorNombre(datos[0]);
                cliente.dejarSeguir();
                System.out.println("Deshecho envío de solicitud.");
                break;

            case "SOLICITUD_ACEPTADA":
                Cliente o = buscarPorNombre(datos[0]);
                Cliente d = buscarPorNombre(datos[1]);
                if (o != null && d != null) {
                    o.dejarDeSeguir(d.getNombre());
                    o.eliminarConexion(d.getNombre());
                    d.eliminarConexion(o.getNombre());
                    System.out.println("Deshecha aceptación de seguimiento y conexión.");
                }
                break;
        }
    }

    // ================== CARGAR ARBOL ==================
    public void cargarClientesImprimirNivel4()
    {
        //AVL<Cliente> avl = new AVL<>();
        ABB<Cliente> abb = new ABB<>();
        ListaDinamica<Cliente> clientes = diccionarioPorNombre.valores();
        
        int indice = 0;
        //clientes.imprimirLista();
        while (indice < clientes.getContador())
        {
            Cliente cliente = clientes.obtener(indice);
            if (clienteSigue(clientes, cliente)){
                abb.insertar(cliente);
                //avl.agregar(cliente);
            }
            indice++;
        }   
        //avl.imprimirPorNivel(4);
        abb.imprimirPorNivel(4);
    }

    private Boolean clienteSigue(ListaDinamica<Cliente> lista,Cliente seguido)
    {
        Boolean sigue = false;
        int indice = 0;
        while(indice<lista.getContador())
        {
            if (lista.obtener(indice).getSiguiendo().contiene(seguido.getNombre()))
            {
                sigue = true;
                return sigue;

            }
            indice++;
        }
        return sigue;
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
                    String nombre = o.get("nombre").getAsString();
                    int scoring = o.get("scoring").getAsInt();

                    if (scoring <= 100) {
                        Cliente nuevoCliente = new Cliente(nombre, scoring);
                        
                        if (o.has("siguiendo")) {
                            for (var s : o.getAsJsonArray("siguiendo")) {
                                nuevoCliente.seguir(s.getAsString());
                            }
                        }
                        
                        if (o.has("conexiones")) {
                            for (var con : o.getAsJsonArray("conexiones")) {
                                nuevoCliente.agregarConexion(con.getAsString());
                            }
                        }

                        agregarClienteAlSistema(nuevoCliente);
                    } else {
                        System.out.println("Error al cargar " + nombre + ": scoring > 100");
                    }
                }
            }
            lector.close();
        } catch (Exception e) {
            System.out.println("Error JSON");
        }
    }
}
