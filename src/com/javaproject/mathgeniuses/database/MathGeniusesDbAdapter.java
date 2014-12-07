package com.javaproject.mathgeniuses.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.javaproject.mathgeniuses.R;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.Exercise;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.ExerciseScore;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.Lesson;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.LessonCategory;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.LessonScore;
import com.javaproject.mathgeniuses.database.MathGeniusesContract.Operation;
import com.javaproject.mathgeniuses.entities.ExerciseObject;
import com.javaproject.mathgeniuses.entities.LessonObject;
import com.javaproject.mathgeniuses.entities.OperationObject;

public class MathGeniusesDbAdapter {
	
	private static final boolean DEBUG = true;
	
    protected Context mContext;
    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;
    
	
    public MathGeniusesDbAdapter(Context context) {
    	mContext = context;
    }
    
    public MathGeniusesDbAdapter open() {
        mDbHelper = new MathGeniusesDbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        
        return this;
    }

    public void close() {
        mDb.close();
    }
    
    
    public long getOperationId(String operationName) {
    	long operationId = -1;
    	
    	String[] projection = { Operation._ID };
    	String selection = Operation.COLUMN_NAME_NAME + " = ?";
    	String[] selectionArgs = { operationName };
    	
    	Cursor results = mDb.query(
    			Operation.TABLE_NAME,  // The table to query
    			projection,         // The columns to return
    			selection,          		// The columns for the WHERE clause
    			selectionArgs,      			// The values for the WHERE clause
    			null,               // don't group the rows
    			null,               // don't filter by row groups
    			null           		// The sort order
    		);

    	results.moveToFirst();
    	operationId = results.getLong(results.getColumnIndex(Operation._ID));
        results.close();
        
        return operationId;
    }
    
    
    public List<OperationObject> fetchOperations() {
    	List<OperationObject> operationsList = new ArrayList<OperationObject>();
    	
    	String[] projection = {
    	         Operation._ID,
    	         Operation.COLUMN_NAME_NAME,
    	         };
    	
    	Cursor results = mDb.query(
    			Operation.TABLE_NAME,  // The table to query
    			projection,         // The columns to return
    			null,          		// The columns for the WHERE clause
    			null,      			// The values for the WHERE clause
    			null,               // don't group the rows
    			null,               // don't filter by row groups
    			null           		// The sort order
    		);

    	results.moveToFirst();
        while (!results.isAfterLast()) {
        	operationsList.add( new OperationObject(
        			results.getLong(results.getColumnIndex(Operation._ID)), 
        			results.getString(results.getColumnIndex(Operation.COLUMN_NAME_NAME))
        			));
            results.moveToNext();
        }
        results.close();
        
        return operationsList;
    }
    
    
    
    
    public List<LessonObject> fetchLessons(long... operationIds) {
    	
    	List<LessonObject> lessonsList = new ArrayList<LessonObject>();
    	
    	String[] projection = {
    	         Lesson._ID,
    	         Lesson.COLUMN_NAME_NAME
    	         };
    	
    	String selection = null;
    	String[] selectionArgs = null;

    	if (operationIds.length > 0) {
    		selectionArgs = new String[operationIds.length];
    		
    		StringBuilder s = new StringBuilder();
    		
    		s.append(Lesson.COLUMN_NAME_OPERATION_ID + " IN (");
    		
    		for (int i = 0; i < operationIds.length; i++) {
    			s.append("?");
    			if (i < operationIds.length-1) {s.append(",");};
    			selectionArgs[i] = String.valueOf(operationIds[i]); 
    		}
    		s.append(")");
    		selection = s.toString();
    	}


    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			Lesson.COLUMN_NAME_LESSON_ORDER + " ASC";

    	Cursor results = mDb.query(
    			Lesson.TABLE_NAME,  // The table to query
    			projection,         // The columns to return
    			selection,          // The columns for the WHERE clause
    			selectionArgs,      // The values for the WHERE clause
    			null,               // don't group the rows
    			null,               // don't filter by row groups
    			sortOrder           // The sort order
    		);

    	results.moveToFirst();
        while (!results.isAfterLast()) {
        	lessonsList.add(new LessonObject(
        			results.getLong(results.getColumnIndex(Lesson._ID)), 
        			results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_NAME)), 
        			0)
        	);
            results.moveToNext();
        }
        results.close();
        
        return lessonsList;
    }
    
