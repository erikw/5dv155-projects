package se.umu.androidcourse.erwe0033.greed.tests;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.test.AndroidTestCase;

import se.umu.androidcourse.erwe0033.greed.*;
import se.umu.androidcourse.erwe0033.greed.GreedGame.GameOverException;

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

	public void testScoreWIthoutNewRound() {
		Set<Die> selectedDice = new HashSet<Die>();
		game.setDice(diceMock);
		try {
			game.scoreDice(selectedDice);
			fail("GreedGame did not throw exception when scoring before starting a new round.");
		} catch (GameOverException goe) {  }
	}

	public void testScoreNothing() {
		Set<Die> selectedDice = new HashSet<Die>();
		game.setDice(diceMock);
		game.newRound();

		try {
			game.scoreDice(selectedDice);
			fail("<300 points on first try means game should end.");
		} catch (GameOverException goe) { }
		TurnScore turnScore = game.getTurnScore();

		assertEquals("No dice selected, should score zero.", 0, turnScore.getTotalScore());

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should have been zero scored.", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("No score shoud have been given.", 0, scores.size());
	}

	public void testTurnScoreZero() throws GameOverException {
		Set<Die> selectedDice = new HashSet<Die>();
		for (DieMock die : diceMock) {
			selectedDice.add(die);
		}
		diceMock[0].setValue(2);
		diceMock[1].setValue(2);
		diceMock[2].setValue(3);
		diceMock[3].setValue(3);
		diceMock[4].setValue(4);
		diceMock[5].setValue(4);
		game.setDice(diceMock);
		game.newRound();

		try {
			game.scoreDice(selectedDice);
			fail("<300 points on first try means game should end.");
		} catch (GameOverException goe) { }
		TurnScore turnScore = game.getTurnScore();

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

	public void testScoreOneTriplet() {
		Set<Die> selectedDice = new HashSet<Die>();
		for (DieMock die : diceMock) {
			die.setValue(2);
			selectedDice.add(die);
		}
		diceMock[0].setValue(3);
		game.setDice(diceMock);
		game.newRound();

		try {
			game.scoreDice(selectedDice);
			fail("200<300 points on first try means game should end.");
		} catch (GameOverException goe) { }
		TurnScore turnScore = game.getTurnScore();

		assertEquals("Triplet of two's should yield 2*100", 2 * GreedGame.POINTS_TRIPLET_FACTOR, turnScore.getTotalScore());

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("3 dice should score zero points", 3, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("One triplet of two's shuld have been found", 1, scores.size());
		assertEquals("Triplet of two's should give 2*100", 2 * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(0).getScore());
	}

	public void testTurnScoreLadder() throws GameOverException {
		int dieValue = 1;
		Set<Die> selectedDice = new HashSet<Die>();
		for (DieMock die : diceMock) {
			die.setValue(dieValue++);
			selectedDice.add(die);
		}
		game.setDice(diceMock);
		game.newRound();
		TurnScore turnScore = game.scoreDice(selectedDice);

		assertEquals("Should score ladder", GreedGame.POINTS_LADDER, turnScore.getTotalScore());

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for ladder", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have one score combo for ladder.", 1, scores.size());
		assertEquals("The only score combo should be ladder.", GreedGame.POINTS_LADDER, scores.get(0).getScore());
	}

	public void testTriplets() throws GameOverException {
		int dieValue = 5;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(dieValue);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		game.newRound();
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for triplet-test", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have one score combo for one triplet.", 1, scores.size());
		assertEquals("Should have " + dieValue + " * tiplet score.", dieValue * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(0).getScore());
	}

	public void testTripletsOfOne() throws GameOverException {
		int dieValue = 1;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(dieValue);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		game.newRound();
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for triplet-test", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have one score combo for one triplet.", 1, scores.size());
		assertEquals("Should have triplet of ones score." , GreedGame.POINTS_TRIPLET_OF_ONES, scores.get(0).getScore());
	}

	public void testTwoTripletsAndContinue() throws GameOverException {
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
		game.newRound();
		TurnScore turnScore = game.scoreDice(selectedDice);

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("No dice should score zero for triplet-test", 0, zeroPointDice.size());

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have two scores for two triplets.", 2, scores.size());
		int expScore = GreedGame.POINTS_TRIPLET_OF_ONES + dieValue2 * GreedGame.POINTS_TRIPLET_FACTOR;
		assertEquals("Should have turn score of triplets of 3 and triplets of one", expScore, turnScore.getTotalScore());
		assertEquals("Should have turn score of triplets of 3 and triplets of one", expScore, game.getRoundScore());
		assertEquals("Should have total round score of triplets of 3 and triplets of one", expScore, game.getRoundScore());
		assertEquals("All dice should be availabe after using all dice.", diceMock.length, game.getAvailableDice().size());
	}

	public void testTwoTripletsOfSameValue() throws GameOverException {
		int dieValue = 2;
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < diceMock.length; ++i) {
			diceMock[i].setValue(dieValue);
			selectedDice.add(diceMock[i]);
		}
		game.setDice(diceMock);
		game.newRound();
		TurnScore turnScore = game.scoreDice(selectedDice);

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should only have two scores for two triplets.", 2, scores.size());
		assertEquals("Should have score of tripletse" , dieValue * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(0).getScore());
		assertEquals("Should have score of tripletse" , dieValue * GreedGame.POINTS_TRIPLET_FACTOR, scores.get(1).getScore());
	}

	public void testScoreSingle1() throws GameOverException {
		diceMock[0].setValue(1);
		diceMock[1].setValue(2);
		Set<Die> selectedDice = new HashSet<Die>();
		selectedDice.add(diceMock[0]);
		selectedDice.add(diceMock[1]);
		game.setDice(diceMock);
		game.newRound();
		assertEquals("All dice should be availabe before scoring.", diceMock.length, game.getAvailableDice().size());
		try {
			game.scoreDice(selectedDice);
			fail("100<300 points on first try means game should end.");
		} catch (GameOverException goe) { }
		TurnScore turnScore = game.getTurnScore();

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("One die should score zero.", 1, zeroPointDice.size());
		if (zeroPointDice.iterator().next().getValue() != 2) {
			fail("Die with value 2 should score 0");
		}

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should have one score combo for single of 1.", 1, scores.size());
		assertEquals("Should have score of single of one", GreedGame.POINTS_SINGLES_ONES, scores.get(0).getScore());
		assertEquals("5 dice should be avaialbe after scoring with one.", (diceMock.length - 1), game.getAvailableDice().size());
	}

	public void testScoreSingle5() throws GameOverException {
		int value1 = 5;
		int value2 = 4;
		diceMock[0].setValue(value1);
		diceMock[1].setValue(value2);
		Set<Die> selectedDice = new HashSet<Die>();
		selectedDice.add(diceMock[0]);
		selectedDice.add(diceMock[1]);
		game.setDice(diceMock);
		game.newRound();
		try {
			game.scoreDice(selectedDice);
			fail("50<300 points on first try means game should end.");
		} catch (GameOverException goe) { }
		TurnScore turnScore = game.getTurnScore();

		Set<Die> zeroPointDice = turnScore.getZeroPointDice();
		assertEquals("One die should score zero.", 1, zeroPointDice.size());
		if (zeroPointDice.iterator().next().getValue() != value2) {
			fail("Die with value " + value2 + " should score 0");
		}

		List<ScoreCombination> scores = turnScore.getScoreCombos();
		assertEquals("Should have one score combo for single of 5.", 1, scores.size());
		assertEquals("Should have score of single of 5.", GreedGame.POINTS_SINGLES_FIVES, scores.get(0).getScore());
	}

	public void testScoreTripletTwoSingles() throws GameOverException {
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
		game.newRound();
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

	public void testScoreLadderTripletSingle() {
		int dieValue = 1;
		Set<Die> selectedDice = new HashSet<Die>();
		for (DieMock die : diceMock) {
			die.setValue(dieValue++);
			selectedDice.add(die);
		}
		game.setDice(diceMock);
		game.newRound();
		int expRoundScore = 0;
		assertEquals("Initial round score should be zero.", expRoundScore, game.getRoundScore());
		assertEquals("All dice should be available at game start.", diceMock.length, game.getAvailableDice().size());

		TurnScore turnScore = null;
		try {
			turnScore = game.scoreDice(selectedDice);
		} catch (GameOverException goe) {
			fail("Game should continue after ladder.");
		}

		assertEquals("Should score ladder", GreedGame.POINTS_LADDER, turnScore.getTotalScore());
		expRoundScore += GreedGame.POINTS_LADDER;
		assertEquals("Round scrore not incremented after ladder", expRoundScore, game.getRoundScore());
		assertEquals("All dice should be available after ladder.", diceMock.length, game.getAvailableDice().size());

		selectedDice.clear();
		diceMock[0].setValue(2);
		for (int i = 1; i < 4; ++i) {
			diceMock[i].setValue(3);
			selectedDice.add(diceMock[i]);
		}
		diceMock[4].setValue(4);
		diceMock[5].setValue(4);
		try {
			turnScore = game.scoreDice(selectedDice);
		} catch (GameOverException goe) {
			fail("Game should continue after ladder.");
		}

		assertEquals("Should score triplet of 3s", 3 * GreedGame.POINTS_TRIPLET_FACTOR, turnScore.getTotalScore());
		expRoundScore += 3 * GreedGame.POINTS_TRIPLET_FACTOR;
		assertEquals("Round scrore not incremented after triplet", expRoundScore, game.getRoundScore());
		assertEquals("3 dice should be avilable after triplet", 3, game.getAvailableDice().size());

		selectedDice.clear();
		Iterator<Die> itr = game.getAvailableDice().iterator();
		for (int i = 0; i < 3; ++i) {
			DieMock die = (DieMock) itr.next();
			die.setValue(i);
			selectedDice.add(die);
		}
		try {
			turnScore = game.scoreDice(selectedDice);
		} catch (GameOverException goe) {
			fail("Game should continue after single score.");
		}

		assertEquals("Should score single of 1s", GreedGame.POINTS_SINGLES_ONES, turnScore.getTotalScore());
		expRoundScore += GreedGame.POINTS_SINGLES_ONES;
		assertEquals("Round scrore not incremented after single of 1s", expRoundScore, game.getRoundScore());
		assertEquals("2 dice should be available after triplet", 2, game.getAvailableDice().size());

		selectedDice.clear();
		itr = game.getAvailableDice().iterator();
		for (int i = 0; i < 2; ++i) {
			DieMock die = (DieMock) itr.next();
			die.setValue(2);
			selectedDice.add(die);
		}
		try {
			turnScore = game.scoreDice(selectedDice);
			fail("Game should stop, nothing scored.");
		} catch (GameOverException goe) { }
	}

	public void testScoreReuseOfDice() throws GameOverException {
		Set<Die> selectedDice = new HashSet<Die>();
		for (int i = 0; i < 3; ++i) {
			diceMock[i].setValue(1);
			selectedDice.add(diceMock[i]);
		}
		diceMock[3].setValue(1);
		selectedDice.add(diceMock[3]);
		game.setDice(diceMock);
		game.newRound();
		try {
			game.scoreDice(selectedDice);
		} catch (GameOverException goe) { }
		diceMock[0].setValue(5);
		diceMock[3].setValue(5);
		selectedDice.clear();
		selectedDice.add(diceMock[0]);
		selectedDice.add(diceMock[3]);
		try {
			game.scoreDice(selectedDice);
			fail("Trying to score with already scoring dice is programming error!");
		} catch (RuntimeException re) { }
	}
}


// TODO test scoreing with diece already used previously to score.
// TODO reset used dice if score using all dice e.g. two triplets, ladder
// TODO can't roll before scored anything (unless first roll).
// TODO return zeroScoreDice to the selection pool? let user play with them again?
// TODO test winning game, reaching 10000
