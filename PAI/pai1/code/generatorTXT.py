
import random

for i in range(0,10000):
    nameFile = "file" + str(i)
    #with open("files/copy.txt", "w") as file:
    with open('files/%s.txt' % nameFile, 'w') as file:
        file.write('prueba' + str(i))