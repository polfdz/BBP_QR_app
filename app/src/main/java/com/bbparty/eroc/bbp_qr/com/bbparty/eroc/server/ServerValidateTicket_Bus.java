package com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bbparty.eroc.bbp_qr.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class ServerValidateTicket_Bus extends AsyncTask<String, String, JSONObject> {
    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;
    private Context context;
    Constants constants;
    JSONObject result;
    String ticket_id;
    String bus_type;
    public ServerValidateTicket_Bus(){
        constants = new Constants();
        result = new JSONObject();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... userInfo) {
        while(!isCancelled()) {
            try {
                ticket_id = userInfo[0];
                bus_type = userInfo[1];
                validateTicket_Bus(ticket_id, bus_type);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
            return result;
        }
        this.onCancelled();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
    }

    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);
    }

    public JSONObject validateTicket_Bus(String _ticket_id, String _bus_type) {
        AsyncHttpClient client = new SyncHttpClient();
        RequestParams rp = new RequestParams();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Authentication",constants.AUTH_KEY);
        param.put("Function", "validateTicket_Bus");
        param.put("qr", _ticket_id);
        param.put("bus_type", _bus_type);
        RequestParams params = new RequestParams(param);

        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                    result = jObject;
            }
            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e) {
                try {
                    result.put("result", 503);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        });
        return result;
    }
    @Override
    protected void onCancelled(){
        super.onCancelled();
    }
}
