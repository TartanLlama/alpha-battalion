package AlphaBattalion;

/**
 * A class representing one Barricade
 * @author 090006772
 */
public class Barricade extends Renderable{
	private int level;
	private int pixelHealth;
	
	public Barricade(int x, int y, int width, int height) {
		super(x, y, width, height);
		level = 1;
		pixelHealth = level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setPixelHealth(int pixelHealth) {
		this.pixelHealth = pixelHealth;
	}

	public int getPixelHealth() {
		return pixelHealth;
	}

}
