package org.example;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static GestorClientes gestor = new GestorClientes();
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)
    {
        //Carga inicial de json
        System.out.println("Iniciando aplicación - Sistema de Gestión de Clientes");
        //Mostrar menú de gestión
        boolean salir = false;
        int opcion;
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("       SISTEMA DE GESTIÓN DE CLIENTES    ");
            System.out.println("========================================");
            System.out.println("1. Alta de Cliente");
            System.out.println("2. Baja de Cliente");
            System.out.println("3. Enviar Solicitud de Seguimiento");
            System.out.println("4. Procesar Siguiente Solicitud");
            System.out.println("5. Búsqueda de Cliente (Por Nombre)");
            System.out.println("6. Búsqueda de Cliente (Por Scoring)");
            System.out.println("7. Deshacer Última Acción");
            System.out.println("8. Ver Lista de Clientes");
            System.out.println("9. Ver Historial de Acciones");
            System.out.println("10. Salir");
            System.out.println("========================================");
            try {
                System.out.print("Seleccione una opción: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Error: Por favor, ingrese un número válido.");
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
                        String origen = scanner.nextLine();
                        System.out.print("Nombre del cliente destino: ");
                        String destino = scanner.nextLine();
                        gestor.enviarSolicitudSeguimiento(origen, destino);
                        esperarRegreso();
                        break;
                    case 4:
                        gestor.procesarSolicitudSeguimiento();
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
                        salir = true;
                        System.out.println("Saliendo del sistema.");
                        break;
                    default:
                        System.out.println("Ocurrió un error: Opción no válida.");
                        esperarRegreso();
                }
            } catch (NoSuchElementException e) {
                System.out.println("Entrada finalizada inesperadamente. Saliendo.");
                break;
            }
        }
    }

    private static void esperarRegreso() {
        System.out.println("\n--> Presione ENTER para volver al menú principal...");
        scanner.nextLine();
    }


    private static void menuAlta()
    {

        System.out.println("Nombre: ");
        if (!scanner.hasNextLine()) {
            System.out.println("Entrada no disponible. Volviendo al menú.");
            return;
        }
        String nombre = scanner.nextLine();

        System.out.println("Puntaje(0-100): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Puntaje inválido. Volviendo al menú.");
            scanner.nextLine();
            return;
        }
        int puntaje = scanner.nextInt();
        scanner.nextLine();
        
        gestor.agregarCliente(nombre, puntaje);
        /*
        System.out.print("Siguiendo (separados por coma): ");
        String[] siguiendo = scanner.nextLine().split(",");
        
        System.out.print("Conexiones (separados por coma): ");
        String[] conexiones = scanner.nextLine().split(",");
         */
        //gestor.agregarCliente(nombre, score);

    }
}
