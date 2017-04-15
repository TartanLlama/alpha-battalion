package AlphaBattalion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * A class containing the playing surface inside a frame
 * Framework code by Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
 * @author 090006772
 */
public final class FrameContents extends JPanel implements Runnable {
	/**
	 * The player's ship
	 */
	private Player player;
	/**
	 * Each missile on screen fired by the player
	 */
	private ArrayList<Missile> activeMissiles = new ArrayList<Missile>();
	
	/**
	 * The possible bomb on screen
	 */
	private Bomb activeBomb;
	
	private BombShrapnel[] activeShrapnel = {null, null, null, null, null, null, null, null};
	
	/**
	 * Every alien
	 */
	private Alien[][] aliens = new Alien[11][5];
	/**
	 * All on screen missiles fired by aliens
	 */
	private ArrayList<EnemyMissile> enemyMissiles = new ArrayList<EnemyMissile>();
	
	/**
	 * Used to count down until the next mystery ship should be created
	 */
	private int timeSinceLastMysteryShip = 0;

	/**
	 * Used to format soubles into currency
	 */
	private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	
	/**
	 * The characters which make up the name of the player
	 */
	private ArrayList<Character> playerName = new ArrayList<Character>();
	
	private int timeSpentDrawingScores = 0;
	
	/**
	 * The state of the game i.e. at the start menu, playing, at the upgrade menu etc.
	 */
	private State state = new State();
	
	/**
	 * Files holding sounds to be played during the game
	 */
	private File explosionClip = new File("..\\Sounds\\explosion.wav");
	private File shootClip = new File("..\\Sounds\\shoot.wav");
	private File specialShipClip = new File("..\\Sounds\\ufo_lowpitch.wav");
	private File invaderKilledClip = new File("..\\Sounds\\invaderkilled.wav");
	private File shieldDownClip = new File("..\\Sounds\\shielddown.wav");
	private File appearClip = new File("..\\Sounds\\appear.wav");
	private File mysteryShipDeathClip = new File("..\\Sounds\\mysteryshipdeath.wav");
	
	/**
	 * The possible barricades
	 */
	private Barricade[] barricades = {null, null, null};
	
	private final int DIFFICULTY_BOX_HEIGHT = 50;
	private final int DIFFICULTY_BOX_WIDTH = 100;
	
	/**
	 * Each difficulty and it's data
	 */
	private final Difficulty[] DIFFICULTIES = {new Difficulty(25, 300, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 1, 1, "Beginner"), new Difficulty(100, 250, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 75, 20, "Master"), new Difficulty(175, 300, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 5, 2, "Amateur"), new Difficulty(250, 250, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 150, 50, "God"), new Difficulty(325, 300, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 25, 5, "Intermediate"), new Difficulty(400, 250, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 250, 100, "Chuck Norris"), new Difficulty(475, 300, DIFFICULTY_BOX_WIDTH, DIFFICULTY_BOX_HEIGHT, 50, 10, "Advanced")}; 
	
	
	private final int UPGRADE_BOX_WIDTH = 80;
	private final int UPGRADE_BOX_HEIGHT = 50;
	
	/**
	 * The x-coordinate of the box and the intitial cost of it's contents
	 */
	private UpgradeBox[] upgradeBoxes = {new UpgradeBox(20, 250, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 2000), new UpgradeBox(140, 250, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 2000), new UpgradeBox(260, 250, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 2000), new UpgradeBox(380, 250, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 7500), new UpgradeBox(500, 250, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 0), new UpgradeBox(80, 200, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 1000), new UpgradeBox(200, 200, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 1000), new UpgradeBox(320, 200, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 1000), new UpgradeBox(440, 200, UPGRADE_BOX_WIDTH, UPGRADE_BOX_HEIGHT, 1500)};
	
	
	private SpecialAlien mysteryShip;
	
	/**
	 * The level which the game is at
	 */
	private int level = 1;
	
	/**
	 * The x-coordinate of the furthest left alien position
	 */
	private int furthestAlienLeft = PWIDTH;
	/**
	 * The x-coordinate of the furthest right alien position
	 */
	private int furthestAlienRight = 0;
	

	/**
	 * Parent frame
	 */
	private GameFrame theFrame;            

	/**
	 * Width of the panel
	 */
	private static final int PWIDTH = 600;
	/**
	 * Height of the panel
	 */
	private static final int PHEIGHT = 500; 

	// Thread control ==========================================================
	/**
	*The thread that performs the animation  
	*/
	private Thread animator;    
	
	/**
	 * Used to stop the animation thread    
	 */
	private boolean running = false;
	private boolean isPaused = false;

	/**
	 * Period between drawing in nanoseconds
	 */
	private long period;                

	/**
	 * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
	 */
	private static final int NO_DELAYS_PER_YIELD = 16;

	/**
	 * No. of frames that can be skipped in any one animation loop i.e the games state is updated but not rendered
	 */
	private static int MAX_FRAME_SKIPS = 5;

	/**
	 * Number of FPS values stored to get an average
	 */
	private static int NUM_FPS = 10;

	/**
	 * Used for rendering
	 */
	private Graphics dbg; 
	private Image dbImage = null;

	// Stats ==================================================================
	private static long MAX_STATS_INTERVAL = 1000000L;

	// used for gathering statistics
	private long statsInterval = 0L;                                   // in ns
	private long gameStartTime;
	private int timeSpentInGame = 0;                              // in seconds

	private long frameCount = 0;
	private double fpsStore[];

	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;
	private double upsStore[];

	/**
	 * Whether or not the left arrow key is pressed
	 */
	private boolean leftPressed;
	/**
	 * Whether or not the left arrow key is pressed
	 */
	private boolean rightPressed;

	private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
	
	private int numberOfAliens = 55;
	/**
	 * The base monetary value of each alien in a row
	 */
	private int[] alienValue = {30, 20, 20, 10, 10};
	
	/**
	 * The monetary value of the mystery ship dependant on how many shots have been fired by the player in the level
	 */
	private final int[] MYSTERY_SHIP_VALUES = {100, 50, 50, 100, 150, 50, 100, 150, 50, 150, 50, 50, 100, 150, 50, 150, 150, 50, 100, 50, 50, 100, 300};
	
	/**
	 * The X and Y values of the barricades
	 */
	private final int[][] BARRICADE_POSITIONS = {{125, 400}, {275, 400}, {425, 400}};
	/**
	 * Score data to be displayed when the player dies
	 */
	private ScoreData[] scoreData = {new ScoreData("Money Earned: ", 100, 150, false), new ScoreData("Level Reached: ", 100, 200, false), new ScoreData("Levels Survived: ", 100, 250, false), new ScoreData("Ship Level: ", 100, 300, false), new ScoreData("Score: ", 100, 350, false), new ScoreData("Hit enter to continue", 170, 400, false)};
	
