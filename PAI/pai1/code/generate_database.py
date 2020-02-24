import os
import random
import string

num_files = int(input('Ingrese el número de archivos que desea generar \n Debe estar entre 1 y 20\n'))
aux = 0
while aux < num_files:
    file = open("directory/{}".format(aux), "w")
    file.write(''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(4)))
    file.close()
    aux += 1





