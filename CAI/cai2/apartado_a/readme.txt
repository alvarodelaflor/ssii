Test01:
===================
Parámetros de entrada: Dos hilos que se encargan de incrementar la variable "x" inicializada a 0.
Parámetro de salido: El contador debe vale 5000

Error encontrado: Los hilos se solapan entre si provocando que el parámetro de salida sea erroneo.

Test02:
===================
Parámetros de entrada: Dos hilos que se encargan de ir leyendo de un array para formar una frase.
Parámetro de salido: La frase esperada es: "Iteración 35: Ingeniería de seguridad US - INSEGUS"

Error encontrado: Los hilos se solapan entre si provocando la repetición de letras o eliminación de ellas.

Test03:
===================
Parámetros de entrada: Dos hilos que se encargan de contar el número de números pares que existen en un rango.
Parámetro de salido: 250001 números pares

Error encontrado: Los hilos se solapan entre si saltandose números o repitindolos.