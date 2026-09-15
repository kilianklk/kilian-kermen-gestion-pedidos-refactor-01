package com.example.gestionpedidos.model;

/**
 * Representa una línea del pedido: un producto y la cantidad solicitada,
 * junto con el subtotal ya calculado por el Service.
 */
public class ItemPedido {

    private final Producto producto;
    private final int cantidad;
    private final double subtotal;

    public ItemPedido(Producto producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
