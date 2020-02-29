import threading
 
res = []
i = 0

def isPar():
    global res, i
    while i <= 500000:
        if(i%2 == 0):
            res.append(i)
        i += 1
      
def main():
    hilo_1 = threading.Thread(target= isPar)
    hilo_2 = threading.Thread(target= isPar)
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()
    #print(res)
    print("Hay: " + str(len(res)) + " números pares")

