
package AlphaBattalion;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * Framework code by Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
 * @author 090006772
 */
public final class GameFrame extends JFrame implements WindowListener {
	private static int DEFAULT_FPS = 60;

	private static int fps;

	private FrameContents thePanel;                     // game drawing surface
	private JTextField levelField;
	private JTextField cashField;
	private JTextField experienceField;
	private JTextField shipLevelField;
	private JTextField shieldField;
	private JTextField bombField;
	private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	
	private DecimalFormat twoDP = new DecimalFormat("0.##");          // 2 dp


	/* ======================================================================
     Constructor
	 ====================================================================== */
	public GameFrame(long period) throws IOException { 
		super("Alpha Battalion");
		makeGUI(period);

		addWindowListener(this);
		pack();
		setResizable(false);
		setVisible(true);
	}  // end of BlankFrame constructor

	/* ======================================================================
     makeGUI
	 Adds an instance of BlankPanel, and two text fields (for UPS, FPS)
	  to the content pane.
	 ====================================================================== */
	private void makeGUI(long period) throws IOException {
		Container c = getContentPane();

		thePanel = new FrameContents(this, period);
		c.add(thePanel, "Center");

		JPanel ctrls = new JPanel();                     // a row of textfields
		ctrls.setLayout( new BoxLayout(ctrls, BoxLayout.X_AXIS));

		levelField = new JTextField("Level: 1");
		levelField.setEditable(false);
		ctrls.add(levelField);

		shieldField = new JTextField("Shield: 3");
		shieldField.setEditable(false);
		ctrls.add(shieldField);
		
		cashField = new JTextField("Money: 0");
		cashField.setEditable(false);
		ctrls.add(cashField);
		
		shipLevelField = new JTextField("Ship Level: 1");
		shipLevelField.setEditable(false);
		ctrls.add(shipLevelField);
		
		experienceField = new JTextField("Experience: 0/" + thePanel.getPlayer().getExperienceLimit()[0]);
		experienceField.setEditable(false);
		ctrls.add(experienceField);
		
		bombField = new JTextField("Bombs: 1");
		bombField.setEditable(false);
		ctrls.add(bombField);

		c.add(ctrls, "South");
	}  // end of makeGUI()

	public void setLevel(int level) { 
		levelField.setText("Level: " + level);
	}
	
	public void setShield(int shield) { 
		shieldField.setText("Shield: " + shield);
	}
	
	public void setCash(double d) {
		cashField.setText("Money: " + currencyFormat.format(d));
	}

	public void setShipLevel(String level) { 
		shipLevelField.setText("Ship Level: " + level);
	}
	
	public void setExperience(int experience, int neededExperience) { 
		experienceField.setText("Experience: " + experience + "/" + neededExperience);
	}
	
	public void setBombs(int bombs) { 
		bombField.setText("Bombs: " + bombs);
	}
	
	// ----------------- window listener methods ----------------------------
	public void windowActivated(WindowEvent e) 
	{ thePanel.resumeGame();  }

	public void windowDeactivated(WindowEvent e) 
	{  thePanel.pauseGame();  }

	public void windowDeiconified(WindowEvent e) 
	{  thePanel.resumeGame();  }

	public void windowIconified(WindowEvent e) 
	{  thePanel.pauseGame(); }

	public void windowClosing(WindowEvent e)
	{  thePanel.stopGame();  }

	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	/* ======================================================================
     main method
	 Takes in a requested FPS, calculates the update period in ns, then
	  creates a new BlankFrame instance
	 ====================================================================== */
	public static void main(String args[]) throws IOException { 
		fps = DEFAULT_FPS;
		if (args.length != 0)
			fps = Integer.parseInt(args[0]);
		new GameFrame(1000000000/fps);
	} // end of main method

	public void restart() {
		try {
			Runtime.getRuntime().exec("java AlphaBattalion.GameFrame");
		} catch (IOException e) {}
		System.exit(0);
	}
} // end of BlankFrame class


