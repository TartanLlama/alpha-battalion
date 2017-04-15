package AlphaBattalion;
import java.awt.Color;
import java.awt.Graphics;

/**
 * A class to hold a single enemy missile
 * @author 090006772
 */
public class EnemyMissile extends Renderable{
	public EnemyMissile(int x, int y, int alienWidth, int alienHeight, int panelHeight){
		super(x + alienWidth / 2, y + alienHeight, 5, 5);
	}
	
	public void draw(Graphics graphic){
		graphic.setColor(Color.black);
		graphic.fillRect(getX(), getY(), getWidth(), getHeight());
	}
	
	public void move(){
		setY(getY() + 4);
	}
}
