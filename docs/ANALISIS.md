# Análisis: Cohesión y Acoplamiento en `GestionPedidos` (código original)

## Código analizado

`src/main/java/com/example/gestionpedidos/legacy/GestionPedidos.java`

Esta clase se conserva intacta en el paquete `legacy` únicamente como
referencia del "antes", para poder comparar contra la solución
refactorizada.

## 1. Problemas de cohesión (baja cohesión)

La cohesión mide qué tan enfocada está una clase/método en **una sola
responsabilidad**. El método `main()` de `GestionPedidos` hace, como
mínimo, **cinco cosas distintas** que no tienen relación directa entre
sí:

| # | Responsabilidad detectada en el código | Debería vivir en... |
|---|---|---|
| 1 | Leer datos por consola (`Scanner`, `System.out.print`) | Controller |
| 2 | Validar reglas de negocio (`stock <= 0`, `cantidad > stock`) | Service |
| 3 | Calcular el total con impuestos (IVA 21% hardcodeado) | Service |
| 4 | Generar una sentencia SQL manual (`INSERT INTO pedidos...`) | Repository |
| 5 | Mostrar la factura por pantalla | Controller |

Al mezclar estas cinco responsabilidades en un único método, la clase
tiene **baja cohesión**: no puede describirse con una sola frase
("¿qué hace esta clase?" → "todo"). Cualquier cambio menor —por
ejemplo, cambiar el IVA del 21% al 10.5%, o cambiar el motor de base
de datos— obliga a tocar el mismo método gigante donde también vive
la lógica de consola.

Consecuencias concretas de esto en el código original:
- Es imposible testear la validación de stock o el cálculo de
  impuestos sin ejecutar todo el flujo de consola (`Scanner`
  bloqueando en `System.in`).
- El `main()` no se puede reutilizar: si mañana se necesita el mismo
  cálculo desde una API REST, hay que copiar y pegar código.

## 2. Problemas de acoplamiento (alto acoplamiento)

El acoplamiento mide qué tan dependiente es una parte del código de
los detalles internos de otra. En el código original:

- La **lógica de negocio** (stock, impuestos) está **acoplada
  directamente a la entrada/salida por consola**: las validaciones
  usan variables (`stock`, `cantidad`) que vienen crudas de
  `Scanner.nextLine()`, sin ninguna capa intermedia.
- La **lógica de negocio** está **acoplada al detalle de persistencia**:
  el cálculo del total se concatena directamente dentro del `String`
  de la sentencia SQL (`"... " + total + ");"`). Si cambia la forma de
  persistir (por ejemplo, pasar de SQL manual a un ORM), hay que tocar
  el mismo bloque de código que calcula el negocio.
- No existen **interfaces ni capas** que actúen como límite: todo es
  una secuencia de instrucciones en un único método `static void
  main`, por lo que cualquier cambio en un "paso" puede romper,
  sin avisar, a los pasos siguientes.
- La generación manual de SQL mediante concatenación de strings
  (`"INSERT INTO pedidos (...) VALUES ('" + cliente + "', ..."`)
  además de ser una mala práctica de seguridad (inyección SQL), es un
  síntoma de acoplamiento: la capa de presentación/negocio "sabe"
  cómo es la tabla `pedidos` en la base de datos.

## 3. Cómo lo resuelve la Arquitectura en Capas

| Capa | Paquete | Responsabilidad única | Solo conoce a |
|---|---|---|---|
| **Controller** | `controller` | Leer entrada del usuario y mostrar la factura | `service` |
| **Service** | `service` | Validar stock y calcular impuestos/total | `repository` |
| **Repository** | `repository` | Simular el almacenamiento (y la sentencia SQL) | `model` |
| **Model** | `model` | Representar las entidades del dominio (`Producto`, `Pedido`, `ItemPedido`) | — |

Regla aplicada en todo el proyecto: **cada capa solo conoce a la capa
inmediatamente inferior**.

- `PedidoController` recibe un `PedidoService` por constructor y
  **nunca** importa nada de `repository`.
- `PedidoService` recibe un `PedidoRepository` por constructor y
  **nunca** importa nada de `controller` (no sabe que existe
  `Scanner`, ni consola, ni nada de UI).
- `PedidoRepository` solo conoce el `model` (la clase `Pedido`), no
  sabe nada de reglas de negocio ni de la interacción con el usuario.
- El cableado de las tres capas (quién instancia a quién) se hace en
  un único lugar: `Main.java`, evitando que las capas se acoplen
  entre sí "buscándose" unas a otras.

### Beneficios concretos obtenidos

1. **Cohesión alta**: cada clase se puede describir en una frase
   (`PedidoService` = "aplica las reglas de negocio de un pedido").
2. **Bajo acoplamiento**: se puede reemplazar `PedidoRepository` por
   una implementación con JDBC/JPA sin tocar `PedidoService` ni
   `PedidoController`, porque la comunicación entre capas se hace
   por métodos públicos, no por detalles internos compartidos.
3. **Testeable**: `PedidoService` se puede probar con JUnit inyectando
   un `PedidoRepository` de prueba, sin necesidad de `Scanner` ni
   `System.in`.
4. **Reusable**: si mañana se agrega una API REST, se puede escribir
   un nuevo `PedidoRestController` que reutilice el mismo
   `PedidoService` sin duplicar ninguna regla de negocio.
