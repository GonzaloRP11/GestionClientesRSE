package org.example;





class App {
    public static void main(String[] args) {

        GestorClientes gestor = new GestorClientes(); 

        
        gestor.agregarCliente("Frank", 70);
        gestor.agregarCliente("Gina", 60);

        
        
        
        

        Cliente david = gestor.buscarPorNombre("David");
        Cliente frank = gestor.buscarPorNombre("Frank");

        if (david == null || frank == null) {
            System.out.println("Faltan clientes para la prueba.");
            return;
        }

        
        
        if (!david.yaSigueA("Frank")) david.seguir("Frank");

        
        if (!frank.yaSigueA("Gina")) frank.seguir("Gina");

        
        gestor.visualizarArbolSeguimiento();
    }

}

