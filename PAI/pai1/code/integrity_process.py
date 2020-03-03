from PAI.pai1.code.hash_utilities import Hash
from PAI.pai1.code.mac_utilities import Hmac


class IntegrityProcess:
    def __init__(self, hash_user, file, hash_server, user_token):
        self.hash_user = hash_user
        self.file = file
        self.hash_server = hash_server
        self.user_token = user_token

    def check_integrity(self):
        file = 'files\\file1'

        hash = Hash(file).get_hash()

        integrity_hash_test = Hash(file, hash, None).compare_hash()
        if integrity_hash_test:
            return Hmac(self.user_token, file, None).get_mac()
        else:
            print('Error of hash integrity')
            return False
