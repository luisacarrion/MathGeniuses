package com.example.mathgeniuses.util;

import android.content.Context;
import android.widget.Toast;
/**
 * This is a helper which shows the dialogs from the app to the user such as Toasts  and 
 * Dialog inputs.
 * 
 */
public class DialogsHelper
{
	private Context context;

	public DialogsHelper(Context _context)
	{
		context = _context;
	}
/**
 * This method shows the Toast with the passed message
 * @param message A string of message to be shown on a toast
 */
	public void showToast(String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	
	
	
}
