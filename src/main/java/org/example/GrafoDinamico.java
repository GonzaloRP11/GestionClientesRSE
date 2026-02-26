package org.example;

public class GrafoDinamico {
    NodoGrafo origen;
    Diccionario<String, NodoGrafo> indiceVertices; 
    
    public GrafoDinamico() {
        origen = null;
        indiceVertices = new Diccionario<>();
    }
    /*public void agregarVertice(Cliente v) {
        NodoGrafo nuevoNodo = new NodoGrafo();
        nuevoNodo.nodo = v;
        nuevoNodo.arista = null;
        nuevoNodo.nodSig = origen;
        origen = nuevoNodo;
    } */
   
    public void agregarVertice(Cliente cliente) 
    {
        String key = cliente.getNombre().toLowerCase();
        if (!indiceVertices.contiene(key)) {  // ← Verificar si existe
            NodoGrafo nuevoNodo = new NodoGrafo(cliente,null,null);
            nuevoNodo.nodSig = origen;
            origen = nuevoNodo;
            indiceVertices.insertar(key, nuevoNodo);  // ← AGREGAR AL ÍNDICE
        }
    }
    public void agregarArista(Cliente v1, Cliente v2, int peso) {
        NodoGrafo nodo1 = buscarVertice(v1);
        NodoGrafo nodo2 = buscarVertice(v2);
        if (nodo1 != null && nodo2 != null) {
            NodoArista nuevaArista = new NodoArista();
            nuevaArista.etiqueta = peso;
            nuevaArista.NodoGrafo = nodo2;
            nuevaArista.sigArista = nodo1.arista;
            nodo1.arista = nuevaArista;
        }
    }
    /*private NodoGrafo buscarVertice(Cliente v1) {
        NodoGrafo actual = origen;
        while (actual != null) {
            if (actual.nodo == v1) {
                return actual;
            }
            actual = actual.nodSig;
        }
        return null; // Si no se encuentra el vértice
    }*/
    private NodoGrafo buscarVertice(Cliente cliente) {
        return indiceVertices.obtener(cliente.getNombre().toLowerCase());  
    }

    private NodoGrafo buscarVerticePorNombre(String nombre) {
        return indiceVertices.obtener(nombre.toLowerCase());
    }

    public void agregarRelacion(Cliente cliente1, Cliente cliente2) {
        NodoGrafo nodo1 = buscarVertice(cliente1);
        NodoGrafo nodo2 = buscarVertice(cliente2);
        
        if (nodo1 != null && nodo2 != null) {
            agregarAristaUnidireccional(nodo1, nodo2);
            agregarAristaUnidireccional(nodo2, nodo1);  // ← Bidireccional
        }
    }

    public void agregarRelacionDirigida(Cliente origen, Cliente destino) {
        NodoGrafo nodoO = buscarVertice(origen);
        NodoGrafo nodoD = buscarVertice(destino);
        
        if (nodoO != null && nodoD != null) {
            agregarAristaUnidireccional(nodoO, nodoD);
        }
    }

    private void agregarAristaUnidireccional(NodoGrafo origen, NodoGrafo destino) {
        if (!existeArista(origen, destino)) {
            NodoArista nuevaArista = new NodoArista();
            nuevaArista.setEtiqueta(1);
            nuevaArista.setNodoGrafo(destino);
            nuevaArista.setSigArista(origen.getArista());
            origen.setArista(nuevaArista);
        }
    }

