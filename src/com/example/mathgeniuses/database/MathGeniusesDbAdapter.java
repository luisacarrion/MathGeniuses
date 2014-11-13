package com.example.mathgeniuses.database;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mathgeniuses.R;
import com.example.mathgeniuses.database.MathGeniusesContract.Exercise;
import com.example.mathgeniuses.database.MathGeniusesContract.Lesson;
import com.example.mathgeniuses.database.MathGeniusesContract.LessonCategory;
import com.example.mathgeniuses.database.MathGeniusesContract.Operation;
import com.example.mathgeniuses.model.LessonObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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
    
    public List<LessonObject> fetchLessons(long operationId) {
    	
    	List<LessonObject> lessonsList = new ArrayList<LessonObject>();
    	
    	String[] projection = {
    	         Lesson._ID,
    	         Lesson.COLUMN_NAME_NAME,
    	         Lesson.COLUMN_NAME_SCORE_OBTAINED,
    	         };

    	String selection = Lesson.COLUMN_NAME_OPERATION_ID + " = ?";
    	String[] selectionArgs = { String.valueOf(operationId) };

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
        			results.getInt(results.getColumnIndex(Lesson.COLUMN_NAME_SCORE_OBTAINED))
        			));
            results.moveToNext();
        }
        results.close();
        
        return lessonsList;
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
			// Print contents of the database
		
			Cursor results = mDb.rawQuery("Select * from lessonCategory", null);
	
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(LessonCategory._ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(LessonCategory.COLUMN_NAME_NAME)));
	            results.moveToNext();
	        }
	        results.close();
	        
	        results = mDb.rawQuery("Select * from operation", null);
	
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(Operation._ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Operation.COLUMN_NAME_NAME)));
	            results.moveToNext();
	        }
	        results.close();
	        
	        results = mDb.rawQuery("Select * from lesson", null);
	
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(Lesson._ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_OPERATION_ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_LESSON_CATEGORY_ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_NAME))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Lesson.COLUMN_NAME_LESSON_ORDER)));
	            results.moveToNext();
	        }
	        results.close();
	        
	        results = mDb.rawQuery("Select * from exercise", null);
	        results.moveToFirst();
	        Log.d("SQL DB", "About to run over the cursor");
	        while (!results.isAfterLast()) {
	            Log.d("SQL DB", results.getString(results.getColumnIndex(Exercise._ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_LESSON_ID))
	            		+ " -"
	            		+ results.getString(results.getColumnIndex(Exercise.COLUMN_NAME_EXERCISE))
	            		+ " -"
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
				Exercise.COLUMN_NAME_EXERCISE_ORDER  + 
				") VALUES (?,?,?);";
        
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
        		"1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 0", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"0 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 0", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 0 + 2", ++exerciseOrder);
        
        
        // Addition - Lesson 2
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to5)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);
        
        // Addition - Lesson 2 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 1 + 1 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 3", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 4", ++exerciseOrder);
  
        
        // Addition - Lesson 3
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to7)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);
        
        // Addition - Lesson 3 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 2 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 3", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 4", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"6 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"4 + 3", ++exerciseOrder);
        
        // Addition - Lesson 4
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to7)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);

        // Addition - Lesson 4 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 2 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 4 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"6 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 4", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"4 + 3", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"1 + 2", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 4", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 3", ++exerciseOrder);
        
        // Addition - Lesson 5
        lastLessonId = insertWithArgs(statementLesson, operationId, categoriesIds.get(mContext.getString(R.string.lesson_category_0to10)), 
        		mContext.getString(R.string.lesson_title) + " " + ++lessonOrder, lessonOrder);

        // Addition - Lesson 5 - Exercises
        exerciseOrder = 0;
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 3", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 5", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"7 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"3 + 4", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"4 + 4", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"8 + 0", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"2 + 3", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"5 + 3", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"8 + 1", ++exerciseOrder);
        insertWithArgs(statementExercise, lastLessonId,
        		"8 + 2", ++exerciseOrder);
        
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
		
		public static final String DATABASE_NAME = "StudentQuizzes.db";
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
