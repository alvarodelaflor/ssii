import socket
import sys

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect the socket to the port where the server is listening
server_address = ('localhost', 10000)
print ('connecting to %s port %s' % server_address)
sock.connect(server_address)

try:
    
    # Send data
    print ('sending This is the message.  It will be repeated.')
    sock.send(('This is the message.  It will be repeated.').encode('utf-8'))

    # Look for the response
    #amount_received = 0
    #amount_expected = len(message)
    
    #while amount_received < amount_expected:
    data = sock.recv(1024).decode('utf-8')
        #amount_received += len(data)
    print ('received from server "%s"' % data)

finally:
    print ('closing socket')
    sock.close()