package se.umu.androidcourse.erwe0033.greed.tests;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.test.AndroidTestCase;

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
    	assertEquals("Total score not initially correct", 0, game.getRoundScore());
    	assertNull("No turn score should be available yet.", game.getTurnScore());
    	game.newRound();
    	assertEquals("Total score not rested after new round", 0, game.getRoundScore());
    }

	private void dieInitTest(Die die) {
		assertEquals("Die initial value correct.", 0, die.getValue());
	}

    public void testNewDice() {
    	Die die = new Die();
    	dieInitTest(die);
    }

    public void testInitialDice() {
    	Die[] dice = game.getAllDice();
    	assertEquals("Not correct number of dice.", 6, dice.length);
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
			assertEquals("Roll value wrong.", nbr.intValue(), rollValue);
		}
		int rollValue = die.roll();
		assertEquals("Roll value wrong.", sequence[0], rollValue);
	}

	public void testScoreNothing() {
		Set<Die> selectedDice = new HashSet<Die>();
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		assertEquals("No dice selected, should score zero.", 0, turnScore.getTotalScore());

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should have been zero scored.", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("No score shoud have been given.", 0, scores.size());
	}

	public void testTurnScoreZero() {
		Set<Die> selectedDice = new HashSet<Die>();
		for (DieMock die : diceMock) {
			die.setValue(2);
			selectedDice.add(die);
		}
		diceMock[0].setValue(3);
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		assertEquals("All dice should score zero.", 0, turnScore.getTotalScore());

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("All dice should be zero scores.", diceMock.length, zeroPointDice.size());
		for (Die die : diceMock) {
			if (!zeroPointDice.contains(die)) {
				fail("Missing MockDie in zero score dice.");
			}
		}

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("No score shoud have been given.", 0, scores.size());
	}

	public void testTurnScoreLadder() {
		int dieValue = 1;
		Set<Die> selectedDice = new HashSet<Die>();
		for (DieMock die : diceMock) {
			die.setValue(dieValue++);
			selectedDice.add(die);
		}
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		assertEquals("Should score ladder", GreedGame.POINTS_LADDER, turnScore.getTotalScore());

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for ladder", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have one score combo for ladder.", 1, scores.size());
		assertEquals("The only score combo should be ladder.", GreedGame.POINTS_LADDER, scores.get(0).getScore());
	}

	public void testTriplets() {
		int dieValue = 5;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(dieValue);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for triplet-test", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have one score combo for one triplet.", 1, scores.size());
		assertEquals("Should have " + dieValue + " * tiplet score.", dieValue * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(0).getScore());
	}

	public void testTripletsOfOne() {
		int dieValue = 1;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(dieValue);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for triplet-test", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have one score combo for one triplet.", 1, scores.size());
		assertEquals("Should have triplet of ones score." , GreedGame.POINTS_TRIPLET_OF_ONES, scores.get(0).getScore());
	}

	public void testTwoTriplets() {
		int dieValue1 = 1;
		int dieValue2 = 3;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(dieValue1);
			selectedDice.add(diceMock[i]);
		}
		for (int i = 3; i < 6; ++i) {
			diceMock[i].setValue(dieValue2);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for triplet-test", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have two scores for two triplets.", 2, scores.size());
		assertEquals("Should have score of triplets of 3 and triplets of one" , (GreedGame.POINTS_TRIPLET_OF_ONES + dieValue2 * GreedGame.POINTS_TRIPLET_FACTOR), turnScore.getTotalScore());
	}

	public void testTwoTripletsOfSameValue() {
		int dieValue = 2;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < diceMock.length; ++i) {
			diceMock[i].setValue(dieValue);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have two scores for two triplets.", 2, scores.size());
		assertEquals("Should have score of tripletse" , dieValue * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(0).getScore());
		assertEquals("Should have score of tripletse" , dieValue * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(1).getScore());
	}

	public void testScoreSingle1() {
		diceMock[0].setValue(1);
		diceMock[1].setValue(2);
		Set<Die> selectedDice = new HashSet<Die>();
		selectedDice.add(diceMock[0]);
		selectedDice.add(diceMock[1]);
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("One die should score zero.", 1, zeroPointDice.size());
		if (zeroPointDice.iterator().next().getValue() != 2) {
			fail("Die with value 2 should score 0");
		}

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should have one score combo for single of 1.", 1, scores.size());
		assertEquals("Should have score of single of one", GreedGame.POINTS_SINGLES_ONES, scores.get(0).getScore());
	}

	public void testScoreSingle5() {
		int value1 = 5;
		int value2 = 4;
		diceMock[0].setValue(value1);
		diceMock[1].setValue(value2);
		Set<Die> selectedDice = new HashSet<Die>();
		selectedDice.add(diceMock[0]);
		selectedDice.add(diceMock[1]);
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("One die should score zero.", 1, zeroPointDice.size());
		if (zeroPointDice.iterator().next().getValue() != value2) {
			fail("Die with value " + value2 + " should score 0");
		}

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should have one score combo for single of 5.", 1, scores.size());
		assertEquals("Should have score of single of 5.", GreedGame.POINTS_SINGLES_FIVES, scores.get(0).getScore());
	}

	public void testScoreTripletTwoSingles() {
		int value1 = 1;
		int value2 = 5;
		int value3 = 6;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(value1);
			selectedDice.add(diceMock[i]);
		}
		for (int i = 3; i < 5; ++i) {
			diceMock[i].setValue(value2);
			selectedDice.add(diceMock[i]);
		}
		diceMock[5].setValue(value3);
		selectedDice.add(diceMock[5]);
		game.setDice(diceMock);
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("One die should score zero.", 1, zeroPointDice.size());
		if (zeroPointDice.iterator().next().getValue() != value3) {
			fail("Die with value " + value3 + " should score 0");
		}

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should have three score combos: triplet + single + single.", 3, scores.size());
		assertEquals("Should have combines score of: 1-triplet + 2*5singles.", GreedGame.POINTS_TRIPLET_OF_ONES + 2 * GreedGame.POINTS_SINGLES_FIVES, turnScore.getTotalScore());
	}

}


// TODO test ladder followed by triplet/single
// TODO reset used dice if score using all dice e.g. two triplets, ladder
// TODO if user press 
// TODO can't roll before scored anything.
// TODO return zeroScoreDice to the selection pool? let user play with them again?
