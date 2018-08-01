/**
 * A class that implements the B-Tree. 
 * @author Vecheka Chhourn
 * @param T generic type
 * @version 2.0
 */
public class BTree<T extends Comparable<T>> {
	
	/** Overall root of the tree.*/
	protected TreeNode<T> root;
	/** Max size of node.*/
	private Integer maxSize;
	/** Minimum size of a node.*/
	private Integer minSize;
	
	/**
	 * A constructor that takes a minimum degree of the B-Tree
	 * and initialize root of the tree.
	 * @param theDegree minimum degree of the tree 
	 */
	public BTree(final int theDegree) {
		root = null;
		minSize = theDegree;
		maxSize = (2 * theDegree) - 1;
	}
	
	
	/**
	 * Insert new data to the tree node.
	 * @param theData data to be inserted
	 */
	public void insert(final T theData) {
		
		
		if (root != null && root.size == maxSize) {
			// split the root
			split(root);
		} 
		
		if (root != null && root.size > 1 && !isLeaf(root)) {
			// find the right node to insert to...
			add(root, theData);
		} else {
			
			if (root == null) {
				root = new TreeNode<T>(theData);
				root.size++;
				return;
			} 
			TreeNode<T> current = root;
			while (current != null) {
				if (current.size < maxSize && isLeaf(current)) {
					addAndSort(current, new TreeNode<>(theData));
					break;
				} else if (current.size == maxSize) {
					// split
					split(current);
					TreeNode<T> parent = findNode(current.parent.data);
					add(parent, theData);
					break;
				} else if (current.data.compareTo(theData) == 1) {
					current = current.left;
				} else if (current.data.compareTo(theData) == -1) {
					current = current.right;
				}
			}
			
		}
	}
	


	/**
	 * Helper method to add new data to the right node in the tree.
	 * @param theRoot root of the tree
	 * @param theData new data to be inserted
	 */
	private void add(final TreeNode<T> theRoot, final T theData) {
		TreeNode<T> current = theRoot;
		while (current != null) {
			if (current.data.compareTo(theData) == -1 && current.next != null) {
				current = current.next;
				
			} else break;
		}
		
		while (current != null) {
			if (current.size < maxSize && isLeaf(current)) {
				addAndSort(current, new TreeNode<>(theData));
				break;
			} else if (current.size == maxSize) {
				split(current);
				add(current.parent, theData);
				break;
			} else if (current.data.compareTo(theData) == 1) {
				current = current.left;
			} else if (current.data.compareTo(theData) == -1) {
				current = current.right;
			}
		}
		
	}


	/**
	 * Split the node, promoting the middle node as a parent, and left of 
	 * the middle node as a left child, and right of the middle node as a right child.
	 * @param theRoot root of the tree
	 */
	private void split(final TreeNode<T> theRoot) {
		final int mid = theRoot.size / 2;
		TreeNode<T> midElement = theRoot;
		TreeNode<T> leftChildren = null;
		// left children of the node
		for (int i = 0; i < mid; i++) {
			if (leftChildren == null) {
				leftChildren = new TreeNode<T>(midElement.data, midElement.left, midElement.right);
				leftChildren.parent = midElement;
				leftChildren.size = 1;
			} else {
				addAndSort(leftChildren, new TreeNode<>(midElement.data, midElement.left, midElement.right));
			}
			midElement = midElement.next;
		}
		leftChildren.parent = midElement;

		// right children of the node
		TreeNode<T> current = midElement;
		TreeNode<T> rightChildren = null;
		while (current.next != null) {
			if (rightChildren == null) {
				rightChildren = new TreeNode<T>(current.next.data, current.next.left,
						current.next.right);
				rightChildren.parent = midElement;
				rightChildren.size = 1;
			} else {
				addAndSort(rightChildren, new TreeNode<T>(current.next.data, current.next.left,
						current.next.right));
			}
			
			current = current.next;
		}
		rightChildren.parent = midElement;
		midElement.left = leftChildren;
		midElement.right = rightChildren;
		midElement.parent = theRoot.parent;
		midElement.next = null;
		
		if (theRoot == root) {
			root = new TreeNode<T>(midElement.data, leftChildren, rightChildren);
			midElement.parent = null;
			leftChildren.parent = root;
			rightChildren.parent = root;
//			root.left.parent = root;
//			root.right.parent = root;
			root.size++;
		} else {
			if (theRoot.parent != root) {
				if (theRoot.data.compareTo(root.data) == 1) {
					addAndSort(root.right, new TreeNode<>(midElement.data, leftChildren, rightChildren));
				} else {
					addAndSort(root.left, new TreeNode<>(midElement.data, leftChildren, rightChildren));
				}
			} else {
				midElement.parent = root;
				addAndSort(theRoot.parent, new TreeNode<>(midElement.data, leftChildren, rightChildren));
			}
		}

	}
	
