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
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

	/** Auto Generated serialVersionUID */
	private static final long serialVersionUID = 1185712731956834898L;

	public NavigationFrame() throws IOException {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setMinimumSize(screenSize);
		indiana = ImageIO.read(new File("pics/indiana.png"));
		String[] strar = {"RHIT","ISU","IU"};
		
		
		JPanel infoPanel = new JPanel();
		JComboBox c = new JComboBox(strar);
		infoPanel.setBackground(Color.GREEN);
		this.add(infoPanel,BorderLayout.EAST);
		infoPanel.add(c);
		
		graph = new Graph<String>();
		importColleges();
	}
	
	private void importColleges() throws FileNotFoundException {
		Scanner s = new Scanner(new File("lib/colleges.csv"));
		ArrayList<String> lines = new ArrayList<String>();
		
		while(s.hasNext()) lines.add(s.next());
		
		for(int i = 0;i<lines.size();i++) {
			String str[] = lines.get(i).split(",");
			String name = str[0];
			int x = Integer.parseInt(str[1]);
			int y = Integer.parseInt(str[2]);
			graph.addCollege(name, x, y);
		}
		s.close();
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
		g2d.translate(350, 500);
		graph.paint(g2d);
	}
	
	@Override
	public void repaint() {
		this.pack();
		this.requestFocus();
		super.repaint();
	}
}
