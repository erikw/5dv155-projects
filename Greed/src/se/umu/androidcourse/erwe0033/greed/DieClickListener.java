package se.umu.androidcourse.erwe0033.greed;

import java.util.Set;

import se.umu.androidcourse.erwe0033.greed.model.Die;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Handle the event when a die is selected in the GUI.
 */
public class DieClickListener implements OnItemClickListener {
	private GreedActivity greedActivity;
	private GreedGame game;
	private DieAdapter dieAdapter;
	private Set<Die> selectedDice;

	public DieClickListener(GreedActivity greedActivity, GreedGame game, DieAdapter dieAdapter, Set<Die> selectedDice) {
		this.greedActivity = greedActivity;
		this.game = game;
		this.dieAdapter = dieAdapter;
		this.selectedDice = selectedDice;
	}

	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	if (!game.isOn()) {
    		return;
    	}
        DieView dieView = (DieView) view;
        Die die = dieView.getDie();
        switch (greedActivity.stateOf(die)) {
            case SELECTED:
            	selectedDice.remove(die);
            	dieAdapter.notifyDataSetChanged();
            	break;
			case AVAILABLE:
				selectedDice.add(die);
            	dieAdapter.notifyDataSetChanged();
				break;
            case USED: return;
        }
    }
}