    private boolean existeArista(NodoGrafo origen, NodoGrafo destino) {
        NodoArista actual = origen.getArista();
        while (actual != null) {
            if (actual.getNodoGrafo().equals(destino)) return true;
            actual = actual.getSigArista();
        }
        return false;
    }
    /*
    public void imprimirGrafo() {
        System.out.print("--- Grafo Dinámico ---\n");
        NodoGrafo actual = origen;
        while (actual != null) {
            System.out.print("Nodo " + actual.nodo + ": ");
            NodoArista aristaActual = actual.arista;
            while (aristaActual != null) {
                System.out.print(" -> " + aristaActual.NodoGrafo.nodo + "(" + aristaActual.etiqueta + ")");
                aristaActual = aristaActual.sigArista;
            }
            System.out.println();
            actual = actual.nodSig;
        }
    } 
    */
   //Carga de grafo
    public void cargarDesdeLista(ListaDinamica<Cliente> clientes) {
        
        if (clientes == null || clientes.getContador() == 0) {
            System.out.println("No hay clientes para cargar.");
            return;
        }
        
        /*System.out.println("=== Cargando Grafo de Relaciones ===");
        System.out.println("Total clientes: " + clientes.getContador());*/
        
        // Agregar todos los vértices
        for (int i = 0; i < clientes.getContador(); i++) {
            agregarVertice(clientes.obtener(i));
        }
        //System.out.println("Vértices agregados: " + clientes.getContador());
        
        //  Agregar todas las conexiones (seguimientos y amistades)
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente cliente = clientes.obtener(i);
            
            // 1. Agregar seguidores (Relaciones dirigidas)
            ListaDinamica<String> siguiendo = cliente.getSiguiendo();
            for (int j = 0; j < siguiendo.getContador(); j++) {
                Cliente destino = buscarClientePorNombre(siguiendo.obtener(j), clientes);
                if (destino != null) {
                    agregarRelacionDirigida(cliente, destino);
                }
            }

            // 2. Asegurar conexiones mutuas (por si acaso no estaban en siguiendo)
            ListaDinamica<String> conexiones = cliente.getConexiones();
            for (int j = 0; j < conexiones.getContador(); j++) {
                String nombreAmigo = conexiones.obtener(j);
                Cliente amigo = buscarClientePorNombre(nombreAmigo, clientes);
                
                if (amigo != null) {
                    agregarRelacion(cliente, amigo);
                }
            }
        }
        
        /*System.out.println("Conexiones agregadas: " + (totalConexiones / 2) + " (relaciones únicas)");
        System.out.println("=== Grafo Cargado ===\n");*/
    }
    
    // Auxiliar: buscar cliente por nombre en la lista
    private Cliente buscarClientePorNombre(String nombre, ListaDinamica<Cliente> clientes) {
        for (int i = 0; i < clientes.getContador(); i++) {
            if (clientes.obtener(i).getNombre().equalsIgnoreCase(nombre)) {
                return clientes.obtener(i);
            }
        }
        return null;
    }

    public void imprimirGrafo() {
        System.out.println("--- Grafo de Relaciones ---");
        NodoGrafo actual = origen;
        while (actual != null) {
            System.out.print("Cliente " + actual.getCliente().getNombre() + ": ");
            NodoArista aristaActual = actual.getArista();
            while (aristaActual != null) {
                System.out.print(" -> " + aristaActual.getNodoGrafo().getCliente().getNombre());
                aristaActual = aristaActual.getSigArista();
            }
            System.out.println();
            actual = actual.getNodSig();
        }
    }

    public ListaDinamica<Cliente> obtenerVecinos(Cliente cliente) {
        ListaDinamica<Cliente> vecinos = new ListaDinamica<>();
        NodoGrafo nodo = buscarVertice(cliente);
        
        if (nodo != null) {
            NodoArista actual = nodo.getArista();
            while (actual != null) {
                vecinos.agregar(actual.getNodoGrafo().getNodo());
                actual = actual.getSigArista();
            }
        }
        
        return vecinos;
    }

    public int calcularDistancia(Cliente origen, Cliente destino) {
        //  mismo cliente
        if (origen.getNombre().equalsIgnoreCase(destino.getNombre())) {
            return 0;
        }
        
        // Estructuras para BFS
        Cola<NodoGrafo> cola = new Cola<>();
        Diccionario<String, Integer> distancias = new Diccionario<>();
        Diccionario<String, Boolean> visitados = new Diccionario<>();
        
        // Inicializar
        NodoGrafo nodoOrigen = buscarVertice(origen);
        if (nodoOrigen == null) return -1;  // Origen no existe
        
        cola.encolar(nodoOrigen);
        visitados.insertar(origen.getNombre().toLowerCase(), true);
        distancias.insertar(origen.getNombre().toLowerCase(), 0);
        
        // BFS
        while (!cola.estaVacia()) {
            NodoGrafo actual = cola.desencolar();
            String keyActual = actual.getNodo().getNombre().toLowerCase();
            int distanciaActual = distancias.obtener(keyActual);
            
            // Recorrer vecinos (aristas)
            NodoArista arista = actual.getArista();
            while (arista != null) {
                Cliente vecino = arista.getNodoGrafo().getNodo();
                String keyVecino = vecino.getNombre().toLowerCase();
                
                if (!visitados.contiene(keyVecino)) {
                    visitados.insertar(keyVecino, true);
                    distancias.insertar(keyVecino, distanciaActual + 1);
                    
                    
                    if (vecino.getNombre().equalsIgnoreCase(destino.getNombre())) {
                        return distanciaActual + 1;  // ← Distancia encontrada!
                    }
                    
                    cola.encolar(arista.getNodoGrafo());
                }
                arista = arista.getSigArista();
            }
        }
        
        return -1;  //  No hay camino entre los clientes
    }
}