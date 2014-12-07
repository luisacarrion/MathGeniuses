package com.javaproject.mathgeniuses.entities;

import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;

public class ExerciseObject {
	private long mId;
	private String mExercise;
	private int mAnswer;
	private int mScoreObtained;
	
	public ExerciseObject(long id, String exercise, int answer, int scoreObtained) {
		mId = id;
		mExercise = exercise;
		mAnswer = answer;
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
	
	public int getAnswer() {
		return mAnswer;
	}


	public void setAnswer(int Answer) {
		this.mAnswer = Answer;
	}


	public int getScoreObtained() {
		return mScoreObtained;
	}
	
	public void setScoreObtained(int scoreObtained) {
		this.mScoreObtained = scoreObtained;
	}
	
}
