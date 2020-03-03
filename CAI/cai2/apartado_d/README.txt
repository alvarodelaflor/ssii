Apartado a)

Test apartado A - (testA_1, testA_2, testC_3)
Las condiciones de carrera de los tres test se han solucionado con Lock(). Bloqueando la partes del código que poseen riegos de producirse alguna condición de carrera.
============================================================================================================================================================================================
Apartado b)

Para arreglar los problemas de todas las pruebas debemos ser conscientes de cómo funcionan realmente los tubos y los tiempos entre los procesos.

Tenemos que ajustar las velocidades de los productores y consumidores y asegurarnos de que el canal de comunicación no se cierre hasta que 
todos los procesos se han consumido.
============================================================================================================================================================================================
Apartado c)

Para probar este caso habria que ejecutar los dos archivos, primero se ejecutara el archivo echo-server.py, una vez ejecutado y esperando conexion se ejecutara el archivo 
echo-cliente.py, tras esto se procedera a realizar un eco de un mensaje que el cliente enviara y el servidor mandara de vuelta.

Para solventar el problema de integridad se ha procedido a enviar los datos como bits, aun asi cabe una remota posibilidad de que el metodo recv de la libreria sockets de python devuelva
una cadena vacia.

Mensaje enviado: This is the message.  It will be repeated.
Mensaje recibido por el cliente: This is the message.  It will be repeated.

============================================================================================================================================================================================
