import bst as binaryTree
import sys
import integrity_process as integrity_process

if __name__ == "__main__":
    print("Inserte el nombre del fichero:")
    res1 = input() 
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
        fileToVerify = res1
        file = tree.search(fileToVerify)
        
        print("Inserte en hash del usuario")
        user = input() 
        user_token = user
        tree_files = []
        server1 = ["code/files/"+file[0], file[1]]
        tree_files.append(server1)
        file_name = file[0]
        print("")
        integrity_check = integrity_process.IntegrityProcess(None, None, user_token, tree_files, file_name).check_integrity_servers()
        
    except: 
        print()