Para probar este caso habr�a que ejecutar los dos archivos, primero se ejecutar�a el archivo echo-server.py, una vez ejecutado y esperando conexi�n se ejecutar� el archivo echo-cliente.py
, tras esto se proceder� a realizar un eco de un mensaje que el cliente enviar� y el servidor mandar� de vuelta.

El problema de integridad en este caso viene dado en que el cliente usa una codificaci�n 'utf-8' y el servidor decodifica y codifica el mensaje de vuelta en 'base64'.

Mensaje enviado: This is the message.  It will be repeated.
Mensaje recibido por el cliente: VGhpcyBpcyB0aGUgbWVzc2FnZS4gIEl0IHdpbGwgYmUgcmVwZWF0ZWQu

