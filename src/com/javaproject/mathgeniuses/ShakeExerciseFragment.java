package com.javaproject.mathgeniuses;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ShakeExerciseFragment extends AbstractExerciseFragment implements SensorEventListener {
	
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
	
	private int mCounter = 0;
	
	private ArrayList<Integer> mAnswerImageRefs;
	
	private ImageAdapter mAnswerImageAdapter;

	public ShakeExerciseFragment() {
		// Required empty public constructor
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        if (args != null) {
        	mExercise = args.getString(AbstractExerciseFragment.KEY_EXERCISE);
        	mScoreAwarded = args.getInt(AbstractExerciseFragment.KEY_SCORE_AWARDED);
        	mScoreObtained = args.getInt(AbstractExerciseFragment.KEY_SCORE_OBTAINED);
        	mAnswer = args.getInt(AbstractExerciseFragment.KEY_ANSWER);
        }
        
        initializeShakeSensors();
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shake_exercise, container, false);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setActivityCallback(activity);
    }
	
	@Override
	public void onStart() {
		super.onStart();
		
		mTvTimer = (TextView) getActivity().findViewById(R.id.tvTimer);
		mTvExercise = (TextView) getActivity().findViewById(R.id.tvExercise);
		mTvAnswer = (TextView) getActivity().findViewById(R.id.tvAnswer);
		mRatingBar = (RatingBar) getActivity().findViewById(R.id.ratingBar);
		mAnswerGrid = (GridView) getActivity().findViewById(R.id.answerGrid);
		
		mVerify = (Button)getActivity().findViewById(R.id.btnVerify);
		mVerify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				onVerify();
			}
			
		});
		
		ratingStarsNumber = getActivity().getResources().getInteger(R.integer.rating_num_stars);
		mTvExercise.setText(mExercise + " =");
		mTvAnswer.setText("0");
		mRatingBar.setRating(mScoreObtained / mScoreAwarded * ratingStarsNumber);
		// The score will be recalculated at the end of the activity, so it should be 0 at the start (in case the user doesn't give the correct answer)
		mScoreObtained = 0;
		
		mAnswerImageRefs = new ArrayList<Integer>();
		mAnswerImageAdapter = new ImageAdapter(getActivity(), mAnswerImageRefs);
		mAnswerGrid.setAdapter(mAnswerImageAdapter);
		
		setTimer(mTvTimer);
		startTimer();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopTimer();
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	public int calculateScore() {
		int answer = Integer.parseInt( mTvAnswer.getText().toString() );
		if (answer == mAnswer) {
			mScoreObtained = mScoreAwarded;
		}
		return mScoreObtained;
	}
	
	public void dropObject() {
		mAnswerImageRefs.add(R.drawable.waterdrop);
		mAnswerImageAdapter.notifyDataSetChanged();
		mCounter++;
		mTvAnswer.setText(String.valueOf(mCounter));
	}
	
	public void initializeShakeSensors() {
        mSensorManager = (SensorManager)getActivity().getSystemService(Activity.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public static final float SHAKE_THRESHOLD = 1100;
	public static final float MINIMUM_ELAPSED_TIME_TO_RECEIVE_SENSOR_DATA_MILISECONDS = 100;
	public static final float MINIMUM_ELAPSED_TIME_TO_ADD_NEW_OBJECT_MILISECONDS = 400;
	
	long lastUpdate;
	long lastObjectAdded;
	float last_x = 0;
	float last_y = 0;
	float last_z = 0;
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		float[] values = sensorEvent.values;
		
		    long curTime = System.currentTimeMillis();
		    // only allow one update every 100ms.
		    if ((curTime - lastUpdate) > MINIMUM_ELAPSED_TIME_TO_RECEIVE_SENSOR_DATA_MILISECONDS) {
		    	
		    	
		    	
		      long diffTime = (curTime - lastUpdate);
		      lastUpdate = curTime;
		      
		      long diffTimeObjectAdded = (curTime - lastObjectAdded);
		      

		      float x = values[SensorManager.DATA_X];
		      float y = values[SensorManager.DATA_Y];
		      float z = values[SensorManager.DATA_Z];

		      float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
		      Log.d("sensor", "shake 1: " + speed);
		      
		      if (speed > SHAKE_THRESHOLD && diffTimeObjectAdded >= MINIMUM_ELAPSED_TIME_TO_ADD_NEW_OBJECT_MILISECONDS) {
		        Log.d("sensor", "shake detected w/ speed: " + speed);
		        lastObjectAdded = curTime;
		        dropObject();
		      }
		      last_x = x;
		      last_y = y;
		      last_z = z;
		    }
		  
		
	}
	
	
	private class ImageAdapter extends BaseAdapter
	{
		private Context mContext;
		private ArrayList<Integer> mRefs;
		
		public ImageAdapter(Context context, ArrayList<Integer> refs) {
			mContext = context;
			mRefs = refs;
			
		}

		@Override
		public int getCount()
		{
			return mRefs.size();
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int id)
		{
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;
			if (convertView == null)
			{
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setPadding(8, 8, 8, 8);

			} else
			{
				imageView = (ImageView) convertView;
			}
			
			imageView.setImageResource(mRefs.get(position));
			
			
			return imageView;
		}

	}
	
}
