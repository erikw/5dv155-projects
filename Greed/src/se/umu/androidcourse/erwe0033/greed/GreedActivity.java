// TODO make scalable app icon and die images.
// TODO better (transparent icon)
// TODO change button Roll to Score when first selected
// TODO Give user chans to roll all dice at first turn.
// TODO model's data is not propagated to the GUI. if all dice were used gui should be updated to make them available.
// TODO split action of selecting for roll and selecting for roll.

package se.umu.androidcourse.erwe0033.greed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import se.umu.androidcourse.erwe0033.greed.model.Die;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame.GameOverException;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame.GameWonException;
import se.umu.androidcourse.erwe0033.greed.model.TurnScore;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GreedActivity extends Activity
{
	private Set<Die> selectedDice;
	private Map<Die, DieView> diceMap;
	private GreedGame game;
	private DieAdapter dieAdapter;
	private TextView roundScoreText;
	private TextView numberTurnsText;

	private final String TAG = this.getClass().getName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greed_activity);

        this.selectedDice = new HashSet<Die>();
        this.diceMap = new HashMap<Die, DieView>();
        this.game = new GreedGame();
        this.game.newRound();
		this.dieAdapter = new DieAdapter(this, game.getAllDice(), selectedDice, diceMap);
		this.roundScoreText = (TextView) findViewById(R.id.round_score);
		this.numberTurnsText = (TextView) findViewById(R.id.number_turns);

    	GridView gridView = (GridView) findViewById(R.id.dice_grid);
    	gridView.setAdapter(this.dieAdapter);
    	gridView.setOnItemClickListener(new DieClickListener());

        //Button rollScoreButton = (Button) findViewById(R.id.roll_score_button);
    }


    private static class DieClickListener implements OnItemClickListener {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DieView dieView = (DieView) view;
                dieView.toggleSelect();
				
            }
    }

    public void onRollScoreClick(View view) {
		if (selectedDice.size() == 0) {
        	Toast.makeText(this, "You must select at least one die.", Toast.LENGTH_SHORT).show();
		} else {
			Log.v(TAG, "Number selected dice:" + selectedDice.size());
			for (Die die : selectedDice) {
				die.roll();
			}
			TurnScore turnScore = null;
			try {
				turnScore = game.scoreDice(selectedDice);
			} catch (GameOverException goe) {
        		Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();
        		return;
			} catch (GameWonException gwe) {
        		Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();
        		return;
			}
			Set<Die> availDice = game.getAvailableDice();
			for (Die die : game.getAllDice()) {
				DieView dieView = diceMap.get(die);
				if (availDice.contains(die)) {
					dieView.unselect();
				} else {
					dieView.used();
				}
			}
			selectedDice.clear();
			roundScoreText.setText(Integer.toString(game.getRoundScore()));
			numberTurnsText.setText(Integer.toString(game.getNoTurnsTaken()));
		}
    }

}
