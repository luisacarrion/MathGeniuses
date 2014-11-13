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
			
			dialogs.showToast("The addition "+message);  
		} else if (label.equals("-"))
		{
			// show subtraction lessons
			
			dialogs.showToast("The subtraction "+message); 
		} else if (label.equals("x"))
		{
			// show multiplication lessons
			
			dialogs.showToast("The multiplication "+message); 
		} else if (label.equals("/"))
		{
			// show division lessons
			
			dialogs.showToast("The division "+message); 
		} else
		{
			// show lessons with mixed operations
			
			dialogs.showToast("The mixed "+message); 
		}

	}

}
