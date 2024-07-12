
Aplicación de una tienda online realizada para el proyecto final del curso que es Sprinboot.

## Autor: Anconeyra Suyo Frank

**Fecha Inicio Proyecto: 27/06/2024**

**Ultima Actualización: 12/07/2024**


## Funcionalidades
- La web contará con una cabecera y un pie común a todas las páginas que componen la aplicación (uso de includes/fragments), así como de un menú para la navegación entre las diferentes páginas de la web.
- Al acceder a la página inicial se mostrará el catálogo completo de productos (tabla Productos).
- En la página principal existirá un filtro de búsqueda de los productos del catálogo por diferentes características: búsqueda general, categoría (tabla Categorias).
- La tienda contará con un formulario de login, permitiendo al usuario de la aplicación logarse en todo momento (común a todos los roles).
- Existirán 3 roles de usuario (tabla Roles) además del rol por defecto ‘anónimo’ cuando el usuario no está logado.


Anónimo - (Funcionalidad dependiendo del rol del usuario)
- Ver el catálogo de productos.
- Navegar a través del catálogo, pudiendo ver los detalles de los productos pinchando en cada uno de ellos desde la vista del catálogo.


Cliente - (Funcionalidad dependiendo del rol del usuario)
- Realizar pedido (proceso de compra).
- Ver historial de pedidos realizados (incluido el estado actual del pedido).
- Ver detalle del pedido.
- Cancelar pedido: podrá solicitar la cancelación total del pedido realizado (a través del historial de pedidos).
- Ver perfil de usuario (el suyo propio).

Empleado - (Funcionalidad dependiendo del rol del usuario)
- Gestionar productos (altas, actualizaciones).
- Gestionar clientes (altas, actualizaciones).
- Ver perfil de usuario (el suyo propio).

Administrador - (Funcionalidad dependiendo del rol del usuario)
- Gestionar productos (bajas lógicas).
- Gestionar clientes (bajas lógicas).
- Gestionar empleados (altas, actualizaciones, bajas lógicas).
- Procesar cancelaciones: validará las cancelaciones de los pedidos solicitadas por los clientes.


Cliente - (Funcionalidad dependiendo del rol del usuario)
- Modificar perfil de usuario (el suyo propio), sin incluir la contraseña de acceso a la aplicación.


Administrador - (Funcionalidad dependiendo del rol del usuario)
Además de las funciones descritas para el usuario con rol ‘empleado’:
- Gestionar administradores (altas, actualizaciones, bajas lógicas): solo el usuario ‘admin’ (superadministrador).

## Front end
- HTML
- CSS
- JavaScript
- Boostrap

## Back end
- Sprinboot

## Database
- MySql

## Si deseas que funcione el aplicativo web necesitas crear un base de datos despúes de ello necesitas poner toda esa configuración en properties.
