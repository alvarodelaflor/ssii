from PAI.pai1.code.hash_utilities import Hash
from PAI.pai1.code.integrity_process import IntegrityProcess
from PAI.pai1.code.mac_utilities import Hmac
import unittest


class TestIntegrity(unittest.TestCase):

    def test_integrity_true(self):
        user_token = '1234567891011121314151617181921'
        file1 = 'files/file1'
        file2 = 'files/file1'

        hash1 = Hash(file1).get_hash()
        hash2 = Hash(file2).get_hash()

        integrity_check = IntegrityProcess(hash1, file1, hash2, user_token).check_integrity()

        self.assertEqual(integrity_check, True)


if __name__ == '__main__':
    unittest.main()
