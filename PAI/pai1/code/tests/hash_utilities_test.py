import unittest
from code.utilities.hash_utilities import Hash


class TestHash(unittest.TestCase):

    def test_integrity_true(self):
        file1 = 'files\\file1'
        file2 = 'files\\file1'

        hash2 = Hash(file2).get_hash()

        integrity_hash_test = Hash(file1, hash2).get_hash_and_compare()

        self.assertEqual(integrity_hash_test, True)

    def test_integrity_false(self):
        file1 = 'files\\file1'
        file2 = 'files\\file2'

        hash2 = Hash(file2).get_hash()

        integrity_hash_test = Hash(file1, hash2).get_hash_and_compare()

        self.assertEqual(integrity_hash_test, False)

    def test_equals_hash_true(self):
        file1 = 'files\\file1'
        file2 = 'files\\file1'

        hash1 = Hash(file1).get_hash()
        hash2 = Hash(file2).get_hash()

        integrity_hash_test = Hash(None, hash1, hash2).compare_hash()

        self.assertEqual(integrity_hash_test, True)

    def test_equals_hash_false(self):
        file1 = 'files\\file1'
        file2 = 'files\\file2'

        hash1 = Hash(file1).get_hash()
        hash2 = Hash(file2).get_hash()

        integrity_hash_test = Hash(None, hash1, hash2).compare_hash()

        self.assertEqual(integrity_hash_test, False)


if __name__ == '__main__':
    unittest.main()
