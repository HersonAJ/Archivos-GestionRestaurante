package com.example.service.operaciones;

import com.example.model.Restaurante;

import java.io.IOException;

public class BuscarRestaurante {

    private final ArchivoUtil archivoUtil;

    public BuscarRestaurante(ArchivoUtil archivoUtil) {
        this.archivoUtil = archivoUtil;
    }

    public Restaurante ejecutar(int id) {
        try {
            long posicion = buscarPosicionPorId(id);
            if (posicion == -1) {
                return null;
            }

            archivoUtil.getRaf().seek(posicion);
            return leerRestauranteDesdePosicion(posicion);
        } catch (IOException e) {
            System.err.println("Error al buscar restaurante: " + e.getMessage());
            return null;
        }
    }

    private long buscarPosicionPorId(int id) throws IOException {
        if (archivoUtil.getRaf().length() == 0) {
            return -1;
        }

        archivoUtil.getRaf().seek(0);
        long posicion = 0;

        while (archivoUtil.getRaf().getFilePointer() < archivoUtil.getRaf().length()) {
            posicion = archivoUtil.getRaf().getFilePointer();
            int idLeido = archivoUtil.getRaf().readInt();

            archivoUtil.getRaf().skipBytes(ArchivoUtil.TAMANO_NOMBRE + ArchivoUtil.TAMANO_DIRECCION + ArchivoUtil.TAMANO_VALORACION);
            byte activo = archivoUtil.getRaf().readByte();

            if (idLeido == id && activo == 1) {
                return posicion;
            }
        }
        return  -1;
    }

    private Restaurante leerRestauranteDesdePosicion(long posicion) throws IOException {
        archivoUtil.getRaf().seek(posicion);

        int id = archivoUtil.getRaf().readInt();
        String nombre = archivoUtil.readFixedString(ArchivoUtil.TAMANO_NOMBRE);
        String direccion = archivoUtil.readFixedString(ArchivoUtil.TAMANO_DIRECCION);
        double valoracion = archivoUtil.getRaf().readDouble();
        byte activo = archivoUtil.getRaf().readByte();

        Restaurante restaurante = new Restaurante(id, nombre, direccion, valoracion);
        restaurante.setActivo(activo == 1);
        return restaurante;
    }

    public boolean existeRestaurante(int id) {
        try {
            return buscarPosicionPorId(id) != -1;
        }catch (IOException e) {
            return false;
        }
    }
}
