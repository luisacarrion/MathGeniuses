package com.javaproject.mathgeniuses;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DragAndDropExerciseFragment extends AbstractExerciseFragment {
	
	public static final String OPTION_IMAGE = "optionImage";
	public static final String ANSWER_IMAGE = "answerImage";
	
	private int mCounter = 0;
	
	private ArrayList<Integer> mOptionImageRefs;
	private ArrayList<Integer> mAnswerImageRefs;
	
	private ImageAdapter mOptionImageAdapter;
	private ImageAdapter mAnswerImageAdapter;
	
	private GridView mOptionsGridView;
	
	public DragAndDropExerciseFragment() {
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
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drag_and_drop_exercise, container, false);
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
		mAnswerGrid = (GridView) getActivity().findViewById(R.id.answerGrid);
		mTvAnswer = (TextView) getActivity().findViewById(R.id.tvAnswer);
		mOptionsGridView = (GridView) getActivity().findViewById(R.id.optionsGrid);

		mVerify = (Button)getActivity().findViewById(R.id.btnVerify);
		mVerify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				onVerify();
			}
			
		});

		mRatingBar = (RatingBar) getActivity().findViewById(R.id.ratingBar);
		
		ratingStarsNumber = getActivity().getResources().getInteger(R.integer.rating_num_stars);
		mTvExercise.setText(mExercise + " =");
		mTvAnswer.setText("0");
		mRatingBar.setRating(mScoreObtained / mScoreAwarded * ratingStarsNumber);
		// The score will be recalculated at the end of the activity, so it should be 0 at the start (in case the user doesn't give the correct answer)
		mScoreObtained = 0;
		
		mOptionImageRefs = new ArrayList<Integer>();
		mAnswerImageRefs = new ArrayList<Integer>();
		populateOptionImageReferences(mScoreAwarded, R.drawable.finger);
		populateAnswerImageReferences(0, R.drawable.finger);

		mOptionImageAdapter = new ImageAdapter(getActivity(), mOptionImageRefs, OPTION_IMAGE);
		mAnswerImageAdapter = new ImageAdapter(getActivity(), mAnswerImageRefs, ANSWER_IMAGE);

		mOptionsGridView.setAdapter(mOptionImageAdapter);
		mAnswerGrid.setAdapter(mAnswerImageAdapter);

		setTimer(mTvTimer);
		startTimer();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopTimer();
	}
	
	@Override
	public int calculateScore() {
		int answer = Integer.parseInt( mTvAnswer.getText().toString() );
		if (answer == mAnswer) {
			mScoreObtained = mScoreAwarded;
		}
		return mScoreObtained;
	}
	
	private void populateOptionImageReferences(int n, int drawable)
	{
		for (int i = 0; i < n; i++)
		{
			mOptionImageRefs.add(drawable);
		}
	}

	private void populateAnswerImageReferences(int n, int drawable)
	{
		for (int i = 0; i < n; i++)
		{
			mAnswerImageRefs.add(drawable);
		}
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
				imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setPadding(2, 2, 2, 2);

			} else
			{
				imageView = (ImageView) convertView;
			}
			
			imageView.setImageResource(mRefs.get(position));
			
			if (mType.equals(OPTION_IMAGE))
			{
				imageView.setOnTouchListener(new TouchListener());
				imageView.setOnDragListener(new OnDragListener()
				{
					@Override
					public boolean onDrag(View v, DragEvent event)
					{
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
							if (v.getId() != R.id.optionsGrid)
							{
								Log.i("MGN", "Current id: " + v.getId()
										+ " grid: " + R.id.optionsGrid);
								mAnswerImageRefs.add(R.drawable.finger);
								// mGridView.refreshDrawableState();
								mAnswerImageAdapter.notifyDataSetChanged();
								mCounter++;
								// Just remove one, it doesn't matter that it's the first one
								mOptionImageRefs.remove(0);
								mOptionImageAdapter.notifyDataSetChanged();
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
			ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

			String[] mimeType =
			{ ClipDescription.MIMETYPE_TEXT_PLAIN };
			ClipData dragData = new ClipData((CharSequence) view.getTag(),
					mimeType, item);

			//Toast.makeText(getActivity(), "In long click",
					//Toast.LENGTH_LONG).show();

			DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
			view.startDrag(dragData, shadowBuilder, view, 0);

			return true;
		}
	}
	
}
