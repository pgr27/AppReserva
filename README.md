**AppReservaCitas** es una aplicación móvil desarrollada en Android, pensada para facilitar la reserva de citas de forma rápida, intuitiva y desde cualquier lugar.  
Está diseñada para clientes que necesitan pedir cita en negocios como peluquerías caninas, centros de estética, veterinarios, etc., sin necesidad de llamadas ni complicaciones.

La app ofrece una experiencia clara y sencilla, permitiendo a los usuarios consultar disponibilidad, seleccionar el servicio que desean y completar la reserva con todos los datos necesarios.  
Su objetivo es ofrecer una alternativa cómoda, moderna y eficaz a los sistemas tradicionales de gestión de citas.

## 🧩 Funcionalidades principales

- **Reserva de citas personalizada**: elige día, hora y servicio, y añade los datos necesarios del cliente y su mascota (si aplica).
- **Confirmación visual**: una vez hecha la reserva, se muestra un resumen claro de la cita.
- **Consulta de disponibilidad**: el usuario ve solo las horas libres en función del servicio y la duración del mismo.
- **Asociación usuario-mascota**: el sistema vincula cada reserva a una persona y su mascota.
- **Gestión sencilla desde la app del negocio** (complementaria): los dueños pueden ver todas las citas registradas por sus clientes. Esta funcionalidad se ofrece desde AppGestionReservaCitas.

## 🛠️ Tecnologías utilizadas

- React Native
- Firebase (Firestore)
- Android Studio
- Git / GitHub

Además, toda la lógica de negocio está desarrollada desde cero, incluyendo la validación de datos, la gestión de disponibilidad horaria y el sistema de confirmación.

## 📦 Instalación

Si quieres probar **AppReservaCitas** en tu equipo local, sigue estos pasos:

1. Clona el repositorio desde GitHub: `git clone https://github.com/pgr27/AppReservaCitas.git`
2. Abre el proyecto en Android Studio:Selecciona "Open" y busca la carpeta del proyecto.
3. Espera a que se sincronicen todas las dependencias (Gradle).
4. Conecta un dispositivo físico o inicia un emulador Android.
5. Pulsa en el botón "Run" o ejecuta desde terminal: `./gradlew installDebug`
   
*Asegúrate de tener instalado Android Studio, el SDK de Android necesario y un entorno Java correctamente configurado*
