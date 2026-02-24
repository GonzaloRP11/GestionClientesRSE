package org.example;

public class ListaEstatica<T> {
    private T[] elementos;
    private int contador = 0;

    @SuppressWarnings("unchecked")
    public ListaEstatica(int capacidad) {
        elementos = (T[]) new Object[capacidad];
    }

    public void insertar(int pos, T elemento) {
        for (int i = contador; i > pos; i--) { 
            elementos[i] = elementos[i - 1];
        }
        elementos[pos] = elemento; 
        contador++; 
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


