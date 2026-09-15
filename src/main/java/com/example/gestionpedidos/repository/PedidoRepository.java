package com.example.gestionpedidos.repository;

import com.example.gestionpedidos.model.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Capa de Repository.
 *
 * Responsabilidad única: simular el almacenamiento de Pedidos
 * (en este TP, en memoria, ya que no hay una base de datos real).
 * Es la única clase del sistema que "sabe" cómo se guarda un Pedido
 * y cómo se generaría la sentencia SQL correspondiente.
 *
 * No conoce ninguna capa superior (Service/Controller): solo trabaja
 * con el modelo de dominio (Pedido). Esto es lo que garantiza el bajo
 * acoplamiento: si mañana esto pasa a usar JDBC, JPA o un archivo,
 * el Service no se entera del cambio porque sigue llamando a los
 * mismos métodos públicos.
 */
public class PedidoRepository {

    private final List<Pedido> almacenamiento = new ArrayList<>();

    public Pedido guardar(Pedido pedido) {
        // Simulación de la sentencia SQL que se ejecutaría en una BD real.
        String sqlInsert = generarSqlInsert(pedido);
        System.out.println("[REPOSITORY] Ejecutando (simulado): " + sqlInsert);

        almacenamiento.add(pedido);
        return pedido;
    }

    public List<Pedido> buscarTodos() {
        return new ArrayList<>(almacenamiento);
    }

    public Optional<Pedido> buscarPorId(String id) {
        return almacenamiento.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    private String generarSqlInsert(Pedido pedido) {
        return "INSERT INTO pedidos (id, cliente, total) VALUES ('"
                + pedido.getId() + "', '" + pedido.getCliente() + "', " + pedido.getTotal() + ");";
    }
}