public List<LessonObject> fetchLessonsWithScore(long... operationIds) {
    	
    	List<LessonObject> lessonsList = new ArrayList<LessonObject>();
    	
    	String selection = null;
    	String[] selectionArgs = null;

    	if (operationIds.length > 0) {
    		// Build a WHERE clause of type "columnName IN (value1, value2, ...)" with all the operationIds provided
    		selectionArgs = new String[operationIds.length];
    		StringBuilder s = new StringBuilder();
    		
    		s.append(Lesson.COLUMN_NAME_OPERATION_ID + " IN (");
    		
    		for (int i = 0; i < operationIds.length; i++) {
    			s.append("?");
    			if (i < operationIds.length-1) {s.append(",");};
    			
    			selectionArgs[i] = String.valueOf(operationIds[i]); 
    		}
    		s.append(")");
    		selection = s.toString();
    	}
    	
    	// Select all Exercises for a Lesson with their score
    	// TODO: add a selection argument for the current user
    	String sql = "SELECT " + Lesson.TABLE_NAME + "." + Lesson._ID + ", " + Lesson.COLUMN_NAME_NAME + ", " + LessonScore.COLUMN_NAME_SCORE +
    			" FROM " + Lesson.TABLE_NAME +
    			" LEFT JOIN " + LessonScore.TABLE_NAME +
    			" ON " + Lesson.TABLE_NAME + "." + Lesson._ID +
    			" = " + LessonScore.TABLE_NAME + "." + LessonScore.COLUMN_NAME_LESSON_ID +
    			(selection != null ? " WHERE " + selection : "") +
    			" ORDER BY " + Lesson.COLUMN_NAME_LESSON_ORDER + " ASC";
    	// TODO: change the hardcoded number of attempted exercises. I think the query should be like 
    	// WHERE ExerciseScore != null HAVING Exercise.lessonid == Lesson._id
    	
    	Cursor results = mDb.rawQuery(sql, selectionArgs);
    	
    	results.moveToFirst();
        while (!results.isAfterLast()) {
        	lessonsList.add(new LessonObject(
        			results.getLong(results.getColumnIndex(Lesson._ID)), 
        			results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_NAME)), 
        			results.getInt(results.getColumnIndex(LessonScore.COLUMN_NAME_SCORE))) 
        	);
            results.moveToNext();
        }
        results.close();
        
        return lessonsList;
    }
    
    public List<ExerciseObject> fetchExercises(long lessonId) {
    	List<ExerciseObject> exercisesList = new ArrayList<ExerciseObject>();
    	
    	String[] projection = {
    	         Exercise._ID,
    	         Exercise.COLUMN_NAME_EXERCISE
    	         };
    	
    	String selection = Exercise.COLUMN_NAME_LESSON_ID + " = ?";
    	String[] selectionArgs = { String.valueOf(lessonId) };


    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			Exercise.COLUMN_NAME_EXERCISE_ORDER + " ASC";

    	Cursor results = mDb.query(
    			Exercise.TABLE_NAME,  // The table to query
    			projection,         // The columns to return
    			selection,          // The columns for the WHERE clause
    			selectionArgs,      // The values for the WHERE clause
    			null,               // don't group the rows
    			null,               // don't filter by row groups
    			sortOrder           // The sort order
    		);

    	results.moveToFirst();
        while (!results.isAfterLast()) {
        	exercisesList.add(new ExerciseObject(
        			results.getLong(results.getColumnIndex(Lesson._ID)), 
        			results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_EXERCISE)), 
        			0, 0
        			));
            results.moveToNext();
        }
        results.close();
        
        return exercisesList;
    }
    
    public List<ExerciseObject> fetchExercisesWithScore(long lessonId) {
    	List<ExerciseObject> exercisesList = new ArrayList<ExerciseObject>();
    	
    	// Select all Exercises for a Lesson with their score
    	// TODO: add a selection argument for the current user
    	String sql = "SELECT " + Exercise.TABLE_NAME + "." + Exercise._ID + ", " + Exercise.COLUMN_NAME_EXERCISE + ", " + 
    			Exercise.COLUMN_NAME_ANSWER + ", " + ExerciseScore.COLUMN_NAME_SCORE +
    			" FROM " + Exercise.TABLE_NAME +
    			" LEFT JOIN " + ExerciseScore.TABLE_NAME +
    			" ON " + Exercise.TABLE_NAME + "." + Exercise._ID +
    			" = " + ExerciseScore.TABLE_NAME + "." + ExerciseScore.COLUMN_NAME_EXERCISE_ID +
    			" WHERE " + Exercise.COLUMN_NAME_LESSON_ID + " = ?" +
    			" ORDER BY " + Exercise.COLUMN_NAME_EXERCISE_ORDER + " ASC";
    			
    	String[] selectionArgs = { String.valueOf(lessonId) };
    	
    	Cursor results = mDb.rawQuery(sql, selectionArgs);
    	
    	results.moveToFirst();
        while (!results.isAfterLast()) {
        	exercisesList.add(new ExerciseObject(
        			results.getLong(results.getColumnIndex(Lesson._ID)), 
        			results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_EXERCISE)),
        			results.getInt(results.getColumnIndex(Exercise.COLUMN_NAME_ANSWER)),
        			results.getInt(results.getColumnIndex(ExerciseScore.COLUMN_NAME_SCORE))
        			));
            results.moveToNext();
        }
        results.close();
        
        return exercisesList;
    }
    
    public int getNumberOfAttemptedExercisesForLesson(long lessonId) {
    	final String columnCountName = "quantity";
    	int attemptedExercises = 0;
    	
    	// TODO: add a selection argument for the current user
    	// For each row that matches, return a 1, and count the amount of rows
    	String sql = "SELECT COUNT(1) AS " + columnCountName +
    			" FROM " + Exercise.TABLE_NAME +
    			" INNER JOIN " + ExerciseScore.TABLE_NAME +
    			" ON " + Exercise.TABLE_NAME + "." + Exercise._ID +
    			" = " + ExerciseScore.TABLE_NAME + "." + ExerciseScore.COLUMN_NAME_EXERCISE_ID +
    			" WHERE " + Exercise.COLUMN_NAME_LESSON_ID + " = ?" +
    			" AND " + ExerciseScore.COLUMN_NAME_SCORE + " NOT NULL";
    	
    	String[] selectionArgs = { String.valueOf(lessonId) };
    	
    	Cursor results = mDb.rawQuery(sql, selectionArgs);
    	
    	results.moveToFirst();
    	attemptedExercises = results.getInt(results.getColumnIndex(columnCountName));
    	results.close();
    	
    	return attemptedExercises;
    }
    
    public int getScoreAwardedForLesson(long lessonId) {
    	int scoreAwarded = 0;
    	
    	String sql = "SELECT " + LessonCategory.TABLE_NAME + "." + LessonCategory.COLUMN_NAME_SCORE_AWARDED +
    			" FROM " + LessonCategory.TABLE_NAME +
    			" INNER JOIN " + Lesson.TABLE_NAME +
    			" ON " + LessonCategory.TABLE_NAME + "." + LessonCategory._ID +
    			" = " + Lesson.TABLE_NAME + "." + Lesson.COLUMN_NAME_LESSON_CATEGORY_ID +
    			" WHERE " + Lesson.TABLE_NAME + "." + Lesson._ID + " = ?";
    			
    	String[] selectionArgs = { String.valueOf(lessonId) };
    	
    	Cursor results = mDb.rawQuery(sql, selectionArgs);
    	
    	results.moveToFirst();
    	scoreAwarded = results.getInt(results.getColumnIndex(LessonCategory.COLUMN_NAME_SCORE_AWARDED));
        results.close();
    	
    	return scoreAwarded;
    }
    
    public void saveExerciseScore(long id, int score) {
    	ContentValues values = new ContentValues();
    	values.put(ExerciseScore.COLUMN_NAME_EXERCISE_ID, id);
    	values.put(ExerciseScore.COLUMN_NAME_SCORE, score);

    	// TODO: add argument for the user
    	String selection = ExerciseScore.COLUMN_NAME_EXERCISE_ID + " = ?";
    	String[] selectionArgs = { String.valueOf(id) };

    	int affectedRows = mDb.update(
    			ExerciseScore.TABLE_NAME,
	    	    values,
	    	    selection,
	    	    selectionArgs);
    	
    	if (affectedRows == 0) {
    		mDb.insert(
    				ExerciseScore.TABLE_NAME,
    				null,
		    	    values);
    	}
    }
    
    public void saveLessonScore(long id, int score) {
    	ContentValues values = new ContentValues();
    	values.put(LessonScore.COLUMN_NAME_LESSON_ID, id);
    	values.put(LessonScore.COLUMN_NAME_SCORE, score);

    	// TODO: add argument for the user
    	String selection = LessonScore.COLUMN_NAME_LESSON_ID + " = ?";
    	String[] selectionArgs = { String.valueOf(id) };

    	int affectedRows = mDb.update(
    			LessonScore.TABLE_NAME,
	    	    values,
	    	    selection,
	    	    selectionArgs);
    	
    	if (affectedRows == 0) {
    		mDb.insert(
    				LessonScore.TABLE_NAME,
    				null,
		    	    values);
    	}
    }
    
    public void bulkInsertCatalogs() {
		HashMap<String, Long> operationsIds;
		HashMap<String, Long> categoriesIds;
		
		mDb.beginTransaction();
		
		operationsIds = bulkInsertOperations();
		categoriesIds = bulkInsertLessonCategories();
		bulkInsertLessonsAndExercises(operationsIds, categoriesIds);
		
		mDb.setTransactionSuccessful();
	    mDb.endTransaction();
		
		if (DEBUG) {
			// Log contents of the database
		
			Cursor results = mDb.rawQuery("Select * from lessonCategory", null);
	
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(LessonCategory._ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(LessonCategory.COLUMN_NAME_NAME)));
	            results.moveToNext();
	        }
	        results.close();
	        
	        results = mDb.rawQuery("Select * from operation", null);
	
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(Operation._ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Operation.COLUMN_NAME_NAME)));
	            results.moveToNext();
	        }
	        results.close();
	        
	        results = mDb.rawQuery("Select * from lesson", null);
	
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(Lesson._ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_OPERATION_ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_LESSON_CATEGORY_ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_NAME))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_LESSON_ORDER)));
	            results.moveToNext();
	        }
	        results.close();
	        
	        results = mDb.rawQuery("Select * from exercise", null);
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(Exercise._ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_LESSON_ID))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_EXERCISE))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_ANSWER))
	            		+ " - "
	            		+ results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_EXERCISE_ORDER)));
	            results.moveToNext();
	        }
	        results.close();
		}
        
		
	}
	
	private HashMap<String, Long> bulkInsertOperations() {
		HashMap<String, Long> operationsIds = new HashMap<String, Long>();
		
		String sql = "INSERT INTO "+ Operation.TABLE_NAME + 
				" (" + Operation.COLUMN_NAME_NAME +
				") VALUES (?);";
		SQLiteStatement statement = mDb.compileStatement(sql);
        
		operationsIds.put(mContext.getString(R.string.operation_addition), insertWithArgs(statement, mContext.getString(R.string.operation_addition))) ;
		operationsIds.put(mContext.getString(R.string.operation_subtraction), insertWithArgs(statement, mContext.getString(R.string.operation_subtraction)));
		operationsIds.put(mContext.getString(R.string.operation_multiplication), insertWithArgs(statement, mContext.getString(R.string.operation_multiplication)));
		operationsIds.put(mContext.getString(R.string.operation_division), insertWithArgs(statement, mContext.getString(R.string.operation_division)));
		operationsIds.put(mContext.getString(R.string.operation_4_basic), insertWithArgs(statement, mContext.getString(R.string.operation_4_basic)));
		
		return operationsIds;
	}
	
	private HashMap<String, Long> bulkInsertLessonCategories() {
		HashMap<String, Long> categoriesIds = new HashMap<String, Long>();
		
		String sql = "INSERT INTO "+ LessonCategory.TABLE_NAME +
				" (" + LessonCategory.COLUMN_NAME_NAME  + ", " + LessonCategory.COLUMN_NAME_SCORE_AWARDED +
				") VALUES (?,?);";
        SQLiteStatement statement = mDb.compileStatement(sql);
        
        categoriesIds.put(mContext.getString(R.string.lesson_category_0to3), insertWithArgs(statement, mContext.getString(R.string.lesson_category_0to3), 3));
        categoriesIds.put(mContext.getString(R.string.lesson_category_0to5), insertWithArgs(statement, mContext.getString(R.string.lesson_category_0to5), 5));
        categoriesIds.put(mContext.getString(R.string.lesson_category_0to7), insertWithArgs(statement, mContext.getString(R.string.lesson_category_0to7), 7));
        categoriesIds.put(mContext.getString(R.string.lesson_category_0to10), insertWithArgs(statement, mContext.getString(R.string.lesson_category_0to10), 10));
        
        return categoriesIds;
	}
	
	private void bulkInsertLessonsAndExercises(HashMap<String, Long> operationsIds, HashMap<String, Long> categoriesIds) {
		
		String sqlLesson = "INSERT INTO "+ Lesson.TABLE_NAME +
				" (" + Lesson.COLUMN_NAME_OPERATION_ID  + ", " + Lesson.COLUMN_NAME_LESSON_CATEGORY_ID  + ", " + 
				Lesson.COLUMN_NAME_NAME  + ", " + Lesson.COLUMN_NAME_LESSON_ORDER  + 
				") VALUES (?,?,?,?);";
        String sqlExercise = "INSERT INTO "+ Exercise.TABLE_NAME +
				" (" + Exercise.COLUMN_NAME_LESSON_ID  + ", " + Exercise.COLUMN_NAME_EXERCISE  + ", " + 
				Exercise.COLUMN_NAME_ANSWER  + ", " + Exercise.COLUMN_NAME_EXERCISE_ORDER  + 
				") VALUES (?,?,?,?);";
        
        SQLiteStatement statementLesson = mDb.compileStatement(sqlLesson);
        SQLiteStatement statementExercise = mDb.compileStatement(sqlExercise);
        
        long lastLessonId = 0;
        int lessonOrder = 0;
        int exerciseOrder = 0;
        
        // Addition Lessons
        long operationId = operationsIds.get(mContext.getString(R.string.operation_addition));
        lessonOrder = 0;
        
        // Addition - Lesson 1
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to3)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);
        
        // Addition - Lesson 1 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1", 2, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 0", 1, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1", 2, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1 + 1", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"0 + 2", 2, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 1", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1", 2, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 0", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 0 + 2", 3, ++exerciseOrder);
        
        
        // Addition - Lesson 2
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to5)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);
        
        // Addition - Lesson 2 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1 + 1", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2 + 1", 4, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 1", 4, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 1 + 1", 4, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2", 4, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2 + 1", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 1 + 1 + 1", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 3", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 4", 5, ++exerciseOrder);
  
        
        // Addition - Lesson 3
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to7)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);
        
        // Addition - Lesson 3 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2 + 1", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 2", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 2 + 1", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 3", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 4", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 1", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"6 + 1", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 1", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 2", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"4 + 3", 7, ++exerciseOrder);
        
        // Addition - Lesson 4
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to7)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);

        // Addition - Lesson 4 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2 + 2", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 2", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 4 + 2", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 2", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"6 + 1", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 4", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"4 + 3", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2", 3, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 4", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 3", 6, ++exerciseOrder);
        
        // Addition - Lesson 5
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to10)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);

        // Addition - Lesson 5 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 3", 6, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 5", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"7 + 1", 8, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 4", 7, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"4 + 4", 8, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"8 + 0", 8, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 3", 5, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 3", 8, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"8 + 1", 9, ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"8 + 2", 10, ++exerciseOrder);
        
	}
	
	public Long insertWithArgs(SQLiteStatement statement, Object... args) {
		statement.clearBindings();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("null")) {
				statement.bindNull(i+1);
			} else if (args[i] instanceof String) {
				statement.bindString(i+1, (String)args[i]);
			} else if (args[i] instanceof Integer) {
				statement.bindLong(i+1, (Integer)args[i]);
			} else if (args[i] instanceof Long) {
				statement.bindLong(i+1, (Long)args[i]);
			} else if (args[i] instanceof Double) {
				statement.bindDouble(i+1, (Double)args[i]);
			}
		}
        return statement.executeInsert();
	}

	public class MathGeniusesDbHelper extends SQLiteOpenHelper {
		
		public static final String DATABASE_NAME = "MathGeniuses.db";
		public static final int DATABASE_VERSION = 1;
		
		public MathGeniusesDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Operation.CREATE_TABLE);
			db.execSQL(LessonCategory.CREATE_TABLE);
			db.execSQL(Lesson.CREATE_TABLE);
			db.execSQL(Exercise.CREATE_TABLE);
			db.execSQL(ExerciseScore.CREATE_TABLE);
			db.execSQL(LessonScore.CREATE_TABLE);
		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("DROP TABLE IF EXISTS " + Operation.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + LessonCategory.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Lesson.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Exercise.TABLE_NAME);
			
			onCreate(db);
			
			switch(oldVersion) {
				
			    case 1:
			    	// No updates yet
			    	// we want both updates, so no break statement here...
			    case 2:
			    	// No updates yet 
			}
			
			
		}
	}
	
	
}
