package org.example;

public class ListaEstatica<T> {
    private T[] elementos;
    private int contador = 0;

    @SuppressWarnings("unchecked")
    public ListaEstatica(int capacidad) {
        elementos = (T[]) new Object[capacidad];
    }

    public void insertar(int pos, T elemento) {
        for (int i = contador; i > pos; i--) { // 1 
            elementos[i] = elementos[i - 1];
        }
        elementos[pos] = elemento; // 1
        contador++; // 1
    }

    public void agregar(T elemento) {
        elementos[contador++] = elemento;
    }

    public T obtener(int indice) {
        return elementos[indice];
    }

    public void imprimirLista() {
        for (int i = 0; i < contador; i++) {
            System.out.println(elementos[i]);
        }
    }

    public int getContador(){
        return contador;
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