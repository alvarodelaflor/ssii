import unittest

from code.utilities.hash_utilities import Hash
from code.utilities.integrity_process import IntegrityProcess


class TestIntegrity(unittest.TestCase):

    def test_integrity_true(self):
        user_token = '1234567891011121314151617181921'
        file1 = 'files/file1'
        file2 = 'files/file1'

        hash2 = Hash(file2).get_hash()

        integrity_check = IntegrityProcess(file1, hash2, user_token, None).check_integrity()

        self.assertEqual(integrity_check[0], True)

    def test_integrity_false(self):
        user_token = '1234567891011121314151617181921'
        file1 = 'files/file1'
        file2 = 'files/file2'

        hash2 = Hash(file2).get_hash()

        integrity_check = IntegrityProcess(file1, hash2, user_token, None).check_integrity()

        self.assertEqual(integrity_check[0], False)


if __name__ == '__main__':
    unittest.main()
