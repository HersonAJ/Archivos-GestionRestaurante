package com.example.util;

import com.example.model.Restaurante;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    /*
    private static final String FILE_PATH = "data/restaurante.txt";

    public void guardarRestaurantes(List<Restaurante> restaurantes) {
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdir();
            }

            try (BufferedWriter write = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (Restaurante r : restaurantes) {
                    write.write(r.toFileFormat());
                    write.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los restaurantes: " + e.getMessage());
        }
    }

    public List<Restaurante> cargarRestaurantes() {
        List<Restaurante> restaurantes = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return restaurantes;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Restaurante r = Restaurante.fromFileFormat(line);
                    if (r != null) {
                        restaurantes.add(r);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los restaurantes: " + e.getMessage());
        }
        return restaurantes;
    }
    */

}
