import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;

public class Graph<T, N> {
	HashMap<String,Node> nodes;
	ArrayList<Edge> edges;
	
	public Graph() {
		
	}	
	
	public class Node extends JButton{
		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = 7442789711473774875L;

		public Node(){
			HashSet<Integer> hs = new HashSet<Integer>();
			hs.getBucket();
		}
	}
	
	public class Edge {
		public Edge() {
			
		}
	}
}