	/**
	 * Used while displaying scores
	 */
	private int numberOfScoresDrawn;
	private boolean canContinueToHighScores;
	private ArrayList<HighScore> highScores = new ArrayList<HighScore>();

	/**
	 * Whether or not to reset the game upon termination
	 */
	private boolean reset = false;
	/**
	 * The images to use for each row of aliens
	 */
	private int[] alienImageAddress = {0, 1, 1, 2, 2};
	/**
	 * To hold the alien images
	 */
	private BufferedImage[][] alienImages = new BufferedImage[5][2];
	
	
	/**
	 * @param inFrame The parent frame
	 * @param period Period between drawing in nanoseconds
	 * @throws IOException 
	 */
	public FrameContents(GameFrame inFrame, long period) throws IOException {
		theFrame = inFrame;
		this.period = period;

		setBackground(Color.white);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

		setFocusable(true);
		requestFocus();    // the JPanel now has focus, so receives key events
		listen();

		player = new Player((PWIDTH/2), (PHEIGHT - 30)); //Creates the player object halfway across the screen and 30 pixels from the bottom

		// initialise timing elements
		fpsStore = new double[NUM_FPS];
		upsStore = new double[NUM_FPS];
		for (int i=0; i < NUM_FPS; i++) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}
		
		//Stores the images for each alien and initialises them
		for(int outerC = 0; outerC < aliens.length; outerC ++){
			for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
				alienImages[innerC][0] = ImageIO.read(new File("../Sprites/invader" + alienImageAddress [innerC] + "1.jpg"));
				alienImages[innerC][1] = ImageIO.read(new File("../Sprites/invader" + alienImageAddress [innerC] + "2.jpg"));
				aliens[outerC][innerC] = new Alien(outerC * 40 + 10, innerC * 25 + 110, alienImages[innerC][0], alienImages[innerC][1]);
			}
		}
	}

	
	/**
	 * Listens for keypresses and executes the relevant commands
	 */
	private void listen() {
		addKeyListener( new KeyAdapter() {
			public void keyPressed(KeyEvent e){ 
				int keyCode = e.getKeyCode();
				if (state.equals("highScores")){
					//If the user presses Q at the highscore screen, quit
					if(keyCode == KeyEvent.VK_Q){
						running = false;
					}
					//If the user presses Q at the highscore screen, quit and restart the game
					if(keyCode == KeyEvent.VK_ENTER){
						running = false;
						reset = true;
					}
				}
				
				//Used for smooth movement
				if(keyCode == KeyEvent.VK_LEFT){
					leftPressed = true;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					rightPressed = true;
				}
				
				if(state.isPlaying()){
					//Debug command REMOVE
					if(keyCode == KeyEvent.VK_N){
						state.setState("upgrading");
						int numberOfMissiles = activeMissiles.size();
						for(int missileC = 0; missileC < numberOfMissiles; missileC ++){
							activeMissiles.remove(0);
						}
					}
					//Debug command REMOVE
					if(keyCode == KeyEvent.VK_C){
						player.setCash(player.getCash() + 10000);	
					}
					//Debug command REMOVE
					if(keyCode == KeyEvent.VK_L){
						player.setShield(9001);
					}
					//If the player presses space while in game then shoot a missile
					if(keyCode == KeyEvent.VK_SPACE){
						if(activeMissiles.size() < player.getLevel()){
							activeMissiles.add(new Missile(player.getX(), player.getWidth(), player.getHeight(), PHEIGHT));
							player.setShotsFired(player.getShotsFired() + 1);
							
							new ClipPlayer(shootClip, 0);
							checkShotsFired();
						}
					}

					//Fires bombs
					if(state.equals("playing") && keyCode == KeyEvent.VK_B){
						boolean canFire = true;
						//Makes sure that there is no shrapnel on screen
						for(BombShrapnel tempShrapnel : activeShrapnel){
							if(tempShrapnel != null){
								canFire = false;
							}
						}
						
						//Fire a bomb if the player has any left
						if(activeBomb == null && canFire && player.getNumberOfBombs() != 0){
							activeBomb = new Bomb(player.getX(), player.getWidth(), player.getHeight(), PHEIGHT);
							player.setNumberOfBombs(player.getNumberOfBombs() - 1);
						//Explode the bomb if it is on screen
						}else if(activeBomb != null){
							explodeBomb();
						}
					}
				}
				
				if(keyCode == KeyEvent.VK_ENTER){
					if(state.equals("score")){
						//Move to the next screen if you should be able to and are at the score screen
						if(canContinueToHighScores){
							state.setState("enterName");
						//If not, jump to a point where you are
						}else{
							for(int displayScoreDataC = 0; displayScoreDataC < scoreData.length; displayScoreDataC ++){
								scoreData[displayScoreDataC].setShouldDisplay(true);
								canContinueToHighScores = true;
							}
						}
					//Move to the highscores screen if at the entername screen and have entered a valid name
					}else if(state.equals("enterName") && !player.getName().equals("")){
						state.setState("highScores");
						try {
							updateHighScores();
						} catch (IOException e1) {}
					}
				}
				
				//Alters the stored player name dependant on which key is pressed
				if(state.equals("enterName")){
					if(keyCode == KeyEvent.VK_BACK_SPACE && playerName.size() != 0){
						playerName.remove(playerName.size() - 1);
					//Captures all text characters (not space)
					}else if(keyCode >= 33 && keyCode <= 126 && playerName.size() < 15){
						playerName.add((char)keyCode);
					}
				}
			}
			private void explodeBomb() {
				for(int explodeC = 0; explodeC < 8; explodeC ++){
					activeShrapnel[explodeC] = new BombShrapnel(activeBomb.getX() + activeBomb.getWidth()/2, activeBomb.getY() + activeBomb.getHeight()/2, explodeC);
				}
				activeBomb = null;
			}

			public void keyReleased(KeyEvent e){
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_LEFT){
					leftPressed = false;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					rightPressed = false;
				}
			}
		});
	}

	/**
	 * Notifies this component that it now has a parent component wait for the JPanel to be added to the JFrame before starting
	 */
	public void addNotify() { 
		super.addNotify();   // creates the peer
		startGame();         // start the thread
	}

	/**
     *initialise and start the thread 
	 */
	private void startGame() { 
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
		state.setState("startMenu");
		File bgMusic = new File("..\\Sounds\\alpha battalion.wav");
		new ClipPlayer(bgMusic, 9001);
		
	}

	// ------------- game life cycle methods ---------------------------------
	// called by the JFrame's window listener methods

	/**
	 * Called when the JFrame is activated / deiconified
	 */
	public void resumeGame() {
		isPaused = false;
	} 

	/**
	 * Called when the JFrame is deactivated / iconified
	 */
	public void pauseGame() {
		isPaused = true;
	} 

	/**
	 * Called when the JFrame is closing
	 */
	public void stopGame() {
		running = false;
	}

	/**
	 * Required by Runnable interface.
     * The frames of the animation are drawn inside the while loop.
     */
	public void run() {
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		gameStartTime = System.nanoTime();
		beforeTime = gameStartTime;

		running = true;

		while(running) {
			gameUpdate();
			gameRender();
			paintScreen();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;  

			if (sleepTime > 0) {   // some time left in this cycle
				try {
					Thread.sleep(sleepTime/1000000L);  // nano -> ms
				}
				catch(InterruptedException ex){}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}
			else {    // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime;                  // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();         // give another thread a chance to run
					noDelays = 0; 
				}
			}

			beforeTime = System.nanoTime();

			/* If frame animation is taking too long, update the game state
         without rendering it, to get the updates/sec nearer to
         the required FPS. */
			int skips = 0;
			while((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate();                  // update state but don't render
				skips++;
			}
			framesSkipped += skips;

			storeStats();
		}
		if(reset){
			theFrame.restart();
		}else{
			printStats();
			System.exit(0);  
		}
	}


	/**
	* The objects in the game are each updated.
	*/
	private void gameUpdate() { 
		if (!isPaused) {
			if(state.isPlaying()){
				if(!player.isDying()){
					if(leftPressed == true){
						player.moveLeft();
					}
					if(rightPressed == true){
						player.moveRight(PWIDTH); //Move right if not at the boundary
					}
				}
				
				//Checks each missile for if it is out of bounds or not
				for(int missileC = 0; missileC < activeMissiles.size(); missileC ++){
					checkOutOfBoundsMissiles(missileC);
				}
			}
				
			if(state.equals("startMenu")){
				checkMissileDifficultyCollision();
			}
			
			if(state.equals("playing")){
				processPlayerMissiles();									
				processEnemyMissiles();
				processBomb();
				processShrapnel();
				moveAliens();
				getAlienBoundaries();
				shootBack();			
				try {
					checkMysteryShip();
				} catch (IOException e) {}
				moveMysteyShip();
				checkPlayerDying();
				
			}else if(state.equals("upgrading")){
				checkUpgradeCollision();
			}
		}
	}

	/**
	 * Move bomb shrapnel and check it for collisions
	 */
	private void processShrapnel() {
		checking:
		for(int shrapnelC = 0; shrapnelC < activeShrapnel.length; shrapnelC ++){
			if(activeShrapnel[shrapnelC] != null){
				moveShrapnel(shrapnelC);
				if(checkOutOfBoundsShrapnel(shrapnelC)){
					continue checking;
				}
				
				for(int outerC = 0; outerC < aliens.length; outerC ++){
					for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
						if(checkShrapnelEnemyCollision(shrapnelC, outerC, innerC)){
							continue checking;
						}
					}
				}
				if(checkShrapnelMysteryShipCollision(shrapnelC)){
					continue checking;
				}
			}
		}
	}


	/**
	 * Detects collisions between a piece of shrapnel and the mystery ship
	 * @param shrapnelC The piece of shrapnel to check
	 * @return Whether a collision was detected or not
	 */
	private boolean checkShrapnelMysteryShipCollision(int shrapnelC) {
		BombShrapnel tempShrapnel = activeShrapnel[shrapnelC];
		if(mysteryShip != null && checkCollision(tempShrapnel.getY(), tempShrapnel.getHeight(), mysteryShip.getY(), mysteryShip.getHeight(), tempShrapnel.getX(), tempShrapnel.getWidth(), mysteryShip.getX(), mysteryShip.getWidth())){
			mysteryShip.setAlive(false);
			activeShrapnel[shrapnelC] = null;
			player.setCash(player.getCash() + MYSTERY_SHIP_VALUES[player.getShotsFired() - 1]);
			new ClipPlayer(mysteryShipDeathClip, 0);
			return true;
		}
		return false;
	}


	/**
	 * Checks for a collision between a piece of shrapnel and an enemy
	 * @param shrapnelC The piece of shrapnel to check
	 * @param outerC The column number of the alien to check
	 * @param innerC The row number of the alien to check
	 * @return Whether a collision was detected
	 */
	private boolean checkShrapnelEnemyCollision(int shrapnelC, int outerC, int innerC) {
		BombShrapnel tempShrapnel = activeShrapnel[shrapnelC];
		Alien tempAlien = aliens[outerC][innerC];
		if(tempAlien.getState().equals("Alive") && checkCollision(tempShrapnel.getY(), tempShrapnel.getHeight(), tempAlien.getY(), tempAlien.getHeight(), tempShrapnel.getX(), tempShrapnel.getWidth(), tempAlien.getX(), tempAlien.getWidth())){
			activeShrapnel[shrapnelC] = null;
			killInvader(outerC, innerC);
			return true;
		}				
		return false;
	}


	/**
	 * Checks if a piece of shrapnel is out of bounds
	 * @param shrapnelC The piece of shrapnel to check
	 * @return Whether the shrapnel was out of bounds or not
	 */
	private boolean checkOutOfBoundsShrapnel(int shrapnelC) {
		BombShrapnel tempShrapnel = activeShrapnel[shrapnelC];
		if((tempShrapnel.getY() > PHEIGHT || tempShrapnel.getY() < 0) || (tempShrapnel.getX() > PWIDTH || tempShrapnel.getX() < 0)){
			activeShrapnel[shrapnelC] = null;
			return true;
		}
		return false;
	}


	private void moveShrapnel(int shrapnelC) {
		activeShrapnel[shrapnelC].move();
	}


	private void processBomb() {
		if(activeBomb != null){
			activeBomb.move();
			checkOutOfBoundsBomb();	
		}
	}


	private void checkOutOfBoundsBomb() {
		if(activeBomb.getY() > PHEIGHT){
			activeBomb = null;
		}
	}


	/**
	 * Checks if the player is dying and carries out the necessary actions
	 */
	private void checkPlayerDying() {
		if(player.isDying()){
			if(player.getTimeSpentDying() < 50){
				player.setTimeSpentDying(player.getTimeSpentDying() + 1);
			//If the player has finished dying i.e. is dead, then move to the score page and calculate the player's score
			}else{
				state.setState("score");
				player.setScoreWithLevel(level); //Handled by the player object so less passing is needed
			}
		}
	}

	/**
	 * @throws IOException
	 */
	private void updateHighScores() throws IOException {
		String name = player.getName();
		
		Scanner highScoreTable = getHighscoreTable();
		
		//Creates highScore objects for each score held in the file
		while(highScoreTable.hasNext()){
			highScores.add(new HighScore(highScoreTable.next(), highScoreTable.nextInt()));
		}
		
		highScores.add(new HighScore(name, player.getScore())); //Adds the new player's score to the current ones
		
		HighScoreComparator comparator = new HighScoreComparator();
		Collections.sort(highScores, comparator);
		
		highScores = checkNumberOfHighScores(highScores);
		
		writeBackToFile(highScores);
		highScoreTable.close();
		
		state.setState("highScores");
	}

	/**
	 * Makes sure that there are ten or less entries and deletes one if not
	 * @param highscores The unedited list
	 * @return The fixed list
	 */
	private ArrayList<HighScore> checkNumberOfHighScores(ArrayList<HighScore> highscores) {
		if(highscores.size() > 10){
			highscores.remove(10);
		}
		return highscores;
	}

	/**
	 * Writes the highscores into a new file or back into the old one if it already exists
	 * @param highscores
	 * @throws IOException
	 */
	private void writeBackToFile(ArrayList<HighScore> highscores) throws IOException {
		BufferedWriter writeScores = null;
		File highScoreFile = new File("..\\highscoretable.txt");
		highScoreFile.createNewFile();
		
		writeScores = new BufferedWriter(new FileWriter(highScoreFile));
		
		
		HighScore element1 = highscores.get(0);
		writeScores.write(element1.getName() + " " + element1.getScore() + " ");
		
		for(int scoresC = 1; scoresC < highscores.size(); scoresC ++){
			writeScores.append(highscores.get(scoresC).getName() + " " + highscores.get(scoresC).getScore() + " ");
		}
		writeScores.flush();
		writeScores.close();
	}

	/**
	 * Scans the high score table to allow it to be processed
	 * @return A scanner holding every token in the high score table
	 * @throws IOException
	 */
	private Scanner getHighscoreTable() throws IOException {
		File highscoreTableFile = new File("..\\highscoretable.txt");
		highscoreTableFile.createNewFile();
		Scanner highscoreTable = null;
		
		highscoreTable = new Scanner(highscoreTableFile);
		
		return highscoreTable;	
	}

	/**
	 * Checks each missile fired by the player for collisions
	 */
	private void processPlayerMissiles(){
		checking:
		for(int missileC = 0; missileC < activeMissiles.size(); missileC ++){
			for(int barricadeC = 0; barricadeC < barricades.length; barricadeC ++){
				if(checkMissileBarricadeCollision(missileC, barricadeC)){
					continue checking; //The missile has been destroyed, so move to the next one
				}
			}
			for(int alienOuterC = 0; alienOuterC < aliens.length; alienOuterC ++){
				for(int alienInnerC = 0; alienInnerC < aliens[alienOuterC].length; alienInnerC ++){
					if(checkMissileEnemyCollision(missileC, alienOuterC, alienInnerC)){
						continue checking;
					}
				}
			}
			if(mysteryShip != null && checkMissileMysteryCollision(missileC)){
				continue checking;
			}
		}
	}
	
	/**
	 * Checks for collisions between a player-fired missile and a difficulty box
	 */
	private void checkMissileDifficultyCollision() {
		outside:
			for(int missileC = 0; missileC < activeMissiles.size(); missileC ++){
				for(int difficultyC = 0; difficultyC < DIFFICULTIES.length; difficultyC ++){
					if(activeMissiles.get(missileC) != null){
						Missile tempActiveMissile = activeMissiles.get(missileC);
							
						if(checkCollision(tempActiveMissile.getY(), tempActiveMissile.getHeight(), DIFFICULTIES[difficultyC].getY(), 50, tempActiveMissile.getX(), tempActiveMissile.getWidth(), DIFFICULTIES[difficultyC].getX(), 100)){
							activeMissiles.remove(missileC);
							level = DIFFICULTIES[difficultyC].getStartLevel();
							player.setDifficultySetting(difficultyC); 
							state.setState("playing");
							break outside;
						}
					}
				}
			}
	}

	/**
	 * Checks a player-fired missile for collisions with a barricade
	 * @param missileC The missile to check
	 * @param barricadeC The barricade to check
	 * @return
	 */
	private boolean checkMissileBarricadeCollision(int missileC, int barricadeC) {
		if(activeMissiles.get(missileC) != null && barricades[barricadeC] != null){
			Missile tempActiveMissile = activeMissiles.get(missileC);
			Barricade tempBarricade = barricades[barricadeC];
				
			if(checkCollision(tempActiveMissile.getY(), tempActiveMissile.getHeight(), tempBarricade.getY(), tempBarricade.getHeight(), tempActiveMissile.getX(), tempActiveMissile.getWidth(), tempBarricade.getX(), tempBarricade.getWidth())){
				activeMissiles.remove(missileC);
				tempBarricade.setPixelHealth(tempBarricade.getPixelHealth() - 1);
				
				if(tempBarricade.getPixelHealth() == 0){
					//Destroy the barricade if it is too small to have collisions detected anymore
					if(tempBarricade.getHeight() <= 1){
						tempBarricade = null;	
					}else{
						tempBarricade.setHeight(tempBarricade.getHeight() - 1);
						tempBarricade.setPixelHealth(tempBarricade.getLevel());
					}	
					barricades[barricadeC] = tempBarricade;	
				}	
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Checks an enemy-fired missile for collisions with a barricade
	 * @param missileC The missile to check
	 * @param barricadeC The barricade to check
	 * @return
	 */
	private boolean checkEnemyMissileBarricadeCollision(int missileC, int barricadeC) {
		if(enemyMissiles.get(missileC) != null && barricades[barricadeC] != null){
			EnemyMissile tempEnemyMissile = enemyMissiles.get(missileC);
			Barricade tempBarricade = barricades[barricadeC];
			
			if(checkCollision(tempEnemyMissile.getY(), tempEnemyMissile.getHeight(), tempBarricade.getY(), tempBarricade.getHeight(), tempEnemyMissile.getX(), tempEnemyMissile.getWidth(), tempBarricade.getX(), tempBarricade.getWidth())){
				enemyMissiles.remove(missileC);
				tempBarricade.setPixelHealth(tempBarricade.getPixelHealth() - 1);
				
				if(tempBarricade.getPixelHealth() == 0){
					tempBarricade.setY(tempBarricade.getY() + 1);
					//Destroy the barricade if it is too small to have collisions detected anymore
					if(tempBarricade.getHeight() <= 1){
						tempBarricade= null;
		
					}else{
						tempBarricade.setHeight(tempBarricade.getHeight() - 1);
						tempBarricade.setPixelHealth(tempBarricade.getLevel());
					}
					barricades[barricadeC] = tempBarricade;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks a player-fired missile for collisions with an upgrade box
	 */
	private void checkUpgradeCollision() {
		checking:
		for(int missileC = 0; missileC < activeMissiles.size(); missileC ++){
			for(int upgradeBoxC = 0; upgradeBoxC < upgradeBoxes.length; upgradeBoxC ++){
				if(activeMissiles.get(missileC) != null){
					Missile tempMissile = activeMissiles.get(missileC);
					boolean collided = checkCollision(tempMissile.getY(), tempMissile.getHeight(), upgradeBoxes[upgradeBoxC].getY(), upgradeBoxes[upgradeBoxC].getHeight(), tempMissile.getX(), tempMissile.getWidth(), upgradeBoxes[upgradeBoxC].getX(), upgradeBoxes[upgradeBoxC].getWidth());
					if(collided){
						carryOutUpgradeAction(upgradeBoxC);
						activeMissiles.remove(missileC);
						continue checking;
					}
				}
			}
		}
	}

	/**
	 * @param upgradeBox The upgrade box to execute
	 */
	private void carryOutUpgradeAction(int upgradeBox) {
		if(upgradeBox >= 0 && upgradeBox < 3){
			//If an upgrade/repair barricade box was shot, process it
			if(player.getCash() > upgradeBoxes[upgradeBox].getPrice()){
				player.setCash(player.getCash() - upgradeBoxes[upgradeBox].getPrice());
				processBarricade(upgradeBox);
			}
		}
		if(upgradeBox == 3){
			//If the buy new shield box was shot, add another shield
			if(player.getCash() >= upgradeBoxes[3].getPrice()){
				player.setCash(player.getCash() - upgradeBoxes[3].getPrice());
				player.setShield(player.getShield() + 1);
			}
		}else if(upgradeBox == 4){
			//If the next level box was shot, go to the next level
			state.setState("playing");
			try {
				nextLevel();
			} catch (IOException e) {}
		}else if(upgradeBox >= 5 && upgradeBox < 8){
			if(player.getCash() > upgradeBoxes[upgradeBox].getPrice()){
				player.setCash(player.getCash() - upgradeBoxes[upgradeBox].getPrice());
				barricades[upgradeBox - 5].setLevel(barricades[upgradeBox - 5].getLevel() + 1);
			}
		}else if(upgradeBox == 8){
			if(player.getCash() > upgradeBoxes[upgradeBox].getPrice()){
				player.setCash(player.getCash() - upgradeBoxes[upgradeBox].getPrice());
				player.setNumberOfBombs(player.getNumberOfBombs() + 1);
			}
		}
	}

	/**
	 * Creates/repairs a barricade dependent on whether it exists or not
	 * @param barricadeNumber
	 */
	private void processBarricade(int barricadeNumber) {
		if(barricades[barricadeNumber] == null){
			barricades[barricadeNumber] = new Barricade(BARRICADE_POSITIONS [barricadeNumber][0], BARRICADE_POSITIONS[barricadeNumber][1], 50, 50);
		}else{
			//Reset the barricade
			barricades[barricadeNumber].setY(BARRICADE_POSITIONS[barricadeNumber][1]);
			barricades[barricadeNumber].setHeight(50);
		}
	}

	/**
	 * Checks whether a given object has collided with another
	 * @param y1 The y position of object 1
	 * @param h1 The height of object 1
	 * @param y2 The y position of object 2
	 * @param h2 The height of object 2
	 * @param x1 The x position of object 1
	 * @param w2 The width of object 1
	 * @param x2 The x position of object 2
	 * @param w2 The width of object 2
	 * @return If object 1 is within object 2
	 */
	private boolean checkCollision(int y1, int h1, int y2, int h2, int x1, int w1, int x2, int w2) {
		if ((x1 > (x2 + w2)) || ((x1 + w1) < x2) || (y1 > (y2 + h2)) || ((y1 + h1) < y2)){
			return false;    
		}else{
			 return true;	
		}		    
	}

	/**
	 * Moves the mystery ship if it exists
	 */
	private void moveMysteyShip() {
		if(mysteryShip != null){
			mysteryShip.move();
		}
	}

	/**
	 * Moves a given enemy-fired missile if it exists
	 * @param missileC The missile to move
	 */
	private void moveEnemyMissile(int missileC) {
		if(enemyMissiles.get(missileC) != null){
			enemyMissiles.get(missileC).move();
		}
	}

	/**
	 * Alters the number of shots fired in a level to create a kind of loop for the value of the mystery ship
	 * Taken from the original game where the 23rd shot fired and every 15th shot after that will give 300 points if they hit the mystery ship
	 */
	private void checkShotsFired() {
		if(player.getShotsFired() == 24){
			player.setShotsFired(8);
		}
	}

	/**
	 * Checks if an enemy-fired missile is out of bounds and deletes it if so
	 * @param missileC
	 * @return
	 */
	private boolean checkOutOfBoundsAlienMissiles(int missileC) {
		if(enemyMissiles.get(missileC).getY() > PHEIGHT){
			enemyMissiles.remove(missileC);
			if(player.getLevel() != 10){
				player.setExperience(player.getExperience() + 1);
			}
			checkPlayerLevelUp();
			return true;
		}	
		return false;
	}

	/**
	 * Moves each alien if necessary
	 */
	private void moveAliens() {
		@SuppressWarnings("unused")
		int vertCheck = 0;
		for(int outerC = 0; outerC < aliens.length; outerC ++){
			for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
				if (aliens[outerC][innerC].getState().equals("Alive")){
					vertCheck = aliens[outerC][innerC].move(furthestAlienLeft, furthestAlienRight, PWIDTH, numberOfAliens);
					checkEnemyBarricadeCollision(outerC, innerC);
					checkEnemyPlayerCollision(outerC, innerC);
				}else if(aliens[outerC][innerC].getState().equals("Dying")){
					vertCheck = aliens[outerC][innerC].move(furthestAlienLeft, furthestAlienRight, PWIDTH, numberOfAliens);
					if(aliens[outerC][innerC].getDyingC() < 20){
						aliens[outerC][innerC].setDyingC(aliens[outerC][innerC].getDyingC() + 1);
					}else{
						aliens[outerC][innerC].setState("Dead");
						numberOfAliens --;
						checkLevelEnd();
					}
				}
			}
		}	
	}

	/**
	 * Checks if an alien has reached the player and, if so, kills him/her
	 * @param outerC The column number of the alien
	 * @param innerC The row number of the alien
	 */
	private void checkEnemyPlayerCollision(int outerC, int innerC) {
		Alien tempAlien = aliens[outerC][innerC];
		if(tempAlien != null && tempAlien.getY() + tempAlien.getHeight() > player.getY()){
			player.setShield(0);
			new ClipPlayer(explosionClip, 0);
			player.setDying(true);
		}
	}


	/**
	 * Checks if an alien has collided with a barrier and alters it accordingly
	 * @param outerC The column number of the alien
	 * @param innerC The row number of the alien
	 */
	private void checkEnemyBarricadeCollision(int outerC, int innerC) {
		for(Barricade tempBarricade : barricades){
			Alien tempAlien = aliens[outerC][innerC];
			if(tempBarricade != null && tempAlien != null && checkCollision(tempAlien.getY(), tempAlien.getHeight(), tempBarricade.getY(), tempBarricade.getHeight(), tempAlien.getX(), tempAlien.getWidth(), tempBarricade.getX(), tempBarricade.getWidth())){
				int alienDifference = tempAlien.getY() + tempAlien.getHeight();
				tempBarricade.setHeight(tempBarricade.getHeight() + (tempBarricade.getY() - alienDifference));
				tempBarricade.setY(alienDifference);
				if(tempBarricade.getHeight() <= 1){
					tempBarricade = null;
				}
			}
		}
	}


	/**
	 * Checks if the player has enough experience to level up
	 */
	private void checkPlayerLevelUp() {
		if(player.getLevel() != 10 && player.getExperience() >= player.getExperienceLimit()[player.getLevel() - 1]){
			player.setExperience(0);
			player.setLevel(player.getLevel() + 1);
		}
	}

	/**
	 * Checks if a player-fired missile is out of bounds
	 * @param missileC The missile to check
	 */
	private void checkOutOfBoundsMissiles(int missileC) {
		if(activeMissiles.get(missileC) != null){
			if(activeMissiles.get(missileC).getY() < 0){
				activeMissiles.remove(missileC);
			}else{
				activeMissiles.get(missileC).move();
			}
		}
	}

	/**
	 * Creates a new mystery ship if it the right time to do so
	 * @throws IOException
	 */
	private void checkMysteryShip() throws IOException {
		Random rand = new Random();
		if(timeSinceLastMysteryShip == 1600){
			
			new ClipPlayer(specialShipClip, 0);
			int direction = rand.nextInt(2);
			if(direction == 0){
				mysteryShip = new SpecialAlien(-1 * 80, 20, 0);
			}else if(direction == 1){
				mysteryShip = new SpecialAlien(PWIDTH, 20, 1);
			}
			timeSinceLastMysteryShip = 0;
		}
		
		if(mysteryShip != null && !mysteryShip.getAlive()){
			mysteryShip.incerementDyingC();
			if(mysteryShip.getDyingC() == 50){
				mysteryShip = null;
			}
		}
		timeSinceLastMysteryShip ++;
	}

	/**
	 * Goes to the upgrades screen if all aliens are dead
	 */
	private void checkLevelEnd() {
		if(numberOfAliens  == 0){
			state.setState("upgrading");
			int numberOfMissiles = activeMissiles.size();
			for(int missileC = 0; missileC < numberOfMissiles; missileC ++){
				activeMissiles.remove(0);
			}
		}
	}

	/**
	 * Advances the game to the next level
	 * @throws IOException
	 */
	private void nextLevel() throws IOException {
		//Re-initialises the aliens
		for(int outerC = 0; outerC < aliens.length; outerC ++){
			for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
				aliens[outerC][innerC] = new Alien(outerC * 40 + 10, innerC * 25 + 110, alienImages[innerC][0], alienImages[innerC][1]);
			}
		}
		
		for(int enemyMissileC = 0; enemyMissileC < enemyMissiles.size(); enemyMissileC ++){
			enemyMissiles.remove(0);
		}
		
		level += DIFFICULTIES[player.getDifficultySetting()].getDifficultyJump();
		player.setLevelsSurvived(player.getLevelsSurvived() + 1);
		numberOfAliens = 55;
		timeSinceLastMysteryShip = 0;
		mysteryShip = null;
	}

	/**
	 * Moves the enemy missiles and checks them for collisions
	 */
	private void processEnemyMissiles() {
		for(int missileC = 0; missileC < enemyMissiles.size(); missileC ++){
			moveEnemyMissile(missileC);
		}
		
		checking:
		for(int missileC = 0; missileC < enemyMissiles.size(); missileC ++){
			if(checkEnemyMissilePlayerCollision(missileC)){
				continue checking;
			}
			if(checkOutOfBoundsAlienMissiles(missileC)){
				continue checking;
			}
			for(int barricadeC = 0; barricadeC < barricades.length; barricadeC ++){
				if(checkEnemyMissileBarricadeCollision(missileC, barricadeC)){
					continue checking;
				}
			}
		}
	}
	
	/**
	 * Checks to see if an enemy missile has collided with the player
	 * @param missileC The missile to check
	 * @return
	 */
	private boolean checkEnemyMissilePlayerCollision(int missileC){
		if(enemyMissiles.get(missileC) != null){
			if(enemyMissiles.get(missileC).getY() > player.getY() && enemyMissiles.get(missileC).getY() < player.getY() + player.getHeight() && enemyMissiles.get(missileC).getX() > player.getX() && enemyMissiles.get(missileC).getX() < player.getX() + player.getWidth()){
				enemyMissiles.remove(missileC);
				
				if(!player.isDying()){
					player.setShield(player.getShield() - 1);
				}
				
				if(player.getShield() > 0){
					new ClipPlayer(shieldDownClip, 0);
				}				
				if(player.getShield() == 0){
					new ClipPlayer(explosionClip, 0);
					player.setDying(true);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Randomly fires a missile from an alien given a chance
	 */
	private void shootBack() {
		Random rand = new Random();
		for(int outerC = 0; outerC < aliens.length; outerC ++){
			for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
				if(rand.nextInt(10000/level) == 0 && aliens[outerC][innerC].getState().equals("Alive")){
					Alien tempAlien = aliens[outerC][innerC];
					enemyMissiles.add(new EnemyMissile(tempAlien.getX(), tempAlien.getY(), tempAlien.getWidth(), tempAlien.getHeight(), PHEIGHT));
				}
			}
		}
	}

	/**
	 * Checks to see if a player-fired missile has collided with an alien
	 * @param missileC The missile to check
	 * @param outerC The column number of the alien to check
	 * @param innerC The row number of the alien to check
	 * @return
	 */
	private boolean checkMissileEnemyCollision(int missileC, int outerC, int innerC) {
		Missile tempMissile = activeMissiles.get(missileC);
		Alien tempAlien = aliens[outerC][innerC];
		if(checkCollision(tempMissile.getY(), tempMissile.getHeight(), tempAlien.getY(), tempAlien.getHeight(), tempMissile.getX(), tempMissile.getWidth(), tempAlien.getX(), tempAlien.getWidth()) && aliens[outerC][innerC].getState().equals("Alive")){
			activeMissiles.remove(missileC);
			killInvader(outerC, innerC);
			return true;
		}		
		return false;
	}
	
	private void killInvader(int outerC, int innerC) {
		new ClipPlayer(invaderKilledClip, 0);
		aliens[outerC][innerC].setState("Dying");
		double cashToAdd = alienValue[innerC] * player.getLevel()/2 + 0.5;
		player.setCash(player.getCash() + cashToAdd);
		player.setOverallCash(player.getOverallCash() + cashToAdd);
	}


	/**
	 * Checks to see is a player-fired missile has collided with the mystery ship
	 * @param missileC
	 * @return
	 */
	private boolean checkMissileMysteryCollision(int missileC){
		if(activeMissiles.get(missileC) != null){
			Missile tempMissile = activeMissiles.get(missileC);
			if(tempMissile != null && checkCollision(tempMissile.getY(), tempMissile.getHeight(), mysteryShip.getY(), mysteryShip.getHeight(), tempMissile.getX(), tempMissile.getWidth(), mysteryShip.getX(), mysteryShip.getWidth())&& mysteryShip.getAlive()){
				mysteryShip.setAlive(false);
				activeMissiles.remove(missileC);
				player.setCash(player.getCash() + MYSTERY_SHIP_VALUES[player.getShotsFired() - 1]);
				new ClipPlayer(mysteryShipDeathClip, 0);
				return true;
			}
		}	
		return false;
	}

	/**
	 * Gets the X-coordinate of the furthest right alien position and the furthest left alien position
	 */
	private void getAlienBoundaries() {	
		furthestAlienLeft = PWIDTH;
		furthestAlienRight = 0;
		for(int outerC = 0; outerC < aliens.length; outerC ++){
 			for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
				if(aliens[outerC][innerC].getX() < furthestAlienLeft && aliens[outerC][innerC].getState().equals("Alive")){
					furthestAlienLeft = aliens[outerC][innerC].getX();
				}
				if((aliens[outerC][innerC].getX() + aliens[outerC][innerC].getWidth()) > furthestAlienRight && aliens[outerC][innerC].getState().equals("Alive")){
					furthestAlienRight = aliens[outerC][innerC].getX() + aliens[outerC][innerC].getWidth();
				}
			}
		}
	}

	/**
	 * Render the game objects onto dbImage
	*/
	private void gameRender() {
		if (dbImage == null){
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			}
			else
				dbg = dbImage.getGraphics();
		}

		// clear the background
		dbg.setColor(Color.black);
		dbg.fillRect (0, 0, PWIDTH, PHEIGHT);

		drawObjects();
	}  // end of gameRender()
	
	/**
	 * Draw every necessary object onto the playing area
	 */
	private void drawObjects() {
		if(state.isPlaying()){
			drawPlayer();
			drawPlayerMissiles();
		}
		
		if(state.equals("startMenu")){
			drawStartMenu();
		}else if(state.equals("playing")){
			drawAliens();	
			drawEnemyMissiles();
			drawMysteryShip();
			drawBarricades();
			drawBomb();
			drawShrapnel();
		}else if(state.equals("upgrading")){
			drawUpgradeMenu();
		}else if(state.equals("score")){
			drawScore();
		}else if(state.equals("enterName")){
			drawEnterNameMenu();
		}else if(state.equals("highScores")){
			drawHighScores();
		}
		
	}

	private void drawShrapnel() {
		for(int shrapnelC = 0; shrapnelC < activeShrapnel.length; shrapnelC ++){
			if(activeShrapnel[shrapnelC] != null){
				activeShrapnel[shrapnelC].drawRectangle(dbg, Color.RED);
			}
		}	
	}


	private void drawBomb() {
		if(activeBomb != null){
			activeBomb.drawRectangle(dbg, Color.red);
		}
	}


	private void drawHighScores() {
		Font font = new Font("serif", Font.PLAIN, 30);
		dbg.setColor(Color.white);
		dbg.setFont(font);
		dbg.drawString("HIGHSCORES", 200, 100);
		for(int highScoreC = 0; highScoreC < highScores.size(); highScoreC ++){
			dbg.setFont(new Font("serif", Font.PLAIN, 20));
			dbg.drawString((highScoreC + 1) + ". " + highScores.get(highScoreC).getName() + "    " + highScores.get(highScoreC).getScore(), 100, (100 + (highScoreC + 1) * 25));
		}
		dbg.setFont(font);
		dbg.drawString("Press Enter To Retry Or Q To Quit", 100, 450);
	}

	private void drawEnterNameMenu() {
		String name = "";
		for(char character : playerName){
			name += character;
		}
		
		player.setName(name);
		
		Font font = new Font("serif", Font.PLAIN, 30);
		dbg.setColor(Color.white);
		dbg.setFont(font);
		dbg.drawString("Score: " + player.getScore(), 100, 200);
		dbg.drawString("Enter Your Name: ", 100, 300);
		dbg.setFont(new Font("serif", Font.PLAIN, 20));
		dbg.drawString(name, 330, 300);
	}

	private void drawScore() {
		Font font = new Font("serif", Font.PLAIN, 30);
		dbg.setColor(Color.white);
		dbg.setFont(font);
		dbg.drawString("Game Over", 230, 100);
		double playerCash = player.getOverallCash();
		int playerLevelsSurvived = player.getLevelsSurvived();
		int playerLevel = player.getLevel();
		scoreData[0].setMessage(currencyFormat.format(playerCash));
		scoreData[1].setMessage(String.valueOf(level));
		scoreData[2].setMessage(String.valueOf(playerLevelsSurvived));
		scoreData[3].setMessage(String.valueOf(playerLevel));
		if(playerLevelsSurvived > 0){
			scoreData[4].setMessage(String.valueOf(player.getScore()));
		}else{
			player.setScore(0);
			scoreData[4].setMessage(String.valueOf(player.getScore()));
		}
		
		//Tell the program to display the next score if the alloted time has passed
		if(timeSpentDrawingScores == 100 && numberOfScoresDrawn < scoreData.length){
			scoreData[numberOfScoresDrawn].setShouldDisplay(true);
			timeSpentDrawingScores = 0;
			numberOfScoresDrawn ++;
			new ClipPlayer(appearClip, 0);
		}
		
		for(int scoreDataC = 0; scoreDataC < scoreData.length; scoreDataC ++){
			if(scoreData[scoreDataC].shouldDisplay()){
				ScoreData tempScoreData = scoreData[scoreDataC];
				dbg.drawString(tempScoreData.getMessage(), tempScoreData.getX(), tempScoreData.getY());
			}	
		}
		
		if(numberOfScoresDrawn == scoreData.length - 1){
			canContinueToHighScores = true;
		}
		timeSpentDrawingScores ++;
	}

	private void drawPlayer() {
		player.draw(dbg);
	}

	private void drawPlayerMissiles() {
		for(int missileC = 0; missileC < activeMissiles.size(); missileC ++){
			if(activeMissiles.get(missileC) != null){
				activeMissiles.get(missileC).drawRectangle(dbg, Color.yellow);
			}
		}
	}

	private void drawStartMenu() {
		Font font = new Font("serif", Font.PLAIN, 30);
		dbg.setColor(Color.white);
		dbg.setFont(font);
		dbg.drawString("Alpha Battalion", 206, 100);
		font = new Font("serif", Font.PLAIN, 15);
		dbg.setFont(font);
		dbg.drawString("Choose your skill level", 233, 150);
		for(int difficultyC = 0; difficultyC < DIFFICULTIES.length; difficultyC ++){
			DIFFICULTIES[difficultyC].draw(dbg);
		}
	}

	private void drawBarricades() {
		for(int barricadeC = 0; barricadeC < barricades.length; barricadeC ++){
			if(barricades[barricadeC] != null){
				barricades[barricadeC].drawRectangle(dbg, Color.green);
			}
		}
	}

	private void drawMysteryShip() {
		if(mysteryShip != null){
			mysteryShip.draw(dbg);
		}
	}

	private void drawEnemyMissiles() {
		for(int missileC = 0; missileC < enemyMissiles.size(); missileC ++){
			if(enemyMissiles.get(missileC) != null){
				enemyMissiles.get(missileC).drawRectangle(dbg, Color.green);
			}
		}	
	}

	private void drawAliens() {
		for(int outerC = 0; outerC < aliens.length; outerC ++){
			for(int innerC = 0; innerC < aliens[outerC].length; innerC ++){
				aliens[outerC][innerC].draw(dbg);	
			}
		}
	}

	private void drawUpgradeMenu() {
		Font font = new Font("serif", Font.PLAIN, 10);
		dbg.setColor(Color.white);
		dbg.setFont(font);
	
		String display;
		for(int barricadeBoxC = 0; barricadeBoxC < 3; barricadeBoxC ++){
			if(barricades[barricadeBoxC] == null){
				display = "Buy barricade " + (barricadeBoxC + 1);
				upgradeBoxes[barricadeBoxC].setPrice(2000);
			}else{
				display = "Repair barricade " + (barricadeBoxC + 1);
				upgradeBoxes[barricadeBoxC].setPrice((50 - barricades[barricadeBoxC].getHeight()) * 10);
			}
			upgradeBoxes[barricadeBoxC].draw(dbg, display, true);
		}
		upgradeBoxes[3].draw(dbg, "Buy extra shield", true);
		
		upgradeBoxes[4].draw(dbg, "Next Level", false);
		
		for(int barricadeUpgradeBoxC = 5; barricadeUpgradeBoxC < 8; barricadeUpgradeBoxC ++){
			if(barricades[barricadeUpgradeBoxC - 5] != null){
				display = "Upgrade barricade " + (barricadeUpgradeBoxC - 4);
				upgradeBoxes[barricadeUpgradeBoxC].setPrice(barricades[barricadeUpgradeBoxC - 5].getLevel() * 1000);
				upgradeBoxes[barricadeUpgradeBoxC].draw(dbg, display, true);
			}
		}
		
		upgradeBoxes[8].draw(dbg, "Buy extra bomb", true);
	}

	/**
	 * Use active rendering to put the buffered image on-screen
	 */
	private void paintScreen() { 
		Graphics g;
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			g.dispose();
		}
		catch (Exception e)
		{ System.out.println("Graphics context error: " + e);  }
	}

	/**
	 * The statistics:
     *  - the summed periods for all the iterations in this interval
     *    (period is the amount of time a single frame iteration should take), 
     *    the actual elapsed time in this interval, 
     *    the error between these two numbers;
     *    
     * - the total frame count, which is the total number of calls to run();
     * 
     * - the frames skipped in this interval, the total number of frames
     *   skipped. A frame skip is a game update without a corresponding render;
     *
     * - the FPS (frames/sec) and UPS (updates/sec) for this interval, 
     *   the average FPS & UPS over the last NUM_FPSs intervals.
     *  
     * The data is collected every MAX_STATS_INTERVAL  (1 sec).
	 */
	private void storeStats() { 
		frameCount++;
		statsInterval += period;

		if (statsInterval >= MAX_STATS_INTERVAL) {     // record stats every MAX_STATS_INTERVAL
				
			theFrame.setLevel(level);	 
			theFrame.setShield(player.getShield());
			theFrame.setCash(player.getCash());	
			theFrame.setExperience(player.getExperience(), player.getExperienceLimit()[player.getLevel() - 1]);
			theFrame.setBombs(player.getNumberOfBombs());
			
			if(player.getLevel() == 10){
				theFrame.setShipLevel("MAX");
			}else{
				theFrame.setShipLevel(String.valueOf(player.getLevel()));
			}
			
 
			framesSkipped = 0;
			statsInterval = 0L;   // reset
		}
	}

	/**
	 * Displays a stat summary, called upon termination. 
	 */
	private void printStats() {
		System.out.println("Frame Count/Loss: " + frameCount + " / " + totalFramesSkipped);
		System.out.println("Average FPS: " + df.format(furthestAlienLeft));
		System.out.println("Average UPS: " + df.format(furthestAlienRight));
		System.out.println("Time Spent: " + timeSpentInGame + " secs");
	}
	
	public int getLevel() {
		return level;
	}
	
	public Player getPlayer() {
		return player;
	}
}


