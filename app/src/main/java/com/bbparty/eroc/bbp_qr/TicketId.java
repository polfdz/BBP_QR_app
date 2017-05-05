package com.bbparty.eroc.bbp_qr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.CheckInternetConnection;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.SendMail;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerDeleteTicket;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerGetBusPlaces;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerGetTotalPlaces;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerValidateTicket_Bus;
import com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server.ServerValidateTicket_Entrance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Pol on 10/10/2016.
 */
public class TicketId extends AppCompatActivity implements View.OnClickListener {
    Context context;
    TextView tTicketId, tSpace;
    Button bBack, bInfo;
    ImageView iImage, iMode;
    String ticket_id;
    String scanner_option; //entrance , autocar
    ServerValidateTicket_Entrance serverValidateTicket_Entrance;
    ServerValidateTicket_Bus serverValidateTicket_Bus;
    ServerGetBusPlaces serverGetBusPlaces;
    ServerGetTotalPlaces serverGetTotalPlaces;
    ServerDeleteTicket serverDeleteTicket;
    SharedPreferencesHelper preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.layout_warning);
        context = getApplicationContext();
        preferences = new SharedPreferencesHelper();

        tTicketId = (TextView) findViewById(R.id.tTicketId);
        bBack = (Button) findViewById(R.id.bBack);
        bBack.setOnClickListener(this);
        bInfo = (Button) findViewById(R.id.bInfo);
        bInfo.setOnClickListener(this);
        iImage = (ImageView) findViewById(R.id.image);
        iMode = (ImageView) findViewById(R.id.iMode);
        tSpace = (TextView) findViewById(R.id.textSpace);

        Bundle extras = getIntent().getExtras();
        scanner_option = preferences.getString(context,"scanner_option");

        if (extras != null && scanner_option != null) {
            ticket_id = extras.getString("ticket_id");
            tTicketId.setText(ticket_id);
            Log.d("Escaner", scanner_option);
            Log.d("Escaner", ticket_id);
            if(scanner_option.equalsIgnoreCase("entrance")){ //ticket entrada
                tSpace.setVisibility(View.VISIBLE);
                int check = checkTicket_Entrance(ticket_id);
                iMode.setImageResource(R.drawable.bb_party_black_big);
                if (check == 0) { //ticket valid
                    iImage.setBackgroundResource(R.drawable.accepted);
                    tTicketId.setText(R.string.accepted);
                }else if(check == 1) { //ticket already in
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.ticket_in);
                }else if(check == 2){ //error
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.not_accepted);
                }else if (check == 3){
                    iImage.setBackgroundResource(R.drawable.failed);
                    tTicketId.setText(R.string.internet_error);
                }
                checkTotalSpace();
            }else if(scanner_option.equalsIgnoreCase("autocar1")){ //ticket autocar 00:30
                /*
                * 200 --> 1 ticket valid for bus
                * 504 ticket doesn't exists
                * 505 ticket not valid for bus
                * 506 ticket for wrong bus
                * 507 ticket already in for this bus
                * 503 error
                 */
                tSpace.setVisibility(View.VISIBLE);
                int check = checkTicket_Autocar(ticket_id, "1");
                iMode.setImageResource(R.drawable.autocar);
                if(check == 200){ //bus 1
                    iImage.setBackgroundResource(R.drawable.bus1);
                    tTicketId.setText(R.string.bus1_accepted);
                }else if(check == 504){ //ticket doesn't exist
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.not_accepted);
                }else if (check == 505) { //ticket not for bus
                    iImage.setBackgroundResource(R.drawable.bb_party_black_big);
                    tTicketId.setText(R.string.bus_rejected);
                }else if(check == 506){
                    iImage.setBackgroundResource(R.drawable.bus2);
                    tTicketId.setText(R.string.bus1_wrong_bus);
                }else if(check == 507){
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.ticket_in);
                }else{
                    iImage.setBackgroundResource(R.drawable.failed);
                    tTicketId.setText(R.string.internet_error_info);
                }
                checkBusSpace("1");
            }else if(scanner_option.equalsIgnoreCase("autocar2")){ //ticket autocar 01:30
                tSpace.setVisibility(View.VISIBLE);
                int check = checkTicket_Autocar(ticket_id, "2");
                iMode.setImageResource(R.drawable.autocar);
                if(check == 200){ //bus 2
                    iImage.setBackgroundResource(R.drawable.bus2);
                    tTicketId.setText(R.string.bus2_accepted);
                }else if(check == 504){ //ticket doesn't exist
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.not_accepted);
                }else if (check == 505) { //ticket not for bus
                    iImage.setBackgroundResource(R.drawable.bb_party_black_big);
                    tTicketId.setText(R.string.bus_rejected);
                }else if(check == 506){
                    iImage.setBackgroundResource(R.drawable.bus1);
                    tTicketId.setText(R.string.bus2_wrong_bus);
                }else if(check == 507){
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.ticket_in);
                }else{
                    iImage.setBackgroundResource(R.drawable.failed);
                    tTicketId.setText(R.string.internet_error_info);
                }
                checkBusSpace("2");
            }else if(scanner_option.equalsIgnoreCase("admin")){
                tSpace.setVisibility(View.INVISIBLE);
                String check = checkDeleteTicket(ticket_id);
                iMode.setImageResource(R.drawable.settings);
                if(check.equalsIgnoreCase("504")){
                    iImage.setBackgroundResource(R.drawable.rejected);
                    tTicketId.setText(R.string.nobbp);
                }else if(check.equalsIgnoreCase("503") || check.equalsIgnoreCase("0")){
                    iImage.setBackgroundResource(R.drawable.failed);
                    tTicketId.setText(R.string.contact);
                }else{
                    tTicketId.setText(R.string.accepted);
                    tTicketId.setText(R.string.deleted);
                    sendEmail(check);
                }
            }else{
                iImage.setBackgroundResource(R.drawable.ic_launcher);
                tTicketId.setText(R.string.contact);
            }
        }else{
            iImage.setBackgroundResource(R.drawable.ic_launcher);
            tTicketId.setText(R.string.contact);
        }
    }

    public int checkTicket_Entrance(String _ticket_id){
        //connexió amb servidor i retornar si el ticket ha sigut comprovat ja o no
        //COMPROVAR SI QR EXISTEIX I/O SI JA HA ENTRAT
        //modificar bd si no ha entrat --> a 1

        Log.d("Track_ticket", _ticket_id);
        serverValidateTicket_Entrance = new ServerValidateTicket_Entrance();
        try {
            JSONObject result = serverValidateTicket_Entrance.execute(_ticket_id).get();
            int conn = result.getInt("MessageCode");
            if(conn == 200){
                //ticket vàlid
                return 0;
            }else if(conn == 505){
                //ticket already IN
                return 1;
            }else if(conn == 504){
                //ticket not vàlid
                return 2;
            }else{ //internet error
                return 3;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 3;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return 3;
        } catch (JSONException e) {
            e.printStackTrace();
            return 3;
        }finally {
            serverValidateTicket_Entrance.cancel(true);
        }
    }

    public int checkTicket_Autocar(String _ticket_id, String bus_type){
        //connexió amb servidor i retornar si el ticket es d'autocar o no

        serverValidateTicket_Bus = new ServerValidateTicket_Bus();
        try {
            JSONObject result = serverValidateTicket_Bus.execute(_ticket_id, bus_type).get();
            int conn = result.getInt("MessageCode");

            /*int type = 0;
            if(result.has("bus")){
                type = result.getInt("bus");
            }*/
            switch(conn){
                case 200:
                    return 200;
                case 504:
                    return 504;
                case 505:
                    return 505;
                case 506:
                    return 506;
                case 507:
                    return 507;
                case 503:
                    return 503;
                default:
                    return 503;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 503;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return 503;
        } catch (JSONException e) {
            e.printStackTrace();
            return 503;
        }finally {
            serverValidateTicket_Bus.cancel(true);
        }
    }

    public void checkBusSpace(String type){
        serverGetBusPlaces = new ServerGetBusPlaces();
        JSONObject result = null;
        try {
            result = serverGetBusPlaces.execute(type).get();
            int conn = result.getInt("MessageCode");
            int total = result.getInt("total");
            int places = result.getInt("places");
            Log.d("BUSSPACE", result.toString());
            tSpace.setText(places + "/" + total);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkTotalSpace(){
        serverGetTotalPlaces = new ServerGetTotalPlaces();
        JSONObject result = null;
        try {
            result = serverGetTotalPlaces.execute().get();
            int conn = result.getInt("MessageCode");
            int total = result.getInt("total");
            int dins = result.getInt("dins");
            tSpace.setText(dins + "/" + total);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String checkDeleteTicket(String _ticket_id){
        //connexió amb servidor i retornar si el ticket ha sigut comprovat ja o no
        //COMPROVAR SI QR EXISTEIX I/O SI JA HA ENTRAT
        //modificar bd si no ha entrat --> a 1

        Log.d("Track_ticket", _ticket_id);
        serverDeleteTicket = new ServerDeleteTicket();
        try {
            JSONObject result = serverDeleteTicket.execute(_ticket_id).get();
            int conn = result.getInt("MessageCode");
            String email ="";
            if(result.has("Email")){
                email = result.getString("Email");
            }
            if(conn == 200){
                return email;
            }else{
                return ""+conn;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "0";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "0";
        } catch (JSONException e) {
            e.printStackTrace();
            return "0";
        }finally {
            serverDeleteTicket.cancel(true);
        }
    }
    public void sendEmail(String email){

        //Creating SendMail object
        String subject = getResources().getString(R.string.mailSubject);
        String message = getResources().getString(R.string.mailBody);

        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bBack:
                goBack();
                finish();
                break;
            case R.id.bInfo:
                //check internet
                if(CheckInternetConnection.isNetworkAvailable(this)){
                    if (ticket_id != null) {
                        Intent intentInfo = new Intent(this, Info.class);
                        intentInfo.putExtra("ticket_id", ticket_id);
                        //intentInfo.putExtra("scanner_option", scanner_option);
                        startActivity(intentInfo);
                    }else{
                        //ERROR COMPROVAR VALIDESA DEL QR
                        popUpToast(getResources().getString(R.string.escaner_error));
                    }
                }else{
                    popUpToast(getResources().getString(R.string.internet_error_info));
                }
                break;

        }
    }
    private void popUpToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
    public void goBack(){
        Intent intent = new Intent(this, QRScan.class);
        //intent.putExtra("scanner_option", scanner_option);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        goBack();
    }
}
