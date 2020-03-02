TEST1
INPUT  PARAMETERS: 10 Requests for admission
OUTPUT PARAMETERS: 10 Acknowledgement of receipt of applications
DETECTED PROBLEM:  consumer has exhausted his time before he has finished generating all the requests the producer


TEST 2
INPUT  PARAMETERS: process an ordered queue of 10 elements
OUTPUT PARAMETERS: message of having processed the 10 processes
DETECTED PROBLEM:  not all processes have been processed

TEST 3
INPUT  PARAMETERS: process the four pipeline messages
OUTPUT PARAMETERS: message received by the customer
DETECTED PROBLEM:  pipe is closed before messages are received