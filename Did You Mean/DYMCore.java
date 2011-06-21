package didyoumean;

import java.util.List;
import java.util.ArrayList;

public class DYMCore {
	
	public BinarySearchTree search(String str, TernaryTree tree) {
		List<String> list = new ArrayList<String>();
		BinarySearchTree bst;
		if( tree.isPresent(str) ) {
			bst = tree.suggestions(str);
		}
		
		else {
			bst = tree.suggestions(str);
			list = bst.tree2list(bst);
			bst = tree.suggest(list, str);
		}
		
		return bst;
	}

}
