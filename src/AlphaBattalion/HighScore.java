package AlphaBattalion;

/**
 * A class representing a single high score
 * @author 090006772
 */
public class HighScore {
	private String name;
	private int score;
	
	public HighScore(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore() {
		return score;
	}
}
