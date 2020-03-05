import bst as binaryTree
import sys

if __name__ == "__main__":
    print("Inserte el nombre del fichero:")
    res1 = input() #file0.txt
    print("")

    print("¿Desea usar el árbol previamente guardado?(Y/N)")
    res = input()

    if res=="N":
        tree = binaryTree.binary_search_tree.generate()
    elif res=="Y":
        tree = binaryTree.binary_search_tree.deserialize()
    else:
        print("Inserto una letra erronea")
        sys.exit()


    print("")
    try:
        fileToVerify = (res1)
        file = tree.search(fileToVerify)
        print("Aquí va la pararte de Alvaritus")
        
    except:
        print("No se encuentra el arvhivo")

    