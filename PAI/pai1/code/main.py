import time
#import utilities.integrity_process as binaryTree
from utilities import integrity_process, bst as binaryTree

if __name__ == "__main__":
    case = input("Seleccione el caso deseado-. \n"
                 "      CASO 1. Los tres servidores contienen la misma información.\n"
                 "      CASO 2. El servidor 2 tiene el archivo file81.txt modificado y su hash por parte del"
                 " usuario sería 2c89791a1f0f63771de1ffdf53f219ab0835e5e68c4836054216419fd2020a9b\n"
                 "      CASO 3. El servidor 1 y 2 tienen el archivo file6.txt modificado y su hash por parte del"
                 " usuario sería cc89fd4a1ff2b97ad283384df871b15f18489ac879e04ecc8a0dde308bd042cc\n "
                 "Escriba su decisión: ")
    file_name_input = input("Inserte el nombre del fichero: ")

    tree_option = ""
    while not (tree_option == "N" or tree_option == "n" or tree_option == "Y" or tree_option == "y"):
        tree_option = input("¿Desea usar el árbol previamente guardado (Y/N)?: ")
        if not (tree_option == "N" or tree_option == "n" or tree_option == "Y" or tree_option == "y"):
            print('No ha insertado un valor válido, inténtelo de nuevo.')

    if tree_option == "N" or tree_option == "n":
        tree1 = binaryTree.binary_search_tree.generate(0, case)
        tree2 = binaryTree.binary_search_tree.generate(1, case)
        tree3 = binaryTree.binary_search_tree.generate(2, case)

    else:
        tree1 = binaryTree.binary_search_tree.deserialize(0, case)
        tree2 = binaryTree.binary_search_tree.deserialize(1, case)
        tree3 = binaryTree.binary_search_tree.deserialize(2, case)

    try:  # Busco en el primer directorio
        tree_files = []
        fileToVerify = file_name_input
        file1 = tree1.search(fileToVerify)
        file2 = tree2.search(fileToVerify)
        file3 = tree3.search(fileToVerify)

        token_user = input("Inserte el token del usuario: ")
        if case == '1':
            server1 = ["servidores/caso1/servidor1/" + file1[0], file1[1]]
            server2 = ["servidores/caso1/servidor2/" + file2[0], file2[1]]
            server3 = ["servidores/caso1/servidor3/" + file3[0], file3[1]]
        elif case == '2':
            server1 = ["servidores/caso2/servidor1/" + file1[0], file1[1]]
            server2 = ["servidores/caso2/servidor2/" + file2[0], file2[1]]
            server3 = ["servidores/caso2/servidor3/" + file3[0], file3[1]]
        else:
            server1 = ["servidores/caso3/servidor1/" + file1[0], file1[1]]
            server2 = ["servidores/caso3/servidor2/" + file2[0], file2[1]]
            server3 = ["servidores/caso3/servidor3/" + file3[0], file3[1]]

        tree_files.append(server1)
        tree_files.append(server2)
        tree_files.append(server3)
        file_name = fileToVerify
        print("")

        def check_integrity():
            integrity_check = integrity_process.IntegrityProcess(None, None, token_user, tree_files)\
                .check_integrity_servers()
            time.sleep(10)


        while True:
            check_integrity()
    except Exception as e:
        print('Se ha producido un error: %s' % e)
