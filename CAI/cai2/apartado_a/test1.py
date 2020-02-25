import threading

y = ['c', 'a', 'r', 'm', 'e', 'n']
res = " "
x = 0

def increment_global():
    global x
    x += 1

def getLetter():
    global res
    res = res + y[x]
    print(res)
    increment_global()

def main():
    global x
    global res

    hilo_1 = threading.Thread(target= getLetter)
    hilo_2 = threading.Thread(target= getLetter)
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    for i in range(3):
        print("Iteración {0}".format(i))
        main()