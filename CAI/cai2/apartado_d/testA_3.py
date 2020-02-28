import threading
import queue
 
def isPar(count, cola):
    for i in range(500000):
        if(i%2 == 0):
            count = count + 1
    cola.put("Hay " + str(count) + " pares")
      
def main():
    count = 0
    cola = queue.Queue()
    hilo_1 = threading.Thread(target= isPar,args=(count, cola))
    hilo_2 = threading.Thread(target= isPar,args=(count, cola))
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()
    print(cola.get())

if __name__ == "__main__":
    main()

