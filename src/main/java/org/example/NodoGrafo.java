package org.example;
public class NodoGrafo {
    Cliente nodo;
    NodoArista arista;
    NodoGrafo nodSig;

    public NodoGrafo() {
        this.nodo = null;
        this.nodSig = null;
        this.arista = null;
    }
    
    public NodoGrafo(Cliente nodo, NodoArista arista, NodoGrafo nodSig) {
        this.nodo = nodo;
        this.arista = arista;
        this.nodSig = nodSig;
    }

     public Cliente getCliente() {
        return nodo;
    }
    public Cliente getNodo() {
        return nodo;
    }
    public void setNodo(Cliente nodo) {
        this.nodo = nodo;
    }
    public NodoGrafo getNodSig() {
        return nodSig;
    }
    public void setNodSig(NodoGrafo nodSig) {
        this.nodSig = nodSig;
    }
    public NodoArista getArista() {
        return arista;
    }
    public void setArista(NodoArista arista) {
        this.arista = arista;
    }
    @Override
    public String toString() {
        return "NodoGrafo{" +
                "nodo=" + nodo +
                ", arista=" + arista +
                ", nodSig=" + nodSig +
                '}';
    }
}
