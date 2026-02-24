package org.example;

public class Pila <T>{
    private Nodo<T> cima;

    public Pila() {
        this.cima = null;
    }

    public void apilar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        nuevo.setSiguiente(cima);
        cima = nuevo;
    }

    public T desapilar() {
        if (estaVacia()) {
            return null;
        }
        T dato = cima.getElemento();
        cima = cima.getSiguiente();
        return dato;
    }

    public T cima() {
        if (estaVacia()) {
            return null;
        }
        return cima.getElemento();
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public void mostrarElementos() {
        Nodo<T> actual = cima;
        while (actual != null) {
            System.out.println(actual.getElemento());
            actual = actual.getSiguiente();
        }
    }
}

