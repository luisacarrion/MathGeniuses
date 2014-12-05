package com.javaproject.mathgeniuses.entities;

import java.util.HashMap;
import java.util.Map;

import com.javaproject.mathgeniuses.database.MathGeniusesContract.Lesson;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.LessonScore;

public class LessonObject {

	private long mId;
	private String mName;
	private int mScoreObtained;
	
	public LessonObject(long id, String name, int scoreObtained) {
		mId = id;
		mName = name;
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
