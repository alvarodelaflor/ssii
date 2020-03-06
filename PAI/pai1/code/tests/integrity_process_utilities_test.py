from PAI.pai1.code.hash_utilities import Hash
from PAI.pai1.code.integrity_process import IntegrityProcess
import unittest


class TestIntegrity(unittest.TestCase):

    def test_integrity_true(self):
        user_token = '1234567891011121314151617181921'
        file1 = 'files/file1'
        file2 = 'files/file1'

        hash2 = Hash(file2).get_hash()

        integrity_check = IntegrityProcess(file1, hash2, user_token).check_integrity()

        self.assertEqual(integrity_check[0], True)

    def test_integrity_false(self):
        user_token = '1234567891011121314151617181921'
        file1 = 'files/file1'
        file2 = 'files/file2'

        hash2 = Hash(file2).get_hash()

        integrity_check = IntegrityProcess(file1, hash2, user_token).check_integrity()

        self.assertEqual(integrity_check[0], False)

    def test_integrity_server_true(self):
        user_token = '1234567891011121314151617181921'
        tree_files = []
        server1 = ['dirección1', 'hash1']
        server2 = ['dirección2', 'hash2']
        server3 = ['dirección3', 'hash3']
        tree_files.append(server1)
        tree_files.append(server2)
        tree_files.append(server3)

        integrity_check = IntegrityProcess(None, None, user_token, tree_files).check_integrity_servers()

        if integrity_check is not None:
            for failure in integrity_check:
                print(failure)

        self.assertEqual(integrity_check, [])


if __name__ == '__main__':
    unittest.main()
