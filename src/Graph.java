import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;

public class Graph<T> {
	HashMap<T,College> colleges;
	ArrayList<Edge> edges;
	
	public Graph() {
		colleges = new HashMap<T,College>();
		edges = new ArrayList<Edge>();
	}
	
	/**
	 * College Class
	 * 
	 */
	public class College extends JButton{
		/** Auto Generated serialVersionUID */
		private static final long serialVersionUID = 7442789711473774875L;
		private T name;
		private int x;
		private int y;
		private Image icon;
		private ArrayList<Edge> edges;

		public College(T name, int x, int y){
			this.x = x;
			this.y = y;
			this.name = name;
		}
		
		public void addEdge(T otherCollege, double speedLimit) {
			College other = colleges.get(otherCollege);
			edges.add(new Edge(this, other, speedLimit));
		}
	}
	
	/**
	 * Edge Class
	 *
	 */
	public class Edge {
		private double speedLimit;
		private College c1;
		private College c2;
		
		public Edge(College c1, College c2, double speedLimit) {
			this.c1 = c1;
			this.c2 = c2;
			this.speedLimit = speedLimit;
		}
	}
}
