package AlphaBattalion;
import java.awt.Color;
import java.awt.Graphics;


/**
 * A class representing a single missile
 * @author 090006772
 */
public class Missile extends Renderable{	
	public Missile(int x, int playerWidth, int playerHeight, int panelHeight){
		super(x + playerWidth / 2 - 1 / 2, panelHeight - playerHeight, 1, 12);
	}
	
	public void move(){
		setY(getY() - 10);
	}
}
