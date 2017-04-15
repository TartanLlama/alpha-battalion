package AlphaBattalion;
import java.awt.Color;
import java.awt.Graphics;


/**
 * An abstract class representing an object which can be drawn on the playing surface
 * @author 090006772
 */
public abstract class Renderable {
	protected int x;
	protected int y;
	private int width;
	private int height;
	
	public Renderable(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	public void setWidth(int width) {
		this.width = width;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void drawRectangle(Graphics graphic, Color colour){
		graphic.setColor(colour);
		graphic.fillRect(x, y, width, height);
	}
	
	public void drawBox(Graphics graphic){
		graphic.drawRect(getX(), getY(), getWidth(), getHeight());
	}
	
	public void drawString(Graphics graphic, String string, int x, int y){
		graphic.drawString(string, x, y);
	}
}
