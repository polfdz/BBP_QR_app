package com.bbparty.eroc.bbp_qr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bbparty.eroc.bbp_qr.arbreRefranyer.DatabaseModel;
import com.bbparty.eroc.bbp_qr.arbreRefranyer.ParseRefranys;
import com.bbparty.eroc.bbp_qr.arbreRefranyer.RefranyDBObject;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.CheckInternetConnection;

import java.io.IOException;
import java.util.List;


/**
 * By: Pol Fernández
 * following:
 * https://www.numetriclabz.com/android-qr-code-scanner-using-zxingscanner-library-tutorial/
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bScan, bBus1, bBus2, bEroc;
    Context context;
    final static int CAMERA_RESULT = 0;
    SharedPreferencesHelper preferences;
    private DatabaseModel db;
    private ParseRefranys parseRefranys;
    private List<RefranyDBObject> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        context = getApplicationContext();

        //arbre refranyer
        /*db = new DatabaseModel(context);
        parseRefranys = new ParseRefranys(context, db);
        try {
            parseRefranys.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        list = db.getAllRefranys();
        for(int i = 0; i< list.size()-1; i++){
            Log.i("DB",list.get(i).getPregunta());
        }*/


        PackageManager pm = context.getPackageManager();

        requestPermisions();
        setContentView(R.layout.activity_main);
        bScan = (Button) findViewById(R.id.bScan);
        bBus1 = (Button) findViewById(R.id.bBus1);
        bBus2 = (Button) findViewById(R.id.bBus2);
        bEroc = (Button) findViewById(R.id.bEroc);

        bScan.setOnClickListener(this);
        bBus1.setOnClickListener(this);
        bBus2.setOnClickListener(this);
        bEroc.setOnClickListener(this);

        preferences = new SharedPreferencesHelper();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bScan:
                Intent intent = new Intent(this, QRScan.class);
                preferences.putString(context,"scanner_option","entrance"); //Entrada
                popUpToast("entrance");
                startActivity(intent);
                finish();
                break;
            case R.id.bBus1:
                Intent intent2 = new Intent(this, QRScan.class);
                preferences.putString(context, "scanner_option", "autocar1"); //Autocar1
                popUpToast("Autocar 00:30");
                startActivity(intent2);
                finish();
                break;
            case R.id.bBus2:
                Intent intent3 = new Intent(this, QRScan.class);
                preferences.putString(context, "scanner_option", "autocar2"); //Autocar2
                popUpToast("Autocar 01:30");
                startActivity(intent3);
                finish();
                break;
            case R.id.bEroc:
                Intent intent4 = new Intent(this, Admin.class);
                preferences.putString(context, "scanner_option", "admin"); //admin
                startActivity(intent4);
                finish();

        }
    }

    public void requestPermisions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_RESULT);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    public void requestPermisions2(){
        popUpToast("Has d'acceptar els permisos de camera per seguir!");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_RESULT);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    requestPermisions2();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //android.os.Process.killProcess(android.os.Process.myPid());
        moveTaskToBack(true);

    }
}
