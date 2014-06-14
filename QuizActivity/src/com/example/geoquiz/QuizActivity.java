package com.example.geoquiz;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
	private Button mTrueButton;  
 	private Button mFalseButton;
 	private ImageButton mNextButton;
 	private ImageButton mPrevButton;
 	private Button cheatButton;
 	private NextListener nextListener;
 	private boolean viewedCheat;
 	

 	private final String TAG = this.getClass().getName();
 	private static final String STATE_INDEX = "current_index_state";
 	private static final String STATE_CHEATED = "did_cheat_state";
 	private static final int INTENT_VIEW_CHEAT = 0x1;
 	public static final String INTENT_Q_ANSWER = "com.exmaple.geoquiz.question_answer";

 	private TextView mQuestionTextView;
 	
	private TrueFalse[] mAnswerKey = new TrueFalse[] {
	 		new TrueFalse(R.string.question_oceans, true),
	    	new TrueFalse(R.string.question_mideast, false),
	    	new TrueFalse(R.string.question_africa, false),
	    	new TrueFalse(R.string.question_americas, true),
	    	new TrueFalse(R.string.question_asia, true)
		};
	    
	private int mCurrentIndex = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.d("oncreate", "In portrait mode.");
		} else {
			Log.d("oncreate", "In landscape mode.");
		}
		
		nextListener = new NextListener();
		
		mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
		mQuestionTextView.setOnClickListener(nextListener);
		
		mTrueButton = (Button)findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(true);	
			}
		});


		mFalseButton = (Button)findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkAnswer(false);	
			}
		});

 		mNextButton = (ImageButton) findViewById(R.id.next_button);
 		mNextButton.setOnClickListener(nextListener);
 		
 		mPrevButton = (ImageButton) findViewById(R.id.prev_button);
 		mPrevButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCurrentIndex = (((mCurrentIndex - 1) % mAnswerKey.length) + mAnswerKey.length) % mAnswerKey.length;
				updateQuestion();
			}
		});

		cheatButton = (Button) findViewById(R.id.do_cheat_button);
		cheatButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.d(TAG, "Clicked do_cheat_button");
				Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
				intent.putExtra(QuizActivity.INTENT_Q_ANSWER, mAnswerKey[mCurrentIndex].isTrueQuestion());
				startActivityForResult(intent, INTENT_VIEW_CHEAT);
			}
		});
 		
 		updateQuestion();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		if (requestCode == QuizActivity.INTENT_VIEW_CHEAT) {
			if (resultCode == RESULT_OK) {
				if (result == null) {
					return;
				}
				this.viewedCheat = result.getBooleanExtra(CheatActivity.VIEWED_CHEAT, false);
				Log.v(TAG, "viewd cheat: " + this.viewedCheat);
			}
		}
	}
	
	protected void onSaveInstanceState(Bundle instance) {
		instance.putInt(QuizActivity.STATE_INDEX, mCurrentIndex);
		instance.putBoolean(QuizActivity.STATE_CHEATED, this.viewedCheat);
		super.onSaveInstanceState(instance);
	}
	
	protected void onRestoreInstanceState(Bundle instance) {
		super.onRestoreInstanceState(instance);
		mCurrentIndex = instance.getInt(QuizActivity.STATE_INDEX);
		updateQuestion();
		viewedCheat = instance.getBoolean(QuizActivity.STATE_CHEATED);
	}
	
	private class NextListener implements View.OnClickListener {
		public void onClick(View v) {
			mCurrentIndex = (mCurrentIndex + 1) % mAnswerKey.length; 
		    updateQuestion();
		}
	}
	
	 private void updateQuestion() {
	        int question = mAnswerKey[mCurrentIndex].getQuestion();
	        mQuestionTextView.setText(question);
	        viewedCheat = false;
	 }
	 
	 private void checkAnswer(boolean userPressedTrue) {
			boolean answerIsTrue = mAnswerKey[mCurrentIndex].isTrueQuestion();        
			int messageResId = 0;
		        
			if (userPressedTrue == answerIsTrue) {
				if (viewedCheat) {
					messageResId = R.string.correct_toast_cheat;
				} else {
					messageResId = R.string.correct_toast;
				}
		 	} else {
				messageResId = R.string.incorrect_toast;
			}
		 	Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

}
