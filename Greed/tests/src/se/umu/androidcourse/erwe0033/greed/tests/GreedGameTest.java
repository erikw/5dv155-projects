package se.umu.androidcourse.erwe0033.greed.tests;

import java.util.Set;

import android.test.AndroidTestCase;
import static junit.framework.Assert.*;

import se.umu.androidcourse.erwe0033.greed.GreedGame;
import se.umu.androidcourse.erwe0033.greed.Die;

public class GreedGameTest extends AndroidTestCase {

    public void testNewGame() {
    	GreedGame game = new GreedGame();
    	assertEquals("Total score not initially correct", 0, game.getTotalScore());
    	assertEquals("Round score not initially correct.", 0, game.getRoundScore());
    }

	private void dieInitTest(Die die) {
		assertEquals("Die initial value correct.", 0, die.getValue());
	}

    public void testNewDice() {
    	Die die = new Die();
    	dieInitTest(die);
    }

    public void testInitialDice() {
    	GreedGame game = new GreedGame();
    	Set<Die> dice = game.getDice();
    	assertEquals("Not correct number of dice.", 6, dice.size());
    	for (Die die : dice) {
    		dieInitTest(die);
    	}
    }
}
