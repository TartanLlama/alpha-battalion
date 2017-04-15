package AlphaBattalion;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A class representing one Alien
 * @author 090006772
 */
public class Alien extends Renderable {
	private String state;
	private int dyingC;
	private int direction; 
	int loopsSinceLastMove;
	private boolean vertChecked;
	private Image[] image = new Image[2];
	private int imageState;
	private static Image dyingImage;
	
	public Alien(int x, int y, Image image1, Image image2) throws IOException {
		super(x, y, 30, 15);
		setDyingC(0);
		state = "Alive";
		direction = 1;
		vertChecked = false;
		image[0] = image1;
		image[1] = image2;
		imageState = -1;
		dyingImage = ImageIO.read(new File("../Sprites/invaderdying.jpg"));
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	/**
	 * @param furthestLeft The furthest X-coordinate to the left
	 * @param furthestRight The furthest X-coordinate to the right
	 * @param panelWidth
	 * @param numberOfAliens
	 * @return If the alien moved down or not
	 */
	public int move(int furthestLeft, int furthestRight, int panelWidth, int numberOfAliens) {
		if(numberOfAliens > 0){
			if((furthestLeft <= 0 || furthestRight >= panelWidth) && vertChecked == false && loopsSinceLastMove >= numberOfAliens + 5){
				setY(getY() + 30);
				direction *= -1;
				imageState *= -1;
				loopsSinceLastMove = 0;
				vertChecked = true;
				return 1;
			}else{
				if(direction == 1 && loopsSinceLastMove >= numberOfAliens + 5){
					imageState *= -1;
					setX(getX() + 15);
					loopsSinceLastMove = 0;
					vertChecked = false;
				}else if(direction == -1 & loopsSinceLastMove >= numberOfAliens + 5){
					imageState *= -1;
					setX(getX() - 15);
					loopsSinceLastMove = 0;
					vertChecked = false;
				}
				loopsSinceLastMove ++;
				return 0;
			}
		}
		return 0;
	}

	public void setDyingC(int dyingC) {
		this.dyingC = dyingC;
	}

	public int getDyingC() {
		return dyingC;
	}
	
	/**
	 * Draws the Alien with the correct image
	 * @param graphic
	 */
	public void draw(Graphics graphic){
		int index;
		if(imageState == -1){
			index = 0;
		}else{
			index = 1;
		}
		if(state.equals("Alive")){
			graphic.drawImage(image[index], getX(), getY(), null);
		}else if(state.equals("Dying")){
			graphic.drawImage(dyingImage, getX(), getY(), null);
		}
	}

	public void setImage(Image image, int index) {
		this.image[index] = image;
	}

	public Image getImage(int index) {
		return image[index];
	}
}
