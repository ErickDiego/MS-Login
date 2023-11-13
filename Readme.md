
# Proyecto MS-Login
Este proyecto, denominado MS-Login, es una aplicación desarrollada con Spring Boot, Java 8, y utiliza MongoDB como base de datos. Además, incorpora las bibliotecas Lombok para simplificar la creación de clases Java y H2 para pruebas integradas. Para realizar las pruebas, se emplea el Spock Framework, un marco de pruebas y especificaciones en Groovy.

## Tecnologías utilizadas
Spring Boot: El framework de desarrollo de aplicaciones Java que facilita la creación de aplicaciones robustas y escalables.

* **Java 8**: La versión del lenguaje de programación Java utilizada para el desarrollo del proyecto.

* **MongoDB**: Una base de datos NoSQL que proporciona flexibilidad y escalabilidad para almacenar y gestionar datos.

* **Lombok**: Una biblioteca que simplifica la creación de clases Java al generar automáticamente métodos como getters, setters, constructores y otros.

* **H2**: Una base de datos en memoria que se utiliza para pruebas integradas y desarrollo.

* **Spock Framework**: Un marco de pruebas y especificaciones en Groovy que facilita la escritura y ejecución de pruebas unitarias y de integración.

## Configuración del Proyecto
A continuación, se detallan los pasos para configurar y ejecutar el proyecto MS-Login:

1.- **Clonar el Repositorio:**

```copy
git clone https://github.com/ErickDiego/MS-Login.git
````
2.- **Importar en tu IDE:**
Importa el proyecto en tu entorno de desarrollo integrado (IDE) preferido.

3.- **Configurar la Base de Datos:**
Asegúrate de tener una instancia de MongoDB en ejecución y configura las propiedades de conexión en el archivo `application.properties` del proyecto.

4.- **Ejecutar la Aplicación:**
Inicia la aplicación desde tu IDE o mediante el siguiente comando en la terminal:

```copy
./mvnw spring-boot:run
````
5.- **Ejecutar Pruebas:**
Puedes ejecutar las pruebas del proyecto utilizando el siguiente comando:

```copy
./mvnw test
````
## Estructura del Proyecto
La estructura del proyecto sigue las convenciones de Spring Boot:

`src/main/java/com/example/demo/controller/`: Contiene los controladores de la aplicación.

`src/main/java/com/example/demo/entity/`: Contiene las entidades (modelos) utilizadas en la aplicación.

`src/main/java/com/example/demo/repository/`: Contiene los repositorios para acceder a la base de datos.

`src/main/java/com/example/demo/JWT/`: Contiene las clases relacionadas con la generación y verificación de tokens JWT.

`src/main/java/com/example/demo/service/`: Contiene los servicios de la aplicación.

`src/test/groovy/com/example/demo/controller/`: Contiene las pruebas Spock para los controladores.
## Contribuir
Si deseas contribuir al proyecto MS-Login, puedes seguir estos pasos:

Haz un fork del repositorio.
Crea una nueva rama para tu función o corrección.
Realiza los cambios necesarios y haz commit.
Envía un pull request.

¡Gracias por tu interés en el Proyecto MS-Login!