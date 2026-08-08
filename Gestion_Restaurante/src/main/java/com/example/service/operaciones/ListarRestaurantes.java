package com.example.service.operaciones;

import com.example.model.Restaurante;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListarRestaurantes {
    private final ArchivoUtil archivoUtil;

    public ListarRestaurantes(ArchivoUtil archivoUtil) {
        this.archivoUtil = archivoUtil;
    }

    public List<Restaurante> ejecutar() {
        List<Restaurante> restaurantes = new ArrayList<>();

        try {
            if (archivoUtil.getRaf().length() == 0) {
                return restaurantes;
            }

            archivoUtil.getRaf().seek(0);
            while (archivoUtil.getRaf().getFilePointer() < archivoUtil.getRaf().length()) {
                long posicion = archivoUtil.getRaf().getFilePointer();
                Restaurante r = leerRestauranteDesdePosicion(posicion);
                if (r != null && r.isActivo()) {
                    restaurantes.add(r);
                }
            }
            return restaurantes;
        } catch (IOException e) {
            System.err.println("Error al listar restaurantes: " + e.getMessage());
            return restaurantes;
        }
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

    public long getTotalRegistros() throws IOException {
        if (archivoUtil.getRaf() == null || archivoUtil.getRaf().length() == 0) {
            return 0;
        }
        return archivoUtil.getRaf().length() / ArchivoUtil.TAMANO_REGISTRO;
    }
}
