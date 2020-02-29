from multiprocessing import Process, Queue, JoinableQueue
from queue import Empty
import time


def square_list(to_do, done, id_process):
    """
    function to square a given list
    """
    while not to_do.empty():
        try:
            elem = to_do.get(timeout=10)
            done.put(elem * elem)
            print('%s * %s done by process %s' % (elem, elem, id_process))
            to_do.task_done()
        except Empty:
            break
    print('Consumer %s done' % id_process)


def print_queue(to_do, done):
    """
    function to print queue elements
    """
    while not to_do.empty() or not done.empty():
        try:
            elem = done.get()
            print(elem)
            done.task_done()
        except Empty:
            break
    print("Queue is now empty!")


if __name__ == "__main__":
    # input list
    mylist = [1, 2, 3, 4]

    # creating multiprocessing Queue
    to_do = JoinableQueue()
    [to_do.put(elem) for elem in mylist]

    done = JoinableQueue()

    # creating new processes
    c1 = Process(target=print_queue, args=(to_do, done))
    c1.start()
    p1 = Process(target=square_list, args=(to_do, done, 1))
    p1.start()
    p2 = Process(target=square_list, args=(to_do, done, 2))
    p2.start()

    # running process p1 to square list
    p1.join()

    # running process p2 to get queue elements
    p2.join()

    # running process p2 to get queue elements
    c1.join()
