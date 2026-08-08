package com.example.service.operaciones;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class ArchivoUtil {
    public static final String FILE_NAME = "restaurantes.dat";

    // Constantes para el tamaño de los campos
    public static final int TAMANO_ID = 4;
    public static final int TAMANO_NOMBRE = 50;
    public static final int TAMANO_DIRECCION = 50;
    public static final int TAMANO_VALORACION = 8;
    public static final int TAMANO_ACTIVO = 1;

    //tamaño del registro
    public static final int TAMANO_REGISTRO = TAMANO_ID + TAMANO_NOMBRE + TAMANO_DIRECCION + TAMANO_VALORACION + TAMANO_ACTIVO; // 113 BYTES

    private RandomAccessFile raf;
    private int nextId;

    public ArchivoUtil() {
        try {
            raf = new RandomAccessFile(FILE_NAME, "rw");
            calcularNextId();
        } catch (IOException e) {
            System.err.println("Error al abrir el archivo: " + e.getMessage());
        }
    }

    public RandomAccessFile getRaf() {
        return raf;
    }

    public int getNextId() {
        return nextId;
    }

    public void incrementarNextId() {
        this.nextId++;
    }

    private void calcularNextId() throws IOException {
        int maxId = 0;
        if (raf.length() > 0) {
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();
                if (id > maxId) {
                    maxId = id;
                }
                raf.skipBytes(TAMANO_NOMBRE + TAMANO_DIRECCION + TAMANO_VALORACION + TAMANO_ACTIVO);
            }
        }
        this.nextId = maxId + 1;
    }

    public void writeFixedString(String string, int size) throws IOException {
        byte[] bytes = new byte[size];

        if (string != null && !string.trim().isEmpty()) {
            byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
            int copyLength = Math.min(stringBytes.length, size);
            System.arraycopy(stringBytes, 0 , bytes, 0 , copyLength);

            if (stringBytes.length > size) {
                System.out.println("Advertencia: El string fue cambiado a " + size + " caracteres");
            }
        }
        raf.write(bytes);
    }

    public String readFixedString(int size) throws IOException {
        byte[] bytes = new byte[size];
        raf.readFully(bytes);

        int length = 0;
        while (length < size && bytes[length] != 0) {
            length++;
        }
        return  new String(bytes, 0, length, StandardCharsets.UTF_8);
    }

    public void cerrar() {
        try {
            if (raf != null) {
                raf.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo: " + e.getMessage());
        }
    }
}
