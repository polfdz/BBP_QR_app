package com.bbparty.eroc.bbp_qr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerGetTicketInfo;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerValidateTicket_Entrance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pol on 11/10/2016.
 */
public class Info extends AppCompatActivity implements View.OnClickListener {

    Context context;
    String ticket_id, scanner_option;
    TextView tTicketId, tTicketType, tUserName, tHora;
    ServerGetTicketInfo serverGetTicketInfo;
    SharedPreferencesHelper preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.layout_info);
        context = getApplicationContext();
        preferences = new SharedPreferencesHelper();

        tTicketId = (TextView) findViewById(R.id.info_ticketId);
        tTicketType = (TextView) findViewById(R.id.info_ticketType);
        tUserName = (TextView) findViewById(R.id.info_userName);
        tHora = (TextView) findViewById(R.id.info_hora);

        Bundle extras = getIntent().getExtras();
        scanner_option = preferences.getString(context,"scanner_option");
        if (extras != null && scanner_option != null) {
            ticket_id = extras.getString("ticket_id");
        }

        //get info
        getTicketInfo(ticket_id);
    }

    public void getTicketInfo(String _ticket_id){
        serverGetTicketInfo = new ServerGetTicketInfo();
        try {
            JSONObject result = serverGetTicketInfo.execute(_ticket_id).get();
            int conn = result.getInt("MessageCode");
            if(conn == 200){
                JSONObject jsonObject= result.getJSONObject("Data");
                String id = jsonObject.getString("id");
                String nom = jsonObject.getString("nom");
                String cognoms = jsonObject.getString("cognoms");
                String hora = jsonObject.getString("hora_entrada");
                String tipus = jsonObject.getString("entrada_tipus");

                tTicketId.setText("["+id+"] "+ticket_id);

                if(tipus.equalsIgnoreCase("0")){
                    tTicketType.setText(R.string.entrada1);
                }else if(tipus.equalsIgnoreCase("1")){
                    tTicketType.setText(R.string.entrada2);
                }else if(tipus.equalsIgnoreCase("2")){
                    tTicketType.setText(R.string.entrada3);
                }

                tUserName.setText(nom + " "+cognoms);
                tHora.setText(hora);

            }else if(conn == 504){
                popUpToast("Entrada no valida per BBP");
                tTicketId.setText(ticket_id);
                tTicketType.setText("--");
                tUserName.setText("--");
                tHora.setText("--");
            }else{
                popUpToast("Error servidor 1");
                goBack();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            popUpToast("Error servidor 2");
            goBack();
        } catch (ExecutionException e) {
            e.printStackTrace();
            popUpToast("Error servidor 3");
            goBack();
        } catch (JSONException e) {
            e.printStackTrace();
            popUpToast("Error servidor 4");
            goBack();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBack:
                goBack();
                break;
        }
    }


    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void goBack(){
        Intent intent = new Intent(this, TicketId.class);
        intent.putExtra("ticket_id", ticket_id);
        //intent.putExtra("scanner_option", scanner_option);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
