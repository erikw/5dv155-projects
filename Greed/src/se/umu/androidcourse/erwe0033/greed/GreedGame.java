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
		score.addZeroScoreDice(dice);
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

	private boolean scoreLadder(Set<Die> selectedDice, TurnScore score) {
		if (selectedDice.size() != allDice.length) {
			return false;
		}
		int sum = 0;
		for (Die die : selectedDice) {
			sum += die.getValue();
		}
		if (sum == GreedGame.LADDER_SUM) {
			ScoreCombination combo = new ScoreCombination(GreedGame.POINTS_LADDER, selectedDice);
			score.addScore(combo);
			selectedDice.clear();
			return true;
		} else {
			return false;
		}
	}

	private void scoreTriplets(Set<Die> selectedDice, TurnScore score) {
		Map<Integer, LinkedList<Die>> triplets = new HashMap<Integer, LinkedList<Die> >();
		for (Die die : selectedDice) {
			if (!triplets.containsKey(die.getValue())) {
				triplets.put(new Integer(die.getValue()), new LinkedList<Die>());
			}
			triplets.get(die.getValue()).add(die);
		}
		for (Integer dieValue : triplets.keySet()) {
			LinkedList<Die> triplet = triplets.get(dieValue);
			if (triplet.size() == 6) {
				Set<Die> t0 = new HashSet<Die>();
				Set<Die> t1 = new HashSet<Die>();
				for (int i = 0; i < 3; ++i) {
					t0.add(triplet.get(i));
				}
				for (int i = 3; i < 6; ++i) {
					t1.add(triplet.get(i));
				}
				registerTripletScore(dieValue, selectedDice, t0, score);
				registerTripletScore(dieValue, selectedDice, t1, score);
			} else if (triplet.size() == 3) {
				registerTripletScore(dieValue, selectedDice, new HashSet<Die>(triplet), score);
			}
		}
	}

	private void registerTripletScore(int dieValue, Set<Die> selectedDice, Set<Die> triplet, TurnScore score) {
		int points;
		if (dieValue == 1)	{
			points = GreedGame.POINTS_TRIPLET_OF_ONES;
		} else {
			points = dieValue * GreedGame.POINTS_TRIPLET_FACTOR;
		}
		score.addScore(new ScoreCombination(points, new HashSet<Die>(triplet)));
		availableDice.removeAll(triplet);
		selectedDice.removeAll(triplet);
	}
	private void scoreSingles(Set<Die> selectedDice, TurnScore score) {

	}

}
