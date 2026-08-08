package com.example.service.operaciones;

import java.io.IOException;

public class EliminarRestaurante {
    private final ArchivoUtil archivoUtil;

    public EliminarRestaurante(ArchivoUtil archivoUtil) {
        this.archivoUtil = archivoUtil;
    }

    public boolean ejecutar(int id) {
        try {
            long posicion = buscarPosicionPorId(id);
            if (posicion == -1) {
                return false;
            }

            //posicionarse en el byte del estado
            archivoUtil.getRaf().seek(posicion + ArchivoUtil.TAMANO_ID + ArchivoUtil.TAMANO_NOMBRE + ArchivoUtil.TAMANO_DIRECCION + ArchivoUtil.TAMANO_VALORACION);
            archivoUtil.getRaf().writeByte(0);// se marca como inactivo

            archivoUtil.getRaf().getFD().sync();
            return true;
        } catch (IOException e){
            System.err.println("Error al eliminar restaurante: " + e.getMessage());
            return false;
        }
    }

    private long buscarPosicionPorId(int id) throws IOException {
        if (archivoUtil.getRaf().length() == 0) {
            return  -1;
        }

        archivoUtil.getRaf().seek(0);
        long posicion = 0;

        while (archivoUtil.getRaf().getFilePointer() < archivoUtil.getRaf().length()) {
            posicion = archivoUtil.getRaf().getFilePointer();
            int idLeido = archivoUtil.getRaf().readInt();

            archivoUtil.getRaf().skipBytes(ArchivoUtil.TAMANO_NOMBRE +
                    ArchivoUtil.TAMANO_DIRECCION +
                    ArchivoUtil.TAMANO_VALORACION);
            byte activo = archivoUtil.getRaf().readByte();

            if (idLeido == id && activo == 1) {
                return posicion;
            }
        }
        return  -1;
    }
}
