# Fegordomo

_Fegordomo es un software para el control de dispositivos IoT, en particular las familias de microcontroladores ESP32._
_Está formado por un componente manager que gestiona las peticiones, scheduler, etc. como sistema de servicios REST para sistemas operativos Linux y un agente que es instalado en cada dispositivo controlador. Puede usarse en una Raspberry Pi u Orange Pi_

## Comenzando 🚀

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas._

Mira **Deployment** para conocer como desplegar el proyecto.


### Pre-requisitos 📋

_Una Raspberry Pi 2+ o superior o similar, incluido un PC con sistema operativo Linux (Debian, CentOS, etc.)_
_Tener instalado Java 11 o superior, MariaDB y Apache 2_

### Generando paquetes 🔧
Interfaz fdwm:
ng build --prod
copiar ./dist/fdwm al servidor de páginas
Modificar /etc/apache2/sites-available/000-default o similar para añadir la ubicación y añadir a ./sites-enabled

Para tener acceso con el usuario fegor:
cd /var/www
sudo usermod -a -G www-data fegor
sudo chmod 755 /opt/fegordomo/fdwm
sudo chown -R www-data:www-data /opt/fegordomo/fdwm
sudo ln -s /opt/fegordomo/fdwm/ fdwm

Editar: sudo vi /etc/apache2/sites-enabled/000-default.conf
Y cambiar: 
    ```
    DocumentRoot /var/www/html 
    ```
por 
    ```
    DocumentRoot /var/www/fdwm
    ```

Crear .htaccess en /opt/fegordomo/fdwm y añadir:
```
<IfModule mod_rewrite.c>
  Options Indexes FollowSymLinks
  RewriteEngine On
  RewriteBase /client/
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . index.html [L]
</IfModule>
```

### Instalación 🔧

Crear base de datos: fegordomo
Crear usuario: fegordomo
Dar al usuario permisos a la base de datos

Cargar tablas para Quartz:
mysql -u fegordomo -p fegordomo < quartz.sql
 
fdwm
Arrancar el servidor.

## Ejecutando las pruebas ⚙️

Pendiente de documentar.

## Despliegue 📦

Pendiente de documentar.

## Construido con 🛠�?

* [Maven](https://maven.apache.org/) - Manejador de dependencias

## Contribuyendo 🖇�?
Por favor lee el [CONTRIBUTING.md](https://gist.github.com/fegorama/fegordomo/xxxxxx) para detalles de nuestro código de conducta, y el proceso para enviarnos pull requests.

## Wiki 📖

Puedes encontrar mucho más de cómo utilizar este proyecto en nuestra [Wiki](https://github.com/fegorama/fegordomo/wiki)

## Versionado 📌

Usamos [SemVer](http://semver.org/) para el versionado. Para todas las versiones disponibles, mira los [tags en este repositorio](https://github.com/fegorama/fegordomo/tags).

## Autores ✒️

* **Fernando González Ruano** - *Trabajo Inicial* - [fegorama](https://github.com/fegorama)

También puedes mirar la lista de todos los [contribuyentes](https://github.com/fegorama/fegordomo/contributors) quíenes han participado en este proyecto. 

## Licencia 📄

Este proyecto está bajo la Licencia (Tu Licencia) - mira el archivo [LICENSE.md](LICENSE.md) para detalles

---
2021 - [fegorama](https://github.com/fegorama) 😊