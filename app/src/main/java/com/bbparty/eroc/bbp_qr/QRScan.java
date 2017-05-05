package com.bbparty.eroc.bbp_qr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.CheckInternetConnection;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * By: Pol Fernández
 * following:
 * https://www.numetriclabz.com/android-qr-code-scanner-using-zxingscanner-library-tutorial/
 */
public class QRScan extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private String scanner_option;
    private Context context;
    SharedPreferencesHelper preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        context = getApplicationContext();
        preferences = new SharedPreferencesHelper();

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
        scanner_option = preferences.getString(context,"scanner_option");
        //Log.d("EXTRAS TAG", extras.getString(""+getIntent().toString()));

        if (scanner_option != null) {
            //popUpToast(scanner_option);
        }else{ //error d'entrada
            popUpToast(getResources().getString(R.string.not_scaner_warning));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        mScannerView.startCamera(); // Local method to handle camera init
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        //Check internet
        if(CheckInternetConnection.isNetworkAvailable(this)){
            mScannerView.stopCameraPreview();
            Intent intent = new Intent(this, TicketId.class);
            intent.putExtra("ticket_id", rawResult.getText());
            //intent.putExtra("scanner_option", scanner_option);
            startActivity(intent);
            finish();
        }else{// NO INTERNET
            mScannerView.stopCameraPreview();
            popUpToast(getResources().getString(R.string.internet_error));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
