import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

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
		
		public int straightLineDistance(College otherCollege) {
			int x = this.x - otherCollege.x;
			int y = this.y - otherCollege.y;
			return (int)Math.sqrt(x*x - y*y);
		}
		
		public void aStarSearch(PriorityQueue<Path> q) {
			for (Edge e : edges) {
				College c = (this != e.getCollege1()) ? e.getCollege1() : e.getCollege2();
				int distanceToCollege = straightLineDistance(c);
			}
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
		private int x1;
		private int x2;
		private int y1;
		private int y2;
		
		public Edge(College c1, College c2, double speedLimit) {
			this.c1 = c1;
			this.c2 = c2;
			this.speedLimit = speedLimit;
			this.x1 = c1.x;
			this.x2 = c2.x;
			this.y1 = c1.y;
			this.y2 = c2.y;
		}
		
		public void paint(Graphics2D g2d) {
			g2d.setColor(Color.BLACK);
			g2d.drawLine(x1, y1, x2, y2);
		}
		
		public College getCollege1() {
			return c1;
		}
		
		public College getCollege2() {
			return c2;
		}
	}
	
	public class Path {
		
		private College college;
		private int distanceTraveled;
		private int distanceLeft;
		
		public Path(College college, int distanceTraveled, int distanceLeft) {
			this.college = college;
			this.distanceTraveled = distanceTraveled;
			this.distanceLeft = distanceLeft;
		}
		
		public int getDistanceTraveled() {
			return this.distanceTraveled;
		}
	}
	
	/**
	 * A* search algorithm for shortest path
	 * 
	 * @param start
	 * @param finish
	 * @return
	 */
	public ArrayList<Edge> shortestPath(College start, College finish) {
		PriorityQueue<Path> q = new PriorityQueue<>();
		q.add(new Path(start, 0, start.straightLineDistance(finish)));
		start.aStarSearch(q);
		
		return null;
	}

}
