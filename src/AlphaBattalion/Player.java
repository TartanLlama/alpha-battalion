package AlphaBattalion;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A class to hold the player's ship
 * @author 090006772
 */
public class Player extends Renderable{
	private double cash;
	private double overallCash;
	private int shotsFired;
	private int experience;
	private boolean dying;
	private int[] experienceLimit = {50, 150, 300, 500, 750, 1050, 1400, 1800, 2250, 0};
	private int level;
	private int shield;
	private int difficultySetting;
	private int timeSpentDying;
	private int levelsSurvived;
	private int score;
	private String name;
	private static Image image;
	private static Image dyingImage;
	private int numberOfBombs;
	
	public Player(int x, int y) throws IOException {
		super(x, y, 30, 30);
		shotsFired = 0;
		cash = 0;
		experience = 0;
		level = 1;
		shield = 3;
		difficultySetting = 0;
		dying = false;
		timeSpentDying = 0;
		overallCash = 0;
		levelsSurvived = 0;
		score = 0;
		name = "";
		image = ImageIO.read(new File("../Sprites/player.jpg"));
		dyingImage = ImageIO.read(new File("../Sprites/playerdying.jpg"));
		numberOfBombs = 1;
	}
	
	public void moveLeft(){
		if(x > 0){
			x -= level + 1;
		}
	}
	
	public void moveRight(int boundary){
		if((getX() + getWidth())< boundary){
			x += level + 1;
		}
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getCash() {
		return cash;
	}
	
	public void setShotsFired(int shotsFired) {
		this.shotsFired = shotsFired;
	}

	public int getShotsFired() {
		return shotsFired;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperienceLimit(int[] experienceLimit) {
		this.experienceLimit = experienceLimit;
	}

	public int[] getExperienceLimit() {
		return experienceLimit;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setShield(int shield) {
		this.shield = shield;
	}

	public int getShield() {
		return shield;
	}

	public void setDifficultySetting(int difficultySetting) {
		this.difficultySetting = difficultySetting;
	}

	public int getDifficultySetting() {
		return difficultySetting;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}

	public boolean isDying() {
		return dying;
	}

	public void setTimeSpentDying(int timeSpentDying) {
		this.timeSpentDying = timeSpentDying;
	}

	public int getTimeSpentDying() {
		return timeSpentDying;
	}

	public void setOverallCash(double overallCash) {
		this.overallCash = overallCash;
	}

	public double getOverallCash() {
		return overallCash;
	}

	public void setLevelsSurvived(int levelsSurvived) {
		this.levelsSurvived = levelsSurvived;
	}

	public int getLevelsSurvived() {
		return levelsSurvived;
	}

	public void setScore(int score){
		this.score = score;
	}
	
	public void setScoreWithLevel(int level) {
		this.score = (int) (overallCash * ((level / 50) + 1) * ((levelsSurvived / 10) + 1) * ((this.level / 2) + 1) * 10);
	}

	public int getScore() {
		return score;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void draw(Graphics graphic){
		if(dying){
			graphic.drawImage(dyingImage, x, y, null);
		}else{
			graphic.drawImage(image, x, y, null);
		}
	}

	public void setNumberOfBombs(int numberOfBombs) {
		this.numberOfBombs = numberOfBombs;
	}

	public int getNumberOfBombs() {
		return numberOfBombs;
	}
	
}
