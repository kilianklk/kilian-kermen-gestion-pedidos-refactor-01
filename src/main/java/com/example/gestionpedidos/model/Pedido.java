package com.example.gestionpedidos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Agregado de dominio "Pedido". Contiene sus items y los totales ya
 * calculados por la capa de servicio (el modelo no calcula nada por
 * sí mismo, solo transporta el estado).
 */
public class Pedido {

    private final String id;
    private final String cliente;
    private final List<ItemPedido> items = new ArrayList<>();
    private double subtotal;
    private double impuesto;
    private double total;

    public Pedido(String cliente) {
        this.id = UUID.randomUUID().toString();
        this.cliente = cliente;
    }

    public String getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void agregarItem(ItemPedido item) {
        items.add(item);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