	/** 
	 * Add and sort data in a given tree node.
	 * @param theRoot root of the tree
	 * @param theNewNode new node to be inserted
	 */
	private void addAndSort(final TreeNode<T> theRoot, final TreeNode<T> theNewNode) {
		TreeNode<T> current = theRoot;
		if (theRoot.data.compareTo(theNewNode.data) == 1) {
			TreeNode<T> temp = new TreeNode<>(current.data, theNewNode.right, current.right
					, current.next);
			temp.parent = current.parent;
			theNewNode.parent = theRoot.parent;
			theNewNode.next = temp;
			current.data = theNewNode.data;
			current.left = theNewNode.left;
			current.right = theNewNode.right;
			if (current.left != null) current.left.parent = current;
			if (current.right != null) current.right.parent = current;
			current.next = temp;
			theRoot.size++;
		} else {
			for (int i = 1; i < theRoot.size; i++) {
				if (current.next.data.compareTo(theNewNode.data) == -1) {
					current = current.next;
				} else break;
			}
			if (current.next != null) {
				theNewNode.next = current.next;
			}
			current.next = theNewNode;
			current.next.parent = theRoot.parent;
			current.right = theNewNode.left;
//			if (current.right != null) current.right.parent = theNewNode;
			theRoot.size++;
			current.next.size = theRoot.size;
		}
	}
	
	/**
	 * Delete a given data from the tree.
	 * @param theData, data to be deleted
	 */
	public void delete(final T theData) {
		if (root == null) return;
		
		TreeNode<T> deletedNode = findNode(theData);
		if (deletedNode == null) return;

		
		// simple case when there is only root in the tree
		if (isLeaf(root)) {
			remove(root, theData);
			return;
		}
		
		if (root.size == 1 && root.data.compareTo(theData) == 0) {
			removeRootNode(theData);
			return;
		}
		
		
		// if the node has at least t keys, and is a leaf, remove it.
		if (isLeaf(deletedNode)) {
			removeLeafNode(deletedNode, theData);
		} else {
			// remove internal node
			removeInternalNode(deletedNode, theData);
		}
		

	}
	
	
	/**
	 * Remove a root of the tree.
	 * @param theData root data
	 */
	private void removeRootNode(final T theData) {
		TreeNode<T> current = root.left;
		T max = findMax(current);
		current = findInternalNode(current, max);
		while (current.right != null) {
			current = current.right;
		}
		max = findMax(current);
		removeLeafNode(current, max); 
		current = findNode(theData);
		current.data = max;
		
	}


	/**
	 * Remove internal node from the tree.
	 * Pre-Condition:
	 * 	1. 
	 * @param theNode node to be deleted
	 * @param theData data connected to the deleted node
	 */
	private void removeInternalNode(final TreeNode<T> theNode, final T theData) {
		
		TreeNode<T> internalNode = findInternalNode(theNode, theData);
		T data = null;
		TreeNode<T> children = null;
		boolean hasChildren = false;
		if (internalNode.left.size >= minSize) {
			data = findMax(internalNode.left);
			children = internalNode.left;
			hasChildren = true;
		} else if (internalNode.right.size >= minSize) {
			data = internalNode.right.data;
			children = internalNode.right;
			hasChildren = true;
		}
		if (hasChildren) {
			internalNode.data = data;
			remove(children, data);
			updateParent(internalNode.left, internalNode);
			updateParent(internalNode.right, internalNode);
			return;
		} else {
			T temp = internalNode.data;
			internalNode.data = internalNode.right.data;
			internalNode.right.data = temp;
			TreeNode<T> deletedNode = internalNode.right;
			updateParent(internalNode.right, internalNode);
			removeLeafNode(deletedNode, temp);
			
		}
	}


