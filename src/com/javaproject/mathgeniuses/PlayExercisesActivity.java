package com.javaproject.mathgeniuses;

import java.util.List;

import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.entities.ExerciseObject;
import com.javaproject.mathgeniuses.entities.LessonObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

/**
 * This class loads all the exercises for the lesson and then, loads a fragment per exercise.
 * @author MariaLuisa
 *
 */
public class PlayExercisesActivity extends Activity {
	
	public static final String KEY_LESSON_ID = "keyLessonId";
	public static final int TOTAL_NUMBER_OF_EXERCISES = 10;

	private long mLessonId;
	private List<ExerciseObject> mExerciseObjectsList;
	private int mCurrentExercise;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_exercises);
		
		// Get intent data
		Intent intent = getIntent();
		mLessonId = intent.getLongExtra(KEY_LESSON_ID, -1);
		
		mCurrentExercise = 0;
		mExerciseObjectsList = getExercises(mLessonId);	
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_exercises, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private List<ExerciseObject> getExercises(long lessonId) {
		MathGeniusesDbAdapter mathAdapter = new MathGeniusesDbAdapter(this);
		mathAdapter.open();
		
		List<ExerciseObject> exerciseObjectsList;
		exerciseObjectsList = mathAdapter.fetchExercises(lessonId);
		mathAdapter.close();
		
		return exerciseObjectsList;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_play_exercises,
					container, false);
			return rootView;
		}
	}
}
