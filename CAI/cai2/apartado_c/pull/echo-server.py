import socket
import sys
from base64 import b64encode

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = ('localhost', 10000)
print ('starting up on %s port %s' % server_address)
sock.bind(server_address)

# Listen for incoming connections
sock.listen(1)

while True:
    # Wait for a connection
    print ('waiting for a connection')
    connection, client_address = sock.accept()

    try:
        print ('connection from', client_address)

        data = b64encode(connection.recv(1024))
        print ('received from client"%s"' % data)
        if data:
            print ('sending data back to the client')
            connection.send(data)
        else:
            print ('no more data from', client_address)
            break
            
    finally:
        # Clean up the connection
        connection.close()

