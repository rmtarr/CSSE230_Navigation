package navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import navigation.Graph.Path;

/**
 * This class implements the GUI for the navigation of the graph and handles the listeners
 * 
 * @author snyderc1
 *
 */
public class NavigationFrame extends JFrame {
	private Graph<String> graph;
	private BufferedImage indiana;
	private Dimension screenSize;
	private double xScale; //The original program was based on a 1080 x 1920 screen
	private double yScale; //The content needs to be scaled to fit any screen
	

	/** Auto Generated serialVersionUID */
	private static final long serialVersionUID = 1185712731956834898L;

	public NavigationFrame() throws IOException {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setMinimumSize(screenSize);
		xScale = screenSize.getWidth()/1920.0;
		yScale = screenSize.getHeight()/1080.0;
		
		indiana = ImageIO.read(new File("pics/indiana.png"));
		
		graph = new Graph<String>();
		ArrayList<String> names = importColleges();
		
		//String[] strar = {"RHIT","ISU","IU"};
		
		JPanel infoPanel = new JPanel();
		JComboBox c = new JComboBox(names.toArray());
		infoPanel.setBackground(Color.GREEN);
		this.add(infoPanel,BorderLayout.EAST);
		infoPanel.add(c);
		

		testAStar("Franklin", "USI");
	}
	
	private ArrayList<String> importColleges() throws FileNotFoundException {
		Scanner s = new Scanner(new File("lib/colleges.csv"));
		ArrayList<String> lines = new ArrayList<String>();
		ArrayList<String> namesList = new ArrayList<String>();
		
		while(s.hasNext()) lines.add(s.nextLine());
		
		for(int i = 0;i<lines.size();i++) {
			String str[] = lines.get(i).split(",");
			String name = str[0];
			namesList.add(name);
			int x = Integer.parseInt(str[1]);
			int y = Integer.parseInt(str[2]);
			ArrayList<String> connections = new ArrayList<String>();
			for(int j = 3; j< str.length; j++) {
				connections.add(str[j]);
			}
			graph.addCollege(name, x, y,connections);
		}
		graph.synthesizeEdges();
		s.close();
		return namesList;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//This is where we draw all the images for the nodes and draw all the edges if they are visible
		
		//Color the background gray
		g2d.setColor(Color.GRAY);
		g2d.fill(new Rectangle((int)screenSize.getWidth()+20,(int)screenSize.getHeight()+20));
		
		//Scale the image of Indiana to the window size and draw it
		g2d.drawImage(indiana, 0, 20, 
				(int) ((screenSize.getHeight()/indiana.getHeight())*indiana.getWidth()), 
				(int) (screenSize.getHeight()), null);
		
		//Paint the graph
		g2d.translate(350*xScale, 20 + 530*yScale);
		graph.paint(g2d,xScale,yScale);
	}
	
	@Override
	public void repaint() {
		this.pack();
		this.requestFocus();
		super.repaint();
	}
	
	public void testAStar(String start, String finish) {
		// TODO when all edges are added to graph
		LinkedList<Graph<String>.Path> path = graph.shortestPath(start, finish);
		for (Path p : path) {
			System.out.println(p.getCollegeName());
		}
	}
}
