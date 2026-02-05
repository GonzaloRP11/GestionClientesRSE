package org.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClienteTest {

    @Test
    public void testAgregarYBuscarPorNombre() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("Alejadro", 75);

        Cliente c = gestor.buscarPorNombre("Alejadro");
        assertNotNull("Cliente agregado debe encontrarse por nombre", c);
        assertEquals("Alejadro", c.getNombre());
        assertEquals(75, c.getScoring());
    }

    @Test
    public void testAgregarYBuscarPorScoring() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("Nicolas", 50);

        Cliente c = gestor.buscarPorScoring(50);
        assertNotNull("Cliente con scoring 50 debe encontrarse", c);
        assertEquals("Nicolas", c.getNombre());
        assertEquals(50, c.getScoring());
    }

    @Test
    public void testHistorialRegistroAlAgregar() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente("Gonzalo", 30);

        HistorialAcciones h = gestor.getHistorial();
        Accion a = h.deshacerAccion();
        assertNotNull("Debe existir una acción en el historial después de agregar", a);
        assertEquals("Agregar cliente", a.getTipo());
        assertEquals("Gonzalo", a.getDetalle());
    }

}
