import threading

x = 0

def increment(lock):
    global x
    for i in range(500000):
        lock.acquire()
        x +=1
        lock.release()

def main():
    lock = threading.Lock() 
    hilo_1 = threading.Thread(target= increment, args = (lock,))
    hilo_2 = threading.Thread(target= increment, args = (lock,))
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()
    print("x = {0} ".format(x))