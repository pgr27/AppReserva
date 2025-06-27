# AppReservaCitas

**AppReservaCitas** es una aplicación móvil desarrollada en Android que forma parte del sistema de gestión de citas de *Peluditos & Felices*, junto con la app para propietarios `AppGestionReservaCitas`.

Está diseñada para clientes de peluquerías caninas que desean solicitar cita de forma rápida, sencilla y sin necesidad de realizar llamadas.  
Ofrece una experiencia intuitiva y accesible desde cualquier lugar, permitiendo consultar disponibilidad, elegir un servicio y completar la reserva con todos los datos necesarios.

Su objetivo es proporcionar una alternativa moderna y eficiente a los métodos tradicionales de gestión de citas.

---

## 🧩 Funcionalidades principales

- **Reserva de citas personalizada**: elige día, hora y servicio, y añade los datos necesarios del cliente y su mascota (si aplica).
- **Confirmación visual**: una vez hecha la reserva, se muestra un resumen claro de la cita.
- **Consulta de disponibilidad**: el usuario ve solo las horas libres en función del servicio y la duración del mismo.
- **Asociación usuario-mascota**: el sistema vincula cada reserva a una persona y su mascota.
- **Gestión sencilla desde la app del negocio** (complementaria): los dueños pueden ver todas las citas registradas por sus clientes. Esta funcionalidad se ofrece desde AppGestionReservaCitas.

---

## 🛠️ Tecnologías utilizadas

- **Kotlin**
 **Firebase (Firestore)**
- **Android Studio**
- **Git / GitHub**

Además, toda la lógica de negocio está desarrollada desde cero, incluyendo la validación de datos, la gestión de disponibilidad horaria y el sistema de confirmación.

---

## 📦 Instalación

Si quieres probar **AppReservaCitas** en tu equipo local, sigue estos pasos:

1. Clona el repositorio desde GitHub: `git clone https://github.com/pgr27/AppReservaCitas.git`
2. Abre el proyecto en Android Studio:Selecciona "Open" y busca la carpeta del proyecto.
3. Espera a que se sincronicen todas las dependencias (Gradle).
4. Conecta un dispositivo físico o inicia un emulador Android.
5. Pulsa en el botón "Run" o ejecuta desde terminal: `./gradlew installDebug`
   
*Asegúrate de tener instalado Android Studio, el SDK de Android necesario y un entorno Java correctamente configurado*

---


## 🔗 Proyecto complementario
Esta aplicación está diseñada para funcionar junto con `AppGestionReservaCitas` (app para los propietarios que permite la gestión del negocio).
