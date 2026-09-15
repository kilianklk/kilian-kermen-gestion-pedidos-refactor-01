package com.example.gestionpedidos;

import com.example.gestionpedidos.controller.PedidoController;
import com.example.gestionpedidos.repository.PedidoRepository;
import com.example.gestionpedidos.service.PedidoService;

import java.util.Scanner;

/**
 * Punto de entrada de la aplicación.
 *
 * Es el único lugar del proyecto donde se "cablean" las tres capas
 * entre sí (inyección de dependencias manual): se crea el Repository,
 * se lo inyecta en el Service, y el Service se inyecta en el
 * Controller. Cada capa solo recibe una referencia a la capa
 * inmediatamente inferior, nunca al revés.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        PedidoRepository pedidoRepository = new PedidoRepository();
        PedidoService pedidoService = new PedidoService(pedidoRepository);
        PedidoController pedidoController = new PedidoController(pedidoService, scanner);

        pedidoController.iniciarPedido();

        scanner.close();
    }
}
