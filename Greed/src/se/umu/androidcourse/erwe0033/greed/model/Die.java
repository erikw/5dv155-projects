package se.umu.androidcourse.erwe0033.greed.model;

import java.util.Random;

public class Die {

	protected int value;
	private Random randGen;
	private static int dieCount = 0;
	private int dieID; // For debugging



	public Die(Random randGen) {
		this.randGen = randGen;
		this.value = 1;
		this.dieID = dieCount++;
		//roll();
	}

	public Die() {
		this(new Random());
	}

	public int getValue() {
		return value;
	}

	public int roll() {
		this.value = randGen.nextInt(6) + 1;
		return this.value;
	}


	public String toString() {
		return "Die #" + dieID + " with value " + value;
	}

}
