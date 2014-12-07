package com.javaproject.mathgeniuses.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.javaproject.mathgeniuses.database.MathGeniusesContract.Lesson;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.LessonScore;
import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;

public class LessonObject {

	private long mId;
	private String mName;
	private int mNumberOfAttemptedExercises;
	private int mScoreAwarded;
	private int mScoreObtained;
	
	public LessonObject(long id, String name, int scoreObtained) {
		mId = id;
		mName = name;
		mNumberOfAttemptedExercises = -1;
		mScoreAwarded = 0;
		mScoreObtained = scoreObtained;
	}
	
	
	public long getId() {
		return mId;
	}
	
	public void setId(long id) {
		this.mId = id;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public int getNumberOfAttemptedExercises(MathGeniusesDbAdapter db) {
		if (mNumberOfAttemptedExercises == -1) {
			db.open();
			mNumberOfAttemptedExercises = db.getNumberOfAttemptedExercisesForLesson(mId);
			db.close();
		} 
		
		return mNumberOfAttemptedExercises;
	}

	public void setNumberOfAttemptedExercises(int mNumberOfAttemptedExercises) {
		this.mNumberOfAttemptedExercises = mNumberOfAttemptedExercises;
	}

	public int getScoreAwarded(MathGeniusesDbAdapter db) {
		if (mScoreAwarded == 0) {
			db.open();
			mScoreAwarded = db.getScoreAwardedForLesson(mId);
			db.close();
		} 
		
		return mScoreAwarded;
	}
	
	public static int getScoreAwarded(MathGeniusesDbAdapter db, long lessonId) {
		int scoreAwarded = 0;
		db.open();
		scoreAwarded = db.getScoreAwardedForLesson(lessonId);
		db.close();
		return scoreAwarded;
	}
	
	public int getScoreObtained() {
		return mScoreObtained;
	}
	
	public void setScoreObtained(int scoreObtained) {
		this.mScoreObtained = scoreObtained;
	}
	
	public Map<String, String> asMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Lesson.COLUMN_NAME_NAME, mName);
		map.put(LessonScore.COLUMN_NAME_SCORE, String.valueOf(mScoreObtained));
		return map;
	}
	
	
}
