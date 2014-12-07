package com.javaproject.mathgeniuses;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javaproject.mathgeniuses.database.MathGeniusesDbAdapter;
import com.javaproject.mathgeniuses.entities.ExerciseObject;

public class DragAndDropActivity extends Activity
{
	private GridView mGridView, mAnswerGrid;
	private ArrayList<Integer> mImageRefs;
	private ArrayList<Integer> mAnswerImageRefs;
	private ImageAdapter mImageAdapter;
	private ImageAdapter mAnswerImageAdapter;
	private TextView mTvAnswer;
	private long mLessonId;
	private int mCounter = 0;
	private TextView mTvTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingers_grid_layout);
		Intent intent = getIntent();
		mLessonId = intent.getExtras().getLong("lessonId");
		Log.i("MGN", "LessonID: " + mLessonId);
		mImageRefs = new ArrayList<Integer>();
		mAnswerImageRefs = new ArrayList<Integer>();
		mTvAnswer = (TextView) findViewById(R.id.tvAnswer);
		mTvTimer = (TextView) findViewById(R.id.tvTimer);

		populate(10, R.drawable.finger);
		populateResponseGrid(0, R.drawable.finger);

		mGridView = (GridView) findViewById(R.id.gridview);
		mAnswerGrid = (GridView) findViewById(R.id.answerGrid);

		mImageAdapter = new ImageAdapter(this, mImageRefs, "set");
		mAnswerImageAdapter = new ImageAdapter(this, mAnswerImageRefs, "");

		mGridView.setAdapter(mImageAdapter);
		mAnswerGrid.setAdapter(mAnswerImageAdapter);

		getExercises();
		startTimer();
	}

	private class ImageAdapter extends BaseAdapter
	{
		private Context mContext;
		private ArrayList<Integer> mRefs;
		private String mType;// answer/set

		public ImageAdapter(Context context, ArrayList<Integer> refs,
				String type)
		{
			mContext = context;
			mRefs = refs;
			mType = type;

		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return mRefs.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int id)
		{
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			ImageView imageView;
			if (convertView == null)
			{
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);

			} else
			{
				imageView = (ImageView) convertView;
			}
			imageView.setImageResource(mRefs.get(position));
			if (mType.equals("set"))
			{
				imageView.setOnTouchListener(new TouchListener());
				imageView.setOnDragListener(new OnDragListener()
				{

					@Override
					public boolean onDrag(View v, DragEvent event)
					{
						// TODO Auto-generated method stub
						switch (event.getAction())
						{
						case DragEvent.ACTION_DRAG_STARTED:

							Log.i("MGN",
									"Action is DragEvent.ACTION_DRAG_STARTED");
							// Do nothing
							break;
						case DragEvent.ACTION_DRAG_ENTERED:
							Log.d("MGN",
									"Action is DragEvent.ACTION_DRAG_ENTERED");
							break;
						case DragEvent.ACTION_DRAG_EXITED:
							Log.d("MGN",
									"Action is DragEvent.ACTION_DRAG_EXITED");
							if (v.getId() != R.id.gridview)
							{
								Log.i("MGN", "Current id: " + v.getId()
										+ " grid: " + R.id.gridview);
								mAnswerImageRefs.add(R.drawable.finger);
								// mGridView.refreshDrawableState();
								mAnswerImageAdapter.notifyDataSetChanged();
								mCounter++;
								mTvAnswer.setText(String.valueOf(mCounter));

							}
							break;
						case DragEvent.ACTION_DRAG_LOCATION:
							Log.d("MGN",
									"Action is DragEvent.ACTION_DRAG_LOCATION");
							break;
						case DragEvent.ACTION_DRAG_ENDED:
							Log.d("MGN",
									"Action is DragEvent.ACTION_DRAG_ENDED");
							// Do nothing

							break;
						case DragEvent.ACTION_DROP:
							Log.d("MGN", "ACTION_DROP event");
							// Do nothing
							Log.i("MGEN",
									"Dragged and Dropped in the answer view!");
							break;
						default:
							break;
						}
						return true;
					}

				});

			}
			return imageView;
		}

	}

	private class TouchListener implements View.OnTouchListener
	{

		@Override
		public boolean onTouch(View view, MotionEvent event)
		{
			// TODO Auto-generated method stub
			ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

			String[] mimeType =
			{ ClipDescription.MIMETYPE_TEXT_PLAIN };
			ClipData dragData = new ClipData((CharSequence) view.getTag(),
					mimeType, item);

			Toast.makeText(getApplicationContext(), "In long click",
					Toast.LENGTH_LONG).show();

			DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
			view.startDrag(dragData, shadowBuilder, view, 0);

			return true;
		}
	}

	private void populate(int n, int drawable)
	{
		for (int i = 0; i < n; i++)
		{
			mImageRefs.add(drawable);
		}
	}

	private void populateResponseGrid(int n, int drawable)
	{
		for (int i = 0; i < n; i++)
		{
			mAnswerImageRefs.add(drawable);
		}
	}

	private void getExercises()
	{
		List<ExerciseObject> exercises = new ArrayList<ExerciseObject>();
		MathGeniusesDbAdapter dbAdapter = new MathGeniusesDbAdapter(this);
		dbAdapter.open();
		exercises = dbAdapter.fetchExercises(mLessonId);
		for (int i = 0; i < exercises.size(); i++)
		{
			Log.i("MGN", "Lesson " + mLessonId + " Exercise " + i + 1 + ": "
					+ exercises.get(i).getExercise());

		}

	}

	private void startTimer()
	{
		new CountDownTimer(180000, 1000)
		{

			public void onTick(long millisUntilFinished)
			{
				mTvTimer.setText((millisUntilFinished / 60000) + ":"
						+ (millisUntilFinished % 60000 / 1000));
			}

			public void onFinish()
			{
				mTvTimer.setText("00:00");
			}
		}.start();
	}
}
