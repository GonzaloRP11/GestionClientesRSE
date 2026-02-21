package org.example;

// import com.google.gson.Gson;
// import java.io.FileReader;
// import java.util.Scanner;

class App {
    public static void main(String[] args) {

        GestorClientes gestor = new GestorClientes(); // carga JSON automáticamente

        // 1) Agrego clientes nuevos al sistema
        gestor.agregarCliente("Frank", 70);
        gestor.agregarCliente("Gina", 60);

        // 2) Engancho la cadena DESDE la raíz del JSON (Alice)
        // JSON: Alice -> Bob (ya existe)
        // JSON: Bob -> David (ya existe)
        // Agrego: David -> Frank -> Gina  => Gina queda en nivel 4 desde Alice

        Cliente david = gestor.buscarPorNombre("David");
        Cliente frank = gestor.buscarPorNombre("Frank");

        if (david == null || frank == null) {
            System.out.println("Faltan clientes para la prueba.");
            return;
        }

        // Importante: como tu árbol usa 'siguiendo' y solo toma hasta 2,
        // agregamos Frank como seguido de David
        if (!david.yaSigueA("Frank")) david.seguir("Frank");

        // Frank sigue a Gina
        if (!frank.yaSigueA("Gina")) frank.seguir("Gina");

        // 3) Probar nivel 4
        gestor.imprimirClientesNivel4();
    }

}
