// TODO make scalable app icon and die images.
// TODO better (transparent icon)
// TODO result activity
// TODO turn scores list with points given.
// TODO should not have to roll each turn..... if they are good, may not want to roll
// TODO small legend with scoring rules?
// TODO disable landscape mode?

package se.umu.androidcourse.erwe0033.greed;

import java.util.HashSet;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GreedActivity extends Activity
{
	private GreedGame game;
	private Set<Die> selectedDice;
	private DieAdapter dieAdapter;
	private TextView roundScoreText;
	private TextView numberTurnsText;
	private Button restartButton;
	private Button rollButton;
	private Button scoreButton;
	public enum DieState {AVAILABLE, SELECTED, USED};

	private final String TAG = this.getClass().getName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greed_activity);

        this.selectedDice = new HashSet<Die>();
        this.game = new GreedGame();
		this.dieAdapter = new DieAdapter(this, game.getAllDice());
		this.roundScoreText = (TextView) findViewById(R.id.round_score);
		this.numberTurnsText = (TextView) findViewById(R.id.number_turns);
		this.restartButton = (Button) findViewById(R.id.restart_button);
		this.rollButton = (Button) findViewById(R.id.roll_button);
		this.scoreButton = (Button) findViewById(R.id.score_button);
		//scoreButton.setVisibility(View.GONE);
		//scoreButton.setVisibility(View.GONE);

    	GridView gridView = (GridView) findViewById(R.id.dice_grid);
    	gridView.setAdapter(this.dieAdapter);
    	gridView.setOnItemClickListener(new DieClickListener(this, dieAdapter, selectedDice));
    }

    public DieState stateOf(Die die) {
        if (!game.getAvailableDice().contains(die)) {
			return DieState.USED;
        } else if (selectedDice.contains(die)) {
            return DieState.SELECTED;
        } else {
            return DieState.AVAILABLE;
        }
    }

    public void onRestartClick(View view) {
        //Log.v(TAG, "restarting...");
        game.newRound();
        selectedDice.clear();
        for (Die die : game.getAllDice()) {
        	die.roll();
        }
        dieAdapter.notifyDataSetChanged();

        restartButton.setText("Restart");
        rollButton.setVisibility(View.VISIBLE);
        scoreButton.setVisibility(View.GONE);
		roundScoreText.setText("0");
		numberTurnsText.setText("0");
        Toast.makeText(this, "New game started.", Toast.LENGTH_SHORT).show();
    }

    public void onRollClick(View view) {
    	if (!game.gameIsOn()) {
        	Toast.makeText(this, "Game is not started yet!", Toast.LENGTH_SHORT).show();
        	return;
    	}
		if (selectedDice.size() == 0) {
        	Toast.makeText(this, "You must select at least one die.", Toast.LENGTH_SHORT).show();
		} else {
			Log.v(TAG, "Number selected dice:" + selectedDice.size());
			for (Die die : selectedDice) {
				die.roll();
			}
			selectedDice.clear();
        	dieAdapter.notifyDataSetChanged();
        	rollButton.setVisibility(View.GONE);
        	scoreButton.setVisibility(View.VISIBLE);
        }

    }

    public void onScoreClick(View view) {
    	if (!game.gameIsOn()) {
        	Toast.makeText(this, "Game is not started yet!", Toast.LENGTH_SHORT).show();
        	return;
    	}
		TurnScore turnScore = null;
		try {
			turnScore = game.scoreDice(selectedDice);
        	scoreButton.setVisibility(View.GONE);
        	rollButton.setVisibility(View.VISIBLE);
		} catch (GameOverException goe) {
			Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();
        	scoreButton.setVisibility(View.GONE);
        	rollButton.setVisibility(View.GONE);
		} catch (GameWonException gwe) {
			Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();
        	scoreButton.setVisibility(View.GONE);
        	rollButton.setVisibility(View.GONE);
		}
		selectedDice.clear();
        dieAdapter.notifyDataSetChanged();

		roundScoreText.setText(Integer.toString(game.getRoundScore()));
		numberTurnsText.setText(Integer.toString(game.getNoTurnsTaken()));
    }

}
