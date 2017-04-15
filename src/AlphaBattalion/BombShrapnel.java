package AlphaBattalion;

/**
 * A class to hold a piece of bomb shrapnel
 * @author 090006772
 */
public class BombShrapnel extends Renderable{
	private int direction;
	
	public BombShrapnel(int x, int y, int direction) {
		super(x, y, 3, 3);
		this.direction = direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Moves the shrapnel in the correct direction
	 * The directions are numbered from 0 to 7 representing each point on an 8-point compass
	 */
	public void move(){
		switch (direction){
		case 0: y -= 3; break;
		case 1: y -= 3; x += 3; break;
		case 2: x += 3; break;
		case 3: y += 3; x += 3; break;
		case 4: y += 3; break;
		case 5: y += 3; x -= 3; break;
		case 6: x -= 3; break;
		case 7: y -= 3; x -= 3; break;
		}
	}
	
}
