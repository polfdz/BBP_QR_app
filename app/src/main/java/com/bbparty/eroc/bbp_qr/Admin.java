package com.bbparty.eroc.bbp_qr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.Constants;

/**
 * By: Pol Fernández
 * following:
 * https://www.numetriclabz.com/android-qr-code-scanner-using-zxingscanner-library-tutorial/
 */
public class Admin extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    LinearLayout layout_login;
    EditText tPassword, tUser;
    Context context;
    Constants constants;
    SharedPreferencesHelper preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        context = getApplicationContext();

        constants = new Constants();
        setContentView(R.layout.layout_admin);
        layout_login = (LinearLayout) findViewById(R.id.layout_login);
        tPassword = (EditText) findViewById(R.id.password);
        tUser = (EditText) findViewById(R.id.user);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        preferences = new SharedPreferencesHelper();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                String user = tUser.getText().toString();
                String password = tPassword.getText().toString();
                if(user.equals(constants.ADMIN_USER) && password.equals(constants.ADMIN_PASSWORD)){
                    Intent intent = new Intent(this, QRScan.class);
                    preferences.putString(context,"scanner_option","admin"); //Entrada
                    popUpToast("admin");
                    startActivity(intent);
                    finish();
                    break;
                }else{
                    popUpToast("Invalid credentials");
                }
                break;
        }
    }

    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        goBack();

    }
}
