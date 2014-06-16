package se.umu.androidcourse.erwe0033.greed.tests;

import java.util.Random;
import java.util.Set;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import se.umu.androidcourse.erwe0033.greed.GreedGame;
import se.umu.androidcourse.erwe0033.greed.Die;

public class GreedGameTest extends AndroidTestCase {

    public void testNewGame() {
    	GreedGame game = new GreedGame();
    	Assert.assertEquals("Total score not initially correct", 0, game.getTotalScore());
    	Assert.assertEquals("Round score not initially correct.", 0, game.getRoundScore());
    }

	private void dieInitTest(Die die) {
		Assert.assertEquals("Die initial value correct.", 0, die.getValue());
	}

    public void testNewDice() {
    	Die die = new Die();
    	dieInitTest(die);
    }

    public void testInitialDice() {
    	GreedGame game = new GreedGame();
    	Set<Die> dice = game.getDice();
    	Assert.assertEquals("Not correct number of dice.", 6, dice.size());
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
			return next;
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
}
