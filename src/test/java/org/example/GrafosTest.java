package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrafosTest {

    private GrafoDinamico grafo;
    private Cliente alice;
    private Cliente bob;
    private Cliente charlie;
    private Cliente david;

    @BeforeEach
    public void setUp() {
        grafo = new GrafoDinamico();
        alice = new Cliente("Alice", 90);
        bob = new Cliente("Bob", 80);
        charlie = new Cliente("Charlie", 70);
        david = new Cliente("David", 60);

        grafo.agregarVertice(alice);
        grafo.agregarVertice(bob);
        grafo.agregarVertice(charlie);
        grafo.agregarVertice(david);
    }

    @Test
    public void testAgregarRelacionYVecinos() {
        grafo.agregarRelacion(alice, bob);

        ListaDinamica<Cliente> vecinosAlice = grafo.obtenerVecinos(alice);
        ListaDinamica<Cliente> vecinosBob = grafo.obtenerVecinos(bob);

        assertEquals(1, vecinosAlice.getContador(), "Alice debería tener 1 vecino");
        assertEquals(1, vecinosBob.getContador(), "Bob debería tener 1 vecino");
        assertEquals("Bob", vecinosAlice.obtener(0).getNombre());
        assertEquals("Alice", vecinosBob.obtener(0).getNombre());
    }

    @Test
    public void testDistanciaDirecta() {
        grafo.agregarRelacion(alice, bob);
        int distancia = grafo.calcularDistancia(alice, bob);
        assertEquals(1, distancia, "La distancia entre amigos directos debe ser 1");
    }

    @Test
    public void testDistanciaIndirecta() {
        grafo.agregarRelacion(alice, bob);
        grafo.agregarRelacion(bob, charlie);

        int distancia = grafo.calcularDistancia(alice, charlie);
        assertEquals(2, distancia, "La distancia Alice -> Bob -> Charlie debe ser 2");
    }

    @Test
    public void testDistanciaLarga() {
        grafo.agregarRelacion(alice, bob);
        grafo.agregarRelacion(bob, charlie);
        grafo.agregarRelacion(charlie, david);

        int distancia = grafo.calcularDistancia(alice, david);
        assertEquals(3, distancia, "La distancia Alice a David debe ser 3 saltos");
    }

    @Test
    public void testSinCamino() {
        grafo.agregarRelacion(alice, bob);

        int distancia = grafo.calcularDistancia(alice, david);
        assertEquals(-1, distancia, "Si no hay conexión, la distancia debe ser -1");
    }

    @Test
    public void testDistanciaMismoCliente() {
        int distancia = grafo.calcularDistancia(alice, alice);
        assertEquals(0, distancia, "La distancia a uno mismo debe ser 0");
    }
}
