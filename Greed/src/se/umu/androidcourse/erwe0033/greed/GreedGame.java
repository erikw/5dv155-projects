package se.umu.androidcourse.erwe0033.greed;

import java.util.Set;
import java.util.HashSet;

public class GreedGame {

	private int totalScore;
	private int roundScore;
	private Set<Die> dice;

	public GreedGame() {
		totalScore = 0;
		roundScore = 0;
		dice = new HashSet<Die>();
		for (int i = 0; i < 6; ++i) {
			dice.add(new Die());
		}
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public Set<Die> getDice() {
		return dice;
	}
}
