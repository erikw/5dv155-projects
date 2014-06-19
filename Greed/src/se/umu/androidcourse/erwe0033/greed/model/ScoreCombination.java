package se.umu.androidcourse.erwe0033.greed.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Representation of a score combination that consists of some dice and a score.
 */
public class ScoreCombination {
	
	private int points;
	private Set<Die> dice;

	public ScoreCombination(int points, Set<Die> dice) {
		this.points = points;
		this.dice = new HashSet<Die>(dice);
	}

	/**
	 * Get the score points.
	 */
	public int getScore() {
		return points;
	}

	/**
	 * Get the dice who scored.
	 */
	public Set<Die> getDice() {
		return dice;
	}
}
