package com.example.gestionpedidos.model;

/**
 * Entidad de dominio. No conoce a ninguna otra capa (ni controller,
 * ni service, ni repository). Solo modela el concepto de negocio.
 */
public class Producto {

    private final String nombre;
    private final double precioUnitario;
    private int stockDisponible;

    public Producto(String nombre, double precioUnitario, int stockDisponible) {
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stockDisponible = stockDisponible;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void descontarStock(int cantidad) {
        this.stockDisponible -= cantidad;
    }
}
