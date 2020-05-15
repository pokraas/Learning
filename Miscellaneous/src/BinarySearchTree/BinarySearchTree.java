package BinarySearchTree;

/**
 * An unbalanced (not every node has two children)
 * binary (every node has two or less children)
 * search tree.
 */
public class BinarySearchTree<E extends Comparable<E>> {
	
	private TreeNode<E> root = null;
	
	public E getRootData() {
		return root.getData();
	}
	/**
	 * A recursive function to add a new element
	 * @param e The new element to be added
	 * @param node Node after which the new element is added
	 */
	private void addAfterNode(E e,TreeNode<E> node) {
		if (e.compareTo(node.getData())<0) {
			if (node.getLeft()==null) node.addLeftNode(e);
			else addAfterNode(e,node.getLeft());
		}
		if (e.compareTo(node.getData())>0) {
			if (node.getRight()==null) node.addRightNode(e);
			else addAfterNode(e,node.getRight());
		}
	}
	
	/**
	 * Adds a new element to the tree
	 * @param e The new element to be added
	 * @throws NullPointerException if the element to be added (e) is null
	 */
	public void insert(E e) {
		if (e==null) throw new NullPointerException("Cannot insert a null element into the tree!");
		if (root==null) {
			root = new TreeNode<E>(e);
			return;
		}
		addAfterNode(e,root);
	}
	
	//In-order traversal of the tree
	private String s="";
	private void toString(TreeNode<E> node) {
		if (node==null) return;
		else {
			toString(node.getLeft());
			s=s+node.getData().toString()+" ";
			toString(node.getRight());
		}
	}
	//Printing the tree with branches
	private void printBinaryTree(TreeNode<E> root, int level){
	    if(root==null)
	         return;
	    printBinaryTree(root.right, level+1);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+root.getData());
	    }
	    else
	        System.out.println(root.getData());
	    printBinaryTree(root.left, level+1);
	} 
	@Override
	public String toString() {	
		printBinaryTree(root,0);
		toString(root);
		String toReturn = "In-order traversal: "+s+" (should be in growing order!)";
		s="";
		return toReturn;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();
		testBST.insert(20);
		for (int i=0; i<9;i+=2) {
			testBST.insert(i);
		}
		testBST.root.addRightNode(30);
		testBST.root.getRight().addRightNode(40);
		testBST.insert(25);
		System.out.println(testBST+"\n");
		testBST.insert(35);
		testBST.insert(5);
		System.out.println(testBST);
	}
	
	class TreeNode<E extends Comparable<E>>{
		private E data;
		private TreeNode<E> left;
		private TreeNode<E> right;
		public TreeNode(E data){
			this.setData(data);
		}
		
		public E getData() {
			return data;
		}
		public void setData(E data) {
			this.data = data;
		}

		public TreeNode<E> getLeft() {
			return left;
		}
		
		public void addLeftNode(E leftData) {
			this.left = new TreeNode<E>(leftData);
		}

		public TreeNode<E> getRight() {
			return right;
		}

		public void addRightNode(E rightData) {
			this.right = new TreeNode<E>(rightData);
		}
	}
}
