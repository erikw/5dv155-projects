package se.umu.androidcourse.erwe0033.greed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WinActivity extends Activity
{
	private final String TAG = this.getClass().getName();
	private int roundScore;
	private int noTurns;
	private TextView roundScoreText;
	private TextView noTurnsText;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_activity);

        roundScoreText = (TextView) findViewById(R.id.win_round_score);
        noTurnsText = (TextView) findViewById(R.id.win_number_turns);
    }

    @Override
    public void onResume() {
    	super.onResume();
    	Intent intent = getIntent();
    	if (intent != null) {
			roundScore = intent.getIntExtra(GreedActivity.INTENT_ROUND_SCORE, 0);
			noTurns = intent.getIntExtra(GreedActivity.INTENT_NO_TURNS, 0);
			roundScoreText.setText(Integer.toString(roundScore));
			noTurnsText.setText(Integer.toString(noTurns));
    	}
    }

    public void onReturnClick(View view) {
    	finish();
    }
}
