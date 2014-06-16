package se.umu.androidcourse.erwe0033.greed;

public class GreedGame {

	private int roundScore;
	private int turnScore;
	private Die[] dice;


	public GreedGame() {
		newRound();
		nextTurn();
		dice = new Die[6];
		for (int i = 0; i < dice.length; ++i) {
			dice[i] = new Die();
		}
	}

	public void setDice(Die[] dice) {
		this.dice = dice;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public int getTurnScore() {
		return turnScore;
	}

	public Die[] getAllDice() {
		return dice;
	}

	public void newRound() {
		roundScore = 0;
	}

	public void nextTurn() { // TODO select which dices to use
		roundScore += turnScore;
		turnScore = 0;
	}

	public RoundScore calcMaxRoundScore() {
		return null;
	}
}