	/**
	 * Remove the leaf node from the tree.
	 * Pre-conditions: 
	 * 	1. If sibling's node have at least t keys, borrow one from sibling.
	 * 	   Then, make the borrow node the parent, and the previous parent the child.
	 *  2. If sibling's node doesn't have enough t keys, merge parent's node
	 *     and sibling's node, make parent's sibling the new root, and the root the parent of the 
	 *     merge child with the left/right child of the parent's sibling the other sibling of the merge
	 *     child.
	 *  3. If sibling's node doesn't have enough t keys, merge parent's node and sibling's node.
	 *     If the parent's sibling doesn't have enough t keys, merge the parent's sibling with the root
	 *     and make the merge sibling and the children of the parent's siblings their children. 
	 * @param theNode node to be deleted
	 * @param theData connected to the deleted node
	 */
	private void removeLeafNode(final TreeNode<T> theNode, final T theData) {
		
		if (theNode.size >= minSize) {
			remove(theNode, theData);
			return;
		}
		
		TreeNode<T> parent = findNode(theNode.parent.data);
		parent = findInternalNode(parent, theNode.parent.data);
		TreeNode<T> sibling = null;
		boolean isLeft = true;
		// left-branch of the tree
		if (parent.left == theNode) {
			sibling = parent.right;
		} else {
			sibling = parent.left;
			isLeft = false;
		}
		
		
		// if sibling has enough node, borrow one.
		if (sibling.size >= minSize) {
			T parentData = parent.data;
			T newData = null;
			if(isLeft) {
				newData = sibling.data;
			} else {
				newData = findMax(sibling);
			}
			parent.data = newData;
			addAndSort(theNode, new TreeNode<>(parentData));
			updateParent(parent.left, parent);
			updateParent(parent.right, parent);
			remove(theNode, theData);
			remove(sibling, newData);
		} else if (parent.size >= minSize) { // if parent has enough node, merge parent with sibling
			remove(theNode, theData);
			T parentData = parent.data;
			parent = findNode(parentData);
			addAndSort(sibling, new TreeNode<>(parentData));
			remove(parent, parentData);
			// update parent
			updateParent(sibling, parent);
			if (theData.compareTo(parent.data) == 1) {
				parent.right = sibling;
			} else {
				parent.left = sibling;
			}
			
		} else if (parent.parent != null) {
			TreeNode<T> parentSibling = null;
			
			if (theData.compareTo(root.data) == 1) {
				parentSibling = parent.parent.left;
				isLeft = true;
			} else {
				parentSibling = parent.parent.right;
				isLeft = false;
			}
			
			remove(theNode, theData);
			addAndSort(sibling, new TreeNode<>(parent.data));
			if (parentSibling.size >= minSize) {
				
				if (isLeft) {
					T max = findMax(parentSibling);
					TreeNode<T> temp = findInternalNode(parentSibling, max);
					parent.data = parent.parent.data;
					parent.right = sibling;
					parent.left = temp.right;
					updateParent(sibling, parent);
					updateParent(parent.left, parent);
					parent.parent.data = max;
					parentSibling.right = temp.left;
					remove(parentSibling, max);
				} else {
					parent.data = parent.parent.data;
					parent.left = sibling;
					parent.right = parentSibling.left;
					updateParent(sibling, parent);
					updateParent(parent.right, parent);
					parent.parent.data = parentSibling.data;
					parentSibling.left = parentSibling.right;
					remove(parentSibling, parentSibling.data);
				}
			} else {
				
				if (isLeft) {
					parent.parent.right = sibling;
					addAndSort(parent.parent, parentSibling);
					parent.parent.next.size = parent.parent.size;
					updateParent(sibling, parent.parent.next);
				} else {
					addAndSort(parent.parent, parentSibling);
					parent.parent.left = sibling;
					parent.parent.right = parentSibling.left;
					updateParent(sibling, parent.parent);
				}
			}	
		} else {
			remove(theNode, theData);
			addAndSort(sibling, new TreeNode<T>(parent.data));
			root = new TreeNode<>(sibling.data);
			if (sibling.next != null) {
				root.next = sibling.next;
				root.next.size = sibling.size;
			}
			root.size = sibling.size;
		}
		
	}
	
	/**
	 * Helper method to update parent of a node.
	 * @param theNode node to be updated
	 * @param theParent new parent of the node
	 */
	private void updateParent(final TreeNode<T> theNode, final TreeNode<T> theParent) {
		TreeNode<T> current = theNode;
		while (current != null) {
			current.parent = theParent;
			current = current.next;
		}
		
	}


	/**
	 * Find the right internal node to a given data.
	 * @param theNode node use to search
	 * @param theData data relating to the node
	 * @return
	 */
	private TreeNode<T> findInternalNode(final TreeNode<T> theNode, final T theData) {
		TreeNode<T> current = theNode;
		while (current != null) {
			if (current.data.compareTo(theData) == 0) break;
			else current = current.next;
		}
		return current;
	}


	/**
	 * Find the max data in a given node.
	 * @param theNode node of the tree
	 * @return a max value of the given node
	 */
	private T findMax(final TreeNode<T> theNode) {
		TreeNode<T> current = theNode;
		while (current.next != null) {
			current = current.next;
		}
		return current.data;
	}


