import hmac
import hashlib


class Hmac:
    def __init__(self, user_token, file, mac_to_compare=None):
        self.user_token = user_token
        self.file = file
        self.mac_to_compare = mac_to_compare

    ''' This method returns a hexadecimal string using hmac with sha256 from the token and the given file (using your local address)'''
    def get_mac(self):
        with open(self.file, 'rb') as f:
            body = f.read()

        digest_maker = hmac.new(
            bytes(self.user_token, 'utf-8'),
            body,
            hashlib.sha256,
        )

        digest_maker.update(self.file.encode('utf-8'))
        digest = digest_maker.hexdigest()

        return digest

    ''' 
        This method compares the given MAC with a new one generated from the given file and token
        Returns True if it passes the test, False if it doesn't pass and None if there is an error in the construction
    '''
    def integrity_check(self):
        if self.mac_to_compare is not None:
            local_mac = self.get_mac()
            if local_mac == self.mac_to_compare:
                return True
            else:
                return False
        else:
            return None

