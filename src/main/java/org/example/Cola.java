package org.example;

public class Cola<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;

    public Cola() {
        this.primero = null;
        this.ultimo = null;
    }

    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (estaVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
    }

    public T desencolar() {
        if (estaVacia()) {
            return null;
        }
        T dato = primero.getElemento();
        primero = primero.getSiguiente();
        if (primero == null) {
            ultimo = null;
        }
        return dato;
    }

    public boolean estaVacia() {
        return primero == null;
    }
}

