package com.example.twmoore.drunktest;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bpho on 5/2/17.
 */

public class RestrictActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient c = null;
    private Context context = RestrictActivity.this;
    private boolean addressRequested;
    protected Location lastLocation;
    public AddressResultReceiver resultReceiver = new AddressResultReceiver(null);
    private Handler mHandler;
    private String addressOutput;
    private boolean openUberLyft = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restrict_activity);
        mHandler = new Handler();

        if (c == null) {
            c = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }

        Button uberButton = (Button) findViewById(R.id.uber);
        Button lyftButton = (Button) findViewById(R.id.lyft);

        uberButton.setBackgroundResource(R.drawable.uber);
        lyftButton.setBackgroundResource(R.drawable.lyft);

        // Dynamically generate recent call log
        TextView callLog = (TextView) findViewById(R.id.callLog);
        callLog.setMovementMethod(new ScrollingMovementMethod());
        callLog.setText(getCallDetails());
        Linkify.addLinks(callLog, Patterns.PHONE, "tel: ");

        // Hide navigation bar
        hideNavigation();

        // Start timer to retake test
        retakeCountdown();
    }

    public void retakeCountdown() {
        // Display retake test button after certain time
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button retakeBtn = (Button) findViewById(R.id.retakeTest);
                        retakeBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 10000);  // Set at display after 10 seconds


        new CountDownTimer(10000, 1000) {
            TextView t1 = (TextView) findViewById(R.id.timerText1);
            TextView t2 = (TextView) findViewById(R.id.timerText2);

            public void onTick(long msUntilFinished) {
                t2.setText(msUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                t1.setText("");
                t2.setText("Retake when Ready!");
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigation();
    }

    public void hideNavigation() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void retakeTest(View view) {
//        Intent i = new Intent(this, MainActivity.class);    // Change intent here to main screen of app
//        startActivity(i);
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Display the address string
            // or an error message sent from the intent service.
            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Log.v("UPDATE ", "onReceiveResult is called");
            Log.v("Add Output ", addressOutput);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    displayAddressOutput();
                }
            });
        }
    }

    private String getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Set recentNums = new HashSet();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALL_LOG)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        }
        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String regx = "+";      // Remove the '+' before a phone number
            char[] ca = regx.toCharArray();
            for (char c : ca) {
                phNumber = phNumber.replace("" + c, "");
            }
            String contactName = getContactName(phNumber);
            if (contactName != null) {
                if (recentNums.contains(phNumber)) {

                } else {
                    sb.append("\n " + contactName + " : " + phNumber + "    ");
                    sb.append("\n--------------------------------------------------------------------");
                    recentNums.add(phNumber);
                }
            }

        }
        managedCursor.close();
        return sb.toString();
    }


    public String getContactName(String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }


    public void emergencyCall(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:7049418063"));    // Change to 911 in deployment
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
        startActivity(callIntent);
    }


    public void openUber(View view) {
        openUberLyft = true;
        Intent i = new Intent(Intent.ACTION_MAIN);
        PackageManager managerclock = getPackageManager();
        i = managerclock.getLaunchIntentForPackage("com.ubercab");
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);
    }

    public void openLyft(View view) {
        openUberLyft = true;
        Intent i = new Intent(Intent.ACTION_MAIN);
        PackageManager managerclock = getPackageManager();
        i = managerclock.getLaunchIntentForPackage("me.lyft.android");
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);
    }

    // Prevents you from going back (Set a timer for this function, could nest functions)
    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(context, "You're not allowed to exit this app!", Toast.LENGTH_SHORT);
        toast.show();
    }


    // Prevents you from using the recent apps button (Set a timer for this function, could nest function)
    @Override
    protected void onPause() {
        if (openUberLyft == false) {
            super.onPause();

            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.moveTaskToFront(getTaskId(), 0);
            Toast toast = Toast.makeText(context, "You're not allowed to exit this app!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            super.onPause();
            openUberLyft = false;
        }
    }


    /**
     * Location Services
     */

    private void displayAddressOutput() {
        Log.v("LOG ", "displaying output address");
        System.out.println(addressOutput);
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(addressOutput);

    }

    public void getLocation(View view) {
        Log.v("LOG ", "Butotn pressed");
        if (c.isConnected() && lastLocation != null) {
            Log.v("LOG ", "Button pressed, intent started");
            startIntentService();
        }
        addressRequested = true;
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastLocation);
        Log.v("LOG ", "starting intent service");
        startService(intent);
    }

    @Override
    protected void onStart() {
        c.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        c.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("LOG: ", "NEED PERMISSION, RETURNED");
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(c);

        if (lastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Log.v("ERROR: ", "no geocoder available");
                return;
            } else {
                Log.v("LOG: ", "Geocoder is present");
            }

            if (addressRequested) {
                Log.v("LOG ", "addressRequested Check, intent started");
                startIntentService();
            } else {
                Log.v("LOG: ", "addressRequested is false");
            }

        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
