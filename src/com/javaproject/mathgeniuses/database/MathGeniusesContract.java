package com.javaproject.mathgeniuses.database;

import android.provider.BaseColumns;

public final class MathGeniusesContract {
	
    // Create table sentences
	private static final String TYPE_PRIMARY_KEY = " PRIMARY KEY";
	private static final String TYPE_AUTOINCREMENT = " AUTOINCREMENT";
	private static final String TYPE_UNIQUE = " UNIQUE";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_FOREIGN_KEY = "FOREIGN KEY(";
    private static final String TYPE_REFERENCES = ") REFERENCES ";
    private static final String COMMA_SEP = ",";

	/**
	 * Holds math operations catalog
	 * @author MariaLuisa
	 *
	 */
	public static final class Operation implements BaseColumns {
		public static final String TABLE_NAME = "operation";
		
		public static final String COLUMN_NAME_NAME = "name";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_NAME + TYPE_TEXT + TYPE_UNIQUE +
                        " )";
	}
	
	/**
	 * Holds the lesson categories catalog
	 * The category defines the complexity of the lesson.
	 * Example categories: numbers 0 to 3, numbers 0 to 5, numbers 0 to 7, etc.
	 * @author MariaLuisa
	 *
	 */
	public static final class LessonCategory implements BaseColumns {
		public static final String TABLE_NAME = "lessonCategory";
		
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_SCORE_AWARDED = "scoreAwarded";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_NAME + TYPE_TEXT + TYPE_UNIQUE + COMMA_SEP +
                        COLUMN_NAME_SCORE_AWARDED + TYPE_INTEGER +
                        " )";
	}
	
	/**
	 * Holds the lessons catalog
	 * Also saves the score of the user for a specific lesson
	 * @author MariaLuisa
	 *
	 */
	public static final class Lesson implements BaseColumns {
		public static final String TABLE_NAME = "lesson";
		
		public static final String COLUMN_NAME_OPERATION_ID = "operationId";
		public static final String COLUMN_NAME_LESSON_CATEGORY_ID = "lessonCategoryId";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_LESSON_ORDER = "lessonOrder";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_OPERATION_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_LESSON_CATEGORY_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_NAME + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_LESSON_ORDER + TYPE_INTEGER + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_OPERATION_ID + TYPE_REFERENCES + Operation.TABLE_NAME + "(" + Operation._ID + ")" + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_LESSON_CATEGORY_ID + TYPE_REFERENCES + LessonCategory.TABLE_NAME + "(" + LessonCategory._ID + ")" +
                        " )";
	}
	
	/**
	 * Holds the exercises catalog
	 * A lesson has many exercises.
	 * Also saves the score of the user for a specific exercise
	 * @author MariaLuisa
	 *
	 */
	public static final class Exercise implements BaseColumns {
		public static final String TABLE_NAME = "exercise";
		
		public static final String COLUMN_NAME_LESSON_ID = "lessonId";
		public static final String COLUMN_NAME_EXERCISE = "exercise";
		public static final String COLUMN_NAME_EXERCISE_ORDER = "exerciseOrder";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_LESSON_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_EXERCISE + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_EXERCISE_ORDER + TYPE_INTEGER + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_LESSON_ID + TYPE_REFERENCES + Lesson.TABLE_NAME + "(" + Lesson._ID + ")" +
                        " )";
	}

	public static final class User implements BaseColumns {
		public static final String TABLE_NAME = "user";
		
		public static final String COLUMN_NAME_USERNAME = "username";
		public static final String COLUMN_NAME_PASSWORD = "password";
		public static final String COLUMN_NAME_EMAIL = "email";
		public static final String COLUMN_NAME_BIRTHDAY = "birthday";
		public static final String COLUMN_NAME_REGISTRATION_DATE = "registrationDate";
		public static final String COLUMN_NAME_REGISTRATION_LATITUDE = "registrationLatitude";
		public static final String COLUMN_NAME_REGISTRATION_LONGITUDE = "registrationLongitude";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_USERNAME + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_PASSWORD + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_EMAIL + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_BIRTHDAY + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_REGISTRATION_DATE + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_REGISTRATION_LATITUDE + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_REGISTRATION_LONGITUDE + TYPE_INTEGER +
                        " )";
		
	}
	
	
	public static final class ExerciseScore implements BaseColumns {
		public static final String TABLE_NAME = "exerciseScore";
		
		public static final String COLUMN_NAME_USER_ID = "userId";
		public static final String COLUMN_NAME_EXERCISE_ID = "exerciseId";
		public static final String COLUMN_NAME_SCORE = "score";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_USER_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_EXERCISE_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_SCORE + TYPE_INTEGER + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_USER_ID + TYPE_REFERENCES + User.TABLE_NAME + "(" + User._ID + ")" + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_EXERCISE_ID + TYPE_REFERENCES + Exercise.TABLE_NAME + "(" + Exercise._ID + ")" +
                        " )";
	}
	
	public static final class LessonScore implements BaseColumns {
		public static final String TABLE_NAME = "lessonScore";
		
		public static final String COLUMN_NAME_USER_ID = "userId";
		public static final String COLUMN_NAME_LESSON_ID = "lessonId";
		public static final String COLUMN_NAME_SCORE = "score";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_USER_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_LESSON_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_SCORE + TYPE_INTEGER + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_USER_ID + TYPE_REFERENCES + User.TABLE_NAME + "(" + User._ID + ")" + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_LESSON_ID + TYPE_REFERENCES + Lesson.TABLE_NAME + "(" + Lesson._ID + ")" +
                        " )";
	}
	
	public static final class OverallScore implements BaseColumns {
		public static final String TABLE_NAME = "overallScore";
		
		public static final String COLUMN_NAME_USER_ID = "userId";
		public static final String COLUMN_NAME_SCORE = "score";
		
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + TYPE_INTEGER + TYPE_PRIMARY_KEY + TYPE_AUTOINCREMENT + COMMA_SEP +
                        COLUMN_NAME_USER_ID + TYPE_INTEGER + COMMA_SEP +
                        COLUMN_NAME_SCORE + TYPE_INTEGER + COMMA_SEP +
                        TYPE_FOREIGN_KEY + COLUMN_NAME_USER_ID + TYPE_REFERENCES + User.TABLE_NAME + "(" + User._ID + ")" +
                        " )";
	}
	
}
