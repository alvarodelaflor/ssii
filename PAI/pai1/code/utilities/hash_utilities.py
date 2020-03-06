import hashlib


class Hash:
    def __init__(self, file=None, hash1=None, hash2=None):
        self.file = file
        self.hash1 = hash1
        self.hash2 = hash2

    def get_hash(self):
        if self.file is None:
            return None
        else:
            with open(self.file, 'rb') as f:
                body = f.read()

            res = hashlib.sha256(body).hexdigest()
            return res

    def compare_hash(self):
        if self.hash1 is not None and self.hash2 is not None:
            return self.hash1 == self.hash2
        else:
            return None

    def get_hash_and_compare(self):
        if self.file is not None and self.hash1 is not None:
            if self.hash2 is None:
                hash_generate = self.get_hash()
                return hash_generate == self.hash1
            else:
                print('Only use one hash to compare if use this method')
                return None
