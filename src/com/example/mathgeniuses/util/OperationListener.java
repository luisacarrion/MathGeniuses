package com.example.mathgeniuses.util;

import com.example.mathgeniuses.database.MathGeniusesDbAdapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * This class is a listener of all the operations buttons.
 * It directs the user to the lessons according to the operation button
 * he/she pressed.
 *
 */
public class OperationListener implements OnClickListener
{
	private Context context;
	private String label;
	private DialogsHelper dialogs;
	private MathGeniusesDbAdapter mDb;

	public OperationListener(Context _context, String _label, MathGeniusesDbAdapter db)
	{

		label = _label;
		context = _context;
		dialogs=new DialogsHelper(context);
		mDb = db;
	}

	@Override
	public void onClick(View arg0)
	{
		String message ="lessons to come shortly!";
		if (label.equals("+"))
		{
			mDb.fetchLessons(1);
			mDb.fetchOperations();			
			dialogs.showToast("The addition "+message);  
		} else if (label.equals("-"))
		{
			mDb.fetchLessons(2);
			
			dialogs.showToast("The subtraction "+message); 
		} else if (label.equals("x"))
		{
			mDb.fetchLessons(3);
			
			dialogs.showToast("The multiplication "+message); 
		} else if (label.equals("/"))
		{
			mDb.fetchLessons(4);
			
			dialogs.showToast("The division "+message); 
		} else
		{
			mDb.fetchLessons();
			
			dialogs.showToast("The mixed "+message); 
		}

	}

}
