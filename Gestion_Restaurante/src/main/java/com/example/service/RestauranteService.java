package com.example.service;

import com.example.model.Restaurante;
import com.example.service.operaciones.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RestauranteService {
    private final ArchivoUtil archivoUtil;
    private final CrearRestaurante crearRestaurante;
    private final BuscarRestaurante buscarRestaurante;
    private final ActualizarRestaurante actualizarRestaurante;
    private final EliminarRestaurante eliminarRestaurante;
    private final ListarRestaurantes listarRestaurantes;

    public RestauranteService() {
        this.archivoUtil = new ArchivoUtil();
        this.crearRestaurante = new CrearRestaurante(archivoUtil);
        this.buscarRestaurante = new BuscarRestaurante(archivoUtil);
        this.actualizarRestaurante = new ActualizarRestaurante(archivoUtil, buscarRestaurante);
        this.eliminarRestaurante = new EliminarRestaurante(archivoUtil);
        this.listarRestaurantes = new ListarRestaurantes(archivoUtil);
    }

    public Restaurante crearRestaurante(String nombre, String direccion, double valoracion) {
        return crearRestaurante.ejecutar(nombre, direccion, valoracion);
    }

    public Restaurante buscarPorId(int id) {
        return buscarRestaurante.ejecutar(id);
    }

    public Restaurante actualizarRestaurante(int id, String nombre, String direccion, double valoracion){
        return  actualizarRestaurante.ejecutar(id, nombre, direccion, valoracion);
    }

    public boolean eliminarRestaurante(int id) {
        return  eliminarRestaurante.ejecutar(id);
    }

    public List<Restaurante> listarTodos() {
        return  listarRestaurantes.ejecutar();
    }

    public boolean existeRestaurante(int id){
        return buscarRestaurante.existeRestaurante(id);
    }

    public long getTotalRegistros() {
        try {
            return listarRestaurantes.getTotalRegistros();
        } catch (Exception e) {
            return 0;
        }
    }

    public void debugMostrarArchivo() {
        try {
            System.out.println("\n=== DEBUG: CONTENIDO DEL ARCHIVO ===");
            System.out.println("Tamaño del archivo: " + archivoUtil.getRaf().length() + " bytes");
            System.out.println("Tamaño del registro: " + ArchivoUtil.TAMANO_REGISTRO + " bytes");
            System.out.println("Total registros posibles: " +
                    (archivoUtil.getRaf().length() / ArchivoUtil.TAMANO_REGISTRO));
            System.out.println("----------------------------------------");

            if (archivoUtil.getRaf().length() == 0) {
                System.out.println("El archivo está vacío");
                return;
            }

            archivoUtil.getRaf().seek(0);
            int contador = 0;
            while (archivoUtil.getRaf().getFilePointer() < archivoUtil.getRaf().length()) {
                long pos = archivoUtil.getRaf().getFilePointer();
                System.out.println("\nRegistro #" + (++contador) + " (posición: " + pos + ")");

                int id = archivoUtil.getRaf().readInt();
                String nombre = archivoUtil.readFixedString(ArchivoUtil.TAMANO_NOMBRE);
                String direccion = archivoUtil.readFixedString(ArchivoUtil.TAMANO_DIRECCION);
                double valoracion = archivoUtil.getRaf().readDouble();
                byte activo = archivoUtil.getRaf().readByte();

                System.out.println("  ID: " + id);
                System.out.println("  Nombre: '" + nombre + "'");
                System.out.println("  Dirección: '" + direccion + "'");
                System.out.println("  Valoración: " + valoracion);
                System.out.println("  Activo: " + (activo == 1 ? "Sí" : "No"));
            }
            System.out.println("----------------------------------------");
        } catch (Exception e) {
            System.err.println("Error en debug: " + e.getMessage());
        }
    }

    public void close() {
        archivoUtil.cerrar();
    }
}