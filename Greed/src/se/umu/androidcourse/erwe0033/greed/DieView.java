package se.umu.androidcourse.erwe0033.greed;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.content.Context;
import android.widget.ImageView;



public class DieView extends ImageView {
	private Die die;
	private enum DieStates {AVAILABLE, SELECTED, USED};
	private DieStates state;

    private static Integer[] availableDice = {
        R.drawable.red1, R.drawable.red2, R.drawable.red3, 
        R.drawable.red4, R.drawable.red5, R.drawable.red6, 
    };
    private static Integer[] selectedDice = {
        R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, 
        R.drawable.grey4, R.drawable.grey5, R.drawable.grey6, 
    };
    private static Integer[] usedDice = {
        R.drawable.red1, R.drawable.red2, R.drawable.red3, 
        R.drawable.red4, R.drawable.red5, R.drawable.red6, 
    };

	public DieView(Context context, Die die) {
		super(context);	
		this.die = die;
		this.state = DieStates.AVAILABLE;
	}

	public void setDie(Die die) {
		this.die = die;
	}

	public Die getDie() {
		return die;
	}

	public void select() {
		this.state = DieStates.SELECTED;
		setImage();
	}

	public void toggleSelect() {
        switch (state) {
        	case AVAILABLE:
        		state = DieStates.SELECTED;
        		break;
        	case SELECTED:
        		state = DieStates.AVAILABLE;
        		break;
        	default:
        }
		setImage();	
	}

	public void used() {
		this.state = DieStates.USED;
		setImage();
	}

	public void setImage() {
		int pos = die.getValue() - 1;
        switch (state) {
        	case AVAILABLE:
        		setImageResource(availableDice[pos]);
        		break;
        	case SELECTED:
        		setImageResource(selectedDice[pos]);
        		break;
        	case USED:
        		setImageResource(usedDice[pos]);
        		break;
        }
	}

}
