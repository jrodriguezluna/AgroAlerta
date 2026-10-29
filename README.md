# AgroAlerta

App Android para el ramo Desarrollo de Aplicaciones Móviles. Permite ver el estado de un predio agrícola y sus alertas desde el celular. Los datos de monitoreo son ficticios.

## Pantallas

- **Login**: inicio de sesión con correo y contraseña.
- **Registro**: crea una cuenta nueva. Siempre se crea con rol Técnico.
- **Principal**: tiene una barra inferior con cuatro secciones:
  - Inicio: estado general del predio y sus sistemas.
  - Cuarteles: lista de cuarteles y detalle con sensores.
  - Equipos: estado de válvulas, bombas y anti-heladas.
  - Alertas: alertas activas e historial, con detalle.
- **Perfil**: datos del usuario y cerrar sesión.

La información que se muestra cambia según el rol (Técnico o Supervisor). Solo el Supervisor puede marcar una alerta como revisada.

## Tecnologías

- Kotlin
- Jetpack Compose y Material 3 (Scaffold, TopAppBar, NavigationBar)
- Navigation Compose
- Room sobre SQLite para guardar los usuarios
- ViewModel y StateFlow (MVVM)
- Contraseñas guardadas con hash SHA-256

## Cuenta por defecto

Se crea automáticamente la primera vez que se abre la app.

| Rol | Correo | Contraseña |
|---|---|---|
| Supervisor | supervisor@agroalerta.cl | supervisor123 |

Para entrar como Técnico, registrar una cuenta nueva desde la pantalla de registro.

## Cómo ejecutarlo

1. Clonar el repositorio.
2. Abrirlo en Android Studio y esperar el Gradle Sync.
3. Ejecutarlo en un emulador o celular con Android 7.0 (API 24) o superior.

## Integrantes

- Brandom Hormazábal
- Jefferson Luna
