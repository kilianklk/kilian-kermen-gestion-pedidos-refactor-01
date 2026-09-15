# Gestión de Pedidos — Refactor a Arquitectura en Capas

Trabajo Práctico: identificar problemas de acoplamiento y cohesión en
un código monolítico (`GestionPedidos`) y refactorizarlo aplicando una
**Arquitectura en Capas** (`Controller -> Service -> Repository`).

## 📄 Análisis de cohesión y acoplamiento

El análisis detallado del código original y de cómo lo resuelve el
refactor está en **[`docs/ANALISIS.md`](docs/ANALISIS.md)**.

## 🗂️ Estructura del proyecto

```
src/main/java/com/example/gestionpedidos/
├── Main.java                      # Punto de entrada, arma las capas
├── legacy/
│   └── GestionPedidos.java        # Código ORIGINAL (monolítico), solo de referencia
├── model/
│   ├── Producto.java
│   ├── ItemPedido.java
│   └── Pedido.java
├── repository/
│   └── PedidoRepository.java      # Simula el almacenamiento (y el SQL)
├── service/
│   └── PedidoService.java         # Reglas de negocio: stock e impuestos
└── controller/
    └── PedidoController.java      # Interacción con el cliente (consola)
```

## 🏗️ Arquitectura

```
 Cliente (consola)
        │
        ▼
 ┌─────────────────┐      solo conoce a      ┌─────────────────┐      solo conoce a      ┌──────────────────────┐
 │  Controller     │ ───────────────────────▶ │  Service        │ ───────────────────────▶ │  Repository          │
 │ PedidoController│                           │ PedidoService   │                           │ PedidoRepository     │
 └─────────────────┘                           └─────────────────┘                           └──────────────────────┘
   flujo de interacción                          validación de stock                           simula almacenamiento
   con el usuario                                cálculo de impuestos                           y genera el SQL
```

Cada capa **solo conoce a la capa inmediatamente inferior**:
- `PedidoController` → conoce a `PedidoService` (no conoce `PedidoRepository`).
- `PedidoService` → conoce a `PedidoRepository` (no conoce `PedidoController`).
- `PedidoRepository` → solo conoce el modelo de dominio (`Pedido`).

## ▶️ Cómo ejecutarlo

Requiere JDK 11+.

```bash
# Compilar
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Ejecutar la versión refactorizada (en capas)
java -cp out com.example.gestionpedidos.Main

# Ejecutar la versión original (monolítica), solo para comparar
java -cp out com.example.gestionpedidos.legacy.GestionPedidos
```

Durante la ejecución se solicitan: nombre del cliente, nombre del
producto, precio unitario, stock disponible y cantidad a comprar. Al
finalizar se muestra la factura con subtotal, IVA (21%) y total.

## ✅ Requerimientos cubiertos

- [x] Análisis de cohesión y bajo acoplamiento del código original (`docs/ANALISIS.md`).
- [x] Paquete `repository` con `PedidoRepository` (simula almacenamiento).
- [x] Paquete `service` con `PedidoService` (impuestos y control de stock).
- [x] Paquete `controller` con `PedidoController` (flujo de interacción).
- [x] Cada capa solo conoce a la capa inmediatamente inferior.
