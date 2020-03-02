Para probar este caso habría que ejecutar los dos archivos, primero se ejecutaría el archivo echo-server.py, una vez ejecutado y esperando conexión se ejecutará el archivo echo-cliente.py
, tras esto se procederá a realizar un eco de un mensaje que el cliente enviará y el servidor mandará de vuelta.

El problema de integridad en este caso viene dado en que el cliente usa una codificación 'utf-8' y el servidor decodifica y codifica el mensaje de vuelta en 'base64'.

Mensaje enviado: This is the message.  It will be repeated.
Mensaje recibido por el cliente: VGhpcyBpcyB0aGUgbWVzc2FnZS4gIEl0IHdpbGwgYmUgcmVwZWF0ZWQu

