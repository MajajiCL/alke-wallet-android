# Alke Wallet - Android (Arquitectura Empresarial MVVM)

**Evaluación Módulo #6: Desarrollo de Aplicaciones Empresariales Android**  
*Bootcamp: Desarrollo de Aplicaciones Móviles Android Trainee (SENCE / Alkemy)*

Repositorio Oficial: [https://github.com/VannePulgar/alke-wallet-android](https://github.com/VannePulgar/alke-wallet-android)

---

## 📱 Descripción del Proyecto

**Alke Wallet** es una aplicación móvil financiera nativa desarrollada en **Kotlin** para Android. En esta versión empresarial correspondiente al **Módulo #6**, la aplicación ha sido refactorizada e impulsada bajo el patrón arquitectónico oficial **MVVM (Model - View - ViewModel)** de Android Jetpack.

Integra consumo de **APIs REST externas mediante Retrofit**, almacenamiento local persistente con **modo Offline**, carga y caché asíncrona de imágenes mediante **Picasso**, gestión segura de sesiones, validaciones robustas y un conjunto de **pruebas unitarias automatizadas con JUnit y Mockito**.

---

## 🏛️ Arquitectura MVVM (Model - View - ViewModel)

El proyecto implementa una estricta separación de responsabilidades:

```text
com.example.alkewallet/
├── data/
│   ├── model/                  # MODELO DE DOMINIO Y DTOs
│   │   ├── UserProfile.kt      # Entidad de perfil de usuario
│   │   ├── TransactionItem.kt  # Entidad y modelo de transacción
│   │   ├── NetworkModels.kt    # DTOs: LoginRequest, BalanceDto, TransactionDto
│   │   └── Resource.kt         # Sealed class (Success, Error, Loading)
│   ├── remote/                 # RED Y API REST (Retrofit 2)
│   │   ├── WalletApiService.kt # Interfaz Retrofit (@POST login, @GET balance, @GET/@POST tx)
│   │   └── RetrofitClient.kt   # Cliente HTTP con OkHttp, Gson y HttpLoggingInterceptor
│   ├── repository/             # PATRÓN REPOSITORIO (Fuente única de verdad)
│   │   └── WalletRepository.kt # Coordina API remota con almacenamiento local (Offline-first)
│   └── DatabaseHelper.kt       # Persistencia local SQLite/Room con soporte Offline
├── ui/
│   ├── viewmodel/              # VIEWMODEL (Lógica de presentación desacoplada)
│   │   ├── AuthViewModel.kt    # Manejo reactivo de login y registro (LiveData)
│   │   └── WalletViewModel.kt  # Gestión de saldo, transacciones y envíos (LiveData)
│   ├── LoginActivity.kt        # Vista de autenticación observando AuthViewModel
│   ├── SignupActivity.kt       # Vista de registro observando AuthViewModel
│   ├── HomeActivity.kt         # Tablero observando WalletViewModel y Picasso para avatar
│   ├── SendMoneyActivity.kt    # Envío de fondos conectado a WalletViewModel
│   ├── RequestMoneyActivity.kt # Solicitud / Depósito de fondos con WalletViewModel
│   └── ProfileActivity.kt      # Perfil con carga remota de imágenes vía Picasso
```

---

## 🚀 Tecnologías y Librerías Implementadas

1. **Retrofit 2 + Gson Converter (`com.squareup.retrofit2:retrofit:2.9.0`):**
   * Cliente HTTP type-safe para consumo de la API REST externa de la billetera.
   * Serialización y deserialización automática de objetos JSON.
2. **OkHttp Logging Interceptor (`com.squareup.okhttp3:logging-interceptor:4.12.0`):**
   * Monitoreo detallado de peticiones y respuestas HTTP en consola de depuración.
3. **Picasso (`com.squareup.picasso:picasso:2.8`):**
   * Descarga, renderizado y caché asíncrono de imágenes de perfil remotas en `HomeActivity` y `ProfileActivity` con placeholders y control de errores.
4. **Android Lifecycle ViewModel & LiveData (`androidx.lifecycle:2.7.0`):**
   * Persistencia del estado ante rotaciones de pantalla y arquitectura reactiva.
5. **Kotlin Coroutines (`kotlinx.coroutines:1.7.3`):**
   * Concurrencia y ejecución de tareas de red y base de datos en segundo plano (`Dispatchers.IO`), manteniendo la interfaz fluida en `Dispatchers.Main`.
6. **Almacenamiento Local (Modo Offline):**
   * Persistencia de usuarios, saldos y transacciones en el dispositivo para garantizar que la app siga funcionando sin conexión a internet.
7. **Testing Automatizado (`JUnit 4`, `Mockito`, `InstantTaskExecutorRule`, `kotlinx-coroutines-test`):**
   * Pruebas unitarias para las reglas de negocio del `WalletViewModel` y `AuthViewModel`.

---

## 🧪 Pruebas Unitarias Ejecutadas

Ubicación: `app/src/test/java/com/example/alkewallet/`

* **`WalletViewModelTest.kt`:**
  * `sendMoney_withInvalidAmount_setsErrorState()`: Valida que montos negativos sean rechazados con mensaje claro.
  * `sendMoney_withZeroAmount_setsErrorState()`: Valida que montos en $0 sean rechazados.
  * `requestMoney_withEmptyAmount_setsErrorState()`: Valida campos obligatorios en solicitudes.
  * `loadWalletData_updatesBalanceAndTransactions()`: Prueba de integración con Coroutines Test Dispatcher y LiveData.
* **`AuthViewModelTest.kt`:**
  * `login_withBlankFields_setsErrorState()`: Valida prevención de accesos con campos vacíos.
  * `register_withPasswordMismatch_setsErrorState()`: Comprueba validación de coincidencia de contraseñas.
  * `register_withEmptyFields_setsErrorState()`: Asegura el llenado de información obligatoria.

Comando para ejecutar pruebas:
```bash
./gradlew testDebugUnitTest
```
*(Resultado: 100% pruebas aprobadas - `BUILD SUCCESSFUL`).*

---

## 🔑 Credenciales de Demostración

* **Correo:** `amanda@alkewallet.com`
* **Contraseña:** `1234`
*(O cualquier usuario nuevo registrado a través de la pantalla de Registro).*
