import threading
 
count = 0

def isPar():
    global count
    for i in range(500000):
        if(i%2 == 0):
            count = count + 1
      
def main():
    hilo_1 = threading.Thread(target= isPar)
    hilo_2 = threading.Thread(target= isPar)
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()
    print("Hay " + str(count) + " pares")

