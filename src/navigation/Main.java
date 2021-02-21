package navigation;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/**
 * The main class for CSSE230_Navigation containing static void main()
 * 
 * This Main class constructs a NavigationFrame and configures it. 
 * 
 */
public class Main {
	private JFrame frame;

	Main() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		frame = new NavigationFrame();
		frame.setTitle("College Navigator");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		new Main();
	}
}
