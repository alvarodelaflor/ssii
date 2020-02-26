import threading

x = 0

def increment():
    global x
    for i in range(500000):
        x +=1

def main():
    hilo_1 = threading.Thread(target= increment)
    hilo_2 = threading.Thread(target= increment)
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()
    print("x = {0} ".format(x))