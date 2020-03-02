from multiprocessing import Process, JoinableQueue
import time

'''
INPUT  PARAMETERS: process the four pipeline messages
OUTPUT PARAMETERS: message received by the customer

DETECTED PROBLEM:  pipe is closed before messages are received
'''


def sender(queue, messages):
    """
    function to send messages to the pipeline
    """
    for message in messages:
        queue.put(message)
        time.sleep(0.05)
        print("Sent the message: {}".format(message))


def receiver(queue):
    """
    function to print the messages received from other
    end of pipe
    """
    time.sleep(0.1)
    while not queue.empty():
        message = queue.get()
        if message == "CLOSE PIPE":
            break
        print("Received the message: {}".format(message))


if __name__ == "__main__":
    # messages to be sent
    messages = ["Close the door", "Make an appointment with Rosario", "Completing the Workbook Exercises",
                "Say hello to Juan Manuel", "CLOSE PIPE"]

    # creating a queue
    messages_queue = JoinableQueue()

    # creating new processes
    p1 = Process(target=sender, args=(messages_queue, messages))
    p2 = Process(target=receiver, args=(messages_queue,))

    # running processes
    p2.start()
    p1.start()

    # wait until processes finish
    p1.join()
    p2.join() 