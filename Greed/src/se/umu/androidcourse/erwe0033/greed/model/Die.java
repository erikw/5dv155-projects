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

	public Die(int value) {
		this();
		validValue(value);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	private void validValue(int value) {
		if (!(value >= 1 && value <= 6)) {
			throw new RuntimeException(value + "is not a valid die value.");
		}
	}

	public void setValue(int value) {
		validValue(value);
		this.value = value;
	}

	public int roll() {
		this.value = randGen.nextInt(6) + 1;
		return this.value;
	}


	public String toString() {
		return "Die #" + dieID + " with value " + value;
	}

}
