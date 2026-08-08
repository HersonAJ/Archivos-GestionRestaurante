package com.example.model;

import java.io.Serial;
import java.io.Serializable;

public class Restaurante implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String direccion;
    private double valoracion;
    private boolean activo; // Para marcar registros eliminados

    public Restaurante() {
        this.activo = true;
    }

    public Restaurante(int id, String nombre, String direccion, double valoracion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.valoracion = valoracion;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nombre: " + nombre +
                " | Dirección: " + direccion +
                " | Valoración: " + String.format("%.1f", valoracion) +
                (activo ? "" : " (ELIMINADO)");
    }
}