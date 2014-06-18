package se.umu.androidcourse.erwe0033.greed;

import java.util.Set;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.content.Context;
import android.widget.ImageView;



public class DieView extends ImageView {
	private Die die;
	private enum DieStates {AVAILABLE, SELECTED, USED};
	private DieStates state;
	private Set<Die> selectedDice;
	private DieAdapter dieAdapter;
	private static int count = 0;
	private int id;

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

	public DieView(Context context, DieAdapter dieAdapter, Set<Die> selectedDice, Die die) {
		super(context);	
		this.dieAdapter = dieAdapter;
		this.selectedDice = selectedDice;
		this.die = die;
		this.state = DieStates.USED;
		this.id = count++;
	}

	public void setDie(Die die) {
		this.die = die;
	}

	public Die getDie() {
		return die;
	}

	public void select() {
		if (state != DieStates.USED) {
			this.state = DieStates.SELECTED;
        	selectedDice.add(die);
			setImage();
		}
		dieAdapter.notifyDataSetChanged();
	}

	public void unselect() {
		if (state != DieStates.USED) {
			this.state = DieStates.AVAILABLE;
        	selectedDice.remove(die);
			setImage();
		}
		dieAdapter.notifyDataSetChanged();
	}

	public void makeAvailable() {
		this.state = DieStates.AVAILABLE;
        selectedDice.remove(die);
		setImage();
		dieAdapter.notifyDataSetChanged();
	}

	public void toggleSelect() {
        switch (state) {
        	case AVAILABLE: select(); break;
        	case SELECTED: unselect(); break;
        	default:
        }
	}

	public void used() {
		this.state = DieStates.USED;
		setImage();
		dieAdapter.notifyDataSetChanged();
	}

	public void setImage() {
		int pos = die.getValue() - 1;
        switch (state) {
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

	public String toString() {
		return "DieViewID= " + id;
	}
}
