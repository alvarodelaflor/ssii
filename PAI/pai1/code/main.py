import bst as binaryTree
import sys

if __name__ == "__main__":
    print("Inserte el nombre del fichero:")
    res1 = input() #PATA
    print("")

    print("¿Desea usar el árbol previamente guardado?(Y/N)")
    res = input()
    directorio = 0
    if res=="N":
        tree = binaryTree.binary_search_tree.generate(directorio)
    elif res=="Y":
        tree = binaryTree.binary_search_tree.deserialize(directorio)
    else:
        print("Inserto una letra erronea")
        sys.exit()

    print("")

    try: # Busco en el primer directorio
        fileToVerify = (res1)
        file = tree.search(fileToVerify)
        print("Aquí va la pararte de Alvaritus")
    except: 
        print("No se encuentra el arvhivo en el primer directorio, buscando en el segundo...")

        directorio += 1
        if res=="N":
            tree = binaryTree.binary_search_tree.generate(directorio)
        elif res=="Y":
            tree = binaryTree.binary_search_tree.deserialize(directorio)

        try: # Busco en el segundo directorio
            fileToVerify = (res1)
            file = tree.search(fileToVerify)
            print("Aquí va la pararte de Alvaritus")
        except:
            print("No se encuentra el arvhivo en el segundo directorio, buscando en el tercero...")
            directorio += 1
            if res=="N":
                tree = binaryTree.binary_search_tree.generate(directorio)
            elif res=="Y":
                tree = binaryTree.binary_search_tree.deserialize(directorio)

            try: # Busco en el tercer directorio
                fileToVerify = (res1)
                file = tree.search(fileToVerify)
                print("Aquí va la pararte de Alvaritus")
            except: 
                print("Lo siento, el archivo no existe")

