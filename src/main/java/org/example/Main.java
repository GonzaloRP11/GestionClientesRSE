package org.example;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static GestorClientes gestor = new GestorClientes();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Iniciando aplicación - Sistema de Gestión de Clientes");

        boolean salir = false;
        int opcion;

        while (!salir) {

            System.out.println("\n========================================");
            System.out.println("       SISTEMA DE GESTIÓN DE CLIENTES    ");
            System.out.println("========================================");
            System.out.println("1. Alta de Cliente");
            System.out.println("2. Baja de Cliente");
            System.out.println("3. Enviar Solicitud de Seguimiento");
            System.out.println("4. Procesar Solicitudes de Seguimiento por cliente");
            System.out.println("5. Búsqueda de Cliente (Por Nombre)");
            System.out.println("6. Búsqueda de Cliente (Por Scoring)");
            System.out.println("7. Deshacer Última Acción");
            System.out.println("8. Ver Lista de Clientes");
            System.out.println("9. Ver Historial de Acciones");
            System.out.println("10. Visualizar Árbol de Seguimiento Completo");
            System.out.println("11. Ver a quién sigue un cliente");
            System.out.println("12. Cargar Caso de Prueba (Nivel 4)");
            System.out.println("13. Visualizar Árbol por Scoring (AVL)");
            System.out.println("14. Agregar relación");
            System.out.println("15. Imprimir grafo");
            System.out.println("16. Obtener vecinos de un cliente");
            System.out.println("17. Calcular distancia entre dos clientes");
            System.out.println("18. Salir");
            System.out.println("========================================");

            try {

                System.out.print("Seleccione una opción: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Error: Ingrese un número válido.");
                    scanner.nextLine();
                    continue;
                }

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {

                    case 1:
                        menuAlta();
                        esperarRegreso();
                        break;

                    case 2:
                        System.out.print("Nombre del cliente a dar de baja: ");
                        String nombreBaja = scanner.nextLine();
                        gestor.eliminarCliente(nombreBaja);
                        esperarRegreso();
                        break;

                    case 3:
                        System.out.print("Nombre del cliente origen: ");
                        String cliente = scanner.nextLine();

                        Cliente clienteActual = gestor.buscarPorNombre(cliente);

                        while (clienteActual == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            cliente = scanner.nextLine();
                            clienteActual = gestor.buscarPorNombre(cliente);
                        }

                        if (!clienteActual.getSolicitudes().estaVacia()) {
                            System.out.println("El cliente ya tiene solicitudes pendientes.");
                        } else {

                            ListaEstatica<String> posiblesSeguidores = new ListaEstatica<>(2);
                            int contador = 0;

                            while (contador < 2) {

                                System.out.print("Nombre del cliente a seguir (o 'terminar'): ");
                                String aSeguir = scanner.nextLine();

                                if (aSeguir.equalsIgnoreCase("terminar")) {
                                    break;
                                }


                                posiblesSeguidores.agregar(aSeguir);
                                contador++;
                            }

                            gestor.enviarSolicitudSeguimiento(clienteActual, posiblesSeguidores);
                        }

                        esperarRegreso();
                        break;

                    case 4:
                        System.out.print("Nombre del cliente a procesar: ");
                        String nombreProcesar = scanner.nextLine();

                        Cliente clienteProcesar = gestor.buscarPorNombre(nombreProcesar);

                        while (clienteProcesar == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            nombreProcesar = scanner.nextLine();
                            clienteProcesar = gestor.buscarPorNombre(nombreProcesar);
                        }

                        gestor.procesarSolicitudSeguimiento(clienteProcesar);
                        esperarRegreso();
                        break;

                    case 5:
                        System.out.print("Ingrese el nombre a buscar: ");
                        String nombreBusca = scanner.nextLine();
                        Cliente encontradoNom = gestor.buscarPorNombre(nombreBusca);

                        if (encontradoNom != null) {
                            System.out.println(encontradoNom);
                        } else {
                            System.out.println("Cliente no encontrado.");
                        }

                        esperarRegreso();
                        break;

                    case 6:
                        System.out.print("Ingrese el scoring a buscar: ");

                        if (scanner.hasNextInt()) {
                            int scoringBusca = scanner.nextInt();
                            scanner.nextLine();

                            ListaDinamica<Cliente> encontradosSc = gestor.buscarPorScoring(scoringBusca);

                            if (encontradosSc.getContador() > 0) {
                                encontradosSc.imprimirLista();
                            } else {
                                System.out.println("No hay clientes con ese scoring.");
                            }
                        } else {
                            System.out.println("Scoring inválido.");
                            scanner.nextLine();
                        }

                        esperarRegreso();
                        break;

                    case 7:
                        gestor.deshacerUltimaAccion();
                        esperarRegreso();
                        break;

                    case 8:
                        gestor.imprimirLista();
                        esperarRegreso();
                        break;

                    case 9:
                        gestor.getHistorial().mostrarHistorial();
                        esperarRegreso();
                        break;

                    case 10:
                        gestor.visualizarArbolSeguimiento();
                        esperarRegreso();
                        break;

                    case 11:
                        System.out.print("Ingrese el nombre del cliente: ");
                        String nombreConsulta = scanner.nextLine();
                        gestor.imprimirSiguiendoDeCliente(nombreConsulta);
                        esperarRegreso();
                        break;
                         case 12:
                        gestor.cargarCasoPruebaNivel4();
                        esperarRegreso();
                        break;

                    case 13:
                        gestor.visualizarArbolScoring();
                        esperarRegreso();
                        break;

                    case 14:
                        System.out.print("Nombre del cliente A: ");
                        String nombreProcesarA = scanner.nextLine();
                        Cliente clienteProcesarA = gestor.buscarPorNombre(nombreProcesarA);

                        while (clienteProcesarA == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            nombreProcesarA= scanner.nextLine();
                            clienteProcesarA = gestor.buscarPorNombre(nombreProcesarA);
                        }

                        System.out.print("Nombre del cliente B: ");
                        String nombreProcesarB = scanner.nextLine();
                        Cliente clienteProcesarB = gestor.buscarPorNombre(nombreProcesarB);

                        while (clienteProcesarB == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            nombreProcesarB= scanner.nextLine();
                            clienteProcesarB = gestor.buscarPorNombre(nombreProcesarB);
                        }

                        gestor.agregarRelacionClientes(nombreProcesarA, nombreProcesarB);
                        esperarRegreso();
                        break;
                    case 15:
                        gestor.imprimirGrafo();
                        esperarRegreso();
                        break;
                    case 16:
                        System.out.print("Nombre del cliente a visualizar vecinos: ");
                        String nombreProcesarV = scanner.nextLine();
                        Cliente clienteProcesarV = gestor.buscarPorNombre(nombreProcesarV);

                        while (clienteProcesarV == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            nombreProcesarV= scanner.nextLine();
                            clienteProcesarV = gestor.buscarPorNombre(nombreProcesarV);
                        }
                        gestor.imprimirVecinos(nombreProcesarV);
                        esperarRegreso();
                        break;
                    case 17:
                        System.out.print("Nombre del cliente A: ");
                        String nombreProcesarDistaciaA = scanner.nextLine();
                        Cliente clienteProcesarDistanciaA = gestor.buscarPorNombre(nombreProcesarDistaciaA);

                        while (clienteProcesarDistanciaA == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            nombreProcesarDistaciaA= scanner.nextLine();
                            clienteProcesarDistanciaA = gestor.buscarPorNombre(nombreProcesarDistaciaA);
                        }

                        System.out.print("Nombre del cliente B: ");
                        String nombreProcesarDistanciaB = scanner.nextLine();
                        Cliente clienteProcesarDistanciaB = gestor.buscarPorNombre(nombreProcesarDistanciaB);

                        while (clienteProcesarDistanciaB == null) {
                            System.out.println("Cliente no existe. Ingrese nuevamente:");
                            nombreProcesarDistanciaB= scanner.nextLine();
                            clienteProcesarDistanciaB = gestor.buscarPorNombre(nombreProcesarDistanciaB);
                        }

                        gestor.imprimirDistancia(nombreProcesarDistaciaA, nombreProcesarDistanciaB);
                        esperarRegreso();
                        break;
                    case 18:
                        salir = true;
                        System.out.println("Saliendo del sistema.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        esperarRegreso();
                }

            } catch (NoSuchElementException e) {
                System.out.println("Entrada finalizada inesperadamente.");
                break;
            }
        }
    }

    private static void esperarRegreso() {
        System.out.println("\n--> Presione ENTER para volver al menú principal...");
        scanner.nextLine();
    }

    private static void menuAlta() {

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Puntaje (0-100): ");

        if (!scanner.hasNextInt()) {
            System.out.println("Puntaje inválido.");
            scanner.nextLine();
            return;
        }

        int puntaje = scanner.nextInt();
        scanner.nextLine();

        gestor.agregarCliente(nombre, puntaje);
    }
}
