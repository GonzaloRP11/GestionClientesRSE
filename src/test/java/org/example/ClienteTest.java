package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClienteTest {

    @Test
    public void testAgregarYBuscarPorNombre() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("AlejandroTest", 75);

        Cliente c = gestor.buscarPorNombre("AlejandroTest");
        assertNotNull("Cliente agregado debe encontrarse por nombre", c);
        assertEquals("AlejandroTest", c.getNombre());
        assertEquals(75, c.getScoring());
    }

    @Test
    public void testBuscarPorScoring() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("TestScoring1", 50);
        gestor.agregarCliente("TestScoring2", 50);

        ListaDinamica<Cliente> encontrados = gestor.buscarPorScoring(50);
        assertTrue("Debe haber al menos 2 clientes con scoring 50", encontrados.getContador() >= 2);
    }

    @Test
    public void testHistorialRegistroAlAgregar() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("GonzaloTest", 30);

        HistorialAcciones h = gestor.getHistorial();
        Accion a = h.deshacerAccion();
        assertNotNull("Debe existir una acción en el historial después de agregar", a);
        assertEquals("AGREGAR", a.getTipo());
        assertEquals("GonzaloTest;30", a.getDetalle());
    }

    @Test
    public void testDeshacerAgregar() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("DeshacerMe", 100);
        assertNotNull(gestor.buscarPorNombre("DeshacerMe"));
        
        gestor.deshacerUltimaAccion();
        assertNull("El cliente debe haber sido eliminado tras deshacer", gestor.buscarPorNombre("DeshacerMe"));
    }

    @Test
    public void testDeshacerEliminar() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("VolverAca", 80);
        gestor.eliminarCliente("VolverAca");
        assertNull(gestor.buscarPorNombre("VolverAca"));
        
        gestor.deshacerUltimaAccion();
        assertNotNull("El cliente debe haber sido restaurado tras deshacer", gestor.buscarPorNombre("VolverAca"));
        assertEquals(80, gestor.buscarPorNombre("VolverAca").getScoring());
    }
}
