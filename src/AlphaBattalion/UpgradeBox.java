package AlphaBattalion;

import java.awt.Graphics;
import java.text.NumberFormat;

/**
 * A class to hold information for upgrade boxes
 * @author 090006772
 */
public class UpgradeBox extends Renderable{
	private int price;
	/**
	 * Used to format numbers into currency
	 */
	private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	
	public UpgradeBox(int x, int y, int width, int height, int price) {
		super(x, y, width, height);
		this.price = price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}
	
	public void draw(Graphics graphic, String string, boolean displayPrice){
		super.drawBox(graphic);
		super.drawString(graphic, string, getX() + 1, getY() + 25);
		if(displayPrice){
			super.drawString(graphic, currencyFormat.format(price), getX() + 1, getY() + 35);
		}
	}
}
