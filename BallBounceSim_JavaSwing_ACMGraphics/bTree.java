import acm.graphics.GObject;
import acm.graphics.GOval;

public class bTree {
	bNode root = null;
	private boolean notFinished;
	private double nextX;
	
	
	public bTree() {
		this.root = null;
		
	}
	
	public static class bNode {
		gBall iBall;
		bNode left;
		bNode right;
		
		public bNode(gBall iBall) {
			this.iBall = iBall;
			this.left = null;
			this.right = null;
			
		}
	}
	public void addNode(gBall iBall) {
		root = rNode(root, iBall);
	}
	
	private bNode rNode(bNode root, gBall iBall) {
			if (root == null) {
				bNode new_node = new bNode(iBall);
				root = new_node;
				return root;
			}
			else if (root.iBall.bSize > iBall.bSize){
				root.left = rNode(root.left, iBall);
			}
			else {
				root.right = rNode(root.right, iBall);
			}
			return root;
		}
	
	public void inorder() {
		traverse_inorder(root);
	}
	
		
	void traverse_inorder(bNode root) {
		if (root.left != null) traverse_inorder(root.left);
		System.out.println(root.iBall.bSize);
		if (root.right != null) traverse_inorder(root.right);
	}
	
	
	void traverse_move(bNode root) {
		if (root.left != null) traverse_move(root.left);
		
		double X = nextX;
		double Y = 600 - root.iBall.bSize;
		nextX = X + root.iBall.bSize;
		root.iBall.moveTo(X, Y);
		
		if (root.right != null) traverse_move(root.right);
	}
	
	void moveSort() {
		nextX = 0;
		traverse_move(root);
	}
	
	
	void traverse_running(bNode root) {
		if (root.left != null) traverse_running(root.left);
		if (root.iBall.isFinished == false) notFinished = true;
		if (root.right != null) traverse_running(root.right);
	}
	
	boolean isRunning() {
		notFinished = false;
		traverse_running(root);
		return notFinished;
	}
	
	public bTree re_sort(GObject gobj) {
		bTree new_tree = new bTree();
		new_tree(root, new_tree, gobj);
		return new_tree;
	 }

	public void new_tree(bNode root, bTree tree, GObject gobj) {
		 if (root.left != null) new_tree(root.left, tree, gobj);
		 if (root.iBall.myBall != gobj) tree.addNode(root.iBall);
		 if (root.right != null) new_tree(root.right, tree, gobj);
		 
	 }
	
	public void traverse_stop(bNode root, GObject gobj) {
		 if (root.left != null) traverse_stop(root.left, gobj);
		 if (root.iBall.myBall == gobj) root.iBall.stop();
		 if (root.right != null) traverse_stop(root.right, gobj);
	}
}


