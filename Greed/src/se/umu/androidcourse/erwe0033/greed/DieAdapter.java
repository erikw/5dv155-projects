package se.umu.androidcourse.erwe0033.greed;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Presents the underlaying dice in the GUI by creating views for the dice repsenting their state.
 */
public class DieAdapter extends BaseAdapter {
    private GreedActivity greedActivity;
	private Die[] dice;

	private final String TAG = this.getClass().getName();

    public DieAdapter(GreedActivity greedActivity, Die[] dice) {
        this.greedActivity = greedActivity;
        this.dice = dice;
    }

    @Override
    public int getCount() {
        return dice.length;
    }

    @Override
    public Object getItem(int position) {
        return dice[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DieView dieView;
		if (convertView == null) {
            dieView = new DieView(greedActivity, dice[position]);
            dieView.setLayoutParams(new GridView.LayoutParams(85, 85));
            dieView.setAdjustViewBounds(true);
            dieView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			dieView = (DieView) convertView;
			dieView.setDie(dice[position]);
		}
		dieView.updateImage();
        return dieView;
    }
}
