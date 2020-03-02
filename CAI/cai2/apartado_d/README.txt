Para probar este caso habría que ejecutar los dos archivos, primero se ejecutaría el archivo echo-server.py, una vez ejecutado y esperando conexión se ejecutará el archivo echo-cliente.py
, tras esto se procederá a realizar un eco de un mensaje que el cliente enviará y el servidor mandará de vuelta.

Para solventar el problema de integridad se ha procedido a enviar los datos como bits, aún asi cabe una remota posibilidad de que el método recv de la librería sockets de python devuelva
una cadena vacía.

Mensaje enviado: This is the message.  It will be repeated.
Mensaje recibido por el cliente: This is the message.  It will be repeated.
