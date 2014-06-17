package se.umu.androidcourse.erwe0033.greed.model;

import java.util.HashSet;
import java.util.Set;

public class ScoreCombination {
	
	private int points;
	private Set<Die> dice;

	public ScoreCombination(int points, Set<Die> dice) {
		this.points = points;
		this.dice = new HashSet<Die>(dice);
	}

	public int getScore() {
		return points;
	}

	// TODO  get total score for this combo.


}
