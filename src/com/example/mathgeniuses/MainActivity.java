package com.example.mathgeniuses;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.mathgeniuses.util.OperationListener;
import com.example.mathgeniuses.util.PlusBaseActivity;
import com.google.android.gms.plus.Plus;

public class MainActivity extends PlusBaseActivity
{

	private TextView mTxtWelcome;
	private String mUsername;
	private ArrayList<Button> operations;

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTxtWelcome = ((TextView) findViewById(R.id.txtWelcome));
		initialize();
		// add listeners to the buttons
		for (int i = 0; i < operations.size(); i++)
		{
			Button btnOp = operations.get(i);
			String label = btnOp.getText().toString();
			
			btnOp.setOnClickListener((OnClickListener) new OperationListener(
					getApplicationContext(), label));
		}
		signIn();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		} else if (id == R.id.plus_sign_out)
		{
			signOut();
		} else if (id == R.id.plus_disconnect)
		{
			revokeAccess();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPlusClientSignIn()
	{
		Log.d(TAG, "Entered onPlusClientSignIn");

		try
		{
			String givenName = Plus.PeopleApi
					.getCurrentPerson(getGoogleApiClient()).getName()
					.getGivenName();
			mUsername = givenName != null ? givenName : "";
			mTxtWelcome.setText(mUsername + ", " + getString(R.string.welcome));

			// adding listeners to the buttons

		} catch (Exception e)
		{
			Log.e(TAG, "Given name not found.\n" + e.getMessage());
		}

	}

	@Override
	protected void onPlusClientBlockingUI(boolean show)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "Entered onPlusClientBlockingUI");
	}

	@Override
	protected void updateConnectButtonState()
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "Entered updateConnectButtonState");
	}

	@Override
	protected void onPlusClientRevokeAccess()
	{
		returnToLogin();
	}

	@Override
	protected void onPlusClientSignOut()
	{
		returnToLogin();
	}

	private void returnToLogin()
	{
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * This method initializes all the operation buttons and store them in an
	 * arrayList of Button objects
	 */
	public void initialize()
	{
		operations = new ArrayList<Button>();

		operations.add((Button) findViewById(R.id.btnAdd));
		operations.add((Button) findViewById(R.id.btnSubtract));
		operations.add((Button) findViewById(R.id.btnMultiply));
		operations.add((Button) findViewById(R.id.btnDivide));
		operations.add((Button) findViewById(R.id.btnAll));
	}
}
