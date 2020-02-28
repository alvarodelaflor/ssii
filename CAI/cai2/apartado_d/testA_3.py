import threading
 

count = 0

def isPar(lock):
    global count
    for i in range(500000):
        lock.acquire()
        if(i%2 == 0):
            count = count + 1
        lock.release()
      
def main():
    lock = threading.Lock()
    hilo_1 = threading.Thread(target= isPar,args=(lock,))
    hilo_2 = threading.Thread(target= isPar,args=(lock,))
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()
    print("Hay " + str(count) + " pares")

