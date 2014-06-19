package se.umu.androidcourse.erwe0033.greed.model;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class GreedGame {

	private int roundScore;
	private Stack<TurnScore> turnScores;
	private Die[] allDice;
	private Set<Die> availableDice;
	private boolean gameIsOn;

	private static final int LADDER_SUM = 1 + 2 + 3 + 4 + 5 + 6;

	public static final int POINTS_TRIPLET_FACTOR = 100;
	public static final int POINTS_TRIPLET_OF_ONES = 1000;
	public static final int POINTS_SINGLES_ONES = 100;
	public static final int POINTS_SINGLES_FIVES = 50;
	public static final int POINTS_LADDER = 1000;
	public static final int POINTS_CUT = 300;
	//public static final int POINTS_WINNER = 10000;
	public static final int POINTS_WINNER = 500;


	public class GameOverException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public class GameWonException extends Exception {
		private static final long serialVersionUID = 1L;
	}


	public GreedGame() {
		roundScore = 0;
		turnScores = new Stack<TurnScore>();
		allDice = new Die[6];
		for (int i = 0; i < allDice.length; ++i) {
			allDice[i] = new Die();
		}
		availableDice = new HashSet<Die>();
		gameIsOn = false;
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

	public boolean isOn() {
		return gameIsOn;
	}

	public TurnScore getTurnScore() {
		try {
			return turnScores.peek();
		} catch (EmptyStackException ese) {
			return null;
		}
	}

	public int getNoTurnsTaken() {
		return turnScores.size();
	}

	public Die[] getAllDice() {
		return allDice;
	}

	public Set<Die> getAvailableDice() {
		return availableDice;
	}

	public void newRound() {
		roundScore = 0;
		gameIsOn = true;
		availableDice.clear();
		turnScores.clear();
		for (Die die : this.allDice) {
			availableDice.add(die);
		}
	}

	public TurnScore scoreDice(Set<Die> selectedDice) throws GameOverException, GameWonException {
		if (!gameIsOn) {
			throw new GameOverException();
		}
		checkForReuse(selectedDice);

		Set<Die> dice = new HashSet<Die>(selectedDice); // Don't modify original.
		TurnScore score = new TurnScore();
		if (!scoreLadder(dice, score)) {
			scoreTriplets(dice, score);
			scoreSingles(dice, score);
		}
		score.addZeroScoreDice(dice);
		turnScores.push(score);
		this.roundScore += score.getTotalScore();
		if (availableDice.size() == 0) {
			for (Die die : this.allDice) {
				die.roll();
				availableDice.add(die);
			}
		}

		if (score.getTotalScore() == 0 || this.turnScores.size() == 1 && score.getTotalScore() < GreedGame.POINTS_CUT) {
			this.gameIsOn = false;
			throw new GameOverException();
		} else if (this.roundScore >= GreedGame.POINTS_WINNER) {
			this.gameIsOn = false;
			throw new GameWonException();
		}
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
		Map<Integer, LinkedList<Die>> triplets = new HashMap<Integer, LinkedList<Die>>();
		for (Die die : selectedDice) {
			int value = die.getValue();
			if (!triplets.containsKey(value)) {
				triplets.put(value, new LinkedList<Die>());
			}
			triplets.get(value).add(die);
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
			} else if (triplet.size() >= 3) {
				Set<Die> t0 = new HashSet<Die>();
				for (int i = 0; i < 3; ++i) {
					t0.add(triplet.get(i));
				}
				registerTripletScore(dieValue, selectedDice, t0, score);
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
		Iterator<Die> itr = selectedDice.iterator();
		while (itr.hasNext()) {
			Die die = itr.next();
			int value = die.getValue();
			if (value == 1 || value == 5) {
				Set<Die> comboSet = new HashSet<Die>();
				comboSet.add(die);
				int points = (value == 1) ? GreedGame.POINTS_SINGLES_ONES : GreedGame.POINTS_SINGLES_FIVES;
				score.addScore(new ScoreCombination(points, comboSet));
				availableDice.remove(die);
				itr.remove();
			}
		}
	}
}
