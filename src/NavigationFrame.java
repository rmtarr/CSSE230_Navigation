import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;



/**
 * This class implements the GUI for the navigation of the graph and handles the listeners
 * 
 * @author snyderc1
 *
 */
public class NavigationFrame extends JFrame {
	//Graph<String,Node> graph;
	//ArrayList<Node> nodes;
	//ArrayList<Edge> edges;

	/**
	 * Auto Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1185712731956834898L;

	public NavigationFrame() {
		this.setMinimumSize(new Dimension(1500, 800));
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//This is where we draw all the images for the nodes and draw all the edges if they are visible
	}
	
	@Override
	public void repaint() {
		this.pack();
		this.requestFocus();
		super.repaint();
	}

}
