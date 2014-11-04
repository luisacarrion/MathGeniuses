package com.example.mathgeniuses;

import com.google.android.gms.plus.Plus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends PlusBaseActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        signIn();
         
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.plus_sign_out) {
        	signOut();
        } else if (id == R.id.plus_disconnect) {
        	revokeAccess();
        }
        
        return super.onOptionsItemSelected(item);
    }





	@Override
	protected void onPlusClientSignIn() {
		Log.d(TAG, "Entered onPlusClientSignIn");
		
        //try {
    	((TextView) findViewById(R.id.txtHelloWorld)).setText(Plus.AccountApi.getAccountName(getGoogleApiClient()));

	}

	@Override
	protected void onPlusClientBlockingUI(boolean show) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Entered onPlusClientBlockingUI");
	}
	
	@Override
	protected void updateConnectButtonState() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Entered updateConnectButtonState");
	}

	@Override
	protected void onPlusClientRevokeAccess() {
		returnToLogin();
	}
	
	@Override
	protected void onPlusClientSignOut() {
		returnToLogin();
	}
	
	private void returnToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}
}
