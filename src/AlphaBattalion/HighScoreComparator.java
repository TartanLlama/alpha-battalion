package AlphaBattalion;

import java.util.Comparator;

/**
 * A comparator used in sorting high scores
 * @author 090006772
 */
public class HighScoreComparator implements Comparator<HighScore>{
	/** 
	 * Returns the ranking order of two given objects
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(HighScore score0, HighScore score1) {
		if(score0.getScore() > score1.getScore()){
			return -1;
		}else if(score0.getScore() < score1.getScore()){
			return 1;
		}else{
			return 0;
		}
	}

}
