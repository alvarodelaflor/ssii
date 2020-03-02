Para probar este caso habr�a que ejecutar los dos archivos, primero se ejecutar�a el archivo echo-server.py, una vez ejecutado y esperando conexi�n se ejecutar� el archivo echo-cliente.py
, tras esto se proceder� a realizar un eco de un mensaje que el cliente enviar� y el servidor mandar� de vuelta.

Para solventar el problema de integridad se ha procedido a enviar los datos como bits, a�n asi cabe una remota posibilidad de que el m�todo recv de la librer�a sockets de python devuelva
una cadena vac�a.

Mensaje enviado: This is the message.  It will be repeated.
Mensaje recibido por el cliente: This is the message.  It will be repeated.

====================
Test apartado A - (testA_1, testA_2, testC_3)
Las condiciones de carrera de los tres test se han solucionado con Lock(). Bloqueando la partes del código que poseen riegos de producirse alguna condición de carrera.
