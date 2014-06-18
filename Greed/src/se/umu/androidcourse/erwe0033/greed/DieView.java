package se.umu.androidcourse.erwe0033.greed;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.widget.ImageView;

public class DieView extends ImageView {
	private Die die;
	private GreedActivity greedActivity;

    private static Integer[] availDieIDs = {
        R.drawable.red1, R.drawable.red2, R.drawable.red3, 
        R.drawable.red4, R.drawable.red5, R.drawable.red6, 
    };
    private static Integer[] selectedDieIDs = {
        R.drawable.white1, R.drawable.white2, R.drawable.white3, 
        R.drawable.white4, R.drawable.white5, R.drawable.white6, 
    };
    private static Integer[] usedDieIDs = {
        R.drawable.grey1, R.drawable.grey2, R.drawable.grey3, 
        R.drawable.grey4, R.drawable.grey5, R.drawable.grey6, 
    };

	public DieView(GreedActivity greedActivity, Die die) {
		super(greedActivity);	
		this.greedActivity = greedActivity;
		this.die = die;
	}

	public void setDie(Die die) {
		this.die = die;
	}

	public Die getDie() {
		return die;
	}

	public void updateImage() {
		int pos = die.getValue() - 1;
        switch (greedActivity.stateOf(die)) {
        	case AVAILABLE:
        		setImageResource(availDieIDs[pos]);
        		break;
        	case SELECTED:
        		setImageResource(selectedDieIDs[pos]);
        		break;
        	case USED:
        		setImageResource(usedDieIDs[pos]);
        		break;
        }
	}
}
