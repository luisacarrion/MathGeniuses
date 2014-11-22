package com.javaproject.mathgeniuses;

import java.util.ArrayList;
import java.util.List;

import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.entities.LessonObject;
import com.javaproject.mathgeniuses.util.DialogsHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LessonChoiceActivity extends Activity
{
	private ListView mLessonsList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_choice);
		mLessonsList = (ListView) findViewById(R.id.listLessons);
		// List<LessonObject> lessons=new
		// MathGeniusesDbAdapter(this).fetchLessons(null);
		ArrayList<String>lessons=getLessonName();
		mLessonsList.setAdapter(new LessonChoiceHelper(this,lessons));
		mLessonsList.setOnItemClickListener((new LessonsListListener()));

	}

	// The name of lessons should be got from the data base
	private ArrayList<String> getLessonName()
	{
		ArrayList<String> lessons = new ArrayList<String>();
		for (int i = 0; i < 5; i++)
		{
			lessons.add("Lesson Name " + (i + 1));

		}
		return lessons;

	}
	
	private class LessonsListListener implements OnItemClickListener {
		DialogsHelper dialogs=new DialogsHelper(LessonChoiceActivity.this);
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3)
		{
			// TODO Auto-generated method stub
			dialogs.showToast("Lesson "+(position+1)+" Exrecices to come shortly!");
			
		}
		
		
	}

}
