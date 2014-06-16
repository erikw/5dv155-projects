package se.umu.androidcourse.erwe0033.greed;

import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class GreedGame {

	private int roundScore;
	private Stack<TurnScore> turnScores;
	private Die[] dice;
	private Set<Die> availableDice;

	public static final int POINTS_TRIPLET_FACTOR = 100;
	public static final int POINTS_SINGLES_ONES = 100;
	public static final int POINTS_SINGLES_FIVES = 50;
	public static final int POINTS_LADDER = 1000;


	public GreedGame() {
		newRound();
		turnScores = new Stack<TurnScore>();
		availableDice = new HashSet<Die>();
		dice = new Die[6];
		for (int i = 0; i < dice.length; ++i) {
			Die die = new Die();
			dice[i] = die;
			availableDice.add(die);
		}
	}

	public void setDice(Die[] dice) {
		this.dice = dice;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public TurnScore getTurnScore() {
		try {
			return turnScores.peek();
		} catch (EmptyStackException ese) {
			return null;
		}
	}

	public Die[] getAllDice() {
		return dice;
	}

	public void newRound() {
		roundScore = 0;
	}

	public TurnScore scoreDice() {
		TurnScore score = new TurnScore();
		if (availableDice.size() == dice.length && !scoreLadder(score)) { // Ladder can only score with all dice.
			scoreTriplets(score);
			scoreSingles(score);
		}
		turnScores.push(score);
		 return score;
	}

	private boolean scoreLadder(TurnScore score) {
		return false;
	}

	private void scoreTriplets(TurnScore score) {

	}
	private void scoreSingles(TurnScore score) {

	}

}
