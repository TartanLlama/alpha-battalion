package AlphaBattalion;

/**
 * A class holding a single Bomb
 * @author 090006772
 */
public class Bomb extends Renderable{



	public Bomb(int x, int playerWidth, int playerHeight, int panelHeight){

		super(x + playerWidth / 2 - 5, panelHeight - playerHeight, 10, 10);

	}



	public void move(){

		setY(getY() - 4);

	}



}
