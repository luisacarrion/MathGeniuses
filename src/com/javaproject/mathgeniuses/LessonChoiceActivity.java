package com.javaproject.mathgeniuses;

import java.util.ArrayList;
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

public class LessonChoiceActivity extends Activity
{
	private ListView mLessonsList;
	private List<LessonObject> lessonObject;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson_choice);
		mLessonsList = (ListView) findViewById(R.id.listLessons);
		ArrayList<String>lessons=getLessonName();
		mLessonsList.setAdapter(new LessonChoiceAdapter(this,lessons));
		mLessonsList.setOnItemClickListener((new LessonsListListener()));

	}

	// Getting the list of lessons available in the database
	private ArrayList<String> getLessonName()
	{
		
		MathGeniusesDbAdapter mathAdapter=new MathGeniusesDbAdapter(this);
		mathAdapter.open();
		lessonObject=mathAdapter.fetchLessons();
		Log.i("MGEN", "The number of lessons: "+lessonObject.size());
		mathAdapter.close();
		ArrayList<String> lessons = new ArrayList<String>();
		for (int i = 0; i < lessonObject.size(); i++)
		{			
			Log.i("MGEN", "Name: "+i+"" +lessonObject.get(i).getName()+" Id:"+lessonObject.get(i).getId());
			lessons.add(lessonObject.get(i).getName());
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
			switch(position){
			case 0:
				// Drag and drop exercise
				Intent intent=new Intent(LessonChoiceActivity.this,DragAndDropActivity.class);
				intent.putExtra("lessonId", lessonObject.get(0).getId());
				startActivity(intent);
			}
			
		}
		
		
	}

}
