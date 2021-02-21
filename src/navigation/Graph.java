package navigation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Graph<T> {
	private static double maxSpeed = 100; //max speed of any edge
	private static double minSpeed = 10; //min speed of any edge
	private HashMap<T,College> colleges;
	private LinkedList<Path> currentPath;
	
	public Graph() {
		colleges = new HashMap<T,College>();
		currentPath = null;
	}
	
	public void addCollege(T name, int x, int y, ArrayList<String> connections) {
		colleges.put(name, new College(name,x,y, connections));
	}
	
	public void synthesizeEdges() {
		for(College c : colleges.values()) c.synthesizeEdges();
	}
	
	public void paint(Graphics2D g2d, double xScale, double yScale) {
		//Paint all of the colleges
		for(College c : colleges.values()) c.paint(g2d,xScale,yScale);
		//Paint the current path results from A*
		paintPath(g2d, xScale, yScale);
	}
	
	public void paintPath(Graphics2D g2d, double xS, double yS) {
		if(currentPath!=null) {
			int tempx = currentPath.get(0).college.x;
			int tempy = currentPath.get(0).college.y;
			
			for(int i=1; i<currentPath.size();i++) {
				g2d.setColor(Color.GREEN);
				g2d.drawLine((int)(xS*tempx), (int)(yS*tempy), (int)(xS*currentPath.get(i).college.x), (int)(yS*currentPath.get(i).college.y));
				tempx = currentPath.get(i).college.x;
				tempy = currentPath.get(i).college.y;
			}
		}
	}
	
	/**
	 * This method takes the location of a mouse click and finds the college that was clicked on
	 * @param x location that the mouse was clicked
	 * @param y location that the mouse was clicked
	 * @return name of the college that was clicked on
	 */
	public T getClicked(int x, int y, double xScale, double yScale) {
		double xAdjusted = (x-350.0*xScale)/xScale;
		double yAdjusted = (y-20.0-530.0*yScale)/yScale;
		for(College c : colleges.values()) {
			if(Math.sqrt((c.x-xAdjusted)*(c.x-xAdjusted)+(c.y-yAdjusted)*(c.y-yAdjusted))<5) {
				return c.name;
			}
		}
		return null;
	}
	
	/**
	 * College Class
	 * 
	 */
	public class College{
		private T name;
		private int x;
		private int y;
		private ArrayList<Edge> edges;
		private ArrayList<String> connections;

		public College(T name, int x, int y, ArrayList<String> connections){
			this.x = x;
			this.y = y;
			this.name = name;
			this.edges = new ArrayList<Edge>();
			this.connections = new ArrayList<String>();
			this.connections.addAll(connections);
		}
		
		public void synthesizeEdges() {
			for(String connection : connections) {
				if(!connection.isEmpty()) {
					College other = colleges.get(connection);
					
					//Arbitrary algorithm for making up a fictional speed for the edge
					int xDiff = Math.abs(this.x - other.x);
					int yDiff = Math.abs(this.y - other.y);
					double speedLimit = 50.0+50.0*((double)(xDiff-yDiff)/(double)(xDiff+yDiff));
					speedLimit = Math.min(speedLimit, maxSpeed);
					speedLimit = Math.max(speedLimit, minSpeed);

					edges.add(new Edge(other, speedLimit));
				}
			}
		}
		
		public double straightLineDistance(College otherCollege) {
			double x = this.x - otherCollege.x;
			double y = this.y - otherCollege.y;
			return Math.sqrt(x*x + y*y);
		}
		
		private void paint(Graphics2D g2d, double xScale, double yScale) {
			g2d.setColor(Color.BLACK);
			g2d.fillOval((int) (x*xScale-5),(int) (y*yScale-5), 10, 10);
			g2d.drawString((String) this.name,(int) (x*xScale+3),(int) (y*yScale-3));
			for(Edge e : edges) {
				g2d.drawLine((int) (this.x*xScale), (int) (this.y*yScale), 
						(int) (e.otherCollege.x*xScale), (int) (e.otherCollege.y*yScale));
			}
		}
	}
	
	/**
	 * The Edge class represents a unidirectional edge going from one college to another.  
	 * It contains the object of the neighbor college, and the speed limit along the edge
	 */
	public class Edge {
		private double speedLimit;
		private College otherCollege;
		
		public Edge(College otherCollege, double speedLimit) {
			this.otherCollege = otherCollege;
			this.speedLimit = speedLimit;
		}
	}
	
	public class Path implements Comparable<Path> {
		private College college;
		private College goal;
		private double costAccumulated;
		private double totalCost;
		private Path parent;
		private boolean speedConsidered;
		
		public Path(College current, College goal, double costAccumulated, Path parent, boolean speedConsidered) {
			double maxSpeedLimit = (speedConsidered) ? Graph.maxSpeed : 1;
			this.college = current;
			this.goal = goal;
			this.costAccumulated = costAccumulated;
			this.totalCost = current.straightLineDistance(goal)/maxSpeedLimit + this.costAccumulated;
			this.parent = parent;
			this.speedConsidered = speedConsidered;
		}
		
		public Path aStarSearch(PriorityQueue<Path> q) {
			College c;
			Path child;
			double speedLimit;
			double cost;
			ArrayList<College> collegesVisited = new ArrayList<>();
			for (Path p : q) {
				collegesVisited.add(p.college);
			}
			for (Edge e : college.edges) {
				c = e.otherCollege;
				if (parent == null || !collegesVisited.contains(c)) {
					speedLimit = (speedConsidered) ? e.speedLimit : 1;
					cost = college.straightLineDistance(c)/speedLimit;
					child = new Path(c, goal, cost + costAccumulated, this, speedConsidered);
					q.add(child);
				}
			}
			Path next = q.poll();
			if (next.college == goal) {
				return next;
			}
			return next.aStarSearch(q);
		}
		
		public T getCollegeName() {
			return college.name;
		}
		
		public int compareTo(Path other) {
			if (this.totalCost == other.totalCost) return 0;
			return (this.totalCost > other.totalCost) ? 1 : -1;
		}

	}
	
	public LinkedList<Path> shortestPath(T start, T finish, boolean speedConsidered) {
		PriorityQueue<Path> q = new PriorityQueue<>();
		Path begin = new Path(colleges.get(start), colleges.get(finish), 0, null, speedConsidered);
		Path college = begin.aStarSearch(q);
		LinkedList<Path> path = new LinkedList<>();
		while (college != null) {
			path.addFirst(college);
			college = college.parent;
		}
		currentPath=path;
		return path;
	}
}
