package com.example.gestionpedidos.controller;

import com.example.gestionpedidos.model.Pedido;
import com.example.gestionpedidos.model.Producto;
import com.example.gestionpedidos.service.PedidoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Capa de Controller.
 *
 * Responsabilidad única: manejar el flujo de interacción con el
 * cliente (en este TP, por consola): leer datos de entrada, invocar
 * a la capa de Service, y mostrar el resultado (la factura).
 *
 * Solo conoce a la capa inmediatamente inferior (Service). No conoce
 * el Repository ni cómo se calculan impuestos o se valida stock:
 * eso es responsabilidad exclusiva del Service.
 */
public class PedidoController {

    private final PedidoService pedidoService;
    private final Scanner scanner;

    public PedidoController(PedidoService pedidoService, Scanner scanner) {
        this.pedidoService = pedidoService;
        this.scanner = scanner;
    }

    public void iniciarPedido() {
        System.out.println("=== Gestion de Pedidos (version en capas) ===");

        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();

        System.out.print("Nombre del producto: ");
        String nombreProducto = scanner.nextLine();

        System.out.print("Precio unitario: ");
        double precio = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock disponible: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Cantidad a comprar: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        Producto producto = new Producto(nombreProducto, precio, stock);

        List<Producto> productos = new ArrayList<>();
        List<Integer> cantidades = new ArrayList<>();
        productos.add(producto);
        cantidades.add(cantidad);

        try {
            Pedido pedido = pedidoService.crearPedido(cliente, productos, cantidades);
            mostrarFactura(pedido);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("\nNo se pudo generar el pedido: " + e.getMessage());
        }
    }

    private void mostrarFactura(Pedido pedido) {
        System.out.println("\n===== FACTURA =====");
        System.out.println("Pedido ID: " + pedido.getId());
        System.out.println("Cliente: " + pedido.getCliente());
        pedido.getItems().forEach(item -> System.out.printf(Locale.US,
                "- %s x%d = $%.2f%n",
                item.getProducto().getNombre(), item.getCantidad(), item.getSubtotal()));
        System.out.printf(Locale.US, "Subtotal: $%.2f%n", pedido.getSubtotal());
        System.out.printf(Locale.US, "IVA (21%%): $%.2f%n", pedido.getImpuesto());
        System.out.printf(Locale.US, "TOTAL: $%.2f%n", pedido.getTotal());
        System.out.println("====================");
    }
}
