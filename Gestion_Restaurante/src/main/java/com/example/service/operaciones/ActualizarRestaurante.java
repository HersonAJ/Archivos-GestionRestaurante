package com.example.service.operaciones;

import com.example.model.Restaurante;

import java.io.IOException;

public class ActualizarRestaurante {
    private final ArchivoUtil archivoUtil;
    private final BuscarRestaurante buscarRestaurante;

    public ActualizarRestaurante(ArchivoUtil archivoUtil, BuscarRestaurante buscarRestaurante) {
        this.archivoUtil = archivoUtil;
        this.buscarRestaurante = buscarRestaurante;
    }

    public Restaurante ejecutar(int id, String nombre, String direccion, double valoracion) {
        try {
            long posicion = buscarPosicionPorId(id);
            if (posicion == -1) {
                throw new IllegalArgumentException("Restaurante no encontrado con ID: " + id);
            }

            Restaurante existente = buscarRestaurante.ejecutar(id);
            if (existente == null) {
                throw new IllegalArgumentException("Restaurante no encontrado con ID: " + id);
            }

            //actualizar solo si se proporcionan nuevos valores
            if (nombre != null && !nombre.trim().isEmpty()) {
                if (nombre.length() > ArchivoUtil.TAMANO_NOMBRE) {
                    System.out.println("El nombre es muy largo, se cambiara a " + ArchivoUtil.TAMANO_NOMBRE + " caracteres");
                    nombre = nombre.substring(0, ArchivoUtil.TAMANO_NOMBRE);
                }
                existente.setNombre(nombre);
            }
            if (direccion != null && !direccion.trim().isEmpty()) {
                if (direccion.length() > ArchivoUtil.TAMANO_DIRECCION) {
                    System.out.println("La direccion es muy larga, se cambiara a " + ArchivoUtil.TAMANO_DIRECCION + " acaracteres");
                    direccion = direccion.substring(0, ArchivoUtil.TAMANO_DIRECCION);
                }
                existente.setDireccion(direccion);
            }
            if (valoracion >= 0 && valoracion <= 10) {
                existente.setValoracion(valoracion);
            }

            // Volver a la posición y sobrescribir
            archivoUtil.getRaf().seek(posicion);
            archivoUtil.getRaf().writeInt(existente.getId());
            archivoUtil.writeFixedString(existente.getNombre(), ArchivoUtil.TAMANO_NOMBRE);
            archivoUtil.writeFixedString(existente.getDireccion(), ArchivoUtil.TAMANO_DIRECCION);
            archivoUtil.getRaf().writeDouble(existente.getValoracion());
            archivoUtil.getRaf().writeByte(existente.isActivo() ? 1 : 0);

            archivoUtil.getRaf().getFD().sync();
            return existente;
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar el restaurante: " + e.getMessage());
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
            int idLeid = archivoUtil.getRaf().readInt();

            archivoUtil.getRaf().skipBytes(ArchivoUtil.TAMANO_NOMBRE + ArchivoUtil.TAMANO_DIRECCION + ArchivoUtil.TAMANO_VALORACION);
            byte activo = archivoUtil.getRaf().readByte();

            if (idLeid == id && activo == 1) {
                return posicion;
            }
        }
        return -1;
    }
}
