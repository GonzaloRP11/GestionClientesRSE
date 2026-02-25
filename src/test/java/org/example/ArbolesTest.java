package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArbolesTest {

    @Test
    public void testBusquedaScoringAVL() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("Test1", 50);
        gestor.agregarCliente("Test2", 90);
        
        Cliente encontrado = gestor.buscarPorNombre("Test1");
        assertNotNull(encontrado);
        assertEquals(50, encontrado.getScoring());
    }

    @Test
    public void testOrdenamientoScoring() {
        Cliente c1 = new Cliente("Zoe", 10);
        Cliente c2 = new Cliente("Ana", 100);
        
        ClientePorScoring cs1 = new ClientePorScoring(c1);
        ClientePorScoring cs2 = new ClientePorScoring(c2);
        
        assertTrue(cs1.compareTo(cs2) < 0, "Zoe (10) debería ser menor que Ana (100) en el árbol");
    }

    @Test
    public void testArbolSeguimientoEstructura() {
        ListaDinamica<Cliente> lista = new ListaDinamica<>();
        Cliente a = new Cliente("Alice", 100);
        Cliente b = new Cliente("Bob", 90);
        
        a.seguir("Bob");
        lista.agregar(a);
        lista.agregar(b);
        
        ArbolSeguimientoGlobal arbol = new ArbolSeguimientoGlobal();
        arbol.construir(lista);
        
        assertDoesNotThrow(arbol::imprimirCuartoNivel);
    }

    @Test
    public void testDiferenciacionMismoScoring() {
        Cliente c1 = new Cliente("Ana", 50);
        Cliente c2 = new Cliente("Zoe", 50);
        
        ClientePorScoring cs1 = new ClientePorScoring(c1);
        ClientePorScoring cs2 = new ClientePorScoring(c2);
        
        assertNotEquals(0, cs1.compareTo(cs2), "Clientes con mismo scoring pero distinto nombre deben ser nodos distintos");
    }
}