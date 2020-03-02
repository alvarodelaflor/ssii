import hashlib 
import codecs

transactions = ['a', 'b', 'c', 'd', 'e']
leaf_hash = []

class Merkleroot(object):
    def __init__(self):
        pass
    
    def doubleSha256(input):
        return hashlib.sha256(hashlib.sha256(input).hexdigest()).hexdigest() 
    
    def find_root(self, leaf_hash):
        hash = []
        hash2 = []
        if len(leaf_hash) % 2 != 0:                             
            leaf_hash.extend(leaf_hash[-1:])
        
        for leaf in sorted(leaf_hash):                         
            hash.append(leaf)
            if len(hash) % 2 == 0:
                sum = hash[0]+hash[1]
                hash_aux = codecs.encode(codecs.decode(hashlib.sha256(sum.encode('utf-8')).hexdigest(), 'hex'), 'base64').decode().replace('\n', '')   
                hash2.append(hash_aux)   
                hash == []                                    
        if len(hash2) == 1:  
            print(hash2)                                 
            return hash2
        else:
            return self.find_root(hash2)                 


for trans in transactions:
    trans_hash = codecs.encode(codecs.decode(hashlib.sha256(trans.encode('utf-8')).hexdigest(), 'hex'), 'base64').decode().replace('\n', '')
    leaf_hash.append(trans_hash)

mr = Merkleroot()
mr.find_root(leaf_hash)