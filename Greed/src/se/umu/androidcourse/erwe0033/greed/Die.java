package se.umu.androidcourse.erwe0033.greed;

import java.util.Random;

public class Die {

	protected int value;
	private Random randGen;


	public Die(Random randGen) {
		this.value = 0;
		this.randGen = randGen;
	}

	public Die() {
		this(new Random());
	}

	public int getValue() {
		return value;
	}

	public int roll() {
		return randGen.nextInt(6) + 1;
	}

}
