import threading
 
res = []
i = 0

def isPar(lock):
    global res, i
    while i <= 500000:
        lock.acquire()
        if(i%2 == 0):
            res.append(i)
        i += 1
        lock.release()
      
def main():
    lock = threading.Lock()
    hilo_1 = threading.Thread(target= isPar, args = (lock,))
    hilo_2 = threading.Thread(target= isPar, args = (lock,))
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()
    #print(res)
    print("Hay: " + str(len(res)) + " números pares")

