package se.umu.androidcourse.erwe0033.greed;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TurnScore {

	private int totalScore;

	public TurnScore() {
	}

	public int getTotalScore() {
		return totalScore;
	}

	public Set<Die> getZeroPointDice() {
		return new HashSet<Die>();
	}

	public List<ScoreCombination> getScoreCombos() {
		return new LinkedList<ScoreCombination>();
	}
}
