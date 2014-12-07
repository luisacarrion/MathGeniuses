package com.javaproject.mathgeniuses;

import com.javaproject.mathgeniuses.util.DialogsHelper;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

public abstract class AbstractExerciseFragment extends Fragment
{
	public static final int TIME_LIMIT_SECONDS = 6;
	public static final String KEY_EXERCISE = "keyExercise";
	public static final String KEY_ANSWER = "keyAnswer";
	public static final String KEY_SCORE_AWARDED = "keyScoreAwarded";
	public static final String KEY_SCORE_OBTAINED = "keyScoreObtained";

	protected String mExercise;
	protected int mAnswer;
	protected int mScoreAwarded;
	protected int mScoreObtained;
	protected long mLessonId;

	private CountDownTimer mCountDownTimer;
	protected ExerciseEvents mCallback;

	TextView mTvTimer;

	public void setActivityCallback(Activity activity)
	{
		try
		{
			mCallback = (ExerciseEvents) activity;
		} catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement ExerciseEvents interface");
		}

	}

	public void setTimer(TextView tvTimer)
	{
		mTvTimer = tvTimer;

		mCountDownTimer = new CountDownTimer(TIME_LIMIT_SECONDS * 1000, 1000)
		{
			// mCountDownTimer = new CountDownTimer(30 * 1000, 1000) {
			public void onTick(long millisUntilFinished)
			{
				int secondsUntilFinished = (int) millisUntilFinished / 1000;

				int minutesToDisplay;
				int secondsTodisplay;

				secondsTodisplay = (secondsUntilFinished) % 60;
				minutesToDisplay = (secondsUntilFinished - secondsTodisplay) / 60;
				mTvTimer.setText(minutesToDisplay + ":"
						+ String.format("%02d", secondsTodisplay));
			}

			public void onFinish()
			{
				mTvTimer.setText("00:00");
				mCallback.onExerciseEnd(calculateScore());
				MediaPlayer player = MediaPlayer.create(getActivity(),
						R.raw.bubble);
				player.start();
				showExerciseScore();
			}
		};

	}

	// Called when the user clicks the Verify button
	public void onVerify()
	{
		int score = calculateScore();
		mCallback.onExerciseEnd(score);
	}

	public void startTimer()
	{
		mCountDownTimer.start();
	}

	public void stopTimer()
	{
		mCountDownTimer.cancel();
	}

	public void showExerciseScore()
	{
		DialogsHelper toast = new DialogsHelper((Activity) mCallback);
		toast.showToast("+" + mScoreObtained);
	}

	public abstract int calculateScore();

	/**
	 * Defines methods to be implemented by an activity that wants to know when
	 * an exercise ends.
	 * 
	 * @author MariaLuisa
	 * 
	 */
	public interface ExerciseEvents
	{
		public void onExerciseEnd(int scoreObtained); // Activity loads next
														// exercise
	}

}
