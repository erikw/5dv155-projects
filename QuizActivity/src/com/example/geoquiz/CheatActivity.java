package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CheatActivity extends Activity {
	private Button yesButton;
	private Button noButton;
	private boolean questionAnswer;

	private final String TAG = this.getClass().getName();
	public static final String VIEWED_CHEAT = "com.example.geoquiz.viewed_cheat";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		yesButton = (Button) findViewById(R.id.cheat_yes_button);
		yesButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.d(TAG, "Clicked yes");
				//boolean isTrueQuestion = mAnswerKey[mCurrentIndex].is
				Toast.makeText(CheatActivity.this, "Answer is: " + questionAnswer, Toast.LENGTH_LONG).show();
				sendResult(true);
			}
		});
		noButton = (Button) findViewById(R.id.cheat_no_button);
		noButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.d(TAG, "Clicked no");
				sendResult(false);
			}
		});
	}

	protected void onResume() {
		super.onResume();
		Log.v(TAG, "In resume on cheat activity");
		Intent intent = getIntent();
		if (intent != null) {
			questionAnswer = intent.getBooleanExtra(QuizActivity.INTENT_Q_ANSWER, false);
		}
	}

	private void sendResult(boolean viewedCheat) {
		Intent result = new Intent();
		result.putExtra(CheatActivity.VIEWED_CHEAT, viewedCheat);
		setResult(RESULT_OK, result);
		finish();
	}
}
