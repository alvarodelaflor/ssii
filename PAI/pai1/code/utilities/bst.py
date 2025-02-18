import os
import sys

#from code.utilities.hash_utilities import Hash
from utilities.hash_utilities import Hash

sys.setrecursionlimit(10000)

COUNT = [10]

class node:
	def __init__(self,value=None):
		self.value=value
		self.left_child=None
		self.right_child=None
		self.parent=None # pointer to parent node in tree

class binary_search_tree:
	def __init__(self):
		self.root=None

	def insert(self,value):
		if self.root==None:
			self.root=node(value)
		else:
			self._insert(value,self.root)

	def _insert(self,value,cur_node):
		if value[0]<cur_node.value[0]:
			if cur_node.left_child==None:
				cur_node.left_child=node(value)
				cur_node.left_child.parent=cur_node # set parent
			else:
				self._insert(value,cur_node.left_child)
		elif value[0]>cur_node.value[0]:
			if cur_node.right_child==None:
				cur_node.right_child=node(value)
				cur_node.right_child.parent=cur_node # set parent
			else:
				self._insert(value,cur_node.right_child)
		else:
			print("Value already in tree!")

	def print_tree(self):
		if self.root!=None:
			self._print_tree(self.root)

	def _print_tree(self,cur_node):
		if cur_node!=None:
			self._print_tree(cur_node.left_child)
			print (str(cur_node.value))
			self._print_tree(cur_node.right_child)

	def height(self):
		if self.root!=None:
			return self._height(self.root,0)
		else:
			return 0

	def _height(self,cur_node,cur_height):
		if cur_node==None: return cur_height
		left_height=self._height(cur_node.left_child,cur_height+1)
		right_height=self._height(cur_node.right_child,cur_height+1)
		return max(left_height,right_height)

	def find(self,value):
		if self.root!=None:
			return self._find(value,self.root)
		else:
			return None

	def _find(self,value,cur_node):
		if value==cur_node.value:
			return cur_node
		elif value<cur_node.value and cur_node.left_child!=None:
			return self._find(value,cur_node.left_child)
		elif value>cur_node.value and cur_node.right_child!=None:
			return self._find(value,cur_node.right_child)

	def delete_value(self,value):
		return self.delete_node(self.find(value))

	def delete_node(self,node):

		## -----
		# Improvements since prior lesson

		# Protect against deleting a node not found in the tree
		if node==None or self.find(node.value)==None:
			print("Node to be deleted not found in the tree!")
			return None 
		## -----

		# returns the node with min value in tree rooted at input node
		def min_value_node(n):
			current=n
			while current.left_child!=None:
				current=current.left_child
			return current

		# returns the number of children for the specified node
		def num_children(n):
			num_children=0
			if n.left_child!=None: num_children+=1
			if n.right_child!=None: num_children+=1
			return num_children

		# get the parent of the node to be deleted
		node_parent=node.parent

		# get the number of children of the node to be deleted
		node_children=num_children(node)

		# break operation into different cases based on the
		# structure of the tree & node to be deleted

		# CASE 1 (node has no children)
		if node_children==0:

			# Added this if statement post-video, previously if you 
			# deleted the root node it would delete entire tree.
			if node_parent!=None:
				# remove reference to the node from the parent
				if node_parent.left_child==node:
					node_parent.left_child=None
				else:
					node_parent.right_child=None
			else:
				self.root=None

		# CASE 2 (node has a single child)
		if node_children==1:

			# get the single child node
			if node.left_child!=None:
				child=node.left_child
			else:
				child=node.right_child

			# Added this if statement post-video, previously if you 
			# deleted the root node it would delete entire tree.
			if node_parent!=None:
				# replace the node to be deleted with its child
				if node_parent.left_child==node:
					node_parent.left_child=child
				else:
					node_parent.right_child=child
			else:
				self.root=child

			# correct the parent pointer in node
			child.parent=node_parent

		# CASE 3 (node has two children)
		if node_children==2:

			# get the inorder successor of the deleted node
			successor=min_value_node(node.right_child)

			# copy the inorder successor's value to the node formerly
			# holding the value we wished to delete
			node.value=successor.value

			# delete the inorder successor now that it's value was
			# copied into the other node
			self.delete_node(successor)

	def search(self,value):
		if self.root!=None:
			return self._search(value,self.root)
		else:
			return False

	def _search(self,value,cur_node):
		if value==cur_node.value[0]:
			return cur_node.value
		elif value<cur_node.value[0] and cur_node.left_child.value[0]!=None:
			return self._search(value,cur_node.left_child)
		elif value>cur_node.value[0] and cur_node.right_child.value[0]!=None:
			return self._search(value,cur_node.right_child)
		return False 
    

	def fill_tree(tree,num_elems=100,max_int=1000):
		from random import randint
		for i in range (num_elems):
			cur_elem = randint(0,max_int)
			tree.insert(cur_elem)
		return tree

