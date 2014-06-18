package se.umu.androidcourse.erwe0033.greed;

import java.util.Map;
import java.util.Set;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class DieAdapter extends BaseAdapter {
    private Context context;
	private Die[] dice;
	private Set<Die> selectedDice;
	private Map<Die, DieView> diceMap;

	private final String TAG = this.getClass().getName();

    public DieAdapter(Context context, Die[] dice, Set<Die> selectedDice, Map<Die, DieView> diceMap) {
        this.context = context;
        this.dice = dice;
        this.selectedDice = selectedDice;
        this.diceMap = diceMap;
    }

    public int getCount() {
        return dice.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.v(TAG, "getView(" + position + ")");
        DieView dieView;
		if (convertView == null) {
            dieView = new DieView(context, this, selectedDice, dice[position]);
            dieView.setLayoutParams(new GridView.LayoutParams(85, 85)); // TODO why?
            dieView.setAdjustViewBounds(true);
            dieView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			dieView = (DieView) convertView;
			dieView.setDie(dice[position]);
		}
		diceMap.put(dieView.getDie(), dieView);
		dieView.setImage();
        return dieView;
    }

}
