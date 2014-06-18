package se.umu.androidcourse.erwe0033.greed;

import se.umu.androidcourse.erwe0033.greed.model.Die;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class DieAdapter extends BaseAdapter {
    private Context context;
	private Die[] dice;

    public DieAdapter(Context context, Die[] dice) {
        this.context = context;
        this.dice = dice;
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
        DieView dieView;
        if (convertView == null) {
            dieView = new DieView(context, dice[position]);
            dieView.setLayoutParams(new GridView.LayoutParams(85, 85)); // TODO why?
            dieView.setAdjustViewBounds(true);
            dieView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            dieView = (DieView) convertView;
			dieView.setDie(dice[position]);
        }

		dieView.setImage();
        return dieView;
    }

}
