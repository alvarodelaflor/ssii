import hashlib
import time
import base64
import datetime
import codecs
from itertools import permutations

counter = 1

#Hash = dWwYYmszPa4xHOsLuVi3fvoBkcKdWJmGM5wmIN6K2x8=

passwordIn = input("Enter Your md5 Pass : ")

perm_list = permutations(["r","a","f","a","e","l",
                         "c", "o", "n", "s", "e", "r", "y",
                         "t", "a", "m", "p", "e", "r"],6)
for perm_aux in perm_list:
    perm = "".join(perm_aux)
    
    password = perm + "17"
    hash_obj = codecs.encode(codecs.decode(hashlib.sha256(password.encode('utf-8')).hexdigest(), 'hex'), 'base64').decode().replace('\n', '')
    start = time.time()
    counter += 1
    end = time.time()
    t_time = end - start

    if hash_obj == passwordIn:
        print("\nPassword Found!!! Password Is : %s " % perm)
        print("Total Ruuning Time is :  ",t_time,"seconds")
        break

else:
    print("\n password Not Found")
    