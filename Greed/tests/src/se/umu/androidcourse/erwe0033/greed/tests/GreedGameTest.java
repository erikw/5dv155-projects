package se.umu.androidcourse.erwe0033.greed.tests;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import se.umu.androidcourse.erwe0033.greed.*;

public class GreedGameTest extends AndroidTestCase {
	private GreedGame game;
	private DieMock[] diceMock;

    public void setUp() {
    	game = new GreedGame();
		diceMock = new DieMock[6];
		for (int i = 0; i < diceMock.length; ++i) {
			diceMock[i] = new DieMock(1);
		}
    }

    public void tearDown() {
		diceMock = null;
		game = null;
    }

    public void testNewGame() {
    	Assert.assertEquals("Total score not initially correct", 0, game.getRoundScore());
    	Assert.assertEquals("Round score not initially correct.", 0, game.getTurnScore());
    	game.newRound();
    	Assert.assertEquals("Total score not rested after new round", 0, game.getRoundScore());
    	game.nextTurn();
    	Assert.assertEquals("Turn score not rested with new turn", 0, game.getTurnScore());
    }

	private void dieInitTest(Die die) {
		Assert.assertEquals("Die initial value correct.", 0, die.getValue());
	}

    public void testNewDice() {
    	Die die = new Die();
    	dieInitTest(die);
    }

    public void testInitialDice() {
    	Die[] dice = game.getAllDice();
    	Assert.assertEquals("Not correct number of dice.", 6, dice.length);
    	for (Die die : dice) {
    		dieInitTest(die);
    	}
    }


	public class RandomMock extends Random {
		private static final long serialVersionUID = 0;

		private int[] sequence;
		private int index;

		public RandomMock(int[] sequence) {
			this.sequence = sequence;
			this.index = 0;
		}

		public int nextInt(int n) {
			int next = sequence[index];
			index = ++index % sequence.length;
			return next - 1; // Die.roll() will add one to put number in range.
		}
	}

	public class DieMock extends Die {
		public DieMock(int value) {
			super();
			this.value = value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	public void testDieRoll() {
		int[] sequence = new int[] {1,3,5,6,2,3};
		Die die = new Die(new RandomMock(sequence));
		for (Integer nbr : sequence) {
			int rollValue = die.roll();
			Assert.assertEquals("Roll value wrong.", nbr.intValue(), rollValue);
		}
		int rollValue = die.roll();
		Assert.assertEquals("Roll value wrong.", sequence[0], rollValue);
	}

	public void testZeroRoundScorePeek() {
		for (DieMock die : diceMock) {
			die.setValue(2);
		}
		game.setDice(diceMock);
		RoundScore possibleScore = game.calcMaxRoundScore();
		assertEquals("Should not have points for only twos in round score.", 0, possibleScore.getTotalScore());

		Set<Die> zeroScoreDice = possibleScore.getZeroScoreDice();
		assertEquals("All twos should yield 0 points.", diceMock.length, zeroScoreDice.size());

		List<ScoreCombination> scores = possibleScore.getScoreCombos();
		assertEquals("No score shoud be given for only twos", 0, scores.size()); 
	}
}
