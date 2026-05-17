# Neon Breaker

Minijuego arcade 2D estilo **Breakout / Arkanoid** desarrollado en **Java Swing** con persistencia de datos en **MySQL**.



---

## Descripción

El jugador controla una paleta en la parte inferior de la pantalla para rebotar una pelota y destruir los ladrillos de la parte superior. El objetivo es limpiar la pantalla acumulando la mayor puntuación posible.

**Características principales:**
- Pantalla de inicio con nickname personalizado
- Física de rebote y detección de colisiones
- Sistema de puntuación con **combos** (ladrillos seguidos en un mismo rebote valen más)
- 3 vidas por partida
- Guardado automático de puntuaciones en MySQL
- **Leaderboard TOP 10** consultando la base de datos

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Java Swing | Interfaz gráfica y bucle de juego |
| MySQL | Base de datos para usuarios y puntuaciones |
| MySQL Connector/J | Driver JDBC para conectar Java con MySQL |

---

## Estructura del proyecto

```
NeonBreaker/
├── src/
│   └── neonbreaker/
│       ├── Main.java                          ← Punto de entrada
│       ├── controlador/
│       │   └── ControladorJuego.java          ← Lógica del juego y bucle
│       ├── modelo/
│       │   ├── Pelota.java
│       │   ├── Paleta.java
│       │   ├── Ladrillo.java
│       │   └── Puntuacion.java
│       ├── vista/
│       │   ├── PantallaInicio.java
│       │   ├── VentanaJuego.java
│       │   ├── PanelJuego.java                ← Dibujado del juego
│       │   └── PantallaLeaderboard.java
│       └── database/
│           ├── Conexion.java                  ← Parámetros de conexión MySQL
│           └── PartidaDAO.java                ← Consultas SQL (INSERT, SELECT)
├── lib/
│   └── mysql-connector-j-x.x.x.jar           ← Driver JDBC (añadir manualmente)
└── neonbreaker.sql                            ← Script de creación de la BD
```

---

## Instrucciones para ejecutar

### 1. Requisitos previos
- Java 21 o superior
- MySQL instalado y en ejecución
- IntelliJ IDEA (recomendado)

### 2. Crear la base de datos

Abre MySQL Workbench (o la terminal de MySQL) y ejecuta el archivo `neonbreaker.sql`:

```sql
SOURCE ruta/al/proyecto/neonbreaker.sql;
```

### 3. Configurar la conexión

Abre `src/neonbreaker/database/Conexion.java` y ajusta estos valores:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/neonbreaker";
private static final String USUARIO  = "root";      // tu usuario de MySQL
private static final String PASSWORD = "root";      // tu contraseña de MySQL
```

### 4. Añadir el driver JDBC

1. Descarga **MySQL Connector/J** desde: https://dev.mysql.com/downloads/connector/j/
2. Copia el `.jar` a la carpeta `lib/` del proyecto
3. En IntelliJ: `File → Project Structure → Modules → Dependencies → + → JARs` → selecciona el `.jar`

### 5. Ejecutar

Ejecuta la clase `Main.java` desde IntelliJ (botón ▶ verde).

---

## Controles

| Tecla | Acción |
|---|---|
| `←` / `→` | Mover la paleta |
| `Espacio` | Lanzar la pelota / Continuar |

---

## Modelo de base de datos

```
USUARIOS                        PUNTUACIONES
─────────────────────           ──────────────────────────
id_usuario  PK  NOT NULL        id_partida          PK  NOT NULL
username        NOT NULL        id_usuario          FK  NOT NULL → USUARIOS
fecha_registro  NOT NULL        puntuacion_obtenida     NOT NULL
                                fecha_partida           NOT NULL
```

---

## Commits y ramas (Git)

Este proyecto sigue el flujo de trabajo requerido:

- **`main`** — rama principal con versiones estables
- **`dev`** — rama de desarrollo activa
- Commits descriptivos y progresivos
- Tag `v1.0` en la primera versión jugable

---

*"¿Cómo sacaste adelante el proyecto? — Tuve Fe 🗣️"*
