package com.example.mathgeniuses.model;

public class ExerciseObject {
	private long mId;
	private String mExercise;
	private int mScoreObtained;
	
	public ExerciseObject(long id, String exercise, int scoreObtained) {
		mId = id;
		mExercise = exercise;
		mScoreObtained = scoreObtained;
	}
	
	
	public long getId() {
		return mId;
	}
	
	public void setId(long id) {
		this.mId = id;
	}
	
	public String getExercise() {
		return mExercise;
	}
	
	public void setExercise(String exercise) {
		this.mExercise = exercise;
	}
	
	public int getScoreObtained() {
		return mScoreObtained;
	}
	
	public void setScoreObtained(int scoreObtained) {
		this.mScoreObtained = scoreObtained;
	}
}
