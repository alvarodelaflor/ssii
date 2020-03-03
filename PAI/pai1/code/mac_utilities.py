import hmac
import hashlib
from base64 import b64encode


class Hmac:
    def __init__(self, user_token, file, mac_to_compare=None):
        self.user_token = user_token
        self.file = file
        self.mac_to_compare = mac_to_compare

    def get_mac(self):
        digest_maker = hmac.new(
            bytes(self.user_token, 'utf-8'),
            b'',
            hashlib.sha256,
        )

        digest_maker.update(self.file.encode('utf-8'))

        digest = digest_maker.hexdigest()

        return digest

    def integrity_check(self):
        if self.mac_to_compare is not None:
            local_mac = self.get_mac()
            if local_mac == self.mac_to_compare:
                return True
            else:
                return False
        else:
            return None

