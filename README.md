# MPAZ Pañol Francisca Orreg9i

Aplicación móvil para gestionar el inventario, los préstamos y las devoluciones de equipos tecnológicos de un pañol.

## Integrante
Francisca Orrego Muñoz

## Problemática
En un pañol de equipos de un colegio, el control de qué material hay, quién lo pidió y cuándo se devolvió suele llevarse a mano. Eso genera préstamos 
sin registro, materiales que no vuelven y devoluciones en mal estado que nadie anota. MPAZ Pañol digitaliza ese proceso: el encargado del pañol registra los préstamos y las devoluciones desde el celular y puede consultar el historial cuando lo necesite.

## Cómo funciona la aplicación

El flujo principal es: **Bienvenida → Login → Menú principal → (Inventario | Préstamos | Devoluciones | Historial)**.

### 1. Pantalla de bienvenida
Muestra el logo con una animación de flotación y el botón **Ingresar**, que lleva al login.

### 2. Login y registro
- **Registro:** Crea un usuario con nombre, correo y contraseña, y lo guarda en la base de datos local. Valida que no haya campos vacíos, que las contraseñas coincidan y que el correo no esté ya registrado.
- **Login:** Comprueba el correo y la contraseña contra la base de datos local. Si son correctos, lleva al menú principal.

### 3. Menú principal
Es el centro de la app. Incluye un **indicador del nivel de batería** del dispositivo (recurso nativo) que avisa cuando está bajo el 20 % y recomienda cargar el equipo antes de solicitar material. Desde aquí se entra a las cuatro secciones.

### 4. Inventario
Lista los materiales del pañol con las unidades disponibles:

| Material | Disponibles |
|---|---|
| Proyector | 3 |
| Notebook | 5 |
| Cable HDMI | 8 |
| Parlante | 4 |
| Control remoto | 6 |

### 5. Préstamos
Formulario para solicitar material con tres campos: **material, cantidad y sala**. Reglas de negocio:
- El material debe existir en el inventario (no distingue mayúsculas).
- La cantidad debe ser mayor a 0 y no puede superar las unidades disponibles de ese material.
- Todos los campos son obligatorios.

Cada campo muestra su propio ícono y mensaje de error. Al registrar un préstamo, la app **vibra** (recurso nativo), el mensaje de confirmación se anima y el préstamo se guarda en la base de datos local. Un contador muestra cuántos préstamos hay registrados.

### 6. Devoluciones
Formulario para registrar lo que vuelve al pañol: **material, cantidad, sala de origen, estado y observaciones**. Reglas de negocio:
- La cantidad debe ser mayor a 0.
- El estado se elige entre **Bueno, Dañado o Incompleto**.
- Si el material vuelve **Dañado o Incompleto**, las observaciones son obligatorias (mínimo 10 caracteres) para dejar registrado qué ocurrió.

Al guardar vibra distinto según el resultado: 500 ms si se registró y 150 ms si hubo errores. Debajo del formulario se listan las devoluciones guardadas, con un color distinto según el estado.

### 7. Historial
Muestra todas las devoluciones registradas, de la más reciente a la más antigua, con fecha y hora. Incluye:
- Un **resumen** con el total y la cantidad por estado.
- **Búsqueda** por material o sala.
- **Filtro** por estado (Todos, Bueno, Dañado, Incompleto).
- Botón para limpiar los filtros.

## Recursos nativos del dispositivo
- **Vibración:** confirma que una acción salió bien o mal en Préstamos y Devoluciones. Usa el permiso `VIBRATE`, declarado en el `AndroidManifest.xml`.
- **Batería:** lee el nivel actual con `BatteryManager` y lo muestra en el menú principal, con un botón para actualizarlo.

## Animaciones
- Logo flotante en la pantalla de bienvenida.
- Temblor del mensaje de confirmación al registrar un préstamo.
- Cambio animado de color y de tamaño en las tarjetas del historial.

## Arquitectura (MVVM)

La lógica está separada de la interfaz: las pantallas solo muestran el estado y los **ViewModel** concentran las validaciones y el acceso a la base de datos.

```
app/src/main/java/com/example/mpazpanol/
├── MainActivity.kt
├── data/
│   ├── database/        AppDatabase (Room, singleton)
│   └── local/
│       ├── dao/         UsuarioDao, PrestamoDao, DevolucionDao
│       └── entity/      UsuarioEntity, PrestamoEntity, DevolucionEntity
├── model/               Estados de cada pantalla y reglas (LoginModel, RegisterModel,
│                        PrestamoModel, DevolucionModel, HistorialModel)
└── ui/
    ├── components/      Header, Footer, Bateria, Vibracion
    ├── navigation/      AppNavigation (rutas)
    ├── screen/          ScreenPanol, Login, Register, Principal, Inventario,
    │                    Prestamos, Devoluciones, Historial
    ├── theme/           Tema Material 3
    └── viewmodel/       LoginViewModel, RegisterViewModel, PrestamoViewModel,
                         DevolucionViewModel, HistorialViewModel
```

## Persistencia local
Se usa **Room (SQLite)** con tres tablas: `users`, `prestamos` y `devoluciones`. La base de datos se llama `Myapp_database.db`.

## Tecnologías
Kotlin · Jetpack Compose · Material 3 · Navigation Compose · Room · ViewModel · Corrutinas


## Mockups
Los mockups de las pantallas están en la carpeta [`mockups/`](mockups/).

## Planificación y trabajo en equipo
- **Tablero de Trello:** [enlace al tablero]
- **Flujo de Git:** se usa GitHub Flow. La rama `main` se mantiene estable y cada tarea se desarrolla en su propia rama (`feature/...`), se revisa mediante Pull Request y luego se une a `main`.

## Mejoras futuras
- Guardar el inventario en la base de datos y descontar las unidades al prestar.
- Mostrar también los préstamos en el historial.
- Encriptar las contraseñas almacenadas.ne estable y cada tarea se desarrolla en su propia rama (`feature/...`) y se une mediante Pull Request.