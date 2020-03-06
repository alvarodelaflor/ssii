import bst as binaryTree
import sys
import integrity_process as integrity_process
import time

if __name__ == "__main__":
    print("Inserte el caso:")
    caso = input()
    print("Inserte el nombre del fichero:")
    res1 = input() 
    print("")

    print("¿Desea usar el árbol previamente guardado?(Y/N)")
    res = input()
    if res=="N":
        tree1 = binaryTree.binary_search_tree.generate(0,caso)
        tree2 = binaryTree.binary_search_tree.generate(1,caso)
        tree3 = binaryTree.binary_search_tree.generate(2,caso)

    elif res=="Y":
        tree1 = binaryTree.binary_search_tree.deserialize(0,caso)
        tree2 = binaryTree.binary_search_tree.deserialize(1,caso)
        tree3 = binaryTree.binary_search_tree.deserialize(2,caso)
    else:
        print("Inserto una letra erronea")
        sys.exit()

    print("")

    try: # Busco en el primer directorio
        tree_files = []
        fileToVerify = res1
        file1 = tree1.search(fileToVerify)
        file2 = tree2.search(fileToVerify)
        file3 = tree3.search(fileToVerify)

        
        print("Inserte el hash del usuario")
        user = input() 
        user_hash = user
        if caso == '1':
            server1 = ["servidores/caso1/servidor1/"+file1[0], file1[1]]
            server2 = ["servidores/caso1/servidor2/"+file2[0], file2[1]]
            server3 = ["servidores/caso1/servidor3/"+file3[0], file3[1]]
        elif caso == '2':
            server1 = ["servidores/caso2/servidor1/"+file1[0], file1[1]]
            server2 = ["servidores/caso2/servidor2/"+file2[0], file2[1]]
            server3 = ["servidores/caso2/servidor3/"+file3[0], file3[1]]
        elif caso == '3':
            server1 = ["servidores/caso3/servidor1/"+file1[0], file1[1]]
            server2 = ["servidores/caso3/servidor2/"+file2[0], file2[1]]
            server3 = ["servidores/caso3/servidor3/"+file3[0], file3[1]]
        
        tree_files.append(server1)
        tree_files.append(server2)
        tree_files.append(server3)
        file_name = fileToVerify
        print("")
        
        def ejecutaScript():
            integrity_check = integrity_process.IntegrityProcess(None, None, user_hash, tree_files, file_name).check_integrity_servers()
            time.sleep(10)
        while True:
            ejecutaScript()
    except: 
        print()