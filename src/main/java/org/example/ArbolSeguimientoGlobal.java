package org.example;

public class ArbolSeguimientoGlobal {

    private static class NodoSeg {
        Cliente dato;
        NodoSeg izq;
        NodoSeg der;
        NodoSeg(Cliente c) { dato = c; }
    }

    private static class RefNodo implements Comparable<RefNodo> {
        String nombre;
        NodoSeg ref;
        RefNodo(String nombre, NodoSeg ref) { this.nombre = nombre; this.ref = ref; }
        @Override public int compareTo(RefNodo o) { return nombre.compareToIgnoreCase(o.nombre); }
    }

    private NodoSeg raiz;

    private AVLRelaciones<RefNodo> indiceNodos = new AVLRelaciones<>();
    private AVLRelaciones<Cliente> indiceClientes = new AVLRelaciones<>();

    public void construir(ListaDinamica<Cliente> clientes) {
        if (clientes == null || clientes.getContador() == 0) return;

        // Índice nombre -> Cliente
        for (int i = 0; i < clientes.getContador(); i++) {
            indiceClientes.agregar(clientes.obtener(i));
        }

        // Raíz: primer cliente
        raiz = getOrCreateNodo(clientes.obtener(0));

        // Conectar relaciones siguiendo: máx 2 => izq/der
        for (int i = 0; i < clientes.getContador(); i++) {
            Cliente a = clientes.obtener(i);
            NodoSeg na = getOrCreateNodo(a);

            ListaDinamica<String> sig = a.getSiguiendo();

            if (sig.getContador() >= 1) {
                Cliente b = buscarCliente(sig.obtener(0));
                if (b != null) na.izq = getOrCreateNodo(b);
            }
            if (sig.getContador() >= 2) {
                Cliente c = buscarCliente(sig.obtener(1));
                if (c != null) na.der = getOrCreateNodo(c);
            }
        }
    }

    public void imprimirCuartoNivel() {
        imprimirNivelBFS(4); // raíz=0 => nivel4=4 (si tu profe usa raíz=1, poné 3)
    }

    // BFS por niveles + visitados (evita ciclos)
    public void imprimirNivelBFS(int objetivo) {
        if (raiz == null) return;

        Cola<NodoSeg> q = new Cola<>();
        Cola<Integer> qNivel = new Cola<>();

        AVLRelaciones<String> visitados = new AVLRelaciones<>();

        q.encolar(raiz);
        qNivel.encolar(0);
        visitados.agregar(raiz.dato.getNombre().toLowerCase());

        boolean imprimioAlgo = false;

        while (!q.estaVacia()) {
            NodoSeg n = q.desencolar();
            Integer nivel = qNivel.desencolar();
            if (nivel == null) break;

            if (nivel == objetivo) {
                System.out.println(n.dato.getNombre());
                imprimioAlgo = true;
                continue; // no expandir desde este nivel
            }
            if (nivel > objetivo) break;

            if (n.izq != null) {
                String key = n.izq.dato.getNombre().toLowerCase();
                if (visitados.buscar(key) == null) {
                    visitados.agregar(key);
                    q.encolar(n.izq);
                    qNivel.encolar(nivel + 1);
                }
            }

            if (n.der != null) {
                String key = n.der.dato.getNombre().toLowerCase();
                if (visitados.buscar(key) == null) {
                    visitados.agregar(key);
                    q.encolar(n.der);
                    qNivel.encolar(nivel + 1);
                }
            }
        }

        if (!imprimioAlgo) {
            System.out.println("(No hay clientes en el nivel " + objetivo + ")");
        }
    }

    private Cliente buscarCliente(String nombre) {
        return indiceClientes.buscar(new Cliente(nombre, 0));
    }

    private NodoSeg getOrCreateNodo(Cliente c) {
        RefNodo r = indiceNodos.buscar(new RefNodo(c.getNombre(), null));
        if (r != null) return r.ref;

        NodoSeg nuevo = new NodoSeg(c);
        indiceNodos.agregar(new RefNodo(c.getNombre(), nuevo));
        return nuevo;
    }
}