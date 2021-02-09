import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.swing.JButton;

public class Graph <T extends Comparable<? super T>> implements Iterable<T> {
	HashMap<T,College> colleges;
	ArrayList<Edge> edges;
	
	public Graph() {
		colleges = new HashMap<T,College>();
		edges = new ArrayList<Edge>();
	}
	
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
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
		
	}
	
	public class Path implements Comparable<Path> {
		
		private College college;
		private College goal;
		private Path parent;
		private int distanceTraveled;
		private int cost;
		
		public Path(Path parent, College current, College goal, int distanceTraveled) {
			this.parent = parent;
			this.college = current;
			this.goal = goal;
			this.distanceTraveled = distanceTraveled;
			this.cost = college.straightLineDistance(goal) + this.distanceTraveled;
		}
		
		public void aStarSearch(PriorityQueue<Path> q, HashSet<College> visitedNodes) {
			College c;
			Path child;
			for (Edge e : college.edges) {
				c = (college != e.c1) ? e.c1 : e.c2;
				if (!visitedNodes.contains(c) && c!= parent.college) {
					int distanceToCollege = college.straightLineDistance(c);
					child = new Path(this, c, goal, distanceToCollege + distanceTraveled);
					q.add(child);
				}
			}
			Path next = q.poll();
			
			next.aStarSearch(q, visitedNodes);
		}
		
		public int compareTo(Path other) {
			if (this.cost == other.cost) return 0;
			return (this.cost > other.cost) ? 1 : -1;
		}
	}
	
	/**
	 * A* search algorithm for shortest path
	 * 
	 * @param start
	 * @param finish
	 * @return
	 */
	public ArrayList<College> shortestPath(College start, College finish) {
		PriorityQueue<Path> q = new PriorityQueue<>();
		HashSet<College> visitedNodes = new HashSet<>();
		Path begin = new Path(null, start, finish, 0);
		q.add(begin);
		begin.aStarSearch(q, visitedNodes);
		
		return null;
	}

}
