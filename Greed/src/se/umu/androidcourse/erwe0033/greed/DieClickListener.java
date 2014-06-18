package se.umu.androidcourse.erwe0033.greed;

import java.util.Set;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class DieClickListener implements OnItemClickListener {
	private GreedActivity greedActivity;
	private DieAdapter dieAdapter;
	private Set<Die> selectedDice;

	public DieClickListener(GreedActivity greedActivity, DieAdapter dieAdapter, Set<Die> selectedDice) {
		this.greedActivity = greedActivity;
		this.dieAdapter = dieAdapter;
		this.selectedDice = selectedDice;
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

