package com.javaproject.mathgeniuses;

import java.util.List;

import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.entities.LessonObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class LessonChoiceAdapter extends BaseAdapter {
	private List<LessonObject> mLessons;
	private LayoutInflater mInflater;
	private Context mContext;

	public LessonChoiceAdapter(Context context, List<LessonObject> lessons)
	{
		mLessons = lessons;
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public int getCount()
	{
		return mLessons.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mLessons.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return mLessons.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.lesson_choice_views, null);
			holder.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar);
			holder.tvProgress = (TextView) convertView
					.findViewById(R.id.tvProgess);
			holder.tvLessonName = (TextView) convertView
					.findViewById(R.id.tvLessonName);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LessonObject lesson = mLessons.get(position);
		
		holder.tvLessonName.setText(lesson.getName());
		
		int attemptedExercises = lesson
				.getNumberOfAttemptedExercises(new MathGeniusesDbAdapter(mContext));
		int progress = attemptedExercises * PlayExercisesActivity.TOTAL_NUMBER_OF_EXERCISES;
		holder.progressBar.setProgress(progress);
		
		if (progress < 100) {
			// If lesson is not completed, show % of progress
			holder.tvProgress.setText(String.valueOf(progress) + "%");
			
		} else {
			// If lesson is completed, show from 0 to 5 stars
			float totalScore = lesson.getScoreObtained(); 
			// score out of 10. This value determines the stars
			float ratePoints = totalScore / 2;
			holder.tvProgress.setVisibility(View.GONE);
			holder.ratingBar.setVisibility(View.VISIBLE);
			holder.ratingBar.setRating(ratePoints);
		}
		
		return convertView;
	}

	private static class ViewHolder
	{
		private RatingBar ratingBar;
		private TextView tvProgress;
		private TextView tvLessonName;
		private ProgressBar progressBar;
	}

}
