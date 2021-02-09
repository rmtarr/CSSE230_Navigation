import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * The main class for CSSE230_Navigation.
 * 
 * This Main class constructs a NavigationFrame and configures it. 
 * 
 * @author snyderc1
 *
 */
public class Main
{
	private JFrame frame;

	Main() throws IOException
	{
		frame = new NavigationFrame();
		frame.setTitle("College Navigator");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException
	{
		new Main();
	}
}
