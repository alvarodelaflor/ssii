from PAI.pai1.code.mac_utilities import Hmac
import unittest


class TestHmac(unittest.TestCase):

    def test_integrity_true(self):
        user_token = '1234567891011121314151617181921'
        message = 'elalvaroeselmejor'

        hmac = Hmac(user_token, message)
        mac_to_compare = hmac.get_mac()

        hmac = Hmac(user_token, message, mac_to_compare)
        mac = hmac.get_mac()

        integrity_check = Hmac(user_token, message, mac).integrity_check()

        self.assertEqual(integrity_check, True)

    def test_integrity_false(self):
        user_token = '1234567891011121314151617181921'
        message = 'elalvaroeselmejor'

        hmac = Hmac(user_token, message)
        mac_to_compare = hmac.get_mac()

        hmac = Hmac(user_token, message, mac_to_compare)
        mac = hmac.get_mac() + '9'
        integrity_check = Hmac(user_token, message, mac).integrity_check()

        self.assertEqual(integrity_check, False)

    def test_integrity_method_bad_construct(self):
        user_token = '1234567891011121314151617181921'
        message = 'elalvaroeselmejor'

        integrity_check = Hmac(user_token, message).integrity_check()

        self.assertEqual(integrity_check, None)


if __name__ == '__main__':
    unittest.main()
