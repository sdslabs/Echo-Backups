package didyoumean;

import java.util.ArrayList;
import java.util.List;


	public class TernaryTree {
		
		// -----------------definition of ternary node------------------------
		static class TernaryNode {
			public TernaryNode high;
			public TernaryNode low;
			public TernaryNode middle;
			public String str;
			Character chr;
			int count;
			int indexCount;
			public TernaryNode(char value) {
				chr = value;
				high = null;
				low = null;
				middle = null;
				count = 0;
				indexCount = 0;
			}
		}
		
		//---------------------defining root of tree--------------------------
		public TernaryNode root;
	
		//---------------------TernaryTree constructor-------------------------
		public TernaryTree() {
			root = new TernaryNode('e');
		}
		
		//--------------------inserting a word in the tree with indexCount and count--------
		public void insertNode(String word, int indexCount, int count) {
			
			TernaryNode temp = root;
			TernaryNode tempPrev = temp;
			int i=0;
			while( i < word.length() ) {
				if(temp != null) {
					if( temp.chr.compareTo( word.charAt(i)) < 0 ) {
						tempPrev = temp;
						temp = temp.high;
					}
					
					else if( temp.chr.compareTo( word.charAt(i)) > 0 ) {
						tempPrev = temp;
						temp = temp.low;
					}
					
					else if( temp.chr.equals( word.charAt(i)) ) {
						tempPrev = temp;
						temp = temp.middle;
						i++;
					}
				}
				
				else {
					TernaryNode newNode = new TernaryNode( word.charAt(i) );
					TernaryNode prevTemp = tempPrev;
					while( tempPrev != null) {
						if( tempPrev.chr.equals(word.charAt(i)) ) {
							prevTemp = tempPrev;
							tempPrev = tempPrev.middle;
						}
						
						else if( tempPrev.chr.compareTo( word.charAt(i) ) < 0 ) {
							prevTemp = tempPrev;
							tempPrev = tempPrev.high;
						}
						
						else if( tempPrev.chr.compareTo( word.charAt(i) ) > 0 ) {
							prevTemp = tempPrev;
							tempPrev = tempPrev.low;
						}
						
					}
						if( prevTemp.chr.equals(word.charAt(i)) ) {
							prevTemp.middle = newNode;
							prevTemp = prevTemp.middle;
						}
						
						else if( prevTemp.chr.compareTo( word.charAt(i) ) < 0 ) {
							prevTemp.high = newNode;
							prevTemp = prevTemp.high;
						}
						
						else if( prevTemp.chr.compareTo( word.charAt(i) ) > 0 ) {
							prevTemp.low = newNode;
							prevTemp = prevTemp.low;
						}					
					i++;
					
					while( i < word.length() ) {
						TernaryNode newNode1 = new TernaryNode( word.charAt(i));
						prevTemp.middle = newNode1;
						prevTemp = prevTemp.middle;
						i++;
					}
					
				
				
				if( i == word.length() ) {
					prevTemp.str = word;
					prevTemp.count = count;
					prevTemp.indexCount = indexCount;
				}
				}
			}
					
		}
		
		//------------to print all the strings present in tree-----------------------------
		public void traversal(TernaryNode node) {
			if(node != null) {
				TernaryNode temp = node;
				traversal(temp.low);
				if(temp.str != null) {
					System.out.println(temp.str);
				}
				traversal(temp.middle);
				traversal(temp.high);
			}
		}
		
		//-------------to check whether word is in the tree-----------------------------------
		public boolean isPresent(String str) {
			TernaryNode temp = root;
			int i=0;
			while(i < str.length() && temp != null) {
				if( temp.chr.compareTo( str.charAt(i)) > 0) {
					temp = temp.low;
				}
				
				else if( temp.chr.compareTo( str.charAt(i)) < 0 ) {
					temp = temp.high;
				}
				
				else if( temp.chr.equals( str.charAt(i)) ) {
					temp = temp.middle;
					i++;
				}			
			}
			if( i == str.length()) return true;
			else return false;
		}
		
		//--------------to return suggestions----------------------------------------
		public BinarySearchTree suggestions(String str) {
			TernaryNode temp = root;
			TernaryNode tempPrev = temp;
			BinarySearchTree tree = new BinarySearchTree();
			int i = 0;
			while(i < str.length() && temp != null ) {
				if( temp.chr.equals(str.charAt(i)) ) {
					tempPrev = temp;
					temp = temp.middle;
					i++;
				}
				
				else if( temp.chr.compareTo( str.charAt(i) ) < 0) {
					temp = temp.high;
				}
				
				else if( temp.chr.compareTo( str.charAt(i) ) > 0) {
					temp = temp.low;
				}
			}
			
			if(temp != null) {
				tree = trav(temp);
			}
			else if(tempPrev.middle != null) {
				tree = trav(tempPrev.middle);
			}			
			else {
				tree = trav(tempPrev);
			}
			return tree;
		}
		
		public BinarySearchTree trav(TernaryNode node) {
			BinarySearchTree tree = new BinarySearchTree();
			List<String> list_str = new ArrayList<String>();
			List<Integer> list_count = new ArrayList<Integer>();
			this.preTrav(node, list_str, list_count);
			int size = list_str.size();
			int i = 0;
			while(i < size) {
				tree.insert(tree.root, list_str.get(i), list_count.get(i));
				i++;
			}			
			return tree;
		}
		
		public void preTrav(TernaryNode node, List<String> list, List<Integer> list_count) {
			TernaryNode temp = node;
			if(temp != null) {
				if( temp.str != null ){
					list.add(temp.str );
					list_count.add( (temp.count + temp.indexCount) );
				}
				preTrav(temp.low, list, list_count);
				preTrav(temp.middle, list, list_count);
				preTrav(temp.high, list, list_count);		
			}
		}
				
		//------------------------to extract suggestions-------------------------------
		public BinarySearchTree suggest(List<String> list, String str) {
			BinarySearchTree BST = new BinarySearchTree();
			int count = 0;
			int len = str.length();
			int size = list.size();
			int dis;
			LevenshteinDistance d = new LevenshteinDistance();
			for(int i=0;i<size;i++) {
				if(list.get(i).length() >= len+3 ) {
					dis = d.LD(list.get(i).substring(0, len + 2 ) , str);
				}
				
				else {
					dis = d.LD(list.get(i) , str);
					
				}
				 
				if( list.get(i).equals(" p") == false ){
					BST.insert(BST.root, list.get(i) , dis);
					count++;
				}
			}
			
			if(count == 0) {
				for(int i=0;i<size;i++) {
					BST.insert(BST.root, list.get(i), i);
				}
			}
			return BST;
		}		
	}
		
		