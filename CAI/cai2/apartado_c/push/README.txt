En este caso se ha creado un servicio de streaming en el que se env�a constantemente el resultadod del time.time, para ello se ha
creado tambi�n un buffer. El posible fallo de integridad ser�a en el caso en el que el tama�o de lo que queramos enviar sea superior al
l�mite del buffer. Otro posible fallo ser�a que el cliente y servidor usaran distintos m�todos de codificaci�n al enviar datos.