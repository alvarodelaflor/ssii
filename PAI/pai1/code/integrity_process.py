from datetime import *
from PAI.pai1.code.hash_utilities import Hash
from PAI.pai1.code.mac_utilities import Hmac


class IntegrityProcess:
    def __init__(self, file, hash_from_server, user_token):
        self.file = file
        self.hash_from_server = hash_from_server
        self.user_token = user_token
        self.log_file = '../logs/log'

    def check_integrity(self):
        hash_from_file = Hash(self.file).get_hash()

        integrity_hash_test = Hash(None, hash_from_file, self.hash_from_server).compare_hash()
        if integrity_hash_test:
            return [True, Hmac(self.user_token, self.file, None).get_mac()]
        else:
            f = open(self.log_file, "w")
            now = datetime.now()
            f.write("ERROR: %s\n Integrity test has been failure in the file %s" % (str(now), self.file))
            f.close()
            return [False, None]
