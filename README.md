# Alke Wallet - Android

**Evaluación Módulo #4: Desarrollo de la Interfaz de Usuario Android**  
*Bootcamp: Desarrollo de Aplicaciones Móviles Android Trainee (SENCE / Alkemy)*

Repositorio Oficial: [https://github.com/MajajiCL/alke-wallet-android](https://github.com/MajajiCL/alke-wallet-android)

---

## 📱 Descripción del Proyecto

**Alke Wallet** es una aplicación móvil nativa desarrollada para la plataforma Android en Kotlin y XML. El objetivo central de este proyecto es la construcción de las interfaces de usuario (UI) responsivas, limpias y fieles al diseño corporativo provisto en Figma, con navegación simulada mediante `Intent` y un sistema de persistencia local SQLite para registro e inicio de sesión funcional.

---

## 🎨 Especificación de las 9 Pantallas Desarrolladas

Siguiendo la consigna de la evaluación y los recursos de Figma, se desarrollaron e integraron las **9 pantallas clave**:

1. **Splash Screen (`SplashActivity` / `activity_splash.xml`)**:
   - Fondo celeste corporativo (`#63B8FC`).
   - Logotipo centrado con el nombre de la aplicación `AlkeWallet`.
   - Transición automática de 2 segundos hacia la pantalla de bienvenida.

2. **Login / Signup Page (`LoginSignupActivity` / `activity_login_signup.xml`)**:
   - Fondo de diseño combinado con curvatura superior celeste y contenedor blanco.
   - Botón principal de bordes redondeados: *"Crear cuenta nueva"*.
   - Botón secundario de texto: *"Ya tienes cuenta?"*.

3. **Login Page (`LoginActivity` / `activity_login.xml`)**:
   - Título tipográfico corporativo y elementos gráficos decorativos.
   - Campos de entrada con estilos redondeados: Email y Contraseña (con botón de visibilidad `password_toggle`).
   - Opción *"¿Olvidaste tu contraseña?"*.
   - Botón *"Login"* que valida credenciales contra la base de datos local y accede a Home.
   - Botón de navegación rápida a *"Crear una nueva cuenta"*.

4. **Signup Page (`SignupActivity` / `activity_signup.xml`)**:
   - Formulario completo con ScrollView para adaptabilidad a cualquier tamaño de pantalla.
   - 5 campos de entrada: Nombre, Apellido, Email, Contraseña y Reingresar contraseña.
   - Validación de campos obligatorios y coincidencia de contraseñas.
   - Botón *"Crear Cuenta"* que persiste al usuario en SQLite y redirige a Login.

5. **Home Page (`HomeActivity` / `activity_home.xml`)**:
   - Cabecera celeste con saludo personalizado (*"Hola, Amanda!"*), indicador de *"Balance Total"* (`$124.57`) y campanita de notificaciones.
   - Avatar de usuario con acceso a la pantalla de Perfil.
   - Botones de acción rápida: *"Enviar Dinero"* (botón verde corporativo) e *"Ingresar dinero"* (botón celeste).
   - Sección de *"Últimas transacciones"* con detalle de contacto, fecha/hora, montos diferenciados por color e íconos de entrada/salida.

6. **Home Page - Empty Case (`HomeEmptyActivity` / `activity_home_empty.xml`)**:
   - Variante de la pantalla principal cuando el usuario aún no registra movimientos.
   - Muestra la ilustración vectorial oficial de estado vacío (`ic_empty_illustration`) y el mensaje informativo *"No hay transacciones registradas!"*.

7. **Profile Page (`ProfileActivity` / `activity_profile.xml`)**:
   - Cabecera con avatar redondeado, nombre del usuario e ícono de edición.
   - Menú de opciones estructurado en tarjetas de fondo gris claro:
     - *Mi Información*
     - *Mis tarjetas*
     - *Opciones*
     - *Centro de ayuda*
   - Botón de retorno a la pantalla previa.

8. **Send Money (`SendMoneyActivity` / `activity_send_money.xml`)**:
   - Barra superior con botón de regreso y título *"Enviar Dinero"*.
   - Tarjeta con resumen del destinatario seleccionado (Avatar, Nombre y correo electrónico).
   - Campo numérico para la cantidad a transferir con resalte visual.
   - Campo multilínea para notas de transferencia opcionales.
   - Botón de confirmación *"Enviar Dinero"* en color verde (`#72DB31`).

9. **Request Money (`RequestMoneyActivity` / `activity_request_money.xml`)**:
   - Barra superior con botón de regreso y título *"Ingresar Dinero"*.
   - Tarjeta de contacto solicitante (Avatar, Nombre y correo).
   - Campo para monto a ingresar con resalte en azul corporativo.
   - Campo multilínea para notas de solicitud.
   - Botón de acción *"Ingresar Dinero"* en color celeste (`#63B8FC` / `#1A87DD`).

---

## 🛠️ Fundamentos y Decisiones Técnicas

- **Diseño Responsivo con ConstraintLayout**: Se estructuraron las vistas con `ConstraintLayout` y `LinearLayout` para garantizar que la interfaz se ajuste a distintas resoluciones y densidades de pantalla sin distorsión.
- **Fidelidad Visual (Figma)**:
  - **Paleta de Colores**: Definida en `colors.xml` según especificaciones HEX de Figma (`celeste: #63B8FC`, `green_button: #72DB31`, `yellow_icon: #F8BB18`, `text_primary: #1A1A1A`, etc.).
  - **Tipografía**: Incorporación de la fuente tipográfica corporativa **Jua** en `res/font/jua.ttf` y aplicada globalmente mediante `themes.xml`.
  - **Vector Drawables**: Conversión limpia de los recursos `.svg` originales de Figma a `VectorDrawable` XML nativos de Android para nitidez en cualquier densidad (hdpi, xhdpi, xxhdpi).
- **Conectividad y Navegación**: Uso de `Intent` explícitos para transicionar fluidamente entre actividades, con finalización (`finish()`) en flujos donde no corresponde volver atrás (como Splash o Login exitoso).
- **Persistencia de Datos (SQLite)**: Se implementó `DatabaseHelper` heredando de `SQLiteOpenHelper` para almacenar localmente la tabla `usuario` (`user_id`, `nombre`, `correo_electronico`, `contrasena`, `saldo`), permitiendo que el flujo de registro e inicio de sesión sea 100% funcional y verificable durante pruebas en vivo.

---

## 📂 Organización del Proyecto

```text
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/alkewallet/
│   │   │   ├── SplashActivity.kt
│   │   │   ├── LoginSignupActivity.kt
│   │   │   ├── LoginActivity.kt
│   │   │   ├── SignupActivity.kt
│   │   │   ├── HomeActivity.kt
│   │   │   ├── HomeEmptyActivity.kt
│   │   │   ├── ProfileActivity.kt
│   │   │   ├── SendMoneyActivity.kt
│   │   │   ├── RequestMoneyActivity.kt
│   │   │   └── data/
│   │   │       └── DatabaseHelper.kt       # Gestor SQLite local
│   │   ├── res/
│   │   │   ├── drawable/                  # Íconos vectoriales SVG/XML y fondos
│   │   │   ├── font/                      # Fuente tipográfica Jua
│   │   │   ├── layout/                    # Los 9 layouts XML de la app
│   │   │   └── values/                    # colors.xml, strings.xml, themes.xml
│   │   └── AndroidManifest.xml
build.gradle.kts
settings.gradle.kts
```

---

## 🚀 Compilación y Ejecución

### Opción 1: En Android Studio
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/MajajiCL/alke-wallet-android.git
   ```
2. Abrir el proyecto en **Android Studio**.
3. Dejar que Gradle sincronice las dependencias.
4. Seleccionar un emulador o dispositivo físico y presionar **Run (`Shift + F10`)**.

### Opción 2: Instalación del APK
El archivo compilado listo para instalar se encuentra disponible en:
- `app/build/outputs/apk/debug/app-debug.apk` (o en la raíz del proyecto para pruebas rápidas).
