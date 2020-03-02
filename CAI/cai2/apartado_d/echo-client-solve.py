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
    sock.sendall(b'This is the message.  It will be repeated.')
    data = sock.recv(1024)
    print ('received from server "%s"' % data)

finally:
    print ('closing socket')
    sock.close()