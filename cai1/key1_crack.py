import hashlib
import time
import codecs

counter = 1

pass_user = input("Enter Your sha256 Pass : ")
file_user = 'dictionary\\english_common_word.txt'

try:
    file_input = open(file_user,"r")
except:
    print("\n File Not Found")
    quit()

for pass1 in file_input:
    password = pass1.replace('\n', '')
    password +='40'
    hash_obj = codecs.encode(codecs.decode(hashlib.sha256(password.encode('utf-8')).hexdigest(), 'hex'), 'base64').decode().replace('\n', '')
    start = time.time()
#    print("Trying Password %d : %s " % (counter,password.strip()))
    counter += 1
    end = time.time()
    t_time = end - start

    if hash_obj == pass_user:
        print("\nPassword Found! Password Is : %s " % password)
        print("Total Ruuning Time is :  ",t_time,"seconds")
        break

else:
    print("\n password Not Found")