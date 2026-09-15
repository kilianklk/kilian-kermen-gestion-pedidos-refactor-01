package com.example.gestionpedidos.service;

import com.example.gestionpedidos.model.ItemPedido;
import com.example.gestionpedidos.model.Pedido;
import com.example.gestionpedidos.model.Producto;
import com.example.gestionpedidos.repository.PedidoRepository;

import java.util.List;

/**
 * Capa de Service.
 *
 * Responsabilidad única: reglas de negocio del dominio "Pedidos":
 *  - Validar que haya stock suficiente.
 *  - Calcular subtotal, impuesto (IVA) y total.
 *
 * Solo conoce a la capa inmediatamente inferior (Repository) para
 * persistir el resultado. NO conoce al Controller, ni sabe nada de
 * consola, HTTP, o cualquier otro detalle de interacción con el
 * usuario. Esto es lo que permite reusar este Service desde una app
 * de consola, una API REST o una UI web sin cambiar una línea acá.
 */
public class PedidoService {

    private static final double PORCENTAJE_IVA = 0.21;

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Crea un pedido a partir de un cliente y una lista de productos
     * con sus cantidades, validando stock y calculando totales.
     */
    public Pedido crearPedido(String cliente, List<Producto> productos, List<Integer> cantidades) {
        if (productos.size() != cantidades.size()) {
            throw new IllegalArgumentException("La cantidad de productos y cantidades no coincide.");
        }

        Pedido pedido = new Pedido(cliente);
        double subtotalPedido = 0.0;

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            int cantidad = cantidades.get(i);

            validarStock(producto, cantidad);

            double subtotalItem = producto.getPrecioUnitario() * cantidad;
            pedido.agregarItem(new ItemPedido(producto, cantidad, subtotalItem));
            producto.descontarStock(cantidad);

            subtotalPedido += subtotalItem;
        }

        double impuesto = calcularImpuesto(subtotalPedido);
        double total = subtotalPedido + impuesto;

        pedido.setSubtotal(subtotalPedido);
        pedido.setImpuesto(impuesto);
        pedido.setTotal(total);

        return pedidoRepository.guardar(pedido);
    }

    private void validarStock(Producto producto, int cantidadSolicitada) {
        if (producto.getStockDisponible() <= 0) {
            throw new IllegalStateException(
                    "No hay stock disponible para el producto: " + producto.getNombre());
        }
        if (cantidadSolicitada > producto.getStockDisponible()) {
            throw new IllegalStateException(
                    "La cantidad solicitada (" + cantidadSolicitada + ") supera el stock disponible ("
                            + producto.getStockDisponible() + ") para: " + producto.getNombre());
        }
    }

    private double calcularImpuesto(double subtotal) {
        return subtotal * PORCENTAJE_IVA;
    }
}
