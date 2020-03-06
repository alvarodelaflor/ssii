from datetime import *
from PAI.pai1.code.hash_utilities import Hash
from PAI.pai1.code.mac_utilities import Hmac


class IntegrityProcess:
    def __init__(self, file, hash_from_server, user_token, tree_files=None):
        self.file = file
        self.hash_from_server = hash_from_server
        self.user_token = user_token
        self.log_file = '../logs/log'
        self.tree_files = tree_files

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

    def check_integrity_servers(self):
        if self.tree_files is None or len(self.tree_files) < 0:
            return None
        else:
            failures = []
            for tree_file in self.tree_files:
                self.file = tree_file[0]
                self.hash_from_server = tree_file[1]
                check = self.check_integrity()
                if not check[0]:
                    failures.append(self.file)
            return failures
