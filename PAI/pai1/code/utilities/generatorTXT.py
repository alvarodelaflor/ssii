for i in range(0,1000):
    nameFile = "file" + str(i)
    with open('servidores/caso1/servidor1/%s.txt' % nameFile, 'w') as file:
        file.write('prueba' + str(i))
