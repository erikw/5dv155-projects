package se.umu.androidcourse.erwe0033.greed.model;

import java.util.Random;

public class Die {

	protected int value;
	private Random randGen;


	public Die(Random randGen) {
		this.randGen = randGen;
		roll();
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

}
