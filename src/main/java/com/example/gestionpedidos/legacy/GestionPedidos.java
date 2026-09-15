package com.example.gestionpedidos.legacy;

import java.util.Scanner;

/**
 * ======================================================================
 *  CLASE LEGACY - NO USAR EN PRODUCCION
 * ======================================================================
 * Esta clase representa el código ORIGINAL, tal como lo dejó el colega.
 * Se conserva únicamente como referencia para el análisis del TP
 * (ver /docs/ANALISIS.md), mostrando los problemas de acoplamiento y
 * cohesión que motivaron la refactorización a capas
 * (Controller -> Service -> Repository).
 *
 * NO modificar esta clase: es el "antes" del ejercicio.
 * ======================================================================
 */
public class GestionPedidos {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1) LECTURA DE DATOS POR CONSOLA (responsabilidad de UI)
        System.out.println("=== Gestion de Pedidos (version monolitica) ===");
        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();

        System.out.print("Nombre del producto: ");
        String producto = scanner.nextLine();

        System.out.print("Precio unitario: ");
        double precio = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock disponible: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Cantidad a comprar: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        // 2) VALIDACION DE REGLAS DE NEGOCIO (mezclada con la UI)
        if (stock <= 0) {
            System.out.println("Error: no hay stock disponible.");
            return;
        }
        if (cantidad > stock) {
            System.out.println("Error: la cantidad solicitada supera el stock.");
            return;
        }

        // 3) CALCULO DE TOTAL CON IMPUESTOS (regla de negocio hardcodeada)
        double subtotal = precio * cantidad;
        double impuesto = subtotal * 0.21; // IVA 21% "quemado" en el código
        double total = subtotal + impuesto;

        // 4) GENERACION DE UNA CONSULTA SQL "A MANO" (responsabilidad de persistencia)
        String sqlInsert = "INSERT INTO pedidos (cliente, producto, cantidad, total) VALUES ('"
                + cliente + "', '" + producto + "', " + cantidad + ", " + total + ");";
        System.out.println("\n[SIMULACION DE PERSISTENCIA] Ejecutando SQL:");
        System.out.println(sqlInsert);

        // 5) IMPRESION DE LA FACTURA (nuevamente responsabilidad de UI)
        System.out.println("\n===== FACTURA =====");
        System.out.println("Cliente: " + cliente);
        System.out.println("Producto: " + producto);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("IVA (21%): $" + impuesto);
        System.out.println("TOTAL: $" + total);
        System.out.println("====================");

        scanner.close();
    }
}