	/**
	 * Helper method to delete a node from the tree.
	 * @param theNode to be deleted
	 * @param theData in the given node to be deleted
	 */
	private void remove(final TreeNode<T> theNode, final T theData) {
		if (root == null) return;
		
		TreeNode<T> current = theNode;
		if (current.data.compareTo(theData) == 0) {
			if (current.next != null) {
				current.data = current.next.data;
				if (current.next.right != null) current.right = current.next.right;
				current.next = current.next.next;
			}
			else current.data = null;
			
			theNode.size--;
		} else {
			while (current.next != null) {
				if (current.next.data.compareTo(theData) == 0) {
//					current.next = null;
					current.next = current.next.next;
					break;
				}
				current = current.next;
			}
			theNode.size--;
		}

	}


	/**
	 * Find a node in the tree with the given data.
	 * @param theData data of the node.
	 * @return the node with the given data
	 */
	private TreeNode<T> findNode(final T theData) {
		if (root == null) return null;
		
		TreeNode<T> current = root;
		TreeNode<T> node = null;
		boolean hasFound = false;
		while (current != null && !hasFound) {
			if (current.size > 1) {
				TreeNode<T> temp = current;
				while (temp != null) {
					if (temp.data.compareTo(theData) == 0) {
						node = current;
						hasFound = true;
						break;
					} 
					temp = temp.next;
				}
				if (!hasFound) {
					while (current.next != null) {
						if (current.next.data.compareTo(theData) == -1) {
							current = current.next;
						} else break;
					}
				}
				
			} 
			if (current.data.compareTo(theData) == 0) {
				node = current;
				break;
			} else if (current.data.compareTo(theData) == 1) {
				current = current.left;
			} else if (current.data.compareTo(theData) == -1) {
				current = current.right;
			} 
		}
		
		return node;
	}
	
	
	
	/** 
	 * Print pre-order of the tree.
	 */
	public void printPreOrder() {
		TreeNode<T> current = root;
		while (current != null) {
			if (current.data != null) System.out.print(current.data + "rt: ");
			printPreOrder(current.left, "L ");
			printPreOrder(current.right, "R ");
			System.out.println();
			current = current.next;
		}
	} 
	
	/** 
	 * Helper method to print tree in pre-order traversal.
	 * @param theRoot root of the tree
	 * @param theChildrenSide specify left or right children
	 */
	private void printPreOrder(final TreeNode<T> theRoot, final String theChildrenSide) {
		TreeNode<T> current = theRoot;
		while (current != null) {
			if (current.data != null) System.out.print(current.data + theChildrenSide);
			printPreOrder(current.left, "l ");
			printPreOrder(current.right, "r ");
			current = current.next;
		}
		
	}


	/** 
	 * Determine if it has children or not.
	 * @param theRoot root of the tree
	 * @return tree if it's a leaf.
	 */
	private boolean isLeaf(final TreeNode<T> theRoot) {
		return theRoot.left == null && theRoot.right == null;
	}
}



/**
 * A class to construct a node in the tree.
 * @param <T> Generic type
 */
class TreeNode<T> {
	
	/** Value of a node.*/
	protected T data;
	/** Size of each node.*/
	protected Integer size;
	/** Pointer to next data.*/
	protected TreeNode<T> next;
	/** Left child.*/
	protected TreeNode<T> left;
	/** Right child.*/
	protected TreeNode<T> right;
	/** Parent of the node.*/
	protected TreeNode<T> parent;
	
	
	/** 
	 * Constructor that takes an object, left root, and right root.
	 * @param theData to add to the tree
	 * @param theLeft left root
	 * @param theRight right root
	 */
	public TreeNode(final T theData, final TreeNode<T> theLeft, 
			final TreeNode<T> theRight, final TreeNode<T> theNext) {
		data = theData;
		left = theLeft;
		right = theRight;
		next = theNext;
		parent = null;
		size =  0;
	}
	
	/** 
	 * Constructor that takes an object, left root, and right root.
	 * @param theData to add to the tree
	 * @param theLeft left root
	 * @param theRight right root
	 */
	public TreeNode(final T theData, final TreeNode<T> theLeft, 
			final TreeNode<T> theRight) {
		data = theData;
		left = theLeft;
		right = theRight;
		next = null;
		parent = null;
		size =  0;
	}
	
	/**
	 * Copy constructor that takes an object and add to the tree, 
	 * and initialize both children to null.
	 * @param theData to add to the tree
	 */
	public TreeNode(final T theData) {
		this(theData, null, null, null);
	}

}