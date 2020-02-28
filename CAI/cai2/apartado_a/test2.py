import threading

y = ['I','n','g','e','n','i','e','r','í','a',' ','d','e',' ','s','e','g','u','r','i','d','a','d',' ','U','S',' ','-',' ','I','N','S','E','G','U','S']
res = ""
i = 0

def getPhase():
    global res, i
    global y
    while i < len(y)-1:
        res = res + y[i]
        print("Iteración " + str(i) + ": " + res)
        i = i + 1

def main():
    hilo_1 = threading.Thread(target= getPhase)
    hilo_2 = threading.Thread(target= getPhase)
    hilo_1.start()
    hilo_2.start()
    hilo_1.join()
    hilo_2.join()

if __name__ == "__main__":
    main()