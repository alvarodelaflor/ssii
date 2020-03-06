import unittest

from code.utilities.mac_utilities import Hmac


class TestHmac(unittest.TestCase):

    def test_integrity_true(self):
        user_token = '1234567891011121314151617181921'
        file = 'files/file1'

        hmac1 = Hmac(user_token, file)
        mac1 = hmac1.get_mac()

        hmac2 = Hmac(user_token, file, mac1)
        integrity_check = Hmac(user_token, file, mac1).integrity_check()

        self.assertEqual(integrity_check, True)

    def test_integrity_diferent_file(self):
        user_token = '1234567891011121314151617181921'
        file1 = 'files\\file1'
        file2 = 'files\\file2'

        hmac1 = Hmac(user_token, file1)
        mac1 = hmac1.get_mac()

        hmac2 = Hmac(user_token, file2, mac1)
        integrity_check = hmac2.integrity_check()

        self.assertEqual(integrity_check, False)

    def test_integrity_different_user(self):
        user_token1 = '2234567891011121314151617181921'
        user_token2 = '24232524535t6356354234234234245'

        file = 'files/file1'

        hmac1 = Hmac(user_token1, file)
        mac1 = hmac1.get_mac()

        hmac2 = Hmac(user_token2, file, mac1)
        integrity_check = hmac2.integrity_check()

        self.assertEqual(integrity_check, False)

    def test_integrity_method_bad_construct(self):
        user_token = '1234567891011121314151617181921'
        file = 'files/file1'

        integrity_check = Hmac(user_token, file).integrity_check()

        self.assertEqual(integrity_check, None)


if __name__ == '__main__':
    unittest.main()
