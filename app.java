import java.util.Scanner;

public class app {

    private static GestorClientes gestor = new GestorClientes();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            System.out.println("\n1. Agregar cliente");
            System.out.println("2. Eliminar cliente");
            System.out.println("3. Mostrar clientes");
            System.out.println("4. Enviar solicitud de seguimiento");
            System.out.println("5. Procesar solicitud");
            System.out.println("6. Deshacer última acción");
            System.out.println("7. Buscar cliente por nombre");
            System.out.println("8. Buscar cliente por scoring");
            System.out.println("0. Salir");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> menuAlta();

                case 2 -> {
                    System.out.print("Nombre: ");
                    gestor.eliminarCliente(scanner.nextLine());
                }

                case 3 -> gestor.imprimirLista();

                case 4 -> {
                    System.out.print("Origen: ");
                    String o = scanner.nextLine();
                    System.out.print("Destino: ");
                    String d = scanner.nextLine();
                    gestor.enviarSolicitudSeguimiento(o, d);
                }

                case 5 -> gestor.procesarSolicitudSeguimiento();

                case 6 -> gestor.deshacerUltimaAccion();

                case 7 -> {
                    System.out.print("Nombre: ");
                    Cliente c = gestor.buscarPorNombre(scanner.nextLine());
                    if (c != null) {
                        System.out.println(c);
                    } else {
                        System.out.println("Cliente no encontrado.");
                    }
                }

                case 8 -> {
                    System.out.print("Scoring: ");
                    int s = scanner.nextInt();
                    scanner.nextLine();

                    ListaDinamica<Cliente> lista = gestor.buscarPorScoring(s);
                    if (lista.getContador() == 0) {
                        System.out.println("No hay clientes con ese scoring.");
                    } else {
                        lista.imprimirLista();
                    }
                }
            }
        } while (opcion != 0);
    }

    private static void menuAlta() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Scoring: ");
        int scoring = scanner.nextInt();
        scanner.nextLine();

        gestor.agregarCliente(nombre, scoring);
    }
}
