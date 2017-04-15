package AlphaBattalion;

/**
 * A class holding the data necessary to display a score fragment at the score page
 * @author 090006772
 */
public class ScoreData {
	private String message;
	private String basicMessage;
	private int x;
	private int y;
	private boolean shouldDisplay;
	
	public ScoreData(String message, int x, int y, boolean shouldDisplay) {
		this.message = message;
		basicMessage = message;
		this.x = x;
		this.y = y;
		this.shouldDisplay = shouldDisplay;
	}
	
	public void setMessage(String message) {
		this.message = basicMessage + message;
	}
	public String getMessage() {
		return message;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}

	public void setShouldDisplay(boolean shouldDisplay) {
		this.shouldDisplay = shouldDisplay;
	}

	public boolean shouldDisplay() {
		return shouldDisplay;
	}
}