#tree = binary_search_tree()
#tree.insert("files/file0.txt")
#tree.print_tree()
	def generate(directorio,caso):
		if directorio == 0 and caso == '1':
			entries = os.scandir('servidores/caso1/servidor1')
		elif directorio == 1 and caso == '1':
			entries = os.scandir('servidores/caso1/servidor2')
		elif directorio == 2 and caso == '1':
			entries = os.scandir('servidores/caso1/servidor3')

		if directorio == 0 and caso == '2':
			entries = os.scandir('servidores/caso2/servidor1')
		elif directorio == 1 and caso == '2':
			entries = os.scandir('servidores/caso2/servidor2')
		elif directorio == 2 and caso == '2':
			entries = os.scandir('servidores/caso2/servidor3')

		if directorio == 0 and caso == '3':
			entries = os.scandir('servidores/caso3/servidor1')
		elif directorio == 1 and caso == '3':
			entries = os.scandir('servidores/caso3/servidor2')
		elif directorio == 2 and caso == '3':
			entries = os.scandir('servidores/caso3/servidor3')

		dictionary = dict()

		for entry in entries:
			file = open(entry.path,mode='r')
			all_of_it = file.read()
			hashFileContent = Hash(entry.path).get_hash()
			dictionary[entry.name] = hashFileContent
			file.close()

		tree = binary_search_tree()

		items = list(dictionary.items())
		length = int(len(items)/2)

		middle = items[length-1]

		dictionaryMiddle = {middle[0] : middle[1]}


		dictionaryCopy = dictionary.copy()
		dictionaryCopy.pop(middle[0])


		for dicts in dictionaryMiddle.items():
			tree.insert(dicts)
		for dicts in dictionaryCopy.items():
			tree.insert(dicts)

		def serialize(root):
			queue = [root]
			for node in queue:
				if not node:
					continue
				queue += [node.left_child, node.right_child]

			return ':'.join(
				map(lambda item: str(item.value) if item else '#', queue))

		s = serialize(tree.root)
		if directorio == 0 and caso == '1':
			txt = open ('servidores/caso1/tree1/binaryTree1.txt','w', encoding="utf-8")		
		elif directorio == 1 and caso == '1':
			txt = open ('servidores/caso1/tree2/binaryTree2.txt','w', encoding="utf-8")		
		elif directorio == 2 and caso == '1':
			txt = open ('servidores/caso1/tree3/binaryTree3.txt','w', encoding="utf-8")	

		if directorio == 0 and caso == '2':
			txt = open ('servidores/caso2/tree1/binaryTree1.txt','w', encoding="utf-8")		
		elif directorio == 1 and caso == '2':
			txt = open ('servidores/caso2/tree2/binaryTree2.txt','w', encoding="utf-8")		
		elif directorio == 2 and caso == '2':
			txt = open ('servidores/caso2/tree3/binaryTree3.txt','w', encoding="utf-8")	

		if directorio == 0 and caso == '3':
			txt = open ('servidores/caso3/tree1/binaryTree1.txt','w', encoding="utf-8")		
		elif directorio == 1 and caso == '3':
			txt = open ('servidores/caso3/tree2/binaryTree2.txt','w', encoding="utf-8")		
		elif directorio == 2 and caso == '3':
			txt = open ('servidores/caso3/tree3/binaryTree3.txt','w', encoding="utf-8")		
		txt.write(s)

		return tree

	def deserialize(directorio,caso):
		if directorio == 0 and caso == '1':
			data = open("servidores/caso1/tree1/binaryTree1.txt").read()
		elif directorio == 1 and caso == '1':
			data = open("servidores/caso1/tree2/binaryTree2.txt").read()
		elif directorio == 2 and caso == '1':
			data = open("servidores/caso1/tree3/binaryTree3.txt").read()

		if directorio == 0 and caso == '2':
			data = open("servidores/caso2/tree1/binaryTree1.txt").read()
		elif directorio == 1 and caso == '2':
			data = open("servidores/caso2/tree2/binaryTree2.txt").read()
		elif directorio == 2 and caso == '2':
			data = open("servidores/caso2/tree3/binaryTree3.txt").read()

		if directorio == 0 and caso == '3':
			data = open("servidores/caso3/tree1/binaryTree1.txt").read()
		elif directorio == 1 and caso == '3':
			data = open("servidores/caso3/tree2/binaryTree2.txt").read()
		elif directorio == 2 and caso == '3':
			data = open("servidores/caso3/tree3/binaryTree3.txt").read()

		parts = data.split(':')
		tree1 = binary_search_tree()
		for p in parts:
			if p != "#":
				p_aux = p.split("'")
				res = []
				file_name = p_aux[1]
				hash_file = p_aux[3]  
				res.append(file_name)
				res.append(hash_file)
				tree1.insert(res)
		return tree1
		

		