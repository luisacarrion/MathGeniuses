package com.javaproject.mathgeniuses;

import java.util.List;

import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.entities.LessonObject;
import com.javaproject.mathgeniuses.util.DialogsHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LessonChoiceActivity extends Activity {
	
	public static final String KEY_OPERATION_ID = "keyOperationId";
	
	private long mOperationId;
	List<LessonObject> mLessons;
	
	private LessonChoiceAdapter mLessonChoiceAdapter;
	
	private ListView mLessonsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_choice);
		
		// Get data from intent
		Intent intent = getIntent();
		mOperationId = intent.getLongExtra(KEY_OPERATION_ID, -1);
		
		// Load data in ListView
		mLessonsList = (ListView) findViewById(R.id.listLessons);
		mLessons = getLessons();
		mLessonChoiceAdapter = new LessonChoiceAdapter(this,mLessons);
		mLessonsList.setAdapter(mLessonChoiceAdapter);
		mLessonsList.setOnItemClickListener((new LessonsListListener()));

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Refresh lessons, so if the user practiced new exercises, the progress can be seen
		mLessons = getLessons();
		mLessonChoiceAdapter.setDataSource(mLessons);
		mLessonChoiceAdapter.notifyDataSetChanged();
	}
	
	// Getting the list of lessons available in the database
	private List<LessonObject> getLessons()
	{
		MathGeniusesDbAdapter mathAdapter = new MathGeniusesDbAdapter(this);
		mathAdapter.open();
		
		List<LessonObject> lessonObjectsList;
		lessonObjectsList = mathAdapter.fetchLessons(mOperationId);
		Log.i("MGEN", "The number of lessons: " + lessonObjectsList.size());
		mathAdapter.close();
		
		return lessonObjectsList;
	}
	
	private class LessonsListListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long rowId)
		{
			Intent intent = new Intent(getApplicationContext(), PlayExercisesActivity.class);
			intent.putExtra(PlayExercisesActivity.KEY_LESSON_ID, rowId);
			startActivity(intent);
		}
		
		
	}

}
