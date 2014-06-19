// TODO Handle saveinstances?
// TODO comment code lightly

package se.umu.androidcourse.erwe0033.greed;

import java.util.HashSet;
import java.util.Set;

import se.umu.androidcourse.erwe0033.greed.model.Die;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame.GameOverException;
import se.umu.androidcourse.erwe0033.greed.model.GreedGame.GameWonException;
import se.umu.androidcourse.erwe0033.greed.model.ScoreCombination;
import se.umu.androidcourse.erwe0033.greed.model.TurnScore;

import android.app.Activity;
import android.content.Intent;
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
	private TextView roundScoreLabel;
	private TextView roundScoreText;
	private TextView numberTurnsLabel;
	private TextView numberTurnsText;
	private TextView turnScoresLabel;
	private TextView turnScoresText;
	private StringBuilder turnScoresBuilder;
	private Button restartButton;
	private Button rollButton;
	private Button scoreButton;
	public enum DieState {AVAILABLE, SELECTED, USED};

	// Package visibility.
	static final String INTENT_ROUND_SCORE = "se.umu.androidcourse.erwe0033.greed.round_score";
	static final String INTENT_NO_TURNS =  "se.umu.androidcourse.erwe0033.greed.no_turns";

	private final String TAG = this.getClass().getName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greed_activity);

        this.selectedDice = new HashSet<Die>();
        this.turnScoresBuilder = new StringBuilder();
        this.game = new GreedGame();

    	int dieCount = 1;
    	for (Die die : game.getAllDice()) {
			die.setValue(dieCount++);
    	}

		this.dieAdapter = new DieAdapter(this, game.getAllDice());
		this.roundScoreLabel= (TextView) findViewById(R.id.round_score_label);
		this.roundScoreText = (TextView) findViewById(R.id.round_score);
		this.numberTurnsLabel = (TextView) findViewById(R.id.number_turns_label);
		this.numberTurnsText = (TextView) findViewById(R.id.number_turns);
		this.turnScoresLabel = (TextView) findViewById(R.id.turn_scores_label);
		this.turnScoresText = (TextView) findViewById(R.id.turn_scores);
		this.restartButton = (Button) findViewById(R.id.restart_button);
		this.rollButton = (Button) findViewById(R.id.roll_button);
		this.scoreButton = (Button) findViewById(R.id.score_button);

		roundScoreLabel.setVisibility(View.GONE);
		roundScoreText.setVisibility(View.GONE);
		numberTurnsLabel.setVisibility(View.GONE);
		numberTurnsText.setVisibility(View.GONE);
		turnScoresLabel.setVisibility(View.GONE);


    	GridView gridView = (GridView) findViewById(R.id.dice_grid);
    	gridView.setAdapter(this.dieAdapter);
    	gridView.setOnItemClickListener(new DieClickListener(this, game, dieAdapter, selectedDice));

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
		roundScoreLabel.setVisibility(View.VISIBLE);
		roundScoreText.setVisibility(View.VISIBLE);
		numberTurnsLabel.setVisibility(View.VISIBLE);
		numberTurnsText.setVisibility(View.VISIBLE);
		turnScoresLabel.setVisibility(View.VISIBLE);
        rollButton.setVisibility(View.VISIBLE);
        scoreButton.setVisibility(View.VISIBLE);
		roundScoreText.setText("0");
		numberTurnsText.setText("0");
		turnScoresText.setText("");
		turnScoresBuilder.setLength(0);
        Toast.makeText(this, "New game started.", Toast.LENGTH_SHORT).show();
    }

	private boolean nothingSelected() {
		if (selectedDice.size() == 0) {
        	Toast.makeText(this, "You must select at least one die.", Toast.LENGTH_SHORT).show();
        	return true;
        } else {
        	return false;
        }
	}

    public void onRollClick(View view) {
    	if (!game.isOn()) {
        	Toast.makeText(this, "Game is not started yet!", Toast.LENGTH_SHORT).show();
        	return;
    	} else if (nothingSelected()) {
			return;
		} else {
			Log.v(TAG, "Number selected dice:" + selectedDice.size());
			for (Die die : selectedDice) {
				die.roll();
			}
			selectedDice.clear();
        	dieAdapter.notifyDataSetChanged();
        	rollButton.setVisibility(View.GONE);
        }
    }

    public void onScoreClick(View view) {
    	if (!game.isOn()) {
        	Toast.makeText(this, "Game is not started yet!", Toast.LENGTH_SHORT).show();
        	return;
    	}
		if (nothingSelected()) {
			return;
		}
		try {
			TurnScore turnScore = game.scoreDice(selectedDice);
        	rollButton.setVisibility(View.VISIBLE);
			Toast.makeText(this, turnScore.getTotalScore() + " points! ", Toast.LENGTH_SHORT).show();
        	updateTurnScoreBoard(turnScore);
		} catch (GameOverException goe) {
			String message;
			if (game.getNoTurnsTaken() == 1) {
				message = "<300 points on first round.";
			} else {
				message = "No points scored.";
			}
			message += " Game over!";
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        	scoreButton.setVisibility(View.GONE);
        	rollButton.setVisibility(View.GONE);
			TurnScore turnScore = game.getTurnScore();
        	updateTurnScoreBoard(turnScore);
		} catch (GameWonException gwe) {
			Toast.makeText(this, "Game won!", Toast.LENGTH_SHORT).show();
        	scoreButton.setVisibility(View.GONE);
        	rollButton.setVisibility(View.GONE);
        	Intent winIntent = new Intent(GreedActivity.this, WinActivity.class);
        	winIntent.putExtra(GreedActivity.INTENT_ROUND_SCORE, game.getRoundScore());
        	winIntent.putExtra(GreedActivity.INTENT_NO_TURNS, game.getNoTurnsTaken());
        	startActivity(winIntent);
		}
		selectedDice.clear();
        dieAdapter.notifyDataSetChanged();

		roundScoreText.setText(Integer.toString(game.getRoundScore()));
		numberTurnsText.setText(Integer.toString(game.getNoTurnsTaken()));
    }

    private void updateTurnScoreBoard(TurnScore score) {
    	for (ScoreCombination combo : score.getScoreCombos()) {
			for (Die die : combo.getDice()) {
				turnScoresBuilder.append(String.format("[%d] ", die.getValue()));
			}
			turnScoresBuilder.append(String.format("= %dp\n", combo.getScore()));
    	}
    	turnScoresText.setText(turnScoresBuilder.toString());
    }
}
