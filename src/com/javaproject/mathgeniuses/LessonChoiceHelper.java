package com.javaproject.mathgeniuses;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class LessonChoiceHelper extends BaseAdapter
{
	private ArrayList<String> mLessonNames;
	private LayoutInflater mInflater;

	public LessonChoiceHelper(Context context, ArrayList<String> lessonNames)
	{
		mLessonNames = lessonNames;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mLessonNames.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mLessonNames.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int poition, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.lesson_choice_views, null);
			holder.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar);
			holder.tvProgress = (TextView) convertView
					.findViewById(R.id.tvProgess);
			holder.tvLessonName = (TextView) convertView
					.findViewById(R.id.tvLessonName);
			holder.progrssBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		// The number of attempted exercises per lesson should be gotten from
		// the database

		// int attemptedExrcises=dbObj.getAttemptedExercises(int lessonId);
		
		int attemptedExercises = 9; //This value shows the progress
		int progress = attemptedExercises * 10;
		// int totalScore=dbObj.getTotalScore(int lessonId) // from the database
		
		// score out of 10. This value determines the rate points
		float totalScore = 9; 
		float ratePoints = totalScore / 2;
		if (progress < 100)
		{
			holder.tvProgress.setText(String.valueOf(progress) + "%");

		} else
		{
			holder.tvProgress.setVisibility(View.GONE);
			holder.ratingBar.setVisibility(View.VISIBLE);
			holder.ratingBar.setRating(ratePoints);
		}
		holder.progrssBar.setProgress(progress);
		holder.tvLessonName.setText(mLessonNames.get(poition));

		return convertView;
	}

	private static class ViewHolder
	{
		private RatingBar ratingBar;
		private TextView tvProgress;
		private TextView tvLessonName;
		private ProgressBar progrssBar;
	}

}
