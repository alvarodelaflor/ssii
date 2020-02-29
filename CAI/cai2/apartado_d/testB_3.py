from multiprocessing import Pipe, Process


def sender(pipe, messages):
    """
    function to send messages to the pipeline
    """
    for message in messages:
        pipe.send(message)
        print("Sent the message: {}".format(message))
    pipe.close()


def receiver(pipe):
    """
    function to print the messages received from other
    end of pipe
    """
    while True:
        message = pipe.recv()
        if message == "CLOSE PIPE":
            break
        print("Received the message: {}".format(message))


if __name__ == "__main__":
    # messages to be sent
    messages = ["Close the door", "Make an appointment with Rosario", "Completing the Workbook Exercises",
                "Say hello to Juan Manuel", "CLOSE PIPE"]

    # creating a pipe
    sender_pipe, receiver_pipe = Pipe()

    # creating new processes
    p1 = Process(target=sender, args=(sender_pipe, messages))
    p2 = Process(target=receiver, args=(receiver_pipe,))

    # running processes
    p1.start()
    p2.start()

    # wait until processes finish
    p1.join()
    p2.join()
