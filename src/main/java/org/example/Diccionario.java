package org.example;

public class Diccionario<K, V> {
    private class Entrada {
        K clave;
        V valor;

        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    private ListaDinamica<Entrada>[] tabla;
    private int tamaño;
    private static final int CAPACIDAD_INICIAL = 16;

    @SuppressWarnings("unchecked")
    public Diccionario() {
        tabla = new ListaDinamica[CAPACIDAD_INICIAL];
        for (int i = 0; i < CAPACIDAD_INICIAL; i++) {
            tabla[i] = new ListaDinamica<>();
        }
        tamaño = 0;
    }

    private int hash(K clave) {
        return Math.abs(clave.hashCode()) % tabla.length;
    }

    public void insertar(K clave, V valor) {
        int indice = hash(clave);
        ListaDinamica<Entrada> lista = tabla[indice];
        
        for (int i = 0; i < lista.getContador(); i++) {
            Entrada e = lista.obtener(i);
            if (e.clave.equals(clave)) {
                e.valor = valor;
                return;
            }
        }
        
        lista.agregar(new Entrada(clave, valor));
        tamaño++;
    }

    public V obtener(K clave) {
        int indice = hash(clave);
        ListaDinamica<Entrada> lista = tabla[indice];
        
        for (int i = 0; i < lista.getContador(); i++) {
            Entrada e = lista.obtener(i);
            if (e.clave.equals(clave)) {
                return e.valor;
            }
        }
        return null;
    }

    public void eliminar(K clave) {
        int indice = hash(clave);
        ListaDinamica<Entrada> lista = tabla[indice];
        
        for (int i = 0; i < lista.getContador(); i++) {
            Entrada e = lista.obtener(i);
            if (e.clave.equals(clave)) {
                lista.eliminar(i);
                tamaño--;
                return;
            }
        }
    }

    public boolean contiene(K clave) {
        return obtener(clave) != null;
    }

    public int size() {
        return tamaño;
    }

    public ListaDinamica<K> claves() {
        ListaDinamica<K> todasLasClaves = new ListaDinamica<>();
        for (ListaDinamica<Entrada> lista : tabla) {
            for (int i = 0; i < lista.getContador(); i++) {
                todasLasClaves.agregar(lista.obtener(i).clave);
            }
        }
        return todasLasClaves;
    }

    public ListaDinamica<V> valores() {
        ListaDinamica<V> todosLosValores = new ListaDinamica<>();
        for (ListaDinamica<Entrada> lista : tabla) {
            for (int i = 0; i < lista.getContador(); i++) {
                todosLosValores.agregar(lista.obtener(i).valor);
            }
        }
        return todosLosValores;
    }
}

