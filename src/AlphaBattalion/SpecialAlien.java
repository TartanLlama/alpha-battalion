package AlphaBattalion;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A class holding a mystery ship
 * @author 090006772
 */
public class SpecialAlien extends Renderable{
	private int direction;
	private boolean alive;
	private int dyingC;
	private static Image image;
	private static Image dyingImage;
	
	public SpecialAlien(int x, int y, int direction) throws IOException {
		super(x, y, 80, 40);
		this.direction = direction;
		alive = true;
		dyingC = 0;
		image = ImageIO.read(new File("../Sprites/mysteryship.jpg"));
		dyingImage = ImageIO.read(new File("../Sprites/dyingmysteryship.jpg"));
	}
	
	public void move(){
		if(alive){
			if(direction == 0){
				setX(getX() + 3);
			}else if(direction == 1){
				setX(getX() - 3);
			}
		}
	}

	public void setAlive(Boolean alive) {
		this.alive = alive;
	}

	public boolean getAlive() {
		return alive;
	}

	public void incerementDyingC() {
		dyingC ++;
	}

	public int getDyingC() {
		return dyingC;
	}
	
	public void draw(Graphics graphic){
		if(!alive){
			graphic.drawImage(dyingImage, getX(), getY(), null);
		}else{
			graphic.drawImage(image, getX(), getY(), null);
		}
	}
}
