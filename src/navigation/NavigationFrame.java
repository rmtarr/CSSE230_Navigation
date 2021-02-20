package navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import navigation.Graph.Path;

/**
 * This class constructs and handles the JFrame containing the graph 
 * of the colleges in Indiana. It 
 * 
 * 
 *
 */
public class NavigationFrame extends JFrame {
	private Graph<String> graph;
	private BufferedImage indiana;
	private Dimension screenSize;
	private double xScale; //The original program was based on a 1080 x 1920 screen
	private double yScale; //The content needs to be scaled to fit any screen
	private String currentDest;
	private String currentSrc;

	/** Auto Generated serialVersionUID */
	private static final long serialVersionUID = 1185712731956834898L;

	/**
	 * Constructs a Navigation JFrame
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
	public NavigationFrame() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		//JONESIN BABY
		File audioFile = new File("lib/IndianaJones.WAV");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip audioClip = (Clip) AudioSystem.getLine(info);
		audioClip.open(audioStream);
		audioClip.start();
		//WOOOH
		
		//Make the default destination and source Rose-Hulman
		currentDest = "Rose-Hulman";
		currentSrc = "Rose-Hulman";
		
		//Get the Screen Size and make scale variables to handle screen scaling
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setMinimumSize(screenSize);
		xScale = screenSize.getWidth()/1920.0;
		yScale = screenSize.getHeight()/1080.0;
		getContentPane().setBackground(Color.GRAY);
		
		//Draw image of Indiana
		indiana = ImageIO.read(new File("pics/indiana.png"));
		
		//Make the graph and import the colleges from the csv
		graph = new Graph<String>();
		ArrayList<String> names = importColleges(graph, "lib/colleges.csv");
		
		//GUI Setup
		JPanel infoPanel = new JPanel();
		infoPanel.setBackground(Color.GRAY);
		infoPanel.setLayout(new FlowLayout());
		infoPanel.setPreferredSize(new Dimension(600,5000));
		this.add(infoPanel,BorderLayout.EAST);
		
		//							  infoPanel
		//  |------------------------------------|
		//  |						|	title	 |
		//  |						|------------|
		//  |						|			 |
		//  |						| selection  |
		//  |						|			 |
		//  |						|------------|
		//  |						|			 |
		//  |						|    GO!	 |
		//  |						|			 |
		//  |						|------------|
		//  |------------------------------------|
		
		//Title Panel
		JPanel title = new JPanel();
		title.setBackground(Color.GRAY);
		JLabel label0 = new JLabel("Indiana College Navigator");
		label0.setFont(new Font("Cooper Black",1,40));
		title.add(label0);
		infoPanel.add(title);
		
		//Selection Panel
		JPanel selection = new JPanel();
		selection.setBackground(Color.GRAY);
		JLabel label1 = new JLabel("Source College");
		JLabel label2 = new JLabel("Destination College");
		JComboBox src = new JComboBox(names.toArray());
		src.setSelectedItem(currentSrc);
		src.setToolTipText("Select the college you are coming from");
		src.setMaximumSize(new Dimension(1000,20));
		src.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSrc = String.valueOf(src.getSelectedItem());
			}
		});
		JComboBox dest = new JComboBox(names.toArray());
		dest.setSelectedItem(currentDest);
		dest.setToolTipText("Select the college you are going to");
		dest.setMaximumSize(new Dimension(1000,20));
		dest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentDest = String.valueOf(dest.getSelectedItem());
			}
		});
		GroupLayout layout = new GroupLayout(selection);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(label1)
						.addComponent(src))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(label2)
						.addComponent(dest)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(label1)
						.addComponent(label2))
				.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(src)
						.addComponent(dest)));
		selection.setLayout(layout);
		infoPanel.add(selection);
		
		//GOPanel
		JPanel GOPanel = new JPanel();
		JButton GOButton = new JButton("GO!");
		GOPanel.setMaximumSize(new Dimension(100,50));
		GOPanel.add(GOButton);
		GOButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graph.shortestPath(currentSrc, currentDest, false);
				pack();
				repaint();
			}
		});
		infoPanel.add(GOPanel);
		
		//Add the mouse listener to handle clicking on colleges
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Object clicked = graph.getClicked(e.getX(), e.getY(), xScale, yScale);
				if(clicked != null) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						currentSrc = clicked.toString();
						src.setSelectedItem(currentSrc);
					} else if(SwingUtilities.isRightMouseButton(e)) {
						currentDest = clicked.toString();
						dest.setSelectedItem(currentDest);
					}
				}
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		testAStar("IU","Ivy Tech", true);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		//Scale the image of Indiana to the window size and draw it
		g2d.drawImage(indiana, 0, 20, 
				(int) ((screenSize.getHeight()/indiana.getHeight())*indiana.getWidth()), 
				(int) (screenSize.getHeight()), null);
		
		//Paint the graph starting at the middle of the indiana image: (350, 550)
		g2d.translate(350*xScale, 20 + 530*yScale);
		graph.paint(g2d,xScale,yScale);
	}
	
	private ArrayList<String> importColleges(Graph<String> g, String filename) throws FileNotFoundException {
		Scanner s = new Scanner(new File(filename));
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
			for(int j = 3; j< str.length; j++) connections.add(str[j]);
			
			g.addCollege(name, x, y,connections);
		}
		g.synthesizeEdges();
		s.close();
		return namesList;
	}
	
	public void testAStar(String start, String finish, boolean speedConsidered) {
		// TODO when all edges are added to graph
		LinkedList<Graph<String>.Path> path = graph.shortestPath(start, finish, speedConsidered);
		for (Path p : path) {
			System.out.println(p.getCollegeName());
		}
	}
}
