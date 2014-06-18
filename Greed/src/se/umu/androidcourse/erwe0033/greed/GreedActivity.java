// TODO make scalable app icon and die images.
// TODO better (transparent icon)

package se.umu.androidcourse.erwe0033.greed;

import se.umu.androidcourse.erwe0033.greed.model.GreedGame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GreedActivity extends Activity
{
	private GreedGame game;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greed_activity);

        this.game = new GreedGame();

    	GridView gridview = (GridView) findViewById(R.id.dice_grid);
    	gridview.setAdapter(new DieAdapter(this, game.getAllDice()));

    	gridview.setOnItemClickListener(new DieClickListener());
    }

    private class DieClickListener implements OnItemClickListener {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DieView dieView = (DieView) view;
                dieView.toggleSelect();
            }
    }
}
