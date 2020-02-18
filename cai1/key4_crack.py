import hashlib
import time
import base64
import datetime
import codecs

counter = 1

#Hash = kPhCBVXOOqkNinWXy44FUUPjP2dKKJcB/J0x7lrYEQQ=

passwordIn = input("Enter Your md5 Pass : ")

base = datetime.datetime.today()
date_list = [base - datetime.timedelta(days=x) for x in range(36500)]


for date in date_list:
    password = date.strftime("%d%m%Y")
    password += "29"
    print("Trying Password %d : %s " % (counter,password))
    hash_obj = codecs.encode(codecs.decode(hashlib.sha256(password.encode('utf-8')).hexdigest(), 'hex'), 'base64').decode().replace('\n', '')
    start = time.time()
    counter += 1
    end = time.time()
    t_time = end - start

    if hash_obj == passwordIn:
        print("\nPassword Found!!! Password Is : %s " % password)
        print("Total Ruuning Time is :  ",t_time,"seconds")
        break

else:
    print("\n password Not Found")
    