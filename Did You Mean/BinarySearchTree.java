package didyoumean;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {
	
	static class BSTNode {
		BSTNode left;
		BSTNode right;
		String word;
		int count;
		BSTNode(String str) {
			word = str;
			left = null;
			right = null;
			count = 50;
		}
	}
	
	public BSTNode root;
	
	BinarySearchTree() {
		root = new BSTNode(" p");
	}
	
	public void insert(BSTNode node, String str, int rating) {
		if( node.count >= rating) {
			if( node.left == null) {
				node.left = new BSTNode(str);
				node.left.count = rating;
			}
			else {
				insert(node.left,str,rating);
			}
			
		}
		
		else if( node.count < rating) {
			if( node.right == null) {
				node.right = new BSTNode(str);
				node.right.count = rating;
			}
			else {
				insert(node.right,str,rating);
			}
		}
	}
	
	public void preTrav(BSTNode node) {
		BSTNode temp = node;
		if(temp != null){
		preTrav(temp.left);
		System.out.println(temp.word);
		preTrav(temp.right);
		}
	}
	
	public List<String> tree2list(BinarySearchTree tree) {
		List<String> list =new ArrayList<String>();
		BSTNode temp = tree.root;
		tree2list(temp, list);
		return list;
	}
	
	public void tree2list(BSTNode node, List<String> list) {
		if( node.left != null)
		tree2list(node.left, list);
		
		if( node.word != null)
		list.add( node.word);
		
		if( node.right != null)
		tree2list(node.right, list);
	}

}
