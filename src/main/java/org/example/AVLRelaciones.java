package org.example;

public class AVLRelaciones<T extends Comparable<T>> {

    private ArbolAVL<T> arbol;

    public AVLRelaciones() {
        arbol = new ArbolAVL<>();
    }

    public void agregar(T elemento) {
        arbol.setRaiz(agregarRecursivo(arbol.getRaiz(), elemento));
    }
    public T buscar(T elemento) {
        NodoAVL<T> n = buscarRec(arbol.getRaiz(), elemento);
        return (n == null) ? null : n.getDato();
    }

    private NodoAVL<T> buscarRec(NodoAVL<T> nodo, T elemento) {
        if (nodo == null) return null;

        int cmp = elemento.compareTo(nodo.getDato());
        if (cmp == 0) return nodo;
        if (cmp < 0) return buscarRec(nodo.getIzquierdo(), elemento);
        return buscarRec(nodo.getDerecho(), elemento);
    }


    private NodoAVL<T> agregarRecursivo(NodoAVL<T> nodo, T elemento) {
        if (nodo == null) {
            return new NodoAVL<>(elemento);
        }

        int cmp = elemento.compareTo(nodo.getDato());

        if (cmp < 0) {
            nodo.setIzquierdo(agregarRecursivo(nodo.getIzquierdo(), elemento));
        } else if (cmp > 0) {
            nodo.setDerecho(agregarRecursivo(nodo.getDerecho(), elemento));
        } else {
            return nodo;
        }

        nodo.setAltura(1 + Math.max(
                altura(nodo.getIzquierdo()),
                altura(nodo.getDerecho())
        ));

        int balance = getBalance(nodo);

        
        if (balance > 1 && elemento.compareTo(nodo.getIzquierdo().getDato()) < 0)
            return rotarDerecha(nodo);

        if (balance < -1 && elemento.compareTo(nodo.getDerecho().getDato()) > 0)
            return rotarIzquierda(nodo);

        if (balance > 1 && elemento.compareTo(nodo.getIzquierdo().getDato()) > 0) {
            nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            return rotarDerecha(nodo);
        }

        if (balance < -1 && elemento.compareTo(nodo.getDerecho().getDato()) < 0) {
            nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private int altura(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 : nodo.getAltura();
    }

    private int getBalance(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 :
                altura(nodo.getIzquierdo()) - altura(nodo.getDerecho());
    }

    private NodoAVL<T> rotarDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.getIzquierdo();
        NodoAVL<T> T2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(T2);

        y.setAltura(1 + Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())));
        x.setAltura(1 + Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())));

        return x;
    }

    private NodoAVL<T> rotarIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.getDerecho();
        NodoAVL<T> T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        x.setAltura(1 + Math.max(altura(x.getIzquierdo()), altura(x.getDerecho())));
        y.setAltura(1 + Math.max(altura(y.getIzquierdo()), altura(y.getDerecho())));

        return y;
    }

    public void imprimirNivel(int nivel) {
        arbol.imprimirPorNivel(nivel);
    }

    public void imprimirTodoElArbol() {
        if (arbol.estaVacio()) {
            System.out.println("Árbol vacío.");
            return;
        }
        imprimirRecursivo(arbol.getRaiz(), "", true);
    }

    private void imprimirRecursivo(NodoAVL<T> nodo, String prefijo, boolean esUltimo) {
        if (nodo == null) return;

        System.out.print(prefijo);
        System.out.print(esUltimo ? "└── " : "├── ");
        System.out.println(nodo.getDato().toString());

        String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");

        if (nodo.getIzquierdo() != null || nodo.getDerecho() != null) {
            imprimirRecursivo(nodo.getIzquierdo(), nuevoPrefijo, nodo.getDerecho() == null);
            imprimirRecursivo(nodo.getDerecho(), nuevoPrefijo, true);
        }
    }

    public boolean estaVacio() {
        return arbol.estaVacio();
    }
}
