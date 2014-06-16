package se.umu.androidcourse.erwe0033.greed;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TurnScore {

	private int totalScore;
	private List<ScoreCombination> combos;
	private Set<Die> zeroDice;

	public TurnScore() {
		totalScore = 0;
		combos = new LinkedList<ScoreCombination>();
		zeroDice = new HashSet<Die>();
	}

	public int getTotalScore() {
		return totalScore;
	}

	public Set<Die> getZeroPointDice() {
		return zeroDice;
	}

	public List<ScoreCombination> getScoreCombos() {
		return combos;
	}

	public void addScore(ScoreCombination combo) {
		combos.add(combo);
		totalScore += combo.getScore();
	}
}
