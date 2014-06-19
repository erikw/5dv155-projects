package se.umu.androidcourse.erwe0033.greed.model;

import java.util.Random;

/**
 * Representation of a die.
 */
public class Die {
	private int value;
	private Random randGen;

	public Die(Random randGen) {
		this.randGen = randGen;
		this.value = 1;
	}

	public Die() {
		this(new Random());
	}

	public Die(int value) {
		this();
		validValue(value);
		this.value = value;
	}

	/**
	 * Get the value of this die.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Is the value a valid die value?
	 */
	private void validValue(int value) {
		if (!(value >= 1 && value <= 6)) {
			throw new RuntimeException(value + "is not a valid die value.");
		}
	}

	/**
	 * Set a new value for this die.
	 */
	public void setValue(int value) {
		validValue(value);
		this.value = value;
	}

	/**
	 * Roll the die.
	 */
	public int roll() {
		this.value = randGen.nextInt(6) + 1;
		return this.value;
	}

	@Override
	public String toString() {
		return "Die with value " + value;
	}
}
