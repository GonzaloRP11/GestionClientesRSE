package org.example;

public class ListaDinamica<T> {  // linked list

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int contador = 0;

    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        contador++;
    }

    public void imprimirLista() {
        Nodo<T> actual = primero;
        while (actual != null) {
            System.out.println(actual.getElemento());
            actual = actual.getSiguiente();
        }
    }

    public T obtener(int indice) {
        Nodo<T> actual = primero;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getElemento();
    }

    public void insertar(int pos, T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (pos == 0) {
            nuevo.setSiguiente(primero);
            primero = nuevo;
        } else {
            Nodo<T> actual = primero;
            for (int i = 0; i < pos - 1; i++) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        contador++;
    }

    public void eliminar(int pos) {
        if (pos == 0) {
            primero = primero.getSiguiente();
        } else {
            Nodo<T> actual = primero;
            for (int i = 0; i < pos - 1; i++) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
            if (pos == contador - 1) {
                ultimo = actual;
            }
        }
        contador--;
    }

    public int getContador() {
        return contador;
    }

    public boolean contiene(T elemento) {
        Nodo<T> actual = primero;
        while (actual != null) {
            if (actual.getElemento().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    @Override
    public String toString() {
        if (primero == null) return "[]";
        String resultado = "[";
        Nodo<T> actual = primero;
        while (actual != null) {
            resultado += actual.getElemento();
            if (actual.getSiguiente() != null) {
                resultado += ", ";
            }
            actual = actual.getSiguiente();
        }
        resultado += "]";
        return resultado;
    }
}

class Nodo<T> {
    private T elemento;
    private Nodo<T> siguiente;

    public Nodo(T elemento) {
        this.elemento = elemento;
        this.siguiente = null;
    }

    public T getElemento() {
        return elemento;
    }

    public void setElemento(T elemento) {
        this.elemento = elemento;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}