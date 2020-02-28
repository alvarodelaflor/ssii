import threading

y = ['I','n','g','e','n','i','e','r','í','a',' ','d','e',' ','s','e','g','u','r','i','d','a','d',' ','U','S',' ','-',' ','I','N','S','E','G','U','S']
res = ""
i = 0 

def getPhase(lock):
    global res, i
    global y
    while i < len(y)-1:
        lock.acquire()
        res = res + y[i]
        print("Iteración " + str(i) + ": " + res)
        i = i + 1
        lock.release()
    
def main():
    lock = threading.Lock() 
    hilo_1 = threading.Thread(target= getPhase, args = (lock,))
    hilo_2 = threading.Thread(target= getPhase, args = (lock,))
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()