import hashlib
import time
import codecs
import string
from itertools import *

counter = 0

pass_user = input("Enter Your sha256 Pass : ")
file_user = 'C:\\Users\\Alvaro\\Desktop\\ssii\\cai1\\dictionary\\rockyou.txt'

try:
    file_input = open(file_user,"r", encoding="utf8")
except:
    print("\n File Not Found")
    quit()

for pass1 in file_input:
    password = pass1.replace('\n', '')
    keys = []
    permutations_list = list(product(range(0,9), repeat=2))
    start = time.time()
    for number in permutations_list:
        aux = ''.join(map(str, number))
        key = password+'.'+aux+'53'
        code_key = codecs.encode(codecs.decode(hashlib.sha256(key.encode('utf-8')).hexdigest(), 'hex'), 'base64').decode().replace('\n', '')
        if code_key == pass_user:
            end = time.time()
            t_time = end - start
            password_simple = password + '.' + aux
            print("\nPassword Found! Password Is : %s " % password_simple)
            print("Total Running Time is :  ",t_time,"seconds (counter: " + str(counter) + ")" )
            quit()
        counter += 1

else:
    print("\n password Not Found")
    if counter != 1:
        print('Number of password check: ' + counter)