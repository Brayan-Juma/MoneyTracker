📱 MoneyTracker – Control de Gastos Personales

MoneyTracker es una aplicación móvil desarrollada en Android Studio (Java, API 33) que permite registrar ingresos y gastos, administrar un presupuesto mensual, visualizar estadísticas dinámicas y realizar conversiones de moneda mediante una API en tiempo real.
Su diseño pastel y minimalista crea una experiencia cómoda, moderna y fácil de usar.

✨ Características principales
🧾 Registro de transacciones

Agregar ingresos y gastos

Editar transacciones existentes

Eliminar con gesto “swipe”

Categorías organizadas por tipo

Conversión de moneda usando API de tasas de cambio

Validaciones profesionales de formulario

🏠 Dashboard (Resumen del mes)

Balance disponible

Total de ingresos y gastos

Cálculo del porcentaje del presupuesto

Barra de progreso

Alertas visuales cuando se supera el 80% del presupuesto

📊 Estadísticas visuales

Gráfico circular de gastos por categoría (MPAndroidChart)

Porcentajes y totales automáticos

Promedio diario de gastos

Datos actualizados en tiempo real desde SQLite

⚙️ Configuración completa

Nombre del usuario

Presupuesto mensual

Moneda principal

Día de inicio de cada ciclo

Actualizar tasas de cambio

Restablecer aplicación a modo inicial

🚀 Funcionalidades extra

Animaciones suaves (fade in/out)

Fragmentos con navegación inferior (Bottom Navigation)

Modo offline (todos los datos se guardan en SQLite)

Splash Screen pastel

Onboarding inicial

🛠 Tecnologías utilizadas

Java

Android Studio

SQLite (CRUD completo)

Retrofit / HttpURLConnection (API REST)

MPAndroidChart (gráficas)

Material Design Components

BottomNavigationView

Fragments

SharedPreferences

🗂 Estructura del proyecto
app/
 ├── data/
 │     ├── db/              (SQLite, DAOs)
 │     ├── models/          (Transaction, Category, UserSettings)
 │
 ├── ui/
 │     ├── splash/          (Inicio)
 │     ├── onboarding/      (Primera configuración)
 │     ├── main/            (Home + Bottom Navigation)
 │     ├── dashboard/       (Pantalla principal)
 │     ├── transactions/    (Lista y formulario)
 │     ├── stats/           (Gráficos)
 │     ├── settings/        (Configuración)
 │
 ├── utils/                 (Funciones auxiliares)
 │
 ├── res/
 │     ├── layout/          (Pantallas XML)
 │     ├── drawable/        (íconos y fondos pastel)
 │     ├── values/          (colores y temas)
 │     ├── anim/            (animaciones)

🎨 Diseño pastel utilizado
Color	Código	Uso
Azul pastel	#A3C9F9	Primario
Rosa pastel	#F7C8E0	Acentos / Alertas
Verde pastel	#B8E0D2	Botones
Blanco suave	#FAFAFA	Fondo
Texto oscuro	#333333	Legibilidad
🌐 API utilizada

Exchange Rate API
https://api.exchangerate-api.com/v4/latest/USD

Funciones:

Obtener tasas de cambio actualizadas

Convertir montos dentro del formulario

Manejo de errores cuando no hay conexión

🧪 Pruebas realizadas

✔ Alta, edición y eliminación de transacciones

✔ Filtros por tipo (Ingresos / Gastos)

✔ Dashboard con actualizaciones en tiempo real

✔ Estadísticas con gráficos fáciles de interpretar

✔ Conversión de moneda con API

✔ Splash + Onboarding funcional

✔ Validaciones de formulario

✔ Navegación fluida entre fragments

✔ Modo offline validado

📥 Instalación y ejecución
🔧 Clonar el proyecto
git clone https://github.com/Brayan-Juma/MoneyTracker.git

🔧 Abrir en Android Studio

File → Open

Seleccionar carpeta del proyecto

Esperar que Gradle sincronice

▶️ Ejecutar en dispositivo o emulador

Pulsa Run (▶) y selecciona un dispositivo.

📦 APK

Puedes agregar tu APK en:

/apk/MoneyTracker-release.apk


O usar la sección Releases de GitHub para una presentación profesional.

👨‍💻 Autor

Brayan Juma
Proyecto académico – UTN
Desarrollo móvil | Android | Java

📜 Licencia

Proyecto de uso académico.
Libre para estudio, análisis y mejora.
