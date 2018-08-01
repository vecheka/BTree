
public class Main {
	
	/** Tree's minimum degree.*/
	private static final int TREE_DEGREE = 2;
	
	
	public static void main(String[] args) {
		final BTree<Integer> tree = new BTree<>(TREE_DEGREE);
		
		
		
		tree.insert(16);
		tree.insert(5);
		tree.insert(22);
		tree.insert(45);
		tree.insert(2);
		tree.insert(10);
		tree.insert(18);
		tree.insert(30);
		tree.insert(50);
		tree.insert(12);
		tree.insert(31);
		tree.insert(11);
		tree.insert(13);
//		tree.insert(4);
//		tree.insert(7);
//		tree.insert(60);
//		tree.insert(70);
		
		
//		tree.delete(16);
//		tree.delete(13);
//		tree.delete(12);
//		tree.delete(11);
//		tree.delete(22);
//		tree.delete(18);
//		tree.delete(10);
//		tree.delete(31);
//		tree.delete(12);
//		tree.delete(45);
//		tree.delete(16);
//		tree.delete(5);
//		tree.delete(31);
//		tree.delete(11);
//		tree.delete(2);
//		tree.delete(13);
//		tree.delete(50);
		
		
		// testing deletion of leaf node
//		tree.delete(2);
//		tree.delete(30);
//		tree.delete(18);
//		tree.delete(5);
//		tree.delete(10);
//		tree.delete(22);
//		tree.delete(12);
//		tree.delete(50);
//		tree.delete(16);
//		tree.delete(31);
//		tree.delete(45);

		
		
//		System.out.println(tree.root.left.parent.data);
		
		
		tree.printPreOrder();

	}

}
