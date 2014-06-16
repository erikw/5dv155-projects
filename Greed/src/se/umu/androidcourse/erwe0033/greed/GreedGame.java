package se.umu.androidcourse.erwe0033.greed;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class GreedGame {

	private int roundScore;
	private Stack<TurnScore> turnScores;
	private Die[] allDice;
	private Set<Die> availableDice;

	private static final int LADDER_SUM = 1 + 2 + 3 + 4 + 5 + 6;

	public static final int POINTS_TRIPLET_FACTOR = 100;
	public static final int POINTS_TRIPLET_OF_ONES = 1000;
	public static final int POINTS_SINGLES_ONES = 100;
	public static final int POINTS_SINGLES_FIVES = 50;
	public static final int POINTS_LADDER = 1000;


	public GreedGame() {
		newRound();
		turnScores = new Stack<TurnScore>();
		availableDice = new HashSet<Die>();
		allDice = new Die[6];
		for (int i = 0; i < allDice.length; ++i) {
			Die die = new Die();
			allDice[i] = die;
			availableDice.add(die);
		}
	}

	public void setDice(Die[] dice) {
		allDice = dice;
		availableDice.clear();
		for (Die die : allDice) {
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
		return allDice;
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
		if (dice.size() != allDice.length) {
			return false;
		}
		int sum = 0;
		for (Die die : dice) {
			sum += die.getValue();
		}
		if (sum == GreedGame.LADDER_SUM) {
			ScoreCombination combo = new ScoreCombination(GreedGame.POINTS_LADDER, dice);
			score.addScore(combo);
			return true;
		} else {
			return false;
		}
	}

	private void scoreTriplets(Set<Die> dice, TurnScore score) {
		Map<Integer, LinkedList<Die>> triplets = new HashMap<Integer, LinkedList<Die> >();
		for (Die die : dice) {
			if (!triplets.containsKey(die.getValue())) {
				triplets.put(new Integer(die.getValue()), new LinkedList<Die>());
			}
			triplets.get(die.getValue()).add(die);
		}
		for (Integer dieValue : triplets.keySet()) {
			if (triplets.get(dieValue).size() == 6) {
				// TODO
			} else if (triplets.get(dieValue).size() == 3) {
				int points;
				if (dieValue == 1)	{
					points = GreedGame.POINTS_TRIPLET_OF_ONES;
				} else {
					points = dieValue * GreedGame.POINTS_TRIPLET_FACTOR;
				}
				score.addScore(new ScoreCombination(points, new HashSet<Die>(triplets.get(dieValue))));
			}
		}
	}
	private void scoreSingles(Set<Die> dice, TurnScore score) {

	}

}
