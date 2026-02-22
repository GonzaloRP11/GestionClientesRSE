package org.example;

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
            //System.out.println("4. Procesar Siguiente Solicitud");
            System.out.println("4. Procesar Solicitudes de Seguimiento por cliente");
            System.out.println("5. Búsqueda de Cliente (Por Nombre)");
            System.out.println("6. Búsqueda de Cliente (Por Scoring)");
            System.out.println("7. Cargar e imprimir árbol por nivel");
            System.out.println("8. Deshacer Última Acción");
            System.out.println("9. Ver Lista de Clientes");
            System.out.println("10. Ver Historial de Acciones");
            System.out.println("11. Salir");
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
                        System.out.print("--> Le recordamos que puede seguir como máximo a dos clientes.\n");
                        System.out.print("--> Si desea finalizar la carga de solicitudes ingrese la palabra 'terminar'.\n");

                        System.out.print("Nombre del cliente origen: ");
                        String cliente = scanner.nextLine();
                        Cliente clienteActual = gestor.buscarPorNombre(cliente);
                        ListaEstatica<String> posiblesSeguidores = new ListaEstatica<>(2);
                        
                        while (clienteActual == null){
                            System.out.print("--> El cliente ingresado no existe.\n");
                            cliente = scanner.nextLine();
                            clienteActual = gestor.buscarPorNombre(cliente);
                        }

                        if (!clienteActual.getSolicitudes().estaVacia())
                        {
                            System.out.print("--> El cliente ingresado ya tiene solicitudes encoladas.\n");
                        }else
                        {

                            boolean seguir = true;
                            String aSeguir = null;
                            int contador = 0;
                            while (seguir && contador<2)
                            {
                                //gestor.imprimirListaSinSeguidores(posiblesSeguidores);
                                System.out.print("Nombre del cliente a seguir: ");
                                aSeguir = scanner.nextLine();
                                if (clienteActual.getSiguiendo().contiene(aSeguir)) 
                                {
                                    System.out.print("El cliente a seguir ya existe en seguidores.\n");
                                    continue;
                                }
                                if (!aSeguir.equalsIgnoreCase("terminar")) {
                                    clienteActual.aSeguir(aSeguir);
                                    posiblesSeguidores.agregar(aSeguir);
                                } else {
                                    seguir = false;
                                }
                                contador++;
                            }
                            //gestor.agregarSolicitudes(clienteActual); // Llamamos al método agregarSolicitudes con el cliente actual
                            gestor.enviarSolicitudSeguimiento(clienteActual, posiblesSeguidores);
                        }
                        esperarRegreso();
                        break;

                    case 4:
                        System.out.print("Nombre del cliente a procesar: ");
                        String cliente1 = scanner.nextLine();
                        Cliente clienteActual1 = gestor.buscarPorNombre(cliente1);
                        
                        while (clienteActual1 == null){
                            System.out.print("--> El cliente ingresado no existe.\n");
                            cliente1 = scanner.nextLine();
                            clienteActual1 = gestor.buscarPorNombre(cliente1);
                        }
                        gestor.procesarSolicitudSeguimiento(clienteActual1);
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
                        gestor.cargarClientesImprimirNivel4();
                        esperarRegreso();
                        break;
                    case 8:
                        gestor.deshacerUltimaAccion();
                        esperarRegreso();
                        break;
                    case 9:
                        gestor.imprimirLista();
                        esperarRegreso();
                        break;
                    case 10:
                        gestor.getHistorial().mostrarHistorial();
                        esperarRegreso();
                        break;
                    case 11:
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
