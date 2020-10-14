# Servicio (Java)

Este archivo contiene la información necesaria para construir la imagen de docker e iniciar el contenedor

## Contenido de la imagen

La imagen contiene openjdk 8 y el servicio que utiliza HTTPServer nativo del lenguaje de programación.

## Instrucciones para construir la imagen y correr el contenedor

Previamente se requiere tener instalado `Docker CE`.
Abrir una terminal y dirigirse a la carpeta donde ha clonado este repositorio y ejecutar los comandos listados abajo.

### `docker build -t <usuario_docker/proyecto> .`

Construye la imagen correspondiente.

### `docker run -p <puerto_externo>:8000 <usuario_docker/proyecto>`

Inicia el contenedor, de esta forma es accesible el servicio definido en `Test.py` en `localhost:8000/api`.

## Instrucciones para detener el contenedor

Escribir  `docker stop < id contenedor>` o  `docker kill < id contenedor >`, donde el id del contenedor que deseamos detener lo podemos conocer con  `docker ps`.
## Instrucciones para entrar al contenedor en ejecución

Escribir `docker exec -it < id contenedor> sh`.

## Instrucciones para cambiar el puerto de acceso a la aplicación

Ubicar la línea que contiene la palabra `InetSocketAddress(8000)` en el archivo `Handler.java` y modificar el parámetro de dicho método (Ej. `HttpServer server = HttpServer.create(new InetSocketAddress(1080), 0);`, de esta forma sería accesible el servicio a través de `localhost:1080/api`), claramente luego volver a ejecutar `docker build -t <usuario_docker/proyecto> .`, si estaba el contenedor corriendo entonces antes ejecutar `docker stop < id contenedor>`.

## Instrucciones para modificar las rutinas que realiza nuestro servicio

Se deberá modificar el archivo `Handler.java` que está en `src` y por supuesto debería despues volver a ejecutar `docker build -t <usuario_docker/proyecto> .`.
