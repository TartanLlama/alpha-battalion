package AlphaBattalion;

/**
 * A helper class to assist in checking the stage of gameplay
 * @author 090006772
 */
public class State {
	private String state;
	private boolean playing;
	
	public void setState(String state) {
		this.state = state;
	}
	
	public boolean equals(String toCheck) {
		if(state.equals(toCheck)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isPlaying(){
		if(state.equals("startMenu")||state.equals("playing")||state.equals("upgrading")){
			return true;
		}else{
			return false;
		}
	}
}
