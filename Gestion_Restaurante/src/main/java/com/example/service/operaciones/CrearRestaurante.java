package com.example.service.operaciones;

import com.example.model.Restaurante;

import java.io.IOException;

public class CrearRestaurante {

    private final ArchivoUtil archivoUtil;

    public CrearRestaurante(ArchivoUtil archivoUtil) {
        this.archivoUtil = archivoUtil;
    }

    public Restaurante ejecutar(String nombre, String direccion, double valoracion) {
        //validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw  new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (nombre.length() > ArchivoUtil.TAMANO_NOMBRE) {
            System.out.println("El nombre es muy largo, se cambiara a " + ArchivoUtil.TAMANO_NOMBRE + " caracteres");
            nombre = nombre.substring(0, ArchivoUtil.TAMANO_NOMBRE);
        }
        if (direccion != null && direccion.length() > ArchivoUtil.TAMANO_DIRECCION) {
            System.out.println("La direccion es muy larga, se cambiara a " + ArchivoUtil.TAMANO_DIRECCION + " caracteres");
            direccion = direccion.substring(0, ArchivoUtil.TAMANO_DIRECCION);
        }
        if (valoracion < 0 || valoracion> 10){
            throw new IllegalArgumentException("La valoracion debe estar entre 0 y 10");
        }

        try {
            int id = archivoUtil.getNextId();
            archivoUtil.incrementarNextId();

            Restaurante restaurante = new Restaurante(id, nombre, direccion, valoracion);

            //ir al final de archivo para escribir el siguiente restaurante
            archivoUtil.getRaf().seek(archivoUtil.getRaf().length());
            archivoUtil.getRaf().writeInt(restaurante.getId());
            archivoUtil.writeFixedString(restaurante.getNombre(), ArchivoUtil.TAMANO_NOMBRE);
            archivoUtil.writeFixedString(restaurante.getDireccion(), ArchivoUtil.TAMANO_DIRECCION);
            archivoUtil.getRaf().writeDouble(restaurante.getValoracion());
            archivoUtil.getRaf().writeByte(restaurante.isActivo() ? 1 : 0);

            archivoUtil.getRaf().getFD().sync();
            return restaurante;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al guardar el restaurante: " + e.getMessage());
        }
    }
}
