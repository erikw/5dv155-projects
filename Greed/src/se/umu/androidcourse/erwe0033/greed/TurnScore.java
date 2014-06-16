package se.umu.androidcourse.erwe0033.greed;

import java.util.LinkedList;
import java.util.List;

public class TurnScore {

	private int totalScore;

	public TurnScore() {
	}

	public int getTotalScore() {
		return totalScore;
	}

	public List<ScoreCombination> getScoreCombos() {
		return new LinkedList<ScoreCombination>();
	}
}
