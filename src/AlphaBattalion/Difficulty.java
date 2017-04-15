package AlphaBattalion;

import java.awt.Graphics;

/**
 * A class holding data needed for varying difficulty levels
 * @author 090006772
 */
public class Difficulty extends Renderable{

	private int startLevel;
	private int difficultyJump;
	private String difficultyName;
	
	public Difficulty(int boxX, int boxY, int width, int height, int startLevel, int difficultyJump, String difficultyName) {
		super(boxX, boxY, width, height);
		this.startLevel = startLevel;
		this.difficultyJump = difficultyJump;
		this.difficultyName = difficultyName;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setDifficultyName(String difficultyName) {
		this.difficultyName = difficultyName;
	}

	public String getDifficultyName() {
		return difficultyName;
	}

	public void setDifficultyJump(int difficultyJump) {
		this.difficultyJump = difficultyJump;
	}

	public int getDifficultyJump() {
		return difficultyJump;
	}
	
	public void draw(Graphics graphic){
		super.drawBox(graphic);
		super.drawString(graphic, difficultyName, getX() + 5, getY() + 30);
	}
}
