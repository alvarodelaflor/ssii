from multiprocessing import Process, JoinableQueue
from queue import Empty
import time


def consumer(que, pid):
    while True:
        try:
            item = que.get(timeout=2)
            print("Proceso %s consume: %s. Ingreso aceptado" % (pid, item))
            que.task_done()
        except Empty:
            break
    print('Consumer %s done' % pid)


def producer(sequence, que):
    for item in sequence:
        print('Nueva tarea: ', item)
        que.put(item)
        time.sleep(1.99999999999999999)


if __name__ == '__main__':
    # En este ejemplo el productor crea nuevas tareas superando la capacidad de los consumidores, se perderán datos.
    que = JoinableQueue()

    # create two consumer process
    cons_p1 = Process(target=consumer, args=(que, 1))
    cons_p1.start()
    cons_p2 = Process(target=consumer, args=(que, 2))
    cons_p2.start()

    names = ['Nolasco', 'Álvaro', 'Antonio', 'Francisco', 'Juan', 'Alcora', 'María', 'Carmen', 'Rosario', 'Clara']
    numbers = [i for i in range(10)]
    taks = ['Ingreso de %s€ a la cuenta de %s' % (i, names[i % len(names)]) for i in range(15)]
    producer(taks, que)

    que.join()

    cons_p1.join()
    cons_p2.join()
