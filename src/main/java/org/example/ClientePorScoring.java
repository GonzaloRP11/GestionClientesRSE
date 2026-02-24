package org.example;

public class ClientePorScoring implements Comparable<ClientePorScoring> {
    private Cliente cliente;

    public ClientePorScoring(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public int compareTo(ClientePorScoring otro) {
        
        if (this.cliente.getScoring() != otro.cliente.getScoring()) {
            return Integer.compare(this.cliente.getScoring(), otro.cliente.getScoring());
        }
        
        return this.cliente.getNombre().compareToIgnoreCase(otro.cliente.getNombre());
    }

    @Override
    public String toString() {
        return String.format("%s (%d pts)", cliente.getNombre(), cliente.getScoring());
    }
}
