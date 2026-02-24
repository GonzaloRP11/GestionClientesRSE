package org.example;

public class ArbolAVL<T> {

    private NodoAVL<T> raiz;

    public ArbolAVL() {
        this.raiz = null;
    }

    public NodoAVL<T> getRaiz() { return raiz; }
    public void setRaiz(NodoAVL<T> raiz) { this.raiz = raiz; }

    public boolean estaVacio() {
        return raiz == null;
    }

    public void imprimirPorNivel(int nivel) {
        if (estaVacio()) {
            System.out.println("El árbol está vacío.");
            return;
        }
        imprimirPorNivelRecursivo(raiz, nivel, 0);
    }

    private void imprimirPorNivelRecursivo(NodoAVL<T> nodo, int nivelBuscado, int nivelActual) {
        if (nodo == null) return;

        if (nivelActual == nivelBuscado) {
            System.out.println(nodo.getDato());
        } else {
            imprimirPorNivelRecursivo(nodo.getIzquierdo(), nivelBuscado, nivelActual + 1);
            imprimirPorNivelRecursivo(nodo.getDerecho(), nivelBuscado, nivelActual + 1);
        }
    }
}
