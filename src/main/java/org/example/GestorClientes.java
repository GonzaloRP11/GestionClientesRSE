package org.example;

import com.google.gson.Gson;

public class GestorClientes {
    
    private GrafoDinamico grafoRelaciones; 
    private Diccionario<String, Cliente> diccionarioPorNombre;
    private Diccionario<Integer, ListaDinamica<Cliente>> diccionarioPorScoring;
    private AVLRelaciones<ClientePorScoring> arbolPorScoring;

    private HistorialAcciones historial;

    public GestorClientes() {
        diccionarioPorNombre = new Diccionario<>();
        diccionarioPorScoring = new Diccionario<>();
        arbolPorScoring = new AVLRelaciones<>();
        historial = new HistorialAcciones();
        grafoRelaciones = new GrafoDinamico(); 
        cargarDesdeJson();
        cargarGrafoRelaciones();
    }

    

    private void agregarClienteAlSistema(Cliente cliente) {

        diccionarioPorNombre.insertar(cliente.getNombre().toLowerCase(), cliente);

        ListaDinamica<Cliente> lista = diccionarioPorScoring.obtener(cliente.getScoring());
        if (lista == null) {
            lista = new ListaDinamica<>();
            diccionarioPorScoring.insertar(cliente.getScoring(), lista);
        }
        lista.agregar(cliente);

        
        arbolPorScoring.agregar(new ClientePorScoring(cliente));

        // Registrar el cliente como vértice en el grafo para que pueda tener relaciones
        grafoRelaciones.agregarVertice(cliente);
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
            System.out.println("Aviso: Ya existe un cliente con ese nombre.");
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

    

    public void enviarSolicitudSeguimiento(Cliente cliente, ListaEstatica<String> seguidores) {

        for (int i = 0; i < seguidores.getContador(); i++) {

            cliente.aSeguir(seguidores.obtener(i)); 

            historial.registrarAccion(
                    "SOLICITUD_ENVIADA",
                    cliente.getNombre() + ";" + seguidores.obtener(i)
            );

            System.out.println("Solicitud enviada: "
                    + cliente.getNombre() + " -> "
                    + seguidores.obtener(i));
        }
    }

    public void procesarSolicitudSeguimiento(Cliente cliente) {

        if (cliente.getSolicitudes().estaVacia()) {
            System.out.println("No hay solicitudes a procesar.");
            return;
        }

        Cola<String> procesarSolicitudes = cliente.getSolicitudes();

        while (!procesarSolicitudes.estaVacia()) {

            String aSeguir = procesarSolicitudes.desencolar();

            Cliente origen = buscarPorNombre(cliente.getNombre());
            Cliente destino = buscarPorNombre(aSeguir);

            if (origen != null && destino != null) {

                origen.seguir(destino.getNombre());
                
                // Reflejar el seguimiento en el grafo inmediatamente
                grafoRelaciones.agregarRelacionDirigida(origen, destino);

                if (destino.yaSigueA(origen.getNombre())) {

                    origen.agregarConexion(destino.getNombre());
                    destino.agregarConexion(origen.getNombre());
                    
                    grafoRelaciones.agregarRelacion(origen, destino);

                    System.out.println("Conexión mutua entre "
                            + origen.getNombre()
                            + " y "
                            + destino.getNombre() + "!");
                }

                historial.registrarAccion(
                        "SOLICITUD_ACEPTADA",
                        cliente.getNombre() + ";" + destino.getNombre()
                );

                System.out.println("Solicitud aceptada.");
            }
        }
    }

    

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
                agregarClienteAlSistema(
                        new Cliente(datos[0], Integer.parseInt(datos[1]))
                );
                break;

            case "SOLICITUD_ENVIADA":
                Cliente cliente = buscarPorNombre(datos[0]);
                if (cliente != null) {
                    cliente.dejarSeguir();
                    System.out.println("Deshecho envío de solicitud.");
                }
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

    

    public Cliente buscarPorNombre(String nombre) {
        return diccionarioPorNombre.obtener(nombre.toLowerCase());
    }

    public ListaDinamica<Cliente> buscarPorScoring(int scoring) {
        ListaDinamica<Cliente> resultado = diccionarioPorScoring.obtener(scoring);
        return (resultado != null) ? resultado : new ListaDinamica<>();
    }
    public void imprimirSiguiendoDeCliente(String nombre) {
        Cliente cliente = buscarPorNombre(nombre);

        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        if (cliente.getSiguiendo().getContador() == 0) {
            System.out.println("El cliente no sigue a nadie.");
            return;
        }

        System.out.println("Clientes que sigue " + cliente.getNombre() + ":");

        for (int i = 0; i < cliente.getSiguiendo().getContador(); i++) {
            System.out.println(cliente.getSiguiendo().obtener(i));
        }
    }

    public void imprimirLista() {
        diccionarioPorNombre.valores().imprimirLista();
    }

    public HistorialAcciones getHistorial() {
        return historial;
    }

    



    public void visualizarArbolSeguimiento() {

        ListaDinamica<Cliente> clientes = diccionarioPorNombre.valores();

        if (clientes.getContador() == 0) {
            System.out.println("No hay clientes cargados.");
            return;
        }

        ArbolSeguimientoGlobal arbol = new ArbolSeguimientoGlobal();
        arbol.construir(clientes);

        arbol.imprimirVisual();
    }

    public void visualizarArbolScoring() {
        System.out.println("\nESTRUCTURA DEL ÁRBOL AVL (Ordenado por Scoring):");
        arbolPorScoring.imprimirTodoElArbol();
    }

    public void cargarCasoPruebaNivel4() {
        System.out.println("\n--- Cargando Caso de Prueba (Nivel 4) ---");
        
        
        agregarCliente("Frank", 70);
        agregarCliente("Gina", 60);

        
        Cliente david = buscarPorNombre("David");
        Cliente frank = buscarPorNombre("Frank");
        Cliente gina = buscarPorNombre("Gina");

        if (david == null || frank == null || gina == null) {
            System.out.println("Error: Asegúrese de que el archivo JSON tenga al cliente 'David'.");
            return;
        }

        
        if (!david.yaSigueA("Frank")) {
            david.seguir("Frank");
            grafoRelaciones.agregarRelacionDirigida(david, frank);
        }
        if (!frank.yaSigueA("Gina")) {
            frank.seguir("Gina");
            grafoRelaciones.agregarRelacionDirigida(frank, gina);
        }

        System.out.println("Relaciones creadas: David -> Frank -> Gina");
        System.out.println("Caso de prueba cargado con éxito.");
    }

    

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
                    }
                }
            }

            lector.close();
            cargarGrafoRelaciones();
        } catch (Exception e) {
            System.out.println("Error JSON");
        }
        }
        
        public void cargarGrafoRelaciones() 
        {
            ListaDinamica<Cliente> clientes = diccionarioPorNombre.valores();
            grafoRelaciones.cargarDesdeLista(clientes);
        }

        public void imprimirGrafo(){
            System.out.println("Impresión de grafo\n");
            grafoRelaciones.imprimirGrafo();
        }

        public void imprimirVecinos(String nombre) 
        {
            Cliente cliente = buscarPorNombre(nombre);
            if (cliente != null) {
                ListaDinamica<Cliente> vecinos = grafoRelaciones.obtenerVecinos(cliente);
                System.out.println("\n=== Vecinos de " + nombre + " ===");
                if (vecinos.getContador() == 0) {
                    System.out.println("  (Sin conexiones)");
                } else {
                    System.out.println("Total vecinos: " + vecinos.getContador());
                    vecinos.imprimirLista();
                }
            } else {
                System.out.println("Cliente no encontrado.");
            }
        }

        public void imprimirDistancia(String nombre1, String nombre2) 
        {
            Cliente c1 = buscarPorNombre(nombre1);
            Cliente c2 = buscarPorNombre(nombre2);
            
            if (c1 != null && c2 != null) {
                int distancia = grafoRelaciones.calcularDistancia(c1, c2);
                if (distancia == -1) {
                    System.out.println("No hay camino entre " + nombre1 + " y " + nombre2);
                } else {
                    System.out.println("Distancia entre " + nombre1 + " y " + nombre2 + ": " + distancia + " saltos");
                }
            } else {
                System.out.println("Uno o ambos clientes no existen.");
            }
        }

        public void agregarRelacionClientes(String A,String B){
            Cliente clienteA = diccionarioPorNombre.obtener(A.toLowerCase());
            Cliente clienteB = diccionarioPorNombre.obtener(B.toLowerCase());
            
            if (clienteA != null && clienteB != null) {
                grafoRelaciones.agregarRelacion(clienteA, clienteB);
                clienteA.agregarConexion(clienteB.getNombre());
                clienteB.agregarConexion(clienteA.getNombre());
                
                System.out.println("Relación agregada exitosamente entre " + A + " y " + B);
            } else {
                System.out.println("Error: Uno o ambos clientes no existen.");
            }
        }
    
}
