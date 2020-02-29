from multiprocessing import Process, Queue


def square_list(to_do, done, id_process):
    """
    function to give a queue of numbers to return the squares in another queue
    The process that performs it is identified by a ID
    """
    while not to_do.empty():
        elem = to_do.get(timeout=100)
        done.put(elem * elem)
        print('Operation done by process ' + id_process)
    print('Empty pipe by process %s' % id_process)


def print_queue(done):
    """
    function to print queue elements
    """
    print("Queue elements:")
    while not done.empty():
        print(done.get())
    print("Queue is now empty!")


if __name__ == "__main__":
    # creating multiprocessing Queue of initial numbers
    to_do = Queue()
    mylist = [i for i in range(5)]
    [to_do.put(elem) for elem in mylist]

    # creating multiprocessing Queue of square numbers
    done = Queue()

    # creating new processes
    c1 = Process(target=print_queue, args=(done,))
    c1.start()

    p1 = Process(target=square_list, args=(to_do, done, '1'))
    p1.start()

    p2 = Process(target=square_list, args=(to_do, done, '2'))
    p2.start()

    p1.join()
    p2.join()
    c1.join()

