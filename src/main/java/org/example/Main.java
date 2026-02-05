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
            System.out.println("1. Alta de Cliente (Registrar)");
            System.out.println("2. Baja de Cliente (Eliminar)");
            System.out.println("3. Seguir Cliente (Añadir conexión)");
            System.out.println("4. Búsqueda de Cliente (Por Nombre)");
            System.out.println("5. Ver Ranking de Clientes (Por Scoring)");
            System.out.println("6. Salir del sistema");
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
                        System.out.println("--> Iniciando proceso de Alta...");
                        menuAlta();
                        System.out.println("--> Impresión  de Alta...");
                        gestor.imprimirLista();
                        break;
                    case 2:
                        System.out.print("Nombre del cliente a dar de baja: ");
                        String nombreBaja = scanner.nextLine();
                        break;
                    case 3:
                        System.out.println("--> Gestión de Seguimientos...");
                        break;
                    case 4:
                        System.out.print("Ingrese el nombre a buscar: ");
                        String nombreBusca = scanner.nextLine();
                        break;
                    case 5:
                        System.out.println("--> Generando reporte de Scoring...");
                        // gestor.imprimirRanking();
                        break;
                    case 6:
                        salir = true;
                        System.out.println("Saliendo del sistema.");
                        break;
                    default:
                        System.out.println("Ocurrió un error: Opción no válida.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Entrada finalizada inesperadamente. Saliendo.");
                break;
            }
        }
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
