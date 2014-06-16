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
		availableDice.clear();
		for (Die die : dice) {
			availableDice.add(die);
		}
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

	public TurnScore scoreDice(Set<Die> selecteDice) {
		checkForReuse(selecteDice);
		Set<Die> dice = new HashSet<Die>(selecteDice); // Don't modify original.
		TurnScore score = new TurnScore();
		if (!scoreLadder(dice, score)) {
			scoreTriplets(dice, score);
			scoreSingles(dice, score);
		}
		turnScores.push(score);
		 return score;
	}
	private void checkForReuse(Set<Die> dice) {
		for (Die die : dice) {
			if (!availableDice.contains(die)) {
				throw new RuntimeException("Some dice alreay used for scoring.");
			}
		}
	}

	private boolean scoreLadder(Set<Die> dice, TurnScore score) {
		//availableDice.size() == dice.length && 
		return false;
	}

	private void scoreTriplets(Set<Die> dice, TurnScore score) {

	}
	private void scoreSingles(Set<Die> dice, TurnScore score) {

	}

}
