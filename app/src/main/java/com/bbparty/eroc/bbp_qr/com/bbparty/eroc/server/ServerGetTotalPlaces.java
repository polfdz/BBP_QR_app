package com.bbparty.eroc.bbp_qr.com.bbparty.eroc.server;

import android.os.AsyncTask;
import android.util.Log;

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
public class ServerGetTotalPlaces extends AsyncTask<String, String, JSONObject> {
    Constants constants;
    JSONObject result;
    String ticket_id;
    String bus_type;
    public ServerGetTotalPlaces(){
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
                getTotalPlaces();
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

    public JSONObject getTotalPlaces() {
        AsyncHttpClient client = new SyncHttpClient();
        RequestParams rp = new RequestParams();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Authentication",constants.AUTH_KEY);
        param.put("Function", "getTotalPlaces");
        RequestParams params = new RequestParams(param);

        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                    result = jObject;
            }
            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e) {
                try {
                    result.put("MessageCode", 503);
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
