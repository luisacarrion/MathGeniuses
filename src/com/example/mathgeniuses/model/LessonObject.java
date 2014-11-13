package com.example.mathgeniuses.model;

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
	
	
}
