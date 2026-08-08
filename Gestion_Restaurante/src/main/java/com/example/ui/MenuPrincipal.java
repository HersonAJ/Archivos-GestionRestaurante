package com.example.ui;

import com.example.model.Restaurante;
import com.example.service.RestauranteService;

import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private final RestauranteService service;
    private final Scanner scanner;
    private boolean ejecutando;

    public MenuPrincipal() {
        this.service = new RestauranteService();
        this.scanner = new Scanner(System.in);
        this.ejecutando = true;
    }

    public void iniciar() {
        try {
            mostrarBienvenida();
            mostrarDebugInicial();

            while (ejecutando) {
                mostrarMenu();
                int opcion = leerOpcion();
                procesarOpcion(opcion);
            }
        } catch (Exception e) {
            System.err.println("Error en la aplicacion: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
    }

    private void mostrarBienvenida() {
        System.out.println("=== SISTEMA DE ADMINISTRACION DE RESTAURANTES ===\n");
        System.out.println("Usando RandomAccessFile para almacenamiento");
        System.out.println("Tamaño del registro: 113 bytes\n");
    }

    private void mostrarDebugInicial() {
        service.debugMostrarArchivo();
    }

    private void mostrarMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Crear restaurante");
        System.out.println("2. Actualizar restaurante");
        System.out.println("3. Eliminar restaurante");
        System.out.println("4. Buscar restaurante por ID");
        System.out.println("5. Listar todos los restaurantes");
        System.out.println("6. Salir");
        System.out.println("7. [DEBUG] Mostrar contenido del archivo");
        System.out.print("\nSeleccione una opcion: ");
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                crearRestaurante();
                break;
            case 2:
                actualizarRestaurante();
                break;
            case 3:
                eliminarRestaurante();
                break;
            case 4:
                buscarRestaurante();
                break;
            case 5:
                listarRestaurantes();
                break;
            case 6:
                salir();
                break;
            case 7:
                service.debugMostrarArchivo();
                break;
            default:
                System.out.println("\nOpcion no valida. Por favor, intente de nuevo.");
        }
    }

    private void crearRestaurante() {
        System.out.println("\n=== CREAR NUEVO RESTAURANTE ===");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        if (nombre.length() > 50) {
            System.out.println("El nombre es muy largo, se cambiara a 50 caracteres.");
            nombre = nombre.substring(0, 50);
        }

        System.out.print("Direccion: ");
        String direccion = scanner.nextLine();

        if (direccion.length() > 50) {
            System.out.println("La direccion es muy larga, se cambiara a 50 caracteres.");
            direccion = direccion.substring(0, 50);
        }

        double valoracion = leerValoracion();

        try {
            Restaurante restaurante = service.crearRestaurante(nombre, direccion, valoracion);
            System.out.println("\nRestaurante creado exitosamente:");
            System.out.println(restaurante);
            System.out.println("ID asignado: " + restaurante.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private double leerValoracion() {
        double valoracion = 0;
        boolean valoracionValida = false;

        while (!valoracionValida) {
            try {
                System.out.print("Valoracion (0-10): ");
                valoracion = Double.parseDouble(scanner.nextLine());
                if (valoracion >= 0 && valoracion <= 10) {
                    valoracionValida = true;
                } else {
                    System.out.println("La valoracion debe estar entre 0 y 10");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un numero valido");
            }
        }
        return valoracion;
    }

    private void actualizarRestaurante() {
        System.out.println("\n=== ACTUALIZAR RESTAURANTE ===");

        List<Restaurante> todos = service.listarTodos();
        if (todos.isEmpty()) {
            System.out.println("No hay restaurantes registrados.");
            return;
        }

        int id = leerId("ID del restaurante a actualizar");

        if (!service.existeRestaurante(id)) {
            System.out.println("No existe un restaurante con ID: " + id);
            return;
        }

        Restaurante actual = service.buscarPorId(id);
        System.out.println("Datos actuales: " + actual);
        System.out.println("\nIngrese los nuevos datos (deje en blanco para mantener el valor actual):");

        String nombre = leerTextoConDefault("Nuevo nombre", actual.getNombre());
        String direccion = leerTextoConDefault("Nueva direccion", actual.getDireccion());
        double valoracion = leerValoracionConDefault(actual.getValoracion());

        try {
            Restaurante actualizado = service.actualizarRestaurante(id, nombre, direccion, valoracion);
            System.out.println("\nRestaurante actualizado exitosamente:");
            System.out.println(actualizado);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private String leerTextoConDefault(String mensaje, String valorDefault) {
        System.out.print(mensaje + " [" + valorDefault + "]: ");
        String input = scanner.nextLine();
        if (input.trim().isEmpty()) {
            return valorDefault;
        }
        if (input.length() > 50) {
            System.out.println("El texto es muy largo, se cortara a 50 caracteres.");
            return input.substring(0, 50);
        }
        return input;
    }

    private double leerValoracionConDefault(double valorDefault) {
        System.out.print("Nueva valoracion (0-10) [" + valorDefault + "]: ");
        String input = scanner.nextLine();
        if (input.trim().isEmpty()) {
            return valorDefault;
        }
        try {
            double valor = Double.parseDouble(input);
            if (valor >= 0 && valor <= 10) {
                return valor;
            } else {
                System.out.println("La valoracion debe estar entre 0 y 10. Manteniendo valor anterior.");
                return valorDefault;
            }
        } catch (NumberFormatException e) {
            System.out.println("Valoracion no valida. Manteniendo valor anterior.");
            return valorDefault;
        }
    }

    private void eliminarRestaurante() {
        System.out.println("\n=== ELIMINAR RESTAURANTE ===");

        List<Restaurante> todos = service.listarTodos();
        if (todos.isEmpty()) {
            System.out.println("No hay restaurantes registrados.");
            return;
        }

        int id = leerId("ID del restaurante a eliminar");

        if (!service.existeRestaurante(id)) {
            System.out.println("No existe un restaurante con ID: " + id);
            return;
        }

        Restaurante aEliminar = service.buscarPorId(id);
        System.out.println("Restaurante a eliminar:");
        System.out.println(aEliminar);

        System.out.print("¿Esta seguro de eliminar este restaurante? (S/N): ");
        String confirmacion = scanner.nextLine().toUpperCase();

        if (confirmacion.equals("S")) {
            boolean eliminado = service.eliminarRestaurante(id);
            if (eliminado) {
                System.out.println("Restaurante eliminado exitosamente (marcado como inactivo)");
            } else {
                System.out.println("Error al eliminar el restaurante");
            }
        } else {
            System.out.println("Operacion cancelada");
        }
    }

    private void buscarRestaurante() {
        System.out.println("\n=== BUSCAR RESTAURANTE POR ID ===");

        int id = leerId("ID del restaurante a buscar");

        Restaurante restaurante = service.buscarPorId(id);
        if (restaurante != null && restaurante.isActivo()) {
            System.out.println("\nRestaurante encontrado:");
            System.out.println(restaurante);
        } else {
            System.out.println("\nNo se encontro un restaurante activo con ID: " + id);
        }
    }

    private void listarRestaurantes() {
        System.out.println("\n=== LISTA DE RESTAURANTES ===");

        List<Restaurante> restaurantes = service.listarTodos();
        if (restaurantes.isEmpty()) {
            System.out.println("No hay restaurantes activos registrados.");
            return;
        }

        System.out.println("Total de restaurantes activos: " + restaurantes.size());
        System.out.println("----------------------------------------");
        for (Restaurante r : restaurantes) {
            System.out.println(r);
        }
        System.out.println("----------------------------------------");

        mostrarInfoArchivo();
    }

    private void mostrarInfoArchivo() {
        try {
            System.out.println("\nInformacion del archivo:");
            System.out.println("  - Archivo: restaurantes.dat");
            System.out.println("  - Tamaño del registro: 113 bytes");
            System.out.println("  - Total de registros (incluyendo eliminados): " +
                    service.getTotalRegistros());
        } catch (Exception e) {
            // Ignorar errores al mostrar info del archivo
        }
    }

    private int leerId(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Por favor, ingrese un nmero valido: ");
            }
        }
    }

    private void salir() {
        System.out.println("\n¡Gracias por usar el sistema!");
        ejecutando = false;
    }

    private void cerrarRecursos() {
        if (service != null) {
            service.close();
        }
        if (scanner != null) {
            scanner.close();
        }
    }
}