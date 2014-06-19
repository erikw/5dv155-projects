package se.umu.androidcourse.erwe0033.greed.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A collection of score combinations from a turn and dice that did not score anything.
 */
public class TurnScore {
	private int totalScore;
	private List<ScoreCombination> combos;
	private Set<Die> zeroDice;

	public TurnScore() {
		totalScore = 0;
		combos = new LinkedList<ScoreCombination>();
		zeroDice = new HashSet<Die>();
	}

	/**
	 * Get total score for this turn.
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * Get dice who did not score anything.
	 */
	public Set<Die> getZeroPointDice() {
		return zeroDice;
	}

	/**
	 * Get all score combinations.
	 */
	public List<ScoreCombination> getScoreCombos() {
		return combos;
	}

	/**
	 * Add more scores to this turn.
	 */
	public void addScore(ScoreCombination combo) {
		combos.add(combo);
		totalScore += combo.getScore();
	}

	/**
	 * Add more dice not scoring to this turn.
	 */
	public void addZeroScoreDice(Set<Die> zeroDice) {
		this.zeroDice = new HashSet<Die>(zeroDice);
	}
}
